package mobile.br.com.reclameonibus.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import mobile.br.com.reclameonibus.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BottomBarFragment.OnFragmentMenuClickListener} interface
 * to handle interaction events.
 * Use the {@link BottomBarFragment#} factory method to
 * create an instance of this fragment.
 */
public class BottomBarFragment extends Fragment implements View.OnClickListener {
    public static final String FRAGMENT_ID = "mobile.br.com.reclameonibus.fragment.BottomBarFragment";
    public final static int MENU_FRAGMENT_BUTTON_RECLAMACOES = 0;
    public final static int MENU_FRAGMENT_BUTTON_PERFIL = 1;
    public final static int MENU_FRAGMENT_BUTTON_CONFIG = 2;
    public final static int MENU_FRAGMENT_BUTTON_SOBRE = 3;
    ImageButton btReclamacoes, btPerfil, btConfig, btSobre;
    TextView txReclamacoes, txPerfil, txConfig, txSobre;
    LinearLayout layoutReclamacoes, layoutPerfil, layoutConfig, layoutSobre;
    private OnFragmentMenuClickListener listener;

    public BottomBarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_bottom_bar, container, false);
        findViews(contentView);
        selectMenuItem(MENU_FRAGMENT_BUTTON_RECLAMACOES);
        return contentView;
    }

    private void findViews(View contentView) {
        btReclamacoes = (ImageButton) contentView.findViewById(R.id.menu_button_01);
        btPerfil = (ImageButton) contentView.findViewById(R.id.menu_button_02);
        btConfig = (ImageButton) contentView.findViewById(R.id.menu_button_03);
        btSobre = (ImageButton) contentView.findViewById(R.id.menu_button_04);

        txReclamacoes = (TextView) contentView.findViewById(R.id.menu_text_01);
        txPerfil = (TextView) contentView.findViewById(R.id.menu_text_02);
        txConfig = (TextView) contentView.findViewById(R.id.menu_text_03);
        txSobre = (TextView) contentView.findViewById(R.id.menu_text_04);

        layoutReclamacoes = (LinearLayout) contentView.findViewById(R.id.menu_item_01);
        layoutPerfil = (LinearLayout) contentView.findViewById(R.id.menu_item_02);
        layoutConfig = (LinearLayout) contentView.findViewById(R.id.menu_item_03);
        layoutSobre = (LinearLayout) contentView.findViewById(R.id.menu_item_04);

        layoutReclamacoes.setOnClickListener(this);
        layoutPerfil.setOnClickListener(this);
        layoutConfig.setOnClickListener(this);
        layoutSobre.setOnClickListener(this);
    }

    public void selectMenuItem(int id) {
        switch (id) {
            case MENU_FRAGMENT_BUTTON_RECLAMACOES:
                txReclamacoes.setTextColor(getResources().getColor(R.color.red));
                btReclamacoes.setImageResource(R.drawable.icone_anuncios_on);

                txPerfil.setTextColor(getResources().getColor(R.color.gray_dark));
                btPerfil.setImageResource(R.drawable.icone_perfil_off);

                txSobre.setTextColor(getResources().getColor(R.color.gray_dark));
                btSobre.setImageResource(R.drawable.icone_sobre_off);

                txConfig.setTextColor(getResources().getColor(R.color.gray_dark));
                btConfig.setImageResource(R.drawable.icone_ajustes_off);
                break;
            case MENU_FRAGMENT_BUTTON_PERFIL:
                txReclamacoes.setTextColor(getResources().getColor(R.color.gray_dark));
                btReclamacoes.setImageResource(R.drawable.icone_anuncios_off);

                txPerfil.setTextColor(getResources().getColor(R.color.red));
                btPerfil.setImageResource(R.drawable.icone_perfil_on);

                txSobre.setTextColor(getResources().getColor(R.color.gray_dark));
                btSobre.setImageResource(R.drawable.icone_sobre_off);

                txConfig.setTextColor(getResources().getColor(R.color.gray_dark));
                btConfig.setImageResource(R.drawable.icone_ajustes_off);
                break;
            case MENU_FRAGMENT_BUTTON_CONFIG:
                txReclamacoes.setTextColor(getResources().getColor(R.color.gray_dark));
                btReclamacoes.setImageResource(R.drawable.icone_anuncios_off);

                txPerfil.setTextColor(getResources().getColor(R.color.gray_dark));
                btPerfil.setImageResource(R.drawable.icone_perfil_off);

                txConfig.setTextColor(getResources().getColor(R.color.red));
                btConfig.setImageResource(R.drawable.icone_ajustes_on);

                txSobre.setTextColor(getResources().getColor(R.color.gray_dark));
                btSobre.setImageResource(R.drawable.icone_sobre_off);
                break;
            case MENU_FRAGMENT_BUTTON_SOBRE:
                txReclamacoes.setTextColor(getResources().getColor(R.color.gray_dark));
                btReclamacoes.setImageResource(R.drawable.icone_anuncios_off);

                txPerfil.setTextColor(getResources().getColor(R.color.gray_dark));
                btPerfil.setImageResource(R.drawable.icone_perfil_off);

                txConfig.setTextColor(getResources().getColor(R.color.gray_dark));
                btConfig.setImageResource(R.drawable.icone_ajustes_off);

                txSobre.setTextColor(getResources().getColor(R.color.red));
                btSobre.setImageResource(R.drawable.icone_sobre_on);
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnFragmentMenuClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentMenuClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_item_01:
                listener.onFragmentMenuClick(MENU_FRAGMENT_BUTTON_RECLAMACOES);
                selectMenuItem(MENU_FRAGMENT_BUTTON_RECLAMACOES);
                break;
            case R.id.menu_item_02:
                listener.onFragmentMenuClick(MENU_FRAGMENT_BUTTON_PERFIL);
                selectMenuItem(MENU_FRAGMENT_BUTTON_PERFIL);
                break;
            case R.id.menu_item_03:
                listener.onFragmentMenuClick(MENU_FRAGMENT_BUTTON_CONFIG);
                selectMenuItem(MENU_FRAGMENT_BUTTON_CONFIG);
                break;
            case R.id.menu_item_04:
                listener.onFragmentMenuClick(MENU_FRAGMENT_BUTTON_SOBRE);
                selectMenuItem(MENU_FRAGMENT_BUTTON_SOBRE);
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentMenuClickListener {
        public void onFragmentMenuClick(int menuItem);
    }

}
