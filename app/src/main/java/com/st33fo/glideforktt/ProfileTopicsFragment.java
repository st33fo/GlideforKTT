package com.st33fo.glideforktt;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
public class ProfileTopicsFragment extends Fragment {
    private List<FeedObject> topicFeeds = new ArrayList<FeedObject>();
    private LinearLayoutManager myLinearLM;
    private RecyclerView topicFeedRecyclerView;
    private String topicPost;
    private SwipeRefreshLayout swipeRefreshLayout;
    FeedAdapter topicFeedAdapter;

    private String sessionId = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.profiletopicsfragment, container, false);
        topicFeedRecyclerView = (RecyclerView) layout.findViewById(R.id.topicrv);
        swipeRefreshLayout = (SwipeRefreshLayout)layout.findViewById(R.id.swipeFragment);
        System.out.println("You are in the profiletopicsfragment");
        myLinearLM = new LinearLayoutManager(getActivity());
        sessionId = SecuredSharePreference.getPrefCookies(getActivity());
        swipeRefreshLayout.setRefreshing(true);

        Intent i = getActivity().getIntent();
        topicPost = i.getStringExtra("Profile Link");
        topicFeedRecyclerView.setLayoutManager(myLinearLM);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getTopics().execute();
            }
        });
        new getTopics().execute();
        return layout;

    }
    public void clearData() {

        int size = this.topicFeeds.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.topicFeeds.remove(0);
            }


        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    private class getTopics extends AsyncTask<Void, Void, Void> {
        Document topicPostDoc;
        FeedObject topicFeed;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
            clearData();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                System.out.println("This is the topic link " + ProfilePage.getTopics());
                topicPostDoc = new GetDocument(getActivity()).GetDocument("http://www.kanyetothe.com/forum/index.php?action=profile;area=showposts;sa=topics;"+topicPost);


                Elements lists = topicPostDoc.getElementsByClass("topicindex");
                for (Element message : lists) {
                    topicFeed = new FeedObject();
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

                        topicFeed.setQuote(finalquoteblocks + "\n" + quoteheader);
                    }

                    topicFeed.setTitle(title);
                    topicFeed.setPerson(section);
                    topicFeed.setImage("");
                    topicFeed.setTimeposted(timeposted);
                    topicFeed.setMessage(post);
                    topicFeeds.add(topicFeed);
                }


                }catch(Exception e){
                    e.printStackTrace();
                }
                return null;

            }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            swipeRefreshLayout.setRefreshing(false);
            topicFeedAdapter = new FeedAdapter(getActivity(), topicFeeds);

            topicFeedRecyclerView.setAdapter(topicFeedAdapter);
        }
    }


    }

