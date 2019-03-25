package com.example.administrator.tecsoundclass.JavaBean;

import java.io.Serializable;

public class Course implements Serializable {
    private String course_id="",teacher_user_id="",course_class="",course_name="",course_time ="",course_request="" ,register_time="";

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getTeacher_user_id() {
        return teacher_user_id;
    }

    public void setTeacher_user_id(String teacher_user_id) {
        this.teacher_user_id = teacher_user_id;
    }

    public String getCourse_class() {
        return course_class;
    }

    public void setCourse_class(String course_class) {
        this.course_class = course_class;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_time() {
        return course_time;
    }

    public void setCourse_time(String course_time) {
        this.course_time = course_time;
    }

    public String getCourse_request() {
        return course_request;
    }

    public void setCourse_request(String course_request) {
        this.course_request = course_request;
    }

    public String getRegister_time() {
        return register_time;
    }

    public void setRegister_time(String register_time) {
        this.register_time = register_time;
    }
}