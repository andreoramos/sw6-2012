package dk.aau.cs.giraf.launcher;

import java.util.List;

import dk.aau.cs.giraf.oasis.lib.models.App;
import dk.aau.cs.giraf.oasis.lib.models.Profile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;

class AppInfo extends App {

	/**
	 * The intent used to start the application.
	 */
	Intent mIntent;

	/**
	 * The application icon.
	 */
	private Drawable mIcon;

	/**
	 * 
	 * @return The icon for the app.
	 */
	public Drawable getIconImage() {
		return this.mIcon;
	}

	/**
	 * ID of the guardian who is using the launcher.
	 */
	private Profile mGuardian;
	
	/**
	 * Creates a new AppInfo from a given parent app.
	 * @param parent App to get data from.
	 */
	public AppInfo(App parent) {
		super.id = parent.getId();
		super.activity = parent.getActivity();
		super.aPackage = parent.getaPackage();
		super.icon = parent.getIcon();
		super.name = parent.getName();
		super.settings = parent.getSettings();
	}

	/**
	 * Set ID for the current guardian using the system.
	 * @param guardian The guardian using the system.
	 */
	public void setGuardian(Profile guardian) {
		if (guardian.getPRole() == Profile.pRoles.GUARDIAN.ordinal()) {
			mGuardian = guardian;
		}
	}
	
	/**
	 * The application icon background color.
	 */
	private int mBgColor;
	
	/**
	 * Set the backgroud color
	 * @param color
	 */
	public void setBgColor(int color) {
		this.mBgColor = color;
	}

	/**
	 * 
	 * @return The background color of the app.
	 */
	public int getBgColor() {
		return this.mBgColor;
	}

	/**
	 * 
	 * @return ID of the guardian logged into the system.
	 */
	public Long getGuardianID() {
		return mGuardian.getId();
	}

	/**
	 * Getter for the title of the app. 
	 * Cuts the name off, to make sure it's not too long to show in the launcher.
	 * @return shortened name for the app
	 */
	public String getShortenedName() {
		String title = super.getName();

		if(title.length() > 6){
			return title.subSequence(0, 5) + "...";
		} else {
			return title;
		}
	}

	/**
	 * Loads information needed by the app.
	 * @param context Context of the current activity.
	 */
	public void load(Context context, Profile guardian) {
		setGuardian(guardian);
		
		loadIcon(context);
	}

	/**
	 * Finds the icon of the app.
	 * @param context Context of the current activity.
	 */
	private void loadIcon(Context context) {
		// Is supposed to allow for custom icons, but does not currently support this.

		List<ResolveInfo> systemApps = Tools.getDeviceApps(context);

		for (ResolveInfo app : systemApps) {
			if (app.activityInfo.packageName.equals(this.aPackage)) {
				mIcon = app.loadIcon(context.getPackageManager());
				break;
			}
		}
	}

	
}
