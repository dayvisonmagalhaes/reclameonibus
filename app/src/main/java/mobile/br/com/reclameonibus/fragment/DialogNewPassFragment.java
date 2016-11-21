package mobile.br.com.reclameonibus.fragment;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mobile.br.com.reclameonibus.AndroidUtils;
import mobile.br.com.reclameonibus.R;
import mobile.br.com.reclameonibus.async.Connection;
import mobile.br.com.reclameonibus.async.JsonParser;
import mobile.br.com.reclameonibus.model.User;

/**
 * Created by Matheus on 16/09/2016.
 */
public class DialogNewPassFragment extends DialogFragment {
    private ProgressDialog dialogLoading;

    public DialogNewPassFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_new_pass, container, false);

        final EditText editSenha = (EditText) contentView.findViewById(R.id.editSenha);
        final EditText editNovaSenha = (EditText) contentView.findViewById(R.id.editNovaSenha);
        final EditText editSenhaConf = (EditText) contentView.findViewById(R.id.editConfirmaSenha);
        Button btAlterar = (Button) contentView.findViewById(R.id.btAlterar);

        btAlterar.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                if (AndroidUtils.isNetworkAvailable(getContext())) {
                    String senha = editSenha.getText().toString();
                    String novaSenha = editNovaSenha.getText().toString();
                    String senhaConf = editSenhaConf.getText().toString();
                    String email = User.getLoggedUser().getEmail();

                    ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("action", "change_pass"));
                    params.add(new BasicNameValuePair("senha", senha));
                    params.add(new BasicNameValuePair("email", email));
                    params.add(new BasicNameValuePair("novaSenha", novaSenha));

                    if (novaSenha.equals(senhaConf)) {
                        AsyncChangePassword asyncRequest = new AsyncChangePassword();
                        asyncRequest.execute(params);
                    } else {
                        Toast.makeText(getActivity(), "As novas senhas não são iguais.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Sem conexão com a Internet.", Toast.LENGTH_LONG).show();
                }

            }
        });

        return contentView;
    }

    protected void showDialogLoading() {
        dialogLoading = new ProgressDialog(getActivity());
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

    private class AsyncChangePassword extends AsyncTask<ArrayList<NameValuePair>, Void, Boolean> {

        String message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialogLoading();
        }

        @Override
        protected Boolean doInBackground(ArrayList<NameValuePair>... params) {
            //O primeiro item deve conter os parametros a serem enviados.
            ArrayList<NameValuePair> arrayParams = params[0];

            JSONObject jArrayResponse = JsonParser.postDataObject(Connection.AUTH_URL, JsonParser.POST, arrayParams);
            try {
                message = jArrayResponse.getString("message");
                return jArrayResponse.getBoolean("error");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean error) {
            //se existe alguma mensagem de erro.
            hideDialogLoagind();
            if (!error) {
                DialogNewPassFragment.this.dismiss();
            }
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

        }
    }


}
