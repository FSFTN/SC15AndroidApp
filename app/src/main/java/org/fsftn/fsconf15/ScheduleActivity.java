package org.fsftn.fsconf15;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ScheduleActivity extends Activity implements AbsListView.OnScrollListener{



    public String dayx[][] = {

            // June 8
            {"June 8","Free Software Philosophy", "Free software means that the software's users have freedom. (The issue is not about price.) We developed the GNU operating system so that users can have freedom in their computing.\n" +
                    "\n" +
                    "Specifically, free software means users have the four essential freedoms: (0) to run the program, (1) to study and change the program in source code form, (2) to redistribute exact copies, and (3) to distribute modified versions.", "09:00 AM", "gnu"},
            {"      ","GNU/Linux Installation", "Many computer users run a modified version of the GNU system every day, without realizing it. Through a peculiar turn of events, the version of GNU which is widely used today is often called “Linux”, and many of its users are not aware that it is basically the GNU system, developed by the GNU Project.\n" +
                    "\n", "11:00 AM", "linux"},
            {"      ","Intro to FS and Shell scripting", "A shell script is a computer program designed to be run by the Unix shell, a command line interpreter. The various dialects of shell scripts are considered to be scripting languages.", "02:00 PM", "terminal"},
            {"      ","Git & Github", "Git is a free and open source distributed version control system designed to handle everything from small to very large projects with speed and efficiency.", "04:00 PM", "url"},


            // June 9
            {"June 9","Wikipedia", "Free software means that the software's users have freedom. (The issue is not about price.) We developed the GNU operating system in source code form, (2) to redistribute exact copies, and (3) to distribute modified versions.", "09:00 AM", "wiki"},
            {"      ","Blender", "Many computer users run a modified version of the GNU system every day, without realizing it. Through a peculiar turn of events, the version of GNU which is widely used today is often called “Linux”, and many of its users are not aware that it is basically the GNU system, developed by the GNU Project.\n" +
                    "\n", "11:00 AM", "blender"},
            {"      ","Tamil Computing & Localization", "A shell script is a computer program designed to be run by the Unix shell, a command line interpreter. The various dialects of shell scripts are considered to be scripting languages.", "02:00 PM", "tamil"},
            {"      ","Cultural Event", "Git is a free and open source distributed version control system designed to handle everything from small to very large projects with speed and efficiency.", "04:00 PM", "culture"},

            // June 10
            {"June 10","Ruby", "Free software means that the software's users have freedom. (The issue is not about price.)", "09:00 AM", "ruby"},
            {"      ","HTML, CSS & JS", "Many computer users run a modified version of the GNU system every day, without realizing it. Through a peculiar turn of events, the version of GNU which is widely used today is often called “Linux”, and many of its users are not aware that it is basically the GNU system, developed by the GNU Project.\n" +
                    "\n", "11:00 AM", "html"},
            {"      ","Firefox OS & Contributing to Mozilla", "A shell script is a computer program designed to be run by the Unix shell, a command line interpreter. The various dialects of shell scripts are considered to be scripting languages.", "02:00 PM", "mozilla"},

            // June 11
            {"June 11","Ruby on Rails", "Free software means that the software's users have freedom. (The issue is not about price.) ", "09:00 AM", "rails"},
            {"      ","Building a Web App using RoR", "Many computer users run a modified version of the GNU system every day, without realizing it. Through a peculiar turn of events, the version of GNU which is widely used today is often called “Linux”, and many of its users are not aware that it is basically the GNU system, developed by the GNU Project.\n" +
                    "\n", "11:00 AM", "rails"},
            {"      ","Project Showcase", "A shell script is a computer program designed to be run by the Unix shell, a command line interpreter. The various dialects of shell scripts are considered to be scripting languages.", "02:00 PM", "project"},

            // June 12
            {"June 12","Internet Surveillance, Privacy, Tor, Mesh Networks", "Free software means that the software's users have freedom. (The issue is not about price.) We developed the GNU operating system so that users can have freedom in their computin","09:00 AM", "tor"},
            {"      ","Intro to Node.js, MongoDB and Meteor", "Many computer users run a modified version of the GNU system every day, without realizing it. Through a peculiar turn of events, the version of GNU which is widely used today is often called “Linux”, and many of its users are not aware that it is basically the GNU system, developed by the GNU Project.\n" +
                    "\n", "11:00 AM", "nodejs"},
            {"      ","Open Hardware", "A shell script is a computer program designed to be run by the Unix shell, a command line interpreter. The various dialects of shell scripts are considered to be scripting languages.", "02:00 PM", "arduino"},
            {"      ","Project Showcase Continuation", "Git is a free and open source distributed version control system designed to handle everything from small to very large projects with speed and efficiency.", "04:00 PM", "project"},


            // June 13
            {"June 13","Cloud & Containers", "Free software means that the software's users have freedom.", "09:00 AM", "cloud"},
            {"      ","Getting Started with Free Software Contribution", "Many computer users run a modified version of the GNU system every day, without realizing it. Through a peculiar turn of events, the version of GNU which is widely used today is often called “Linux”, and many of its users are not aware that it is basically the GNU system, developed by the GNU Project.\n" +
                    "\n", "11:00 AM", "linux"},
            {"      ","Net Neutrality", "A shell script is a computer program designed to be run by the Unix shell, a command line interpreter. The various dialects of shell scripts are considered to be scripting languages.", "02:00 PM", "terminal"},
            {"      ","SWIFT", "Git is a free and open source distributed version control system designed to handle everything from small to very large projects with speed and efficiency.", "04:00 PM", "url"},
            {"      ","How to form a GLUG & What's next?", "Git is a free and open source distributed version control system designed to handle everything from small to very large projects with speed and efficiency.", "04:00 PM", "url"},
            {"      ","Certificate Distribution", "Git is a free and open source distributed version control system designed to handle everything from small to very large projects with speed and efficiency.", "04:00 PM", "url"},





    };

    public int sessionCount[] = {4,4,3,3,4,6};
    public int sessionIndex[] = {0,4,8,11,14,18};

    int sessionId;
    private int preLast;

    Intent starterIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sched);

        starterIntent = getIntent();

        // show first day of schedule

        sessionId = starterIntent.getExtras().getInt("session_id");


        // Construct the data source
        ArrayList<Event> arrayOfUsers = new ArrayList<Event>();
        // Create the adapter to convert the array to views
        EventAdapter adapter = new EventAdapter(this, arrayOfUsers);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.schedListView);
        listView.setOnScrollListener(this);
        listView.setAdapter(adapter);

        // populate the list
        // Add item to adapter

        Event event;

        //TextView dayView = (TextView)findViewById(R.id.day_indicator);
        //dayView.set
        ((TextView)findViewById(R.id.day_indicator)).setText(dayx[sessionIndex[sessionId]][0]);
        for(int i=sessionIndex[sessionId];i<sessionCount[sessionId]+sessionIndex[sessionId];i++) {
            event = new Event(dayx[i]);
            adapter.add(event);
        }
        // dummy events
        adapter.add(new Event("down"));
        adapter.add(new Event());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        Log.i("fsconf", "state changed");

    }

    @Override
    public void onScroll(AbsListView lw, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        final int lastItem = firstVisibleItem + visibleItemCount;
        if(lastItem == totalItemCount) {
            if(preLast!=lastItem){ //to avoid multiple calls for last item
                Log.i("Last", "Last");
                //Toast.makeText(ScheduleActivity.this,"Scroll Completed!!",Toast.LENGTH_SHORT).show();
                preLast = lastItem;
                if(sessionId+1 < 6)
                    starterIntent.putExtra("session_id",sessionId+1);
                else
                    starterIntent.putExtra("session_id",0);

                startActivity(starterIntent); finish();
            }
        }

    }


    public class EventAdapter extends ArrayAdapter<Event> {
        private static final String TAG = "EventAdapter";

        public EventAdapter(Context context, ArrayList<Event> events) {
            super(context, 0, events);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Event event = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.notifications_rowlayout, parent, false);
            }
            // Lookup view for data population
            TextView eTitle = (TextView) convertView.findViewById(R.id.title);
            TextView eContent = (TextView) convertView.findViewById(R.id.content);
            TextView eTimestamp = (TextView)convertView.findViewById(R.id.timestamp);
            ImageView eThumbnail = (ImageView)convertView.findViewById(R.id.thumbnail);

            // Populate the data into the template view using the data object
            eTitle.setText(event.title);
            Log.i(TAG, "setting content " + event.content);
            eContent.setText(event.content);
            eTimestamp.setText(event.timestamp);
            eThumbnail.setImageResource(ScheduleActivity.this.getResources().getIdentifier(event.iurl,"drawable",ScheduleActivity.this.getPackageName()));

            // Return the completed view to render on screen
            return convertView;
        }
    }





}
