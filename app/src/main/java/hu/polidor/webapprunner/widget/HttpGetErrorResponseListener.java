package hu.polidor.webapprunner.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.widget.RemoteViews;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import hu.polidor.webapprunner.R;

/**
 * HttpGet widget error response resolver
 *
 * @author Gábor KOLÁROVICS
 * @since 2018.12.28
 */
public class HttpGetErrorResponseListener implements Response.ErrorListener
{

	/**
	 * Parent context
	 */
	private Context context;

	/**
	 * Request widget id
	 */
	private int wdgId;

	/**
	 * Constructor with context and widget id
	 *
	 * @param context Widget context
	 * @param wdgId Current widget id
	 */
	public HttpGetErrorResponseListener(Context context , int wdgId)
	{
		this.context = context;
		this.wdgId = wdgId;
	}

	@Override
	public void onErrorResponse(VolleyError error)
	{
		String errorMsg = error.getMessage();

		NetworkResponse netwotkResponse = error.networkResponse;
		if (netwotkResponse != null)
		{
			errorMsg = String.format("(%d) %s", netwotkResponse.statusCode, netwotkResponse.data);
		}

		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_httpget);
		views.setTextViewText(R.id.widget_httpget_content, errorMsg);
		manager.updateAppWidget(wdgId, views);
	}

}
