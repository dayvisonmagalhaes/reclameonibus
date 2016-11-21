package mobile.br.com.reclameonibus.fragment;

import android.annotation.TargetApi;
import android.app.DialogFragment;
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

/**
 * Created by Matheus on 15/09/2016.
 */
public class DialogForgotPassFragment extends DialogFragment {

    public DialogForgotPassFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_forgot_pass, container, false);

        final EditText editEmail = (EditText) contentView.findViewById(R.id.editEmail);
        Button btSend = (Button) contentView.findViewById(R.id.btRecuperar);

        btSend.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (AndroidUtils.isNetworkAvailable(getContext())) {
                    String email = editEmail.getText().toString();

                    ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("action", "pass"));
                    params.add(new BasicNameValuePair("email", email));

                    AsyncSendPassword asyncRequest = new AsyncSendPassword();
                    asyncRequest.execute(params);

                } else {
                    Toast.makeText(getActivity(), "Sem conexão com a Internet.", Toast.LENGTH_LONG).show();
                }

            }
        });

        return contentView;
    }

    private class AsyncSendPassword extends AsyncTask<ArrayList<NameValuePair>, Void, Boolean> {

        String message;

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
            if (!error) {
                DialogForgotPassFragment.this.dismiss();
            }
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

        }
    }
}
