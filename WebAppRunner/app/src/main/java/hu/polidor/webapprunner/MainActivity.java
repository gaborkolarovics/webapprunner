package hu.polidor.webapprunner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import hu.polidor.webapprunner.common.PreferenceHelper;
import hu.polidor.webapprunner.common.Utils;
import hu.polidor.webapprunner.donate.DonationActivity;
import hu.polidor.webapprunner.rate.AppRate;
import hu.polidor.webapprunner.settings.WebAppSettings;
import hu.polidor.webapprunner.shortcut.UrlShortcutActivity;

import static hu.polidor.webapprunner.Const.TAG;

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
    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    public ProgressBar pbStatus;
    public WebView webView;
    public WebAppWebViewClient webappWebViewClient = null;
    public String deviceId;
    public static String appurl;

    private static Context mContext;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;

    public static void makeMsg(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
        Log.d(TAG, msg);
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

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
        registerReceiver();

        if (Utils.isPlayServicesAvailable(this)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationChannel mChannel = new NotificationChannel(Const.FCM_CHANEL_ID, getString(R.string.fcm_chanel_name), NotificationManager.IMPORTANCE_DEFAULT);
                mChannel.setDescription(getString(R.string.fcm_chanel_description));
                mChannel.enableLights(true);
                mChannel.enableVibration(true);
                if (mNotificationManager != null) {
                    mNotificationManager.createNotificationChannel(mChannel);
                }
            }

            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "getInstanceId failed", task.getException());
                                return;
                            }
                            // Get new Instance ID token
                            String token = task.getResult().getToken();
                            PreferenceHelper.setC2mToken(MainActivity.this, token);
                        }
                    });
        }

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setSaveFormData(false);
        webSettings.setSavePassword(false);
        webView.clearCache(true);
        webView.addJavascriptInterface(new WebAppJsProxy(this), "WebAppRunner");
        webappWebViewClient = new WebAppWebViewClient(this, pbStatus, swipeRefreshLayout);
        webappWebViewClient.setAppUrl(appurl);
        webView.setWebViewClient(webappWebViewClient);
        webView.setWebChromeClient(new WebAppWebChromeClient(pbStatus));
        if (savedInstanceState == null) {
            webView.loadUrl(appurl);
        } else {
            webView.restoreState(savedInstanceState);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pbStatus.setProgress(0);
                pbStatus.setVisibility(View.VISIBLE);
                webView.reload();
            }
        });

        AppRate.ShowRateDialogIfConditionsApply(this);
        Log.d(Const.TAG, "End onCreate [main activity]");
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
        Log.d(TAG, "OnNewIntent (mainactivity)");
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
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
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
            Button btnDonation = exitView.findViewById(R.id.btnDonation);
            builder.setView(exitView);
            builder.setMessage(R.string.question_exit)
                    .setCancelable(false)
                    .setPositiveButton(R.string.base_yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.base_no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            btnRefresh.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    alert.dismiss();
                    pbStatus.setProgress(0);
                    pbStatus.setVisibility(View.VISIBLE);
                    webView.reload();
                }
            });
            btnSettings.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    alert.dismiss();
                    Intent i = new Intent();
                    i.setClass(mContext, WebAppSettings.class);
                    startActivity(i);
                }
            });
            btnShortcut.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    alert.dismiss();
                    Intent i = new Intent();
                    i.setClass(mContext, UrlShortcutActivity.class);
                    startActivity(i);
                }
            });
            btnDonation.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    alert.dismiss();
                    Intent i = new Intent();
                    i.setClass(mContext, DonationActivity.class);
                    startActivity(i);
                }
            });

            alert.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private String getURL(Intent i, Boolean onlyIntent) {
        String respURL = "";
        if (i.getExtras() != null) {
            if (i.getStringExtra(WEBAPP_INTENT_URL) != null && !i.getStringExtra(WEBAPP_INTENT_URL).isEmpty()) {
                respURL = i.getStringExtra(WEBAPP_INTENT_URL);
                i.removeExtra(WEBAPP_INTENT_URL);
            }
        }
        if (!onlyIntent && respURL.isEmpty()) {
            respURL = PreferenceHelper.getUrl(this);
        }
        return respURL;
    }
}
