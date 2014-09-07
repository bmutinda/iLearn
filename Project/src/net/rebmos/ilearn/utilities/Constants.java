package net.rebmos.ilearn.utilities;

public interface Constants {
	
	/**
	 * Backend API settings -----parse.com
	 */
	public static final String PARSE_APP_ID = "vnEYccYWiIkBtzbZcwRtz8J97t5FmOnCtIvXhGIu";
	public static final String PARSE_APP_KEY = "eZlNuasyhFTFCfH13v6CEZkSkfjQWGjtGCdxiv7P";
	
	
	public static final String MAC_ADDRESS_TAG = "device_mac_address";
	public static final String KEY_STATUS = "status";
	public static final String KEY_ID = "id";
	public static final String KEY_DATE_CREATED = "date_created";
	
	/**
	 * Database tables and columns
	 */
	public static final String USERS_TAG = "users";
	public static final String USER_NAME_TAG = "fullnames";
	
	public static final String SCORES_TAG = "scores";
	public static final String WORD_TAG = "word";
	public static final String WON_TAG = "won";
	
	public static final String SETTINGS_TAG = "settings";
	
	public static final String KEY_SETTING_KEY_TAG = "setting_key";
	public static final String KEY_SETTING_VALUE_TAG = "setting_value";
	
	public static final String SOUND_TAG = "sound";
	
	
	/**
	 * Application images directory
	 */
	public static final String DATA_DIR = "iLearnApp";
	
	
	/**
	 * Timers 
	 */
	public static final int TIME_TO_ANSWER = 10000;
	public static final int TIMER_TICK = 1000;

	public static final int SOUND_DELAY_INTERVAL = 500;
	public static final int SOUND_REPEAT_INTERVAL = 500;

	/** 
	 * Logs 
	 */
	public static final String LOG_INFO_TAG = "log_info";
	public static final String LOG_ERROR_TAG = "log_error";
	public static final String LOG_DEBUG_TAG = "log_debug";
	
}
