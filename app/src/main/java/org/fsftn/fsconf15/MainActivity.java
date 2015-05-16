package org.fsftn.fsconf15;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Toast;

import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.lang.reflect.Field;

public class MainActivity extends ActionBarActivity {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    String SENDER_ID = "214467984771";
    GoogleCloudMessaging gcm;
    Context context;
    String regid = "";
    String TAG = "fsconf";
    int PICK_ACCOUNT_REQUEST = 1;
    String accountName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.gnu2);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle("  Summer Camp 2K15");
        getSupportActionBar().setSubtitle("   Organized by FSFTN");

        makeActionOverflowMenuShown();


        context = this;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);

            if (regid.isEmpty()) {
                registerInBackground();
                Log.i(TAG,"registering in background");
            }
        } else {
            Log.i("fsconf", "No valid Google Play Services APK found.");
        }
    }






    private void registerInBackground() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    startActivityForResult(AccountPicker.newChooseAccountIntent(null,null,new String[]{"com.google"},false,null,null,null,null),PICK_ACCOUNT_REQUEST);
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration id=" + regid;
                    Log.i(TAG,msg);
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.i(TAG,msg);
                }
                return msg;
            }
        }.execute();
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i("fsconf", "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("Warning", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i("fsconf", "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i("fsconf", "App version changed.");
            return "";
        }
        return registrationId;
    }

    private SharedPreferences getGCMPreferences(Context context) {
        return getSharedPreferences(MainActivity.class.getSimpleName(),Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.mi_about_us:
                startActivity(new Intent(this, AboutActivity.class));
                return true;

            case R.id.mi_sched:
                Intent targetIntent = new Intent(this, ScheduleActivity.class);
                targetIntent.putExtra("session_id", 0);
                startActivity(targetIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    private String sendRegIdToServer(final String regid, final String account){
        try {
            String response = (String) new AsyncTask() {
                @Override
                protected Object doInBackground(Object... params) {
                    try {
                        Log.i(TAG,"trying to register");
                        String url = "http://sc15.herokuapp.com/devices?device[device_id]=" + regid+"&device[email]="+account;
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost(url);
                        HttpResponse httpResponse = httpClient.execute(httpPost);
                        if (httpResponse.getStatusLine().getStatusCode() == 200) {
                            Log.i(TAG,"registered");
                            return ("Device registered with server");
                        } else {
                            Log.i(TAG,"registration failed");
                            return ("Device not registered");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return ("Error occured while registering with server");
                }
            }.execute().get();
            return response;
        }
        catch (Exception e) {
            e.printStackTrace();
            return ("Error occured while registering with server");
        }
    }

    public void openNotifications(View view) {
        Intent targetIntent = new Intent(this, NotificationsActivity.class);
        startActivity(targetIntent);
    }

    public void openMap(View view) {
        String url = "https://www.google.co.in/maps/place/CS+Lecture+Hall,+Indian+Institute+of+Technology+Madras,+Indian+Institute+Of+Technology,+Chennai,+Tamil+Nadu+600036/@12.9890561,80.2294416,17z/data=!3m1!4b1!4m2!3m1!1s0x3a525d807c494541:0xa711b72ff6bacec8?hl=en";
        Intent targetIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        targetIntent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
        startActivity(targetIntent);
    }

    public void openSchedule(View view) {
        Intent targetIntent = new Intent(this, ScheduleActivity.class);
        targetIntent.putExtra("session_id", 0);
        startActivity(targetIntent);
    }

    public void openContacts(View view) {
        Intent targetIntent = new Intent(this, ContactsActivity.class);
        startActivity(targetIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_ACCOUNT_REQUEST && resultCode == RESULT_OK) {
            accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            String response = "";
            if(!regid.equals("")) {
                response = sendRegIdToServer(regid, accountName);
                Log.i(TAG, response);
                Toast.makeText(context, response, Toast.LENGTH_SHORT);
            }
            else {
                Log.i(TAG, "onAcitivityResult >> Invalid regid");
                Toast.makeText(context, "Invalid regid", Toast.LENGTH_SHORT);
            }
        }
    }


    private void makeActionOverflowMenuShown() {
        //devices with hardware menu button (e.g. Samsung Note) don't show action overflow menu
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
        }
    }

}