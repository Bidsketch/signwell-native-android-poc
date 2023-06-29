//
// SignWell
// Android Demo
// Andrey Butov, 2023
//


package com.signwell.androiddemo;


import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;


public class CreateDocumentResponse extends APIResponse
{
	String _signingURL = "";


	CreateDocumentResponse(APIRequest originalRequest)
	{
		super(originalRequest);
	}


	@Override
	void processSpecificResponse(HashMap<String, Object> responseObj, int statusCode, HashMap<String, Object> processingInfo)
	{
		_successful = (statusCode == 200 || statusCode == 201);
		if ( _successful ) {
			ArrayList<HashMap> recipients = MapUtils.arrayOfHashMaps(responseObj, "recipients");
			if ( recipients.isEmpty() )  {
				_successful = false;
				return;
			}
			HashMap firstRecipient = recipients.get(0);
			_signingURL = MapUtils.stringValue(firstRecipient, "embedded_signing_url");
			if ( _signingURL.isEmpty() ) {
				_successful = false;
				return;
			}
			Log.d(App.TAG, "Signing URL: " + _signingURL);
		}
	}
}
