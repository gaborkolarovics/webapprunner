package hu.polidor.webapprunner.widget;

import static com.android.volley.Request.Method.GET;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import hu.polidor.webapprunner.R;
import hu.polidor.webapprunner.common.PreferenceHelper;

/**
 * HttpGet widget scheduled job manager
 *
 * @author Gábor KOLÁROVICS
 * @since 2018.12.26
 */
public class HttpGetJob extends Job {

    /**
     * Job ID
     */
    public static final String JOB_TAG = "widget_httpget_sync_job";

    /**
     * Scheduled HttpGet widget job
     */
    public static void scheduleJob() {

        Set<JobRequest> jobRequests = JobManager.instance().getAllJobRequestsForTag(JOB_TAG);
        if (!jobRequests.isEmpty()) {
            return;
        }

        new JobRequest.Builder(JOB_TAG).setPeriodic(TimeUnit.MINUTES.toMillis(15), TimeUnit.MINUTES.toMillis(5)).setUpdateCurrent(true).setRequiredNetworkType(JobRequest.NetworkType.CONNECTED).setRequirementsEnforced(true).build().schedule();
    }

    /**
     * Updace all HttpGet widget
     *
     * @param context Current application / widget context
     */
    public static void updateWidgets(final Context context) {

        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        ComponentName name = new ComponentName(context, HttpGetProvider.class);
        int[] appWidgetIds = manager.getAppWidgetIds(name);

        RequestQueue queue = Volley.newRequestQueue(context);

        for (int id : appWidgetIds) {

            String wdgTitle = PreferenceHelper.getHttpGetWidgetUrl(context, id);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_httpget);
            views.setTextViewText(R.id.widget_httpget_title, wdgTitle);
            views.setTextColor(R.id.widget_httpget_title, Color.parseColor("#ffffff"));
            views.setTextColor(R.id.widget_httpget_content, Color.parseColor("#ffffff"));
            views.setTextColor(R.id.widget_httpget_config, Color.parseColor("#ffffff"));

            if (!"".equals(wdgTitle)) {
                StringRequest stringRequest = new StringRequest(GET, wdgTitle, new HttpGetResponseListener(context, id), new HttpGetErrorResponseListener(context, id));
                queue.add(stringRequest);
            }

            Intent intent = new Intent(context, HttpGetConfigureActivity.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            int intentFlag = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                intentFlag = PendingIntent.FLAG_MUTABLE;
            }
            PendingIntent pending = PendingIntent.getActivity(context, 0, intent, intentFlag);
            views.setOnClickPendingIntent(R.id.widget_httpget_config, pending);

            manager.updateAppWidget(id, views);
        }

    }

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        updateWidgets(getContext());
        return Result.SUCCESS;
    }

}
