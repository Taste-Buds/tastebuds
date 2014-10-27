package com.codepath.apps.tastebuds.fragments;

import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.apps.tastebuds.R;
import com.codepath.apps.tastebuds.models.DishReview;
import com.codepath.apps.tastebuds.models.RestaurantReview;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class DishReviewDetailDialog extends DialogFragment {
	private TextView tvRestaurantName;
	private TextView tvTags;
	private TextView etTags;
	//private TextView etWords;
	private EditText etReview;
	private EditText etDish;
	private RatingBar rbRating;
	private Button btnTaste;
	private ImageButton btnCancel;
	private String restaurantName;
	private String dishName;
	private String reviewId;
	public DishReviewDetailDialogListener listener;

    public static DishReviewDetailDialog newInstance(String reviewId, String restaurantName) {
    	DishReviewDetailDialog dialog = new DishReviewDetailDialog();
        Bundle args = new Bundle();
        args.putString("review_id", reviewId);
        args.putString("restaurant_name", restaurantName);
        dialog.setArguments(args);
        return dialog;
    }

    public interface DishReviewDetailDialogListener {
    	void onFinishReviewDialog(DishReview review);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dish_review_compose, container);
        restaurantName = getArguments().getString("restaurant_name");
        reviewId = getArguments().getString("review_id");
        etDish = (EditText) view.findViewById(R.id.etDishName);
        tvRestaurantName = (TextView) view.findViewById(R.id.tvRestaurantNameCompose);
        tvRestaurantName.setText(restaurantName);
        tvTags = (TextView) view.findViewById(R.id.tvComposeTags);
        etTags = (EditText) view.findViewById(R.id.etComposeTags);
        etReview = (EditText) view.findViewById(R.id.etComposeReview);
        rbRating = (RatingBar) view.findViewById(R.id.rbComposeReviewRatings);

        btnCancel = (ImageButton) view.findViewById(R.id.btnComposeBack);
 //       etWords = (TextView) view.findViewById(R.id.etWords);

        btnTaste = (Button) view.findViewById(R.id.btnComposeReview);
        btnTaste.setEnabled(false);
        btnTaste.setVisibility(Button.GONE);

        btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			    listener.onFinishReviewDialog(null);
				getDialog().dismiss();
			}
		});

        ParseQuery<DishReview> query = DishReview.getQuery();
        DishReview review;
		try {
			review = query.get(reviewId).fetchIfNeeded();
			dishName = review.getRestaurantName();
	//        etWords.setText(review.getText());
	  //      etWords.setEnabled(false);
	        etDish.setText(dishName);
	        etDish.setEnabled(false);
	        etTags.setText(review.getTags());
	        etTags.setEnabled(false);
	        rbRating.setRating(review.getRating());
	        rbRating.setEnabled(false);
		} catch (ParseException e) {
			e.printStackTrace();
		}
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
