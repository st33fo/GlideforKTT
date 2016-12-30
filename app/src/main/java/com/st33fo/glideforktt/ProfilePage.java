package com.st33fo.glideforktt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.st33fo.glideforktt.tabs.SlidingTabLayout;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

public class ProfilePage extends AppCompatActivity {

    private Toolbar myToolbar;
    private SlidingTabLayout mySlidingTabLayout;
    private ViewPager myViewPager;
    private  String profileName ="";
    private String postNumber ="";
    private TextView postCount;
    private TextView aboutme;
    private static String profileLink;
    private static String profilePost;
    private static String profileTopics;
    private ImageView profilepic;
    private  Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        myToolbar = (Toolbar) findViewById(R.id.profileToolbar);
        myViewPager = (ViewPager) findViewById(R.id.profilepager);

        myViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mySlidingTabLayout = (SlidingTabLayout) findViewById(R.id.slidingtabs);
        mySlidingTabLayout.setDistributeEvenly(true);
       i = getIntent();
        profileName = i.getStringExtra("Profile Name");
        profileLink = i.getStringExtra("Profile Link");
        System.out.println("The profile Link is" + profileLink);
        profilePost = i.getStringExtra("Profile Posts");
        System.out.println("The post link is " +profilePost);
        profileTopics = i.getStringExtra("Profile Topics");
        System.out.println("The topic link is " + profileTopics);
        mySlidingTabLayout.setViewPager(myViewPager);
        mySlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mySlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.colorAccent));
        myToolbar.setTitle(profileName);


        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);




    }


    class MyPagerAdapter extends FragmentPagerAdapter{


        String[] tabs;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new profilePostsFragment();
                case 1:
                    return new ProfileTopicsFragment();
                case 2:
                    return new ProfileFollowersFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profiletoolbar, menu);
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
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        if(id==R.id.profileButton){
            new getProfileCard().execute();

        }


        return super.onOptionsItemSelected(item);
    }

    private class getProfileCard extends AsyncTask<Void,Void,Void>{
    Document profileDocument;
        String imageLink ="";
        String about = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            postNumber = "";
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                System.out.println("This is the topic link " + ProfilePage.getTopics());
                profileDocument = new GetDocument(ProfilePage.this).GetDocument("http://www.kanyetothe.com/forum/index.php?action=profile;"+profileLink);

                Elements infos = profileDocument.select("div[class=user_info]");
                profileDocument.select("a[class=more_link]").remove();
                profileDocument.select("div[style]").tagName("p").remove();
                imageLink = profileDocument.select("div[id=profile_left]").select("img[class=avatar]").attr("src");
                about = profileDocument.select("div[id=profile_right]").tagName("p").text();
                about = about.substring(about.indexOf("About Me")+9,about.indexOf("Info Posts:"));
                postNumber+=infos.text();
                String location ="";
                String posts ="";
                if (postNumber.contains("Location")) {
                    posts = postNumber.substring(postNumber.indexOf("Posts:"), postNumber.indexOf("Location"));
                    location = postNumber.substring(postNumber.indexOf("Location:"), postNumber.indexOf("Registered"));
                }else {
                    location ="Location: Not Specified";
                    posts = postNumber.substring(postNumber.indexOf("Posts:"), postNumber.indexOf("Registered"));
                }

                String registered = postNumber.substring(postNumber.indexOf("Registered:"),postNumber.indexOf("Last active:"));
                String lastActive = postNumber.substring(postNumber.indexOf("Last active:"));

                postNumber = posts +"\n"+location + "\n" +registered +"\n"+ lastActive;








            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MaterialDialog dialog = new MaterialDialog.Builder(ProfilePage.this)
                    .title(profileName)
                    .buttonRippleColorRes(R.color.colorAccent)
                    .customView(R.layout.layout_profiledialog,true)
                    .positiveText("Okay")
                    .build();
            View view = dialog.getCustomView();

            if (view != null) {
                aboutme= (TextView)view.findViewById(R.id.aboutme);
                postCount = (TextView) view.findViewById(R.id.postNumber);
                profilepic = (ImageView)view.findViewById(R.id.profilePicture);

                if(!imageLink.equals("")) {
                    Picasso.with(ProfilePage.this).load(imageLink).into(profilepic);
                }else{
                    profilepic.setImageResource(R.drawable.kttprofileicon);
                }


            }
            aboutme.setText(about);
            postCount.setText(postNumber);

            dialog.show();
        }
    }
    public static String getLink(){
        return profileLink;
    }
    public static String getPostLink(){
        return  profilePost;
    }
    public static String getTopics(){
        return profileTopics;
        }
}
