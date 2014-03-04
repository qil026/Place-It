package com.ucsd.placeit.service.handler;

import android.location.Location;

import com.ucsd.placeit.model.PlaceItBank;

public interface IPlaceItHandler {
	//Called to handle location changed
	public void onLocationChanged(Location location, PlaceItBank placeItBank);
}
