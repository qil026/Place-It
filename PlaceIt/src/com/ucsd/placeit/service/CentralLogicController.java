package com.ucsd.placeit.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.ucsd.placeit.db.DatabaseHelper;
import com.ucsd.placeit.model.PlaceItBankListener;
import com.ucsd.placeit.model.PlaceItBankSubject;
import com.ucsd.placeit.model.impl.NormalPlaceIt;
import com.ucsd.placeit.util.Consts;

/**
 * Singleton pattern. CLC is a single object that is passed around to the
 * different instances that use it. When getInstance is called, you return a
 * single instance of the CLC, which is an object. If it doesnt exist, it would
 * create a new instance of it and store it itself.
 * 
 * @author Kevin
 * 
 */
public class CentralLogicController implements PlaceItBankSubject,
		IPlaceItManager {
	/** An array of placeIt bank listeners */
	private ArrayList<PlaceItBankListener> mListeners;
	private DatabaseHelper db;
	private Context mContext;

	/** Private variable that stores an instance of itself */
	private static CentralLogicController mCic;

	// Prevents others from instantiating
	private CentralLogicController() {
		mListeners = new ArrayList<PlaceItBankListener>();
	}

	public void setContext(Context context) {
		mContext = context;
	}

	/**
	 * Calls a new instance of the CLC passing in the context of the application
	 * 
	 * @param context
	 *            the current application context
	 * @return
	 */
	public static synchronized CentralLogicController getInstance(
			Context context) {
		if (mCic == null) {
			mCic = new CentralLogicController();
		}
		mCic.setContext(context);
		return mCic;
	}

	@Override
	public void registerListener(PlaceItBankListener pListener) {
		mListeners.add(pListener);
	}

	/**
	 * Will notify all the listeners about a change in the placeIt bank by
	 * giving them a new iterator.
	 */
	@Override
	public void notifyListeners(int placeItId, int updateState, int options) {
		for (PlaceItBankListener pListener : mListeners) {
			pListener.onPlaceItBankChange(placeItId, updateState, options);
		}
	}

	/**
	 * Removes a listener
	 */
	@Override
	public void removeListener(PlaceItBankListener pListener) {
		mListeners.remove(pListener);
	}

	/**
	 * Adding a place it to the database
	 */
	@Override
	public long addPlaceIt(NormalPlaceIt placeIt) {
		Log.d(Consts.TAG_NOTIFY, "Adding place it");

		// Call DatabaseStuff
		db = new DatabaseHelper(mContext);
		int placeItId = (int) db.createPlaceIt(placeIt);
		db.closeDB();

		notifyListeners(placeItId, Consts.UPDATE_ADD, 0);
		return placeItId;
	}

	@Override
	public void removePlaceIt(int placeItId) {
		db = new DatabaseHelper(mContext);
		db.deletePlaceIt(placeItId);
		db.closeDB();

		notifyListeners(placeItId, Consts.UPDATE_DELETE, 0);	
	}

	/**
	 * Get a single place it.
	 * 
	 * @param placeItId
	 *            the id of the place it
	 * @return place it object
	 */
	public NormalPlaceIt getPlaceIt(int placeItId) {
		NormalPlaceIt placeIt;
		db = new DatabaseHelper(mContext);
		placeIt = db.getPlaceIt(placeItId);
		db.closeDB();
		return placeIt;
	}

	/**
	 * Get all the placeIts from the database
	 * 
	 * @return list of placeits
	 */
	public List<NormalPlaceIt> getAllPlaceIts() {
		List<NormalPlaceIt> list;
		db = new DatabaseHelper(mContext);
		list = db.getAllPlaceIts();
		db.closeDB();
		return list;
	}

	/**
	 * Method called when user clicks complete button.
	 */
	@Override
	public void changePlaceItState(int placeItId, int placeItState) {
		// Default path: Change
		db = new DatabaseHelper(mContext);
		db.changePlaceItState(placeItId, placeItState);
		db.closeDB();

		notifyListeners(placeItId, Consts.UPDATE_STATE_UPDATE, placeItState);
	}

	/**
	 * If placeIt has recurring property then re create a reminder
	 * 
	 * @param placeItId
	 * @return id of the placeIt
	 */
	public int createRecurringReminder(int placeItId) {
		NormalPlaceIt pi = getPlaceIt(placeItId);
		int frequency = pi.getFrequency();
		// No recurring property
		if (frequency == 0)
			return -1;

		// Proceed only if recurring property is set
		String title = pi.getTitle();
		String desc = pi.getTitle();
		int state = Consts.SLEEP;
		LatLng position = pi.getCoord();
		Date creationDate = new Date();
		Date postDate = pi.getPostDate();
		long ctime = creationDate.getTime() * 1000;
		long ptime = postDate.getTime() * 1000;
		while (ptime <= ctime) {
			ptime += 86400;
		}
		postDate = new Date(ptime);
		NormalPlaceIt newPi = new NormalPlaceIt(title, desc, state, position, creationDate,
				postDate, frequency);
		int id = (int) addPlaceIt(newPi);
		return id;
	}
}