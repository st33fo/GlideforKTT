package com.st33fo.glideforktt;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.MainThread;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.securepreferences.SecurePreferences;
import com.squareup.picasso.Picasso;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar myToolBar;
    private NavigationView myNavigationView;
    private DrawerLayout myDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private static ImageView profilePicture;
    private static TextView headerText;
    private TextView descriptionText;
    private RecyclerView threadRecycler;
    private String URL = "http://www.kanyetothe.com/forum/index.php?board=1.0";
    private static List<ThreadObject> threadObjectList = new ArrayList<ThreadObject>();
    private ThreadObject threadObject;
    private int SubtopicNum = 0;
    private int whichpage = 0;
    private String sectionTitle;
    private static int ColorTheseCards = 0;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AppBarLayout appBarLayout;
    private static RequestQueue myRequestQueue = null;
    private String profileLink = "http://www.kanyetothe.com/forum/index.php?action=profile";
    private String profilePostLink ="http://www.kanyetothe.com/forum/index.php?action=profile;area=showposts";
    private String profileTopicsLink ="http://www.kanyetothe.com/forum/index.php?action=profile;area=showposts;sa=topics";

    /**
     * Way too lazy to reload data since it's already here
     * I'll take advantage of it here, but for other people goddamn it is gonna be a little harder...
     */
    private String profileName = "";



    ThreadAdapter threadAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**The commented out code below is just in case the app messes up and I want to delete the sharedpreference so it doesn't keep me logged in**/
