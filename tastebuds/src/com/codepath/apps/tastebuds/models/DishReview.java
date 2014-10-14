package com.codepath.apps.tastebuds.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


@ParseClassName("DishReviews")
public class DishReview extends ParseObject implements Review {

	// Following fields:
	//private long googlePlacesId;
	//private ParseUser user;
	//private int rating;
	//private String text;

	public DishReview() {
		super();
	}

	public DishReview(long googlePlacesId, int rating, String text) {
		super();
		setGooglePlacesId(googlePlacesId);
		setRating(rating);
		setText(text);
	}

	public static ParseQuery<DishReview> getQuery() {
	    return ParseQuery.getQuery(DishReview.class);
	}

	public long getGooglePlacesId() {
		return getLong("googlePlacesId");
	}

	public void setGooglePlacesId(long googlePlacesId) {
		put("googlePlacesId", googlePlacesId);
	}

	public ParseUser getUser() {
		return getParseUser("owner");
	}

	public void setUser(ParseUser user) {
		put("owner", user);
	}

	public long getCreatedTimestamp() {
		 return getLong("createdAt");
	}

	public int getRating() {
		return getInt("rating");
	}

	public void setRating(float f) {
		put("rating", f);
	}

	public String getText() {
		return getString("text");
	}

	public void setText(String text) {
		put("comment", text);
	}
}
