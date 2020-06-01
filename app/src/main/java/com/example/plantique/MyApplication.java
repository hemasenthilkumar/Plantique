package com.example.plantique;

import android.app.Application;

public class MyApplication extends Application {
    public String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
