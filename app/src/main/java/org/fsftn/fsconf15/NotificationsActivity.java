package org.fsftn.fsconf15;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class NotificationsActivity extends ActionBarActivity {

    ListView notificationsView;
    String TAG = "fsconf_notif";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        notificationsView = (ListView) findViewById(R.id.notificationsListView);
        try {
            SharedPreferences sp = this.getSharedPreferences("org.fsftn.sc15", Context.MODE_PRIVATE);
            String notifications = sp.getString("notifications", "");
            JSONArray notificationsArray = new JSONArray(notifications);
            JSONObject[] nArray = new JSONObject[notificationsArray.length()];
            //for(int i = notificationsArray.length() - 1; i >= 0; i--) {
            for(int i = 0; i < notificationsArray.length(); i++) {
                nArray[i] = new JSONObject(notificationsArray.getString(notificationsArray.length()-i-1));
                //Log.i(TAG, notificationsArray.getString(i));
            }
            ListAdapter notificationAdapter = new NotificationsAdapter(this, nArray);
            notificationsView.setAdapter(notificationAdapter);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        notificationsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //Intent intent = new Intent(MainActivity.this, SendMessage.class);
                //String message = "abc";
                //intent.putExtra(EXTRA_MESSAGE, message);
                //startActivity(intent);
                //Toast.makeText(NotificationsActivity.this,"You clicked item " + position,Toast.LENGTH_SHORT).show();

                /*invokeDialog(((TextView) view.findViewById(R.id.dtitle)).getText().toString(), ((TextView) view.findViewById(R.id.dtitle)).getText().toString(),
                        ((TextView) view.findViewById(R.id.dtitle)).getText().toString());*/

                //TextView dTitle =  (TextView) view.findViewById(R.id.dtitle);

                //String nTitle = parent.getItem(position).getString("title");

                JSONObject row;
                try {
                    row = new JSONObject(notificationsView.getItemAtPosition(position).toString());
                    invokeDialog(row.getString("title"),row.getString("content"),row.getString("timestamp"),Integer.parseInt(row.getString("type")) );
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    private void invokeDialog(String title, String message, String timestamp, int type){

        final Dialog dialog = new Dialog(NotificationsActivity.this);
        dialog.setContentView(R.layout.dialog_custom_view);
        dialog.setTitle(title);


        // set the custom dialog components - text, image and button
        TextView dTitle = (TextView) dialog.findViewById(R.id.ds_ask_ques);
        dTitle.setText(title);
        TextView dContent = (TextView) dialog.findViewById(R.id.ds_fb);
        dContent.setText(message);
        TextView dTimestamp = (TextView) dialog.findViewById(R.id.dtimestamp);
        dTimestamp.setText(timestamp.substring(0,timestamp.length() - 5));



        ImageView image = (ImageView) dialog.findViewById(R.id.dthumbnail);

        switch(type){
            case 0:
                image.setImageResource(R.drawable.notif);
                break;

            case 1:
                image.setImageResource(R.drawable.event);
                break;

            default:
                image.setImageResource(R.drawable.ques1);

        }

        Button dialogButton = (Button) dialog.findViewById(R.id.ds_send_button);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notifications, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {

            case R.id.notif_action_agenda:
                Intent targetIntent = new Intent(this, ScheduleActivity.class);
                targetIntent.putExtra("session_id", 0);
                startActivity(targetIntent);
                break;


            case R.id.notif_action_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }


            return super.onOptionsItemSelected(item);

    }

    private class NotificationsAdapter extends ArrayAdapter<JSONObject> {
        public NotificationsAdapter(Context context, JSONObject[] notifications) {
            super(context, R.layout.notifications_rowlayout,notifications);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            LayoutInflater li = LayoutInflater.from(getContext());

            View nView = li.inflate(R.layout.notifications_rowlayout, parent, false);

            //nView.setOnClickListener();



            String nTitle=null,nContent=null,nTimestamp="";
            int nType = -1;
            try {
                nTitle = getItem(position).getString("title");
                nContent = getItem(position).getString("content");
                nTimestamp = getItem(position).getString("timestamp");
                nType = Integer.parseInt(getItem(position).getString("type"));
            }
            catch (Exception e) {
                e.printStackTrace();
            }




            TextView nTitleView = (TextView) nView.findViewById(R.id.title);
            TextView nContentView = (TextView) nView.findViewById(R.id.content);
            TextView nTimestampView = (TextView) nView.findViewById(R.id.timestamp);

            nTitleView.setText(nTitle);

            if(nContent.length() > 50)
                nContentView.setText(nContent.substring(0,50) + " ...");
            else
                nContentView.setText(nContent);


            if(nTimestamp.length() > 20)
                nTimestampView.setText("  " + nTimestamp.substring(0,nTimestamp.length() - 5));



            ImageView thumbnailView = (ImageView) nView.findViewById(R.id.thumbnail);
            switch(nType){

                // set background color of list item

                case 0:
                    thumbnailView.setImageResource(R.drawable.notif);
                    //nView.setBackgroundColor(getResources().getColor(R.color.notif_item_color));

                    break;

                case 1:
                    thumbnailView.setImageResource(R.drawable.event);
                    //nView.setBackgroundColor(getResources().getColor(R.color.event_item_color));
                    break;

                default:
                    thumbnailView.setImageResource(R.drawable.ques);
                    //nView.setBackgroundColor(getResources().getColor(R.color.default_item_color));


            }

            return nView;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU)
            return true;
        return super.onKeyDown(keyCode, event);
    }



}
