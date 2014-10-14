package com.codepath.apps.tastebuds.fragments;

import com.codepath.apps.tastebuds.models.Dish;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.codepath.apps.tastebuds.R;

public class DishListFragment extends Fragment {

	private ParseQueryAdapter<Dish> adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ParseQueryAdapter.QueryFactory<Dish> factory = new ParseQueryAdapter.QueryFactory<Dish>() {
			public ParseQuery<Dish> create() {
				ParseQuery<Dish> query = Dish.getQuery();
				query.include("name");
				query.orderByDescending("createdAt");
				return query;
			}
		};
		adapter = new ParseQueryAdapter<Dish>(getActivity(), factory) {
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
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}
