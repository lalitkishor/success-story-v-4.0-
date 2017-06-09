package com.myapp.banna.sucessstory;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Lalit on 1/22/2017.
 */

public class UserPOSO {
    private String name;
    private String userName;
    private String photoUrl;
    private String id;
    private String story;
    private String storyId;
    private String storyLike;
    private int likeState;
    public UserPOSO(){
    }
    public void setStoryLike(String storyLike){
        this.storyLike = storyLike;
    }
    public void setLikeState(int likeState){
        this.likeState = likeState;
    }
    public int getLikeState(){
        return likeState;
    }
    public String getStoryLike(){
        return storyLike;
    }
    public String getName(){
        return name;
    }
    public void setStoryId(String storyId){
        this.storyId = storyId;
    }
    public String getStoryId(){
        return storyId;
    }
    public String getStory(){return story;}
    public void setStory(String story){this.story = story;}
    public void SetUserName(String userName){
        this.userName = userName;
    }
    public String getUserName(){
        return userName;
    }
    public String getId(){return id;}
    public void  setId(String id ){this.id=id;}
    public void setName(String name){
        this.name = name;
    }
    public String getPhotoUrl(){
        return photoUrl;
    }
    public void setPhotoUrl(String photoUrl){
        this.photoUrl = photoUrl;
    }
}

