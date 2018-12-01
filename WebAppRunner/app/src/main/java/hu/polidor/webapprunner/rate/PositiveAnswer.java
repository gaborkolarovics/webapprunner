package hu.polidor.webapprunner.rate;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import hu.polidor.webapprunner.common.PreferenceHelper;
import hu.polidor.webapprunner.common.Utils;

import static com.google.android.gms.common.GooglePlayServicesUtil.GOOGLE_PLAY_STORE_PACKAGE;

/**
 * Positive answer to applicaton rate question
 *
 * @author Gábor KOLÁROVICS
 * @since 2018.11.28
 */
public class PositiveAnswer implements DialogInterface.OnClickListener
{

	private Activity activity;

	public PositiveAnswer(final Activity activity)
	{
		this.activity = activity;
	}

	/**
	 * Google play url prefix
	 */
	private static final String GOOGLE_PLAY = "https://play.google.com/store/apps/details?id=";

	@Override
	public void onClick(DialogInterface dialogInterface, int action)
	{
		PreferenceHelper.setRateDialogApproved(activity, true);
		String packageName = activity.getPackageName();
		Intent intent = new Intent(Intent.ACTION_VIEW, packageName == null ? null : Uri.parse(GOOGLE_PLAY + packageName));
		if (Utils.checkPlayServices(activity))
		{
			intent.setPackage(GOOGLE_PLAY_STORE_PACKAGE);
		}
		activity.startActivity(intent);
	}

}
