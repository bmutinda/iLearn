package net.rebmos.ilearn;

import net.rebmos.ilearn.utilities.Utils;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
/**
 * 
 * @author Mutinda Boniface
 *
 */
public class SplashActivity extends Activity {
	Activity activity;
	Utils utils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		Utils.setFullScreen(activity);

		setContentView(R.layout.layout_splash);
		utils = new Utils(activity);
	}

	public void onStart(View view) {
		utils.redictToActivity(activity, GameActivity.class);
	}

	public void onExit(View view) {
		finish();
	}

}
