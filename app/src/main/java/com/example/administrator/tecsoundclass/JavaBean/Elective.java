package com.example.administrator.tecsoundclass.JavaBean;

public class Elective {
    int elective_id;
    String course_id,join_user_id,join_time;

    public int getElective_id() {
        return elective_id;
    }

    public void setElective_id(int elective_id) {
        this.elective_id = elective_id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getJoin_user_id() {
        return join_user_id;
    }

    public void setJoin_user_id(String join_user_id) {
        this.join_user_id = join_user_id;
    }

    public String getJoin_time() {
        return join_time;
    }

    public void setJoin_time(String join_time) {
        this.join_time = join_time;
    }
}
