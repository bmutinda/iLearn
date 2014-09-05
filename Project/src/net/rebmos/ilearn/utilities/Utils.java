package net.rebmos.ilearn.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import net.rebmos.ilearn.SplashActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Window;
 
public class Utils {
	Activity activity;
	static Context context;
	
	public Utils(){
		
	}
	public Utils(Context c ){
		context = c;
	}
	
	public Utils( Activity a){
		activity = a;
		context = a.getApplicationContext();
	}
	public void setActivity( Activity a ){
		activity = a;
		context = a.getApplicationContext();
	}
	
	
	public <T> ArrayList<T> generateRandomData( ArrayList<T> data, int size ){
		ArrayList<T> random = new ArrayList<T>( data );
		ArrayList<T> new_random = new ArrayList<T>(  );
		Collections.shuffle(random);
		
		for( int i=0; i< size; i++ ){
			if( i>size ){
				break;
			}
			new_random.add( random.get(i) );
		}
		return new_random;
	}
	
	public int generateRandomInt( int min, int max ){
		if( min<1 && max <1 ){
			return 0;
		}else{
			// Usually this can be a field rather than a method variable
		    Random rand = new Random();

		    // nextInt is normally exclusive of the top value,
		    // so add 1 to make it inclusive
		    int randomNum = rand.nextInt((max - min) + 1) + min;
		    return randomNum;
		}
	}
	
	
	public String getSoundsDirectory(){
		return "sounds/";
	}
	
	public AssetManager getAssetsDirectory( ){
		return activity.getAssets();
	}
	
    /**
	 * Check internet connection existence 
	 * 
	 * @return boolean
	 */
    public boolean connectionAvailable( ){
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();	
		if(activeNetworkInfo != null && activeNetworkInfo.isConnected()){
			return true;
		}
		else{
			return false;
		}
	}
    
    public static void setFullScreen( Activity activity ){
    	activity.requestWindowFeature( Window.FEATURE_NO_TITLE );
    }
    
    public void redirectToHome( ){
    	activity.getIntent().setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
    	redictToActivity( activity, SplashActivity.class );
    }
    
    public void redictToActivity( Activity activity, Class <?> cls ){
    	Intent intent = new Intent( activity, cls);
    	activity.startActivity( intent );
    }
    
}