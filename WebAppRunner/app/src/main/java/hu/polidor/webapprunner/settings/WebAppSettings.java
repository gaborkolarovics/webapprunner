package hu.polidor.webapprunner.settings;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import hu.polidor.webapprunner.common.PreferenceHelper;
import hu.polidor.webapprunner.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Application settings activity
 *
 * @author Gábor KOLÁROVICS
 * @since 2018.12.28
 */
public class WebAppSettings extends PreferenceActivity
{

	/**
	 * Fragment injection vulnerability (Api19)
	 */
	private static List<String> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBuildHeaders(List<PreferenceActivity.Header> target)
	{
        loadHeadersFromResource(R.xml.header, target);
		fragments.clear();
        for (Header header : target)
		{
            fragments.add(header.fragment);
        }
    }

	@Override
	protected boolean isValidFragment(String fragmentName)
	{
		return fragments.contains(fragmentName);
	}

}
