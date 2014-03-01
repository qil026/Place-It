package edu.ucsd.cse.placeit.main;


import edu.ucsd.cse.placeit.R;
import edu.ucsd.cse.placeit.uimodule.GoogleMapFragment.OnGoogleMapClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class MainActivity extends FragmentActivity implements
			OnGoogleMapClickListener{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public void onMapClick(Uri location) {
		// TODO Auto-generated method stub
		System.out.println("YES");
	}


}
