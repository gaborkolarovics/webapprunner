package hu.polidor.webapprunner.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import hu.polidor.webapprunner.R;
import hu.polidor.webapprunner.common.PreferenceHelper;

/**
 * Configure HttpGet widget
 *
 * @author Gábor KOLÁROVICS
 * @since 2018.12.24
 */
public class HttpGetConfigureActivity extends Activity
{

	/**
	 * Widget url
	 */
	private EditText etUrl;

	/**
	 * Save button
	 */
	private Button btnSave;

	/**
	 * Widget id
	 */
	private Integer widgetId;

	/**
	 * Singleton Url edit text
	 */
	public EditText getEtUrl()
	{
		if (etUrl == null)
		{
			etUrl = findViewById(R.id.widget_httpget_et_url);
		}
		return etUrl;
	}

	/**
	 * Singleton save button
	 */
	private Button getBtnSave()
	{
		if (btnSave == null)
		{
			btnSave = findViewById(R.id.widget_httpget_btn_ok);
		}
		return btnSave;
	}

	/**
	 * Singleton widget id
	 */
	public int getWidgetId()
	{
		if (widgetId == null)
		{
			final Intent intent = getIntent();
			final Bundle extras = intent.getExtras();
			if (extras == null)
			{
				return AppWidgetManager.INVALID_APPWIDGET_ID;
			}
			widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		return widgetId;
	}

    @Override
    public void onCreate(Bundle icicle)
	{
        super.onCreate(icicle);
        setResult(RESULT_OK);
        setContentView(R.layout.widget_httpget_configure);

		if (getWidgetId() == AppWidgetManager.INVALID_APPWIDGET_ID)
		{
			Toast.makeText(this, R.string.widget__invalid_widget_id, Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		getEtUrl().setText(PreferenceHelper.getHttpGetWidgetUrl(this, getWidgetId()));
		getBtnSave().setOnClickListener(new HttpGetConfigureListener(this));
    }

}
