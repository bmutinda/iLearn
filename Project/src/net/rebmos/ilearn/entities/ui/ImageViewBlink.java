package net.rebmos.ilearn.entities.ui;

import net.rebmos.ilearn.R;
import android.app.Activity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class ImageViewBlink {

	ImageView imageView;
	Activity activity;

	public ImageViewBlink(ImageView imageView, Activity activity ) {
		this.imageView = imageView;
		this.activity = activity;
	}
	
	public void blink( ){
		Animation myFadeInAnimation = AnimationUtils.loadAnimation( this.activity, R.anim.tween);
		this.imageView.startAnimation(myFadeInAnimation);
	}
}
