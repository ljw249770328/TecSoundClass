package com.example.administrator.tecsoundclass.JavaBean;

public class Follow {
    private String follower_user_id="",fan_user_id="",follow_time="";

    public String getFollower_user_id() {
        return follower_user_id;
    }

    public void setFollower_user_id(String follower_user_id) {
        this.follower_user_id = follower_user_id;
    }

    public String getFan_user_id() {
        return fan_user_id;
    }

    public void setFan_user_id(String fan_user_id) {
        this.fan_user_id = fan_user_id;
    }

    public String getFollow_time() {
        return follow_time;
    }

    public void setFollow_time(String follow_time) {
        this.follow_time = follow_time;
    }
}