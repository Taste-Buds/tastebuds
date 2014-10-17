package com.codepath.apps.tastebuds.fragments;

import java.util.List;

import com.codepath.apps.tastebuds.adapters.DishListAdapter;
import com.codepath.apps.tastebuds.models.Dish;
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
	private String googlePlacesId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		googlePlacesId = "monrez";
        List<ParseObject> friends = ParseUser.getCurrentUser().getList("userFriends");
		adapter = new DishListAdapter(getActivity(), googlePlacesId, friends);
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
