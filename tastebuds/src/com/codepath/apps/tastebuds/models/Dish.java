package com.codepath.apps.tastebuds.models;

import java.util.ArrayList;
import java.util.List;


//@ParseClassName("Dish")
public class Dish { //extends ParseObject {

	// Fields
	private String name;
	private String googlePlacesId;
	private List<DishReview> reviews;

	//public Dish() {
	//	super();
//	}

	public Dish(String name, String googlePlacesId, List<DishReview> reviews) {
//		super();
		//setName(name);
		//setGooglePlacesId(googlePlacesId);
		this.name = name;
		this.googlePlacesId =  googlePlacesId;
		this.setReviews(reviews);
	}

	/*public static ParseQuery<Dish> getQuery() {
		return ParseQuery.getQuery(Dish.class);
	}

	public static ParseQuery<Dish> getQuery(ParseObject user) {
		return ParseQuery.getQuery(Dish.class)
				.whereEqualTo("owner", user);
	}

	public static ParseQuery<Dish> getQuery(String placesId) {
		return ParseQuery.getQuery(Dish.class)
				.whereEqualTo("placesId", placesId);
	}

	public static ParseQuery<Dish> getQuery(String placesId,
			List<ParseObject> friends) {
		return ParseQuery.getQuery(Dish.class);
//				/.whereEqualTo("placesId", placesId);
				//.whereContainedIn("owner", friends);
	}

	public ParseUser getUser() {
		return getParseUser("owner");
	}

	public void setUser(ParseUser user) {
		put("owner", user);
	}*/

	public Dish() {
		reviews = new ArrayList<DishReview>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGooglePlacesId() {
		return this.googlePlacesId;
	}
	public void setGooglePlacesId(String googlePlacesId) {
		this.googlePlacesId = googlePlacesId;
	}

	public List<DishReview> getReviews() {
		return reviews;
	}

	public void setReviews(List<DishReview> reviews) {
		this.reviews = reviews;
	}

	public void addReview(DishReview review) {
		this.reviews.add(review);
	}

}
