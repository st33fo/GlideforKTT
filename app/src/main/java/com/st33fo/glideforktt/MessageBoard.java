package com.st33fo.glideforktt;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import android.widget.RelativeLayout;
import android.widget.Toast;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessageBoard extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView recyclerView;
    private Toolbar myMessageToolbar;
    protected static EditText sendMessage;
    private ImageButton sendIcon;
    private ImageButton emojiIcon;
    private static String sessionId;
    private String URL = "";
    private String DefaultURL = "";
    private LinearLayoutManager myLinearLM;
    private MessageBoardAdapter messageBoardAdapter;
    private List<MessageBoardObject> messageBoardObjectList = new ArrayList<MessageBoardObject>();
    private String userProfile ="";
    private int navPageNumber = 0;
    private int nomorescrolls = 1;
    private static String messageText = "";

    private int sendMessageNavPageNumber = 0;
    private ProgressBar progressBar;
    private Handler mHandler = new Handler();
    private AppBarLayout appBarLayout;
    private AppBarLayout messageAppBar;
    private  Connection.Response profileConnection;
    private static String quotelink = "";
    private static String profileLink="";
    private static String profileposts ="";
    private static String profileTopics ="";
    private RelativeLayout relativeLayout;
    private View v;
    private AppBarLayout.LayoutParams layoutParams;
    private RecyclerView emojiGridView;
    private RelativeLayout gridlinear;
    private List<EmojiObject> list;
    private GridLayoutManager gridLayoutManager;
    private InputMethodManager imm;
    private boolean selectEmoji = false;



private Window mRootWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_board);
        mToolbar = (Toolbar) findViewById(R.id.messageboardToolBar);
        recyclerView = (RecyclerView) findViewById(R.id.messageBoardRecycler);
        myMessageToolbar = (Toolbar) findViewById(R.id.messageBar);
        sendMessage = (EditText) findViewById(R.id.postsomethingEditText);
        appBarLayout = (AppBarLayout) findViewById(R.id.messageboardAppbar);
        sendIcon  = (ImageButton) findViewById(R.id.sendIcon);
        emojiGridView = (RecyclerView)findViewById(R.id.emoji_gridview);
        emojiGridView.setVisibility(View.GONE);












        messageAppBar = (AppBarLayout)findViewById(R.id.messageAppBar);


        /**put in onclicklistener*/

        sendIcon.setEnabled(false);
        emojiIcon = (ImageButton) findViewById(R.id.emojiIcon);








        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setTitle("MessageBoard");
        Intent intent = getIntent();
        URL = intent.getExtras().getString("URL");
        DefaultURL = intent.getExtras().getString("URL");


        sessionId = SecuredSharePreference.getPrefCookies(this);
        myLinearLM = new LinearLayoutManager(MessageBoard.this);
        myLinearLM.setSmoothScrollbarEnabled(true);

        recyclerView.setLayoutManager(myLinearLM);

        recyclerView.addOnScrollListener(new OnVerticalScrollListener() {
            @Override
            public void onScrolledDown() {
                super.onScrolledDown();
            }

            @Override
            public void onScrolledToBottom() {
                super.onScrolledToBottom();
            }

            @Override
            public void onScrolledToTop() {
                super.onScrolledToTop();
            }

            @Override
            public boolean onScrolledUp() {
                super.onScrolledUp();
                return true;
            }
        });


        new getMessages().execute();



