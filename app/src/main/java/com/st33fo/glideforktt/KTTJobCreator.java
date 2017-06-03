package com.st33fo.glideforktt;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Created by Stefan on 6/2/2017.
 */
class KTTJobCreator implements JobCreator {

    @Override
    public Job create(String tag) {
        switch (tag) {
            case ShowNotificationJob.TAG:
                return new ShowNotificationJob();
            default:
                return null;
        }
    }
}