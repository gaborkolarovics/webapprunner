package hu.polidor.webapprunner.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import hu.polidor.webapprunner.common.PreferenceHelper;

/**
 * HttpGet widget config save listener
 *
 * @author Gábor KOLÁROVICS
 * @since 2018.12.24
 */
public class HttpGetConfigureListener implements OnClickListener
{

	/**
	 * Parent activity
	 */
	private HttpGetConfigureActivity httpGetConfigure;

	/**
	 * Constructor with parent activity
	 */
	public HttpGetConfigureListener(final HttpGetConfigureActivity httpGetConfigure)
	{
		this.httpGetConfigure = httpGetConfigure;
	}

	@Override
	public void onClick(View view)
	{
		PreferenceHelper.setHttpGetWidgetUtl(httpGetConfigure, httpGetConfigure.getWidgetId(), httpGetConfigure.getEtUrl().getText().toString());

		HttpGetJob.updateWidgets(httpGetConfigure);

		final Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, httpGetConfigure.getWidgetId());
		httpGetConfigure.setResult(Activity.RESULT_OK, resultValue);
		httpGetConfigure.finish();
	}

}
