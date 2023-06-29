//
// SignWell
// Android Demo
// Andrey Butov, 2023
//


package com.signwell.androiddemo;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Toast;


public final class Dialogs
{
	public static void showOKDialog(Activity activity, String msg) {
		showOKDialog(activity, msg, null);
	}

	public static void showOKDialog(Activity activity, String msg, Callback callback) {
		showDialogWithDismissalButton(activity, msg, "OK", callback);
	}

	public static void showDialogWithDismissalButton(Activity activity, String msg, String buttonLbl, Callback callback) {
		if ( activity == null || msg == null || msg.isEmpty() ) {
			return;
		}
		if ( buttonLbl == null || buttonLbl.isEmpty() ) {
			buttonLbl = "OK";
		}
		DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if ( callback != null ) {
					callback.onReady();
				}
			}
		};
		AlertDialog dlg = new AlertDialog.Builder(activity)
			.setMessage(msg)
			.setPositiveButton(buttonLbl, clickListener)
			.create();

		dlg.show();

		// must be called after .show()
		dlg.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.DKGRAY);
	}
}
