//
// SignWell
// Android Demo
// Andrey Butov, 2023
//


package com.signwell.androiddemo;


import java.util.ArrayList;
import java.util.HashMap;


public class CreateDocumentRequest extends APIRequest
{
	private String _apiKey = "";
	private String _fileBase64 = "";
	private String _remoteFileURL = "";


	static CreateDocumentRequest withLocalFile(String apiKey, String fileBase64) {
		CreateDocumentRequest req = new CreateDocumentRequest();
		req._apiKey = apiKey;
		req._fileBase64 = fileBase64;
		return req;
	}


	static CreateDocumentRequest withRemoteFile(String apiKey, String fileURL) {
		CreateDocumentRequest req = new CreateDocumentRequest();
		req._apiKey = apiKey;
		req._remoteFileURL = fileURL;
		return req;
	}


	@Override
	void execute()
	{
		HashMap fileSpecObj = new HashMap();
		fileSpecObj.put("name", "file_1.pdf");

		if ( !_remoteFileURL.isEmpty() ) {
			fileSpecObj.put("file_url", _remoteFileURL);
		} else if ( !_fileBase64.isEmpty() ) {
			fileSpecObj.put("file_base64", _fileBase64);
		} else {
			throw new IllegalStateException("CreateDocumentRequest does not contain either a file_url or a file_base64");
		}

		ArrayList fileSpecArr = new ArrayList();
		fileSpecArr.add(fileSpecObj);

		HashMap<String, Object> recipientObj = new HashMap<>();
		recipientObj.put("id", "1");
		recipientObj.put("name", "John Doe");
		recipientObj.put("email", "user@host.com");

		ArrayList recipientsArr = new ArrayList();
		recipientsArr.add(recipientObj);

		HashMap<String, Object> signatureField = new HashMap<>();
		signatureField.put("x", 200);
		signatureField.put("y", 550);
		signatureField.put("page", 1);
		signatureField.put("recipient_id", "1");
		signatureField.put("required", true);
		signatureField.put("type", "signature");

		HashMap<String, Object> dateField = new HashMap<>();
		dateField.put("x", 110);
		dateField.put("y", 550);
		dateField.put("page", 1);
		dateField.put("recipient_id", "1");
		dateField.put("required", true);
		dateField.put("type", "date");
		dateField.put("date_format", "Month DD, YYYY");
		dateField.put("lock_sign_date", false);

		ArrayList fieldsArrayInner = new ArrayList();
		fieldsArrayInner.add(signatureField);
		fieldsArrayInner.add(dateField);

		ArrayList fieldsArrayOuter = new ArrayList();
		fieldsArrayOuter.add(fieldsArrayInner);

		HashMap<String, Object> params = new HashMap<>();
		params.put("name", "Sample Document");
		params.put("embedded_signing", true);
		params.put("draft", false);
		params.put("test_mode", true);
		params.put("reminders", false);
		params.put("files", fileSpecArr);
		params.put("recipients", recipientsArr);
		params.put("fields", fieldsArrayOuter);

		post("documents", params, _apiKey);
	}

	@Override
	void onRequestComplete(HashMap<String, Object> responseData, int statusCode, String error, boolean timedOut)
	{
		_response = new CreateDocumentResponse(this);
		_response.processResponse(responseData, statusCode, error, timedOut);
		callCompletionHandlerWithResponse();
	}
}
