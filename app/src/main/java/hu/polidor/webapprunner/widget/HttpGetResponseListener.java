package hu.polidor.webapprunner.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.text.Html;
import android.widget.RemoteViews;

import com.android.volley.Response;

import hu.polidor.webapprunner.R;

/**
 * HttpGet widget response resolver
 *
 * @author Gábor KOLÁROVICS
 * @since 2018.12.26
 */
public class HttpGetResponseListener implements Response.Listener<String> {

    /**
     * Parent context
     */
    private final Context context;

    /**
     * Request widget id
     */
    private final int wdgId;

    /**
     * Constructor with context and widget id
     *
     * @param context Widget context
     * @param wdgId   Current widget id
     */
    public HttpGetResponseListener(Context context, int wdgId) {
        this.context = context;
        this.wdgId = wdgId;
    }

    @Override
    public void onResponse(String response) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_httpget);
        views.setTextViewText(R.id.widget_httpget_content, Html.fromHtml(response));
        manager.updateAppWidget(wdgId, views);
    }

}
