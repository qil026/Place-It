package edu.ucsd.cse.placeit.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;
import edu.ucsd.cse.placeit.R;
import edu.ucsd.cse.placeit.model.OnFragmentEventListener;
import edu.ucsd.cse.placeit.uimodule.CreateNewLocPlaceIt;
import edu.ucsd.cse.placeit.uimodule.GoogleMapFragment;
import edu.ucsd.cse.placeit.utility.CONST;
import edu.ucsd.cse.placeit.utility.GoogleMapData;


public class MainActivity extends FragmentActivity implements
			OnFragmentEventListener{
	
	private boolean listenToMapClick;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Initialize variables
		listenToMapClick = true;
	}


	// Observes the fragments
	@Override
	public void onFragmentEvent(int action, GoogleMapData data) {
		switch (action){
		case CONST.MAP_CLICK:					
			// Start the "CreateNewPlaceItFragment"
			if(listenToMapClick){ 
				Bundle bundle = new Bundle();
				bundle.putParcelable(CONST.LOCATION, data.getLocation());
				displayFragment(new CreateNewLocPlaceIt(), bundle);
			}
			break;
		case CONST.MARKER_CLICK:
			// User clicks on existing marker's info window

			break;
		case CONST.CREATE_SUBMIT:
			// User creates a new location PlaceIt, add a marker to map
			removeActiveFragment();
			GoogleMapFragment fragment = (GoogleMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
			fragment.performDuty(CONST.DISPLAY_MARKER, data);
			break;
		case CONST.CREATE_CANCEL:
			// User cancels operation, remove
			removeActiveFragment();
			Toast.makeText(this, "Canceled creating new place-it", Toast.LENGTH_SHORT).show();
			break;
		}
	}
	
	

	
// ----------------------------------------------------------------------------
// Private Methods: Display Fragments and Alerts
	
// Displays the pop up alert fragment
//    DialogFragment newFragment = PlaceItAlertFragment.newInstance(R.string.hello_world);
//    newFragment.show(getSupportFragmentManager(), "dialog");
	private void removeActiveFragment(){
		getSupportFragmentManager().popBackStack();
		listenToMapClick = true;
	}


	private void displayFragment(Fragment newFragment, Bundle bundle){
		listenToMapClick = false;
		// Create new fragment and transaction
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		// Add bundle to fragment
		if(bundle != null) newFragment.setArguments(bundle);
		
		// Replace whatever is in the fragment_container view with this fragment,
		// and add the transaction to the back stack
		transaction.replace(R.id.fragment_container, newFragment);
		transaction.addToBackStack(null);
		

		// Commit the transaction
		transaction.commit();
	}

}
