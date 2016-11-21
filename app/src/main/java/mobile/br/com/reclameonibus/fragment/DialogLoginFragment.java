package mobile.br.com.reclameonibus.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import mobile.br.com.reclameonibus.AndroidUtils;
import mobile.br.com.reclameonibus.R;
import mobile.br.com.reclameonibus.interfaces.OnLoginActionListener;

/**
 * Created by Matheus on 15/09/2016.
 */
public class DialogLoginFragment extends DialogFragment implements View.OnClickListener {

    Button btCadastro, btLogin;
    EditText editEmail, editSenha;
    OnLoginActionListener listener;


    public DialogLoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_dialog_login, container, false);

        btCadastro = (Button) view.findViewById(R.id.btCadastro);
        btLogin = (Button) view.findViewById(R.id.btLogin);
        editEmail = (EditText) view.findViewById(R.id.editEmail);
        editSenha = (EditText) view.findViewById(R.id.editSenha);
        TextView textEsqueceuSenha = (TextView) view.findViewById(R.id.textEsqueceuSenha);

        btCadastro.setOnClickListener(this);
        btLogin.setOnClickListener(this);
        textEsqueceuSenha.setOnClickListener(this);

        return view;

    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btCadastro:
                DialogCadastroFragment dialogFrag = new DialogCadastroFragment();
                dialogFrag.show(this.getFragmentManager(), "cadastroDialog");
                this.dismiss();
                break;
            case R.id.btLogin:
                if (AndroidUtils.isNetworkAvailable(getContext())) {
                    if (editEmail.getText() != null && editSenha.getText() != null) {
                        listener.OnLoginClick(editEmail.getText().toString(), editSenha.getText().toString());
                        this.dismiss();
                    } else {
                        Toast.makeText(getActivity(), "Os campos são de preenchimento obrigatório.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Sem conexão com a Internet.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.textEsqueceuSenha:
                DialogForgotPassFragment dialogForgotFrag = new DialogForgotPassFragment();
                dialogForgotFrag.show(this.getFragmentManager(), "passDialog");
                this.dismiss();
                break;
        }


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (OnLoginActionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnLoginActionListener");
        }
    }
}
