package mobile.br.com.reclameonibus;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mobile.br.com.reclameonibus.async.Connection;
import mobile.br.com.reclameonibus.async.JsonParser;
import mobile.br.com.reclameonibus.fragment.AjustesFragment;
import mobile.br.com.reclameonibus.fragment.BottomBarFragment;
import mobile.br.com.reclameonibus.fragment.DialogLoginFragment;
import mobile.br.com.reclameonibus.fragment.ListaReclamacaoFragment;
import mobile.br.com.reclameonibus.fragment.MainFragment;
import mobile.br.com.reclameonibus.fragment.PerfilFragment;
import mobile.br.com.reclameonibus.fragment.SobreFragment;
import mobile.br.com.reclameonibus.fragment.WebViewOnibusFragment;
import mobile.br.com.reclameonibus.interfaces.OnLoginActionListener;
import mobile.br.com.reclameonibus.model.Reclamacao;
import mobile.br.com.reclameonibus.model.ReclamacaoParcelable;
import mobile.br.com.reclameonibus.model.User;

/**
 * Created by Matheus on 16/09/2016.
 */
public class MainActivity extends AppCompatActivity implements
        BottomBarFragment.OnFragmentMenuClickListener,
        MainFragment.OnReclamacao,
        SobreFragment.OnSobreItemClickListener,
        OnLoginActionListener, ListaReclamacaoFragment.OnReclamacaoItemShareListener,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private static final String TAG_MAIN_MENU = "mobile.br.com.reclameonibus.TAG_MAIN_MENU";
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    Reclamacao rec;
    private ActionBar actionBar;
    private SearchQuery query = new SearchQuery();
    private BottomBarFragment fragBottomBar;
    private ProgressDialog dialogLoading;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        fragBottomBar = (BottomBarFragment) getFragmentManager().findFragmentById(R.id.fragmentMenu);

        // Set up the action bar.
        actionBar = getSupportActionBar();
        // Showing logo
        actionBar.setLogo(R.drawable.ic_onibus);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);

        // Exibir a main fragment ao abrir o app
        setActionBarTitle(getString(R.string.app_name));
        MainFragment mainFragment = new MainFragment();
        setFragment(mainFragment, true, MainFragment.FRAGMENT_ID);

    }

    private void setFragment(Fragment fragment, boolean isNew, String fragmentTag) {
        Fragment existingFragment = getFragmentManager().findFragmentByTag(fragmentTag);

        //testando para saber se o fragment atual esta visivel
        if (!(existingFragment != null && existingFragment.isVisible())) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (isNew) {
                fragmentTransaction.add(R.id.frameContent, fragment, fragmentTag);
            } else {
                fragmentTransaction.replace(R.id.frameContent, fragment, fragmentTag);
            }
            fragmentTransaction.addToBackStack(fragmentTag).commit();
        }
    }

    @Override
    public void onBackPressed() {
        int numOfFragments = getFragmentManager().getBackStackEntryCount();
        MainFragment mainFragment = (MainFragment) getFragmentManager().findFragmentByTag(MainFragment.FRAGMENT_ID);
        //Nao sei o motivo, mas apenas adicionar o fragment nao esta funcionando o onbackpressed
        if (numOfFragments > 1 && (mainFragment != null && !mainFragment.isVisible())) {
            getFragmentManager().popBackStack();
            selectMenuItem(getPreviousFragment());
        } else {
            super.onBackPressed();
        }
    }

    private void selectMenuItem(Fragment fragment) {
        if (fragment instanceof MainFragment ||
                fragment instanceof ListaReclamacaoFragment) {
            fragBottomBar.selectMenuItem(BottomBarFragment.MENU_FRAGMENT_BUTTON_RECLAMACOES);
        } else if (fragment instanceof PerfilFragment) {
            fragBottomBar.selectMenuItem(BottomBarFragment.MENU_FRAGMENT_BUTTON_PERFIL);
        } else if (fragment instanceof AjustesFragment) {
            fragBottomBar.selectMenuItem(BottomBarFragment.MENU_FRAGMENT_BUTTON_CONFIG);
        } else if (fragment instanceof SobreFragment) {
            fragBottomBar.selectMenuItem(BottomBarFragment.MENU_FRAGMENT_BUTTON_SOBRE);
        } else {
            Log.e("MainActivity.selectMenu", "Invalid fragment");
        }
    }

    protected Fragment getPreviousFragment() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 2).getName();
        Log.i("MainAcitivty.getActFrag", "Tag achada: " + tag);
        return getFragmentManager().findFragmentByTag(tag);
    }

    protected Fragment getActiveFragment() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1).getName();
        Log.i("MainAcitivty.getActFrag", "Tag achada: " + tag);
        return getFragmentManager().findFragmentByTag(tag);
    }

    protected void showDialogLoading() {
        dialogLoading = new ProgressDialog(this);
        dialogLoading.setMessage(getString(R.string.text_carregando));
        dialogLoading.setIndeterminate(true);
        dialogLoading.setCancelable(false);
        dialogLoading.show();
    }

    private void hideDialogLoagind() {
        if (dialogLoading.isShowing()) {
            dialogLoading.dismiss();
        }
    }

    //metodo para setar nome do ActionBar
    public void setActionBarTitle(String title) {
        actionBar.setTitle(title);
    }

    @Override
    public void onFragmentMenuClick(int menuItem) {
        //Before change fragment, check if is current fragment.
        switch (menuItem) {
            case BottomBarFragment.MENU_FRAGMENT_BUTTON_RECLAMACOES:
                if (!(getActiveFragment() instanceof MainFragment) &&
                        !(getActiveFragment() instanceof ListaReclamacaoFragment)) {
                    setFragment(new MainFragment(), false, MainFragment.FRAGMENT_ID);

                }
                break;
            case BottomBarFragment.MENU_FRAGMENT_BUTTON_PERFIL:
                if (User.getLoggedUser() == null) {
                    //it works!
                    DialogLoginFragment dialogFrag = new DialogLoginFragment();
                    dialogFrag.show(this.getFragmentManager(), "activationDialog");
                } else {
                    if (!(getActiveFragment() instanceof PerfilFragment)) {
                        setFragment(new PerfilFragment(), false, PerfilFragment.FRAGMENT_ID);
                    }
                }
                break;
            case BottomBarFragment.MENU_FRAGMENT_BUTTON_CONFIG:
                if (!(getActiveFragment() instanceof AjustesFragment)) {
                    setFragment(new AjustesFragment(), false, AjustesFragment.FRAGMENT_ID);
                }
                break;
            case BottomBarFragment.MENU_FRAGMENT_BUTTON_SOBRE:
                if (!(getActiveFragment() instanceof SobreFragment)) {
                    setFragment(new SobreFragment(), false, SobreFragment.FRAGMENT_ID);
                }
                break;
        }

    }

    @Override
    public void OnLoginClick(String email, String senha) {
        showDialogLoading();
        User.loginInBackground(email, senha, new User.UserLoginCallback() {
            @Override
            public void OnLoginRequestDone(String message, User user) {
                checkIfMainFragmentIsActive(user);
                hideDialogLoagind();
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkIfMainFragmentIsActive(User loggedUser) {
        MainFragment mainFragment = (MainFragment) getFragmentManager().findFragmentByTag(MainFragment.FRAGMENT_ID);
        if (mainFragment.isVisible() && loggedUser != null) {
            mainFragment.showScreenAsLoggedUser(loggedUser);
        }

    }

    public void ativaGps() {
        LocationManager lm = (LocationManager)
                this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, (LocationListener) this);
    }

    public void OnFacebookShareClick(String contentUrl, String imageUrl, String title, String contentText) {
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(contentUrl))
                .setImageUrl(Uri.parse(imageUrl))
                .setContentTitle(title)
                .setContentDescription(contentText)
                .build();

        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(CallbackManager.Factory.create(), new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {

            }
        });

        if (shareDialog.canShow(ShareLinkContent.class)) {
            shareDialog.show(content);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onReclamacao() {
        query.search(new SearchListener() {
            @Override
            public void OnSearchCompleted(ArrayList<ReclamacaoParcelable> reclamacoes) {
                if (reclamacoes == null) {
                    Log.i("onReclamacao", "OnSearchCompleted: nenhuma reclamacao");
                } else {
                    Log.i("onReclamacao", "OnSearchCompleted: " + reclamacoes.size() + " reclamacoes");
                    ListaReclamacaoFragment listaReclamacaoFragment = new ListaReclamacaoFragment().newInstance(reclamacoes);
                    setFragment(listaReclamacaoFragment, false, ListaReclamacaoFragment.FRAGMENT_ID);
                }
            }

        });
    }

    @Override
    public void OnOnibusClick() {
        setFragment(new WebViewOnibusFragment(), false, "FragmentWebViewOnibus");
        setActionBarTitle(getString(R.string.title_section5));

    }

    protected interface SearchListener {
        public void OnSearchCompleted(ArrayList<ReclamacaoParcelable> reclamacoes);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
//        Log.i("LOG", "onConnected(" + bundle + ")");
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//
//        if (l != null){
//            Log.i("LOG","latitude: "+l.getLatitude());
//            Log.i("LOG","longitude: "+l.getLongitude());
//        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("LOG","onConnectionSuspended(" + i + ")");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }



    public class SearchQuery {

        protected void search(final SearchListener callback) {
            new AsyncTask<Void, Void, Boolean>() {
                ArrayList<ReclamacaoParcelable> reclamacoes = new ArrayList<>();
                String message = "";

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    showDialogLoading();
                }

                @Override
                protected Boolean doInBackground(Void... params) {
                    ArrayList<NameValuePair> paramsData = new ArrayList<NameValuePair>();

                    paramsData.add(new BasicNameValuePair("action", "list"));

                    JSONObject jObjectResponse = JsonParser.
                            postDataObject(Connection.REC_URL, JsonParser.POST, paramsData);

                    try {
                        message = jObjectResponse.getString("message");
                        if (!jObjectResponse.getBoolean("error")) {
                            JSONArray jArrayResponse = jObjectResponse.getJSONArray("response");
                            int reclamacoesSize = jArrayResponse.length();
                            for (int i = 0; i < reclamacoesSize; i++) {
                                reclamacoes.add
                                        (new ReclamacaoParcelable(jArrayResponse.getJSONObject(i)));
                            }
                            return true;
                        } else {
                            reclamacoes = null;
                            return false;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        message = e.getMessage();
                        return false;
                    }
                }

                @Override
                protected void onPostExecute(Boolean success) {
                    hideDialogLoagind();
                    callback.OnSearchCompleted(reclamacoes);
                }
            }.execute();
        }
    }
}


