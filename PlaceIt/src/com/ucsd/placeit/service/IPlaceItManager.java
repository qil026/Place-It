package com.ucsd.placeit.service;

import com.ucsd.placeit.model.impl.NormalPlaceIt;


public interface IPlaceItManager {
	/**
	 * Add a place it
	 * 
	 * @param placeIt
	 */
	public long addPlaceIt(NormalPlaceIt placeIt);
	
	/**
	 * Remove a place it
	 * 
	 * @param placeItId
	 */
	public void removePlaceIt(int placeItId);
	
	/**
	 * Change the placeIt state
	 * 
	 * @param placeItId
	 * @param placeItState
	 */
	public void changePlaceItState(int placeItId, int placeItState);
	
}
