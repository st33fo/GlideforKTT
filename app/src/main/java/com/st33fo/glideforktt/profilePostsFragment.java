package com.st33fo.glideforktt;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Stefan on 8/6/2016.
 */
public class profilePostsFragment extends Fragment {
    private List<FeedObject> profileFeeds = new ArrayList<FeedObject>();
    private LinearLayoutManager myLinearLM;
    private RecyclerView profileFeedRecyclerView;
    private String profileposts = "";
    private SwipeRefreshLayout swipeRefreshLayoutl;
    FeedAdapter profileFeedAdapter;

    private String sessionId = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.profilepostfragment, container, false);
        profileFeedRecyclerView = (RecyclerView) layout.findViewById(R.id.postfragrv);
        swipeRefreshLayoutl = (SwipeRefreshLayout)layout.findViewById(R.id.swipeFragment);
        swipeRefreshLayoutl.setRefreshing(true);

        myLinearLM = new LinearLayoutManager(getActivity());
        profileFeedRecyclerView.setLayoutManager(myLinearLM);
        Intent i = getActivity().getIntent();
        profileposts = i.getStringExtra("Profile Link");
        System.out.println("THe post link is " + profileposts);

        sessionId = SecuredSharePreference.getPrefCookies(getActivity());
        swipeRefreshLayoutl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getPosts().execute();
            }
        });
        new getPosts().execute();
        return layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    public void clearData() {

        int size = this.profileFeeds.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.profileFeeds.remove(0);
            }


        }
    }

    private class getPosts extends AsyncTask<Void, Void, Void> {

        Document profilepostDoc;
        FeedObject profileFeed;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayoutl.setRefreshing(true);
            clearData();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                profilepostDoc = new GetDocument(getActivity()).GetDocument("http://www.kanyetothe.com/forum/index.php?action=profile;area=showposts;"+profileposts);


                Elements lists = profilepostDoc.getElementsByClass("topicindex");
                for (Element message : lists) {
                    profileFeed = new FeedObject();
                    String quote = message.select("blockquote[class=bbc_standard_quote]").text();
                    Elements quotes = message.select("blockquote[class=bbc_standard_quote]");
                    String quoteheader = message.select("div[class=quoteheader]").text();
                    String title = message.select("span[class=category_header]").text();
                    String timeposted = message.select("div[class=post_date]").text();
                    String post = message.select("div[class=post_body]").text();
                    String section = title.substring(title.indexOf("["), title.indexOf("]") + 1);

                    title = title.replace(section, "");
                    title = title.replace("Re:", "");
                    title = title.trim();
                    section = section.replace("[", "");
                    section = section.replace("]", "");

                    if (post.contains(quote) && !quote.equals("")) {
                        post = post.replace(quote, "");
                        post = post.replace(quoteheader, "");
                        post = post.trim();
                        if (quoteheader.contains("Quote from:")) {
                            quoteheader = quoteheader.replace("Quote from: ", "");
                        }
                        quoteheader = " ~ " + quoteheader;
                        if (quoteheader.contains(" ago") && !quoteheader.contains("yesterday")) {
                            quoteheader = quoteheader.replace(quoteheader.substring(quoteheader.indexOf(" on "),
                                    quoteheader.indexOf("ago") + 3), "");
                        }
                        int counter = 1;
                        List<String> quotelist = new ArrayList<String>();
                        for (Element topicquote : quotes) {

                            quote = topicquote.select("blockquote[class=bbc_standard_quote]").text();

                            quotelist.add(quote);
                            counter++;
                        }

                        String finalquoteblocks = " ";
                        for (int x = 0; x < quotelist.size(); x++) {
                            finalquoteblocks += quotelist.get(x) + "\n";

                        }

                        profileFeed.setQuote(finalquoteblocks + "\n" + quoteheader);
                    }

                    profileFeed.setTitle(title);
                    profileFeed.setPerson(section);
                    profileFeed.setImage("");
                    profileFeed.setTimeposted(timeposted);
                    profileFeed.setMessage(post);
                    profileFeeds.add(profileFeed);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            profileFeedAdapter = new FeedAdapter(getActivity(), profileFeeds);
            swipeRefreshLayoutl.setRefreshing(false);

            profileFeedRecyclerView.setAdapter(profileFeedAdapter);
        }
    }
}
