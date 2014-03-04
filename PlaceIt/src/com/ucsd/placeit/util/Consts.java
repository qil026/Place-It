package com.ucsd.placeit.util;

public final class Consts {
	/** TAG to use for debugging */
	public static final String TAG = "PlaceIt";
	public static final String OTHER_TAG = "Location";
	public static final String TAG_NOTIFY = "NotificationTag";
	public static final int ACTIVE = 1;
	public static final int COMPLETE = 2;
	public static final int SLEEP = 3;

	// Milliseconds per second
	private static final int MILLISECONDS_PER_SECOND = 1000;
	// Update frequency in seconds
	private static final int UPDATE_INTERVAL_IN_SECONDS = 5;
	// Update frequency in milliseconds
	public static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND
			* UPDATE_INTERVAL_IN_SECONDS;
	// The fastest update frequency, in seconds
	private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
	// A fast frequency ceiling in milliseconds
	public static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND
			* FASTEST_INTERVAL_IN_SECONDS;

	
	
	//----- Intent stuff -----\\
	
	public static final String EXTRA_LONGITUDE = "com.ucsd.placeit.main.extra_longitude";
	public static final String EXTRA_LATITUDE = "com.ucsd.placeit.main.extra_latitude";
	public static final String EXTRA_PROVIDER = "com.ucsd.placeit.main.extra_provider";
	public static final String EXTRA_IN_PROX_ID = "com.ucsd.placeit.main.extra_in_prox_id";
	public static final String EXTRA_ACTIVITY_ONLINE = "com.ucsd.placeit.main.extra_activity_online";

	//Intent to call service
	public static final String EXTRA_UPDATE_ID = "com.ucsd.placeit.main.extra_update_id";
	public static final String EXTRA_UPDATE_STATE = "com.ucsd.placeit.main.extra_update_state";
	public static final String EXTRA_UPDATE_OPTIONS = "com.ucsd.placeit.main.extra_update_options";
	
	//---- UpdateStates -----\\
	
	public static final int UPDATE_STATE_UPDATE = 1;
	public static final int UPDATE_ADD = 2;
	public static final int UPDATE_DELETE = 3;

	public static final float PLACEIT_RADIUS = 800;
	
	//----- Create new place it Intent Code -----\\
	public static final int CREATE_NEW_MARKER = 0;
	public static final int RESULT_SUCCESS = 0;
	public static final int RESULT_ABORT = 1;
	public static final int DELAYED_INTENT_CODE = 100;
	public static final String RESULT_SET_NAME = "TaskName";
	public static final String RESULT_SET_DISPLAY = "DisplayOption";
	public static final String RESULT_SET_ID = "TaskID";
	public static final String RESULT_SET_LAT = "ReminderLatitude";
	public static final String RESULT_SET_LNG = "ReminderLongitude";
	public static final String RESULT_SET_DATE = "ReminderDate";
	public static final String INTENT_DELAYED_NAME = "com.ucsd.placeit";
		
		
	//----- MESSAGES -----\\
	public static final String MESSAGE_NOTIFICATION = "You are near %s. Please choose to accept or not.";
	
	/**
	 * The caller references the constants using <tt>Consts.EMPTY_STRING</tt>,
	 * and so on. Thus, the caller should be prevented from constructing objects
	 * of this class, by declaring this private constructor.
	 */
	private Consts() {
		throw new AssertionError();
	}
}