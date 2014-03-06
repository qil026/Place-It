package com.ucsd.placeit.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.ucsd.placeit.model.PlaceIt;
import com.ucsd.placeit.model.impl.CategoricalPlaceIt;
import com.ucsd.placeit.model.impl.NormalPlaceIt;
import com.ucsd.placeit.model.impl.ReccuringPlaceIt;
import com.ucsd.placeit.util.Consts;

@SuppressLint("DefaultLocale")
public class DatabaseHelper extends SQLiteOpenHelper {

	// Logcat tag
	private static final String LOG = "DatabaseHelper";

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "placeItsManager";

	// Table Names
	private static final String TABLE_PLACEIT = "placeIts";
	// private static final String TABLE_TAG = "states";
	// private static final String TABLE_PLACEIT_TAG = "placeIt_states";

	// Common column names
	private static final String KEY_ID = "id";

	// PlaceIts Table - column names
	private static final String KEY_TITLE = "title";
	private static final String KEY_DESC = "desc";
	private static final String KEY_STATE = "state";
	private static final String KEY_LONGITUDE = "longitude";
	private static final String KEY_LATITUDE = "latitude";
	private static final String KEY_DATE_CREATED = "date_created";
	private static final String KEY_DATE_TO_POST = "date_to_post";
	private static final String KEY_DATE_FREQUENCY_START = "date_freq_start";
	private static final String KEY_FREQUENCY = "frequency";
	private static final String KEY_TYPE = "type";
	private static final String KEY_CAT_1 = "cat_1";
	private static final String KEY_CAT_2 = "cat_2";
	private static final String KEY_CAT_3 = "cat_3";

	private SimpleDateFormat mDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", Locale.getDefault());

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// create required tables
		String query = String.format(Queries.CREATE_TABLE_PLACEIT,
				TABLE_PLACEIT, KEY_ID, KEY_TITLE, KEY_DESC, KEY_STATE,
				KEY_LONGITUDE, KEY_LATITUDE, KEY_DATE_CREATED,
				KEY_DATE_TO_POST, KEY_DATE_FREQUENCY_START, KEY_FREQUENCY,
				KEY_TYPE, KEY_CAT_1, KEY_CAT_2, KEY_CAT_3);
		Log.d(LOG, query);

		db.execSQL(query);

