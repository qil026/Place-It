package com.ucsd.placeit.service.handler;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.ucsd.placeit.model.PlaceIt;
import com.ucsd.placeit.model.PlaceItBank;
import com.ucsd.placeit.model.PlaceItBank.PlaceItIterator;
import com.ucsd.placeit.model.PlaceItBankListener;
import com.ucsd.placeit.service.handler.impl.PlaceItHandler;
import com.ucsd.placeit.util.Consts;

//import android.location.LocationListener;

public class LocationService extends Service implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener,
		PlaceItBankListener {

	// flag for GPS status
	boolean isGPSEnabled = false;

	// flag for network status
	boolean isNetworkEnabled = false;
	boolean canGetLocation = false;

	Location location; // location
	double latitude; // latitude
	double longitude; // longitude

	// Declaring a Location Manager
	protected LocationManager mLocationManager;
	private LocationClient mLocationClient;
	private LocationRequest mLocationRequest;
	Intent intent;
	int counter = 0;
	private boolean mActivityEnabled;

	// ------- Private variables to be used in the logic ------\\
	/**
	 * Place it bank to store all the current placeIts
	 */
	private PlaceItBank mPlaceItBank;
	
	//----- Handler List -----\\
	private IPlaceItHandler mPlaceItHandler;
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		// Initialize handlers
		mPlaceItHandler = new PlaceItHandler(getApplicationContext());
		
		
		// Initializes new placeIt bank
		mPlaceItBank = new PlaceItBank(getApplicationContext());

		
		Log.d(Consts.TAG, "Location service create");

		int resp = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resp == ConnectionResult.SUCCESS) {
			mLocationClient = new LocationClient(this, this, this);
			mLocationClient.connect();

		} else {
			Toast.makeText(this, "Google Play Service Error " + resp,
					Toast.LENGTH_LONG).show();

		}
		intent = new Intent(Consts.BROADCAST_ACTION);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.d(Consts.TAG, "Location service start");

		// Setting the activity active state
		if (intent.hasExtra(Consts.EXTRA_ACTIVITY_ONLINE)) {
			mActivityEnabled = intent.getExtras().getBoolean(
					Consts.EXTRA_ACTIVITY_ONLINE);
			Log.d(Consts.TAG_NOTIFY, "Activity is on? " + mActivityEnabled);
		}

		// Checking if the local db has to be updated
		if (intent.hasExtra(Consts.EXTRA_UPDATE_ID)) {
			Log.d(Consts.TAG_NOTIFY, "Updating of Service");

			
			int placeItId = intent.getExtras().getInt(
					Consts.EXTRA_UPDATE_ID);
			int updateState = intent.getExtras().getInt(
					Consts.EXTRA_UPDATE_STATE);
			int options = intent.getExtras().getInt(
					Consts.EXTRA_UPDATE_OPTIONS, 0);
			mPlaceItBank.updatePlaceItBank(placeItId, updateState, options);
		}

		if (mLocationClient != null && mLocationClient.isConnected()) {
			mLocationRequest = LocationRequest.create();
			mLocationRequest
					.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
			// Set the update interval to 5 seconds
			mLocationRequest.setInterval(Consts.UPDATE_INTERVAL);
			// Set the fastest update interval to 1 second
			mLocationRequest.setFastestInterval(Consts.FASTEST_INTERVAL);
			mLocationClient.requestLocationUpdates(mLocationRequest, this);
		}

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		Log.d(Consts.TAG, "Location service connected");
		if (mLocationClient != null && mLocationClient.isConnected()) {
			mLocationRequest = LocationRequest.create();
			mLocationRequest
					.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

			// Set the update interval to 5 seconds
			mLocationRequest.setInterval(Consts.UPDATE_INTERVAL);
			// Set the fastest update interval to 1 second
			mLocationRequest.setFastestInterval(Consts.FASTEST_INTERVAL);
			mLocationClient.requestLocationUpdates(mLocationRequest, this);
		}
	}

	@Override
	public void onDestroy() {
		Log.d(Consts.TAG, "Location service destroyed");

		mLocationClient.removeLocationUpdates(this);
		mLocationClient.disconnect();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	/**
	 * Function to show settings alert dialog
	 * */
	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				getApplicationContext());

		// Setting Dialog Title
		alertDialog.setTitle("GPS is settings");

		// Setting Dialog Message
		alertDialog
				.setMessage("GPS is not enabled. Do you want to go to settings menu?");

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						getApplicationContext().startActivity(intent);
					}
				});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}

	/**
	 * Define the callback method that receives location updates
	 */
	@Override
	public void onLocationChanged(Location location) {
		Log.d(Consts.TAG, "Location changed from service!");
		mPlaceItHandler.onLocationChanged(location, mPlaceItBank);
	}

	@Override
	public void onPlaceItBankChange(int placeItId, int updateState, int options) {
		mPlaceItBank.updatePlaceItBank(placeItId, updateState, options);
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
	}

}