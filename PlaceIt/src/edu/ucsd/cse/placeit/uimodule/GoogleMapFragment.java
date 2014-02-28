package edu.ucsd.cse.placeit.uimodule;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.ucsd.cse.placeit.R;

public class GoogleMapFragment extends MapFragment {
	private View mapView;
	private GoogleMap mMap;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		mapView = super.onCreateView(inflater, container, savedInstanceState);
		
		// Set up map, retrieve handle to map
		setUpMapIfNeeded();
		
		return mapView;
	}
	
	private void setUpMapIfNeeded(){
		if(mMap == null){
			mMap = this.getMap();
			
			// Initialize map properties
			mMap.setMyLocationEnabled(true);
		}
	}
}
