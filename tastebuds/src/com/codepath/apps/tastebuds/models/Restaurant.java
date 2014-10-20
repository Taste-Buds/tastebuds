package com.codepath.apps.tastebuds.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Restaurant implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String place_id; 		// place_id
	private String name; 			// name
	private String address;			// formatted_address
	private String phone;			// formatted_phone_number
	private double latitude;		// geometry:location:lat
	private double longitude;		// geometry:location:lng
	private double google_rating;	// rating
	private int price_level;		// price_level
	private String website;			// website
	private boolean open_now; 		// opening_hours:open_now
	private String web_map;			// URI for Restaurant Map on Google
	private int numOfReviews;		// Number of Friend Reviews
	private float friendRating;		// average Rating by Friends
	private String icon;
	private JSONArray googleReviews;
	private JSONArray photos;
	
	public int getNumOfReviews() {
		return numOfReviews;
	}

	public void setNumOfReviews(int numOfReviews) {
		this.numOfReviews = numOfReviews;
	}

	public float getFriendRating() {
		return friendRating;
	}

	public void setFriendRating(float friendRating) {
		this.friendRating = friendRating;
	}

	public String getWeb_map() {
		return web_map;
	}

	public String getAddress() {
		return address;
	}

	public String getPhone() {
		return phone;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getGoogle_rating() {
		return google_rating;
	}

	public int getPrice_level() {
		return price_level;
	}

	public String getWebsite() {
		return website;
	}

	public boolean isOpen_now() {
		return open_now;
	}

	public String getPlace_id() {
		return place_id;
	}

	public String getName() {
		return name;
	}	
	
	public static Restaurant fromJSON(JSONObject jsonObject) {
		Restaurant restaurant = new Restaurant();
		try {
			restaurant.place_id = jsonObject.getString("place_id");
			restaurant.name = jsonObject.getString("name");
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}		
		return restaurant;
	}
	
	public static Restaurant fromJSONDetail(JSONObject jsonObject) {
		Restaurant restaurant = new Restaurant();
		try {
			restaurant.setIcon(jsonObject.getString("icon"));
			restaurant.setGoogleReviews(jsonObject.getJSONArray("reviews"));
			restaurant.setPhotos(jsonObject.getJSONArray("photos"));
			restaurant.place_id = jsonObject.getString("place_id"); // place_id
			restaurant.name = jsonObject.getString("name");			// name
			restaurant.address = jsonObject.getString("formatted_address");		// formatted_address
			restaurant.phone = jsonObject.getString("formatted_phone_number");	// formatted_phone_number
			//restaurant.latitude = Double.parseDouble(jsonObject.getJSONObject("geometry").getJSONObject("location").getString("lat"));		// geometry:location:lat
			//restaurant.longitude = Doub le.parseDouble(jsonObject.getJSONObject("geometry").getJSONObject("location").getString("lng"));	// geometry:location:lng
			if ( jsonObject.has("rating")&& jsonObject.getString("rating") != null) {
				restaurant.google_rating = Double.parseDouble(jsonObject.getString("rating"));	// rating
			}
			if (jsonObject.has("price_level") && jsonObject.getString("price_level") != null) {
				restaurant.price_level  = Integer.parseInt(jsonObject.getString("price_level"));		// price_level
			}
			if (jsonObject.has("website") && jsonObject.getString("website") != null) {
				restaurant.website = jsonObject.getString("website");		// website
			}
			if (jsonObject.has("url") && jsonObject.getString("url") != null) {
				restaurant.web_map = jsonObject.getString("url");		// web_map
			}
			if (jsonObject.has("opening_hours") && jsonObject.getJSONObject("opening_hours") != null &&
					jsonObject.getJSONObject("opening_hours").getString("open_now") != null) {
				restaurant.open_now = Boolean.parseBoolean(jsonObject.getJSONObject("opening_hours").getString("open_now"));		// opening_hours:open_now
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return restaurant;
	}
	
	public static ArrayList<Restaurant> fromJSONArray(JSONArray jsonArray) {
		ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>(jsonArray.length());
		
		for (int i=0; i<jsonArray.length(); i++) {
			JSONObject restaurantJson = null;
			try {
				restaurantJson = jsonArray.getJSONObject(i);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			Restaurant restaurant = Restaurant.fromJSON(restaurantJson);
			if (restaurant != null ) {
				restaurants.add(restaurant);
			}
		}
		return restaurants;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public JSONArray getGoogleReviews() {
		return googleReviews;
	}

	public void setGoogleReviews(JSONArray googleReviews) {
		this.googleReviews = googleReviews;
	}

	public JSONArray getPhotos() {
		return photos;
	}

	public void setPhotos(JSONArray photos) {
		this.photos = photos;
	}

}
