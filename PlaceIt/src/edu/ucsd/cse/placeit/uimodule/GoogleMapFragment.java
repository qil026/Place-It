package edu.ucsd.cse.placeit.uimodule;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import edu.ucsd.cse.placeit.model.OnFragmentEventListener;
import edu.ucsd.cse.placeit.utility.CONST;
import edu.ucsd.cse.placeit.utility.GoogleMapData;




public class GoogleMapFragment extends SupportMapFragment implements OnMapClickListener{

	// Local Private variables
	private View mapView;
	private GoogleMap mMap;
	OnFragmentEventListener mListener;
	

	// Call DatabaseHelper class's getAllLocationPlaceIts() to get all placeIts
	// then display them on map (Note, returned value is: ArrayList<LocationPlaceIt>)
	private void loadAllMarkers(){
		
	}
	

	// Called by Main Activity directly
	public void performDuty(int action, GoogleMapData data){
		switch (action){
		case CONST.DISPLAY_MARKER:
			// Create a new Marker and display it on map at location
			// specified by data
			
			
			
			// For debugging use only
			LatLng location = data.getLocation();
			String text = "Place a marker at Lat:" + String.valueOf(location.latitude);
			text += ", Lng:" + String.valueOf(location.longitude);
			Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
			break;
		}
	}
	
	
	
// ----------------------------------------------------------------------------
// Communication with Main Activity
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		mapView = super.onCreateView(inflater, container, savedInstanceState);
		
		// Set up map, retrieve handle to map
		setUpMapIfNeeded();
		// Load all markers
		loadAllMarkers();
		
		return mapView;
	}
	

	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }
	
	
	// When user click on map (not marker)
	// Tell Main Activity user has clicked the map at "position"
	@Override
	public void onMapClick(LatLng location){
		GoogleMapData data = new GoogleMapData(location, null);
		mListener.onFragmentEvent(CONST.MAP_CLICK, data);
	}
	
// --------------------------------------------------------------------------------------
// Private Helper Methods
	// Initialize google map if it doesn't exist.
	private void setUpMapIfNeeded(){
		if(mMap == null){
			mMap = this.getMap();
			
			// Initialize map properties
			mMap.setMyLocationEnabled(true);
			mMap.setOnMapClickListener(this);
		}
	}
}
