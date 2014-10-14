package com.codepath.apps.tastebuds.models;

import java.util.ArrayList;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("Dishes")
public class Dish extends ParseObject {

	// Fields
	//private String name;
	//private long googlePlacesId;

	public Dish() {
		super();
	}

	public Dish(String theClassName) {
		super(theClassName);
	}

	public Dish(String name, long googlePlacesId) {
		super();
		setName(name);
		setGooglePlacesId(googlePlacesId);
	}

	public static ParseQuery<Dish> getQuery() {
		return ParseQuery.getQuery(Dish.class);
	}

	public String getName() {
		return getString("name");
	}

	public void setName(String name) {
		put("name", name);
	}

	public long getGooglePlacesId() {
		return getLong("googlePlacesId");
	}
	public void setGooglePlacesId(long googlePlacesId) {
		put("googlePlacesId", googlePlacesId);
	}
}
