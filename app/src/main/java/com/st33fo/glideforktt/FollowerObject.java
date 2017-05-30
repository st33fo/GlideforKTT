package com.st33fo.glideforktt;

/**
 * Created by Stefan on 8/8/2016.
 */
public class FollowerObject {
    private String profileName;
    private String profileURL;

    public FollowerObject(){
        profileName = "";
        profileURL = "";
    }
    public FollowerObject(String profileName, String profileURL){
        this.profileName = profileName;
        this.profileURL = profileURL;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }
}
