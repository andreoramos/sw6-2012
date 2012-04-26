package dk.aau.cs.giraf.launcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dk.aau.cs.giraf.gui.GColorAdapter;
import dk.aau.cs.giraf.gui.GWidgetCalendar;
import dk.aau.cs.giraf.gui.GWidgetConnectivity;
import dk.aau.cs.giraf.gui.GWidgetLogout;
import dk.aau.cs.giraf.gui.GWidgetUpdater;
import dk.aau.cs.giraf.gui.GDialog;
import dk.aau.cs.giraf.oasis.lib.Helper;
import dk.aau.cs.giraf.oasis.lib.models.App;
import dk.aau.cs.giraf.oasis.lib.models.Profile;
import dk.aau.cs.giraf.oasis.lib.models.Setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.view.WindowManager;


public class HomeActivity extends Activity {

	private static Context mContext;
	private GridView mGrid;
	private Profile mCurrentUser; 
	private Setting<String,String,String> mSettings;
	private Helper mHelper;
	private TextView mNameView;
	private LinearLayout mPictureLayout;
	private ImageView mProfilePictureView;
	private RelativeLayout mHomeBarLayout;
	private int mProfilePictureWidthLandscape;
	private int mProfilePictureHeightLandscape;
	private int mProfilePictureWidthPortrait;
	private int mProfilePictureHeightPortrait;
	private GWidgetUpdater mWidgetTimer;
	private GWidgetCalendar mCalendarWidget;
	private GWidgetConnectivity mConnectivityWidget;
	private GWidgetLogout mLogoutWidget;
	private RelativeLayout.LayoutParams mHomeBarParams;
	private RelativeLayout mHomeDrawer;
	private final int DRAWER_WIDTH = 400;
	
	private int mLandscapeBarWidth;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		mLandscapeBarWidth = Tools.intToDP(this, 200);

		HomeActivity.mContext = this; //getApplicationContext();
		mHelper = new Helper(mContext);

		mCurrentUser = mHelper.profilesHelper.getProfileById(getIntent().getExtras().getLong("currentGuardianID"));

		mNameView = (TextView)this.findViewById(R.id.nameView);
		mNameView.setText(mCurrentUser.getFirstname() + " " + mCurrentUser.getSurname());
		
		mPictureLayout = (LinearLayout)this.findViewById(R.id.profile_pic);
		mProfilePictureView = (ImageView)this.findViewById(R.id.imageview_profilepic);
		mHomeBarLayout = (RelativeLayout)this.findViewById(R.id.HomeBarLayout);
		
		mProfilePictureWidthLandscape = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
		mProfilePictureHeightLandscape = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
		mProfilePictureWidthPortrait = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
		mProfilePictureHeightPortrait = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
		
		mCalendarWidget = (GWidgetCalendar) findViewById(R.id.calendarwidget);
		mConnectivityWidget = (GWidgetConnectivity) findViewById(R.id.connectivitywidget);
		mLogoutWidget = (GWidgetLogout) findViewById(R.id.logoutwidget);
		
		mWidgetTimer = new GWidgetUpdater();
		mWidgetTimer.addWidget(mCalendarWidget);
		mWidgetTimer.addWidget(mConnectivityWidget);
		
