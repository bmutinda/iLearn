package net.rebmos.ilearn.utilities;

import android.util.Log;

/**
 * App Loggin class .....
 * @author Mutinda Boniface
 *
 */

public class AppLogger {
	
	// make false on production 
	static boolean debug = true;
	
	
	public AppLogger() {
		
	}
	
	public static void logInfo( String message ){
		if( debug ) { Log.i( Constants.LOG_INFO_TAG, message ); }
	}
	
	public static void logError( String error ){
		if( debug ) { Log.e( Constants.LOG_ERROR_TAG, error ); }
	}
	public static void logDebug( String message ){
		if( debug ) { Log.e( Constants.LOG_DEBUG_TAG, message ); }
	}

}
