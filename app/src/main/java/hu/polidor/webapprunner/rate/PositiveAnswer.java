package hu.polidor.webapprunner.rate;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import hu.polidor.webapprunner.common.PreferenceHelper;
import hu.polidor.webapprunner.common.Utils;

/**
 * Positive answer to applicaton rate question
 *
 * @author Gábor KOLÁROVICS
 * @since 2018.11.28
 */
public class PositiveAnswer implements DialogInterface.OnClickListener {

    private final Activity activity;

    public PositiveAnswer(final Activity activity) {
        this.activity = activity;
    }

    /**
     * Google play url prefix
     */
    private static final String GOOGLE_PLAY = "https://play.google.com/store/apps/details?id=";

    /**
     * Google play store package name
     */
    private static final String GOOGLE_PLAY_STORE_PACKAGE = "com.android.vending";

    @Override
    public void onClick(DialogInterface dialogInterface, int action) {
        PreferenceHelper.setRateDialogApproved(activity, true);
        String packageName = activity.getPackageName();
        Intent intent = new Intent(Intent.ACTION_VIEW, packageName == null ? null : Uri.parse(GOOGLE_PLAY + packageName));
        if (Utils.isPlayServicesAvailable(activity)) {
            intent.setPackage(GOOGLE_PLAY_STORE_PACKAGE);
        }
        activity.startActivity(intent);
    }

}
