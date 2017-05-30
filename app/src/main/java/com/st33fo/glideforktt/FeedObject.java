package com.st33fo.glideforktt;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Stefan on 7/1/2016.
 */
public class FeedObject {
    String title;
    String message;
    String person;
    String image;
    String timeposted;
    String quote;
    String link;

    FeedObject(){
        this.title = " ";
        this.message = " ";
        this.image = "";
        this.person = " ";
        this.timeposted = " ";
        this.quote = " ";
        this.link = " ";
    }

    FeedObject(String title, String message, String image, String person, String timeposted, String quote, String link) {
        this.title = title;
        this.message = message;
        this.image = image;
        this.person = person;
        this.timeposted = timeposted;
        this.quote = quote;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getTimeposted() {
        return timeposted;
    }

    public void setTimeposted(String timeposted) {
        this.timeposted = timeposted;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}

