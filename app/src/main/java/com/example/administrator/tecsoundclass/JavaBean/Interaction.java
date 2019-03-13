package com.example.administrator.tecsoundclass.JavaBean;

import org.litepal.crud.LitePalSupport;

public class Interaction {
    private int problem_id=0,answer_grace=0;
    private String propose_course_id="",answer_user_id="",answer_content_src="",answer_time="";

    public int getProblem_id() {
        return problem_id;
    }

    public void setProblem_id(int problem_id) {
        this.problem_id = problem_id;
    }

    public int getAnswer_grace() {
        return answer_grace;
    }

    public void setAnswer_grace(int answer_grace) {
        this.answer_grace = answer_grace;
    }

    public String getPropose_course_id() {
        return propose_course_id;
    }

    public void setPropose_course_id(String propose_course_id) {
        this.propose_course_id = propose_course_id;
    }

    public String getAnswer_user_id() {
        return answer_user_id;
    }

    public void setAnswer_user_id(String answer_user_id) {
        this.answer_user_id = answer_user_id;
    }

    public String getAnswer_content_src() {
        return answer_content_src;
    }

    public void setAnswer_content_src(String answer_content_src) {
        this.answer_content_src = answer_content_src;
    }

    public String getAnswer_time() {
        return answer_time;
    }

    public void setAnswer_time(String answer_time) {
        this.answer_time = answer_time;
    }
}