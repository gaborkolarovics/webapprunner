package hu.polidor.webapprunner;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import hu.polidor.webapprunner.common.PreferenceHelper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static hu.polidor.webapprunner.Const.TAG;

public class ServiceRegistrationIntent extends IntentService
{

	public String getAppVersion()
	{
		try
		{
			return getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        }
		catch (PackageManager.NameNotFoundException e)
		{
            Log.e(TAG, "Package manager not found!", e);
			return "";
        }
	}

    public ServiceRegistrationIntent()
	{
		super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent)
	{
        try
		{
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
											   GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            sendRegistrationToServer(token); 
        }
		catch (Exception e)
		{
            e.printStackTrace();
        }
    }

    private void sendRegistrationToServer(String token)
	{
        try
		{
            URL url = new URL(MainActivity.WEBAPP_SERVER_URL + "/api.php");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String urlParameters = "action=register" + 
				"&" + MainActivity.WEBAPP_CONFIG_DEVICEID + "=" + PreferenceHelper.getDeviceId(this) + 
				"&tokenid=" + token +
				"&" + MainActivity.WEBAPP_CONFIG_LICENSEPRGVER + "=" + getAppVersion() +
				"&" + MainActivity.WEBAPP_CONFIG_LICENSETYPE + "=playstore";
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
			{
                result.append(line);
            }
        }
		catch (IOException e)
		{
            e.printStackTrace();
        }
    }
}
