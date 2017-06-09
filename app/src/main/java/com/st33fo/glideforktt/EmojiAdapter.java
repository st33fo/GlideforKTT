package com.st33fo.glideforktt;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import static com.st33fo.glideforktt.MessageBoard.sendMessage;

/**
 * Created by Stefan on 6/8/2017.
 */
public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder>{
    Context context;
    List<EmojiObject> emojiObjectList;

    EmojiAdapter(Context context,List<EmojiObject>emojiObjectList){
        this.context = context;
        this.emojiObjectList = emojiObjectList;
    }

    @Override
    public EmojiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_emoji, parent, false);
        EmojiViewHolder pvh = new EmojiViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(EmojiViewHolder holder, final int position) {

        Glide.with(context).load(emojiObjectList.get(position).imageId).apply(RequestOptions.fitCenterTransform()).into(holder.imageButton);
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String previousText = sendMessage.getText().toString();
                String emoji = emojiObjectList.get(position).emojiName;
                sendMessage.setText(previousText+" "+emoji);
                sendMessage.setSelection(sendMessage.length());
            }
        });
        holder.imageButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        return emojiObjectList.size();
    }

    public static class EmojiViewHolder extends RecyclerView.ViewHolder{
        ImageButton imageButton;

        EmojiViewHolder(View itemView){
            super(itemView);
            imageButton = (ImageButton) itemView.findViewById(R.id.imageButton);

        }

    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
