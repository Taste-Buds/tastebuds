package com.codepath.apps.tastebuds.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.util.Log;

public class Restaurant implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String place_id; 		// place_id
	private String name; 			// name
	private String address;			// formatted_address
	private String phone;			// formatted_phone_number
	private Location location;		// Restaurant Location
	private double latitude;		// geometry:location:lat
	private double longitude;		// geometry:location:lng
	private float currentDistancetoUser; // Calculated Distance from Current User Location to Restaurant Location
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
	
	public Location getLocation() {
		return location;
	}
	
	public float getCurrentDistancetoUser() {
		return currentDistancetoUser;
	}

	public void setCurrentDistancetoUser(float currentDistancetoUser) {
		this.currentDistancetoUser = currentDistancetoUser;
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
			
			restaurant.place_id = jsonObject.getString("place_id");
			
			try {restaurant.name = jsonObject.getString("name");} 
				catch (Exception e) { restaurant.name = "N/A"; }  // name
			
			try {restaurant.address = jsonObject.getString("formatted_address");}
				catch (Exception e) { restaurant.address = "N/A"; }// formatted_address
			
			try {restaurant.phone = jsonObject.getString("formatted_phone_number");}
				catch (Exception e) { restaurant.phone = "N/A"; }// formatted_phone_number
			
			try {
				JSONObject location = jsonObject.getJSONObject("geometry").getJSONObject("location");
				restaurant.latitude = location.getDouble("lat");
				//restaurant.latitude = Double.parseDouble(jsonObject.getJSONObject("geometry").getJSONObject("location").getString("lat"));
				//restaurant.latitude = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
				Log.d("Debug", "Lat: " + Double.toString(restaurant.getLatitude()));
				}
				catch (Exception e) { 
					Log.d("Debug", "Can't get Location A");
					restaurant.latitude = 0; }
			
			try {
				//restaurant.longitude = Double.parseDouble(jsonObject.getJSONObject("geometry").getJSONObject("location").getString("lng"));
				restaurant.longitude = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
				}
			catch (Exception e) { restaurant.longitude = 0; }		
			
			if ((restaurant.getLatitude() != 0) && (restaurant.getLongitude() != 0)) {
				Location location = new Location("");
			    location.setLatitude(restaurant.getLatitude());
			    location.setLongitude(restaurant.getLongitude()); 
			    restaurant.location = location;
			} else {
				Log.d("Debug", "No Location");
			}
				
			//restaurant.latitude = Double.parseDouble(jsonObject.getJSONObject("geometry").getJSONObject("location").getString("lat"));		// geometry:location:lat
			//restaurant.longitude = Double.parseDouble(jsonObject.getJSONObject("geometry").getJSONObject("location").getString("lng"));	// geometry:location:lng
			
			//restaurant.location = jsonObject.getJSONObject("geometry").getJSONObject("location");
			if (jsonObject.getString("rating") != null) {
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
		
		}
			catch (JSONException e) {
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
