package com.codepath.apps.tastebuds.activities;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.tastebuds.R;
import com.codepath.apps.tastebuds.fragments.FriendsListFragment;
import com.codepath.apps.tastebuds.fragments.RestaurantListFragment;
import com.codepath.apps.tastebuds.fragments.RestaurantReviewDialog;
import com.codepath.apps.tastebuds.fragments.RestaurantReviewDialog.RestaurantReviewDialogListener;
import com.codepath.apps.tastebuds.listeners.FragmentTabListener;
import com.codepath.apps.tastebuds.models.Restaurant;
import com.codepath.apps.tastebuds.models.RestaurantReview;
import com.facebook.Session;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

public class HomeActivity extends FragmentActivity implements
	GooglePlayServicesClient.ConnectionCallbacks,
	GooglePlayServicesClient.OnConnectionFailedListener {

	private ParseUser user;
	ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
	public Location mCurrentLocation;
	LocationClient mLocationClient;
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		user = ParseUser.getCurrentUser();
		mLocationClient = new LocationClient(this, this, this);
		setContentView(R.layout.activity_home);		
		Log.d("Debug", "Home Activity OnCreate");
		//user = ParseUser.getCurrentUser();
		//setupTabs();

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

    /*
     * Called when the Activity becomes visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mLocationClient.connect();
    }
    /*
     * Called when the Activity is no longer visible.
     */
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }
	
	public void onLogoutAction(MenuItem mi){
		Session session = Session.getActiveSession();
		if (session != null) {
			if (!session.isClosed()) {
				session.closeAndClearTokenInformation();
				//clear your preferences if saved
				startActivity(new Intent(getApplicationContext(), TastebudsLoginActivity.class));
				// make sure the user can not access the page after he/she is logged out
				// clear the activity stack
				finish();
			}
		} else {

			session.closeAndClearTokenInformation();
			//clear your preferences if saved

		}
	}
	
	public void onProfileClick(MenuItem mi){
		Intent i = new Intent(this, UserProfileActivity.class);
		i.putExtra("user_id", ParseUser.getCurrentUser().getString("fbId"));
		startActivity(i);
	}
	private void setupTabs(Location mCurrentLocation) {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.removeAllTabs();
		getSupportFragmentManager().executePendingTransactions();
		Bundle args = new Bundle();
		args.putParcelable("mCurrentLocation", mCurrentLocation);
		
		Tab tab1 = actionBar
				.newTab()
				.setText("Restaurants")
				.setIcon(R.drawable.ic_launcher)
				.setTag("RestaurantListFragment")
				.setTabListener(
						new FragmentTabListener<RestaurantListFragment>(R.id.ctRestaurantsLists, this, "first",
								RestaurantListFragment.class, args));

		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		Tab tab2 = actionBar
				.newTab()
				.setText("Friends")
				.setIcon(R.drawable.ic_launcher)
				.setTag("FriendsListFragment")
				.setTabListener(
						new FragmentTabListener<FriendsListFragment>(R.id.ctRestaurantsLists, this, "second",
								FriendsListFragment.class));

		actionBar.addTab(tab2);
	}

	public void onReview(View view) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		RestaurantReviewDialog dialog = RestaurantReviewDialog.newInstance(
				"Shree Datta", "czswrtv");
		dialog.show(ft, "compose");
		dialog.listener = new RestaurantReviewDialogListener() {
			@Override
			public void onFinishReviewComposeDialog(RestaurantReview review) {
				review.setUser(user);
				review.saveInBackground(new SaveCallback() {
					@Override
					public void done(ParseException arg0) {
						if (arg0 == null) {
							Toast.makeText(HomeActivity.this, "Saved Review", Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(HomeActivity.this, "Saved Review FAiled" + arg0.toString(), Toast.LENGTH_LONG).show();
						}
					}


				});
			}
		};
	}
	
    /*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        Log.d("Debug", "onConnected");
        mCurrentLocation = getLocation(mLocationClient);
      //  mCurrentLocation = mLocationClient.getLastLocation();
       // Log.d("Debug", "Location" + mCurrentLocation.toString());
		setupTabs(mCurrentLocation);

    }
    
    public Location getLocation(LocationClient locationClient){

        if(locationClient.getLastLocation() == null){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getLocation(locationClient);
        }else{
            return locationClient.getLastLocation();
        }
    }
    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
        // Display the connection status
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }
    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            //showErrorDialog(connectionResult.getErrorCode());
        }
    }
}
	

