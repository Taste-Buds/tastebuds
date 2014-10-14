package com.codepath.apps.tastebuds.models;

import java.util.ArrayList;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("Users")
public class User extends ParseObject {

	private long userId;
	private ArrayList<User> friends;
	
	 public static ParseQuery<User> getQuery() {
		    return ParseQuery.getQuery(User.class);
	 }
}
