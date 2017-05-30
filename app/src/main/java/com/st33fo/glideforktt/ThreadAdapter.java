package com.st33fo.glideforktt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stefan on 7/7/2016.
 */
public class ThreadAdapter extends RecyclerView.Adapter<ThreadAdapter.ThreadViewHolder> {
    List<ThreadObject> threadObjectList;
    List<ThreadViewHolder> threadholders = new ArrayList<ThreadViewHolder>();
    static Context context;

    ThreadAdapter(List<ThreadObject> threadObjectList, Context context) {
        this.threadObjectList = threadObjectList;
        this.context = context;
    }

    public static class ThreadViewHolder extends RecyclerView.ViewHolder {
        CardView threadcv;
        TextView threadTitle;
        TextView person;
        TextView section;
        TextView latest_poster;
        TextView replies;
        TextView views;
        Toolbar cardViewToolBar;
        ImageView viewDrawable;
        ImageView repliesDrawable;
        String mostRecentPost;


        ThreadViewHolder(View itemView) {
            super(itemView);
            threadcv = (CardView) itemView.findViewById(R.id.threadcv);
            threadTitle = (TextView) itemView.findViewById(R.id.threadTitle);
            person = (TextView) itemView.findViewById(R.id.person);
            section = (TextView) itemView.findViewById(R.id.section);
            latest_poster = (TextView) itemView.findViewById(R.id.latest_poster);
            replies =  (TextView) itemView.findViewById(R.id.replies);
            views =  (TextView) itemView.findViewById(R.id.views);
            cardViewToolBar = (Toolbar) itemView.findViewById(R.id.threadtoolbar);
            viewDrawable = (ImageView)itemView.findViewById(R.id.viewicon);
            repliesDrawable = (ImageView)itemView.findViewById(R.id.replyicon);
            cardViewToolBar.inflateMenu(R.menu.menu_threadcardview);
            cardViewToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return false;
                }
            });




        }
    }
    @Override
    public int getItemCount() {
        return threadObjectList.size();
    }

    public ThreadObject getItem(int position) {
        return threadObjectList.get(position);
    }
    @Override
    public ThreadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_threadcards, parent, false);
        ThreadViewHolder pvh = new ThreadViewHolder(v);
        return pvh;
    }
    @Override
    public void onBindViewHolder(ThreadViewHolder holder, final int position) {
        ThreadObject threadObject = getItem(position);
        holder.person.setText(threadObjectList.get(position).getPerson());
        holder.threadTitle.setText(threadObjectList.get(position).getThreadtitle());
        holder.section.setText(threadObjectList.get(position).getSection());
        holder.latest_poster.setText(threadObjectList.get(position).getLatest_poster());
        holder.replies.setText(threadObjectList.get(position).getReplies());
        holder.views.setText(threadObjectList.get(position).getViews());
        holder.mostRecentPost = threadObjectList.get(position).getMostrecentpage();

        holder.cardViewToolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,MessageBoard.class);
                i.putExtra("URL",threadObjectList.get(position).getMostrecentpage());
                context.startActivity(i);
            }
        });

        holder.threadcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context,MessageBoard.class);

                i.putExtra("URL",threadObjectList.get(position).getMostrecentpage());
                context.startActivity(i);

            }
        });


        }




    @Override
    public long getItemId(int position) {
        return 0;

    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }







}
