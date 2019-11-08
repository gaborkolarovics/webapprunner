package hu.polidor.webapprunner.common;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import static hu.polidor.webapprunner.Const.TAG;

/**
 * Common util methods
 *
 * @author Gábor KOLÁROVICS
 * @since 2018.11.19
 */
public final class Utils {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private static final int ONE_DAY = 24 * 60 * 60 * 1000;

    /**
     * Private constructor with exception because is a util class
     */
    private Utils() {
        throw new IllegalStateException("Utility class!");
    }

    /**
     * Check the GooglePlayService is available
     *
     * @param activity Current context
     * @return boolean
     */
    public static boolean isPlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            return false;
        }
        return true;
    }

    /**
     * Generate unique device Id
     *
     * @return String
     */
    public static String getUUID() {
        final String cpuAbi = getABIs();
        final String szDevIDShort = "35" +
                (Build.BOARD.length() % 10) +
                (Build.BRAND.length() % 10) +
                (cpuAbi.length() % 10) +
                (Build.DEVICE.length() % 10) +
                (Build.MANUFACTURER.length() % 10) +
                (Build.MODEL.length() % 10) +
                (Build.PRODUCT.length() % 10);
        return new UUID(szDevIDShort.hashCode(), getBuildSerial().hashCode()).toString();
    }

    /**
     * Get first supported Application Binary Interface (ABI)
     *
     * @return String supported ABIs
     */
    private static String getABIs() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.SUPPORTED_ABIS.length > 0) {
            return Build.SUPPORTED_ABIS[0];
        }
        return "NOT_FOUND_SUPPORTED_ABI";
    }

    /**
     * Check the target date has past after threshold day
     *
     * @param targetDate The checked date
     * @param threshold  The day count
     * @return boolean
     */
    public static boolean isOverDate(long targetDate, int threshold) {
        return new Date().getTime() - targetDate >= threshold * ONE_DAY;
    }

    /**
     * Set preference summary
     *
     * @param preferenceFregment The actual fregment
     * @param id                 The modified preference
     * @param summary            The content
     * @param enabled            The preference has enabled
     */
    public static void setSummary(final PreferenceFragment preferenceFregment, final String id, final String summary, final boolean enabled) {
        Preference preference = preferenceFregment.findPreference(id);
        preference.setSummary(summary);
        preference.setEnabled(enabled);
    }

    /**
     * Create long toast message, and logged it (info)
     *
     * @param context The parent application context or activity
     * @param msg     Message when showing it
     */
    public static void makeMsg(final Context context, final String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        Log.i(TAG, msg);
    }

    /**
     * If get Build SERIAL if possible
     *
     * @return Build SERAL
     */
    private static String getBuildSerial() {
        try {
            return Build.class.getField("SERIAL").get(null).toString();
        } catch (Exception exception) {
            return UUID.randomUUID().toString();
        }
    }

}
