package com.liborgsadmin.android;

import android.app.Application;

public class LiborsApplication extends Application {
    private String isbn;

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    public String getIsbn()
    {
        return isbn;
    }

    public void setIsbn(String isbn)
    {
        this.isbn = isbn;
    }
}
