package com.example.administrator.tecsoundclass.JavaBean;

public class Message {
    private int mes_id;
    private String set_user_id,receive_user_id,mes_content,mes_time;

    public int getMes_id() {
        return mes_id;
    }

    public void setMes_id(int mes_id) {
        this.mes_id = mes_id;
    }

    public String getSet_user_id() {
        return set_user_id;
    }

    public void setSet_user_id(String set_user_id) {
        this.set_user_id = set_user_id;
    }

    public String getReceive_user_id() {
        return receive_user_id;
    }

    public void setReceive_user_id(String receive_user_id) {
        this.receive_user_id = receive_user_id;
    }

    public String getMes_content() {
        return mes_content;
    }

    public void setMes_content(String mes_content) {
        this.mes_content = mes_content;
    }

    public String getMes_time() {
        return mes_time;
    }

    public void setMes_time(String mes_time) {
        this.mes_time = mes_time;
    }
}