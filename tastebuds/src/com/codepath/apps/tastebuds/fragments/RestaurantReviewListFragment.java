package com.codepath.apps.tastebuds.fragments;

import com.codepath.apps.tastebuds.R;
import com.codepath.apps.tastebuds.models.Dish;
import com.codepath.apps.tastebuds.models.RestaurantReview;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class RestaurantReviewListFragment extends Fragment {

	private ParseQueryAdapter<RestaurantReview> adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ParseQueryAdapter.QueryFactory<RestaurantReview> factory =
				new ParseQueryAdapter.QueryFactory<RestaurantReview>() {
			public ParseQuery<RestaurantReview> create() {
				ParseQuery<RestaurantReview> query = RestaurantReview.getQuery();
				query.include("name");
				query.orderByDescending("createdAt");
				return query;
			}
		};

		adapter = new ParseQueryAdapter<RestaurantReview>(getActivity(), factory) {
			public View getItemView(Dish dish, View view, ViewGroup parent) {
				if (view == null) {
					view = view.inflate(getContext(), R.layout.restaurant_list_item, null);
				}
				TextView tvDish = (TextView) view.findViewById(R.id.lvRestaurants);
				tvDish.setText(dish.getName());
				return view;
			}
		};
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_dish_list, container);
		ListView lvDishes = (ListView) view.findViewById(R.id.lvDishes);
		lvDishes.setAdapter(adapter);
		lvDishes.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				RestaurantReview review = adapter.getItem(position);
			}
		});
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}
