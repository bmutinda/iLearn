package net.rebmos.ilearn.utilities;

public interface Constants {
	
	/**
	 * URLs 
	 */
	public static final String PROJECT_ROOT_URL = "http://rebmos.net/ilearn";
	public static final String PROJECT_API_URL = PROJECT_ROOT_URL+"api/";
	
	
	public static final String MAC_ADDRESS_TAG = "device_mac_address";
	public static final String KEY_STATUS = "status";
	public static final String KEY_ID = "id";
	public static final String KEY_DATE_CREATED = "date_created";
	
	
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
