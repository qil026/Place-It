package com.ucsd.placeit.service.handler.impl;

import java.util.ArrayList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.ucsd.placeit.model.IPlaceIt;
import com.ucsd.placeit.model.PlaceItBank.PlaceItIterator;
import com.ucsd.placeit.service.handler.IPlaceItHandler;
import com.ucsd.placeit.util.Consts;

import edu.ucsd.cse.placeit.main.MainActivity;

public class PlaceItHandler implements IPlaceItHandler {
	private boolean mActivityEnabled;
	private Context context;
	
	public void onLocationChanged(Location location) {
		Log.d(Consts.TAG, "Location changed from service!");
		ArrayList<IPlaceIt> newList = checkInProximity(location, Consts.ACTIVE);

		if (newList.size() > 0) {
			// Convert to a list of IDs
			int size = newList.size();
			int[] idArray = new int[size];
			for (int i = 0; i < size; i++) {
				idArray[i] = newList.get(i).getId();
				if (!mActivityEnabled) {
					createNotification(newList.get(i));
				}
				Log.d(Consts.OTHER_TAG, "USER MOVED INTO RADIUS OF "
						+ newList.get(i).getTitle());
			}

			// Passing the intent
			Intent intent = new Intent(Consts.BROADCAST_ACTION);
			intent.putExtra(Consts.EXTRA_IN_PROX_ID, idArray);
			context.sendBroadcast(intent);

		}
	}

	/**
	 * Creates notification based on proximity. Will make sure the activity is
	 * not running, so only will display when it is in the background.
	 * 
	 * @param placeIt
	 */
	@SuppressWarnings("deprecation")
	private void createNotification(IPlaceIt placeIt) {
		Log.d(Consts.TAG_NOTIFY, "create new notification");
		NotificationManager notificationManager;
		String title = placeIt.getTitle();
		String subject = placeIt.getDesc();
		String body = String.format(Consts.MESSAGE_NOTIFICATION,
				placeIt.getTitle());

		notificationManager = (NotificationManager) = context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notify = new Notification(
				android.R.drawable.stat_notify_more, title,
				System.currentTimeMillis());
		notify.flags |= Notification.FLAG_AUTO_CANCEL;

		// PendingIntent pending = PendingIntent.getActivity(
		// getApplicationContext(), 0, new Intent(), 0);
		/* Creates an explicit intent for an Activity in your app */

		Intent resultIntent = new Intent(this, MainActivity.class);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		notify.setLatestEventInfo(context, subject, body,
				resultPendingIntent);
		notificationManager.notify(placeIt.getId(), notify);
	}

	/**
	 * Check the proximity of proximity of the location passed in
	 * 
	 * @param location
	 * @param state
	 * @return
	 */
	private ArrayList<IPlaceIt> checkInProximity(Location location, int state) {
		PlaceItIterator iterator = mPlaceItBank.iterator(Consts.ACTIVE);
		IPlaceIt placeIt;
		float[] results = new float[1];
		ArrayList<IPlaceIt> newList = new ArrayList<IPlaceIt>();
		int count = 0;
		// Check to see whether there are more placeIts to compare
		while (iterator.hasNext()) {
			placeIt = iterator.next();
			count++;
			Location.distanceBetween(location.getLatitude(),
					location.getLongitude(), placeIt.getCoord().latitude,
					placeIt.getCoord().longitude, results);

			// Log.d(Consts.OTHER_TAG,
			// "Distance from placeIT: " + Float.toString(results[0]));

			// Log.d(Consts.OTHER_TAG, "proximitysize" + mProximityList.size());

			boolean contains = false;
			// Cycle through each of the placeIts in that are already near
			if (results[0] < Consts.PLACEIT_RADIUS) {
				// Check through the proximity list to see if it is inside
				// already
				for (int i = 0; i < mProximityList.size(); i++) {
					// Expand proximity list
					if (placeIt.equals(mProximityList.get(i))) {
						Log.d(Consts.OTHER_TAG, "Already contained " + count
								+ "id = " + placeIt.getId());
						contains = true;
						break;
					}
				}
				if (!contains) {
					Log.d(Consts.OTHER_TAG,
							"Added " + count + "id = " + placeIt.getId()
									+ " , placeIt Name: " + placeIt.getTitle());
					mProximityList.add(placeIt);
					newList.add(placeIt);
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
		return newList;
	}

}