package hu.polidor.webapprunner.rate;

import android.app.Activity;
import android.content.Context;

import hu.polidor.webapprunner.common.PreferenceHelper;
import hu.polidor.webapprunner.common.Utils;

/**
 * Application rate dialog config and managment util
 *
 * @author Gábor KOLÁROVICS
 * @since 2018.11.28
 */
public final class AppRate {
    private AppRate() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Check AppRate conditions to dialog
     *
     * @param activity Aplocation activity for condition check
     */
    public static void showRateDialogIfConditionsApply(final Activity activity) {
        if (!activity.isFinishing()) {
            if (!PreferenceHelper.getRateDialogApproved(activity)
                    && (Utils.isOverDate(PreferenceHelper.getLastRemindDate(activity), 1) || PreferenceHelper.getLastRemindDate(activity) == 0L)
                    && Utils.isOverDate(PreferenceHelper.getInstallDate(activity), 5)
                    && PreferenceHelper.getLaunchCount(activity) > 10) {
                DialogManager.show(activity);
            }
        }
    }

    /**
     * Monitoring application start
     *
     * @param context Application context for monitoring
     */
    public static void monitor(final Context context) {
        PreferenceHelper.getInstallDate(context);
        PreferenceHelper.increaseLaunchCount(context);
    }

}
