package com.ucsd.placeit.service.handler;

import android.content.Intent;

public interface IPlaceItHandler {

	/**
	 * Updates the placeIt state
	 * @param intent
	 * @return
	 */
	boolean updatePlaceItState(Intent intent);

}
