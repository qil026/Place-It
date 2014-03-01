package edu.ucsd.cse.placeit.uimodule;

import android.annotation.TargetApi;
import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;




public class GoogleMapFragment extends MapFragment implements OnMapClickListener{
	// Local Private variables
	private View mapView;
	private GoogleMap mMap;
	OnGoogleMapClickListener mListener;
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		mapView = super.onCreateView(inflater, container, savedInstanceState);
		
		// Set up map, retrieve handle to map
		setUpMapIfNeeded();
		
		return mapView;
	}
	
	
	public interface OnGoogleMapClickListener{
		public void onMapClick(Uri location);
	}
	
	
	@Override
	public void onMapClick(LatLng position){
		// Bring up the "Create new PlaceIt" fragment
	}
		
	
	

	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnGoogleMapClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }
	
// --------------------------------------------------------------------------------------
// Private Helper Methods
	private void setUpMapIfNeeded(){
		if(mMap == null){
			mMap = this.getMap();
			
			// Initialize map properties
			mMap.setMyLocationEnabled(true);
			mMap.setOnMapClickListener(this);
		}
	}
}
