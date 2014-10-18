package com.codepath.apps.tastebuds.models;

import java.util.List;

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

	public DishReview(String googlePlacesId, int rating, String text) {
		super();
		setGooglePlacesId(googlePlacesId);
		setRating(rating);
		setText(text);
	}
	public static ParseQuery<DishReview> getQuery(String googlePlacesId,
			List<ParseObject> owners) {
	    ParseQuery<DishReview> query = ParseQuery.getQuery(
	    		DishReview.class)
	    		.orderByDescending("createdAt");
	    /*if (owners != null) {
	    	query.whereContainedIn("owner", owners);
	    }
	    if (googlePlacesId != null) {
	    	query.whereEqualTo("placesId", googlePlacesId);
	    }*/
	    return query;
	}

	public static ParseQuery<DishReview> getQuery() {
	    return ParseQuery.getQuery(DishReview.class);
	}

	public String getGooglePlacesId() {
		return getString("googlePlacesId");
	}

	public void setGooglePlacesId(String googlePlacesId) {
		put("googlePlacesId", googlePlacesId);
	}

	public static ParseQuery<DishReview> getQuery(ParseObject owner) {
	    return ParseQuery.getQuery(DishReview.class)
	    		.whereEqualTo("owner", owner)
	    		.orderByDescending("createdAt");
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

	public String getDishName() {
		return getString("dishName");
	}

	public void setDishName(String name) {
		put("dishName", name);
	}
}
