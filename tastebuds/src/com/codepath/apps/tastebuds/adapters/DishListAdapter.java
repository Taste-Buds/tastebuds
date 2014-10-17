package com.codepath.apps.tastebuds.adapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.apps.tastebuds.R;
import com.codepath.apps.tastebuds.models.Dish;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class DishListAdapter extends ParseQueryAdapter<Dish> {

	public DishListAdapter(Context context, final String googlePlacesId,
			final List<ParseObject> friends) {
		super(context, new ParseQueryAdapter.QueryFactory<Dish>() {
			public ParseQuery<Dish> create() {
				ParseQuery<Dish> query = Dish.getQuery(
						googlePlacesId, friends);
				return query;
			}
		});
	}

	public DishListAdapter(Context context, final ParseUser user) {
		super(context, new ParseQueryAdapter.QueryFactory<Dish>() {
			public ParseQuery<Dish> create() {
				ParseQuery<Dish> query =
						Dish.getQuery(user);
				return query;
			}
		});
	}

	@Override
	public View getItemView(Dish dish, View view, ViewGroup parent) {
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
