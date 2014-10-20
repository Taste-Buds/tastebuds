package com.codepath.apps.tastebuds.adapters;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.apps.tastebuds.R;
import com.codepath.apps.tastebuds.models.Dish;

public class DishListAdapter extends ArrayAdapter<Dish> {

	/*public DishListAdapter(Context context, final String googlePlacesId,
			final List<ParseObject> friends) {
		super(context, new ParseQueryAdapter.QueryFactory<DishReview>() {
			public ParseQuery<DishReview> create() {
				ParseQuery<DishReview> query = DishReview.getQuery(
						googlePlacesId, friends);
				return query;
			}
		});
	}

	public DishListAdapter(Context context, final ParseUser user) {
		super(context, new ParseQueryAdapter.QueryFactory<DishReview>() {
			public ParseQuery<DishReview> create() {
				ParseQuery<DishReview> query = DishReview.getQuery(user);
				return query;
			}
		});
	}*/

	public DishListAdapter(Context context, List<Dish> dishes) {
		super(context, 0, dishes);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Dish dish = getItem(position);
		View view;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			view = inflater.inflate(R.layout.review_list_item, parent, false);
		} else {
			view = convertView;
		}

		ImageView userImage = (ImageView) view.findViewById(R.id.ivReviewUser);

		TextView username = (TextView) view.findViewById(R.id.tvReviewUsername);
		TextView content = (TextView) view.findViewById(R.id.tvReviewContent);
		RatingBar rating = (RatingBar) view.findViewById(R.id.rbReviewRating);
		TextView time = (TextView) view.findViewById(R.id.tvReviewTime);
		time.setVisibility(TextView.GONE);

		content.setVisibility(TextView.GONE);
		username.setText(dish.getName());
		rating.setRating(dish.getRating());
		rating.setEnabled(false);
		return view;
	}

	/*@Override
	public View getItem(Dish dish, View view, ViewGroup parent) {
		if (view == null) {
			view = View.inflate(getContext(), R.layout.review_list_item, null);
		}
		super.getItemView(dish, view, parent);

		ImageView userImage = (ImageView) view.findViewById(R.id.ivReviewUser);
		TextView username = (TextView) view.findViewById(R.id.tvReviewUsername);
		TextView content = (TextView) view.findViewById(R.id.tvReviewContent);
		RatingBar rating = (RatingBar) view.findViewById(R.id.rbReviewRating);

		username.setText("abcdefgh");//review.getUser().getUsername());
		content.setText(dish.getName());
		rating.setRating(4);
		return view;
	}*/

	/*@Override
	public View getNextPageView(View view, ViewGroup parent) {
		if (view == null) {
			view = View.inflate(getContext(), R.layout.review_list_item, null);
		}
		TextView
		return view;
	}*/
}
