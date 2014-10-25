package com.codepath.apps.tastebuds.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codepath.apps.tastebuds.GooglePlacesApiClient;
import com.codepath.apps.tastebuds.R;
import com.codepath.apps.tastebuds.activities.RestaurantDetailActivity;
import com.codepath.apps.tastebuds.adapters.RestaurantAdapter;
import com.codepath.apps.tastebuds.adapters.ReviewListAdapter;
import com.codepath.apps.tastebuds.adapters.TextWatcherAdapter;
import com.codepath.apps.tastebuds.fragments.RestaurantReviewListFragment.RestaurantReviewListListener;
import com.codepath.apps.tastebuds.models.Restaurant;
import com.codepath.apps.tastebuds.models.RestaurantReview;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment.OnEmojiconClickedListener;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;
import com.codepath.apps.tastebuds.listeners.EndlessScrollListener;


public class RestaurantListFragment extends Fragment implements OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener {
	
	List<Restaurant> restaurants;
	List<String> placeIds;
	List<String> newPlaceIds;
	ArrayAdapter<Restaurant> restaurantAdapter;
	ListView lvRestaurants;
	List<ParseObject> friends;
	//List<RestaurantReview> reviews;
	Location mCurrentLocation;
	int parsingPageToken;
	String nextPageToken;
	SearchView searchView;
	ImageView searchImg;
	EmojiconsFragment nf;
	private RestaurantListListener listener;