		mHomeDrawer = (RelativeLayout) findViewById(R.id.HomeDrawer);
		
		
		mLogoutWidget.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//startActivity(Tools.logOutIntent(mContext));
				View.OnClickListener task = new View.OnClickListener() {
					public void onClick(View v) {
						startActivity(Tools.logOutIntent(mContext));;	
					}
				};
				GDialog g = new GDialog(mContext, R.drawable.large_switch_profile, "Log out", "You're about to log out", task);
				g.show();
			}
		});
		
		View main = findViewById(R.id.HomeWrapperLayout);
		
		main.layout(-600, 0, main.getWidth(), main.getHeight());
		
		findViewById(R.id.HomeBarLayout).setOnTouchListener(new View.OnTouchListener() {
			int offset = 0;
			@Override
			public boolean onTouch(View v, MotionEvent e) {
				int margin = 0;
				
				boolean result = true;
				
				switch(e.getActionMasked()){
				case MotionEvent.ACTION_DOWN:
					offset = (int) e.getX();
					Log.i("thomas", "START DRAG");
					result = true;
					break;
				case MotionEvent.ACTION_MOVE:
					
					mHomeBarParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
					
					margin = mHomeBarParams.leftMargin + ((int) e.getX() - offset);
					
					if(margin < 0){
						margin = 0;
					} else if(margin > DRAWER_WIDTH){
						margin = DRAWER_WIDTH;
					}
					
					mHomeBarParams.setMargins(margin, 0, 0, 0);
					v.setLayoutParams(mHomeBarParams);
					Log.i("thomas", "MARGIN: "+margin+"");
					
					View v2 = findViewById(R.id.HomeDrawer);
					RelativeLayout.LayoutParams v2Params = (RelativeLayout.LayoutParams) v2.getLayoutParams();
					v2Params.setMargins((margin-800), 0, 0, 0);
					v2.setLayoutParams(v2Params);
					
					
					result = true;
					break;
				case MotionEvent.ACTION_UP:
					Log.i("thomas", "STOP DRAG");
					result = false;
					break;
					
				}
				
				
				
				
				//dwfegw
//				RelativeLayout GridViewWrapper = (RelativeLayout) findViewById(R.id.GridViewWrapper);
//				RelativeLayout.LayoutParams GridViewParams = (RelativeLayout.LayoutParams) GridViewWrapper.getLayoutParams();
//				GridViewParams.setMargins(margin + mLandscapeBarWidth, 0, 0, 0);
//				GridViewWrapper.setLayoutParams(GridViewParams);
				//fewfew
				
				return result;
			}
		});
		
		
		GridView AppColors = (GridView) findViewById(R.id.appcolors);
		
		AppColors.setAdapter(new GColorAdapter(this));
		
		
		
		loadApplications();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		this.resizeBar();
	}
	
	@Override
    protected void onPause()
    {
        super.onPause();
        mWidgetTimer.sendEmptyMessage(GWidgetUpdater.MSG_STOP);
    }
	
	@Override
    protected void onResume()
    {
        super.onResume();
        mWidgetTimer.sendEmptyMessage(GWidgetUpdater.MSG_START);
    }

	private void resizeBar() {
		GridView homeGridView = (GridView)this.findViewById(R.id.GridViewHome);
		RelativeLayout homebar = (RelativeLayout)this.findViewById(R.id.HomeBarLayout);

		LayoutParams paramsGrid = (RelativeLayout.LayoutParams)homeGridView.getLayoutParams();
		RelativeLayout.LayoutParams paramsBar = (RelativeLayout.LayoutParams)homebar.getLayoutParams();

		int barHeightLandscape = intToDP(100);
		int barHeightPortrait = intToDP(200);

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int screenWidth = size.x;
		int screenHeight = size.y;

		final boolean isLandscape = isLandscape();

		if (isLandscape) {
			homebar.setBackgroundDrawable(getResources().getDrawable(R.drawable.homebar_back_land));
			//paramsBar.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			paramsBar.height = LayoutParams.MATCH_PARENT;
			paramsBar.width = barHeightLandscape;

			paramsGrid.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			paramsGrid.width = screenWidth - barHeightLandscape;
		} else {
			homebar.setBackgroundDrawable(getResources().getDrawable(R.drawable.homebar_back_port));
			//paramsBar.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			paramsBar.height = barHeightPortrait;
			paramsBar.width = LayoutParams.MATCH_PARENT;

			paramsGrid.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			paramsGrid.height = screenHeight - barHeightPortrait;
		}

		homeGridView.setLayoutParams(paramsGrid);
		homebar.setLayoutParams(paramsBar);
		
		ViewGroup.LayoutParams profilePictureViewParams = mProfilePictureView.getLayoutParams();
		RelativeLayout.LayoutParams connectivityWidgetParams = (LayoutParams) mConnectivityWidget.getLayoutParams();
		RelativeLayout.LayoutParams calendarWidgetParams = (LayoutParams) mCalendarWidget.getLayoutParams();
		RelativeLayout.LayoutParams logoutWidgetParams = (LayoutParams) mLogoutWidget.getLayoutParams();
		
		// remove me later - start
		//mConnectivityWidget.setVisibility(View.VISIBLE);
		//mCalendarWidget.setVisibility(View.VISIBLE);
		// stop
		
		if (isLandscape) {
			mNameView.setVisibility(View.INVISIBLE);

			profilePictureViewParams.width = intToDP(70);
			profilePictureViewParams.height = intToDP(91);
			mHomeBarLayout.setPadding(intToDP(15), intToDP(15), intToDP(15), intToDP(15));
			
			connectivityWidgetParams.setMargins(0, intToDP(106), 0, 0);
			calendarWidgetParams.setMargins(0,intToDP(15), 0,0);
			calendarWidgetParams.addRule(RelativeLayout.BELOW, mConnectivityWidget.getId());
			calendarWidgetParams.addRule(RelativeLayout.LEFT_OF, 0);
			
			logoutWidgetParams.setMargins(0, intToDP(15), 0, 0);
			logoutWidgetParams.addRule(RelativeLayout.BELOW, mCalendarWidget.getId());
			logoutWidgetParams.addRule(RelativeLayout.LEFT_OF, 0);
		} else {
			
			connectivityWidgetParams.setMargins(0, 0, 0, 0);
			calendarWidgetParams.setMargins(0, 0, intToDP(25),0);
			calendarWidgetParams.addRule(RelativeLayout.BELOW, 0);
			calendarWidgetParams.addRule(RelativeLayout.LEFT_OF, mConnectivityWidget.getId());
			
			logoutWidgetParams.setMargins(0, 0, intToDP(25), 0);
			logoutWidgetParams.addRule(RelativeLayout.BELOW, 0);
			logoutWidgetParams.addRule(RelativeLayout.LEFT_OF, mCalendarWidget.getId());
			
			profilePictureViewParams.width = intToDP(100);
			profilePictureViewParams.height = intToDP(130);
			
			mHomeBarLayout.setPadding(intToDP(15), intToDP(15), intToDP(15), intToDP(15));
			mNameView.setVisibility(View.VISIBLE);
		}
		mProfilePictureView.setLayoutParams(profilePictureViewParams);	
		mConnectivityWidget.setLayoutParams(connectivityWidgetParams);
		mCalendarWidget.setLayoutParams(calendarWidgetParams);
		mLogoutWidget.setLayoutParams(logoutWidgetParams);
	}
	
	private int intToDP(int i) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, getResources().getDisplayMetrics());
	}

	private boolean isLandscape() {
		int rotation = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getRotation();
		if ((rotation % 2) == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Loads the user's applications into the grid.
	 */
	private void loadApplications() {		
		List<App> userApps = Tools.getVisibleApps(mContext, mCurrentUser);

		// If a guardian does not have any apps available, give them all on the device:
		if (userApps.size() == 0 && mCurrentUser.getPRole() == Tools.ROLE_GUARDIAN) {
			Tools.attachAllDeviceAppsToUser(mContext, mCurrentUser);
			userApps = Tools.getVisibleApps(mContext, mCurrentUser);
		}

		if (userApps != null) {
			ArrayList<AppInfo> appInfos = new ArrayList<AppInfo>();

			for (App app : userApps) {
				AppInfo appInfo = new AppInfo(app);

				appInfo.load(mContext, mCurrentUser);

				appInfos.add(appInfo);
			}

			mGrid = (GridView)this.findViewById(R.id.GridViewHome);
			mGrid.setAdapter(new AppAdapter(this, appInfos));
			mGrid.setOnItemClickListener(new ProfileLauncher());
		}
	}
}