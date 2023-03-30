package hu.polidor.webapprunner.shortcut;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * Download loaded url after redirect
 *
 * @author Gábor KOLÁROVICS
 * @since 2018.11.01
 */
public class GetDataWebClient extends WebViewClient {

    /**
     * Parent activity
     */
    private final UrlShortcutActivity urlShortcut;

    /**
     * Consrtuctor
     */
    public GetDataWebClient(final UrlShortcutActivity urlShortcut) {
        this.urlShortcut = urlShortcut;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        urlShortcut.getProgressBar().setVisibility(ProgressBar.INVISIBLE);
        urlShortcut.getEtUrl().setText(url);
        urlShortcut.getStore().setUrl(url);
        urlShortcut.getBtnGetData().setEnabled(true);
        urlShortcut.enableAddIconBtn();
        super.onPageFinished(view, url);
    }

}
