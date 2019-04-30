package com.example.administrator.tecsoundclass.JavaBean;

public class Message {

    private String send_user_id="",receive_user_id="",mes_content="",mes_time="";

    public String getSend_user_id() {
        return send_user_id;
    }

    public void setSend_user_id(String send_user_id) {
        this.send_user_id = send_user_id;
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