//
// SignWell
// Android Demo
// Andrey Butov, 2023
//


package com.signwell.androiddemo;


import androidx.appcompat.app.AppCompatActivity;


abstract class BaseScreen extends AppCompatActivity
{
	public void showProgressDialog(String msg)
	{
		runOnUiThread(() -> {
			LoadingOverlay.show(msg, this);
		});
	}

	public void dismissProgressDialog()
	{
		runOnUiThread(() -> {
			if ( LoadingOverlay.isShowing() ) {
				LoadingOverlay.dismiss();
			}
		});
	}
}
