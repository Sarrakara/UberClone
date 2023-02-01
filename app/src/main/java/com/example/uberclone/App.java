package com.example.uberclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("THVSkUvDAlfdvagAYvgPQDLkJZmhRix2BvnihPdN")
                // if defined
                .clientKey("bISUbeYsIbKfqTWXZCSAx7PIPSaxq3MjJV1wdyiz")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
