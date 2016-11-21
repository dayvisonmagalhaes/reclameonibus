package mobile.br.com.reclameonibus.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import mobile.br.com.reclameonibus.NutraBaseImageDecoder;
import mobile.br.com.reclameonibus.R;
import mobile.br.com.reclameonibus.model.ReclamacaoParcelable;
import mobile.br.com.reclameonibus.model.User;

/**
 * Created by Matheus on 20/09/2016.
 */
public class ListaReclamacaoFragment extends Fragment {

    public static final String FRAGMENT_ID = "mobile.br.com.reclameonibus.fragment.ListaReclamacaoFragment";
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_RECLAMACOES = "mobile.br.com.reclameonibus.fragment.ListaReclamacaoFragment.ARG_RECLAMACOES";

    OnReclamacaoItemShareListener callbackShare;

    private ArrayList<ReclamacaoParcelable> reclamacoes;
    private User loggedUser;
    private ImageLoader imageLoader;


    /**
     * The fragment's ListView/GridView.
     */
    private ListView mListView;
    private TextView textEmpty;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListaReclamacaoFragment() {
    }

    public static ListaReclamacaoFragment newInstance(ArrayList<ReclamacaoParcelable> reclamacoes) {
        ListaReclamacaoFragment fragment = new ListaReclamacaoFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_RECLAMACOES, reclamacoes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loggedUser = User.getLoggedUser();

        if (getArguments() != null) {
            reclamacoes = getArguments().getParcelableArrayList(ARG_RECLAMACOES);
        }
        mAdapter = new ReclamacaoAdapter(reclamacoes);


        // Create global configuration and initialize ImageLoader with this config
        imageLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity())
                .imageDecoder(new NutraBaseImageDecoder(true))
                .build();

