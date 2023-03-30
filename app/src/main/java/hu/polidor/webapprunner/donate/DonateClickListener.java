package hu.polidor.webapprunner.donate;

import static hu.polidor.webapprunner.Const.TAG;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import hu.polidor.webapprunner.R;
import hu.polidor.webapprunner.donate.google.util.IabHelper;

/**
 * Start donate button click listener
 *
 * @author Gábor KOLÁROVICS
 * @since 2019.08.23
 */
public class DonateClickListener implements OnClickListener {

    /**
     * Parent activity
     */
    private final DonationActivity donationActivity;

    /**
     * Callback for when a purchase is finished
     */
    private final IabHelper.OnIabPurchaseFinishedListener purchaseFinishedListener;

    /**
     * Constructor when set parent activity and create purchase finished listener
     *
     * @param donationActivity is the parent activity
     */
    public DonateClickListener(final DonationActivity donationActivity) {
        this.donationActivity = donationActivity;
        this.purchaseFinishedListener = new PurchaseFinishedListener(donationActivity);
    }

    @Override
    public void onClick(View view) {
        try {
            final int index = donationActivity.getSkuSpinner().getSelectedItemPosition();
            donationActivity.log("selected item in spinner: " + index);
            donationActivity.getIabHelper().launchPurchaseFlow(donationActivity, donationActivity.getSkuProductId(index), IabHelper.ITEM_TYPE_INAPP, 0, purchaseFinishedListener, null);
        } catch (IllegalStateException e) {
            // In some devices, it is impossible to setup IAB Helper
            // and this exception is thrown, being almost "impossible"
            // to the user to control it and forcing app close.
            Log.e(TAG, e.getMessage());

            donationActivity.openDialog(android.R.drawable.ic_dialog_alert,
                    R.string.donations__google_android_market_not_supported_title,
                    donationActivity.getString(R.string.donations__google_android_market_not_supported));
        }
    }

}
