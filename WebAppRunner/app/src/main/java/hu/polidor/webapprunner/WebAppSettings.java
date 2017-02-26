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

            String licenseType = getPreferenceManager().getSharedPreferences().getString(MainActivity.WEBAPP_CONFIG_LICENSETYPE, "Demo");
            Preference prefLicenseType = findPreference(MainActivity.WEBAPP_CONFIG_LICENSETYPE);
            prefLicenseType.setSummary(licenseType);
            prefLicenseType.setEnabled(false);

            String licenseDate = getPreferenceManager().getSharedPreferences().getString(MainActivity.WEBAPP_CONFIG_LICENSEDATE, "n/a");
            Preference prefLicenseDate = findPreference(MainActivity.WEBAPP_CONFIG_LICENSEDATE);
            prefLicenseDate.setSummary(licenseDate);
            prefLicenseDate.setEnabled(false);

            String licenseLastChk = getPreferenceManager().getSharedPreferences().getString(MainActivity.WEBAPP_CONFIG_LICENSECHK, "");
            Preference prefLicenseChk = findPreference(MainActivity.WEBAPP_CONFIG_LICENSECHK);
            prefLicenseChk.setSummary(licenseLastChk);
            prefLicenseChk.setEnabled(false);
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
