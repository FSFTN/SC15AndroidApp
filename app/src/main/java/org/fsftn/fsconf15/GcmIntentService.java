package org.fsftn.fsconf15;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

public class GcmIntentService extends IntentService {
    String TAG = "fsconf";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("FSCONF15","Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("FSCONF15","Deleted messages on server: " +extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                sendNotification(extras.getString("title"),extras.getString("content"));
            }
        }
        Log.i(extras.getString("title"),extras.getString("content"));
        addNotification(extras.getString("title"), extras.getString("content"));
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String title, String msg) {
        int NOTIFICATION_ID = this.getSharedPreferences("org.fsftn.sc15",Context.MODE_PRIVATE).getInt("notificationCount",1234);
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, NotificationsActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(title)
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        NOTIFICATION_ID++;
        this.getSharedPreferences("org.fsftn.sc15",Context.MODE_PRIVATE).edit().putInt("notificationCount",NOTIFICATION_ID).commit();
    }

    public void addNotification(String title, String content) {
        try {
            SharedPreferences sp = this.getSharedPreferences("org.fsftn.sc15", Context.MODE_PRIVATE);
            String s = sp.getString("notifications", null);
            JSONArray notifications = new JSONArray();
            if(s!=null) {
                notifications = new JSONArray(s);
            }
            Log.i(TAG,notifications.toString());
            JSONObject newNotification = new JSONObject();
            newNotification.put("title",title);
            newNotification.put("content",content);
            notifications.put(newNotification.toString());
            sp.edit().putString("notifications",notifications.toString()).commit();
            Log.i(TAG,notifications.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
