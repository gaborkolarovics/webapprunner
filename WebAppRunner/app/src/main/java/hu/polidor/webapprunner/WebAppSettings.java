package hu.polidor.webapprunner;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import java.util.List;

public class WebAppSettings extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBuildHeaders(List<PreferenceActivity.Header> target) {
        loadHeadersFromResource(R.xml.header, target);
    }

    public static class LicencInfo extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.licenseinfo);

            String deviceId = getPreferenceManager().getSharedPreferences().getString(MainActivity.WEBAPP_CONFIG_DEVICEID, "ID not set!");
            Preference prefDeviceId = findPreference(MainActivity.WEBAPP_CONFIG_DEVICEID);
            prefDeviceId.setSummary(deviceId);
            prefDeviceId.setEnabled(false);
        }

    }

    public static class appSettings extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.appsettings);
			
			String appURL = getPreferenceManager().getSharedPreferences().getString(MainActivity.WEBAPP_CONFIG_URL, "");
            Preference prefAppURL = findPreference(MainActivity.WEBAPP_CONFIG_URL);
            prefAppURL.setSummary(appURL);
        }

    }
}
