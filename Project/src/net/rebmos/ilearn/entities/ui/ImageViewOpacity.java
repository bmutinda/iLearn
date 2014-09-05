package net.rebmos.ilearn.entities.ui;

import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class ImageViewOpacity {
	
	ImageView imageView;
	float alphaFrom = 0.5f;
	float alphaTo = 0.5f;
	
	public ImageViewOpacity( ImageView imageView ) {
		this.imageView = imageView;
	}
	
	public ImageViewOpacity( ImageView imageView, float alphaFrom,  float alphaTo ) {
		this.imageView = imageView;
		this.alphaFrom = alphaFrom;
		this.alphaTo = alphaTo;
	}
	
	public void apply( Boolean persist ){
		AlphaAnimation alpha = new AlphaAnimation( this.alphaFrom,  this.alphaTo ); 
		alpha.setDuration(0); // Make animation instant
		alpha.setFillAfter( persist ); // True- to persist after the animation ends
		// And then on your imageview
		this.imageView.startAnimation(alpha);
	}

}
