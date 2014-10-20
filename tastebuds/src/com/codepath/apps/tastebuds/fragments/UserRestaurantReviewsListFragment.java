package com.codepath.apps.tastebuds.fragments;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.codepath.apps.tastebuds.R;
import com.codepath.apps.tastebuds.adapters.UserReviewListAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class UserRestaurantReviewsListFragment extends Fragment {

	private UserReviewListAdapter adapter;
	private UserRestaurantReviewListListener listener;
	private ListView lvUserReviews;
	private String userId;
	private static ParseUser user;
	private ProgressBar mProgress;

    public interface UserRestaurantReviewListListener {
    	void onReviewSelected(String reviewId, String restaurantName);
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getString("user_id");
 
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user_restaurant_review_list, container, false);
		lvUserReviews = (ListView) view.findViewById(R.id.lvUserReviews);
		mProgress = (ProgressBar) view.findViewById(R.id.pbReviewList);
		mProgress.setVisibility(ProgressBar.VISIBLE);

	    ParseQuery<ParseUser> userQuery = ParseUser.getQuery().whereEqualTo("fbId", userId);
	        userQuery.findInBackground(new FindCallback<ParseUser>() {

				@Override
				public void done(List<ParseUser> users, ParseException arg1) {
						adapter = new UserReviewListAdapter(getActivity(),  users.get(0), listener);
						lvUserReviews.setAdapter(adapter);
						adapter.notifyDataSetChanged();
						lvUserReviews.setOnItemClickListener(adapter);
						mProgress.setVisibility(ProgressBar.GONE);
				}

			});
		


		return view;
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof UserRestaurantReviewListListener) {
			listener = (UserRestaurantReviewListListener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implement RestaurantReviewDialog.UserRestaurantReviewListListener");
		}
	}
}
