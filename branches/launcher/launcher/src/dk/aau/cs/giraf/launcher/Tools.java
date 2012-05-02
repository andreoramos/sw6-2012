package dk.aau.cs.giraf.launcher;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dk.aau.cs.giraf.oasis.lib.Helper;
import dk.aau.cs.giraf.oasis.lib.models.App;
import dk.aau.cs.giraf.oasis.lib.models.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * Class for holding static methods and fields, to minimize code duplication.
 */
public class Tools {


	/**
	 * Saves data for the currently authorized log in.
	 * @param context Context of the current activity.
	 * @param id ID of the guardian logging in.
	 */
	public static void saveLogInData(Context context, Long id) {
		SharedPreferences sp = context.getSharedPreferences(Data.TIMERKEY, 0);
		SharedPreferences.Editor editor = sp.edit();
		Date d = new Date();

		editor.putLong(Data.DATEKEY, d.getTime());
		editor.putLong(Data.GUARDIANID, id);

		editor.commit();
	}

	/**
	 * Finds the currently logged in user.
	 * @param context Context of the current activity.
	 * @return Currently logged in user.
	 */
	public static Profile findCurrentUser(Context context) {
		Helper helper = new Helper(context);

		long currentUserID = findCurrentUserID(context);
		
		if (currentUserID == -1) {
			return null;
		} else {
			return helper.profilesHelper.getProfileById(currentUserID);
		}
	}

	/**
	 * Finds the ID of the currently logged in user.
	 * @param context Context of the current activity.
	 * @return ID of the currently logged in user.
	 */
	public static long findCurrentUserID(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(Data.TIMERKEY, 0);
		return sharedPreferences.getLong(Data.GUARDIANID, -1);
	}

	/**
	 Logs the current guardian out and launches the authentication activity.
	 * @param context Context of the current activity.
	 * @return The intent required to launch authentication.
	 */
	public static Intent logOutIntent(Context context) {
		clearAuthData(context);

		return new Intent(context, AuthenticationActivity.class);
	}

	/**
	 * Clears the current data on who is logged in and when they logged in.
	 * @param context Context of the current activity.
	 */
	public static void clearAuthData(Context context) {
		SharedPreferences sp = context.getSharedPreferences(Data.TIMERKEY, 0);
		SharedPreferences.Editor editor = sp.edit();

		editor.putLong(Data.DATEKEY, 1);
		editor.putLong(Data.GUARDIANID, -1);

		editor.commit();
	}

	/**
	 * Checks whether the current user session has expired.
	 * @param context Context of the current activity.
	 * @return True if a log in is required; otherwise false.
	 */
	public static boolean sessionExpired(Context context) {
		SharedPreferences sp = context.getSharedPreferences(Data.TIMERKEY, 0);
		Long lastAuthTime = sp.getLong(Data.DATEKEY, 1);
		Date d = new Date();

		return d.getTime() > lastAuthTime + Data.TIME_TO_STAY_LOGGED_IN;
	}

