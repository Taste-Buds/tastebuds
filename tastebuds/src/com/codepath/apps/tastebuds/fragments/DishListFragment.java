package com.codepath.apps.tastebuds.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.codepath.apps.tastebuds.adapters.DishListAdapter;
import com.codepath.apps.tastebuds.models.Dish;
import com.codepath.apps.tastebuds.models.DishReview;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

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

	private DishListAdapter adapter;
	private ListView lvDishes;
	private List<Dish> dishes;
	private String googlePlacesId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		googlePlacesId = "monrez";
        List<ParseObject> friends = ParseUser.getCurrentUser().getList("userFriends");
        ParseQuery<DishReview> query = DishReview.getQuery(googlePlacesId, friends);
        dishes = new ArrayList<Dish>();
		HashMap<String, Dish> reviewMap = new HashMap<String, Dish>();
		try {
			List<DishReview>  dishReviews = query.find();
			for (DishReview review : dishReviews) {
				String dishName = review.getDishName();
				if (!reviewMap.containsKey(dishName)) {
					Dish dish = new Dish();
					dish.setName(dishName);
					dish.setGooglePlacesId(googlePlacesId);
					reviewMap.put(dishName, dish);
				}
				reviewMap.get(dishName).addReview(review);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		dishes.addAll(reviewMap.values());
		adapter = new DishListAdapter(getActivity(), dishes);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_dish_list, container, false);
		lvDishes = (ListView) view.findViewById(R.id.lvDishes);
		lvDishes.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		return view;
	}
}
