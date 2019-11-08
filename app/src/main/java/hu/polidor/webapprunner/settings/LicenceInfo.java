package hu.polidor.webapprunner.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import hu.polidor.webapprunner.R;
import hu.polidor.webapprunner.common.PreferenceHelper;

import static hu.polidor.webapprunner.common.Utils.setSummary;

/**
 * Non modifiable device settings
 *
 * @author Gábor KOLÁROVICS
 * @since 2018.12.28
 */
public class LicenceInfo extends PreferenceFragment
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.licenseinfo);

		setSummary(this, "deviceid", PreferenceHelper.getDeviceId(getActivity()), false);
		setSummary(this, "rate_install_date", PreferenceHelper.getInstallDateForHumans(getActivity()), false);
		setSummary(this, "rate_dialog_approved", PreferenceHelper.getRateDialogApproved(getActivity()) ?
			getResources().getString(R.string.base_yes) : getResources().getString(R.string.base_no), false);
		setSummary(this, "rate_apprun_count", Integer.toString(PreferenceHelper.getLaunchCount(getActivity())), false);
		setSummary(this, "c2m_token", PreferenceHelper.getC2mToken(getActivity()), false);
	}

}
