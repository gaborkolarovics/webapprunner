package hu.polidor.webapprunner;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class WebAppLicenseSender extends AsyncTask<String, Integer, String>
{
    Context appContext;

    public WebAppLicenseSender(Context context)
	{
        this.appContext = context;
    }

    @Override
    protected String doInBackground(String[] p1)
	{
        JSONObject jsonObject = new JSONObject();
        try
		{
            jsonObject.put(MainActivity.WEBAPP_CONFIG_DEVICEID, p1[0]);
            jsonObject.put(MainActivity.WEBAPP_CONFIG_LICENSEPRGVER, MainActivity.AppVersion);
            jsonObject.put(MainActivity.WEBAPP_CONFIG_URL, MainActivity.appurl);
        }
		catch (JSONException e)
		{
            e.printStackTrace();
			return "";
        }

		String base64 = "";
		MCrypt mcrypt = new MCrypt();
		try
		{
			String encrypted = MCrypt.bytesToHex(mcrypt.encrypt(jsonObject.toString()));
			if (!encrypted.isEmpty())
			{
				byte[] data = encrypted.getBytes("UTF-8");
				base64 = Base64.encodeToString(data, Base64.DEFAULT);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "";
		}
		if (base64.isEmpty())
		{
			return "";
		}

		HttpURLConnection connection = null;
		try
		{
			//Create connection
			String urlParameters = "data=" + URLEncoder.encode(base64, "UTF-8");
			URL url = new URL(MainActivity.WEBAPP_DEFAULT_URL + "/api.php");
			//URL url = new URL("http://127.0.0.1:8080/webapprunner/api.php");
            connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", "" +  Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");  
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			//Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			//Get Response	
			InputStream is = connection.getInputStream();
			return convertStreamToString(is);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "";
		}
		finally
		{

			if (connection != null)
			{
				connection.disconnect(); 
			}
		}
    }

    @Override
    protected void onPostExecute(String result)
	{
        MCrypt mcrypt = new MCrypt();
        byte[] data64 = null;
        try
		{
            data64 = Base64.decode(result, Base64.DEFAULT);
        }
		catch (Exception e)
		{
            e.printStackTrace();
        }
        if (data64 != null)
		{
            try
			{
				String r = new String(data64, "UTF-8");
                result = new String(mcrypt.decrypt(r));
            }
			catch (Exception e)
			{
                e.printStackTrace();
            }
            Intent intent = new Intent(MainActivity.WEBAPP_INTENT_LICENSERESP);
            intent.putExtra(MainActivity.WEBAPP_INTENT_DATANAME, result);
            LocalBroadcastManager.getInstance(appContext).sendBroadcast(intent);
        }
    }

    private String convertStreamToString(InputStream is)
	{
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try
		{
            while ((line = reader.readLine()) != null)
			{
                sb.append(line).append("\n");
            }
        }
		catch (IOException e)
		{
            e.printStackTrace();
        }
		finally
		{
            try
			{
                is.close();
            }
			catch (IOException e)
			{
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
