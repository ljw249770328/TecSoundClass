package com.example.administrator.tecsoundclass.JavaBean;

import org.litepal.crud.LitePalSupport;

public class Sign {
    private int sign_frequency=0,absence_frequency=0, late_frequency=0;
    private String sign_id="",sign_course="",sign_adress="",sign_time="",sign_state="",sign_voice_src="",sign_pacepic_src="",sign_date="";
    public String getSign_date() {
        return sign_date;
    }

    public void setSign_date(String sign_date) {
        this.sign_date = sign_date;
    }
    public int getSign_frequency() {
        return sign_frequency;
    }

    public void setSign_frequency(int sign_frequency) {
        this.sign_frequency = sign_frequency;
    }

    public int getAbsence_frequency() {
        return absence_frequency;
    }

    public void setAbsence_frequency(int absence_frequency) {
        this.absence_frequency = absence_frequency;
    }
    public int getLate_frequency() {
        return late_frequency;
    }

    public void setLate_frequency(int late_frequency) {
        this.late_frequency = late_frequency;
    }

    public String getSign_id() {
        return sign_id;
    }

    public void setSign_id(String sign_id) {
        this.sign_id = sign_id;
    }

    public String getSign_course() {
        return sign_course;
    }

    public void setSign_course(String sign_course) {
        this.sign_course = sign_course;
    }

    public String getSign_adress() {
        return sign_adress;
    }

    public void setSign_adress(String sign_adress) {
        this.sign_adress = sign_adress;
    }

    public String getSign_time() {
        return sign_time;
    }

    public void setSign_time(String sign_time) {
        this.sign_time = sign_time;
    }

    public String getSign_state() {
        return sign_state;
    }

    public void setSign_state(String sign_state) {
        this.sign_state = sign_state;
    }

    public String getSign_voice_src() {
        return sign_voice_src;
    }

    public void setSign_voice_src(String sign_voice_src) {
        this.sign_voice_src = sign_voice_src;
    }

    public String getSign_pacepic_src() {
        return sign_pacepic_src;
    }

    public void setSign_pacepic_src(String sign_pacepic_src) {
        this.sign_pacepic_src = sign_pacepic_src;
    }
}