        imageLoader.init(config);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_reclamacao, container, false);

        // Set the adapter
        mListView = (ListView) view.findViewById(R.id.listReclamacoes);
        textEmpty = (TextView) view.findViewById(R.id.emptyList);
        mListView.setAdapter(mAdapter);
        setEmptyText(getString(R.string.text_empty));

        return view;
    }


    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        if (mListView.getAdapter().isEmpty()) {
            textEmpty.setText(emptyText);
            mListView.setVisibility(View.GONE);
        }
    }

    private void shareWithWhatsapp(String text) {
        PackageManager pm = getActivity().getPackageManager();
        try {
            //Check if package exists or not. If not then code
            //in catch block will be called
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");

            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(waIntent, getString(R.string.text_compartilhar)));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getActivity(), getString(R.string.toast_no_whatsapp), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void shareWithEmail(String title, String content) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, content);

        startActivity(Intent.createChooser(intent, getString(R.string.text_compartilhar)));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callbackShare = (OnReclamacaoItemShareListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnReclamacaoItemShareListener");
        }
    }

    public interface OnReclamacaoItemShareListener {
        public void OnFacebookShareClick(String contentUrl, String imageUrl, String title, String contentText);
    }

    private class ReclamacaoAdapter extends BaseAdapter {

        private static final int SHARE_WHATS = 0;
        private static final int SHARE_FB = 1;
        private static final int SHARE_EMAIL = 2;

        private ArrayList<ReclamacaoParcelable> reclamacoes;
        private LayoutInflater inflater;

        public ReclamacaoAdapter(ArrayList<ReclamacaoParcelable> reclamacoes) {
            this.reclamacoes = reclamacoes;
            this.inflater = (LayoutInflater) ListaReclamacaoFragment.this.getActivity().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return reclamacoes.size();
        }

        @Override
        public Object getItem(int position) {
            return reclamacoes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.reclamacao_adapter_layout, null, false);
                holder.findViews(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final ReclamacaoParcelable reclamacao = reclamacoes.get(position);

            holder.linhaOnibus.setText(reclamacao.getLinha_onibus());
            holder.numeroOrdem.setText(reclamacao.getNum_ordem());
            holder.hora.setText(reclamacao.getHora_ocorrido());
            holder.data.setText(reclamacao.getData_ocorrido());
            holder.local.setText(reclamacao.getLocal_ocorrido());
            holder.tipoRec.setText(reclamacao.getTipo_rec());

            imageLoader.displayImage("https://paneladepressao.s3.amazonaws.com/uploads/campaign/image/390/Foto_0075.jpg", holder.foto, getImageOptions(), holder.imageLoadingListener);
            holder.btnWhats.setOnClickListener(shareClick(SHARE_WHATS, reclamacao));
            holder.btnFacebook.setOnClickListener(shareClick(SHARE_FB, reclamacao));
            holder.btnEmail.setOnClickListener(shareClick(SHARE_EMAIL, reclamacao));

            return convertView;
        }

        private DisplayImageOptions getImageOptions() {
            return new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_onibus)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();
        }

        private View.OnClickListener shareClick(final int type, final ReclamacaoParcelable reclamacao) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String message, messageTitle;
                    switch (type) {
                        case SHARE_WHATS:
                            message = getString(R.string.text_mensagem_compartilhar_whatsapp)
                                    .replaceAll("%linha", reclamacao.getLinha_onibus());
                            message = message.replace("%hora", reclamacao.getHora_ocorrido());
                            message = message.replace("%data", reclamacao.getData_ocorrido());
                            message = message.replace("%local", reclamacao.getLocal_ocorrido());
                            message = message.replace("%tiporec", reclamacao.getTipo_rec());
                            shareWithWhatsapp(message);
                            break;
                        case SHARE_FB:
                            messageTitle = getString(R.string.text_titulo_compartilhar_fb)
                                    .replaceAll("%linha", reclamacao.getLinha_onibus());
                            message = getString(R.string.text_mensagem_compartilhar);
                            message = message.replace("%linha", reclamacao.getLinha_onibus());
                            message = message.replace("%data", reclamacao.getData_ocorrido());
                            message = message.replace("%hora", reclamacao.getHora_ocorrido());
                            message = message.replace("%local", reclamacao.getLocal_ocorrido());
                            message = message.replace("%tiporec", reclamacao.getTipo_rec());
                            callbackShare.OnFacebookShareClick("www.reclameonibus.com.br", "https://paneladepressao.s3.amazonaws.com/uploads/campaign/image/390/Foto_0075.jpg", messageTitle,
                                    message);
                            break;
                        case SHARE_EMAIL:
                            messageTitle = getString(R.string.text_titulo_compartilhar_email)
                                    .replaceAll("%linha", reclamacao.getLinha_onibus());
                            message = getString(R.string.text_mensagem_compartilhar);
                            message = message.replace("%linha", reclamacao.getLinha_onibus());
                            message = message.replace("%hora", reclamacao.getHora_ocorrido());
                            message = message.replace("%data", reclamacao.getData_ocorrido());
                            message = message.replace("%local", reclamacao.getLocal_ocorrido());
                            message = message.replace("%tiporec", reclamacao.getTipo_rec());
                            shareWithEmail(messageTitle, message);
                            break;
                    }
                }
            };
        }


    }

    private class ViewHolder {
        ImageView foto;
        TextView linhaOnibus, numeroOrdem, hora, data, local, tipoRec;
        ImageButton btnFacebook, btnWhats, btnEmail;
        ProgressBar progressBarFoto;

        ImageLoadingListener imageLoadingListener;

        public void findViews(View row) {
            linhaOnibus = (TextView) row.findViewById(R.id.textLinhaOnibus);
            numeroOrdem = (TextView) row.findViewById(R.id.textNumOrdem);
            hora = (TextView) row.findViewById(R.id.textHoraOcorrido);
            data = (TextView) row.findViewById(R.id.textDataOcorrido);
            local = (TextView) row.findViewById(R.id.textLocal);
            tipoRec = (TextView) row.findViewById(R.id.textTipoRec);
            foto = (ImageView) row.findViewById(R.id.ivFoto);
            btnFacebook = (ImageButton) row.findViewById(R.id.ibtFacebook);
            btnWhats = (ImageButton) row.findViewById(R.id.ibtWhatsapp);
            btnEmail = (ImageButton) row.findViewById(R.id.ibtEmail);
            progressBarFoto = (ProgressBar) row.findViewById(R.id.progressBarFoto);

            imageLoadingListener = new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progressBarFoto.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            };
        }
    }

}
