package com.st33fo.glideforktt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Stefan on 7/1/2016.
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {
    Context context;

    List<FeedObject> feedObjectList;



    FeedAdapter(Context context,List<FeedObject> feedObjectList) {
        this.feedObjectList = feedObjectList;
        this.context=context;

    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder {
        int var_visibility = View.VISIBLE;
        CardView cardView;
        TextView title;
        TextView message;
        TextView person;
        TextView quote;
        ImageView image;
        TextView timeposted;
        Toolbar cardViewToolBar;
        String link;


        FeedViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.person_photo);
            cardView = (CardView) itemView.findViewById(R.id.cv);
            title = (TextView) itemView.findViewById(R.id.threadHeader);
            message = (TextView) itemView.findViewById(R.id.postcontent);
            person = (TextView) itemView.findViewById(R.id.poster);
            quote = (TextView) itemView.findViewById(R.id.quote);
            cardViewToolBar = (Toolbar)itemView.findViewById(R.id.card_toolbar);
            cardViewToolBar.inflateMenu(R.menu.menu_cardview);



            cardViewToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return false;
                }
            });
            timeposted = (TextView) itemView.findViewById(R.id.timeposted);

        }

    }

    @Override
    public int getItemCount() {
        return feedObjectList.size();
    }

    public FeedObject getItem(int position) {
        return feedObjectList.get(position);
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cardview, parent, false);
        FeedViewHolder pvh = new FeedViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, final int position) {
        FeedObject feedObject = getItem(position);
        holder.person.setText(feedObjectList.get(position).getPerson());
        holder.message.setText(feedObjectList.get(position).getMessage());
        holder.timeposted.setText(feedObjectList.get(position).getTimeposted());
        holder.title.setText(feedObjectList.get(position).getTitle());
        holder.quote.setText(feedObjectList.get(position).getQuote());
        holder.link = feedObjectList.get(position).getLink();
        holder.cardViewToolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context,MessageBoard.class);
                i.putExtra("URL",feedObjectList.get(position).getLink());
                context.startActivity(i);
            }
        });
        if(feedObjectList.get(position).getQuote().equals(" ")){
           holder.quote.setVisibility(View.GONE);
        }else{
            holder.quote.setVisibility(View.VISIBLE);
        }
    if(feedObjectList.get(position).getImage().equals("")) {
        holder.image.setVisibility(View.GONE);
    }else{
        Picasso.with(context).load(feedObject.getImage()).into(holder.image);

    }



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
