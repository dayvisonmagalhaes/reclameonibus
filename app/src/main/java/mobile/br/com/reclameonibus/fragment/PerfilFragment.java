package mobile.br.com.reclameonibus.fragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mobile.br.com.reclameonibus.AndroidUtils;
import mobile.br.com.reclameonibus.MainActivity;
import mobile.br.com.reclameonibus.R;
import mobile.br.com.reclameonibus.exception.DadoNaoPreenchidoException;
import mobile.br.com.reclameonibus.model.User;

/**
 * Created by Matheus on 15/09/2016.
 */
public class PerfilFragment extends Fragment implements View.OnClickListener {

    public static final String FRAGMENT_ID = "mobile.br.com.reclameonibus.fragment.PerfilFragment";

    HashMap<String, EditText> hashInputsEditText;
    HashMap<String, Spinner> hashInputsSpinner;

    EditText dadosNome, dadosTelefone, dadosEmail, dadosEmailConf, dadosSenha, dadosSenhaConf;

    Spinner dadosBairro;

    LinearLayout linearEditar, linearConfirmar;
    Button btEditar, btConfirmar, btCancelar;
    ProgressDialog dialogLoading;
    User logado;
    private JSONArray jsonArrayBairros, jsonArrayEstados, jsonArrayCidades;

