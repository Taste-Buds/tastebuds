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
import com.parse.ParseUser;

public class DishReviewDialog extends DialogFragment {

	private TextView tvRestaurantName;
	private TextView tvDish;
	private TextView tvTags;
	private TextView etTags;
	private TextView etWords;
	private EditText etReview;
	private EditText etDish;
	private RatingBar rbRating;
	private Button btnTaste;
	private ImageButton btnCancel;
	private String restaurantName;
	private String restaurantId;
	public DishReviewDialogListener listener;

    public static DishReviewDialog newInstance(String restaurantName,
    		String restaurantId) {
    	DishReviewDialog dialog = new DishReviewDialog();
        Bundle args = new Bundle();
        args.putString("restaurant_id", restaurantId);
        args.putString("restaurant_name", restaurantName);
        dialog.setArguments(args);
        return dialog;
    }

    public interface DishReviewDialogListener {
    	void onFinishReviewComposeDialog(DishReview review);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dish_review_compose, container);
        restaurantName = getArguments().getString("restaurant_name");
        restaurantId = getArguments().getString("restaurant_id");
        etDish = (EditText) view.findViewById(R.id.etDishName);
        tvRestaurantName = (TextView) view.findViewById(R.id.tvRestaurantNameCompose);
        tvRestaurantName.setText(restaurantName);
        tvTags = (TextView) view.findViewById(R.id.tvComposeTags);
        etTags = (EditText) view.findViewById(R.id.etComposeTags);
        etReview = (EditText) view.findViewById(R.id.etComposeReview);
        rbRating = (RatingBar) view.findViewById(R.id.rbComposeReviewRatings);

        btnTaste = (Button) view.findViewById(R.id.btnComposeReview);
        btnTaste.setBackgroundColor(Color.argb(255, 0, 185, 255));
        btnTaste.setTextColor(Color.argb(255, 255, 255, 255));
        btnTaste.setGravity(android.view.Gravity.CENTER);
        btnCancel = (ImageButton) view.findViewById(R.id.btnComposeBack);


        etWords = (TextView) view.findViewById(R.id.etWords);

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

		etReview.addTextChangedListener(txwatcher);

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
    			ViewGroup.LayoutParams.MATCH_PARENT);
    }
}
