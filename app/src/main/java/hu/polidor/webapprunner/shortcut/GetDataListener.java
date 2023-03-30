package hu.polidor.webapprunner.shortcut;

import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * Download contents from url
 *
 * @author Gábor KOLÁROVICS
 * @since 2018.10.22
 */
public class GetDataListener implements OnClickListener {

    /**
     * Parent activity
     */
    private final UrlShortcutActivity urlShortcut;

    /**
     * Constructor with parent activity
     */
    public GetDataListener(UrlShortcutActivity urlShortcut) {
        this.urlShortcut = urlShortcut;
    }

    @Override
    public void onClick(View view) {
        urlShortcut.getBtnGetData().setEnabled(false);
        urlShortcut.getProgressBar().setVisibility(ProgressBar.VISIBLE);

        String url = urlShortcut.getEtUrl().getText().toString();
        if (!url.contains("://")) {
            url = "http://" + url.trim();
        }

        WebView wvPage = new WebView(urlShortcut);
        wvPage.setWebViewClient(new GetDataWebClient(urlShortcut));
        wvPage.setWebChromeClient(new GetDataChromeClient(urlShortcut));
        wvPage.loadUrl(url);
    }

}