    public PerfilFragment() {
        // Required empty public constructor
        hashInputsEditText = new HashMap<>();
        hashInputsSpinner = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_section2));

        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_editar_perfil, container, false);

        logado = User.getLoggedUser();

        findViews(contentView);
        preencheDados();
        disableEdit();

        return contentView;
    }

    @SuppressWarnings("unchecked")
    private void preencheDados() {

        loadBairro(logado);

        dadosNome.setText(logado.nome);
        dadosTelefone.setText(logado.telefone);
        dadosEmail.setText(logado.email);
        dadosEmailConf.setText(logado.email);
        dadosSenha.setText(logado.senha);
        dadosSenhaConf.setText(logado.senha);
        dadosBairro.setSelection(((ArrayAdapter<String>) dadosBairro.getAdapter()).getPosition(logado.bairro));

    }

    /**
     * Encontra todas as views e popula os hashs com elas.
     *
     * @param contentView
     */
    private void findViews(View contentView) {
        dadosNome = (EditText) contentView.findViewById(R.id.editNome);
        dadosTelefone = (EditText) contentView.findViewById(R.id.editTelefone);
        dadosEmail = (EditText) contentView.findViewById(R.id.editEmail);
        dadosEmailConf = (EditText) contentView.findViewById(R.id.editConfirmaEmail);
        dadosSenha = (EditText) contentView.findViewById(R.id.editSenha);
        dadosSenhaConf = (EditText) contentView.findViewById(R.id.editConfirmaSenha);
        dadosBairro = (Spinner) contentView.findViewById(R.id.editBairro);

        btEditar = (Button) contentView.findViewById(R.id.btEditar);
        btEditar.setOnClickListener(this);
        btConfirmar = (Button) contentView.findViewById(R.id.btConfirmar);
        btConfirmar.setOnClickListener(this);
        btCancelar = (Button) contentView.findViewById(R.id.btCancelar);
        btCancelar.setOnClickListener(this);

        linearEditar = (LinearLayout) contentView.findViewById(R.id.linearEditar);
        linearConfirmar = (LinearLayout) contentView.findViewById(R.id.linearConfirmar);
        alteraLinearVisivel(linearConfirmar, linearEditar);

        //Adicionando tudo num hash para facilitar o manuseio dos dados
        hashInputsEditText.put(User.COLUMN_NOME, dadosNome);
        hashInputsEditText.put(User.COLUMN_TEL, dadosTelefone);
        hashInputsEditText.put(User.COLUMN_EMAIL, dadosEmail);
        hashInputsEditText.put(User.COLUMN_EMAIL_CONF, dadosEmailConf);
        hashInputsEditText.put(User.COLUMN_SENHA, dadosSenha);
        hashInputsEditText.put(User.COLUMN_SENHA_CONF, dadosSenhaConf);
        hashInputsSpinner.put(User.COLUMN_BAIRRO, dadosBairro);

    }

    /**
     * Itera em cada hash para desabilitar a edicao
     */
    private void disableEdit() {
        for (Map.Entry<String, EditText> entry : hashInputsEditText.entrySet()) {
            entry.getValue().setEnabled(false);
        }

        for (Map.Entry<String, Spinner> entry : hashInputsSpinner.entrySet()) {
            entry.getValue().setEnabled(false);
        }
    }

    /**
     * Itera em cada hash para habilitar a edicao
     */
    private void enableEdit() {
        for (Map.Entry<String, EditText> entry : hashInputsEditText.entrySet()) {
            entry.getValue().setEnabled(true);
            dadosEmail.setEnabled(false);
            dadosEmailConf.setEnabled(false);
            dadosSenha.setEnabled(false);
            dadosSenhaConf.setEnabled(false);
        }

        for (Map.Entry<String, Spinner> entry : hashInputsSpinner.entrySet()) {
            entry.getValue().setEnabled(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btEditar:
                alteraLinearVisivel(linearEditar, linearConfirmar);
                enableEdit();
                break;
            case R.id.btConfirmar:
                alteraLinearVisivel(linearConfirmar, linearEditar);
                editUser();
                break;
            case R.id.btCancelar:
                alteraLinearVisivel(linearConfirmar, linearEditar);
                reloadUser();
                disableEdit();
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void editUser() {
        try {
            if (AndroidUtils.isNetworkAvailable(getContext())) {
                editaUsuario();
                showDialogLoading();
                logado.editInBackground(new User.UserEditCallback() {
                    @Override
                    public void onEditDone(String message) {
                        logado.save();
                        disableEdit();
                        hideDialogLoagind();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onEditError(String message) {
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

    void checaCamposPreenchidos() throws DadoNaoPreenchidoException {
        if (TextUtils.isEmpty(dadosNome.getText()) || TextUtils.isEmpty(dadosTelefone.getText()) ||
                TextUtils.isEmpty(dadosEmail.getText()) || TextUtils.isEmpty(dadosEmailConf.getText()) ||
                TextUtils.isEmpty(dadosSenha.getText()) || TextUtils.isEmpty(dadosSenhaConf.getText()) ||
                TextUtils.isEmpty(dadosBairro.getItemAtPosition(dadosBairro.getSelectedItemPosition()).toString())) {
            throw new DadoNaoPreenchidoException();
        }
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

    String spinnerToString(Spinner spinner) {
        return (String) spinner.getSelectedItem();
    }

    String editToString(EditText editText) {
        return editText.getText().toString();
    }

    private void editaUsuario() throws DadoNaoPreenchidoException {
        checaCamposPreenchidos();

        logado.setNome(editToString(dadosNome));
        logado.setTelefone(editToString(dadosTelefone));
        logado.setBairro(spinnerToString(dadosBairro));

    }

    private void alteraLinearVisivel(LinearLayout some, LinearLayout aparece) {
        some.setVisibility(View.GONE);
        aparece.setVisibility(View.VISIBLE);
    }

    private void reloadUser() {
        logado = User.getLoggedUser();
        preencheDados();
    }

    private void loadBairro(User user) {
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

            List<String> arrayListBairros = new ArrayList<String>();
            for (int index = 0; index < jsonArrayBairros.length(); index++) {
                arrayListBairros.add(jsonArrayBairros.getJSONObject(index).getString("nome"));
            }

            ArrayAdapter<String> spinnerAdapterBairros = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayListBairros);
            dadosBairro.setAdapter(spinnerAdapterBairros);
            Log.i("loadBairro", "Bairro:" + user.bairro);
            dadosBairro.setSelection(((ArrayAdapter<String>) dadosBairro.getAdapter()).getPosition(user.bairro));

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
