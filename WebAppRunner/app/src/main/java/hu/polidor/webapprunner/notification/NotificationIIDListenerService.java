package hu.polidor.webapprunner.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import hu.polidor.webapprunner.MainActivity;
import hu.polidor.webapprunner.R;
import hu.polidor.webapprunner.common.PreferenceHelper;

import static hu.polidor.webapprunner.Const.TAG;

/**
 * Push notification and device token reciever service
 *
 * @author Gábor KOLÁROVICS
 * @since 2019.08.24
 */
public class NotificationIIDListenerService extends FirebaseMessagingService
{

	@Override
	public void onNewToken(String token)
	{
		super.onNewToken(token);
		Log.d(TAG, "Get new token [NotificationIIDListenerService]: " + token);
		PreferenceHelper.setC2mToken(this, token);
	}

	@Override
	public void onMessageReceived(final RemoteMessage remoteMessage)
	{
		Log.d(TAG, "Recieve id: " + remoteMessage.getMessageId());
		Log.d(TAG, "Recieve type: " + remoteMessage.getMessageType());
		Log.d(TAG, "Recieve body: " + remoteMessage.getNotification().getBody());

		sendNotification(remoteMessage.getNotification().getBody());

		super.onMessageReceived(remoteMessage);
	}

	/**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(final String messageBody)
	{
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
			PendingIntent.FLAG_ONE_SHOT);

        String channelId = "default"; // getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
			new NotificationCompat.Builder(this) //channelId
			.setContentTitle("contentTitle")
			.setContentText(messageBody)
			.setSmallIcon(R.drawable.ic_launcher)
			.setAutoCancel(false)
			.setSound(defaultSoundUri)
			.setContentIntent(pendingIntent);

        NotificationManager notificationManager =
			(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
            NotificationChannel channel = new NotificationChannel(channelId,
				"Channel human readable title",
				NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

		notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