	EmojiconEditText mEditEmojicon;
    //EmojiconTextView mTxtEmojicon;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mCurrentLocation = getArguments().getParcelable("mCurrentLocation");
		restaurants = new ArrayList<Restaurant>();
		restaurantAdapter = new RestaurantAdapter(getActivity(), restaurants);
		placeIds = new ArrayList<String>();
		newPlaceIds = new ArrayList<String>();
		friendsFromFacebook();
		parsingPageToken = 0;
		nextPageToken = "None";
		String search = "None";
		restaurantsFromGooglePlacesApi(search, nextPageToken);
		setHasOptionsMenu(true);
		
	}

    public interface RestaurantListListener {
    	void onRestaurantSelected(String place_id);
    }

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

	    setEmojiconFragment(false);

		lvRestaurants = (ListView) v.findViewById(R.id.lvRestaurants);
		lvRestaurants.setAdapter(restaurantAdapter);
		lvRestaurants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			   public void onItemClick(AdapterView parentView, View childView, int position, long id) {
				   listener.onRestaurantSelected(restaurants.get(position).getPlace_id());
			   } 
			});		
        lvRestaurants.setOnScrollListener(new EndlessScrollListener() {
	    @Override
	    public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
	    		String search = "None";
	    		restaurantsFromGooglePlacesApi(search, nextPageToken);
	           	restaurantAdapter.notifyDataSetChanged();
	    }
        });        
		return v;
	}

	private void friendsFromFacebook() {
		// Get array of friends from Facebook API
		friends = ParseUser.getCurrentUser().getList("userFriends");
	}

	private void restaurantsFromGooglePlacesApi(String search, String nextPageToken) {
		
		GooglePlacesApiClient placesApi = new GooglePlacesApiClient();
		// 700 Illinois Street, SF = 37.764046, -122.387863
		//double latitude = 37.764046; // Static for 700 Illinois
		//double longitude = -122.387863; // Static for 700 Illinois
		
		double latitude = mCurrentLocation.getLatitude();
		double longitude = mCurrentLocation.getLongitude();
		
		placesApi.getRestaurantListfromGooglePlaces(search, nextPageToken, latitude, longitude,
				new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
        		JSONArray placesApiResultsJson = null;
        		try {
        			placesApiResultsJson = response.getJSONArray("results");
        			String newNextPageToken = "";
        			try {
        			newNextPageToken = response.getString("next_page_token");
        			} catch (Exception e) {
        				newNextPageToken = "QueryLimitReached";
        			}
        			updateNextPageToken(newNextPageToken);
        			List<Restaurant> newRestaurants = Restaurant.fromJSONArray(placesApiResultsJson);
        			
        			newPlaceIds.clear();
        			for(int i=0; i<newRestaurants.size(); i++) {
        				Restaurant restaurant = newRestaurants.get(i);
        				String placeId = restaurant.getPlace_id();
        				newPlaceIds.add(placeId);
        				calcuateDistancetoUser(restaurant);
        			}
        			placeIds.addAll(newPlaceIds);
        			restaurants.addAll(newRestaurants);
        			//Log.d("Debug", "Added more Restaurants");
        			restaurantReviewsWithGoogleAndFacebookData(newRestaurants);
        			
				} catch (JSONException e) {
					e.printStackTrace();
				}
            	restaurantAdapter.notifyDataSetChanged();
        	}
			@Override
    		public void onFailure(Throwable e, JSONObject errorResponse) {
				Log.e("Error", e.toString());
    		}
			
		});	
	}
	
	private void calcuateDistancetoUser(Restaurant restaurant) {
		Location locationRestaurant = new Location("");
		try {locationRestaurant = restaurant.getLocation();}
			catch (Exception e) { Log.d("Debug", "No Rest Location"); }
		if (mCurrentLocation == null) {
			Log.d("Debug", "No User Location");
		}
		if (restaurant.getLocation() == null) {
			Log.d("Debug", "No Restaurant Location");
		}
		float distance = 0;
		try { distance = mCurrentLocation.distanceTo(locationRestaurant); }
			catch (Exception e){ //Log.d("Debug", "Can't get Location B"); 
				
			}
		restaurant.setCurrentDistancetoUser(distance);
	}
	
	private void updateNextPageToken (String newNextPageToken) {
		setNextPageToken(newNextPageToken);
	}
	

	public String getNextPageToken() {
		return nextPageToken;
	}

	public void setNextPageToken(String nextPageToken) {
		this.nextPageToken = nextPageToken;
	}

	private void restaurantReviewsWithGoogleAndFacebookData(List<Restaurant> newRestaurants) {

		ParseQuery<RestaurantReview> query = RestaurantReview.getQuery(newPlaceIds, friends);
		query.findInBackground(new FindCallback<RestaurantReview>() {
			@Override
			  public void done(List<RestaurantReview> results, ParseException e) {
				    // results has the list of user reviews from Parse
					List<RestaurantReview> newReviews = new ArrayList<RestaurantReview>();
				  	newReviews = results;
				  	parseReviews(newReviews);
				  }
			});
	}

	private void parseReviews(List<RestaurantReview> newReviews) {
		for(int i=0; i<newPlaceIds.size();i++) {
			String placeId = newPlaceIds.get(i);
			int restaurantArrayPosition = (parsingPageToken*20)+i;
			Restaurant restaurant = restaurants.get(restaurantArrayPosition);
			int numberOfReviews = 0;
			int sumOfRatings = 0;
			for(int j=0; j<newReviews.size();j++) {
				RestaurantReview review = newReviews.get(j);
				String placeIdReview = review.getGooglePlacesId();
				if (placeId.equals(placeIdReview)) {
					int rating = review.getRating();
					numberOfReviews++;
					sumOfRatings = sumOfRatings + rating;
				}
			}
			float friendRating = 0;
			if (numberOfReviews > 0) {
				friendRating = sumOfRatings/numberOfReviews;
			}
			else {
				friendRating = 0;
				numberOfReviews = 0;
			}
			restaurant.setFriendRating(friendRating);
			restaurant.setNumOfReviews(numberOfReviews);		
		}
		parsingPageToken++;
		
		restaurantAdapter.notifyDataSetChanged();
	}
	
	/*public void showRestaurantDetail(int position) {
		//Log.d("Debug", "P: " + position);
		Intent i = new Intent(getActivity(), RestaurantDetailActivity.class);
		i.putExtra("place_id", restaurants.get(position).getPlace_id());
		startActivity(i);
	}*/

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    super.onCreateOptionsMenu(menu, inflater);

	    inflater.inflate(R.menu.search, menu);
	    MenuItem searchItem = menu.findItem(R.id.action_search);
	    /** Get the action view of the menu item whose id is search */
        View v = (View) searchItem.getActionView();
 
        /** Get the edit text from the action view */
        mEditEmojicon = ( EmojiconEditText ) v.findViewById(R.id.txt_search);
        searchImg = (ImageView) v.findViewById(R.id.searchImg);
	    
        searchImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				restaurants.clear();
	    		placeIds.clear();
	    		newPlaceIds.clear();
	    		parsingPageToken = 0;
	    	   String search = mEditEmojicon.getText().toString();
	    	   String nextPageToken = "None";
	    	   restaurantsFromGooglePlacesApi(search, nextPageToken);
	    	   // Dismiss Keyboard
	    	   hideSoftKeyBoard();	    	   
	           				
			}
		});
	    searchItem.setOnActionExpandListener(new OnActionExpandListener() {
	    	@Override
	    	public boolean onMenuItemActionExpand(MenuItem item) {
	    	    Log.d("Debug","onMenuItemActionExpand");
	        	FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		        ft.show(nf);
		        ft.commit();
	            return true;

	    	}

	    	@Override
	    	public boolean onMenuItemActionCollapse(MenuItem item) {
	    	    Log.d("Debug","onMenuItemActionCollapse");
	        	FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		        ft.hide(nf);
		        ft.commit();
	    		restaurants.clear();
	    		placeIds.clear();
	    		newPlaceIds.clear();
	    		parsingPageToken = 0;
	    	   String search = "None";
	    	   String nextPageToken = "None";
	    	   restaurantsFromGooglePlacesApi(search, nextPageToken);
	    	    return true;
	    	}
	    });
	//    mEditEmojicon = (EmojiconEditText) searchItem.getActionView();
       
    //setEmojiconFragment(false);
