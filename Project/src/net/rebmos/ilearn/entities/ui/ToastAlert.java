package net.rebmos.ilearn.entities.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.widget.Toast;

public class ToastAlert {
	Activity activity;
	String message;
	Toast toast;
	
	@SuppressLint("ShowToast")
	public ToastAlert( Activity _activity, String _message ){
		activity = _activity;
		message = _message;
		toast = Toast.makeText(activity, message, Toast.LENGTH_LONG );
	}
	
	public void show(){
		doShow();
	}
	
	public void show( int gravity ){
		toast.setGravity(gravity, 0, 0); // Gravity.CENTER;
		doShow();
	}
	
	
	private void doShow( ){
		toast.show();
	}

}
