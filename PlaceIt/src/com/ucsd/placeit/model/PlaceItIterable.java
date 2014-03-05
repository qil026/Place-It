package com.ucsd.placeit.model;

import java.util.Iterator;

import com.ucsd.placeit.model.impl.NormalPlaceIt;


public interface PlaceItIterable {
	public Iterator<PlaceIt> iterator();
	public Iterator<PlaceIt> iterator(int state);
}
