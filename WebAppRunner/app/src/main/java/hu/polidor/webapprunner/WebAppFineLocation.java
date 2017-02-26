package hu.polidor.webapprunner;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class WebAppFineLocation extends Activity implements LocationListener
{

    LocationManager lm;
    Location loc;
    Button btnOk, btnCancel;
    TextView tvLoc, tvTime, tvAcc;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    int minDistance;
    int minTimeout;

    private Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run()
		{
            minTimeout = minTimeout - 1;
            if (minTimeout == 0)
			{
                if (loc != null)
				{
                    ShowLocation();
                    btnOk.setEnabled(true);
                }
				else
				{
                    tvTime.setText(R.string.please_wait);
                }
            }
			else
			{
                tvTime.setText(String.format(getResources().getString(R.string.please_wait_time), minTimeout).toString());
                handler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);

        btnOk = (Button) findViewById(R.id.locget);
        btnCancel = (Button) findViewById(R.id.loccancel);
        tvAcc = (TextView) findViewById(R.id.tvAccuracy);
        tvLoc = (TextView) findViewById(R.id.tvLocation);
        tvTime = (TextView) findViewById(R.id.tvTime);

        btnCancel.setOnClickListener(new OnClickListener() {
				public void onClick(View v)
				{
					CancelLocation();
				}
			});

        btnOk.setEnabled(false);
        btnOk.setOnClickListener(new OnClickListener() {
				public void onClick(View v)
				{
					SaveLocation();
				}
			});

        minDistance = this.getIntent().getIntExtra(MainActivity.FINELOCATION_MINDIST, 50);
        minTimeout = this.getIntent().getIntExtra(MainActivity.FINELOCATION_MINTIME, 30);
        lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isNetworkEnabled && !isGPSEnabled)
		{
			MainActivity.makeMsg(getResources().getString(R.string.location_disabled));
            CancelLocation();
        }
		else
		{
            handler.postDelayed(runnable, 1000);
			tvLoc.setText("");
			tvTime.setText(String.format(getResources().getString(R.string.please_wait_time), minTimeout).toString());
            if (isNetworkEnabled)
			{
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                tvAcc.setText(R.string.netEnabled);
            }
            if (isGPSEnabled)
			{
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                tvAcc.setText(R.string.gpsEnabled);
            }
        }
    }

    @Override
    protected void onPause()
	{
        if (lm != null)
		{
            lm.removeUpdates(this);
        }
        super.onPause();
    }

    @Override
    public void onLocationChanged(Location p1)
	{
        if (handler != null)
		{
            handler.removeCallbacks(runnable);
            handler = null;
        }

        if (loc == null || loc.getTime() <= p1.getTime())
		{
            loc = p1;
            ShowLocation();
            if (loc.getAccuracy() <= minDistance)
			{
                SaveLocation();
            }
            btnOk.setEnabled(true);
        }
    }

    @Override
    public void onStatusChanged(String p1, int p2, Bundle p3)
	{
        // TODO: Implement this method
    }

    @Override
    public void onProviderEnabled(String p1)
	{
        // TODO: Implement this method
    }

    @Override
    public void onProviderDisabled(String p1)
	{
        // TODO: Implement this method
    }

    private void ShowLocation()
	{
		tvLoc.setText(String.format(getResources().getString(R.string.loc_coor), loc.getLongitude(), loc.getLatitude()).toString());
        tvTime.setText(format.format(loc.getTime()));
        tvAcc.setText(String.format(getResources().getString(R.string.loc_accu), loc.getAccuracy(), loc.getProvider()).toString());
    }

    private void CancelLocation()
	{
        Bundle b = new Bundle();
        b.putString(MainActivity.INTENT_STATUS, MainActivity.INTENT_STATUS_CANCEL);
        Intent intent = new Intent();
        intent.putExtras(b);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void SaveLocation()
	{
        JSONObject jsonObject = new JSONObject();
        try
		{
            jsonObject.put("provider", loc.getProvider());
            jsonObject.put("formatTime", format.format(loc.getTime()));
            jsonObject.put("accuracy", loc.getAccuracy());
            jsonObject.put("latitude", loc.getLatitude());
            jsonObject.put("longitude", loc.getLongitude());
            jsonObject.put("altitude", loc.getAltitude());
            jsonObject.put("time", loc.getTime());
        }
		catch (JSONException e)
		{
            e.printStackTrace();
        }
        Bundle b = new Bundle();
        b.putString(MainActivity.INTENT_STATUS, MainActivity.INTENT_STATUS_DONE);
        b.putString(MainActivity.FINELOCATION_LOCATION, jsonObject.toString());
        Intent intent = new Intent();
        intent.putExtras(b);
        setResult(RESULT_OK, intent);
        finish();
    }
}
