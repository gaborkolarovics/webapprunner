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
public final class AppRate
{
	/**
	 * Singleton AppRate class
	 */
	private static AppRate appRate;

	/**
	 * Public constructor
	 */
	public static AppRate getInstance()
	{
		if (appRate == null)
		{
			synchronized (AppRate.class) 
			{
				if (appRate == null)
				{
					appRate = new AppRate();
				}
			}
		}
		return appRate;
	}

	/**
	 * Check AppRate conditions to dialog
	 *
	 * @param activity Aplocation activity for condition check
	 */
	public static void ShowRateDialogIfConditionsApply(final Activity activity)
	{
		if (!activity.isFinishing())
		{
			if (!PreferenceHelper.getRateDialogApproved(activity)
				&& (Utils.isOverDate(PreferenceHelper.getLastRemindDate(activity), 1) || PreferenceHelper.getLastRemindDate(activity) == 0L)
				&& Utils.isOverDate(PreferenceHelper.getInstallDate(activity), 5)
				&& PreferenceHelper.getLaunchCount(activity) > 10)
			{
				DialogManager.show(activity);
			}
		}
	}

	/**
	 * Monitoring application start
	 *
	 * @paran context Application context for monitoring
	 */
	public void monitor(final Context context)
	{
		PreferenceHelper.getInstallDate(context);
		PreferenceHelper.increaseLaunchCount(context);
	}

}
