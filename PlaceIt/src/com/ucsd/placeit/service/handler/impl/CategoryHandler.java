package com.ucsd.placeit.service.handler.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import android.content.Context;
import android.location.Location;

import com.ucsd.placeit.service.handler.ICategoryHandler;
import com.ucsd.placeit.util.Cfg;

public class CategoryHandler implements ICategoryHandler {
	private final String API_URL = "https://maps.googleapis.com/maps/api/place/search/json?"
			+ "location=%s" // COORDINATES_IN_LAT_LONG
			+ "&radius=%s" // DISTANCE_IN_METERS
			+ "&sensor=%s" // TRUE_OR_FALSE
			+ "&key=%s"; // API KEY
	
	private Context mContext;
	
	
	public CategoryHandler(Context context) {
		mContext = context;
	}

	public List<String> onLocationChanged(Location location) {
		//Building the longitude and latitude
		String urlString = buildURLString(location);
		getUrlContents(urlString);
		return null;
	}

	/**
	 * Method which builds the GooglePlaces URL string using the location
	 * @param location
	 * @return properly formatted string
	 */
	private String buildURLString(Location location) {
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();

		StringBuilder locBuilder = new StringBuilder();
		locBuilder.append(Double.toString(latitude));
		locBuilder.append(",");
		locBuilder.append(Double.toString(longitude));
		String locationString = locBuilder.toString();

		String urlString = String.format(API_URL, locationString,
				Cfg.PLACEIT_RADIUS, Cfg.GOOGLE_PLACES_SENSOR,
				Cfg.GOOGLE_PlACES_KEY);
		return urlString;
	}
	
	/**
	 * Private method to retrieve the PlaceIt contents from GooglePlaces API,
	 * while passing in the built string.
	 * 
	 * @param theUrl
	 * @return
	 */
	private String getUrlContents(String theUrl) {
		StringBuilder content = new StringBuilder();
		try {
			URL url = new URL(theUrl);
			URLConnection urlConnection = url.openConnection();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(urlConnection.getInputStream()), 8);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				content.append(line + "\n");
			}
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content.toString();
	}
}
