package com.example.administrator.tecsoundclass.JavaBean;

import org.litepal.crud.LitePalSupport;

public class Point {
    private String point_id,release_course_id,point_time,point_voice_src;


    public String getPoint_id() {
        return point_id;
    }

    public void setPoint_id(String point_id) {
        this.point_id = point_id;
    }

    public String getRelease_course_id() {
        return release_course_id;
    }

    public void setRelease_course_id(String release_course_id) {
        this.release_course_id = release_course_id;
    }

    public String getPoint_time() {
        return point_time;
    }

    public void setPoint_time(String point_time) {
        this.point_time = point_time;
    }

    public String getPoint_voice_src() {
        return point_voice_src;
    }

    public void setPoint_voice_src(String point_voice_src) {
        this.point_voice_src = point_voice_src;
    }
}