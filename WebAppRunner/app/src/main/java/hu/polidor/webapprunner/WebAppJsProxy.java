package hu.polidor.webapprunner;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.webkit.JavascriptInterface;

import com.google.zxing.integration.android.IntentIntegrator;

import hu.polidor.webapprunner.common.PreferenceHelper;
import hu.polidor.webapprunner.nfc.NfcReaderActivity;
import hu.polidor.webapprunner.sign.CaptureSignature;

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
        return PreferenceHelper.getDeviceId(activity);
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
		return PreferenceHelper.getUrl(activity);
	}

	@JavascriptInterface
    public void setURL(String url)
	{
		PreferenceHelper.setUrl(activity, url);
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

		if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
		{
			ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
		}
		else
		{
			Intent intent = new Intent(activity, WebAppFineLocation.class);
			startLocationActivity(intent);
		}
    }

	@JavascriptInterface
    public void getFineLocation(int minDistance, int minTimeout)
	{
		if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
		{
			ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
		}
		else
		{
			minDistance = Math.min(minDistance, 500);
			minDistance = Math.max(minDistance, 5);
			minTimeout = Math.min(minTimeout, 60);
			minTimeout = Math.max(minTimeout, 1);
			Intent intent = new Intent(activity, WebAppFineLocation.class);
			intent.putExtra(MainActivity.FINELOCATION_MINDIST, minDistance);
			intent.putExtra(MainActivity.FINELOCATION_MINTIME, minTimeout);
			startLocationActivity(intent);
		}
    }

	@JavascriptInterface
    public void scanBarcode()
	{
        IntentIntegrator scanIntegrator = new IntentIntegrator(activity);
        scanIntegrator.initiateScan();
    }

	@JavascriptInterface
	public void scanNfc()
	{
		Intent intent = new Intent(activity, NfcReaderActivity.class);
        activity.startActivityForResult(intent, MainActivity.NFCREADER_ACTIVITY);
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

	/*
	 * Start location activity with parameterized intent
	 * @param intent
	 */
	private void startLocationActivity(final Intent intent)
	{
		activity.startActivityForResult(intent, MainActivity.LOCATION_ACTIVITY);
	}
}
