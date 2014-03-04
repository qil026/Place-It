package com.ucsd.placeit.model;

import java.util.Iterator;

import com.ucsd.placeit.model.impl.NormalPlaceIt;


public interface PlaceItIterable {
	public Iterator<NormalPlaceIt> iterator();
	public Iterator<NormalPlaceIt> iterator(int state);
}
