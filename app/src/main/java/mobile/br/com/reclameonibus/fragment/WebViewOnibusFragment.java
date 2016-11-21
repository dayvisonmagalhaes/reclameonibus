package mobile.br.com.reclameonibus.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import mobile.br.com.reclameonibus.MainActivity;
import mobile.br.com.reclameonibus.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewOnibusFragment extends Fragment {


    public WebViewOnibusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_section5));
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_webview, container, false);

        final ProgressBar progressBar = (ProgressBar) contentView.findViewById(R.id.progressBar);

        WebView webView = (WebView) contentView.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://www.rioonibus.com/rioonibus-wordpress/mapa/linhas.php");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

        });

        return contentView;
    }


}
