package com.example.plantique;

public class PostClass
{
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;


    String post_text;
    String post_date;

    public PostClass(String name,String post_text, String post_date) {
        this.name=name;
        this.post_text = post_text;
        this.post_date = post_date;
    }

    public String getPost_text() {
        return post_text;
    }

    public void setPost_text(String post_text) {
        this.post_text = post_text;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }
}
