package com.st33fo.glideforktt;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.CountDownLatch;


public class Feed extends AppCompatActivity  {
    private Toolbar myToolbar;
    private Toolbar cardViewToolBar;
    private RecyclerView feedRecyclerView;
    private static String URL = "http://www.kanyetothe.com/forum/index.php?action=recent";
    private LinearLayoutManager myLinearLM;
    private FeedObject feed;
    private List<String> image_url = new ArrayList<String>();
    private List<String> Username = new ArrayList<String>();
    List<FeedObject> feeds = new ArrayList<FeedObject>();
    List<String> links = new ArrayList<String>();
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private Bundle mBundleRecyclerViewState;
    private String sessionId;
    private static RequestQueue myRequestQueue = null;

    FeedAdapter feedAdapter;
    private SwipeRefreshLayout swipe;
    private AsyncTask activitystart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        CookieHandler.setDefault(new CookieManager());
        myToolbar = (Toolbar) findViewById(R.id.feedToolBar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        feedRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        sessionId = SecuredSharePreference.getPrefCookies(Feed.this);

        myLinearLM = new LinearLayoutManager(Feed.this);


        feedRecyclerView.setLayoutManager(myLinearLM);

        new getFeed().execute();


        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                clearData();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    new getFeed().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                else
                    new getFeed().execute();




            }
        });


    }

//    public Document GetDocument(String site) throws Exception {
//        final Document[] doc = new Document[1];
//        final CountDownLatch cdl = new CountDownLatch(1);
//
//
//        StringRequest documentRequest = new StringRequest( //
//                Request.Method.GET, //
//                site, //
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        doc[0] = Jsoup.parse(response);
//
//                        cdl.countDown();
//                    }
//                }, //
//
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Error handling
//                        System.out.println("Houston we have a problem ... !");
//                        error.printStackTrace();
//                    }
//                }
//        ){
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
//                params.put("Cookie","PHPSESSID=" +sessionId);
//
//
//
//
//                return params;
//            }
//
//        };
//
//
//
//        if (myRequestQueue == null) {
//            myRequestQueue = Volley.newRequestQueue(this);
//        }
//
//        // Add the request to the queue...
//
//        myRequestQueue.add(documentRequest);
//
//        // ... and wait for the document.
//        // NOTA: Be aware of user experience here. We don't want to freeze the app...
//        cdl.await();
//
//        return doc[0];
//    }





    private class getFeed extends AsyncTask<Void, Void, Void> {
        Document profileDocument;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            clearData();



        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                profileDocument = new GetDocument(Feed.this).GetDocument(URL);
                Elements lists = profileDocument.getElementsByClass("topicindex");

                for (Element message : lists) {
                    feed = new FeedObject();
                    String quote = message.select("blockquote[class=bbc_standard_quote]").text();
                    Elements quotes = message.select("blockquote[class=bbc_standard_quote]");
                    String quoteheader =  message.select("div[class=quoteheader]").text();
                    String title = message.select("span[class=category_header]").text();
                    String timeposted = message.select("div[class=post_date]").text();
                    String post = message.select("div[class=post_body]").text();
                    String section = title.substring(title.indexOf("["),title.indexOf("]")+1);

                    title = title.replace(section,"");
                    title = title.replace("Re:","");
                    title = title.trim();
                    section = section.replace("[","");
                    section = section.replace("]","");

                    if(post.contains(quote)&&!quote.equals("")) {
                        post = post.replace(quote, "");
                        post = post.replace(quoteheader, "");
                        post = post.trim();
                        if(quoteheader.contains("Quote from:")) {
                            quoteheader = quoteheader.replace("Quote from: ", "");
                        }
                        quoteheader = " ~ " + quoteheader;
                        if (quoteheader.contains(" ago") && !quoteheader.contains("yesterday")) {
                            quoteheader = quoteheader.replace(quoteheader.substring(quoteheader.indexOf(" on "),
                                    quoteheader.indexOf("ago") + 3), "");
                        }
                        int counter = 1;
                        List<String> quotelist = new ArrayList<String>();
                        for(Element topicquote: quotes){

                            quote =  topicquote.select("blockquote[class=bbc_standard_quote]").text();

                            quotelist.add(quote);
                            counter++;
                        }

                        String finalquoteblocks = " ";
                        for(int x =0; x<quotelist.size();x++){
                            finalquoteblocks+=quotelist.get(x)+"\n";

                        }

                        feed.setQuote(finalquoteblocks + "\n" + quoteheader);
                    }

                    feed.setTitle(title);
                    feed.setPerson(section);
                    feed.setImage("");
                    feed.setTimeposted(timeposted);
                    feed.setMessage(post);
                    feeds.add(feed);


                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            feedAdapter = new FeedAdapter(Feed.this, feeds);

            feedRecyclerView.setAdapter(feedAdapter);


            swipe.setRefreshing(false);

//            new loadImages().execute();

        }


    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feed, menu);

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
        if (item.getItemId() == R.id.action_refresh) {

        }

        return super.onOptionsItemSelected(item);
    }

    public void clearData() {
        Username.clear();
        image_url.clear();
        int size = this.feeds.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.feeds.remove(0);
            }


        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = feedRecyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // restore RecyclerView state
        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            feedRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

}