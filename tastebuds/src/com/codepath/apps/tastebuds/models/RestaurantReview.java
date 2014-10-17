package com.codepath.apps.tastebuds.models;

import java.util.List;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("RestaurantReviews")
public class RestaurantReview extends ParseObject implements Review {

	// Following fields:
	//private long googlePlacesId;
	//private ParseUser user;
	//private int rating;
	//private String text;

	public RestaurantReview() {
		super();
	}

	public RestaurantReview(long googlePlacesId, int rating, String text) {
		super();
		setGooglePlacesId(googlePlacesId);
		setRating(rating);
		setText(text);
	}

	public static ParseQuery<RestaurantReview> getQuery() {
	    return ParseQuery.getQuery(RestaurantReview.class);
	}

	public static ParseQuery<RestaurantReview> getQuery(long googlePlacesId) {
	    return ParseQuery.getQuery(RestaurantReview.class);
	}

	public static ParseQuery<RestaurantReview> getQuery(ParseUser owner) {
	    return ParseQuery.getQuery(RestaurantReview.class)
	    		.w;
	}

	public static ParseQuery<RestaurantReview> getQuery(long googlePlacesId, List<ParseUser> owner) {
	    return ParseQuery.getQuery(RestaurantReview.class);
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
		return getString("comment");
	}

	public void setText(String text) {
		put("comment", text);
	}
}
