package hu.polidor.webapprunner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.webkit.JavascriptInterface;

import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONException;
import org.json.JSONObject;

public class WebAppJsProxy
{

    private Activity activity = null;

    public WebAppJsProxy(Activity activity)
	{
        this.activity = activity;
    }

	@JavascriptInterface
    public void showToast(String message)
	{
		MainActivity.makeMsg(message);
    }

	@JavascriptInterface
    public String getDeviceID()
	{
        return MainActivity.getUUID();
    }

	@JavascriptInterface
    public String getLicenseInfo()
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
		JSONObject jsonObject = new JSONObject();
        try
		{
            jsonObject.put(MainActivity.WEBAPP_CONFIG_LICENSETYPE, sharedPref.getString(MainActivity.WEBAPP_CONFIG_LICENSETYPE, ""));
			jsonObject.put(MainActivity.WEBAPP_CONFIG_LICENSEDATE, sharedPref.getString(MainActivity.WEBAPP_CONFIG_LICENSEDATE, ""));
        	jsonObject.put(MainActivity.WEBAPP_CONFIG_LICENSECHK, sharedPref.getString(MainActivity.WEBAPP_CONFIG_LICENSECHK, ""));
        }
		catch (JSONException e)
		{
            e.printStackTrace();
        }

		return jsonObject.toString();
    }

	@JavascriptInterface
    public String getURL()
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
		return sharedPref.getString(MainActivity.WEBAPP_CONFIG_URL, "");
	}

	@JavascriptInterface
    public void setURL(String url)
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
		sharedPref.edit().putString(MainActivity.WEBAPP_CONFIG_URL, url).apply();
	}

	@JavascriptInterface
    public void vibrate(String vibrateType)
	{
        String vibratePattern = "";
        if (vibrateType.contains("{"))
		{
            vibratePattern = vibrateType.substring(vibrateType.indexOf("{") + 1);
            vibratePattern = vibratePattern.substring(0, vibratePattern.indexOf("}"));
            vibrateType = vibrateType.substring(0, vibrateType.indexOf("{"));
        }

        Vibrator v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        switch (vibrateType)
		{
            case "SHORT":
                v.vibrate(300);
                break;
            case "LONG":
                v.vibrate(800);
                break;
            case "PATTERN":
                String arrstr[] = vibratePattern.split(",");
                long pattern[] = convertPattern(arrstr);
                v.vibrate(pattern, -1);
                break;
            default:
                v.vibrate(500);
        }
    }

	@JavascriptInterface
    public void getSignature()
	{
        Intent intent = new Intent(activity, CaptureSignature.class);
        activity.startActivityForResult(intent, MainActivity.SIGNATURE_ACTIVITY);
    }

	@JavascriptInterface
    public void getLocation()
	{
        Intent intent = new Intent(activity, WebAppFineLocation.class);
        activity.startActivityForResult(intent, MainActivity.LOCATION_ACTIVITY);
    }

	@JavascriptInterface
    public void getFineLocation(int minDistance, int minTimeout)
	{
        minDistance = Math.min(minDistance, 500);
        minDistance = Math.max(minDistance, 5);
        minTimeout = Math.min(minTimeout, 60);
        minTimeout = Math.max(minTimeout, 1);
        Intent intent = new Intent(activity, WebAppFineLocation.class);
        intent.putExtra(MainActivity.FINELOCATION_MINDIST, minDistance);
        intent.putExtra(MainActivity.FINELOCATION_MINTIME, minTimeout);
        activity.startActivityForResult(intent, MainActivity.LOCATION_ACTIVITY);
    }

	@JavascriptInterface
    public void scanBarcode()
	{
        IntentIntegrator scanIntegrator = new IntentIntegrator(activity);
        scanIntegrator.initiateScan();
    }

    private long[] convertPattern(String[] string)
	{
        long number[] = new long[string.length];
        for (int i = 0; i < string.length; i++)
		{
            try
			{
                number[i] = Integer.parseInt(string[i].replace(" ", ""));
            }
			catch (Exception e)
			{
                e.printStackTrace();
            }
        }
        return number;
    }
}
