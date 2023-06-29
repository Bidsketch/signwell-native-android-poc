//
// SignWell
// Android Demo
// Andrey Butov, 2023
//


package com.signwell.androiddemo;


import android.app.Application;


public final class App extends Application
{
	static App _instance;
	static final String TAG = "SignWell";


	public App()
	{
		_instance = this;
	}


	public void onCreate()
	{
        super.onCreate();
    }
}
