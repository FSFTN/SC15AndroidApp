package org.fsftn.fsconf15;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class AboutActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView contribList = (TextView) findViewById(R.id.ccontent);
        contribList.setMovementMethod(LinkMovementMethod.getInstance());
        TextView contribAr = (TextView) findViewById(R.id.contrib_aravinth);
        contribAr.setMovementMethod(LinkMovementMethod.getInstance());

        TextView contribSu = (TextView) findViewById(R.id.contrib_suriya);
        contribSu.setMovementMethod(LinkMovementMethod.getInstance());





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){

            case R.id.about_action_agenda:
                Intent targetIntent = new Intent(this, ScheduleActivity.class);
                targetIntent.putExtra("session_id", 0);
                startActivity(targetIntent);
                break;

            case R.id.about_action_home:
                startActivity(new Intent(this,MainActivity.class));
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU)
            return true;
        return super.onKeyDown(keyCode, event);
    }

}


/*

1. build version
2. Repo URL
3. License info (inface we have to include GPL license file in the repo itself)
4. Organization Name
5. Credits


 */