package hu.polidor.webapprunner.donate;

import hu.polidor.webapprunner.R;
import hu.polidor.webapprunner.donate.google.util.IabHelper;
import hu.polidor.webapprunner.donate.google.util.IabResult;
import hu.polidor.webapprunner.donate.google.util.Purchase;

/**
 * Implementation of Iab purchase finin"shed listener
 *
 * @author Gábor KOLÁROVICS
 * @since 2019.10.13
 */
public class PurchaseFinishedListener implements IabHelper.OnIabPurchaseFinishedListener {

    /**
     * Parent activity
     */
    private final DonationActivity donationActivity;

    /**
     * Constructor
     *
     * @param donationActivity parent
     */
    public PurchaseFinishedListener(final DonationActivity donationActivity) {
        this.donationActivity = donationActivity;
    }

    @Override
    public void onIabPurchaseFinished(final IabResult result, final Purchase info) {
        donationActivity.log("Purchase finished: " + result + ", purchase: " + info);

        if (donationActivity.getIabHelper() != null && result.isSuccess()) {
            donationActivity.log("Purchase successful.");
            // directly consume in-app purchase, so that people can donate multiple times
            donationActivity.getIabHelper().consumeAsync(info, donationActivity.getConsumeFinishedListener());
            // show thanks openDialog
            donationActivity.openDialog(android.R.drawable.ic_dialog_info, R.string.donations__thanks_dialog_title, donationActivity.getString(R.string.donations__thanks_dialog));
        }
    }

}
