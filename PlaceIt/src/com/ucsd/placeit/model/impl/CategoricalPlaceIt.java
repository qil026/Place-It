package com.ucsd.placeit.model.impl;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import com.ucsd.placeit.model.PlaceIt;

public class CategoricalPlaceIt extends PlaceIt {

	private String[] mCategories;

	private CategoricalPlaceIt(Parcel source) {
		source = super.readFromParcel(source);
		mCategories = new String[3];
		source.readStringArray(mCategories);
	}

	/**
	 * It will be required during un-marshaling data stored in a Parcel
	 * 
	 * @author Kevin
	 */
	public class MyCreator implements Parcelable.Creator<PlaceIt> {
		public PlaceIt createFromParcel(Parcel source) {
			return new CategoricalPlaceIt(source);
		}

		public PlaceIt[] newArray(int size) {
			return new PlaceIt[size];
		}
	}

	public CategoricalPlaceIt(int id, String title, String desc, 
			int state, Date dateCreated, Date datePosted) {
		mId = id;
		mTitle = title;
		mDesc = desc;
		mState = state;
		creationDate = dateCreated;
		postDate = datePosted;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest = super.writeToParcelInit(dest);
		dest.writeStringArray(mCategories);
	}

	/**
	 * Set the categories of the PlaceIt
	 * @param categories
	 */
	public void setCategories(String[] categories) {
		//Set the categories based on the length
		if(categories.length <= 3) {
			mCategories = categories;
		} else {
			for(int i = 0; i < 3; i++) {
				mCategories[i] = categories[i];
			}
		}
	}
	

	/**
	 * Get the categories of the PlaceIt
	 * @return
	 */
	public String[] getCategories() {
		// TODO Auto-generated method stub
		return mCategories;
	}

}
