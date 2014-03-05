package com.ucsd.placeit.service.handler.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.ucsd.placeit.service.handler.IAsyncResponse;
import com.ucsd.placeit.service.handler.ICategoryHandler;
import com.ucsd.placeit.util.Cfg;
import com.ucsd.placeit.util.Consts;

public class CategoryHandler implements ICategoryHandler, IAsyncResponse {
	private final String API_URL = "https://maps.googleapis.com/maps/api/place/search/json?"
			+ "location=%s" // COORDINATES_IN_LAT_LONG
			+ "&radius=%s" // DISTANCE_IN_METERS
			+ "&sensor=%s" // TRUE_OR_FALSE
			+ "&key=%s"; // API KEY

	private Context mContext;

	public CategoryHandler(Context context) {
		mContext = context;
	}

	/**
	 * Called whenever the location change and sees if any of the locations near
	 * the person match the category.
	 * 
	 * @param location
	 * @return list of categories that need to be checked
	 */
	public List<String> onLocationChanged(Location location) {
		// Building the longitude and latitude
		String urlString = buildURLString(location);
		DownloadAysncTask downloadTask = new DownloadAysncTask();
		downloadTask.outerClass = this;
		downloadTask.execute(urlString);
		
		// getUrlContents(urlString);
		return null;
	}

	/**
	 * Method which builds the GooglePlaces URL string using the location
	 * 
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
	 * AsyncTask which retrieves the JSON string based on the input URL.
	 * 
	 * @author Kevin
	 * 
	 */
	private class DownloadAysncTask extends AsyncTask<String, Void, String> {
		public IAsyncResponse outerClass;
		
		@Override
		protected String doInBackground(String... url) {
			String data = "";
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(url[0]);
			try {
				HttpResponse response = client.execute(request);
				HttpEntity entity = response.getEntity();
				data = EntityUtils.toString(entity);
				JSONObject myjson;
			} catch (ClientProtocolException e) {
				Log.d(Consts.TAG,
						"ClientProtocolException while trying to connect to GAE");
			} catch (IOException e) {

				Log.d(Consts.TAG, "IOException while trying to connect to GAE");
			}
			return data;
		}

		/**
		 * Passes the finished process to outer method
		 */
		protected void onPostExecute(String data) {
			outerClass.processFinish(data);
		}
	}

	@Override
	public void processFinish(String data) {
		try {
			JSONObject myjson = new JSONObject(data);
			JSONArray array = myjson.getJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				list.add(obj.get("name").toString());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	/**
	 * Private method to retrieve the PlaceIt contents from GooglePlaces API,
	 * while passing in the built string.
	 * 
	 * @param theUrl
	 * @return
	 */
	// private String getUrlContents(String theUrl) {
	// StringBuilder content = new StringBuilder();
	// try {
	// URL url = new URL(theUrl);
	// URLConnection urlConnection = url.openConnection();
	// BufferedReader bufferedReader = new BufferedReader(
	// new InputStreamReader(urlConnection.getInputStream()), 8);
	// String line;
	// while ((line = bufferedReader.readLine()) != null) {
	// content.append(line + "\n");
	// }
	// bufferedReader.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return content.toString();
	// }
}
