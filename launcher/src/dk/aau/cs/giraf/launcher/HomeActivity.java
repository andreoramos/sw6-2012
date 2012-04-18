package dk.aau.cs.giraf.launcher;

import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.giraf.oasis.lib.Helper;
import dk.aau.cs.giraf.oasis.lib.models.Profile;
import dk.aau.cs.giraf.oasis.lib.models.Setting;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.view.WindowManager;

public class HomeActivity extends Activity {

	private static Context mContext;
	private static ArrayList<ApplicationInfo> mApplications;
	private GridView mGrid;
	private Profile mCurrentUser; 
	private Setting mSettings;

	private Helper mHelper;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		HomeActivity.mContext = getApplicationContext();
		this.mHelper = new Helper(mContext);

		this.mCurrentUser = mHelper.profilesHelper.getProfileById(getIntent().getExtras().getLong("currentGuardianID"));

		loadApplications(true);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		this.resizeBar();
	}

	private void resizeBar() {
		GridView homeGridView = (GridView)this.findViewById(R.id.GridViewHome);
		RelativeLayout homebar = (RelativeLayout)this.findViewById(R.id.HomeBarLayout);

		LayoutParams paramsGrid = (RelativeLayout.LayoutParams)homeGridView.getLayoutParams();
		LayoutParams paramsBar = (RelativeLayout.LayoutParams)homebar.getLayoutParams();

		int barHeightLandscape = 100;
		int barHeightPortrait = 200;

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

		if (isLandscape) {

		} else {
			TextView nameView = new TextView(this);
			TextView institutionView = new TextView(this);

			/// TODO implement mCurrentUser
			//nameView.setText(mCurrentUser.getFirstname() + " " + mCurrentUser.getSurname());
			nameView.setText("Drazenko Banjak");

			/// TODO implement get Department - method should be available 18 april from oasis
			institutionView.setText("Enebakken"); /// TODO change to dynamic value

			homebar.addView(nameView);
			homebar.addView(institutionView);
		}
	}

	private void applySizes() {
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

		if(pkgAppsList != null){
			ArrayList<ApplicationInfo> applications = new ArrayList<ApplicationInfo>();
			for(ResolveInfo info : pkgAppsList){
				//Package (dk.aau.cs.giraf)
				if(info.toString().toLowerCase().contains("dk.aau.cs.giraf") && 
						!info.toString().toLowerCase().contains("launcher")) {
					ApplicationInfo appInfo = new ApplicationInfo();

					appInfo.title = info.loadLabel(getPackageManager());
					appInfo.icon = info.activityInfo.loadIcon(getPackageManager());
					appInfo.packageName = info.activityInfo.applicationInfo.packageName;
					appInfo.activityName = info.activityInfo.name;
					appInfo.guardian = mCurrentUser.getId();
					appInfo.bgcolor = AppColor(mSettings, appInfo.packageName);

					applications.add(appInfo);
				}
			}

			mGrid = (GridView)this.findViewById(R.id.GridViewHome);
			mGrid.setAdapter(new AppAdapter(this,applications));
			mGrid.setOnItemClickListener(new ProfileLauncher());


		}
	}

	private void SetBackgroundColor(Setting setting) {
		
	}
	
	private int AppColor(Setting setting, String appName) {
		return Color.BLUE;
	}
}