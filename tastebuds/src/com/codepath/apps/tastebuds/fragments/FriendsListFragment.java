package com.codepath.apps.tastebuds.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.codepath.apps.tastebuds.R;
import com.codepath.apps.tastebuds.activities.UserProfileActivity;
import com.codepath.apps.tastebuds.adapters.FriendsListAdapter;
import com.parse.ParseUser;


public class FriendsListFragment extends Fragment {

	ArrayList<Object> friends;
	ArrayAdapter<Object> friendsListAdapter;
	ListView lvFriends;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		friends = new ArrayList<Object>();
		friendsListAdapter = new FriendsListAdapter(getActivity(), friends);
		friends.addAll(ParseUser.getCurrentUser().getList("userFriends"));
		friendsListAdapter.notifyDataSetChanged();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_friends_list, container, false);
		lvFriends = (ListView) v.findViewById(R.id.lvFriends);
		lvFriends.setAdapter(friendsListAdapter);
		lvFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			   public void onItemClick(AdapterView parentView, View childView, int position, long id) 
			   {
				   showUserDetail(position);
			   }
			});
		return v;
	}
	
	public void showUserDetail(int position) {
		Log.d("Debug", "P: " + position);
		Intent i = new Intent(getActivity(), UserProfileActivity.class);
		i.putExtra("user_id", ((ParseUser)friends.get(position)).getString("fbId"));
		startActivity(i);
	}

}