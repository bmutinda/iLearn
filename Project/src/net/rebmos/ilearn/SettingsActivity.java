package net.rebmos.ilearn;

import java.util.HashMap;

import net.rebmos.ilearn.entities.ui.ToastAlert;
import net.rebmos.ilearn.utilities.Constants;
import net.rebmos.ilearn.utilities.ILearnApplication;
import net.rebmos.ilearn.utilities.Utils;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ToggleButton;

public class SettingsActivity extends Activity {
	Activity activity;
	Utils utils;

	EditText txtUserName;
	String userName = "";
	ToggleButton soundToggle;
	Boolean soundEnabled;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		// Make full screen
		Utils.setFullScreen(activity);

		setContentView(R.layout.layout_settings);

		utils = new Utils(activity);

		txtUserName = (EditText) findViewById(R.id.txtUserName);
		soundToggle = (ToggleButton) findViewById(R.id.soundToggle);

		userName = ILearnApplication.database
				.getSettingInfo(Constants.USER_NAME_TAG);
		soundEnabled = Boolean.parseBoolean( ILearnApplication.database.getSettingInfo( Constants.SOUND_TAG ) );

		soundToggle.setChecked( soundEnabled );
		txtUserName.setText(userName);
	}

	public void onSave(View view) {
		userName = txtUserName.getText() + "";
		soundEnabled = soundToggle.isChecked( );

		HashMap<String, String> userMap = new HashMap<String, String>();
		userMap.put(Constants.KEY_SETTING_KEY_TAG, Constants.USER_NAME_TAG);
		userMap.put(Constants.KEY_SETTING_VALUE_TAG, userName);
		ILearnApplication.database.addSetting(userMap);
		
		HashMap<String, String> soundMap = new HashMap<String, String>();
		soundMap.put(Constants.KEY_SETTING_KEY_TAG, Constants.SOUND_TAG );
		soundMap.put(Constants.KEY_SETTING_VALUE_TAG, soundEnabled+"" );
		ILearnApplication.database.addSetting( soundMap );
		
		new ToastAlert(activity, "Settings saved successfully")
				.show(Gravity.CENTER);
		
	}

	public void onExit(View view) {
		finish();
		utils.redirectToHome();
	}

}