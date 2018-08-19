package hu.polidor.webapprunner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import org.sufficientlysecure.donations.DonationsFragment;


public class DonationActivity  extends FragmentActivity
{

    /**
     * Google
     */
    private static final String GOOGLE_PUBKEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkdaa6zlFB9y/sx7ptSdiUl981dSi8gB0R552t9ZYAx8ONORYaZwvFscHwiDEauGLUWHQF45vriARdN5irgE/X7+/9+XTUSX1vIDPI770dz1NtT54qlqjPJMj1HFfJrcyhSU+eVbZcPw1TVdZPRoN1n5eM3zYGzOCDsDenq/SgbHQRmQKVsLlPtYUH8N93nmbbNvsWGwmB0gaFqTzYsg62S7hUvfqM0HAcEbHPiC4C0nWmbO1izMZCcGeqOKOyJdZyZQ3k8WZmpPbXYqgm+hxJHC05KO8ffE7TrC6KtL0jQBm73WfBI3R8orieJN8M9dft9PPkf8DPEHIeF7TPp41IQIDAQAB";
    private static final String[] GOOGLE_CATALOG = new String[]{"donate.coffee", "donate.beer", "donate.dinner", "donate.develop"};

    /**
     * PayPal
     */
    private static final String PAYPAL_USER = "kiskope@vipmail.hu";
    private static final String PAYPAL_CURRENCY_CODE = "EUR";

    /**
     * Flattr
     */
    private static final String FLATTR_PROJECT_URL = "https://github.com/dschuermann/android-donations-lib/";
    // FLATTR_URL without http:// !
    private static final String FLATTR_URL = "flattr.com/thing/712895/dschuermannandroid-donations-lib-on-GitHub";

    /**
     * Bitcoin
     */
    private static final String BITCOIN_ADDRESS = "1Kope1L96RAPFGpG7wSF6HaZfkF9JQ4TRJ";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);

        setContentView(R.layout.donations_activity);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DonationsFragment donationsFragment;
		donationsFragment = DonationsFragment.newInstance(false, true, GOOGLE_PUBKEY, GOOGLE_CATALOG,
														  getResources().getStringArray(R.array.donation_google_catalog_values), true, PAYPAL_USER, PAYPAL_CURRENCY_CODE,
														  getString(R.string.donation_paypal_item), false, FLATTR_URL, FLATTR_PROJECT_URL, false, BITCOIN_ADDRESS);
        ft.replace(R.id.donations_activity_container, donationsFragment, "donationsFragment");
        ft.commit();
    }

    /**
     * Needed for Google Play In-app Billing. It uses startIntentSenderForResult(). The result is not propagated to
     * the Fragment like in startActivityForResult(). Thus we need to propagate manually to our Fragment.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
        super.onActivityResult(requestCode, resultCode, data);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("donationsFragment");
        if (fragment != null)
		{
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
