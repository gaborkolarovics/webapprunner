package hu.polidor.webapprunner.common;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * Base WebChromeClient with ProgressBar
 * <p>
 * Implemented onProgressChanged method, and update progress
 *
 * @author Gábor KOLÁROVOCS
 * @since 2018.11.01
 */
public class BaseWebChromeClient extends WebChromeClient {

    /**
     * ProgressBar reference
     */
    private final ProgressBar progressBar;

    /**
     * Constructor with ProgressBar
     */
    public BaseWebChromeClient(final ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        progressBar.setProgress(newProgress);
        super.onProgressChanged(view, newProgress);
    }

}