//    SecuredSharePreference.setCookies(MainActivity.this,"");
        //make sure the main activity doesn't skip this.
        setContentView(R.layout.activity_main);



        if (SecuredSharePreference.getPrefCookies(MainActivity.this).length() == 0)

        {

        Log.i("System.out","There isn't something in the preferences");
            Intent i = new Intent(MainActivity.this, Login.class);
            startActivity(i);

        } else {
            Log.i("System.out","There is something in the preferences");
            System.out.println(SecuredSharePreference.getPrefCookies(MainActivity.this));
            // Stay at the current activity.
            Fabric.with(this, new Crashlytics());
            JobManager.create(this).addJobCreator(new KTTJobCreator());
            ShowNotificationJob.schedulePeriodic();
            appBarLayout = (AppBarLayout) findViewById(R.id.appbar);

            myToolBar = (Toolbar) findViewById(R.id.app_bar);
            myToolBar.setTitle("Glide for KTT");
            setSupportActionBar(myToolBar);

            swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_thread);
            myNavigationView = (NavigationView) findViewById(R.id.main_drawer);
            myNavigationView.setNavigationItemSelectedListener(this);
            myDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawerToggle = new ActionBarDrawerToggle(this, myDrawerLayout, myToolBar, R.string.drawer_open, R.string.drawer_close);
            myDrawerLayout.setDrawerListener(drawerToggle);
            threadRecycler = (RecyclerView) findViewById(R.id.threadrecycler);
            LinearLayoutManager myLinearLM = new LinearLayoutManager(MainActivity.this);

            threadRecycler.setLayoutManager(myLinearLM);
            drawerToggle.syncState();


            View header = myNavigationView.getHeaderView(0);
            headerText = (TextView) header.findViewById(R.id.headerText);
            descriptionText = (TextView) header.findViewById(R.id.Description);
            profilePicture = (ImageView) header.findViewById(R.id.profilePicture);


            new getHeaderInformation().execute();
            if (savedInstanceState == null) {
                myNavigationView.getMenu().performIdentifierAction(R.id.Kanye_West, 0);
            }

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    clearData();
                    new loadTopicData().execute();
                }
            });
        }



        // Stay at the current activity.

    }


    private class loadTopicData extends AsyncTask<Void, Void, Void> {
        Document threadDocument;
        String title = " ";
        String person = " ";
        String latest_post = " ";
        String views = " ";
        String replies = " ";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            appBarLayout.setExpanded(true);
        }

        @Override
        protected Void doInBackground(Void... params) {


            Connection.Response threadConnection = null;
            try {
                URL = URL.replace(
                        URL.substring(URL.lastIndexOf(".") + 1), "" + whichpage);

                threadDocument = new GetDocument(MainActivity.this).GetDocument(URL);

                Elements topics = threadDocument.getElementsByTag("tr");
                for (Element topic : topics) {
                    threadObject = new ThreadObject();
                    threadObject.setSection(sectionTitle);
                    title = topic.select("td[class=topic_title]").select("a[title]").text();
                    if (title.equals("")) {
                        title = topic.select("td.topic_title.locked").select("a[title]").text();
                    }
                    threadObject.setThreadtitle(title);
                    person = topic.select("td[class=topic_title]").select("span.topic_pages.tby").text();
                    person = person.replace("by", "");
                    person = person.trim();
                    if (person.equals("")) {
                        person = topic.select("td[class=topic_title]").select("span[class=topic_pages]").text();
                        person = person.replace("by", "");
                        person = person.trim();
                    }
                    if (person.equals("")) {
                        person = topic.select("td.topic_title.locked").select("span.topic_pages.tby").text();
                        person = person.replace("by", "");
                        person = person.trim();
                    }
                    threadObject.setPerson(person);
                    latest_post = topic.select("td[class=topic_last]").text();
                    latest_post = latest_post.replace(" »", "");
                    latest_post = latest_post.replace("Last post by", "");
                    threadObject.setLatest_poster(latest_post.trim());
                    threadObject.setMostrecentpage(topic.select("td[class=topic_last]").select("a[href]").attr("href"));
                    views = topic.select("td[class=num]").text();
                    replies = views;
                    if (replies.contains("Replies") && views.contains("Views")) {
                        replies = replies.substring(0, views.indexOf("Replies"));
                        replies = replies.trim();

                        views = views.replace(replies, "");
                        views = views.replace("Replies", "");
                        views = views.replace("Views", "");
                    }
                    views = views.trim();
                    threadObject.setReplies(replies);
                    threadObject.setViews(views);


                    threadObjectList.add(threadObjectList.size(), threadObject);


                }
                if (SubtopicNum != 0) {
                    for (int x = 0; x < SubtopicNum; x++) {
                        threadObjectList.remove(0);
                    }
                }
                if (ColorTheseCards != 0) {
                    for (int x = 0; x < ColorTheseCards; x++) {
                        threadObjectList.get(x).setThreadtitle(threadObjectList.get(x).getThreadtitle() + " ");
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            threadAdapter = new ThreadAdapter(threadObjectList, MainActivity.this);
            threadRecycler.setAdapter(threadAdapter);
            int threadsize = threadObjectList.size();

            threadRecycler.getAdapter().notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);


        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        threadAdapter.notifyDataSetChanged();
//
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
////        clearData();
////        new loadTopicData().execute();
//
//    }
public static void clearUserName(Context ctx)
{
    SharedPreferences.Editor editor = SecuredSharePreference.getSecuredPreferences(ctx).edit();
    editor.clear(); //clear all stored data
    editor.commit();
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the actionbar if present.
        getMenuInflater().inflate(R.menu.menu_mainthreads, menu);
        return true;

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle action bar item clicks here
        //Action bar will automatically handle clicks on home/up button
        //But you have to specify that in the manifest file
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_refresh) {
            swipeRefreshLayout.setRefreshing(true);
            clearData();
            new loadTopicData().execute();
        }
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_next_page) {
            swipeRefreshLayout.setRefreshing(true);
            whichpage += 40;
            clearData();
            new loadTopicData().execute();

        }
        if(id ==R.id.action_logout){
            clearUserName(this);
            Intent i = new Intent(this,Login.class);
            startActivity(i);
        }
        if (id == R.id.action_previous_page) {

            if (whichpage == 0) {
                Toast.makeText(MainActivity.this, "You are Already on the First Page!", Toast.LENGTH_SHORT).show();

            } else if (whichpage != 0) {
                swipeRefreshLayout.setRefreshing(true);
                whichpage -= 40;
                clearData();
                new loadTopicData().execute();
            }


        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.feed:

                myDrawerLayout.closeDrawer(GravityCompat.START);
                Intent i = new Intent(MainActivity.this, Feed.class);
                startActivity(i);
                break;
            case R.id.notifications:
                myNavigationView.setCheckedItem(R.id.notifications);
                break;
            case R.id.messages:
                myNavigationView.setCheckedItem(R.id.messages);
                break;
            case R.id.settings_nav_option:
                myNavigationView.setCheckedItem(R.id.settings_nav_option);
                break;
            case R.id.Kanye_West:
                whichpage = 0;
                ColorTheseCards = 6;
                SubtopicNum = 2;

                myNavigationView.setCheckedItem(R.id.Kanye_West);
                URL = "http://www.kanyetothe.com/forum/index.php?board=1.0";
                sectionTitle = "Kanye West";
                myToolBar.setTitle("Kanye West");
                swipeRefreshLayout.setRefreshing(true);
                clearData();
                threadRecycler.removeAllViews();
                myDrawerLayout.closeDrawer(GravityCompat.START);
                new loadTopicData().execute();
                break;
            case R.id.Saint_Pablo_Tour:
                whichpage = 0;
                ColorTheseCards = 5;
                SubtopicNum = 0;
                swipeRefreshLayout.setRefreshing(true);
                myNavigationView.setCheckedItem(R.id.Saint_Pablo_Tour);
                URL = "http://www.kanyetothe.com/forum/index.php?board=194.0";
                sectionTitle = "Saint Pablo Tour";
                myToolBar.setTitle("Saint Pablo Tour");
                clearData();
                threadRecycler.removeAllViews();
                myDrawerLayout.closeDrawer(GravityCompat.START);
                new loadTopicData().execute();
                break;
            case R.id.Good_Music:
                whichpage = 0;
                ColorTheseCards = 7;
                SubtopicNum = 0;
                swipeRefreshLayout.setRefreshing(true);
                myNavigationView.setCheckedItem(R.id.Good_Music);
                URL = "http://www.kanyetothe.com/forum/index.php?board=2.0";
                sectionTitle = "Good Music";
                myToolBar.setTitle("Good Music");
                clearData();
                threadRecycler.removeAllViews();
                myDrawerLayout.closeDrawer(GravityCompat.START);
                new loadTopicData().execute();
                break;
            case R.id.Music:
                whichpage = 0;
                myNavigationView.setCheckedItem(R.id.Music);
                ColorTheseCards = 8;
                SubtopicNum = 3;
                swipeRefreshLayout.setRefreshing(true);
                URL = "http://www.kanyetothe.com/forum/index.php?board=3.0";
                sectionTitle = "Music";
                myToolBar.setTitle("Music");
                clearData();
                threadRecycler.removeAllViews();
                myDrawerLayout.closeDrawer(GravityCompat.START);
                new loadTopicData().execute();
                break;
            case R.id.Album_Discussion:
                whichpage = 0;
                myNavigationView.setCheckedItem(R.id.Album_Discussion);
                ColorTheseCards = 3;
                SubtopicNum = 0;
                swipeRefreshLayout.setRefreshing(true);
                URL = "http://www.kanyetothe.com/forum/index.php?board=17.0";
                sectionTitle = "Album Discussion";
                myToolBar.setTitle("Album Discussion");
                clearData();
                threadRecycler.removeAllViews();
                myDrawerLayout.closeDrawer(GravityCompat.START);
                new loadTopicData().execute();
                break;
            case R.id.Rock_Indie:
                whichpage = 0;
                ColorTheseCards = 5;
                SubtopicNum = 0;
                swipeRefreshLayout.setRefreshing(true);
                myNavigationView.setCheckedItem(R.id.Rock_Indie);
                URL = "http://www.kanyetothe.com/forum/index.php?board=21.0";
                sectionTitle = "Rock/Indie";
                myToolBar.setTitle("Rock/Indie");
                clearData();
                threadRecycler.removeAllViews();
                myDrawerLayout.closeDrawer(GravityCompat.START);
                new loadTopicData().execute();
                break;
            case R.id.Pop_RNB:
                whichpage = 0;
                ColorTheseCards = 6;
                SubtopicNum = 0;
                swipeRefreshLayout.setRefreshing(true);
                myNavigationView.setCheckedItem(R.id.Pop_RNB);
                URL = "http://www.kanyetothe.com/forum/index.php?board=102.0";
                sectionTitle = "Pop/R&B";
                myToolBar.setTitle("Pop/R&B");
                clearData();
                threadRecycler.removeAllViews();
                myDrawerLayout.closeDrawer(GravityCompat.START);
                new loadTopicData().execute();
                break;
            case R.id.TV_Film:
                whichpage = 0;
                ColorTheseCards = 6;
                SubtopicNum = 0;
                swipeRefreshLayout.setRefreshing(true);
                myNavigationView.setCheckedItem(R.id.TV_Film);
                URL = "http://www.kanyetothe.com/forum/index.php?board=4.0";
                sectionTitle = "TV & Films";
                myToolBar.setTitle("TV & Films");
                threadRecycler.removeAllViews();
                clearData();
                myDrawerLayout.closeDrawer(GravityCompat.START);
                new loadTopicData().execute();
                break;
            case R.id.Video_Games:
                whichpage = 0;
                ColorTheseCards = 7;
                SubtopicNum = 1;
                swipeRefreshLayout.setRefreshing(true);
                myNavigationView.setCheckedItem(R.id.Video_Games);
                URL = "http://www.kanyetothe.com/forum/index.php?board=6.0";
                sectionTitle = "Video Games";
                myToolBar.setTitle("Video Games");
                threadRecycler.removeAllViews();
                clearData();
                myDrawerLayout.closeDrawer(GravityCompat.START);
                new loadTopicData().execute();
                break;
            case R.id.Gadgeets_Tech:
                whichpage = 0;
                ColorTheseCards = 7;
                SubtopicNum = 0;
                swipeRefreshLayout.setRefreshing(true);
                myNavigationView.setCheckedItem(R.id.Gadgeets_Tech);
                URL = "http://www.kanyetothe.com/forum/index.php?board=79.0";
                sectionTitle = "Gadgets & Tech";
                myToolBar.setTitle("Gadgets & Tech");
                threadRecycler.removeAllViews();
                clearData();
                myDrawerLayout.closeDrawer(GravityCompat.START);
                new loadTopicData().execute();

                break;
            case R.id.Sports:
                whichpage = 0;
                ColorTheseCards = 8;
                SubtopicNum = 0;
                swipeRefreshLayout.setRefreshing(true);
                myNavigationView.setCheckedItem(R.id.Sports);
                URL = "http://www.kanyetothe.com/forum/index.php?board=5.0";
                sectionTitle = "Sports";
                myToolBar.setTitle("Sports");
                threadRecycler.removeAllViews();
                clearData();
                myDrawerLayout.closeDrawer(GravityCompat.START);
                new loadTopicData().execute();
                break;
            case R.id.Life_Advice:
                whichpage = 0;
                ColorTheseCards = 8;
                SubtopicNum = 2;
                swipeRefreshLayout.setRefreshing(true);
                myNavigationView.setCheckedItem(R.id.Life_Advice);
                URL = "http://www.kanyetothe.com/forum/index.php?board=22.0";
                sectionTitle = "Life & Advice";
                myToolBar.setTitle("Life & Advice");
                threadRecycler.removeAllViews();
                clearData();
                myDrawerLayout.closeDrawer(GravityCompat.START);
                new loadTopicData().execute();
                break;
            case R.id.General_Discussion:
                whichpage = 0;
                ColorTheseCards = 6;
                swipeRefreshLayout.setRefreshing(true);
                SubtopicNum = 0;
                myNavigationView.setCheckedItem(R.id.General_Discussion);
                URL = "http://www.kanyetothe.com/forum/index.php?board=161.0";
                sectionTitle = "General Disc.";
                myToolBar.setTitle("General Disc.");
                threadRecycler.removeAllViews();
                clearData();
                myDrawerLayout.closeDrawer(GravityCompat.START);
                new loadTopicData().execute();

                break;
            case R.id.Relationship_Advice:
                whichpage = 0;
                ColorTheseCards = 6;
                SubtopicNum = 0;
                swipeRefreshLayout.setRefreshing(true);
                myNavigationView.setCheckedItem(R.id.Relationship_Advice);
                URL = "http://www.kanyetothe.com/forum/index.php?board=67.0";
                sectionTitle = "Relationship Advice";
                myToolBar.setTitle("Relationship Advice");
                threadRecycler.removeAllViews();
                clearData();

                myDrawerLayout.closeDrawer(GravityCompat.START);
                new loadTopicData().execute();

                break;
            case R.id.Fashion_Style:
                whichpage = 0;
                ColorTheseCards = 8;
                SubtopicNum = 1;
                swipeRefreshLayout.setRefreshing(true);
                myNavigationView.setCheckedItem(R.id.Fashion_Style);
                URL = "http://www.kanyetothe.com/forum/index.php?board=8.0";
                sectionTitle = "Fashion & Style";
                myToolBar.setTitle("Fashion & Style");
                threadRecycler.removeAllViews();
                clearData();
                myDrawerLayout.closeDrawer(GravityCompat.START);
                new loadTopicData().execute();
                break;
            case R.id.Marketplace:
                whichpage = 0;
                ColorTheseCards = 3;
                SubtopicNum = 0;
                swipeRefreshLayout.setRefreshing(true);
                myNavigationView.setCheckedItem(R.id.Marketplace);
                URL = "http://www.kanyetothe.com/forum/index.php?board=69.0";
                sectionTitle = "Marketplace";
                swipeRefreshLayout.setRefreshing(true);
                myToolBar.setTitle("Marketplace");
                threadRecycler.removeAllViews();
                clearData();
                myDrawerLayout.closeDrawer(GravityCompat.START);
                new loadTopicData().execute();
                break;
            case R.id.Creative_Showcase:
                whichpage = 0;
                ColorTheseCards = 9;
                SubtopicNum = 3;
                swipeRefreshLayout.setRefreshing(true);
                myNavigationView.setCheckedItem(R.id.Creative_Showcase);
                URL = "http://www.kanyetothe.com/forum/index.php?board=9.0";
                sectionTitle = "Creative Showcase";
                myToolBar.setTitle("Creative Showcase");
                threadRecycler.removeAllViews();
                clearData();
                myDrawerLayout.closeDrawer(GravityCompat.START);
                new loadTopicData().execute();
                break;
            case R.id.Art:
                whichpage = 0;
                ColorTheseCards = 4;
                SubtopicNum = 0;
                swipeRefreshLayout.setRefreshing(true);
                myNavigationView.setCheckedItem(R.id.Art);
                URL = "http://www.kanyetothe.com/forum/index.php?board=26.0";
                sectionTitle = "Art";
                myToolBar.setTitle("Art");
                threadRecycler.removeAllViews();
                clearData();
                myDrawerLayout.closeDrawer(GravityCompat.START);
                new loadTopicData().execute();
                break;
            case R.id.Mixtapes:
                whichpage = 0;
                ColorTheseCards = 1;
                SubtopicNum = 0;
                swipeRefreshLayout.setRefreshing(true);
                myNavigationView.setCheckedItem(R.id.Mixtapes);
                URL = "http://www.kanyetothe.com/forum/index.php?board=34.0";
                sectionTitle = "Mixtapes";
                myToolBar.setTitle("Mixtapes");
                threadRecycler.removeAllViews();
                clearData();
                myDrawerLayout.closeDrawer(GravityCompat.START);
                new loadTopicData().execute();
                break;
            case R.id.Writing:
                whichpage = 0;
                ColorTheseCards = 3;
                SubtopicNum = 0;
                swipeRefreshLayout.setRefreshing(true);
                myNavigationView.setCheckedItem(R.id.Writing);
                URL = "http://www.kanyetothe.com/forum/index.php?board=54.0";
                sectionTitle = "Writing";
                myToolBar.setTitle("Writing");
                threadRecycler.removeAllViews();
                clearData();
                myDrawerLayout.closeDrawer(GravityCompat.START);
                new loadTopicData().execute();
                break;
            case R.id.Drake:
                whichpage = 0;
                ColorTheseCards = 4;
                SubtopicNum = 0;
                swipeRefreshLayout.setRefreshing(true);
                myNavigationView.setCheckedItem(R.id.Drake);
                URL = "http://www.kanyetothe.com/forum/index.php?board=38.0";
                sectionTitle = "Drake";
                myToolBar.setTitle("Drake");
                threadRecycler.removeAllViews();
                clearData();
                myDrawerLayout.closeDrawer(GravityCompat.START);
                new loadTopicData().execute();
                break;
            case R.id.Kendrick_Lamar:
                whichpage = 0;
                ColorTheseCards = 5;
                SubtopicNum = 0;
                swipeRefreshLayout.setRefreshing(true);
                myNavigationView.setCheckedItem(R.id.Kendrick_Lamar);
                URL = "http://www.kanyetothe.com/forum/index.php?board=178.0";
                sectionTitle = "Kendrick Lamar";
                myToolBar.setTitle("Kendrick Lamar");
                threadRecycler.removeAllViews();
                clearData();
                myDrawerLayout.closeDrawer(GravityCompat.START);
                new loadTopicData().execute();
                break;
            case R.id.Travis_Scott:
                whichpage = 0;
                SubtopicNum = 0;
                swipeRefreshLayout.setRefreshing(true);
                myNavigationView.setCheckedItem(R.id.Travis_Scott);
                URL = "http://www.kanyetothe.com/forum/index.php?board=202.0";
                sectionTitle = "Travis Scott";
                myToolBar.setTitle(sectionTitle);
                threadRecycler.removeAllViews();
                clearData();
                myDrawerLayout.closeDrawer(GravityCompat.START);
                new loadTopicData().execute();
                break;
            case R.id.The_Weeknd:
                whichpage = 0;
                ColorTheseCards = 7;
                SubtopicNum = 0;
                swipeRefreshLayout.setRefreshing(true);
                myNavigationView.setCheckedItem(R.id.The_Weeknd);
                URL = "http://www.kanyetothe.com/forum/index.php?board=92.0";
                sectionTitle = "The Weeknd";
                myToolBar.setTitle("The Weeknd");
                threadRecycler.removeAllViews();
                clearData();
                myDrawerLayout.closeDrawer(GravityCompat.START);
                new loadTopicData().execute();
                break;
            case R.id.Kid_Cudi:
                whichpage = 0;
                ColorTheseCards = 6;
                SubtopicNum = 0;
                swipeRefreshLayout.setRefreshing(true);
                myNavigationView.setCheckedItem(R.id.Kid_Cudi);
                URL = "http://www.kanyetothe.com/forum/index.php?board=19.0";
                sectionTitle = "Kid Cudi";
                myToolBar.setTitle("Kid Cudi");
                threadRecycler.removeAllViews();
                clearData();
                myDrawerLayout.closeDrawer(GravityCompat.START);
                new loadTopicData().execute();
                break;
            case R.id.Lupe_Fiasco:
                whichpage = 0;
                ColorTheseCards = 3;
                SubtopicNum = 0;
                swipeRefreshLayout.setRefreshing(true);
                myNavigationView.setCheckedItem(R.id.Lupe_Fiasco);
                URL = "http://www.kanyetothe.com/forum/index.php?board=15.0";
                sectionTitle = "Lupe Fiasco";
                myToolBar.setTitle("Lupe Fiasco");
                threadRecycler.removeAllViews();
                clearData();
                myDrawerLayout.closeDrawer(GravityCompat.START);
                new loadTopicData().execute();
                break;
            case R.id.Odd_Future:
                whichpage = 0;
                ColorTheseCards = 5;
                SubtopicNum = 0;
                swipeRefreshLayout.setRefreshing(true);
                myNavigationView.setCheckedItem(R.id.Odd_Future);
                URL = "http://www.kanyetothe.com/forum/index.php?board=56.0";
                sectionTitle = "Odd Future";
                myToolBar.setTitle("Odd Future");
                threadRecycler.removeAllViews();
                clearData();
                myDrawerLayout.closeDrawer(GravityCompat.START);
                new loadTopicData().execute();
                break;
            case R.id.Misc:
                whichpage = 0;
                ColorTheseCards = 6;
                SubtopicNum = 1;
                swipeRefreshLayout.setRefreshing(true);
                myNavigationView.setCheckedItem(R.id.Misc);
                URL = "http://www.kanyetothe.com/forum/index.php?board=64.0";
                sectionTitle = "Misc";
                myToolBar.setTitle("Misc");
                threadRecycler.removeAllViews();
                clearData();
                myDrawerLayout.closeDrawer(GravityCompat.START);
                new loadTopicData().execute();

                break;
            case R.id.Classic_GNG:
                whichpage = 0;
                ColorTheseCards = 6;
                SubtopicNum = 0;
                swipeRefreshLayout.setRefreshing(true);
                myNavigationView.setCheckedItem(R.id.Classic_GNG);
                URL = "http://www.kanyetothe.com/forum/index.php?board=99.0";
                sectionTitle = "Classic G&G";
                myToolBar.setTitle("Classic G&G");
                threadRecycler.removeAllViews();
                clearData();
                myDrawerLayout.closeDrawer(GravityCompat.START);
                new loadTopicData().execute();

                break;

        }

        return false;
    }

    private class getHeaderInformation extends AsyncTask<Void, Void, Void> {
        String imgSrc = "";
        String header = "";
        String description = "";
        Document profileDocument;
        Bitmap profileBitmap;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... params) {
            try {


                profileDocument = new GetDocument(MainActivity.this).GetDocument(profileLink);
                Elements img = profileDocument.select("img[src$=.png]");
                Elements img2 = profileDocument.select("img[class=avatar]");

                imgSrc = img2.attr("src");
                InputStream input = new java.net.URL(imgSrc).openStream();
                profileBitmap = BitmapFactory.decodeStream(input);
                profileDocument.select("div[class=user_info]").select("a[class=more_link]").remove();
                profileDocument.select("div[style]").remove();

                header = profileDocument.select("h2[class=board_title]").html();
                description = profileDocument.select("h3[class]").html();
                //Use picasso for image handling later
                profileName = header;


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            profilePicture.setImageBitmap(profileBitmap);
            headerText.setText(header);

            headerText.setTypeface(null, Typeface.BOLD);
            descriptionText.setText(description);


        }
    }

    public void clearData() {
        threadRecycler.removeAllViews();
        int size = this.threadObjectList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.threadObjectList.remove(0);
            }


        }
    }


    public void openProfilePage(View v) {
        //profile page activity when user clicks on his image in the navigation menu
        // don't forget to send in url
        Intent i = new Intent(this, ProfilePage.class);
        i.putExtra("Profile Name", profileName);
        i.putExtra("Profile Link",profileLink);
        i.putExtra("Profile Posts",profilePostLink);
        i.putExtra("Profile Topics",profileTopicsLink);

        startActivity(i);

    }
public static ImageView getProfilePic(){
    return profilePicture;
}

}