		// String query2 =
		// "create table tbl1(id int primary key, dt datetime default current_timestamp)";
		// db.execSQL(query2);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// On upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACEIT);
		// Create new tables
		onCreate(db);
	}

	// ------METHODS------\\

	/*
	 * Creating a placeIt
	 */
	public long createPlaceIt(PlaceIt placeIt) {
		Log.d(Consts.TAG, "databasehelper create a new placeit");
		SQLiteDatabase db = this.getWritableDatabase();
		Log.d(Consts.TAG, "Received a writable Database");
		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, placeIt.getTitle());
		values.put(KEY_DESC, placeIt.getDesc());
		values.put(KEY_STATE, placeIt.getState());
		values.put(KEY_DATE_CREATED,
				mDateFormat.format(placeIt.getCreationDate()));
		values.put(KEY_DATE_TO_POST, mDateFormat.format(placeIt.getPostDate()));
		values.put(KEY_DATE_FREQUENCY_START,
				mDateFormat.format(placeIt.getPostDate()));

		if (placeIt instanceof NormalPlaceIt) {
			LatLng coord = ((NormalPlaceIt) placeIt).getCoord();
			values.put(KEY_LONGITUDE, coord.longitude);
			values.put(KEY_LATITUDE, coord.latitude);
			values.put(KEY_CAT_1, "");
			values.put(KEY_CAT_2, "");
			values.put(KEY_CAT_3, "");
			values.put(KEY_FREQUENCY, "");

		} else if (placeIt instanceof CategoricalPlaceIt) {
			values.put(KEY_LONGITUDE, "0");
			values.put(KEY_LATITUDE, "0");
			try {
				values.put(KEY_CAT_1,
						((CategoricalPlaceIt) placeIt).getCategories()[0]);
				values.put(KEY_CAT_2,
						((CategoricalPlaceIt) placeIt).getCategories()[1]);
				values.put(KEY_CAT_3,
						((CategoricalPlaceIt) placeIt).getCategories()[2]);
			} catch (ArrayIndexOutOfBoundsException e) {
			}

		} else if (placeIt instanceof ReccuringPlaceIt) {
			LatLng coord = ((ReccuringPlaceIt) placeIt).getCoord();
			values.put(KEY_LONGITUDE, coord.longitude);
			values.put(KEY_LATITUDE, coord.latitude);
			values.put(KEY_FREQUENCY,
					((ReccuringPlaceIt) placeIt).getFrequency());
		}

		// insert row
		Log.d(Consts.TAG, "Before inserting into database");
		long placeIt_id = db.insert(TABLE_PLACEIT, null, values);
		Log.d(Consts.TAG, "intsert" + placeIt_id);

		return placeIt_id;
	}

	/*
	 * Get single placeIt based on the ID
	 * 
	 * @return PlaceIT
	 */
	@SuppressLint("DefaultLocale")
	public PlaceIt getPlaceIt(int placeItId) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = String.format(Queries.SELECT_PLACEIT,
				TABLE_PLACEIT, KEY_ID, placeItId);

		Log.e(Consts.TAG, selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);

		// Only selecting the first
		if (c != null)
			c.moveToFirst();

		return addPlaceItFromDb(c);
	}

	/**
	 * getting all placeIts
	 * */
	public List<PlaceIt> getAllPlaceIts() {
		SQLiteDatabase db = this.getReadableDatabase();

		List<PlaceIt> placeIts = new ArrayList<PlaceIt>();
		String selectQuery = String.format(Queries.SELECT_ALL_PLACEIT,
				TABLE_PLACEIT);

		Log.e(LOG, selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			Log.e(LOG, "TABLE has stuff");
			do {
				placeIts.add(addPlaceItFromDb(c));
			} while (c.moveToNext());
		} else {
			Log.e(LOG, "TABLE IS EMPTY");
		}
		return placeIts;
	}

	/**
	 * Getting all placeIts based on the state and category
	 * 
	 * @param state
	 *            the state to pass in. 0 for any.
	 * @param category
	 *            the category to check for. null for any.
	 * */
	@SuppressLint("NewApi")
	public List<PlaceIt> getAllPlaceIts(int state, String category) {
		SQLiteDatabase db = this.getReadableDatabase();
		String stateCheck = KEY_STATE;
		// Category check to make sure it is not wildcard
		if (category != null && !category.isEmpty()) {
			category = "%";

		}
		// Do a state check
		if (state == 0) {
			state = 0;
			stateCheck = "1";
		}
		

		List<PlaceIt> placeIts = new ArrayList<PlaceIt>();
		String selectQuery = String.format(Queries.SELECT_STATE_CAT_PLACEIT,
				TABLE_PLACEIT, stateCheck, state, KEY_CAT_1, category,
				KEY_CAT_2, category, KEY_CAT_3, category);

		Log.e(LOG, selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			Log.e(LOG, "TABLE has stuff");
			do {
				placeIts.add(addPlaceItFromDb(c));
			} while (c.moveToNext());
		} else {
			Log.e(LOG, "TABLE IS EMPTY");
		}
		return placeIts;
	}

	/**
	 * getting all placeIts under single tag
	 * */
	// public List<PlaceIt> getAllPlaceItsByTag(String tag_name) {
	// List<PlaceIt> placeIts = new ArrayList<PlaceIt>();
	//
	// String selectQuery = "SELECT  * FROM " + TABLE_PLACEIT + " td, "
	// + TABLE_TAG + " tg, " + TABLE_PLACEIT_TAG + " tt WHERE tg."
	// + KEY_TAG_NAME + " = '" + tag_name + "'" + " AND tg." + KEY_ID
	// + " = " + "tt." + KEY_TAG_ID + " AND td." + KEY_ID + " = "
	// + "tt." + KEY_TODO_ID;
	//
	// Log.e(LOG, selectQuery);
	//
	// SQLiteDatabase db = this.getReadableDatabase();
	// Cursor c = db.rawQuery(selectQuery, null);
	//
	// // looping through all rows and adding to list
	// if (c.moveToFirst()) {
	// do {
	// PlaceIt td = new PlaceIt();
	// td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
	// td.setNote((c.getString(c.getColumnIndex(KEY_TODO))));
	// td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
	//
	// // adding to placeIt list
	// placeIts.add(td);
	// } while (c.moveToNext());
	// }
	//
	// return placeIts;
	// }

	/*
	 * getting placeIt count
	 */
	public int getPlaceItCount() {
		String countQuery = String.format(Queries.SELECT_ALL_PLACEIT,
				TABLE_PLACEIT);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}

	/*
	 * Updating a placeIt
	 */
	@SuppressLint("DefaultLocale")
	public int updatePlaceIt(PlaceIt placeIt) {
		SQLiteDatabase db = this.getWritableDatabase();

		// ContentValues values = new ContentValues();

		int placeItId = placeIt.getId();
		String title = placeIt.getTitle();
		String desc = placeIt.getDesc();
		int state = placeIt.getState();

		String created = mDateFormat.format(placeIt.getCreationDate());
		String posted = mDateFormat.format(placeIt.getPostDate());
		String freqStart = mDateFormat.format(placeIt.getPostDate());
		String[] category = new String[Consts.NUM_CAT];
		double longitude;
		double latitude;
		int frequency;

		if (placeIt instanceof NormalPlaceIt) {
			LatLng coord = ((NormalPlaceIt) placeIt).getCoord();
			longitude = coord.longitude;
			latitude = coord.latitude;

		} else if (placeIt instanceof CategoricalPlaceIt) {
			longitude = 0;
			latitude = 0;
			try {
				String[] pCategory = ((CategoricalPlaceIt) placeIt)
						.getCategories();
				for (int i = 0; i < 3; i++) {
					category[i] = pCategory[i];
				}
			} catch (ArrayIndexOutOfBoundsException e) {
			}

		} else if (placeIt instanceof ReccuringPlaceIt) {
			LatLng coord = ((ReccuringPlaceIt) placeIt).getCoord();
			longitude = coord.longitude;
			latitude = coord.latitude;
			frequency = ((ReccuringPlaceIt) placeIt).getFrequency();

		}

		String updateQuery = String.format(Queries.UPDATE_PLACEIT,
				TABLE_PLACEIT, KEY_TITLE, title, KEY_DESC, desc, KEY_STATE,
				state, KEY_LONGITUDE, longitude, KEY_LATITUDE, latitude,
				KEY_DATE_CREATED, created, KEY_DATE_TO_POST, posted,
				KEY_DATE_FREQUENCY_START, freqStart, KEY_FREQUENCY, frequency,
				KEY_CAT_1, category[0], KEY_CAT_2, category[1], KEY_CAT_3,
				category[2], KEY_ID, placeItId);

		Log.e(LOG, updateQuery);
		db.rawQuery(updateQuery, null);
		return 0;
	}
	
	
	

	/**
	 * Deleting a placeIt
	 */
	public void deletePlaceIt(int placeItId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PLACEIT, KEY_ID + " = ?",
				new String[] { String.valueOf(placeItId) });
	}

	public void changePlaceItState(int placeItId, int state) {
		SQLiteDatabase db = this.getWritableDatabase();

		// Just change the state of the query
		String updateQuery = String.format(Queries.CHANGE_STATE_PLACEIT,
				TABLE_PLACEIT, KEY_STATE, state, KEY_ID, placeItId);

		Log.e(LOG, updateQuery);
		db.rawQuery(updateQuery, null);
	}

	/**
	 * Private helper method retrieve single place it from a cursor
	 * 
	 * @param c
	 *            the current cursor
	 * @return
	 */
	private PlaceIt addPlaceItFromDb(Cursor c) {
		PlaceIt placeIt;
		try {
			Log.d(LOG, "retreiving single placeit");
			int id = c.getInt(c.getColumnIndex(KEY_ID));
			String title = c.getString(c.getColumnIndex(KEY_TITLE));
			String desc = c.getString(c.getColumnIndex(KEY_DESC));
			int state = c.getInt(c.getColumnIndex(KEY_STATE));
			double longitude = c.getDouble(c.getColumnIndex(KEY_LONGITUDE));
			double latitude = c.getDouble(c.getColumnIndex(KEY_LATITUDE));
			LatLng coord = new LatLng(latitude, longitude);
			Date dateCreated = mDateFormat.parse(c.getString(c
					.getColumnIndex(KEY_DATE_CREATED)));
			Date datePosted = mDateFormat.parse(c.getString(c
					.getColumnIndex(KEY_DATE_TO_POST)));
			// Date dateFreqStart = mDateFormat.parse(c.getString(c
			// .getColumnIndex(KEY_DATE_FREQUENCY_START)));

			int frequency = c.getInt(c.getColumnIndex(KEY_FREQUENCY));
			int type = c.getInt(c.getColumnIndex(KEY_TYPE));
			String[] categories = new String[3];
			categories[0] = c.getString(c.getColumnIndex(KEY_CAT_1));
			categories[1] = c.getString(c.getColumnIndex(KEY_CAT_2));
			categories[2] = c.getString(c.getColumnIndex(KEY_CAT_3));

			switch (type) {
			case Consts.TYPE_NORMAL:
				placeIt = new NormalPlaceIt(id, title, desc, state, coord,
						dateCreated, datePosted);
				break;
			case Consts.TYPE_CATEGORICAL:
				placeIt = new CategoricalPlaceIt(id, title, desc, state,
						dateCreated, datePosted, categories);
				break;
			case Consts.TYPE_RECURRING:
				placeIt = new ReccuringPlaceIt(id, title, desc, state, coord,
						dateCreated, datePosted, frequency);
				break;
			default:
				Log.d("T", "cant find a placeit");
				break;
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return placeIt;
	}

	/**
	 * Closing the database
	 */
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}

}
