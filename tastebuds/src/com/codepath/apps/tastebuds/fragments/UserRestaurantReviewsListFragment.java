package com.codepath.apps.tastebuds.fragments;

import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.codepath.apps.tastebuds.R;
import com.codepath.apps.tastebuds.adapters.ReviewListAdapter;
import com.codepath.apps.tastebuds.models.RestaurantReview;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class UserRestaurantReviewsListFragment extends Fragment {

	private ReviewListAdapter adapter;
	private ReviewListListener listener;
	private ListView lvUserReviews;
	private String userId;
	private static ParseUser user;

    public interface ReviewListListener {
    	void onReviewSelected(String reviewId, String restaurantName);
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getString("userId");
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_restaurant_review_list, container, false);
		lvUserReviews = (ListView) view.findViewById(R.id.lvUserReviews);
		
		
	        ParseQuery<ParseUser> userQuery = ParseUser.getQuery()
	        		.whereContains("fbId", userId);
	        userQuery.findInBackground(new FindCallback<ParseUser>() {
			
					

				@Override
				public void done(List<ParseUser> users, ParseException e) {
					adapter = new ReviewListAdapter(getActivity(),  users.get(0));
					lvUserReviews.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				}
					
				

			});
		
		adapter = new ReviewListAdapter(getActivity(), user);
		lvUserReviews.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		return view;
	}
}
