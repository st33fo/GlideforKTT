package com.st33fo.glideforktt;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Stefan on 7/14/2016.
 */
public class MessageBoardAdapter extends RecyclerView.Adapter<MessageBoardAdapter.MessageBoardViewHolder> {
    Context context;
    List<MessageBoardObject> messageBoardObjects;

    MessageBoardAdapter(Context context,List<MessageBoardObject> messageBoardObjects){
        this.messageBoardObjects = messageBoardObjects;
        this.context = context;
    }


    public static class MessageBoardViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView person;
        TextView time;
        ImageView profilepicture;
        TextView quote;
        TextView message;
        String quotelink;
        String profileLink;

        MessageBoardViewHolder(View itemView){
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.messageboardCardView);
            person = (TextView)itemView.findViewById(R.id.messageboardPersonName);
            time = (TextView)itemView.findViewById(R.id.messageBoardTime);
            profilepicture = (ImageView)itemView.findViewById(R.id.messageboardprofilepicture);
            quote = (TextView) itemView.findViewById(R.id.messageboardQuote);
            message = (TextView)itemView.findViewById(R.id.messageBoardMessage);


        }



    }

    @Override
    public MessageBoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_messageboardcards, parent, false);
        MessageBoardViewHolder pvh = new MessageBoardViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(MessageBoardViewHolder holder, final int position) {

        holder.person.setText(messageBoardObjects.get(position).getPerson());
        holder.time.setText(messageBoardObjects.get(position).getTime());
        holder.quote.setText(messageBoardObjects.get(position).getQuote());
        holder.message.setText(messageBoardObjects.get(position).getMessage());
        holder.quotelink= messageBoardObjects.get(position).getQuotelink();
        holder.profileLink = messageBoardObjects.get(position).getProfileLink();
        if(messageBoardObjects.get(position).getQuote().equals(""+"\n"+"")){
            holder.quote.setVisibility(View.GONE);
        }else{
            holder.quote.setVisibility(View.VISIBLE);
        }
        if(messageBoardObjects.get(position).getProfilepicture().equals("")) {
            holder.profilepicture.setImageResource(R.drawable.kttprofileicon);
        }else{
            Picasso.with(context).load(messageBoardObjects.get(position).getProfilepicture()).into(holder.profilepicture);

        }
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MessageBoard.setQuoteString(messageBoardObjects.get(position).getQuotelink());
                Toast.makeText(context,"Quoted " + messageBoardObjects.get(position).getPerson(),Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        holder.profilepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String person = messageBoardObjects.get(position).getPerson();
                String profileLink = messageBoardObjects.get(position).getProfileLink();
                System.out.println("This is the profile link tho"+profileLink);


                Intent i = new Intent(context,ProfilePage.class);
                i.putExtra("Profile Name",person);
                i.putExtra("Profile Link",profileLink);

                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return messageBoardObjects.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
