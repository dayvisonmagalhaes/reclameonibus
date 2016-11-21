package mobile.br.com.reclameonibus.fragment;

import android.app.Fragment;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import mobile.br.com.reclameonibus.MainActivity;
import mobile.br.com.reclameonibus.R;
import mobile.br.com.reclameonibus.model.User;

/**
 * Created by Matheus on 15/09/2016.
 */
public class AjustesFragment extends Fragment implements View.OnClickListener {
    public static final String FRAGMENT_ID = "mobile.br.com.reclameonibus.fragment.fragment.AjustesFragment";

    private TextView textLogout, textTrocarSenha;
    private LinearLayout linearUsuarioLogado;

    public AjustesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_section3));

        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_ajustes, container, false);

        findViews(contentView);

        return contentView;
    }

    private void findViews(View contentView) {

        linearUsuarioLogado = (LinearLayout) contentView.findViewById(R.id.linearAjustesLogado);
        textLogout = (TextView) contentView.findViewById(R.id.textLogout);
        textTrocarSenha = (TextView) contentView.findViewById(R.id.textTrocarSenha);
        TextView appVersion = (TextView) contentView.findViewById(R.id.app_version);
        try {
            PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            appVersion.append(" " + packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            appVersion.append(" 0.0.0");
        }
        if (User.getLoggedUser() == null) {
            linearUsuarioLogado.setVisibility(View.GONE);
        }

        textLogout.setOnClickListener(this);
        textTrocarSenha.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textLogout:
                if (User.cascadeLogout()) {
                    linearUsuarioLogado.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), getString(R.string.text_sucesso), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.text_erro), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.textTrocarSenha:
                DialogNewPassFragment dialogNewPass = new DialogNewPassFragment();
                dialogNewPass.show(this.getFragmentManager(), "changePassDialog");
                break;
        }
    }

}
