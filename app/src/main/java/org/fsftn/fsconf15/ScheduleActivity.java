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
            {"June 8","Free Software Philosophy", "Free software means that the software's users have freedom. ", "10:00 AM", "gnu"},
            {"      ","GNU/Linux Installation", "Many computer users run a modified version of the GNU system every day, without realizing it. Through a peculiar turn of events, the version of GNU which is widely used today is often called “Linux”, and many of its users are not aware that it is basically the GNU system, developed by the GNU Project.\n" +
                    "\n", "11:45 AM", "linux"},
            {"      ","Intro to FS and Shell scripting", "A shell script is a computer program designed to be run by the Unix shell, a command line interpreter. The various dialects of shell scripts are considered to be scripting languages.", "02:00 PM", "terminal"},
            {"      ","Git & Github", "Git is a free and open source distributed version control system designed to handle everything from small to very large projects with speed and efficiency.", "03:00 PM", "url"},


            // June 9
            {"June 9","Wikipedia","A wiki is an application, typically a web application, which allows collaborative modification, extension, or deletion of its content and structure.", "09:30 AM", "wiki"},
            {"      ","Blender", "Blender is a professional free and open-source 3D computer graphics software product used for creating animated films, visual effects, art, 3D printed models, interactive 3D applications and video games.", "10:15 AM", "blender"},
            {"      ","Tamil Computing & Localization", "Software localization is the process of adapting a software product to the linguistic, cultural and technical requirements of a target market.", "02:00 PM", "tamil"},
            {"      ","Cultural Event", ": Someone kindly fill this up - I know nothing about this :", "04:00 PM", "culture"},




            // June 10
            {"June 10","Ruby", "Ruby is a dynamic, reflective, object-oriented, general-purpose programming language. It was designed and developed in the mid-1990s by Yukihiro Matsumoto in Japan.", "09:30 AM", "ruby"},
            {"      ","HTML, CSS & JS", "HTML5 is a core technology markup language of the Internet used for structuring and presenting content for the World Wide Web. As of October 2014 this is the final and complete fifth revision of the HTML standard of the World Wide Web Consortium (W3C). The previous version, HTML 4, was standardised in 1997.", "02:00 AM", "html"},
            {"      ","Firefox OS & Contributing to Mozilla", "Firefox OS is designed to provide a complete, community-based alternative system for mobile devices, using open standards and approaches such as HTML5 applications, JavaScript, a robust privilege model, open web APIs to communicate directly with cellphone hardware, and application marketplace.", "03:40 PM", "mozilla"},

            // June 11
            {"June 11","Ruby on Rails", "Ruby on Rails, or simply Rails, is an open source web application framework written in Ruby. Rails is a model–view–controller (MVC) framework, providing default structures for a database, a web service, and web pages.", "09:30 AM", "rails"},
            {"      ","Building a Web App using RoR", "RoR is designed to make programming web applications easier by making assumptions about what every developer needs to get started. It allows you to write less code while accomplishing more than many other languages and frameworks.", "11:00 AM", "rails"},
            {"      ","Project Showcase", "-- Someone kindly fill this up  ---", "03:10 PM", "project"},

            // June 12
            {"June 12","Internet Surveillance, Privacy, Tor, Mesh Networks", "Tor is free software for enabling anonymous communication. The name is an acronym derived from the original software project name The Onion Router.","09:30 AM", "tor1"},
            {"      ","Intro to Node.js, MongoDB and Meteor", "Node.js is a platform built on Chrome's JavaScript runtime for easily building fast, scalable network applications. Node.js uses an event-driven, non-blocking I/O model that makes it lightweight and efficient, perfect for data-intensive real-time applications that run across distributed devices.", "11:15 AM", "nodejs"},
            {"      ","Open Hardware", "Arduino is an open-source computer hardware and software company, project and user community that designs and manufactures kits for building digital devices and interactive objects that can sense and control the physical world.[1] Arduino boards may be purchased preassembled, or as do-it-yourself kits; at the same time, the hardware design information is available for those who would like to assemble an Arduino from scratch.", "02:00 PM", "arduino"},
            {"      ","Project Showcase Continuation", "-- Someone kindly fill this up  ---", "03:10 PM", "project"},


            // June 13
            {"June 13","Cloud & Containers", "loud computing relies on sharing of resources to achieve coherence and economies of scale, similar to a utility (like the electricity grid) over a network. At the foundation of cloud computing is the broader concept of converged infrastructure and shared services.", "09:30 AM", "cloud"},
            {"      ","Getting Started with Free Software Contribution", "The FSF high-priority projects list serves to foster work on projects that are important for increasing the adoption and use of free software and free software operating systems. Our list helps guide volunteers and supporters to projects where their skills can be utilized, whether they be in coding, graphic design, writing, or activism. The FSF does not ask to run or control these projects; some of them are in fact GNU projects (and all are welcome to apply), but we are happy to encourage them whether they are done under our auspices or not.", "11:15 AM", "gnu"},
            {"      ","Net Neutrality", "Net neutrality (also network neutrality, Internet neutrality, or net equality) is the principle that Internet service providers and governments should treat all data on the Internet equally, not discriminating or charging differentially by user, content, site, platform, application, type of attached equipment, or mode of communication.", "12:15 PM", "nn"},
            {"      ","SWIFT", "-- Someone kindly fill this up - I honestly have no idea what this is ---", "02:00 PM", "ques"},
            {"      ","How to form a GLUG & What's next?", "GNU/Linux User Group (GLUG) is a private, generally non-profit or not-for-profit organization that provides support and/or education for Linux users, particularly for inexperienced users. ", "02:30 PM", "gnu"},
            {"      ","Certificate Distribution", "-- Someone kindly fill this up  ---", "03:10 PM", "certificate"},





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
