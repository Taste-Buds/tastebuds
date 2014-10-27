package com.codepath.apps.tastebuds.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.apps.tastebuds.R;
import com.codepath.apps.tastebuds.models.DishReview;
import com.codepath.apps.tastebuds.models.RestaurantReview;
import com.codepath.apps.tastebuds.models.Tag;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.rockerhieu.emojicon.EmojiconsFragment;

public class DishReviewDialog extends DialogFragment {

	private TextView tvRestaurantName;
	private TextView tvDish;
	private TextView tvTags;
	private AutoCompleteTextView etTags;
	//private TextView etWords;
	private EditText etReview;
	private AutoCompleteTextView etDish;
	private RatingBar rbRating;
	private Button btnTaste;
	private ImageButton btnCancel;
	private String restaurantName;
	private String restaurantId;
	public DishReviewDialogListener listener;
	private ArrayList<String> tagStrings;
	private ArrayList<String> dishStrings;

    public static DishReviewDialog newInstance(String dishName,
    		String dishId) {
    	DishReviewDialog dialog = new DishReviewDialog();
        Bundle args = new Bundle();
        args.putString("dish_id", dishId);
        args.putString("dish_name", dishName);
        dialog.setArguments(args);
   //     setEmojiconFragment(false);
        return dialog;
    }
//    
//	private void setEmojiconFragment(boolean useSystemDefault) {
//		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
//		nf = (EmojiconsFragment) EmojiconsFragment.newInstance(useSystemDefault);
//		ft.add(R.id.emojicons, nf,"main");
//		ft.hide(nf);
//		ft.commit();
//	}

    public interface DishReviewDialogListener {
    	void onFinishReviewComposeDialog(DishReview review);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dish_review_compose, container);
        restaurantName = getArguments().getString("dish_name");
        restaurantId = getArguments().getString("dish_id");
        etDish = (AutoCompleteTextView) view.findViewById(R.id.etDishName);
        tvRestaurantName = (TextView) view.findViewById(R.id.tvRestaurantNameCompose);
        tvRestaurantName.setText(restaurantName);
        tvTags = (TextView) view.findViewById(R.id.tvComposeTags);
        etTags = (AutoCompleteTextView) view.findViewById(R.id.etComposeTags);
        etReview = (EditText) view.findViewById(R.id.etComposeReview);
        rbRating = (RatingBar) view.findViewById(R.id.rbComposeReviewRatings);

        btnTaste = (Button) view.findViewById(R.id.btnComposeReview);
        btnTaste.setGravity(android.view.Gravity.CENTER);
        btnCancel = (ImageButton) view.findViewById(R.id.btnComposeBack);

        /*etWords = (TextView) view.findViewById(R.id.etWords);

        final TextWatcher txwatcher = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				etWords.setText(Html.fromHtml("<font color=\"#FFFFFF\" type=\"roboto\">" + 
						String.valueOf(420 - s.length()) + " | </font>"));
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				etWords.setText(Html.fromHtml("<font color=\"#606060\" type=\"roboto\">420 | </font>"));
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		};
		etReview.addTextChangedListener(txwatcher);*/

		tagStrings = new ArrayList<String>();
		ParseQuery<Tag> query = Tag.getQuery();
		query.findInBackground(new FindCallback<Tag>() {
			@Override
			public void done(List<Tag> tags, ParseException e) {
				for (Tag tag : tags) {
					tagStrings.add(tag.getTag());
				}
				ArrayAdapter<String> tagsAdapter = new ArrayAdapter<String>(getActivity(),
						android.R.layout.simple_list_item_1, tagStrings);
				etTags.setAdapter(tagsAdapter);
			}
		});

		dishStrings = new ArrayList<String>();
		ParseQuery<DishReview> dishQuery = DishReview.getQuery();
		dishQuery.findInBackground(new FindCallback<DishReview>() {
			@Override
			public void done(List<DishReview> reviews, ParseException e) {
				for (DishReview review : reviews) {
					dishStrings.add(review.getDishName());
				}
				ArrayAdapter<String> dishesAdapter = new ArrayAdapter<String>(getActivity(),
						android.R.layout.simple_list_item_1, dishStrings);
				etDish.setAdapter(dishesAdapter);
			}
		});

        btnTaste.setOnClickListener(new View.OnClickListener() {
        	@Override
            public void onClick(View v) {
        		DishReview review = new DishReview();
        		review.setText(etReview.getText().toString());
        		review.setRating(rbRating.getRating());
        		review.setGooglePlacesId(restaurantId);
        		review.setUser(ParseUser.getCurrentUser());
        		review.setDishName(etDish.getText().toString());
        		review.setTags(restaurantId, etTags.getText().toString());
        		review.setRestaurantName(restaurantName);
        		listener.onFinishReviewComposeDialog(review);
        		getDialog().dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			    listener.onFinishReviewComposeDialog(null);
				getDialog().dismiss();
			}
		});

    	Window window = getDialog().getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setGravity(Gravity.CENTER);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return view;
    }

    @Override
    public void onStart() {
    	super.onStart();
    	// safety check
    	if (getDialog() == null) {
    		return;
    	}
    	getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
    		ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
