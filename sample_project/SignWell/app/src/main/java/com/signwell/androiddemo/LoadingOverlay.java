//
// SignWell
// Android Demo
// Andrey Butov, 2023
//


package com.signwell.androiddemo;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import com.signwell.androiddemo.R;


public final class LoadingOverlay
{
	private static ProgressDialog _progressDialog = null;


	public static void show ( String msg, Context context )
	{
		dismiss();

		msg = StringUtils.safe(msg);

		_progressDialog = ProgressDialog.show(context, null, msg, true, false);

		if ( msg.isEmpty() ) {
			// Show a spinner only, without a background or text.
			Window window = _progressDialog.getWindow();
			if ( window != null ) {
				window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			}
			_progressDialog.setContentView(R.layout.loading_overlay);
		}
	}


	public static void dismiss()
	{
		try
		{
			if ( _progressDialog != null )
			{
				_progressDialog.dismiss();
				_progressDialog = null;
			}
		}
		catch ( Exception ignored )
		{

		}
	}


	public static boolean isShowing()
	{
		return LoadingOverlay._progressDialog != null;
	}
}
