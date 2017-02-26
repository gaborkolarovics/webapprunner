package hu.polidor.webapprunner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WebAppLicenseReceiver extends BroadcastReceiver
{
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private WebView webView;

    public WebAppLicenseReceiver(WebView view)
	{
        this.webView = view;
    }

    @Override
    public void onReceive(Context context, Intent intent)
	{
        String licenseResponse = intent.getStringExtra(MainActivity.WEBAPP_INTENT_DATANAME);
        try
		{
            JSONObject obj = new JSONObject(licenseResponse);
            String licenseType = obj.getString(MainActivity.WEBAPP_CONFIG_LICENSETYPE);
            String licenseDate = obj.getString(MainActivity.WEBAPP_CONFIG_LICENSEDATE);
            String licenseVer = obj.getString(MainActivity.WEBAPP_CONFIG_LICENSEVER);
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            sharedPrefs.edit().putString(MainActivity.WEBAPP_CONFIG_LICENSETYPE, licenseType).apply();
            sharedPrefs.edit().putString(MainActivity.WEBAPP_CONFIG_LICENSEDATE, licenseDate).apply();
            String deviceId = sharedPrefs.getString(MainActivity.WEBAPP_CONFIG_DEVICEID, "");
            try
			{
                Date date = format.parse(licenseDate);
                if (new Date().after(date))
				{
                    MainActivity.makeMsg(context.getResources().getString(R.string.license_error));
                    webView.loadUrl(MainActivity.WEBAPP_DEFAULT_URL + "/index.php?device=" + deviceId);
                }
				else
				{
                    if (licenseType.equalsIgnoreCase("unique") && !licenseVer.equalsIgnoreCase(MainActivity.AppVersion))
					{
                        MainActivity.makeMsg(String.format(context.getResources().getString(R.string.license_error_version), MainActivity.AppVersion));
                        webView.loadUrl(MainActivity.WEBAPP_DEFAULT_URL + "/index.php?device=" + deviceId);
                    }
					else
					{
                        String lastChk = format.format(new Date());
                        sharedPrefs.edit().putString(MainActivity.WEBAPP_CONFIG_LICENSECHK, lastChk).apply();
						MainActivity.makeMsg(String.format(context.getResources().getString(R.string.license_expire), licenseDate));
                    }
                }
            }
			catch (ParseException e)
			{
                e.printStackTrace();
            }
        }
		catch (JSONException e)
		{
            e.printStackTrace();
        }
    }
}
