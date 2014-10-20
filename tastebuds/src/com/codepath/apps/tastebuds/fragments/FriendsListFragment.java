package com.codepath.apps.tastebuds.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.codepath.apps.tastebuds.R;
import com.codepath.apps.tastebuds.activities.UserProfileActivity;
import com.codepath.apps.tastebuds.adapters.FriendsListAdapter;
import com.codepath.apps.tastebuds.models.DishReview;
import com.codepath.apps.tastebuds.models.RestaurantReview;
import com.codepath.apps.tastebuds.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class FriendsListFragment extends Fragment {

	ArrayList<Object> friends;
	ArrayAdapter<Object> friendsListAdapter;
	ListView lvFriends;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		friends = new ArrayList<Object>();
		friendsListAdapter = new FriendsListAdapter(getActivity(), friends);
		friends.addAll(ParseUser.getCurrentUser().getList("userFriends"));
		friendsListAdapter.notifyDataSetChanged();
		for(int i=0;i<friends.size();i++){
			populateReviewNumbers((ParseUser)(friends.get(i)));
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_friends_list, container, false);
		lvFriends = (ListView) v.findViewById(R.id.lvFriends);
		lvFriends.setAdapter(friendsListAdapter);
		lvFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			   public void onItemClick(AdapterView parentView, View childView, int position, long id) 
			   {
				   showUserDetail(position);
			   }
			});
		return v;
	}
	
	public void showUserDetail(int position) {
		Log.d("Debug", "P: " + position);
		Intent i = new Intent(getActivity(), UserProfileActivity.class);
		i.putExtra("user_id", ((ParseUser)friends.get(position)).getString("fbId"));
		startActivity(i);
	}
	public void  populateReviewNumbers( final ParseUser user){
	
		ParseQuery<RestaurantReview> restQuery = RestaurantReview.getQuery(user);
		
        restQuery.findInBackground(new FindCallback<RestaurantReview>() {

			@Override
			public void done(List<RestaurantReview> reviews, ParseException e) {
				//tvRestaurantReview.setText(Integer.toString(reviews.size()));
				if(user.has("restReviews")){
					user.remove("restReviews");
				}
				user.add("restReviews", reviews.size());
				//user.setNumRestReview(reviews.size());
				//((FriendsListAdapter) friendsListAdapter).setRestaurantReviewNum(reviews.size());
				friendsListAdapter.notifyDataSetChanged();
			}
		});
        
        ParseQuery<DishReview> dishQuery = DishReview.getQuery(user);
        dishQuery.findInBackground(new FindCallback<DishReview>() {

			@Override
			public void done(List<DishReview> reviews, ParseException e) {
				//tvDishReview.setText(Integer.toString(reviews.size()));	
				//tempUser.setNumDishReview(reviews.size());
				user.remove("dishReviews");
				user.add("dishReviews", reviews.size());
				//((FriendsListAdapter) friendsListAdapter).setDishReviewNum(reviews.size());
				friendsListAdapter.notifyDataSetChanged();
			}
		});
	}
}