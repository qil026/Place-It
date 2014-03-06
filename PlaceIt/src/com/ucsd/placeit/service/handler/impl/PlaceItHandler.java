package com.ucsd.placeit.service.handler.impl;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ucsd.placeit.db.DatabaseHelper;
import com.ucsd.placeit.model.PlaceIt;
import com.ucsd.placeit.model.impl.NormalPlaceIt;
import com.ucsd.placeit.service.handler.IPlaceItHandler;
import com.ucsd.placeit.util.Consts;

public class PlaceItHandler implements IPlaceItHandler {
	private Context mContext;
	private DatabaseHelper mDatabaseHelper;

	public PlaceItHandler(Context context) {
		mContext = context;
		mDatabaseHelper = new DatabaseHelper(context);
	}

	@Override
	public boolean updatePlaceItState(Intent intent) {
		int placeItId = intent.getExtras().getInt(Consts.EXTRA_UPDATE_ID);
		int updateState = intent.getExtras().getInt(Consts.EXTRA_UPDATE_STATE);
		int options = intent.getExtras().getInt(Consts.EXTRA_UPDATE_OPTIONS, 0);
		// TODO pass in PlaceIT as intent
		updatePlaceItBank(placeItId, updateState, options);
		return false;
	}

	/**
	 * Updating the place it bank
	 * 
	 * @param placeItId
	 * @param updateState
	 * @param option
	 */
	private void updatePlaceItBank(int placeItId, int updateState, int option) {
		switch (updateState) {
		case Consts.UPDATE_STATE_UPDATE:
			get(placeItId).setState(option);
			break;
		case Consts.UPDATE_ADD:
			// IPlaceIt placeIt = mCic.getPlaceIt(placeItId);
			mPlaceItBank.add(placeIt);
			break;
		case Consts.UPDATE_DELETE:
			try {
				mPlaceItBank.remove(placeItId);
			} catch (IndexOutOfBoundsException e) {
				Log.e(Consts.TAG, "CANNOT DELETE PlaceIt #" + placeItId
						+ " . Index out of bounds ");
			}
		}
	}

	/**
	 * Adding a place it to the database
	 */
	public long addPlaceIt(NormalPlaceIt placeIt) {
		Log.d(Consts.TAG_NOTIFY, "Adding place it");

		// Call DatabaseStuff
		int placeItId = (int) mDatabaseHelper.createPlaceIt(placeIt);
		// mDatabaseHelper.closeDB();

		// notifyListeners(placeItId, Consts.UPDATE_ADD, 0);
		return placeItId;
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
		mDatabaseHelper = new DatabaseHelper(mContext);
		placeIt = mDatabaseHelper.getPlaceIt(placeItId);
		mDatabaseHelper.closeDB();
		return placeIt;
	}

	/**
	 * Get all the placeIts from the database
	 * 
	 * @return list of placeits
	 */
	public List<NormalPlaceIt> getAllPlaceIts() {
		List<NormalPlaceIt> list;
		mDatabaseHelper = new DatabaseHelper(mContext);
		list = mDatabaseHelper.getAllPlaceIts();
		mDatabaseHelper.closeDB();
		return list;
	}

	/**
	 * Removing a placeIt from the database
	 * 
	 * @param placeItId
	 */
	public void removePlaceIt(int placeItId) {
		mDatabaseHelper.deletePlaceIt(placeItId);
		// mDatabaseHelper.closeDB();

		// notifyListeners(placeItId, Consts.UPDATE_DELETE, 0);
	}

	/**
	 * Retreive a list of the placeIts in the service
	 * 
	 * @param state
	 * @param category
	 * @return
	 */
	public List<PlaceIt> getAllPlaceIts(int state, String category) {
		return mDatabaseHelper.getAllPlaceIts(state, category);
	}

}
