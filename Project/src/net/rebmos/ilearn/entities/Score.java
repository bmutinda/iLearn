package net.rebmos.ilearn.entities;

import java.util.HashMap;

import net.rebmos.ilearn.utilities.Constants;
import net.rebmos.ilearn.utilities.ILearnApplication;
import android.text.TextUtils;

import com.parse.ParseObject;

public class Score {
	public DictionaryWord word;
	public boolean won;
	public String user="";
	
	public Score( DictionaryWord word, boolean won ) {
		this.word = word;
		this.won = won;
	}
	
	public Score( DictionaryWord word, boolean won, String user ) {
		this.word = word;
		this.won = won;
		this.user = user;
	}
	
	public void save( ){
		if( TextUtils.isEmpty( this.user )){
			this.user = ILearnApplication.database.getSettingInfo( Constants.USER_NAME_TAG );
		}
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put( Constants.USER_NAME_TAG, this.user );
		map.put( Constants.WORD_TAG, this.word.word );
		map.put( Constants.WON_TAG, this.won );
		map.put( Constants.KEY_STATUS, 0 );
		
		ILearnApplication.database.addScore(map);
		
		final ParseObject scoreObj = new ParseObject( "Score");
		scoreObj.put( Constants.USER_NAME_TAG , this.user );
		scoreObj.put( Constants.WORD_TAG , this.word.word );
		scoreObj.put( Constants.WON_TAG , this.won ? true: false );
		scoreObj.saveEventually();
	}

}