	/**
	 * Converts integer to density pixels (dp)
	 * @param context Context of the current activity
	 * @param i The integer which should be used for conversion
	 * @return i converted to density pixels (dp)
	 */
	public static int intToDP(Context context, int i) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, context.getResources().getDisplayMetrics());
	}

	/**
	 * Returns true if the device currently is held in landscape orientation by the user.
	 * @param context Context of the current activity.
	 * @return true if the device currently is held in landscape orientation by the user.
	 */
	public static boolean isLandscape(Context context) {
		int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
		if ((rotation % 2) == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gets the GIRAF apps that are usable by the given user, relative to their settings and the system they're logged in on.
	 * @param context Context of the current activity.
	 * @param currentUser The user to find apps for.
	 * @return List of apps that are usable by this user on this device.
	 */
	public static List<App> getVisibleGirafApps(Context context, Profile user) {
		Helper helper = new Helper(context);

		List<App> userApps = helper.appsHelper.getAppsByProfile(user);
		List<App> deviceApps = getAvailableGirafApps(context);

		if (userApps.isEmpty() || deviceApps.isEmpty()) {
			return new ArrayList<App>();
		}

		// Remove all apps from user's list of apps that are not installed on the device.
		for (int i = 0; i < userApps.size(); i++) {
			if (!appsContain_A(deviceApps, userApps.get(i))) {
				userApps.remove(i);
				i--;
			}
		}

		return userApps;
	}
	
	/**
	 * Gets the Android apps that are usable by the given user, relative to their settings and the system they're logged in on.
	 * @param context Context of the current activity.
	 * @param currentUser The user to find apps for.
	 * @return List of apps that are usable by this user on this device.
	 */
	public static List<App> getVisibleAndroidApps(Context context, Profile user) {
		Helper helper = new Helper(context);

		List<App> userApps = helper.appsHelper.getAppsByProfile(user);
		List<App> deviceApps = getAvailableAndroidApps(context);

		if (userApps.isEmpty() || deviceApps.isEmpty()) {
			return new ArrayList<App>();
		}

		// Remove all apps from user's list of apps that are not installed on the device.
		for (int i = 0; i < userApps.size(); i++) {
			if (!appsContain_A(deviceApps, userApps.get(i))) {
				userApps.remove(i);
				i--;
			}
		}

		return userApps;
	}
	
	/**
	 * Gets the apps that are usable by the given user, relative to their settings and the system they're logged in on.
	 * @param context Context of the current activity.
	 * @param currentUser The user to find apps for.
	 * @return List of apps that are usable by this user on this device.
	 */
	public static List<App> getVisibleApps(Context context, Profile user) {
		Helper helper = new Helper(context);

		List<App> userApps = helper.appsHelper.getAppsByProfile(user);
		List<App> deviceApps = getAvailableApps(context);

		if (userApps.isEmpty() || deviceApps.isEmpty()) {
			return new ArrayList<App>();
		}

		userApps = subtractAppLists(userApps, deviceApps);

		return userApps;
	}
	
	
	/**
	 * Subtracts the apps lists: a, b.
	 * @param List<App> a, the list to be substracted from. 
	 * @param List<App> b, the list to subtract.
	 * @return The substract of a - b.
	 */
	private static List<App> subtractAppLists(List<App> a, List<App> b){
		
		for (int i = 0; i < a.size(); i++) {
			if (!appsContain_A(b, a.get(i))) {
				a.remove(i);
				i--;
			}
		}
		
		return a;
	}

	/**
	 * Finds all GIRAF apps not attached to a given profile.
	 * @param context Context of the current activity.
	 * @param currentUser Profile to find apps for.
	 * @return List of apps not attached to the given profile.
	 */
	public static List<App> getHiddenGirafApps(Context context, Profile user) {
		Helper helper = new Helper(context);

		List<App> userApps = helper.appsHelper.getAppsByProfile(user);
		List<App> deviceApps = getAvailableGirafApps(context);

		if (userApps.isEmpty() || deviceApps.isEmpty()) {
			return new ArrayList<App>();
		}

		deviceApps = subtractAppLists(deviceApps, userApps);

		return deviceApps;
	}
	
	/**
	 * Finds all Android apps not attached to a given profile.
	 * @param context Context of the current activity.
	 * @param currentUser Profile to find apps for.
	 * @return List of apps not attached to the given profile.
	 */
	public static List<App> getHiddenAndroidApps(Context context, Profile user) {
		Helper helper = new Helper(context);

		List<App> userApps = helper.appsHelper.getAppsByProfile(user);
		List<App> deviceApps = getAvailableAndroidApps(context);

		if (userApps.isEmpty() || deviceApps.isEmpty()) {
			return new ArrayList<App>();
		}

		deviceApps = subtractAppLists(deviceApps, userApps);

		return deviceApps;
	}
	
	/**
	 * Finds all apps not attached to a given profile.
	 * @param context Context of the current activity.
	 * @param currentUser Profile to find apps for.
	 * @return List of apps not attached to the given profile.
	 */
	public static List<App> getHiddenApps(Context context, Profile user) {
		Helper helper = new Helper(context);

		List<App> userApps = helper.appsHelper.getAppsByProfile(user);
		List<App> deviceApps = getAvailableApps(context);

		if (userApps.isEmpty() || deviceApps.isEmpty()) {
			return new ArrayList<App>();
		}

		deviceApps = subtractAppLists(deviceApps, userApps);

		return deviceApps;
	}
	
	/**
	 * Finds all GIRAF apps installed on the device that are also registered in the database.
	 * @param context Context of the current activity.
	 * @return List of apps available for use on the device.
	 */
	private static List<App> getAvailableGirafApps(Context context) {
		Helper helper = new Helper(context);

		List<App> dbApps = helper.appsHelper.getApps();
		List<ResolveInfo> deviceApps = getDeviceGirafApps(context);

		if (dbApps.isEmpty() || deviceApps.isEmpty()) {
			return new ArrayList<App>();
		}

		for (int i = 0; i < dbApps.size(); i++) {
			if (!appsContain_RI(deviceApps, dbApps.get(i))) {
				dbApps.remove(i);
				i--;
			}
		}

		return dbApps;
	}
	
	/**
	 * Finds all Android apps (not GIRAF apps) installed on the device that are also registered in the database.
	 * @param context Context of the current activity.
	 * @return List of Android apps available for use on the device.
	 */
	private static List<App> getAvailableAndroidApps(Context context) {
		Helper helper = new Helper(context);

		List<App> dbApps = helper.appsHelper.getApps();
		List<ResolveInfo> deviceApps = getDeviceAndroidApps(context);

		if (dbApps.isEmpty() || deviceApps.isEmpty()) {
			return new ArrayList<App>();
		}

		for (int i = 0; i < dbApps.size(); i++) {
			if (!appsContain_RI(deviceApps, dbApps.get(i))) {
				dbApps.remove(i);
				i--;
			}
		}

		return dbApps;
	}
	
	/**
	 * Finds all apps installed on the device that are also registered in the database.
	 * @param context Context of the current activity.
	 * @return List of apps available for use on the device.
	 */
	private static List<App> getAvailableApps(Context context) {
		Helper helper = new Helper(context);

		List<App> dbApps = helper.appsHelper.getApps();
		List<ResolveInfo> deviceApps = getDeviceApps(context);

		if (dbApps.isEmpty() || deviceApps.isEmpty()) {
			return new ArrayList<App>();
		}

		for (int i = 0; i < dbApps.size(); i++) {
			if (!appsContain_RI(deviceApps, dbApps.get(i))) {
				dbApps.remove(i);
				i--;
			}
		}

		return dbApps;
	}

	/**
	 * Finds all GIRAF apps (as ResolveInfos) installed on the device.
	 * @param context Context of the current activity.
	 * @return List of GIRAF apps.
	 */
	public static List<ResolveInfo> getDeviceGirafApps(Context context) {
		List<ResolveInfo> systemApps = getDeviceApps(context);

		if (systemApps.isEmpty()) {
			return systemApps;
		}

		// Remove all non-GIRAF apps from the list of apps in the system.
		for (int i = 0; i < systemApps.size(); i++) {
			if (!systemApps.get(i).toString().toLowerCase().contains("dk.aau.cs.giraf")) {
				systemApps.remove(i); 
				i--;
			}
		}

		return systemApps;
	}

	/**
	 * Finds all Android apps installed on the device that are not GIRAF apps.
	 * @param context Context of the current activity.
	 * @return List of Android apps.
	 */
	public static List<ResolveInfo> getDeviceAndroidApps(Context context) {
		List<ResolveInfo> systemApps = getDeviceApps(context);

		if (systemApps.isEmpty()) {
			return systemApps;
		}

		// Remove all GIRAF apps from the list of apps in the system.
		for (int i = 0; i < systemApps.size(); i++) {
			if (systemApps.get(i).toString().toLowerCase().contains("dk.aau.cs.giraf") && 
					!systemApps.get(i).toString().toLowerCase().contains("launcher")) {
				systemApps.remove(i); 
				i--;
			}
		}

		return systemApps;
	}

	/**
	 * Finds all apps installed on the device.
	 * @param context Context of the current activity.
	 * @return List of apps.
	 */
	public static List<ResolveInfo> getDeviceApps(Context context) {
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		return context.getPackageManager().queryIntentActivities(mainIntent, 0);
	}
	
	/**
	 * Updates the database with all installed GIRAF apps on the device, but does not attach them to any profile.
	 * Is run every time the launcher starts, so any GIRAF app on the device will be in the database.
	 * @param context Context of the current activity.
	 */
	public static void updateGirafApps_DB(Context context) {
		Helper helper = new Helper(context);

		// Insert the launcher into the DB.
		if (!packageRegistered(helper, context.getPackageName())) {
			Intent launcherIntent = new Intent(Intent.ACTION_MAIN, null);
			launcherIntent.addCategory(Intent.CATEGORY_HOME);

			List<ResolveInfo> launchers = context.getPackageManager().queryIntentActivities(launcherIntent, 0);

			for (ResolveInfo launcher : launchers) {
				if (launcher.activityInfo.packageName.equals(context.getPackageName())) {
					insertAppInDB(context, launcher);
					break;
				}
			}
		}

		List<ResolveInfo> systemApps = getDeviceGirafApps(context);

		for (ResolveInfo installedApp : systemApps) {
			if (!appRegistered(helper, installedApp)) {
				insertAppInDB(context, installedApp);
			}
		}
	}
	
	/**
	 * Inserts all installed Android apps (not GIRAF apps) on the device in the database, but does not attach them to any profile.
	 * @param context Context of the current activity.
	 */
	public static void updateAndroidApps_DB(Context context) {
		Helper helper = new Helper(context);

		List<ResolveInfo> systemApps = getDeviceAndroidApps(context);

		for (ResolveInfo installedApp : systemApps) {
			if (!appRegistered(helper, installedApp)) {
				insertAppInDB(context, installedApp);
			}
		}
	}
	
	/**
	 * Finds all apps installed on the device.
	 * @param context Context of the current activity.
	 * @return List of apps.
	 */
	public static void attachLauncher(Context context) {
		Helper helper = new Helper(context);
		Profile currentUser = Tools.findCurrentUser(context);
		
		if (currentUser != null) {
			helper.appsHelper.attachAppToProfile(helper.appsHelper.getAppByPackageName(), currentUser);
		} else {
			Log.e(Data.ERRORTAG, "Could not get current user!");
		}
	}
	
	/**
	 * Attaches all GIRAF apps currently available on a device to a given user.
	 * @param context Context of the current activity.
	 * @param currentUser The user to attach apps to.
	 */
	public static void attachAvailableGirafAppsToUser(Context context) {
		Helper helper = new Helper(context);
		Profile currentUser = findCurrentUser(context);

		List<App> deviceApps = getAvailableGirafApps(context);
		
		for (App app : deviceApps) {
			helper.appsHelper.attachAppToProfile(app, currentUser);
		}
	}
	
	/**
	 * Attaches all Android apps currently available on a device to a given user.
	 * @param context Context of the current activity.
	 * @param currentUser The user to attach apps to.
	 */
	public static void attachAvailableAndroidAppsToUser(Context context) {
		Helper helper = new Helper(context);
		Profile currentUser = findCurrentUser(context);

		List<App> deviceApps = getAvailableAndroidApps(context);
		
		for (App app : deviceApps) {
			helper.appsHelper.attachAppToProfile(app, currentUser);
		}
	}
	
	/**
	 * Attaches all apps currently available on a device to a given user.
	 * @param context Context of the current activity.
	 * @param currentUser The user to attach apps to.
	 */
	public static void attachAvailableAppsToUser(Context context) {
		Helper helper = new Helper(context);
		Profile currentUser = findCurrentUser(context);

		List<App> deviceApps = getAvailableApps(context);
		
		for (App app : deviceApps) {
			helper.appsHelper.attachAppToProfile(app, currentUser);
		}
	}

	/**
	 * Transforms a ResolveInfo into an App and inserts it into the database.
	 * @param context Context of the current activity.
	 * @param info The ResolveInfo to insert.
	 */
	public static void insertAppInDB(Context context, ResolveInfo info) {
		Helper helper = new Helper(context);

		App app = new App(info.loadLabel(context.getPackageManager()).toString(), info.activityInfo.packageName, info.activityInfo.name);
		app.setId(helper.appsHelper.insertApp(app));
	}

	/**
	 * Checks whether a list of GIRAF apps installed on the system contains a specified app.
	 * @param systemApps List of apps (as ResolveInfos) to check.
	 * @param app The app to check for.
	 * @return True if the app is contained in the list; otherwise false.
	 */
	public static boolean appsContain_RI(List<ResolveInfo> systemApps, App app) {
		return appsContain_RI(systemApps, app.getaPackage());
	}

	/**
	 * Checks whether a list of GIRAF apps installed on the system contains a specified app.
	 * @param systemApps List of apps (as ResolveInfos) to check.
	 * @param app The app to check for.
	 * @return True if the app is contained in the list; otherwise false.
	 */
	public static boolean appsContain_RI(List<ResolveInfo> systemApps, ResolveInfo app) {
		return appsContain_RI(systemApps, app.activityInfo.packageName);
	}

	/**
	 * Checks whether a list of GIRAF apps installed on the system contains a specified app.
	 * @param systemApps List of apps (as ResolveInfos) to check.
	 * @param packageName Package name of the app to check for.
	 * @return True if the app is contained in the list; otherwise false.
	 */
	public static boolean appsContain_RI(List<ResolveInfo> systemApps, String packageName) {
		for (ResolveInfo app : systemApps) {
			if (app.activityInfo.packageName.equals(packageName)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Checks whether a list of GIRAF apps installed on the system contains a specified app.
	 * @param systemApps List of apps (as Apps) to check.
	 * @param app The app to check for.
	 * @return True if the app is contained in the list; otherwise false.
	 */
	public static boolean appsContain_A(List<App> systemApps, App app) {
		return appsContain_A(systemApps, app.getaPackage());
	}

	/**
	 * Checks whether a list of GIRAF apps installed on the system contains a specified app.
	 * @param systemApps List of apps (as Apps) to check.
	 * @param app The app to check for.
	 * @return True if the app is contained in the list; otherwise false.
	 */
	public static boolean appsContain_A(List<App> systemApps, ResolveInfo app) {
		return appsContain_A(systemApps, app.activityInfo.packageName);
	}

	/**
	 * Checks whether a list of GIRAF apps installed on the system contains a specified app.
	 * @param systemApps List of apps (as Apps) to check.
	 * @param packageName Package name of the app to check for.
	 * @return True if the app is contained in the list; otherwise false.
	 */
	public static boolean appsContain_A(List<App> systemApps, String packageName) {
		for (App app : systemApps) {
			if (app.getaPackage().equals(packageName)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Checks whether a given GIRAF package is registered in the database.
	 * @param helper Database-helper to use.
	 * @param packageName Name of the package to check for.
	 * @return True if the package exists in the database already; otherwise false.
	 */
	public static boolean packageRegistered(Helper helper, String packageName) {
		try {
			for (App app : helper.appsHelper.getApps()) {
				if (app.getaPackage().equals(packageName)) {
					return true;
				}
			}
		} catch (NullPointerException e) {}

		return false;
	}

	/**
	 * Checks whether a given GIRAF app is registered in the database.
	 * @param helper Database-helper to use.
	 * @param app App to check for.
	 * @return True if the app exists in the database already; otherwise false.
	 */
	public static boolean appRegistered(Helper helper, ResolveInfo app) {
		return packageRegistered(helper, app.activityInfo.packageName);
	}

	/**
	 * Checks whether a given GIRAF app is registered in the database.
	 * @param helper Database-helper to use.
	 * @param app App to check for.
	 * @return True if the app exists in the database already; otherwise false.
	 */
	public static boolean appRegistered(Helper helper, App app) {
		return packageRegistered(helper, app.getaPackage());
	}
}
