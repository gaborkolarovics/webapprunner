package hu.polidor.webapprunner.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import hu.polidor.webapprunner.R;
import hu.polidor.webapprunner.common.PreferenceHelper;

import static hu.polidor.webapprunner.common.Utils.setSummary;

/**
 * Base application config
 *
 * @author Gábor KOLÁROVICS
 * @since 2018.12.28
 */
public class AppConfig extends PreferenceFragment
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.appsettings);

		setSummary(this, "webappurl", PreferenceHelper.getUrl(getActivity()), true);
	}

}
