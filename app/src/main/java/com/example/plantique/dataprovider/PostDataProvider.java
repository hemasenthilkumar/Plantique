package com.example.plantique.dataprovider;

public class PostDataProvider
{
        private String post;
        private String id;
        private String Date;

    public PostDataProvider(String post, String id, String date) {
        this.post = post;
        this.id = id;
        this.Date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getPost()
    {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}
