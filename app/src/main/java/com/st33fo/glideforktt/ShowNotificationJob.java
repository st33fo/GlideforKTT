package com.st33fo.glideforktt;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.renderscript.RenderScript;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by Stefan on 6/2/2017.
 */

public class ShowNotificationJob extends Job {

    static final String TAG = "show_notification_job_tag";
    private String notificationURL = "http://www.kanyetothe.com/forum/index.php?action=notifications";
    private int notifnumber = 0;




    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        Intent messageBoard = new Intent(getContext(),MessageBoard.class);





        Document notificationpage;
        String getNotificationCount=null;
        Elements navbar = null;
        int notificationnumber =0;

        try{
            notificationpage = new GetDocument(getContext()).GetDocument(notificationURL);
            navbar = notificationpage.select("ul[id=navigation");
            getNotificationCount= navbar.select("li[id=notification_li").text();
            if(getNotificationCount.contains("Notifications")){
                getNotificationCount = getNotificationCount.replace(" Notifications","").trim();
                notificationnumber = Integer.parseInt(getNotificationCount);
            }else {
                getNotificationCount = getNotificationCount.replace(" Notification", "").trim();
                notificationnumber = Integer.parseInt(getNotificationCount);
            }

            //System.out.println(notificationnumber);


            if(notificationnumber>0){
                notifnumber++;
                Elements notifpage =notificationpage.select("div[id=cmn_wrap").select("div[id=page]").select("ul[class=notifications]");
                String profile = notifpage.select("li").select("a[href]").get(0).text();
                String thread = notifpage.select("li").select("a[href]").get(1).text();
                String URL = "http://www.kanyetothe.com"+notifpage.select("li").select("a[href]").get(1).attr("href");

                System.out.println(URL);
                messageBoard.putExtra("URL",URL);
                PendingIntent pi = PendingIntent.getActivity(getContext(), UUID.randomUUID().hashCode(), messageBoard, PendingIntent.FLAG_UPDATE_CURRENT);




                Notification groupBuilder =
                        new NotificationCompat.Builder(getContext())
                                .setGroupSummary(true)
                                .setSmallIcon(R.drawable.ic_format_quote_white_48dp)
                                .setGroup("KTT")
                                .setNumber(2)

                                .setContentIntent(pi)
                                .setAutoCancel(true)

                                .build();

                Notification notification = new NotificationCompat.Builder(getContext())
                        .setContentTitle(profile)
                        .setContentText(thread)
                        .setAutoCancel(true)
                        .setContentIntent(pi)
                        .setSmallIcon(R.drawable.ic_format_quote_white_48dp)
                        .setWhen(System.currentTimeMillis())
                        .setShowWhen(true)
                        .setGroup("KTT")
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setVibrate(new long[] { 100,300,300,300,300 })
           //later             .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL))
                        .setNumber(++notifnumber)
                        .build();



                NotificationManagerCompat.from(getContext())
                        .notify(4269352, groupBuilder);
                NotificationManagerCompat.from(getContext())
                        .notify(new Random().nextInt(), notification);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return Result.SUCCESS;
    }


    static void schedulePeriodic() {
        new JobRequest.Builder(ShowNotificationJob.TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(15))
                .setUpdateCurrent(true)
                .setPersisted(true)
                .build()
                .schedule();

    }
}
