package com.st33fo.glideforktt;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Stefan on 8/8/2016.
 */
public class FollowerListAdapter extends RecyclerView.Adapter<FollowerListAdapter.FollowerListViewHolder>{
    Context context;
    List<FollowerObject> followerObjects;

    FollowerListAdapter(Context context, List<FollowerObject> followerObjects){
        this.context = context;
        this.followerObjects = followerObjects;
    }
    public static class FollowerListViewHolder extends RecyclerView.ViewHolder{
        Toolbar myToolbar;
        TextView profileName;
        String URL;

        FollowerListViewHolder(View itemView){
            super(itemView);
            myToolbar = (Toolbar) itemView.findViewById(R.id.followerToolbar);
            profileName = (TextView) itemView.findViewById(R.id.profileName);
            myToolbar.inflateMenu(R.menu.menu_followercards);
        }
    }

    @Override
    public FollowerListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.followercards, parent, false);
        FollowerListViewHolder pvh = new FollowerListViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(FollowerListViewHolder holder, int position) {
        holder.profileName.setText(followerObjects.get(position).getProfileName());
        holder.URL = followerObjects.get(position).getProfileURL();

    }

    @Override
    public int getItemCount() {
        return followerObjects.size();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
