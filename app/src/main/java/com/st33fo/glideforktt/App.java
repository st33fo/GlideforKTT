package com.st33fo.glideforktt;

import android.app.Application;

import com.evernote.android.job.JobManager;

/**
 * Created by Stefan on 6/12/2017.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JobManager.create(this).addJobCreator(new KTTJobCreator());
    }
}
