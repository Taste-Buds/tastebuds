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

public class RestaurantReviewListFragment extends Fragment {

	private ReviewListAdapter adapter;
	private ReviewListListener listener;
	private ListView lvReviews;
	private long googlePlacesId;

    public interface ReviewListListener {
    	void onReviewSelected(String reviewId, String restaurantName);
    }

    static RestaurantReviewListFragment newInstance(String userId, long googlePlacesId) {
    	RestaurantReviewListFragment fragment = new RestaurantReviewListFragment();
        Bundle args = new Bundle();
        args.putString("user_id", userId);
        args.putLong("google_places_id", googlePlacesId);
        fragment.setArguments(args);
        return fragment;
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String userId =  getArguments().getString("user");
        googlePlacesId = getArguments().getLong("google_places_id");
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user_restaurant_review_list, container, false);
		lvReviews = (ListView) view.findViewById(R.id.lvUserReviews);

        List<Object> friendsId = (List<Object>) ParseUser.getCurrentUser().getList("userFriends");
        ParseQuery<ParseUser> friendsQuery = ParseUser.getQuery()
        		.whereContainedIn("fbId", friendsId);
        friendsQuery.findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(List<ParseUser> friends, ParseException arg1) {
				adapter = new ReviewListAdapter(getActivity(), googlePlacesId, friends);
				lvReviews.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
		});

		/*lvReviews.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				RestaurantReview review = adapter.getItem(position);
			}
		});*/
		//super.onCreateView(inflater, container, savedInstanceState);
		return view;
	}
}
