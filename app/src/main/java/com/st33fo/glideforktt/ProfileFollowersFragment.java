package com.st33fo.glideforktt;

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
public class ProfileFollowersFragment extends Fragment {
    private FollowerListAdapter followerListAdapter;
    private FollowerObject followerObject;
    private List<FollowerObject> followerObjectList = new ArrayList<FollowerObject>();
    private RecyclerView followerRecycler;
    private LinearLayoutManager myLinearLM;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String profileFollowers;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.profilefollowersfragment,container,false);
        System.out.println("You are currently on the followers fragment");
        followerRecycler = (RecyclerView)layout.findViewById(R.id.followerFrag);
        swipeRefreshLayout = (SwipeRefreshLayout)layout.findViewById(R.id.swipeFragment);
        myLinearLM = new LinearLayoutManager(getActivity());
        followerRecycler.setLayoutManager(myLinearLM);
        Intent i = getActivity().getIntent();
        swipeRefreshLayout.setRefreshing(true);
        profileFollowers = i.getStringExtra("Profile Link");
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getFollowers().execute();
            }
        });

        new getFollowers().execute();
        return layout;
    }
    public void clearData() {

        int size = this.followerObjectList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.followerObjectList.remove(0);
            }


        }
    }

    /**
     * Don't forget to wrap these in a swupetoreresh layout
     * All the fragments. Can't have a blank screen loading...
     */

    public class getFollowers extends AsyncTask<Void,Void,Void>{
        Document profilePage;

        @Override
        protected void onPreExecute() {
            swipeRefreshLayout.setRefreshing(true);
            super.onPreExecute();
            clearData();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                System.out.println("This is the topic link " + ProfilePage.getTopics());
                profilePage = new GetDocument(getActivity()).GetDocument("http://www.kanyetothe.com/forum/index.php?action=profile;"+profileFollowers);


                profilePage.select("div[id=profile_more]").remove();
                Elements followers = profilePage.select("div[class=user_info]").select("div[style]").tagName("p").select("a[href]");

                for(Element follower:followers){
                   followerObject = new FollowerObject();
                    followerObject.setProfileName(follower.text());
                    followerObject.setProfileURL("http://www.kanyetothe.com/forum/index.php" +follower.select("a[href]").attr("href"));

                    followerObjectList.add(followerObject);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            swipeRefreshLayout.setRefreshing(false);
            followerListAdapter = new FollowerListAdapter(getActivity(),followerObjectList);

            followerRecycler.setAdapter(followerListAdapter);

        }
    }



}
