package mobile.br.com.reclameonibus.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import mobile.br.com.reclameonibus.AndroidUtils;
import mobile.br.com.reclameonibus.MainActivity;
import mobile.br.com.reclameonibus.R;

/**
 * Created by Matheus on 15/09/2016.
 */
public class SobreFragment extends Fragment implements View.OnClickListener {
    public static final String FRAGMENT_ID = "mobile.br.com.reclameonibus.fragment.SobreFragment";

    private OnSobreItemClickListener mListener;

    public SobreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_section4));
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_sobre, container, false);

        LinearLayout linkOnibus = (LinearLayout) contentView.findViewById(R.id.linkOnibus);

        linkOnibus.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (AndroidUtils.isNetworkAvailable(getContext())) {
                    mListener.OnOnibusClick();
                } else {
                    Toast.makeText(getActivity(), "Sem conexão com a Internet.", Toast.LENGTH_LONG).show();
                }
            }
        });

        return contentView;
    }

    @Override
    public void onClick(View v) {
        mListener.OnOnibusClick();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnSobreItemClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSobreItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnSobreItemClickListener {
        public void OnOnibusClick();
    }

}
