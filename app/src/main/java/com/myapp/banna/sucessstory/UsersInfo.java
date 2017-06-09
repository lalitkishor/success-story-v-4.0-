package com.myapp.banna.sucessstory;

/**
 * Created by Lalit on 5/26/2017.
 */

public class UsersInfo {
    private String userName;
    private String userPhotoUrl;
    private String id;
    public UsersInfo(){
    }
    public void setUsersName(String userName){
        this.userName=userName;
    }
    public String getUsersName(){
        return userName;
    }
    public void setUsersPhotoUrl(String userPhotoUrl){
        this.userPhotoUrl = userPhotoUrl;
    }
    public String getUsersPhotoUrl(){
        return userPhotoUrl;
    }
    public void setUsersId(String id){
        this.id=id;
    }
    public String getUsersId(){
        return  id;
    }
}
