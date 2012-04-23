package dk.aau.cs.giraf.launcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dk.aau.cs.giraf.gui.GButton;
import dk.aau.cs.giraf.gui.GWidgetCalendar;
import dk.aau.cs.giraf.gui.GWidgetConnectivity;
import dk.aau.cs.giraf.gui.GWidgetUpdater;
import dk.aau.cs.giraf.oasis.lib.Helper;
import dk.aau.cs.giraf.oasis.lib.models.Profile;
import dk.aau.cs.giraf.oasis.lib.models.Setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
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
	private static ArrayList<ApplicationInfo> mApplications;
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
	
	
	private GButton mLogoutButton;
	
	//Hash keys
	public final String BACKGROUNDCOLOR = "backgroundColor";
	public final String APPBACKGROUNDCOLOR = "appBackgroundColor";
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		HomeActivity.mContext = getApplicationContext();
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
		
		mWidgetTimer = new GWidgetUpdater();
		mWidgetTimer.addWidget(mCalendarWidget);
		mWidgetTimer.addWidget(mConnectivityWidget);
		
		// Log ud knap:
		/*mLogoutButton = (GButton) findViewById(R.id.logoutGButton);
		mLogoutButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SharedPreferences sp = getSharedPreferences("TIMING", 0);
				SharedPreferences.Editor editor = sp.edit();
				
				editor.putLong("DATE", 1);
				editor.putLong("currentGuardianID", -1);
				
				editor.commit();
				
				Intent i = new Intent(mContext, AuthenticationActivity.class);
				startActivity(i);
			}
		});*/
		
		loadApplications(true);
		
		
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
		LayoutParams paramsBar = (RelativeLayout.LayoutParams)homebar.getLayoutParams();

		int barHeightLandscape = intToDP(100);
		int barHeightPortrait = intToDP(200);

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int screenWidth = size.x;
		int screenHeight = size.y;

		final boolean isLandscape = isLandscape();

		if (isLandscape) {
			paramsBar.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			paramsBar.height = LayoutParams.MATCH_PARENT;
			paramsBar.width = barHeightLandscape;

			paramsGrid.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			paramsGrid.width = screenWidth - barHeightLandscape;
		} else {
			paramsBar.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			paramsBar.height = barHeightPortrait;
			paramsBar.width = LayoutParams.MATCH_PARENT;

			paramsGrid.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			paramsGrid.height = screenHeight - barHeightPortrait;
		}

		homeGridView.setLayoutParams(paramsGrid);
		homebar.setLayoutParams(paramsBar);
		
		ViewGroup.LayoutParams profilePictureViewParams = mProfilePictureView.getLayoutParams();
		ViewGroup.LayoutParams connectivityWidgetParams = mConnectivityWidget.getLayoutParams();
		if (isLandscape) {
			mNameView.setVisibility(View.INVISIBLE);

			profilePictureViewParams.width = intToDP(70);
			profilePictureViewParams.height = intToDP(91);
			mHomeBarLayout.setPadding(intToDP(15), intToDP(15), intToDP(15), intToDP(15));
			//connectivityWidgetParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		} else {
			profilePictureViewParams.width = intToDP(100);
			profilePictureViewParams.height = intToDP(130);
			
			mHomeBarLayout.setPadding(intToDP(15), intToDP(15), intToDP(15), intToDP(15));
			mNameView.setVisibility(View.VISIBLE);
		}
		mProfilePictureView.setLayoutParams(profilePictureViewParams);	
		mConnectivityWidget.setLayoutParams(connectivityWidgetParams);
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

	private void loadApplications(boolean isLaunching) {
		if (isLaunching && mApplications != null) {
			return;
		}

		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		final List<ResolveInfo> pkgAppsList = mContext.getPackageManager().queryIntentActivities(mainIntent, 0);
		Collections.sort(pkgAppsList, new ResolveInfo.DisplayNameComparator(mContext.getPackageManager()));

		if(pkgAppsList != null){
			ArrayList<ApplicationInfo> applications = new ArrayList<ApplicationInfo>();
			
			int i = 0;
			for(ResolveInfo info : pkgAppsList){
				//Package (dk.aau.cs.giraf)
				if(info.toString().toLowerCase().contains("dk.aau.cs.giraf") && !info.toString().toLowerCase().contains("launcher")) {
					ApplicationInfo appInfo = new ApplicationInfo();

					appInfo.title = info.loadLabel(getPackageManager());
					if(appInfo.title.length() > 6){
						appInfo.title = appInfo.title.subSequence(0, 5) + "...";
					}
					appInfo.icon = info.activityInfo.loadIcon(getPackageManager());
					appInfo.packageName = info.activityInfo.applicationInfo.packageName;
					appInfo.activityName = info.activityInfo.name;
					appInfo.guardian = mCurrentUser.getId();
					
					if(mHelper.appsHelper.getApps().size() > 0) {
						mSettings = mHelper.appsHelper.getAppsByProfile(mCurrentUser).get(i).getSettings();
						appInfo.color = AppColor(i, mSettings);
					} else {
						appInfo.color = TEMPAppColor(i);
					}

					applications.add(appInfo);
					
					//have to change
					i++;
				}
			}
			
			mGrid = (GridView)this.findViewById(R.id.GridViewHome);
			mGrid.setAdapter(new AppAdapter(this,applications));
			mGrid.setOnItemClickListener(new ProfileLauncher());
		}
	}
	
	private int TEMPAppColor(int position) {
		int[] c = getResources().getIntArray(R.array.appcolors);
		return c[position];
	}
	
	private int AppColor(int position, Setting<String,String,String> settings) {
		final String COLOR = "Color";
		final String BACKGROUND = "Background";
		int[] c = getResources().getIntArray(R.array.appcolors);
		
		if(settings.containsKey(COLOR)) {
			int color = Integer.parseInt(settings.get(COLOR).get(BACKGROUND));
			return c[color];
		} else {
			settings.addValue(COLOR, BACKGROUND, String.valueOf(c[position]));
			return c[position];
		}
	}
}