package mobile.br.com.reclameonibus.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import mobile.br.com.reclameonibus.AndroidUtils;
import mobile.br.com.reclameonibus.MainActivity;
import mobile.br.com.reclameonibus.R;
import mobile.br.com.reclameonibus.interfaces.OnLoginActionListener;
import mobile.br.com.reclameonibus.model.User;

/**
 * Created by Matheus on 20/09/2016.
 */
public class MainFragment extends Fragment implements View.OnClickListener {
    public static final String FRAGMENT_ID = "mobile.br.com.reclameonibus.fragment.MainFragment";


    private Button btReclamacao, btPesquisar;
    private LinearLayout linearTextNaoLogado, linearTextBemVindo;
    private TextView textNome;
    private OnLoginActionListener listener;
    private OnReclamacao listener2;


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.app_name));
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        btReclamacao = (Button) view.findViewById(R.id.btNovaReclamacao);
        btPesquisar = (Button) view.findViewById(R.id.btPesquisar);
        linearTextBemVindo = (LinearLayout) view.findViewById(R.id.LinearTextBemVindo);
        linearTextNaoLogado = (LinearLayout) view.findViewById(R.id.linearTextLogin);
        textNome = (TextView) view.findViewById(R.id.textBemVindo);

        btReclamacao.setOnClickListener(this);

        btPesquisar.setOnClickListener(this);
        btPesquisar.setOnClickListener(new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                if (AndroidUtils.isNetworkAvailable(getContext())) {
                    if (listener2 != null) {
                        listener2.onReclamacao();
                    }
                } else {
                    Toast.makeText(getActivity(), "Sem conexão com a Internet.", Toast.LENGTH_LONG).show();
                }

            }
        });


        linearTextNaoLogado.setOnClickListener(this);

        User loggedUser = User.getLoggedUser();

        if (loggedUser != null) {
            linearTextNaoLogado.setVisibility(View.GONE);
            textNome.setText(textNome.getText().toString().replace("%s", loggedUser.getNome()));
        } else {
            linearTextBemVindo.setVisibility(View.GONE);
        }

        return view;

    }

    public void showScreenAsLoggedUser(User loggedUser) {
        linearTextNaoLogado.setVisibility(View.GONE);
        textNome.setText(textNome.getText().toString().replace("%s", loggedUser.getNome()));
        linearTextBemVindo.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (OnLoginActionListener) activity;
            listener2 = (OnReclamacao) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnLoginActionListener"
                    + " must implement OnReclamacao");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener2 = null;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btNovaReclamacao: //ir para o fragment de reclamacao sem estar logado, porém estar logado p/ confirmar.
                DialogReclamacaoFragment dialogRec = new DialogReclamacaoFragment();
                dialogRec.show(getFragmentManager(), "reclamacaoFrag");
                break;
            case R.id.linearTextLogin:
                DialogLoginFragment dialogLoginFragment = new DialogLoginFragment();
                dialogLoginFragment.show(getFragmentManager(), "loginFrag");
                break;

        }


    }

    public interface OnReclamacao {
        public void onReclamacao();
    }

}

