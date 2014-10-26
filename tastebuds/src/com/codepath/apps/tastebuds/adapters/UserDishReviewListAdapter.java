package com.codepath.apps.tastebuds.adapters;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.codepath.apps.tastebuds.R;
import com.codepath.apps.tastebuds.fragments.UserDishReviewsListFragment.UserDishReviewListListener;
import com.codepath.apps.tastebuds.fragments.UserRestaurantReviewsListFragment.UserRestaurantReviewListListener;
import com.codepath.apps.tastebuds.models.DishReview;
import com.codepath.apps.tastebuds.models.RestaurantReview;
import com.parse.ParseException;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class UserDishReviewListAdapter extends ParseQueryAdapter<DishReview> implements OnItemClickListener{
	private UserDishReviewListListener uListener;
	public UserDishReviewListAdapter(Context context, final String googlePlacesId,
			final List<ParseObject> friends) {
		super(context, new ParseQueryAdapter.QueryFactory<DishReview>() {
			public ParseQuery<DishReview> create() {
				ParseQuery<DishReview> query = DishReview.getQuery(googlePlacesId, friends);
				return query;
			}
		});
	}

	public UserDishReviewListAdapter(Context context, final String googlePlacesId,
			final List<ParseObject> friends, final String dishName) {
		super(context, new ParseQueryAdapter.QueryFactory<DishReview>() {
			public ParseQuery<DishReview> create() {
				ParseQuery<DishReview> query = DishReview.getQuery(googlePlacesId, friends,
						dishName);
				return query;
			}
		});
	}

	public UserDishReviewListAdapter(Context context,final ParseUser user) {
		super(context, new ParseQueryAdapter.QueryFactory<DishReview>() {
			public ParseQuery<DishReview> create() {
				ParseQuery<DishReview> query = DishReview.getQuery(user);
				return query;
			}
		});
	}

	public UserDishReviewListAdapter(Context context,final ParseUser user, UserDishReviewListListener listener) {
		super(context, new ParseQueryAdapter.QueryFactory<DishReview>() {
			public ParseQuery<DishReview> create() {
				ParseQuery<DishReview> query = DishReview.getQuery(
						user);
				return query;
			}
			
		});
		this.uListener = listener;
	}
	@Override
	public View getItemView(DishReview review, View view, ViewGroup parent) {
		if (view == null) {
			view = View.inflate(getContext(), R.layout.review_list_item, null);
		}
		super.getItemView(review, view, parent);

		ImageView userImage = (ImageView) view.findViewById(R.id.ivReviewUser);
		TextView username = (TextView) view.findViewById(R.id.tvReviewUsername);
		TextView content = (TextView) view.findViewById(R.id.tvReviewContent);
		RatingBar rating = (RatingBar) view.findViewById(R.id.rbReviewRating);
		TextView time = (TextView) view.findViewById(R.id.tvReviewTime);

		ParseUser reviewer;
		try {
			reviewer = review.getUser().fetchIfNeeded();
		
		} catch (ParseException e) {
			e.printStackTrace();
		}

		content.setText(review.getText());
		rating.setRating(review.getRating());
		username.setText(review.getDishName());
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		time.setText(df.format(review.getCreatedAt()));
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(uListener != null){
			DishReview review = getItem(position);
			uListener.onDishReviewSelected(review.getObjectId(), review.getDishName());
			Toast.makeText(getContext(), "Review clicked", Toast.LENGTH_LONG).show();
		}else if(uListener != null){
			DishReview review = getItem(position);
			uListener.onDishReviewSelected(review.getObjectId(), review.getDishName());
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
