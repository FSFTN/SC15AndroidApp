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

import java.util.Calendar;

public class GcmIntentService extends IntentService {
    String TAG = "fsconf_notif";
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
                sendNotification("FSCONF15","Send error: " + extras.toString(),"");
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("FSCONF15","Deleted messages on server: " +extras.toString(),"");
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                sendNotification(extras.getString("title"),extras.getString("content"),extras.getString("time_stamp"));
            }
        }


        Log.i(TAG,extras.getString("title")+" " + extras.getString("content")+ " @ " + extras.getString("time_stamp"));
        addNotification(extras.getString("title"), extras.getString("content"), extras.getString("time_stamp"));
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String title, String msg, String timestamp) {
        int NOTIFICATION_ID = this.getSharedPreferences("org.fsftn.sc15",Context.MODE_PRIVATE).getInt("notificationCount",1234);
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, NotificationsActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(title + " @" + timestamp)
                        .setContentText(msg);


        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        NOTIFICATION_ID++;
        this.getSharedPreferences("org.fsftn.sc15",Context.MODE_PRIVATE).edit().putInt("notificationCount",NOTIFICATION_ID).commit();
    }

    public void addNotification(String title, String content, String timestamp) {

        Log.i(TAG,"timestamp> "+timestamp);

        try {
            SharedPreferences sp = this.getSharedPreferences("org.fsftn.sc15", Context.MODE_PRIVATE);
            String s = sp.getString("notifications", null);
            JSONArray notifications = new JSONArray();
            if(s!=null) {
                notifications = new JSONArray(s);
            }
            Log.i(TAG, notifications.toString());
            JSONObject newNotification = new JSONObject();
            newNotification.put("title",title);
            newNotification.put("content",content);
            newNotification.put("timestamp",timestamp);

            Log.i(TAG,"timestamp : " + timestamp);

            // add timestamp of message reception
            /*Calendar curTime = Calendar.getInstance();
            String timeInReadableFormat = curTime.get(Calendar.DAY_OF_MONTH) + "-" + curTime.get(Calendar.MONTH) + "-" + curTime.get(Calendar.YEAR) +
                    " " + curTime.get(Calendar.HOUR_OF_DAY) + ":" + curTime.get(Calendar.MINUTE) + ":" + curTime.get(Calendar.SECOND);
            newNotification.put("timestamp",timeInReadableFormat);*/


            notifications.put(newNotification.toString());
            sp.edit().putString("notifications",notifications.toString()).commit();
            //Log.i(TAG,notifications.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
