package com.ucsd.placeit.util;

import com.ucsd.placeit.model.impl.NormalPlaceIt;

/**
 * Class to hold all of the configurable information
 * @author Kevin
 *
 */
public class Cfg {

	//----- Google Places API Information -----\\ 
	public static String GOOGLE_PlACES_KEY = "AIzaSyDFrT57O1tdIKCfkxQWOJ-D8mCckou2qKE";
	public static boolean GOOGLE_PLACES_SENSOR = true;
	//-----------------------------------------\\
	
	
	//----- PlaceIt Config -----\\
	public static final float PLACEIT_RADIUS = 800;
	//--------------------------\\
	
	PlaceIt shot = new NormalPlaceIt(parcel);
	
	
	private Cfg() {
		throw new AssertionError();
	}
}
