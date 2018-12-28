package hu.polidor.webapprunner.widget;

import android.appwidget.AppWidgetProvider;
import android.content.Context;

/**
 * HttpGet widget provider (only job schedule)
 *
 * @author Gábor KOLÁROVICS
 * @since 2018.12.26
 */
public class HttpGetProvider extends AppWidgetProvider
{

	@Override
	public void onEnabled(Context context)
	{
		HttpGetJob.scheduleJob();
		super.onEnabled(context);
	}

}
