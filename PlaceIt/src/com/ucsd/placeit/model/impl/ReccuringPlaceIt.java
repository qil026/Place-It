package com.ucsd.placeit.model.impl;

import java.util.Date;
import com.google.android.gms.maps.model.LatLng;
import com.ucsd.placeit.model.IPlaceIt;

public class ReccuringPlaceIt implements IPlaceIt {
	private int mId;
	private LatLng mCoord;
	private String mTitle;
	private String mDesc;
	private int mExpiration;
	private int mStartTime;
	private Date creationDate;
	private Date postDate;
	private int mState;
	private int mFrequency;

	public ReccuringPlaceIt(String title, String desc, int state, LatLng coord, Date dateCreated, Date datePosted, int frequency) {
		mTitle = title;
		mCoord = coord;
		mDesc = desc;
		mState = state;
		creationDate = dateCreated;
		postDate = datePosted;
		mFrequency = frequency;
	}

	public ReccuringPlaceIt(int id, String title, String desc, int state, LatLng coord, Date dateCreated, Date datePosted, int frequency) {
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
	 * @return the id
	 */
	public int getId() {
		return mId;
	}

	/**
	 * Sets the ID
	 * 
	 * @param id
	 *            the ID to set
	 */
	public void setId(int id) {
		mId = id;
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
	 * @return the title
	 */
	public String getTitle() {
		return mTitle;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		mTitle = title;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return mDesc;
	}

	/**
	 * @param desc
	 *            the desc to set
	 */
	public void setDesc(String desc) {
		mDesc = desc;
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
	 * @return the startTime
	 */
	public int getStartTime() {
		return mStartTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(int startTime) {
		this.mStartTime = startTime;
	}

	/**
	 * @return the date
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	public Date getPostDate() {
		return postDate;
	}

	public void setCreationDate(Date date) {
		creationDate = date;
	}

	public void setPostDate(Date date) {
		postDate = date;
	}

	/**
	 * @return the state
	 */
	public int getState() {
		return mState;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(int state) {
		mState = state;
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
	 * Check if two placeIts are equal based on their ID's
	 * 
	 * @param other
	 * @return
	 */
	public boolean equals(ReccuringPlaceIt other) {
		return mId == other.getId();
	}

}
