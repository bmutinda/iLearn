package net.rebmos.ilearn.utilities;

import java.util.HashMap;

import net.rebmos.ilearn.R;
import net.rebmos.ilearn.entities.Dictionary;
import net.rebmos.ilearn.entities.DictionaryWord;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class ILearnApplication extends Application {

	public static Context context;
	public static DatabaseManager database;
	public static Dictionary dictionary;
	public static SoundPlayer player;

	@Override
	public void onCreate() {
		ILearnApplication.context = getApplicationContext();
		ILearnApplication.dictionary = new Dictionary();
		ILearnApplication.database = new DatabaseManager(getApplicationContext());
		
		Resources resources = getResources();
		
		// Add in some words to our dictionary
		DictionaryWord zebra = new DictionaryWord("Zebra", "Zebra Description",
				resources.getDrawable(R.drawable.btn_zebra) , resources.getDrawable( R.drawable.zebra ));
		ILearnApplication.dictionary.addWord(zebra);

		DictionaryWord dog = new DictionaryWord("Dog",
				"Dog Description", resources.getDrawable(
						R.drawable.btn_dog), resources.getDrawable( R.drawable.dog ));
		ILearnApplication.dictionary.addWord( dog );

		DictionaryWord lion = new DictionaryWord("Lion", "Lion Description",
				resources.getDrawable(R.drawable.btn_lion) , resources.getDrawable( R.drawable.lion ));
		ILearnApplication.dictionary.addWord(lion);

		DictionaryWord cow = new DictionaryWord("Cow", "A Cow is a domestic animal. It is usually raised for meat, milk and sometimes" +
				" for leather, dung and pulling carts and plows.",
				resources.getDrawable(R.drawable.btn_cow), resources.getDrawable( R.drawable.cow ));
		ILearnApplication.dictionary.addWord(cow);

		DictionaryWord ram = new DictionaryWord("Ram", "Ram Description",
				resources.getDrawable(R.drawable.btn_ram), resources.getDrawable( R.drawable.ram ));
		ILearnApplication.dictionary.addWord( ram);
		
		// Enable parse- Backend API - parse.com
		try{
			Parse.enableLocalDatastore(getApplicationContext());
		       
			Parse.initialize( this, Constants.PARSE_APP_ID, Constants.PARSE_APP_KEY ); 
			 
	        // For security purposes 
	    	ParseUser.enableAutomaticUser(); // if this is enabled, we wont be able to logout
	    	ParseACL defaultACL = new ParseACL();
	    	// Optionally enable public read access while disabling public write access.
	    	defaultACL.setPublicReadAccess(true);
	    	defaultACL.setPublicWriteAccess(true);
	    	ParseACL.setDefaultACL(defaultACL, true);
	    	
		}catch( Exception e){
			AppLogger.logError( "Parse error::: "+e.getLocalizedMessage() );
		}
		
		
		// For the first time the application is intalled, the sound setting value is 
		// blank... we need to set this to true as a default value 
		try{
			if( TextUtils.isEmpty( ILearnApplication.database.getSettingInfo( Constants.SOUND_TAG ) )){

				HashMap<String, String> soundMap = new HashMap<String, String>();
				soundMap.put(Constants.KEY_SETTING_KEY_TAG, Constants.SOUND_TAG );
				soundMap.put(Constants.KEY_SETTING_VALUE_TAG, true+"" );
				ILearnApplication.database.addSetting( soundMap );
			}
		}catch( Exception e){
			AppLogger.logError( "Exception on default volume control"+e.getLocalizedMessage() );
		}
		
		// for blank username, create a guest name 
		try{
			if( TextUtils.isEmpty( ILearnApplication.database.getSettingInfo( Constants.USER_NAME_TAG  ) )){

				HashMap<String, String> userMap = new HashMap<String, String>();
				userMap.put(Constants.KEY_SETTING_KEY_TAG, Constants.USER_NAME_TAG );
				userMap.put(Constants.KEY_SETTING_VALUE_TAG, "GUEST"+Utils.generateRandomInt(1, 1000) );
				ILearnApplication.database.addSetting( userMap );
				
			}
		}catch( Exception e){
			AppLogger.logError( "Exception on default volume control"+e.getLocalizedMessage() );
		}

	}
}
