package mobile.br.com.reclameonibus.fragment;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import mobile.br.com.reclameonibus.AndroidUtils;
import mobile.br.com.reclameonibus.R;
import mobile.br.com.reclameonibus.exception.DadoNaoPreenchidoException;
import mobile.br.com.reclameonibus.model.User;

/**
 * Created by Matheus on 15/09/2016.
 */
public class DialogCadastroFragment extends DialogFragment implements View.OnClickListener {

    EditText dadosNome, dadosTelefone, dadosEmail, dadosEmailConf, dadosSenha, dadosSenhaConf;
    Spinner dadosBairro;

    Button btCadastro;
    User user;
    private JSONArray jsonArrayBairros;
    private ProgressDialog dialogLoading;

    public DialogCadastroFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_dialog_cadastro, container, false);
        loadJSONFromAsset();
        findViews(contentView);

        return contentView;
    }

    /**
     * Encontra todas as views e popula os hashs com elas.
     *
     * @param contentView
     */
    void findViews(View contentView) {
        dadosNome = (EditText) contentView.findViewById(R.id.editNome);
        dadosTelefone = (EditText) contentView.findViewById(R.id.editTelefone);
        dadosEmail = (EditText) contentView.findViewById(R.id.editEmail);
        dadosEmailConf = (EditText) contentView.findViewById(R.id.editConfirmaEmail);
        dadosSenha = (EditText) contentView.findViewById(R.id.editSenha);
        dadosSenhaConf = (EditText) contentView.findViewById(R.id.editConfirmaSenha);
        dadosBairro = (Spinner) contentView.findViewById(R.id.editBairro);

        btCadastro = (Button) contentView.findViewById(R.id.btCadastro);
        btCadastro.setOnClickListener(this);
        try {
            setBairros();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("bairros.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            JSONObject jsonBairros = new JSONObject(json);
            jsonArrayBairros = jsonBairros.getJSONArray("bairro");

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setBairros() throws JSONException {
        List<String> arrayListBairros = new ArrayList<String>();
        for (int index = 0; index < jsonArrayBairros.length(); index++) {
            arrayListBairros.add(jsonArrayBairros.getJSONObject(index)
                    .getString("nome"));
        }

        ArrayAdapter<String> spinnerAdapterBairros =
                new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                arrayListBairros);
        dadosBairro.setAdapter(spinnerAdapterBairros);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @SuppressWarnings("unchecked")
    @Override
    public void onClick(View v) {
        try {
            if (AndroidUtils.isNetworkAvailable(getContext())) {
                criaUsuario();
                showDialogLoading();
                user.signupInBackground(new User.UserSignUpCallback() {
                    @Override
                    public void OnSignUpDone(String message) {
                        hideDialogLoagind();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                        DialogCadastroFragment.this.dismiss();
                    }

                    @Override
                    public void OnSignUpError(String message) {
                        hideDialogLoagind();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    }
                });
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

    void checaSenhas() {
        if (dadosSenha.getText() != dadosSenhaConf.getText()) {
            Toast.makeText(getActivity(), getResources().getString(R.string.toast_senha_diferente), Toast.LENGTH_SHORT).show();
        }

    }

    void checaEmails() {
        if (dadosEmail.getText() != dadosEmailConf.getText()) {
            Toast.makeText(getActivity(), getResources().getString(R.string.toast_email_diferente), Toast.LENGTH_SHORT).show();
        }

    }

    void checaCamposPreenchidos() throws DadoNaoPreenchidoException {
        if (TextUtils.isEmpty(dadosNome.getText()) || TextUtils.isEmpty(dadosEmail.getText()) ||
                TextUtils.isEmpty(dadosSenha.getText()) || TextUtils.isEmpty(dadosSenhaConf.getText())) {
            throw new DadoNaoPreenchidoException();
        }
    }

    void criaUsuario() throws DadoNaoPreenchidoException{
        checaCamposPreenchidos();
//        checaEmails();
//        checaSenhas();

        user = new User(editToString(dadosNome), editToString(dadosEmail), editToString(dadosSenha));
        user.setTelefone(editToString(dadosTelefone));
        user.setBairro(spinnerToString(dadosBairro));

    }

    void showDialogLoading() {
        dialogLoading = new ProgressDialog(getActivity());
        dialogLoading.setMessage(getString(R.string.text_carregando));
        dialogLoading.setIndeterminate(true);
        dialogLoading.setCancelable(false);
        dialogLoading.show();
    }

    void hideDialogLoagind() {
        if (dialogLoading.isShowing()) {
            dialogLoading.dismiss();
        }
    }
}
