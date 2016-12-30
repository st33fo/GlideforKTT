package com.st33fo.glideforktt;

/**
 * Created by Stefan on 7/7/2016.
 */
public class ThreadObject {
    String threadtitle;
    String person;
    String section;
    String latest_poster;
    String replies;
    String views;
    String mostrecentpage;

    ThreadObject(){
        this.threadtitle=" ";
        this.person=" ";
        this.section=" ";
        this.latest_poster=" ";
        this.replies = " ";
        this.views = " ";
    }
    ThreadObject(String threadtitle,String person, String section, String latest_poster, String replies, String views, String mostrecentpage){
        this.threadtitle = threadtitle;
        this.person = person;
        this.section = section;
        this.latest_poster = latest_poster;
        this.replies = replies;
        this.views = views;
        this.mostrecentpage = mostrecentpage;

    }

    public String getMostrecentpage() {
        return mostrecentpage;
    }

    public void setMostrecentpage(String mostrecentpage) {
        this.mostrecentpage = mostrecentpage;
    }

    public String getLatest_poster() {
        return latest_poster;
    }

    public void setLatest_poster(String latest_poster) {
        this.latest_poster = latest_poster;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getReplies() {
        return replies;
    }

    public void setReplies(String replies) {
        this.replies = replies;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getThreadtitle() {
        return threadtitle;
    }

    public void setThreadtitle(String title) {
        this.threadtitle= title;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }
}
