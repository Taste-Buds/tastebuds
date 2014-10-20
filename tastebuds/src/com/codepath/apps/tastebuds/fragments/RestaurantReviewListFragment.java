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

import com.codepath.apps.tastebuds.R;
import com.codepath.apps.tastebuds.adapters.ReviewListAdapter;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class RestaurantReviewListFragment extends Fragment {

	private ReviewListAdapter adapter;
	private RestaurantReviewListListener listener;
	private ListView lvReviews;
	private String googlePlacesId;
	private List<ParseObject> friends;

    public interface RestaurantReviewListListener {
    	void onReviewSelected(String reviewId, String restaurantName);
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        googlePlacesId = getArguments().getString("placeId");
        friends = ParseUser.getCurrentUser().getList("userFriends");
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_user_restaurant_review_list, container, false);
		lvReviews = (ListView) view.findViewById(R.id.lvUserReviews);
		lvReviews.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		lvReviews.setOnItemClickListener(adapter);
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof RestaurantReviewListListener) {
			listener = (RestaurantReviewListListener) activity;
			adapter = new ReviewListAdapter(getActivity(), googlePlacesId, friends, listener);
			adapter.setObjectsPerPage(5);
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implement RestaurantReviewDialog.RestaurantReviewListListener");
		}
	}
}