//    
    mEditEmojicon.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			FragmentTransaction ft = getChildFragmentManager().beginTransaction();
	        ft.show(nf);
	        ft.commit();
			
		}
	});
	   // mEditEmojicon.setOnQueryTextListener(queryTextListener);
	    //searchView = (SearchView) searchItem.
     
    
    mEditEmojicon.setOnEditorActionListener(new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            	restaurants.clear();
	    		placeIds.clear();
	    		newPlaceIds.clear();
	    		parsingPageToken = 0;
	    	   String search = v.getText().toString();
	    	   String nextPageToken = "None";
	    	   restaurantsFromGooglePlacesApi(search, nextPageToken);
	    	   // Dismiss Keyboard
	    	   hideSoftKeyBoard();	    	   
	            return true;
            }
            return false;
        }
    });
//    mEditEmojicon.setOnQueryTextListener(new OnQueryTextListener() {
//	       @Override
//	       public boolean onQueryTextSubmit(String query) {
//	            // perform query here
//	    		restaurants.clear();
//	    		placeIds.clear();
//	    		newPlaceIds.clear();
//	    		parsingPageToken = 0;
//	    	   String search = query;
//	    	   String nextPageToken = "None";
//	    	   restaurantsFromGooglePlacesApi(search, nextPageToken);
//	    	   // Dismiss Keyboard
//	    	   hideSoftKeyBoard();	    	   
//	            return true;
//	       }
//
//	       @Override
//	       public boolean onQueryTextChange(String newText) {
//	           return false;
//	       }
//	   });
//	    searchView.setOnCloseListener(new OnCloseListener() {
//	        @Override
//	        public boolean onClose() {
//	            Log.d("Debug","Testing. 1, 2, 3...");
//	            return false;
//	        }
//	    });
	}
	
	private void hideSoftKeyBoard() {
		Context context = getActivity().getBaseContext();
	    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
	    if(imm.isAcceptingText()) {                      
	        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
	    }
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof RestaurantListListener) {
			listener = (RestaurantListListener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implement RestaurantReviewDialog.RestaurantReviewListListener");
		}
	}


	private void setEmojiconFragment(boolean useSystemDefault) {
		
		
		  FragmentTransaction ft = getChildFragmentManager().beginTransaction();
	        nf = (EmojiconsFragment) EmojiconsFragment.newInstance(useSystemDefault);
	        ft.add(R.id.emojicons, nf,"main");
	        ft.hide(nf);
	        ft.commit();
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault))
//                .commit();
    }

	@Override
	public void onEmojiconClicked(Emojicon emojicon) {
		EmojiconsFragment.input(mEditEmojicon, emojicon);
	}

//	@Override
//	public void onEmojiconBackspaceClicked(View v) {
//		// TODO Auto-generated method stub
//		
//	}

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(mEditEmojicon);
    }

//	@Override
//	public void onEmojiconClicked(Emojicon emojicon) {
//		  EmojiconsFragment.input(mEditEmojicon, emojicon);
//		
//	}
}