package hu.polidor.webapprunner.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import hu.polidor.webapprunner.Const;
import hu.polidor.webapprunner.MainActivity;
import hu.polidor.webapprunner.R;
import hu.polidor.webapprunner.common.PreferenceHelper;

/**
 * Push notification and device token receiver service
 *
 * @author Gábor KOLÁROVICS
 * @since 2019.08.24
 */
public class NotificationIIDListenerService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull final String token) {
        super.onNewToken(token);
        PreferenceHelper.setC2mToken(this, token);
    }

    @Override
    public void onMessageReceived(@NonNull final RemoteMessage remoteMessage) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            sendNotification(notificationManager, remoteMessage);
        }
        super.onMessageReceived(remoteMessage);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param notificationManager current notification manager
     * @param remoteMessage        FCM message body received.
     */
    private void sendNotification(final NotificationManager notificationManager, final RemoteMessage remoteMessage) {

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        if (notification == null) {
            return;
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (remoteMessage.getData().get("link") != null) {
            intent.putExtra(MainActivity.WEBAPP_INTENT_URL, remoteMessage.getData().get("link"));
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setSmallIcon(R.drawable.ic_stat_notification)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setChannelId(Const.FCM_CHANEL_ID);

            NotificationChannel mChannel = new NotificationChannel(Const.FCM_CHANEL_ID, getString(R.string.fcm_chanel_name), NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setDescription(getString(R.string.fcm_chanel_description));
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            notificationManager.createNotificationChannel(mChannel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }

}
