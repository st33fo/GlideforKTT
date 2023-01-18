# GlideforKTT
Alpha stage mobile application for Kanyetothe.com

[My Subreddit: ask questions and report crashes or suggest improvements there!](https://reddit.com/r/glideforktt)


I'll admit, I'm a mess when it comes to coding. That includes comments, which I'm trying my best to get better at. Apparently it helps to have em if you're working on and off on a project! It also makes it a lot clearer to read and understand whats going on. The purpose of this thread is for me to elaborate upon everything in that github page...or at least the things that are important to continuing this project.

**What you see on [Github](https://github.com/st33fo/GlideforKTT)**

What you'll see on github will be the app folder, license and readme. It's the apache license and a readme full of screenshots. 
The real meat lies in the app folder.

**Java Files**

Clicking the app folder followed by src, then main, you'll see a java/com/st33fo/glideforktt folder and a res folder. Let's dive into the java folder. There are 29 .java files in total (hopefully we'll have some more!) 

The first file starts in the tabs folder which contains the **slidingtablayout** as well as the **slidingstrip** tab. The slidingtablayout and slidingstrip both contain open sourced code I used for the profile page view which utilizes the sliding tab layout.

[profile page](https://i.imgur.com/UIsmVTh.png)

The code in here isn't really changed as it's a straight forward implementation. Customization such as font color or tab color is done in the layout xml files.

The next java file would be the **endlessrecyclerviewscrolllistener.** Again, this is more code that's used in implementation. It's used to scroll up infinitely in the thread to see all previous replies.

[infinite scroll example]

Next up is **Feed.java**. This is the recents feed that the KTT website features. It updates with new posts every couple of seconds. Unfortunately my app can't do that and probably won't because updating every 3 seconds and refreshing the recyclerview is probably annoying. Although, I'm not opposed to having some kind of time interval for automatic refreshing or a snackbar alerting of new posts. The feed.java utilizes the feed objects that I put into the cards such as the subject, the author, the time posted, and other parameters. It places them in a vertical list that can be scrolled down on and refreshed. It gets these posts via a volley document request. This is what gets the information. It's not up as of today, but feed.java should utilize the GetDocument.java class for getting the website not the subclass it currently uses. I use Jsoup to analyze the html and grab the information I need to stuff into the author, title, time posted, etc... Using forloops, I'm able to place a new feedobject in an arraylist  for every feed message.

**FeedAdapter and FeedObject** both are the adapter and object respectively for each feed item. The adapter is used to stick the objects to each of the recyclerview items. An adapter makes it easy to make a card full of information. This process is then automated every time volley request the information and boom you have a recyclerview full of cards.

[the feed](https://i.imgur.com/S1bTKvu.png)

**FollowerListAdapter** and **FollowerObject** both contribute to the **ProfileFollowersfragment**. The follower object contains the URL of the profile and the name of the profile. Currently the url under the profile objects won't do anything, but via onClick, it will lead to the profile page of that member. Combined with the adapter, these are put in a recyclerview within a fragment under the sliding tabs. Here's a gif to clear up what I mean with this description.

[Profile follower view]

Next is the **GetDocument.java class**. This class allows me to perform queries for the URLs I need. For any class that needs to lookup something they'll call a new GetDocument(Context).GetDocument(URL). Jsoup will then parse the Document object returned from this GetDocument initialization. Within the GetDocument class exists the context and the sessionID. The context will change depending on which class called the method, but the sessionID is the unique ID I use to store login information. There are no passwords stored in this application. Only the sessionID is stored locally via a SharedPreference library I use called SecuredSharedPreference. After that, it's pretty much just a string GET request and I use headers to put in the user agent and the sessionID. On every search this class is called to ensure the user is still signed in after retrieving the information he/she needs.

Then comes **Login.java**. This is the class used for the login activity with the username and password entry. It utilizes Jsoup connect function with a data(username,password) submission. If the combination is right, you should be able to login and an intent will lead you to the mainactivity class. Otherwise, it'll keep telling you the password/username is incorrect. I have yet to try this with other users yet and I'm suspicious on it working for them because of the useragent I used to get the website in full website mode. If I hadn't used it, the website would default to mobile mode and none of my queries would function.

Then comes **MainActivity.java** which should really be renamed to mainpage or dashboard, but this page features the default section opened, Kanye West. It is within a drawer layout so a swipe from the left opens the navigation drawer with access to all of the other sections as well as a drawer header that shows you a little bit of your info and an onClick takes you to your profile. This activity uses the ThreadObject which features the thread name, the author, the view count, the response amount, the latest poster, and the most recent page. The purpose of the hyperlink to the most recent page is so that you always get taken to the most recent part of the thread. The website has the ability to view the most recent page that you have seen and I may add in that option as well.

Next is **messageboard.java**. Every thread clicked on in the main activity leads straight to this class via an onClick and an intent. This is where the messages and replies are made. This activity features a response bar, which is really just a fancier toolbar at the bottom of the layout and a recyclerview for all of the messages using the messageboardobject. The messageboardobject comprises of the person name, the avatar, the time sent, the quote, the message, the quote link and the profile link. The quote refers to whether a member quoted another member. Performing this action in my application can be done by an onHold event over the message which will copy their message and the quotation format into your entry bar. When a message is quoted, that message appears in a gray box surrounded by dashed lines to indicate the difference between the current message being sent and the quote. The quote link is there so I know where to get the quote from. Some serious string manipulation went into extracting the quoted messages and it still isn't perfect. For example, if more than one person is quoted, this feature ends up screwing up sometimes.

[quote example](http://i.imgur.com/eQoVZPZ.png)


**MessageboardObject.java and MessageboardAdapter.java** both work hand in hand in delivering a nice recyclerview layout of all the responses other users have written. You might have noticed by now, but adapters and objects work with each other all the time if you want to place a bunch of similar information in a list or on a page. It makes it so you won't have to manually create a new card for every single message/post or anything that features the same properties. I'd say this combination makes up most of the code in the application. 

[messageboard example](http://i.imgur.com/IXNFN2t.png)

**ProfileFollowersFragment.java** is one of the 3 important fragments needed to make up the profile page in this application. A fragment is kind of like a sub-activity that can hold its own "things" and have its own ui like a normal activity. It can contain whatever you want in it, that includes recyclerviews. Fragments are important because sometimes the user wants to be able to see multiple clumps of different types of info in the same page without having to leave it or go back. It's a bit confusing, but hopefully you'll see what I'm trying to convey with this gif.


[fragment example]

The first fragment is the profilepostsfragment. This *inflates* or binds a layout in the res folder that features a recyclerview which stores the posts the user sends. The second fragment when you swipe to the left is the profiletopicsfragment. This one also inflates some layout that features a recyclerview that holds the topics you create. Finally another swipe lands you to the third fragment, the profilefollowersfragment which inflates a recyclerview layout with info on your followers. Notice how I didn't have to leave the ProfilePage.java activity which all of these fragments are under. You still see my profile name at the top. I just made these three quickly accessible with a couple of swipes. I could've made separate activities for them yes, but notice how that wouldn't be very efficient. Information like this should be grouped together. 

To sum up, the **ProfilePage.java** contains these 3 fragments via a viewpager and a sliding tab format that allows me to swipe to switch to different fragments.

The scrollingFabBehavior.java is not very important to functionality yet, but it makes it so the Floating Action Button hides on scroll. And the util.java is there as kind of a class where random functions are placed in a class to be used elsewhere. Right now it just contains what I had cloned from another git page, but I might add my own functions in there if its needed.

The last java class we'll worry about is the **SecuredSharedPreference.java**. This is where the cookie is stored when you log in. Every time a user logs into KanyeToThe.com, a sessionId is stored in a cookie so it knows to either keep you logged in for a long time or for a short while. In the case of this website, there is no time limit on the cookie I believe, so you can be logged in to the point where you haven't entered a password in years. This was the way I chose to bypass storing login information. It doesn't matter how secure an app is, I don't believe in storing passwords or data that can be used against you to login to your account. Through locally storing the phpsessionId, I've made it impossible for someone to login to your account unless they physically take your phone away and find where the sessionId is stored and injects it in whatever program they use to login with your account. The sharedpreference is a good way to check whether to go to the login page if the user doesn't have an account or isn't logged in. The app starts and leads you to the main activity, but it also checks if the sharedpreference cookie holds something, and if it doesn't, an intent takes you back to the login page.

This wraps it up in terms of explaining all the java classes in this application. On another day, I'll go over the layout files and how the UI works in this app!


<img src="https://i.imgur.com/1qKdvnw.png" width="350">
<img src="https://i.imgur.com/0SYiert.png" width="350">
<img src="https://i.imgur.com/eXIW10K.png" width="350">
<img src="https://i.imgur.com/S1bTKvu.png" width="350">
<img src="https://i.imgur.com/WUGNtPV.png" width="350">
<img src="https://i.imgur.com/eQoVZPZ.png" width="350">
<img src="https://i.imgur.com/UIsmVTh.png" width="350">
<img src="https://i.imgur.com/0r4dBuo.png" width="350">
