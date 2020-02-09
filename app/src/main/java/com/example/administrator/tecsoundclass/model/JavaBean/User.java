package com.example.administrator.tecsoundclass.model.JavaBean;

import java.io.Serializable;

public class User implements Serializable {
    private  String user_id="",user_password="",user_sex="",user_institution="",user_age="",user_identity="",user_name="",user_pic_src="",update_time="";

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }

    public String getUser_institution() {
        return user_institution;
    }

    public void setUser_institution(String user_institution) {
        this.user_institution = user_institution;
    }

    public String getUser_age() {
        return user_age;
    }

    public void setUser_age(String user_age) {
        this.user_age = user_age;
    }

    public String getUser_identity() {
        return user_identity;
    }

    public void setUser_identity(String user_identity) {
        this.user_identity = user_identity;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_pic_src() {
        return user_pic_src;
    }

    public void setUser_pic_src(String user_pic_src) {
        this.user_pic_src = user_pic_src;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

}