package com.ucsd.placeit.model;

import java.util.Date;

import com.google.android.gms.maps.model.LatLng;
import com.ucsd.placeit.model.impl.NormalPlaceIt;

import android.os.Parcelable;

public interface IPlaceIt extends Parcelable {

	/**
	 * @return the id
	 */
	public int getId();

	/**
	 * Sets the ID
	 * 
	 * @param id
	 *            the ID to set
	 */
	public void setId(int id);

	/**
	 * @return the coord
	 */
	public LatLng getCoord();

	/**
	 * @param coord
	 *            the coord to set
	 */
	public void setCoord(LatLng coord);

	/**
	 * @return the title
	 */
	public String getTitle();

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title);

	/**
	 * @return the desc
	 */
	public String getDesc();

	/**
	 * @param desc
	 *            the desc to set
	 */
	public void setDesc(String desc);

	/**
	 * @return the expiration
	 */
	public int getExpiration();

	/**
	 * @param expiration
	 *            the expiration to set
	 */
	public void setExpiration(int expiration);

	/**
	 * @return the startTime
	 */
	public int getStartTime();

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(int startTime);

	/**
	 * @return the date
	 */
	public Date getCreationDate();

	public Date getPostDate();

	public void setCreationDate(Date date);

	public void setPostDate(Date date);

	/**
	 * @return the state
	 */
	public int getState();

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(int state);

	/**
	 * @return the frequency
	 */
	public int getFrequency();

	/**
	 * @param frequency
	 *            the frequency to set
	 */
	public void setFrequency(int frequency);

	/**
	 * Check if two placeIts are equal based on their ID's
	 * 
	 * @param other
	 * @return
	 */
	public boolean equals(IPlaceIt other);
}
