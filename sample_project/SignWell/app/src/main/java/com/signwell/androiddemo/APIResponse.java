//
// SignWell
// Android Demo
// Andrey Butov, 2023
//


package com.signwell.androiddemo;


import java.util.HashMap;


abstract class APIResponse
{
	boolean _successful;
	APIRequest _originalRequest;
	int _errorCode;
	boolean _requestTimedOut;
	private HashMap<String, Object> _responseData;


	abstract void processSpecificResponse(HashMap<String, Object> responseObj, int statusCode, HashMap<String, Object> processingInfo);


	APIResponse ( APIRequest originalRequest )
	{
		_originalRequest = originalRequest;
		_originalRequest._response = this;
		_successful = false;
		_errorCode = 0;
	}


	void processResponse ( HashMap<String, Object> responseObj, int statusCode, String error, boolean timedOut)
	{
		processResponse(responseObj, statusCode, error, new HashMap<>(), timedOut);
	}


	void processResponse ( HashMap<String, Object> responseObj, int statusCode, String error, HashMap<String, Object> processingInfo, boolean timedOut )
	{
		_responseData = responseObj;

		if ( _responseData == null || _responseData.size() == 0 ) {
			_successful = false;
			_requestTimedOut = timedOut;
			return;
		}

		processSpecificResponse(_responseData, statusCode, processingInfo);
	}
}
