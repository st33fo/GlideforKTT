package com.st33fo.glideforktt;

/**
 * Created by Stefan on 7/14/2016.
 */
public class MessageBoardObject {
    String person;
    String time;
    String profilepicture;
    String quote;
    String message;
    String quotelink;
    String profileLink;
    String postLink;
    String topicLink;

    public MessageBoardObject(){
        person =" ";
        time=" ";
        profilepicture=" ";
        quote = "";
        message = " ";
        quotelink = " ";
        profileLink = "";
    }
    public MessageBoardObject(String person,String time, String profilepicture, String quote, String message, String quotelink, String profileLink){
        this.person = person;
        this.time = time;
        this.profilepicture = profilepicture;
        this.quote = quote;
        this.message = message;
        this.quotelink = quotelink;
        this.profileLink = profileLink;
    }

    public MessageBoardObject(String person,String time, String profilepicture, String quote, String message, String quotelink, String profileLink,String postLink,String topicLink){
        this.postLink = postLink;
        this.topicLink = topicLink;
        this.person = person;
        this.time = time;
        this.profilepicture = profilepicture;
        this.quote = quote;
        this.message = message;
        this.quotelink = quotelink;
        this.profileLink = profileLink;
    }
    public String getQuotelink() {
        return quotelink;
    }

    public String getProfileLink() {
        return profileLink;
    }

    public String getPostLink() {
        return postLink;
    }

    public void setPostLink(String postLink) {
        this.postLink = postLink;
    }

    public String getTopicLink() {
        return topicLink;
    }

    public void setTopicLink(String topicLink) {
        this.topicLink = topicLink;
    }

    public void setProfileLink(String profileLink) {
        this.profileLink = profileLink;
    }

    public void setQuotelink(String quotelink) {
        this.quotelink = quotelink;
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

    public String getProfilepicture() {
        return profilepicture;
    }

    public void setProfilepicture(String profilepicture) {
        this.profilepicture = profilepicture;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
