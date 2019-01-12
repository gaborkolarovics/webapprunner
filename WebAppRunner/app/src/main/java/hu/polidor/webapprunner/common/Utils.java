package hu.polidor.webapprunner.common;

import android.app.Activity;
import android.os.Build;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.Date;
import java.util.UUID;

/**
 * Common util methods
 *
 * @author Gábor KOLÁROVICS
 * @since 2018.11.19
 */
public final class Utils
{

	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	/**
	 * Check the GooglePlayService is available
	 *
	 * @param Activity Current context
	 * @return boolean
	 */
	public static boolean checkPlayServices(Activity activity)
	{
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS)
		{
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
			{
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity, PLAY_SERVICES_RESOLUTION_REQUEST).show();
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
	public static String getUUID()
	{
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);
        String serial;
        try
		{
            serial = Build.class.getField("SERIAL").get(null).toString();
        }
		catch (Exception exception)
		{
            serial = UUID.randomUUID().toString();
        }
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

	/**
	 * Check the target date has past after threshold day
	 *
	 * @param targetDate The checked date
	 * @param treshold The day count
	 * @return boolean
	 */
	public static boolean isOverDate(long targetDate, int threshold)
	{
        return new Date().getTime() - targetDate >= threshold * 24 * 60 * 60 * 1000;
    }

	/**
	 * Set preference summary
	 *
	 * @param preferenceFragment The actual fregment
	 * @param id The modified preference
	 * @param summary The content
	 * @param enabled The preference has enabled
	 */
	public static void setSummary(final PreferenceFragment preferenceFregment, final String id, final String summary, final boolean enabled)
	{
		Preference preference  = preferenceFregment.findPreference(id);
		preference.setSummary(summary);
		preference.setEnabled(enabled);
	}

}
