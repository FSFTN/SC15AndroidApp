package org.fsftn.fsconf15;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class ContactsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {

            case R.id.contact_action_agenda:
                Intent targetIntent = new Intent(this, ScheduleActivity.class);
                targetIntent.putExtra("session_id", 0);
                startActivity(targetIntent);
                break;


            case R.id.contact_action_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    public void callAG(View view) {
        String number = "tel:+919500683173";
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
        startActivity(callIntent);
    }

    public void callRA(View view) {
        String number = "tel:+919566152513";
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
        startActivity(callIntent);
    }

    public void callOffice(View view) {
        String number = "tel:+914443504670";
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
        startActivity(callIntent);
    }

    public void mailUs(View view) {
        String mailID = "mailto:ask@fsftn.org";
        Intent callIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mailID));
        startActivity(callIntent);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU)
            return true;
        return super.onKeyDown(keyCode, event);
    }






}
