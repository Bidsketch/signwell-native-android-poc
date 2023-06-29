//
// SignWell
// Android Demo
// Andrey Butov, 2023
//


package com.signwell.androiddemo;


import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.signwell.androiddemo.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public final class WebViewScreen extends BaseScreen
{
	private WebView _webView;

	public void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_screen);

		String signingURL = getIntent().getStringExtra("url");
		if ( signingURL == null || signingURL.isEmpty() ) {
			throw new IllegalStateException("Unknown Signing URL on the WebViewScreen");
		}

		Log.d(App.TAG, "Starting WebViewScreen with URL " + signingURL);

		_webView = findViewById(R.id.webview_screen__webview);
		_webView.setFocusable(true);
		_webView.setFocusableInTouchMode(true);
		_webView.addJavascriptInterface(this, "AndroidApp");

		WebSettings webSettings = _webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(true);

		String html = readTextFileFromAssets("page.html").replace("$SIGNING_URL$", signingURL);

		_webView.loadData(html, "text/html", "UTF-8");
	}


	@JavascriptInterface
	public void onSigningComplete() {
		Dialogs.showOKDialog(this, "Your document has been signed.", () -> {
			finish();
		});
	}


	private String readTextFileFromAssets(String path) {
		BufferedReader reader = null;
		StringBuilder builder = new StringBuilder("");
		try {
			reader = new BufferedReader(new InputStreamReader(getAssets().open(path)));
			String line = "";
			while ( (line = reader.readLine()) != null ) {
				builder.append(line);
			}
		} catch ( IOException e ) {
			Log.e(App.TAG, e.getLocalizedMessage());
		} finally {
			if ( reader != null ) {
				try {
					reader.close();
				} catch ( IOException e ) {
					Log.e(App.TAG, e.getLocalizedMessage());
				}
			}
		}

		return builder.toString();
	}
}
