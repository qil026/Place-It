package com.ucsd.placeit.model.impl;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.ucsd.placeit.model.PlaceIt;

public class ReccuringPlaceIt extends PlaceIt {
	private int mExpiration;
	private int mFrequency;
	private LatLng mCoord;

	public ReccuringPlaceIt(int id, String title, String desc, int state,
			LatLng coord, Date dateCreated, Date datePosted, int frequency) {
		mId = id;
		mTitle = title;
		mCoord = coord;
		mDesc = desc;
		mState = state;
		creationDate = dateCreated;
		postDate = datePosted;
		mFrequency = frequency;
	}

	/**
	 * New Constructor
	 * @param source
	 */
	public ReccuringPlaceIt(Parcel source) {
		source = super.readFromParcel(source);
		mExpiration = source.readInt();
		mFrequency = source.readInt();
		mCoord = new LatLng(source.readDouble(), source.readDouble());
	}

	/**
	 * @return the expiration
	 */
	public int getExpiration() {
		return mExpiration;
	}

	/**
	 * @param expiration
	 *            the expiration to set
	 */
	public void setExpiration(int expiration) {
		this.mExpiration = expiration;
	}

	/**
	 * @return the frequency
	 */
	public int getFrequency() {
		return mFrequency;
	}

	/**
	 * @param frequency
	 *            the frequency to set
	 */
	public void setFrequency(int frequency) {
		mFrequency = frequency;
	}

	/**
	 * It will be required during un-marshaling data stored in a Parcel
	 * 
	 * @author prasanta
	 */
	public class MyCreator implements Parcelable.Creator<PlaceIt> {
		public PlaceIt createFromParcel(Parcel source) {
			return new ReccuringPlaceIt(source);
		}

		public PlaceIt[] newArray(int size) {
			return new PlaceIt[size];
		}
	}

	/**
	 * Writes to the parcel
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest = super.writeToParcelInit(dest);
		dest.writeInt(mExpiration);
		dest.writeInt(mFrequency);
		dest.writeDouble(mCoord.latitude);
		dest.writeDouble(mCoord.longitude);
	}

}
