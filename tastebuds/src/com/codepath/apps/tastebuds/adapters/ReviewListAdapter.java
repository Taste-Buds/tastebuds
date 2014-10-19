package com.codepath.apps.tastebuds.adapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.tastebuds.R;
import com.codepath.apps.tastebuds.fragments.RestaurantReviewListFragment.RestaurantReviewListListener;
import com.codepath.apps.tastebuds.fragments.UserRestaurantReviewsListFragment.UserRestaurantReviewListListener;
import com.codepath.apps.tastebuds.models.RestaurantReview;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class ReviewListAdapter extends ParseQueryAdapter<RestaurantReview>
	implements OnItemClickListener {

	private RestaurantReviewListListener listener;
	private UserRestaurantReviewListListener uListener;
	public ReviewListAdapter(Context context, final String googlePlacesId,
			final List<ParseObject> friends, RestaurantReviewListListener listener) {
		super(context, new ParseQueryAdapter.QueryFactory<RestaurantReview>() {
			public ParseQuery<RestaurantReview> create() {
				ParseQuery<RestaurantReview> query = RestaurantReview.getQuery(googlePlacesId, friends);
				return query;
			}
		});
		this.listener = listener;
	}

	public ReviewListAdapter(Context context,final ParseUser user, UserRestaurantReviewListListener listener) {
		super(context, new ParseQueryAdapter.QueryFactory<RestaurantReview>() {
			public ParseQuery<RestaurantReview> create() {
				ParseQuery<RestaurantReview> query = RestaurantReview.getQuery(
						user);
				return query;
			}
			
		});
		this.uListener = listener;
	}
	@Override
	public View getItemView(RestaurantReview review, View view, ViewGroup parent) {
		if (view == null) {
			view = View.inflate(getContext(), R.layout.review_list_item, null);
		}
		super.getItemView(review, view, parent);

		ImageView userImage = (ImageView) view.findViewById(R.id.ivReviewUser);
		TextView username = (TextView) view.findViewById(R.id.tvReviewUsername);
		TextView content = (TextView) view.findViewById(R.id.tvReviewContent);
		RatingBar rating = (RatingBar) view.findViewById(R.id.rbReviewRating);

		username.setText("abcdefgh"); //review.getUser().getUsername());
		content.setText(review.getText());
		rating.setRating(review.getRating());
		rating.setEnabled(false);
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(listener != null){
			RestaurantReview review = getItem(position);
			listener.onReviewSelected(review.getObjectId(), review.getGooglePlacesId());
			Toast.makeText(getContext(), "Review clicked", Toast.LENGTH_LONG).show();
		}else if(uListener != null){
			RestaurantReview review = getItem(position);
			uListener.onReviewSelected(review.getObjectId(), review.getGooglePlacesId());
			Toast.makeText(getContext(), "Review clicked", Toast.LENGTH_LONG).show();
		}
	}

	/*@Override
	public View getNextPageView(View view, ViewGroup parent) {
		if (view == null) {
			view = View.inflate(getContext(), R.layout.review_list_item, null);
		}
		TextView
		return view;
	}*/
}
