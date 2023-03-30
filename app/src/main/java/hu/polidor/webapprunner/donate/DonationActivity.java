package hu.polidor.webapprunner.donate;

import static hu.polidor.webapprunner.Const.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import hu.polidor.webapprunner.BuildConfig;
import hu.polidor.webapprunner.R;
import hu.polidor.webapprunner.donate.google.util.IabHelper;

/**
 * Donation activity
 *
 * @author Gábor KOLÁROVICS
 * @since 2019.06.28
 */
public class DonationActivity extends Activity {

    /**
     * Google public key and product cataloge
     */
    private static final String GOOGLE_PUBKEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkdaa6zlFB9y/sx7ptSdiUl981dSi8gB0R552t9ZYAx8ONORYaZwvFscHwiDEauGLUWHQF45vriARdN5irgE/X7+/9+XTUSX1vIDPI770dz1NtT54qlqjPJMj1HFfJrcyhSU+eVbZcPw1TVdZPRoN1n5eM3zYGzOCDsDenq/SgbHQRmQKVsLlPtYUH8N93nmbbNvsWGwmB0gaFqTzYsg62S7hUvfqM0HAcEbHPiC4C0nWmbO1izMZCcGeqOKOyJdZyZQ3k8WZmpPbXYqgm+hxJHC05KO8ffE7TrC6KtL0jQBm73WfBI3R8orieJN8M9dft9PPkf8DPEHIeF7TPp41IQIDAQAB";
    private static final String[] CATALOG_GOOGLE = new String[]{"donate.coffee", "donate.beer", "donate.dinner", "donate.develop"};

    /**
     * Test cataloge
     * <a href="http://developer.android.com/google/play/billing/billing_testing.html">Billing test documentation</a>
     */
    private static final String[] CATALOG_DEBUG = new String[]{"android.test.purchased", "android.test.canceled", "android.test.refunded", "android.test.item_unavailable"};

    /**
     * Product selector spinner
     */
    private Spinner skuSpinner;

    /**
     * Google Play helper object
     */
    private IabHelper iabHelper;

    /**
     * Called when consumption is complete
     */
    private final IabHelper.OnConsumeFinishedListener consumeFinishedListener = new ConsumeFinishedListener(this);

    /**
     * Singleton Google spinner
     */
    protected Spinner getSkuSpinner() {
        if (skuSpinner == null) {
            skuSpinner = findViewById(R.id.donations__google_android_market_spinner);
        }
        return skuSpinner;
    }

    /**
     * Return IabHelper
     */
    protected IabHelper getIabHelper() {
        return iabHelper;
    }

    /**
     * Produce Sku product cataloge nice name
     *
     * @return String aray
     */
    protected String[] getCatalog() {
        if (BuildConfig.DEBUG) {
            return CATALOG_DEBUG;
        } else {
            return getResources().getStringArray(R.array.donation_google_catalog_values);
        }
    }

    /**
     * Present Sku product id from cataloge
     *
     * @return String
     */
    protected String getSkuProductId(final int index) {
        if (BuildConfig.DEBUG) {
            return CATALOG_DEBUG[index];
        } else {
            return CATALOG_GOOGLE[index];
        }
    }

    /**
     * Present Iab consume finished listener
     */
    protected IabHelper.OnConsumeFinishedListener getConsumeFinishedListener() {
        return consumeFinishedListener;
    }

    /**
     * Create adapter for Sku spinner
     */
    private ArrayAdapter<CharSequence> populateSkuAdapter() {
        final ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getCatalog());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    /**
     * Open dialog
     */
    protected void openDialog(int icon, int title, String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setIcon(icon);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(true);
        dialog.setNeutralButton(R.string.donations__button_close,
                (dialog1, which) -> dialog1.dismiss()
        );
        dialog.show();
    }

    /**
     * Logging with debug mode is true
     */
    protected void log(final String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, msg);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donations_activity);

        getSkuSpinner().setAdapter(populateSkuAdapter());

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        log("Starting setup.");

        if (iabHelper == null) {
            iabHelper = new IabHelper(this, GOOGLE_PUBKEY);
            // enable debug logging (for a production application, you should set this to false).
            iabHelper.enableDebugLogging(BuildConfig.DEBUG);
            iabHelper.startSetup(new SetupFinisListener(this));
        }

        Button btGoogle = findViewById(R.id.donations__google_android_market_donate_button);
        btGoogle.setOnClickListener(new DonateClickListener(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        log("onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (iabHelper == null) return;

        // Pass on the fragment result to the helper for handling
        if (!iabHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            log("onActivityResult handled by IABUtil.");
        }

    }

}
