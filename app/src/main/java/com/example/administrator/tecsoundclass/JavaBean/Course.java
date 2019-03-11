package com.example.administrator.tecsoundclass.JavaBean;

public class Course {
    private String course_id,teacher_user_id,belong_class,class_time ,class_request ,register_time;

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

    public String getBelong_class() {
        return belong_class;
    }

    public void setBelong_class(String belong_class) {
        this.belong_class = belong_class;
    }

    public String getClass_time() {
        return class_time;
    }

    public void setClass_time(String class_time) {
        this.class_time = class_time;
    }

    public String getClass_request() {
        return class_request;
    }

    public void setClass_request(String class_request) {
        this.class_request = class_request;
    }

    public String getRegister_time() {
        return register_time;
    }

    public void setRegister_time(String register_time) {
        this.register_time = register_time;
    }
}