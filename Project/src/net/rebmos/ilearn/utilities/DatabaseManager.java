package net.rebmos.ilearn.utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper implements Constants {
	Context context;

	// Database Version
	private static final int DATABASE_VERSION = 2;

	// Database Name
	private static final String DATABASE_NAME = "REBMOS_ILEARN_APP";

	// DATABASE tables
	private static final String TABLE_SCORES = SCORES_TAG;
	private static final String TABLE_USERS = USERS_TAG;
	private static final String TABLE_SETTINGS = SETTINGS_TAG;
	public DatabaseManager(Context c) {
		super(c, DATABASE_NAME, null, DATABASE_VERSION);
		context = c;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		// Users table 
		String create_users_table = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + USER_NAME_TAG + " TEXT," 
                + KEY_STATUS + " TEXT," 
                + KEY_DATE_CREATED + " TEXT"+
                ")";
		db.execSQL( create_users_table );	
		
		//Scores table 
	    String create_scores_table = "CREATE TABLE " + TABLE_SCORES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + USER_NAME_TAG + " TEXT," 
                + WORD_TAG + " TEXT," 
                + WON_TAG + " TEXT," 
                + KEY_STATUS + " TEXT," 
                + KEY_DATE_CREATED + " TEXT"+
                ")";
	    db.execSQL( create_scores_table );

	    //Settings table 
	    String create_settings_table = "CREATE TABLE " + TABLE_SETTINGS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_SETTING_KEY_TAG + " TEXT," 
                + KEY_SETTING_VALUE_TAG + " TEXT," 
                + KEY_STATUS + " TEXT," 
                + KEY_DATE_CREATED + " TEXT"+
                ")";
	    db.execSQL( create_settings_table );	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older tables if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS );
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES );
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS );
		onCreate(db);
	}
	
	/*** 
	 * ---------------------------------------------------------------------------------------------------------------
	 * User Manipulations
	 * --------------------------------------------------------------------------------------------------------------- 
	 */
	@SuppressLint("SimpleDateFormat")
	public int addUser( HashMap<String, Object> info ){
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put( USER_NAME_TAG, info.get(USER_NAME_TAG)+"") ;
		values.put( KEY_STATUS, info.get( KEY_STATUS ) +"");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strDate = sdf.format(new Date());
		values.put( KEY_DATE_CREATED, strDate );
		AppLogger.logError("Adding user with details...."+values.toString() );
		db.insert(TABLE_USERS, null, values);
		db.close(); 
	    
		db = this.getReadableDatabase();
		// get the last inserted id 
		String query = "SELECT "+KEY_ID+" FROM "+TABLE_USERS+" ORDER BY "+KEY_ID+" DESC LIMIT 1";
		Cursor cursor = db.rawQuery( query, null);	
		cursor.moveToFirst();
		int last_id = cursor.getInt( cursor.getColumnIndex(KEY_ID ));
		db.close();
		return last_id;
	}
	
	public ArrayList<HashMap<String, String>> getUsers( ){
		return fetchUsers( null );
	}
	public ArrayList<HashMap<String, String>> getUsers( String whereClause  ){
		return fetchUsers( whereClause );
	}
	
	private ArrayList<HashMap<String, String>> fetchUsers( String whereClause ){
		ArrayList<HashMap<String, String>> users = new ArrayList<HashMap<String, String>>();
		SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_USERS;
		if( whereClause != null ){
			selectQuery+=" WHERE "+whereClause;
		}
		 
		Cursor cursor = db.rawQuery(selectQuery, null);	
		if( cursor.getCount()>0 ){
			if (cursor.moveToFirst()) {
			    do {       
			    	HashMap<String, String> user = new HashMap<String, String>();
			    	user = this.getUserInfo( cursor.getString( cursor.getColumnIndex(KEY_ID) ) );
			    	users.add( user );
			    	
			    } while (cursor.moveToNext());
			}
		}
		db.close();
		return users;
	}
	
	public HashMap<String, String> getUserInfo( String userId ){
		HashMap<String, String> user_info = new HashMap<String, String>();
		SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_USERS+" WHERE "+KEY_ID+" = "+userId;
		
		Cursor cursor = db.rawQuery(selectQuery, null);	
		cursor.moveToFirst();
		
		user_info.put( KEY_ID , cursor.getString( cursor.getColumnIndex(KEY_ID) ) );			
		user_info.put( USER_NAME_TAG , cursor.getString( cursor.getColumnIndex(USER_NAME_TAG) ) );		
    	user_info.put( KEY_STATUS , cursor.getString(cursor.getColumnIndex(KEY_STATUS)) );
    	user_info.put( KEY_DATE_CREATED , cursor.getString(cursor.getColumnIndex(KEY_DATE_CREATED)) );

		db.close();
		return user_info;
	}
	
	public boolean deleteUser( HashMap<String, Object> user_info ){
		SQLiteDatabase db = this.getWritableDatabase();
		
		String name = user_info.get( USER_NAME_TAG )+"";
		
	    db.delete( TABLE_USERS, USER_NAME_TAG+" = ? ",  new String[] { name});
	    db.close();
	    
		return true;
	}
	
	
	public boolean setUserTrasferred( HashMap<String, Object> user_info ){
	    return changeUserStatus( user_info, 1 );
	}
	
	public boolean changeUserStatus( HashMap<String, Object> user_info, int new_status ){
		SQLiteDatabase db = this.getWritableDatabase();
		String names = user_info.get( USER_NAME_TAG )+"";
		
		ContentValues cv = new ContentValues();
		cv.put( KEY_STATUS, new_status);
	    db.update( TABLE_USERS, cv, USER_NAME_TAG+" = ? ", new String[] { names });
	    db.close();
	    return true;
	}
	
	public void deleteAllUsers( ){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete( TABLE_USERS, null, null);
	    db.close();
	}
	
	public int countUsersByStatus( int status ){
		SQLiteDatabase db = this.getReadableDatabase();		
		String selectQuery = "SELECT  * FROM " + TABLE_USERS+" WHERE "+KEY_STATUS+" = "+status;
		Cursor cursor = db.rawQuery(selectQuery, null);	
		int total = cursor.getCount();
		db.close();		
		return total;
	}
	
	public int countUsers( ){
		return getUsers().size();
	}
	
	/*** 
	 * ---------------------------------------------------------------------------------------------------------------
	 * Score Manipulations
	 * --------------------------------------------------------------------------------------------------------------- 
	 */
	@SuppressLint("SimpleDateFormat")
	public void addScore( HashMap<String, Object> info ){
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put( USER_NAME_TAG, info.get(USER_NAME_TAG)+"") ;
	    values.put( WORD_TAG, info.get(WORD_TAG)+"") ;
	    values.put( WON_TAG, info.get(WON_TAG)+"") ;
		values.put( KEY_STATUS, info.get( KEY_STATUS ) +"");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strDate = sdf.format(new Date());
		values.put( KEY_DATE_CREATED, strDate );
		AppLogger.logError("Adding score with details...."+values.toString() );
		db.insert(TABLE_SCORES, null, values);
		db.close(); 
	}
	
	
	
	
	/**
	 * Settings table manipulations 
	 */
	@SuppressLint("SimpleDateFormat")
	public void addSetting( HashMap<String, String> setting_info ){
		
		SQLiteDatabase db = this.getReadableDatabase();
		String query = "SELECT * FROM "+TABLE_SETTINGS;
		for( Entry<String, String> entry: setting_info.entrySet() ){
			query+=" where "+KEY_SETTING_KEY_TAG+" = '"+entry.getValue()+"'";
			break;
		}
		
		Cursor cursor = db.rawQuery( query, null);
		if( cursor.getCount()<=0 ){
			db.close();
			
			db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			for( Entry<String, String> entry: setting_info.entrySet() ){
				values.put(entry.getKey(), entry.getValue() );
			}
			values.put( KEY_STATUS, 1+"");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String strDate = sdf.format(new Date());
			values.put( KEY_DATE_CREATED, strDate );
			
			db.insert( TABLE_SETTINGS, null, values);
			db.close();
			
			AppLogger.logError( "Adding setting... .."+setting_info.toString() );
		}else{
			// update the values specified ....
			for( Entry<String, String> entry: setting_info.entrySet() ){
				updateSetting( entry.getKey(), entry.getValue() );
			}
		}
	}
	
	public boolean updateSetting( String key, String value ){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put( key, value);
		try{
			db.update( TABLE_SETTINGS, cv, null, null );
		}catch( Exception e){
			AppLogger.logError( "Error on update "+e.getMessage() );
		}
	    AppLogger.logError( "Updating .."+key+" to "+value );
	    db.close();
	    return true;
	}
	
	public ArrayList<HashMap<String, String>> getSettings( ){
		ArrayList<HashMap<String, String>> settings = new ArrayList<HashMap<String, String>>();
		SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_SETTINGS;
		 
		Cursor cursor = db.rawQuery(selectQuery, null);		
		if (cursor.moveToFirst()) {
		    do {       
		    	HashMap<String, String> setting = new HashMap<String, String>();
		    	setting.put( KEY_ID , Integer.parseInt(cursor.getString(0))+"" );
		    	setting.put( KEY_SETTING_KEY_TAG , cursor.getString( cursor.getColumnIndex( KEY_SETTING_KEY_TAG )));
		    	setting.put( KEY_SETTING_VALUE_TAG , cursor.getString( cursor.getColumnIndex( KEY_SETTING_VALUE_TAG )));
		    	setting.put( KEY_STATUS , cursor.getString( cursor.getColumnIndex( KEY_STATUS )));
		    	setting.put( KEY_DATE_CREATED , cursor.getString( cursor.getColumnIndex( KEY_DATE_CREATED )) );

		    	settings.add( setting );
		    	
		    } while (cursor.moveToNext());
		}
		db.close();
		return settings;
	}
		
	public String getSettingInfo( String key ){
		SQLiteDatabase db = this.getReadableDatabase();
		String val = "";
		try{
			String query = "SELECT * FROM "+TABLE_SETTINGS+" WHERE "+KEY_SETTING_KEY_TAG +" ='"+key+"'";
			Cursor cursor = db.rawQuery( query, null);
			if( cursor.getCount()>0 ){
				cursor.moveToFirst();
				val = cursor.getString( cursor.getColumnIndex( KEY_SETTING_VALUE_TAG ));
			}
		}catch( Exception e ){
			
		}finally{
			db.close();
		}
		return val;
	}
}
