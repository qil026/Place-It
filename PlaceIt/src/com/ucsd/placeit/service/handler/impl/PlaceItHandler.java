package com.ucsd.placeit.service.handler.impl;

import java.util.ArrayList;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.ucsd.placeit.model.PlaceIt;
import com.ucsd.placeit.model.PlaceItBank;
import com.ucsd.placeit.model.PlaceItBank.PlaceItIterator;
import com.ucsd.placeit.service.handler.IPlaceItHandler;
import com.ucsd.placeit.util.Cfg;
import com.ucsd.placeit.util.Consts;

import edu.ucsd.cse.placeit.main.MainActivity;

public class PlaceItHandler implements IPlaceItHandler {
	private boolean mActivityEnabled;
	private Context mContext;
	/**
	 * The list of placeIt's that entered the proximity previously.
	 */
	private List<PlaceIt> mProximityList;
	private List<PlaceIt> mNewList;
	/**
	 * Constructor which creates new placeIt proximity list
	 */
	public PlaceItHandler(Context context) {
		mProximityList = new ArrayList<PlaceIt>();
		mContext = context;
	}

	/**
	 * Called from the location service facade.
	 * 
	 * @param location
	 */
	public void onLocationChanged(Location location, PlaceItBank placeItBank) {
		Log.d(Consts.TAG, "Location changed from service!");
		// Creating a iterator
		PlaceItIterator iterator = placeItBank.iterator(Consts.ACTIVE);

		// Calling helper method to return a list of proximity
		checkInProximity(location, iterator);
		
		// Create notifications based on the newList
		createNotifications();
	}

	
	/**
	 * Create the notifications based on the newLists
	 */
	private void createNotifications() {
		// Check that newList has something to return
		if (mNewList.size() > 0) {
			// Convert to a list of IDs
			int size = mNewList.size();
			int[] idArray = new int[size];
			for (int i = 0; i < size; i++) {
				idArray[i] = mNewList.get(i).getId();
				if (!mActivityEnabled) {
					createNotification(mNewList.get(i));
				}
				Log.d(Consts.OTHER_TAG, "USER MOVED INTO RADIUS OF "
						+ mNewList.get(i).getTitle());
			}

			// Passing the intent
			Intent intent = new Intent(Consts.BROADCAST_ACTION);
			intent.putExtra(Consts.EXTRA_IN_PROX_ID, idArray);
			mContext.sendBroadcast(intent);

		}
	}

	/**
	 * Check the proximity of proximity of the location passed in
	 * 
	 * @param location
	 * @param state
	 * @return
	 */
	private void checkInProximity(Location location,
			PlaceItIterator iterator) {
		PlaceIt placeIt;

		// Where the distance is stored in the Location.distanceBetween method
		float[] results = new float[1];

		// Create a newList of PlaceIts
		mNewList = new ArrayList<PlaceIt>();
		int count = 0;
		boolean contains;
		// Check to see whether there are more placeIts to compare
		while (iterator.hasNext()) {
			placeIt = iterator.next();
			count++;

			// Getting the distance between the location and each placeit of the
			// iterator
			Location.distanceBetween(location.getLatitude(),
					location.getLongitude(), placeIt.getCoord().latitude,
					placeIt.getCoord().longitude, results);

			contains = false;
			// Cycle through each of the placeIts in that are already near
			if (results[0] < Cfg.PLACEIT_RADIUS) {
				// Check through the proximity list to see if it is inside
				// already
				for (int i = 0; i < mProximityList.size(); i++) {
					// Expand proximity list
					if (placeIt.equals(mProximityList.get(i))) {
						Log.d(Consts.OTHER_TAG, "Already contained " + count
								+ "id = " + placeIt.getId());
						contains = true;
						break; // to the outer loop
					}
				}
				if (!contains) {
					Log.d(Consts.OTHER_TAG,
							"Added " + count + "id = " + placeIt.getId()
									+ " , placeIt Name: " + placeIt.getTitle());
					mProximityList.add(placeIt);
					mNewList.add(placeIt);
				}
			} else {
				// Shrink proximity list
				for (int i = 0; i < mProximityList.size(); i++) {
					if (placeIt.equals(mProximityList.get(i))) {
						Log.d(Consts.OTHER_TAG, "Removed " + count + "id = "
								+ placeIt.getId());
						mProximityList.remove(i);
					}
				}
			}
		}
	}

	/**
	 * Creates notification based on proximity. Will make sure the activity is
	 * not running, so only will display when it is in the background.
	 * 
	 * @param placeIt
	 */
	@SuppressWarnings("deprecation")
	private void createNotification(PlaceIt placeIt) {
		Log.d(Consts.TAG_NOTIFY, "create new notification");
		NotificationManager notificationManager;
		String title = placeIt.getTitle();
		String subject = placeIt.getDesc();
		String body = String.format(Consts.MESSAGE_NOTIFICATION,
				placeIt.getTitle());

		notificationManager = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notify = new Notification(
				android.R.drawable.stat_notify_more, title,
				System.currentTimeMillis());
		notify.flags |= Notification.FLAG_AUTO_CANCEL;

		// PendingIntent pending = PendingIntent.getActivity(
		// getApplicationContext(), 0, new Intent(), 0);
		/* Creates an explicit intent for an Activity in your app */

		Intent resultIntent = new Intent(mContext, MainActivity.class);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);

		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		notify.setLatestEventInfo(mContext, subject, body, resultPendingIntent);
		notificationManager.notify(placeIt.getId(), notify);
	}

}