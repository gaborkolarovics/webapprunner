package hu.polidor.webapprunner;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class WebAppWebChromeClient extends WebChromeClient {

    ProgressBar pbStatus;

    public WebAppWebChromeClient(ProgressBar pbStatus) {
        this.pbStatus = pbStatus;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        pbStatus.setProgress(newProgress);
        super.onProgressChanged(view, newProgress);
    }

}
