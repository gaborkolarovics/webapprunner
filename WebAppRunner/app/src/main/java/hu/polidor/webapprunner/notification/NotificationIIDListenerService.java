package hu.polidor.webapprunner.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import hu.polidor.webapprunner.Const;
import hu.polidor.webapprunner.MainActivity;
import hu.polidor.webapprunner.R;
import hu.polidor.webapprunner.common.PreferenceHelper;

import static hu.polidor.webapprunner.Const.TAG;

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
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        Log.d(TAG, "Recieve id: " + remoteMessage.getMessageId());
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Recieve body: " + remoteMessage.getNotification().getBody());
        }

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (notificationManager != null && remoteMessage.getNotification() != null) {
            sendNotification(notificationManager, remoteMessage.getNotification().getBody());
        }

        super.onMessageReceived(remoteMessage);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(final NotificationManager notificationManager, final String messageBody) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder notBuilder = new Notification.Builder(this)
                .setContentTitle("contentTitle")
                .setContentText(messageBody)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(false)
                .setContentIntent(pendingIntent);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notBuilder.setChannelId(Const.FCM_CHANEL_ID);
        }

        notificationManager.notify(0 /* ID of notification */, notBuilder.build());
    }

}
