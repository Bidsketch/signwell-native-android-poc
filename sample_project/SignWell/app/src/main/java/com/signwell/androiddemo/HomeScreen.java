//
// SignWell
// Android Demo
// Andrey Butov, 2023
//



package com.signwell.androiddemo;



import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.widget.SwitchCompat;
import com.signwell.androiddemo.R;



public final class HomeScreen extends BaseScreen
{
	private static final int PICK_FILE_REQUEST_CODE = 1001;


	private EditText _apiInputField;
	private SwitchCompat _localFileSwitch;
	private EditText _documentURLField;
	private String _localFileBase64 = "";


	public void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);

		_apiInputField = findViewById(R.id.home_screen__api_key_field);
		_apiInputField.setText(Common.SIGNWELL_API_KEY);

		_documentURLField = findViewById(R.id.home_screen__document_url_field);

		_localFileSwitch = findViewById(R.id.home_screen__local_document_switch);
		_localFileSwitch.setChecked(true);
		_localFileSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
			onLocalFileSwitchToggled();
		});

		findViewById(R.id.home_screen__select_local_file_btn).setOnClickListener(v -> {
			onSelectLocalFileClicked();
		});

		findViewById(R.id.home_screen__continue_btn).setOnClickListener(v -> {
			onContinueBtnClicked();
		});

		onLocalFileSwitchToggled();
	}


	private void onSelectLocalFileClicked() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf"); // Or "*/*" to allow for any type of file.
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
	}


	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                _localFileBase64 = StringUtils.safe(FileUtils.getFileBase64Content(uri));
				((TextView)findViewById(R.id.home_screen__selected_file_lbl)).setText(StringUtils.safe(FileUtils.getFileName(uri)));
            }
        }
    }


	private void onLocalFileSwitchToggled() {
		findViewById(R.id.home_screen__local_file_section).setVisibility(_localFileSwitch.isChecked() ? View.VISIBLE : View.GONE);
		_documentURLField.setVisibility(_localFileSwitch.isChecked() ? View.GONE : View.VISIBLE);
	}


	private void onContinueBtnClicked() {
		String apiKey = StringUtils.safeTrimmed(_apiInputField.getText().toString());
		if ( apiKey.isEmpty() ) {
			Dialogs.showOKDialog(this, "Please enter your SignWell API Key");
			return;
		}
		if ( _localFileSwitch.isChecked() && _localFileBase64.isEmpty() ) {
			Dialogs.showOKDialog(this, "Please select a file.");
			return;
		} else if ( !_localFileSwitch.isChecked() && StringUtils.safeTrimmed(_documentURLField.getText().toString()).isEmpty() ) {
			Dialogs.showOKDialog(this, "Please enter the document URL.");
			return;
		}

		String remoteFileURL = StringUtils.safeTrimmed(_documentURLField.getText().toString());

		CreateDocumentRequest req = _localFileSwitch.isChecked() ?
			CreateDocumentRequest.withLocalFile(apiKey, _localFileBase64) :
			CreateDocumentRequest.withRemoteFile(apiKey, remoteFileURL);

		showProgressDialog("Loading ...");

		req.execute(response -> {
			runOnUiThread(() -> {
				dismissProgressDialog();
				if ( !response._successful ) {
					Dialogs.showOKDialog(HomeScreen.this, "Unable to create SignWell document from source file.");
				} else {
					Intent intent = new Intent(HomeScreen.this, WebViewScreen.class);
					intent.putExtra("url", ((CreateDocumentResponse)response)._signingURL);
					startActivity(intent);
				}
			});
		});
	}
}
