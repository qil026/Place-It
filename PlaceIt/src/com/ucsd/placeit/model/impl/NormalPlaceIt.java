package com.ucsd.placeit.model.impl;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.ucsd.placeit.model.PlaceIt;

public class NormalPlaceIt extends PlaceIt {
	/** Stores the coordinates */
	private LatLng mCoord;

	/**
	 * Generate placeIt from parcel
	 * 
	 * @param parcel
	 */
	public NormalPlaceIt(Parcel parcel) {
		parcel = super.readFromParcel(parcel);
		mCoord = new LatLng(parcel.readDouble(), parcel.readDouble());
	}

	public NormalPlaceIt(int id, String title, String desc, int state,
			LatLng coord, Date dateCreated, Date datePosted) {
		mId = id;
		mTitle = title;
		mCoord = coord;
		mDesc = desc;
		mState = state;
		mCreationDate = dateCreated;
		mPostDate = datePosted;
	}

	/**
	 * @return the coord
	 */
	public LatLng getCoord() {
		return mCoord;
	}

	/**
	 * @param coord
	 *            the coord to set
	 */
	public void setCoord(LatLng coord) {
		mCoord = coord;
	}

	/**
	 * Writes to the parcel
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest = super.writeToParcelInit(dest);
		dest.writeDouble(mCoord.latitude);
		dest.writeDouble(mCoord.longitude);
	}

	/**
	 * It will be required during un-marshaling data stored in a Parcel
	 * 
	 * @author prasanta
	 */
	public class MyCreator implements Parcelable.Creator<PlaceIt> {
		public PlaceIt createFromParcel(Parcel source) {
			return new NormalPlaceIt(source);
		}

		public PlaceIt[] newArray(int size) {
			return new PlaceIt[size];
		}
	}

}
