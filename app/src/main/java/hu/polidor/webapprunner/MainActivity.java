package hu.polidor.webapprunner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import hu.polidor.webapprunner.common.PreferenceHelper;
import hu.polidor.webapprunner.common.Utils;
import hu.polidor.webapprunner.rate.AppRate;
import hu.polidor.webapprunner.settings.WebAppSettings;
import hu.polidor.webapprunner.shortcut.UrlShortcutActivity;

public class MainActivity extends Activity {

    public static final int SIGNATURE_ACTIVITY = 1;
    public static final int LOCATION_ACTIVITY = 2;
    public static final int NFCREADER_ACTIVITY = 3;
    public static final String INTENT_STATUS = "status";
    public static final String INTENT_STATUS_DONE = "done";
    public static final String INTENT_STATUS_CANCEL = "cancel";
    public static final String INTENT_VALUE = "extraValue";
    public static final String SIGNATURE_STATUS = "status";
    public static final String SIGNATURE_STATUS_DONE = "done";
    public static final String SIGNATURE_STATUS_CANCEL = "cancel";
    public static final String SIGNATURE_URLIMAGE = "urlImage";
    public static final String FINELOCATION_LOCATION = "location";
    public static final String FINELOCATION_MINDIST = "mindistance";
    public static final String FINELOCATION_MINTIME = "mintimeout";
    public static final String WEBAPP_INTENT_URL = "intenturl";

    public ProgressBar pbStatus;
    public WebView webView;
    public WebAppWebViewClient webappWebViewClient = null;
    public String deviceId;
    public String appurl;

    private Context mContext;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        webView = findViewById(R.id.webview);
        pbStatus = findViewById(R.id.pbProgress);

        if (PreferenceHelper.isFullScreenApp(this)) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        deviceId = PreferenceHelper.getDeviceId(this);
        appurl = getURL(getIntent(), false);

        if (Utils.isPlayServicesAvailable(this)) {

            FirebaseMessaging.getInstance().setAutoInitEnabled(true);
            FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> PreferenceHelper.setC2mToken(MainActivity.this, token));
        }

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setSaveFormData(false);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webView.clearCache(true);
        webView.addJavascriptInterface(new WebAppJsProxy(this), "WebAppRunner");
        webappWebViewClient = new WebAppWebViewClient(pbStatus, swipeRefreshLayout);
        webappWebViewClient.setAppUrl(appurl);
        webView.setWebViewClient(webappWebViewClient);
        webView.setWebChromeClient(new WebAppWebChromeClient(pbStatus));
        if (savedInstanceState == null) {
            webView.loadUrl(appurl);
        } else {
            webView.restoreState(savedInstanceState);
        }

        swipeRefreshLayout.setOnRefreshListener(() -> {
            pbStatus.setProgress(0);
            pbStatus.setVisibility(View.VISIBLE);
            webView.reload();
        });

        AppRate.showRateDialogIfConditionsApply(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TODO: itt lehetne egy osszevont intent integrator ami loadurl listtel jon vissza
        switch (requestCode) {
            case SIGNATURE_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String status = bundle.getString(SIGNATURE_STATUS);
                    if (status.equalsIgnoreCase(SIGNATURE_STATUS_DONE)) {
                        String urlImage = bundle.getString(SIGNATURE_URLIMAGE);
                        String replaced = urlImage.replaceAll("\\x0a", "");
                        webView.loadUrl("javascript:setSignature(\"" + replaced + "\")");
                        webView.loadUrl("javascript:setSignatureImage(\"<img src='data:image/png;base64," + replaced + "' />\")");
                    }
                }
                break;
            case LOCATION_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String status = bundle.getString(INTENT_STATUS);
                    if (status.equalsIgnoreCase(INTENT_STATUS_DONE)) {
                        String locdata = bundle.getString(FINELOCATION_LOCATION);
                        String replaced = locdata.replaceAll("\"", "\\\\\"");
                        webView.loadUrl("javascript:setLocation(\"" + replaced + "\")");
                    }
                }
                break;
            case NFCREADER_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String status = bundle.getString(INTENT_STATUS);
                    if (status.equalsIgnoreCase(INTENT_STATUS_DONE)) {
                        String locdata = bundle.getString(INTENT_VALUE);
                        String replaced = locdata.replaceAll("\"", "\\\\\"");
                        webView.loadUrl("javascript:setNfcData(\"" + replaced + "\")");
                    }
                }
                break;
        }

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            if (scanContent != null) {
                webView.loadUrl("javascript:setBarcode(\"" + scanContent + "\")");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        appurl = getURL(intent, true);
        if (!appurl.isEmpty()) {
            webappWebViewClient.setAppUrl(appurl);
            if (!webappWebViewClient.setLocalUrl(webappWebViewClient.getLastUrl())) {
                pbStatus.setProgress(0);
                pbStatus.setVisibility(View.VISIBLE);
                webView.loadUrl(appurl);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        webView.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack() && !webappWebViewClient.getLocalUrl()) {
            webView.goBack();
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View exitView = inflater.inflate(R.layout.exit, null);
            Button btnRefresh = exitView.findViewById(R.id.btnRefresh);
            Button btnSettings = exitView.findViewById(R.id.btnSettings);
            Button btnShortcut = exitView.findViewById(R.id.btnShortcut);
            builder.setView(exitView);
            builder.setMessage(R.string.question_exit)
                    .setCancelable(false)
                    .setPositiveButton(R.string.base_yes, (dialog, id) -> finish())
                    .setNegativeButton(R.string.base_no, (dialog, id) -> dialog.cancel());
            final AlertDialog alert = builder.create();
            btnRefresh.setOnClickListener(v -> {
                alert.dismiss();
                pbStatus.setProgress(0);
                pbStatus.setVisibility(View.VISIBLE);
                webView.reload();
            });
            btnSettings.setOnClickListener(v -> {
                alert.dismiss();
                Intent i = new Intent();
                i.setClass(mContext, WebAppSettings.class);
                startActivity(i);
            });
            btnShortcut.setOnClickListener(v -> {
                alert.dismiss();
                Intent i = new Intent();
                i.setClass(mContext, UrlShortcutActivity.class);
                startActivity(i);
            });

            alert.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private String getURL(Intent i, Boolean onlyIntent) {
        String respURL = "";
        if (i.getExtras() != null && i.getStringExtra(WEBAPP_INTENT_URL) != null && !i.getStringExtra(WEBAPP_INTENT_URL).isEmpty()) {
            respURL = i.getStringExtra(WEBAPP_INTENT_URL);
            i.removeExtra(WEBAPP_INTENT_URL);
        }
        if (!onlyIntent && respURL.isEmpty()) {
            respURL = PreferenceHelper.getUrl(this);
        }
        return respURL;
    }
}
