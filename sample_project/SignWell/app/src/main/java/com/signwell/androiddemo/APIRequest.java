//
// SignWell
// Android Demo
// Andrey Butov, 2023
//


package com.signwell.androiddemo;


import android.util.Log;
import org.json.JSONObject;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Objects;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


abstract class APIRequest
{
    interface Listener {
        void onComplete(APIResponse response);
    }


    abstract void execute();
    abstract void onRequestComplete(HashMap<String, Object> responseData, int statusCode, String error, boolean timedOut);


    APIResponse _response;
    Listener _listener;
    private final OkHttpClient _httpClient = new OkHttpClient();


	void post ( String endpoint, HashMap<String, Object> params, String apiKey )
	{
		// With okhttp, if we pass in MediaType.parse("application/json") as the media type,
		// the generated "content-type" will be "application/json; charset=utf-8".
		// The SignWell API doesn't like this charset suffix.
		// To work around this, we pass in null and set the "Content-Type" manually as a header.

		RequestBody requestBody = RequestBody.create(new JSONObject(params).toString(), null);

		Request request = new Request.Builder()
			.url(String.format("%s/%s", Common.SERVER_BASE_URL, endpoint))
			.post(requestBody)
			.addHeader("X-Api-Key", apiKey)
  			.addHeader("Content-Type", "application/json")
  			.addHeader("Accept", "application/json")
  			.build();

		sendRequest(request);
	}


    private void sendRequest(Request request)
	{
		try
		{
			Call call = _httpClient.newCall(request);

			call.enqueue(new Callback() {
				@Override
				public void onResponse(Call call, Response response)
                {
					if ( response.isSuccessful() && response.body() != null )
					{
						try
						{
							String responseStr = Objects.requireNonNull(response.body()).string();

							JSONObject json = new JSONObject(responseStr);
							HashMap<String, Object> responseObj = JSONUtils.jsonToHashMap(json);
							onRequestComplete(responseObj, response.code(), null, false);
						}
						catch ( Exception e )
						{
							Log.e(App.TAG, e.getLocalizedMessage());
							onRequestComplete(null, response.code(), response.message(), false);
						}
					}
					else
					{
						Log.e(App.TAG, "received a failed response: " + response.code() + " " + response.message());
						onRequestComplete(null, response.code(), response.message(), false);
					}
				}

				@Override
				public void onFailure(Call call, IOException e)
				{
					Log.e(App.TAG, "onFailure() " + e.getLocalizedMessage());
					boolean timedOut = (e instanceof SocketTimeoutException);
					onRequestComplete(null, 0, e.getLocalizedMessage(), timedOut);
				}
			});
		}
		catch ( Exception e )
		{
			Log.e(App.TAG, e.getLocalizedMessage());
		}
    }


    void execute ( Listener listener ) {
        _listener = listener;
        execute();
    }


    void callCompletionHandlerWithResponse() {
        if ( _listener != null ) {
            _listener.onComplete(_response);
        }
    }
}
