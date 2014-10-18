package com.codepath.apps.tastebuds.fragments;

import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.tastebuds.R;
import com.codepath.apps.tastebuds.adapters.ReviewListAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class RestaurantReviewListFragment extends Fragment {

	private ReviewListAdapter adapter;
	private ReviewListListener listener;
	private ListView lvReviews;
	private String googlePlacesId;

    public interface ReviewListListener {
    	void onReviewSelected(String reviewId, String restaurantName);
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        googlePlacesId = getArguments().getString("placeId");
        List<ParseObject> friends = ParseUser.getCurrentUser().getList("userFriends");
		adapter = new ReviewListAdapter(getActivity(), googlePlacesId, friends);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_user_restaurant_review_list, container, false);
		lvReviews = (ListView) view.findViewById(R.id.lvUserReviews);
		lvReviews.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		/*lvReviews.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				RestaurantReview review = adapter.getItem(position);
			}
		});*/
		return view;
	}
}