/**
 * Lets see if we can get a disabled icon up when the user puts something in the edit text versus when they don't
 */
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emojiGridView.getVisibility()==View.VISIBLE)
                    emojiGridView.setVisibility(View.GONE);
            }
        });
        sendMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().equals("")){
                    sendIcon.setImageResource(R.drawable.ic_send);
                    sendIcon.setEnabled(false);
                }else{
                    sendIcon.setImageResource(R.drawable.ic_send_grey600_24dp);
                    sendIcon.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        sendIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageText = sendMessage.getText().toString();
                if(!messageText.trim().equals("")){
                            sendIcon.setEnabled(false);
                            sendIcon.setImageResource(R.drawable.ic_send);
                            new SaySomething().execute();

                        }else{
                    Toast.makeText(MessageBoard.this,"You gotta say something",Toast.LENGTH_SHORT).show();
                }
            }
        });
        sendIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                return true;
            }
        });
        /**
         * I am going to do a proper image button implementation here. Not that bullshit I had before
         */
        emojiIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(selectEmoji);
    if(emojiGridView.getVisibility()==View.VISIBLE) {

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        emojiGridView.setVisibility(View.GONE);
    }else{

        emojiGridView.setVisibility(View.VISIBLE);
        if(selectEmoji){
            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        }
        selectEmoji=true;

            list = EmojiListCreator.PopulateList(MessageBoard.this);
            emojiGridView = (RecyclerView) findViewById(R.id.emoji_gridview);
            gridLayoutManager = new GridLayoutManager(MessageBoard.this, 7);

            emojiGridView.setHasFixedSize(true);
            emojiGridView.setLayoutManager(gridLayoutManager);
            EmojiAdapter emojiAdapter = new EmojiAdapter(MessageBoard.this, list);
            emojiGridView.setAdapter(emojiAdapter);

    }




            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mainthreads, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menu_refresh) {

                    }

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



    public void clearData() {

        int size = this.messageBoardObjectList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.messageBoardObjectList.remove(0);
            }


        }
    }

    private class getMessages extends AsyncTask<Void, Void, Void> {
        Document messageBoard;
        String appbarTitle;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();



        }

        @Override
        protected Void doInBackground(Void... params) {


            try {

                messageBoard = new GetDocument(MessageBoard.this).GetDocument(URL);
                Elements messages = messageBoard.select("div.category.topicindex.topicview");
                String navpages = messageBoard.select("div[id=board_top_info]").select("div[class=forum_pages]").select("strong").text();
                appbarTitle = messageBoard.select("div[id=board_top_info]").select("h2[class=board_title]").text();

                if(URL.contains("topic")) {
                    String cutURL = URL.substring(URL.lastIndexOf("."));
                    URL = URL.replace(cutURL, "");
                }else{
                    URL = messageBoard.select("div[id=board_top_info]").select("div[class=forum_pages]").select("a[class=navPages").last().attr("href");
                    String cutURL = URL.substring(URL.lastIndexOf("."));
                    URL = URL.replace(cutURL, "");
                }

                navPageNumber = Integer.parseInt(navpages);

                navPageNumber = 18 * (navPageNumber - 1);
                URL = URL + "." + navPageNumber;
                System.out.println("Editted url is" + messageBoard.location());
                for (int x = messages.size() - 1; x >= 0; x--) {
                    String quote = messages.get(x).select("blockquote[class=bbc_standard_quote]").text();
                    Elements quotes = messages.get(x).select("blockquote[class=bbc_standard_quote]");
                    String quoteheader = messages.get(x).select("div[class=quoteheader]").text();
                    String username = messages.get(x).select("span[class=theactualusername]").select("a[href]").text();
                    String profilepicture = messages.get(x).select("div[style]").select("img[src]").attr("src");
                    String post = messages.get(x).select("td.the_post.an_actual_post").select("div[class=post_body").text();
                    String time = messages.get(x).select("td.the_post.an_actual_post").select("div[class=post_date]").text();
                    String proflink = messages.get(x).select("span[class=theactualusername]").select("a[href]").attr("href");
                    String quotebuttonlinksto = messages.get(x).select("div.post_buttons").select("a[href]").attr("href");

                    if (post.contains("INSERT_RANDOM_NUMBER_HERE")) {
                        post = post.replace(post.substring(post.indexOf("<a "), post.indexOf("</a>") + 4), "");

                    }
                    MessageBoardObject messageBoardObject = new MessageBoardObject();
                    if (post.contains(quote) && !quote.equals("")) {
                        post = post.replace(quote, "");
                        post = post.replace(quoteheader, "");
                        post = post.trim();
                        quoteheader = " ~ " + quoteheader;
                        if (quoteheader.contains(" ago") && !quoteheader.contains("yesterday")) {
                            quoteheader = quoteheader.replace(quoteheader.substring(quoteheader.indexOf(" on "),
                                    quoteheader.indexOf("ago") + 3), "");
                        }
                    } else {

                        messageBoardObject.setQuote(quote);
                    }
                    int counter = 1;
                    List<String> quotelist = new ArrayList<String>();
                    for (Element topicquote : quotes) {

                        quote = topicquote.select("blockquote[class=bbc_standard_quote]").text();

                        quotelist.add(quote);
                        counter++;
                    }

                    String finalquoteblocks = " ";
                    for (int y = 0; y < quotelist.size(); y++) {
                        finalquoteblocks += quotelist.get(y) + "\n";
                    }

                    messageBoardObject.setQuote(finalquoteblocks.trim() + "\n" + quoteheader.trim());

                    messageBoardObject.setQuotelink(quotebuttonlinksto);
                    messageBoardObject.setPerson(username);
                    messageBoardObject.setProfilepicture(profilepicture);
                    proflink = proflink.substring(proflink.indexOf("u="));

                    messageBoardObject.setProfileLink(proflink);


                    messageBoardObject.setMessage(post);
                    messageBoardObject.setTime(time);

                    messageBoardObjectList.add(0, messageBoardObject);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getSupportActionBar().setTitle(appbarTitle);
            messageBoardAdapter = new MessageBoardAdapter(MessageBoard.this, messageBoardObjectList);
            recyclerView.setAdapter(messageBoardAdapter);


            recyclerView.scrollToPosition(18);
//            messageBoardAdapter.notifyItemRangeChanged(0,messageBoardObjectList.size());
            /**
              We aren't going to add the previous list to the recyclerview here. We have to figure out
             a better way of loading more items in the background without it changing the UI, after it
             tries to perform the function again
             **/
            if (navPageNumber!=0 &&messageBoardObjectList.size() < 3) {
                navPageNumber -= 18;
                String cutURL = URL.substring(URL.lastIndexOf("."));
                URL = URL.replace(cutURL, "");
                URL = URL + "." + navPageNumber;

                new getMessages().execute();


            }






        }
    }

    private class SaySomething extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
           try{

               Document postpage = new GetDocument(MessageBoard.this).GetDocument(URL);
               String topicvalue = postpage.select("div[id=forum_wide]").select("div[id=forum_wide]").select("form[name=postmodify]")
                       .select("input").get(0).val();
               String subjecttopic =  postpage
                      .select("div[id=forum_wide]").select("div[id=forum_wide]").select("form[name=postmodify]")
                       .select("input").get(1).val();
               String iconvalue =  postpage
                      .select("div[id=forum_wide]").select("div[id=forum_wide]").select("form[name=postmodify]")
                       .select("input").get(2).val();
               String fromqrvalue =  postpage
                      .select("div[id=forum_wide]").select("div[id=forum_wide]").select("form[name=postmodify]")
                       .select("input").get(3).val();
               String notifyvalue = postpage.select("div[id=forum_wide]").select("div[id=forum_wide]").select("form[name=postmodify]").select("input").get(4).val();
               String notapprovedvalue =  postpage.select("div[id=forum_wide]").select("div[id=forum_wide]").select("form[name=postmodify]").select("input").get(5).val();
               String gobackvalue = postpage.select("div[id=forum_wide]").select("div[id=forum_wide]").select("form[name=postmodify]").select("input").get(6).val();
               String repliesvalue = postpage.select("div[id=forum_wide]").select("div[id=forum_wide]").select("form[name=postmodify]").select("input").get(7).val();
               String sessionname = postpage.select("div[id=forum_wide]").select("div[id=forum_wide]").select("form[name=postmodify]").select("input").get(8).attr("name");
               String sessionvalue = postpage.select("div[id=forum_wide]").select("div[id=forum_wide]").select("form[name=postmodify]").select("input").get(8).val();
               String seqnumvalue =  postpage.select("div[id=forum_wide]").select("div[id=forum_wide]").select("form[name=postmodify]").select("input").get(9).val();




		Document poststuff = Jsoup
				.connect(
						postpage.select("div[id=forum_wide]").select("form[name=postmodify").attr("action"))
				.cookie("PHPSESSID", sessionId).data("topic", topicvalue)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
				.data("subject", subjecttopic).data("icon", iconvalue)
				.data("from_qr", fromqrvalue).data("notify", notifyvalue)
				.data("not_approved", notapprovedvalue).data("goback", gobackvalue)
				.data("num_replies", repliesvalue).data(sessionname, sessionvalue)
				.data("seqnum", seqnumvalue).data("message", messageText)
				.post();

               URL = poststuff.baseUri();


           }catch(Exception e){
               e.printStackTrace();
           }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            clearData();
            recyclerView.removeAllViews();
            sendMessage.setText("");

            new getMessages().execute();



        }
    }

    public static void setQuoteString(String quote){
        quotelink = quote;
        new quote().execute();
    }
    public static class quote extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Connection.Response posts = Jsoup.connect(quotelink)
                        .userAgent(
                                "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
                        .cookie("PHPSESSID", sessionId).execute();

                Document postlist= posts.parse();
                String textview = postlist.select("textarea[class=editor]").text();

                messageText = textview+"\n"+"\n";
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            String previoustext = sendMessage.getText().toString();
            sendMessage.setText(previoustext+"\n"+messageText);
            sendMessage.setSelection(sendMessage.length());
            super.onPostExecute(aVoid);
        }
    }

    public abstract class OnVerticalScrollListener
            extends RecyclerView.OnScrollListener {

        @Override
        public final void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (!recyclerView.canScrollVertically(-1)) {
                onScrolledToTop();
            } else if (!recyclerView.canScrollVertically(1)) {
                onScrolledToBottom();
            } else if (dy < 0) {
                onScrolledUp();
            } else if (dy > 0) {
                onScrolledDown();
            }
        }

        public boolean onScrolledUp() {
            return true;

        }

        public void onScrolledDown() {

        }

        public void onScrolledToTop() {
            if (navPageNumber != 0 && onScrolledUp()) {
                navPageNumber -= 18;
                String cutURL = URL.substring(URL.lastIndexOf("."));
                URL = URL.replace(cutURL, "");
                URL = URL + "." + navPageNumber;
                new getMessages().execute();

            }
        }


        public void onScrolledToBottom() {
        }
    }


    public static String getTopics(){
       return profileTopics;
    }
    public static String getPosts(){
        return profileTopics;
    }
    public static String getProfileLink(){
        return profileLink;
    }
}
