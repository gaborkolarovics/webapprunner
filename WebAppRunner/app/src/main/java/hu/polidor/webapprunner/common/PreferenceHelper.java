package hu.polidor.webapprunner.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.util.Date;

/**
 * Prefrences manager class with default value
 *
 * @author Gábor KOLÁROVICS
 * @since 2018.11.16
 */
public class PreferenceHelper
{
	public static final String WEBAPP_DEFAULT_URL = "file:///android_asset/index.html";

	private static final String CONFIG_FULLSCREEN = "fullscreen";
	private static final String CONFIG_DEVICEID = "deviceid";
	private static final String CONFIG_URL = "webappurl";
	private static final String CONFIG_RATE_DIALOG_APPROVED = "ratedlgapproved";
	private static final String CONFIG_RATE_LAST_REMIND_DATE = "ratelastreminddate";
	private static final String CONFIG_RATE_LAUNCH_COUNT = "ratelaunchcount";
	private static final String CONFIG_RATE_INSTALL_DATE = "rateinstalldate";

	/**
	 * Privage constructor
	 */
	private PreferenceHelper()
	{
		throw new UnsupportedOperationException("Private constructor!");
	}

	/**
	 * Shared preferences
	 *
	 * @param context Application context
	 */
	private static SharedPreferences getPreferences(final Context context)
	{
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	/**
	 * SharedPreferences editor
	 *
	 * @param context Application context
	 */
	private static Editor getPreferencesEditor(final Context context)
	{
		return getPreferences(context).edit();
	}

	/**
	 * Get application fullscreen setting
	 */
	public static boolean isFullScreenApp(final Context context)
	{
		return getPreferences(context).getBoolean(CONFIG_FULLSCREEN, false);
	}

	/**
	 * Get unique device id (Generate, if not exist)
	 */
	public static String getDeviceId(final Context context)
	{
		String deviceId = getPreferences(context).getString(CONFIG_DEVICEID, "");
		if (deviceId.isEmpty())
		{
			deviceId = Utils.getUUID();
			getPreferencesEditor(context).putString(CONFIG_DEVICEID, deviceId).apply();
		}
		return deviceId;
	}

	/**
	 * Get configured url (default is webapp default url)
	 */
	public static String getUrl(final Context context)
	{
		final String url = getPreferences(context).getString(CONFIG_URL, WEBAPP_DEFAULT_URL);
		if (url.isEmpty())
		{
			return WEBAPP_DEFAULT_URL;
		}
		return url;
	}

	/**
	 * Set application url
	 */
	public static void setUrl(final Context context, final String url)
	{
		getPreferencesEditor(context).putString(CONFIG_URL, url).apply();
	}

	/**
	 * Rate dialog is approved (positive or negative)
	 */
	public static boolean getRateDialogApproved(final Context context)
	{
		return getPreferences(context).getBoolean(CONFIG_RATE_DIALOG_APPROVED, false);
	}

	/**
	 * Set rate dialog approved status
	 */
	public static void setRateDialogApproved(final Context context, final boolean value)
	{
		getPreferencesEditor(context).putBoolean(CONFIG_RATE_DIALOG_APPROVED, value).apply();
	}

	/**
	 * Get last neutral andsver date (remind later)
	 */
	public static long getLastRemindDate(final Context context)
	{
		return getPreferences(context).getLong(CONFIG_RATE_LAST_REMIND_DATE, 0L);
	}

	/**
	 * Set neutral ansver (remind later)
	 */
	public static void setLastRemindDate(final Context context)
	{
		getPreferencesEditor(context).putLong(CONFIG_RATE_LAST_REMIND_DATE, new Date().getTime()).apply();
	}

	/**
	 * Get launched count
	 */
	public static int getLaunchCount(final Context context)
	{
		return getPreferences(context).getInt(CONFIG_RATE_LAUNCH_COUNT, 0);
	}

	/**
	 * Increase launched count
	 */
	public static void increaseLaunchCount(final Context context)
	{
		getPreferencesEditor(context).putInt(CONFIG_RATE_LAUNCH_COUNT, getLaunchCount(context) + 1).apply();
	}

	/**
	 * Get install date (default is first get date)
	 */
	public static long getInstallDate(final Context context)
	{
		long date = getPreferences(context).getLong(CONFIG_RATE_INSTALL_DATE, 0L);
		if (date == 0L)
		{
			date = new Date().getTime();
			getPreferencesEditor(context).putLong(CONFIG_RATE_INSTALL_DATE, date).apply();
		}
		return date;
	}
}
