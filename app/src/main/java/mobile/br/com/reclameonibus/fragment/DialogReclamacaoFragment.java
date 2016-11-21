package mobile.br.com.reclameonibus.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import mobile.br.com.reclameonibus.AndroidUtils;
import mobile.br.com.reclameonibus.MainActivity;
import mobile.br.com.reclameonibus.R;
import mobile.br.com.reclameonibus.exception.DadoNaoPreenchidoException;
import mobile.br.com.reclameonibus.model.Reclamacao;
import mobile.br.com.reclameonibus.model.User;

/**
 * Created by Matheus on 15/09/2016.
 */
public class DialogReclamacaoFragment extends DialogFragment implements View.OnClickListener,
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener,
        LocationListener{

    private static final int CAMERA_REQUEST = 1888;
    AutoCompleteTextView dadosLinha;
    EditText dadosNumOrdem, dadosLocal;
    TextView textDisplayTime, textDisplayDate;
    Spinner dadosReclamacao;
    Button btFoto, btAlterarHora, btAlterarData, btEnviarSolicitacao;
    ImageView ivImageReclamacao;
    CheckBox boxGps;
    Reclamacao rec;


    private JSONArray jsonArrayReclamações, jsonArrayLinhas;
    private ProgressDialog dialogLoading;
    private int year, month, day, hora, minuto;
    private GoogleApiClient mGoogleApiClient;

    double latitude, longitude;

    public DialogReclamacaoFragment() {

    }

    public void dataHorarioAtual() {
        final Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        hora = cal.get(Calendar.HOUR_OF_DAY);
        minuto = cal.get(Calendar.MINUTE);

    }

    public void atualizaData(View view) {

        initDate();
        final Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);

        com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog =
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        this,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                );


        Calendar cMax = Calendar.getInstance();
        datePickerDialog.setMaxDate(cMax);
        datePickerDialog.show(getFragmentManager(), "datePickerDialog");

    }

    private void initDate() {
        if (year == 0) {
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }
    }

    public void atualizaHorario(View view) {

        final Calendar cal = Calendar.getInstance();
//        cal.set(hora, minuto);

        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                this,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
        );

        timePickerDialog.setMaxTime(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
        timePickerDialog.show(getFragmentManager(), "timePickerDialog");
    }


    private void updateDisplayData() {
        textDisplayDate.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(day).append("/")
                        .append(month + 1).append("/")
                        .append(year).append(" "));
    }


    /**
     * Atualizar TextView da hora
     */
    private void updateDisplayHora() {
        String horaFormatada = String.format("%02d", hora);
        String minutoFormatado = String.format("%02d", minuto);
        textDisplayTime.setText(
                new StringBuilder()

                        .append(horaFormatada).append(":")
                        .append(minutoFormatado));

    }

    private void updateLocal() {
        Geocoder myLocation = new Geocoder(getActivity(), new Locale("pt","BR"));
        try {
            List<Address> list = myLocation.getFromLocation(latitude, longitude, 1);
            if (list.size() > 0) {
                Address address = list.get(0);
                for (int i = 0; i< address.getMaxAddressLineIndex(); i++){
                    dadosLocal.setText(
                            new StringBuilder()
                                    .append(address.getAddressLine(0)));

                }
            }
            Log.i("Endereco", String.valueOf(dadosLocal));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_dialog_reclamacao, container, false);

        loadJSONFromAssetLinhas();
        loadJSONFromAssetReclamacoes();
        findViews(contentView);

        /** Display the current date in the TextView */
        dataHorarioAtual();
        updateDisplayData();
        updateDisplayHora();
//
//        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
//                .addOnConnectionFailedListener(this)
//                .addConnectionCallbacks(this)
//                .addApi(LocationServices.API)
//                .build();

        return contentView;

    }

    /**
     * Encontra todas as views e popula os hashs com elas.
     *
     * @param contentView
     */

    void findViews(View contentView) {
        dadosLinha = (AutoCompleteTextView) contentView.findViewById(R.id.editLinha);
        dadosNumOrdem = (EditText) contentView.findViewById(R.id.editOrdem);
        dadosLocal = (EditText) contentView.findViewById(R.id.editLocal);
        textDisplayTime = (TextView) contentView.findViewById(R.id.textDisplayTime);
        textDisplayDate = (TextView) contentView.findViewById(R.id.textDisplayDate);
        dadosReclamacao = (Spinner) contentView.findViewById(R.id.dadosReclamacao);

        btFoto = (Button) contentView.findViewById(R.id.btFoto);
        btFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPhoto();
            }
        });

        ivImageReclamacao = (ImageView) contentView.findViewById(R.id.ivImageReclamacao);


        btAlterarData = (Button) contentView.findViewById(R.id.btPickDate);
        btAlterarData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizaData(v);

            }
        });


        btAlterarHora = (Button) contentView.findViewById(R.id.btTimerPick);
        btAlterarHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizaHorario(v);
            }
        });

        boxGps = (CheckBox) contentView.findViewById(R.id.boxGps);
        boxGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (boxGps.isChecked()) {
                    catchGPS(getActivity());
                }
            }
        });


        btEnviarSolicitacao = (Button) contentView.findViewById(R.id.btEnviarReclamacao);
        btEnviarSolicitacao.setOnClickListener(this);
        try {
            setReclamacoes();
            setLinhas();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void setReclamacoes() throws JSONException {
        List<String> arrayListReclamacoes = new ArrayList<String>();
        for (int index = 0; index < jsonArrayReclamações.length(); index++) {
            arrayListReclamacoes.add(jsonArrayReclamações.getJSONObject(index).getString("reclamacao"));
        }

        ArrayAdapter<String> spinnerAdapterReclamacoes = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayListReclamacoes);
        dadosReclamacao.setAdapter(spinnerAdapterReclamacoes);

    }

    private void setLinhas() throws JSONException {
        List<String> arrayListLinhas = new ArrayList<String>();
        for (int index = 0; index < jsonArrayLinhas.length(); index++) {
            arrayListLinhas.add(jsonArrayLinhas.getJSONObject(index).getString("linha"));


        }

        ArrayAdapter<String> autoCompleteTextViewAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, arrayListLinhas);
        dadosLinha.setAdapter(autoCompleteTextViewAdapter);

    }




    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {

        User loggedUser = User.getLoggedUser();

        try {
                if (AndroidUtils.isNetworkAvailable(getContext())) {
                    if (loggedUser != null) {
                        criaReclamacao();
                        showDialogLoading();
                        rec.signupInBackground(new Reclamacao.ReclamacaoInsertCallback() {
                            @Override
                            public void OnInsertDone(String message) {
                                hideDialogLoading();
                                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                                DialogReclamacaoFragment.this.dismiss();
                            }

                            @Override
                            public void OnInsertError(String message) {
                                hideDialogLoading();
                                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        DialogLoginFragment dialogLoginFragment = new DialogLoginFragment();
                        dialogLoginFragment.show(getFragmentManager(), "loginFrag");
                        Toast.makeText(getActivity(), "Você precisa estar logado para reclamar.", Toast.LENGTH_LONG).show();
                    }
                } else {
                        Toast.makeText(getActivity(), "Sem conexão com a Internet.", Toast.LENGTH_LONG).show();
                }

        } catch (DadoNaoPreenchidoException e) {
            Toast.makeText(getActivity(), getResources().getString(R.string.toast_preencha), Toast.LENGTH_SHORT).show();
        }


    }

    String spinnerToString(Spinner spinner) {
        return (String) spinner.getSelectedItem();
    }

    String editToString(EditText editText) {
        return editText.getText().toString();
    }

    String imageViewToString(ImageView imageView) {
        return imageView.toString();
    }

    String textViewToString(TextView textView) {
        return textView.getText().toString();
    }

    void checaCamposPreenchidos() throws DadoNaoPreenchidoException {
        if (TextUtils.isEmpty(dadosLinha.getText()) || TextUtils.isEmpty(dadosLocal.getText())) { //definir local como obrigatorio
            throw new DadoNaoPreenchidoException();
        }
    }

    private void loadJSONFromAssetReclamacoes() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("reclamacoes.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            JSONObject jsonReclamacoes = new JSONObject(json);
            jsonArrayReclamações = jsonReclamacoes.getJSONArray("reclamacoes");


        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadJSONFromAssetLinhas() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("linhas2.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            JSONObject jsonLinhas = new JSONObject(json);
            jsonArrayLinhas = jsonLinhas.getJSONArray("onibus");


        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void criaReclamacao() throws DadoNaoPreenchidoException {
        checaCamposPreenchidos();

        rec = new Reclamacao(editToString(dadosLinha), textViewToString(textDisplayTime), textViewToString(textDisplayDate),
                editToString(dadosLocal), spinnerToString(dadosReclamacao));

        rec.setNum_ordem(editToString(dadosNumOrdem));
//        rec.setFoto(imageViewToString(ivImageReclamacao));
    }

    void showDialogLoading() {
        dialogLoading = new ProgressDialog(getActivity());
        dialogLoading.setMessage(getString(R.string.text_carregando));
        dialogLoading.setIndeterminate(true);
        dialogLoading.setCancelable(false);
        dialogLoading.show();
    }

    void hideDialogLoading() {
        if (dialogLoading.isShowing()) {
            dialogLoading.dismiss();
        }
    }

    void pickPhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Fragment frag = this;
        /** Pass your fragment reference **/
        frag.startActivityForResult(intent, 1); // REQUEST_IMAGE_CAPTURE = 12345
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                // Do something with imagePath

                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ivImageReclamacao.setImageBitmap(photo);
                Bitmap b = BitmapFactory.decodeResource(this.getResources(),R.id.ivImageReclamacao);
                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri selectedImage = getImageUri(getActivity(), photo);
                String realPath = getRealPathFromURI(selectedImage);
                selectedImage = Uri.parse(realPath);
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        month = monthOfYear;
        day = dayOfMonth;
        updateDisplayData();

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        hora = hourOfDay;
        minuto = minute;
        updateDisplayHora();
    }

    public void catchGPS(Context context) {

        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean enable = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!enable){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Ativar GPS");
            builder.setMessage("Para localizar seu endereço atual, você precisa ativar o GPS.");
            builder.setPositiveButton
                    ("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent((Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    startActivity(intent);
                }

            });
            builder.setNegativeButton("Não ativar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 50, this);
    }


    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Log.i("LATITUDE", String.valueOf(latitude));
        Log.i("LONGITUDE", String.valueOf(longitude));
        updateLocal();

    }



    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
