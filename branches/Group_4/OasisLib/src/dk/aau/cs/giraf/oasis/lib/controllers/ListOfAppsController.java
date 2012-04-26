package dk.aau.cs.giraf.oasis.lib.controllers;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import dk.aau.cs.giraf.oasis.lib.metadata.ListOfAppsMetaData;
import dk.aau.cs.giraf.oasis.lib.models.App;
import dk.aau.cs.giraf.oasis.lib.models.ListOfApps;
import dk.aau.cs.giraf.oasis.lib.models.Profile;
import dk.aau.cs.giraf.oasis.lib.models.Setting;
import dk.aau.cs.giraf.oasis.lib.models.Stat;

/**
 * List of apps controller
 * @author Admin
 *
 */
class ListOfAppsController {

	private Context _context;
	private String[] columns = { 
			ListOfAppsMetaData.Table.COLUMN_IDAPP,
			ListOfAppsMetaData.Table.COLUMN_IDPROFILE,
			ListOfAppsMetaData.Table.COLUMN_SETTINGS,
			ListOfAppsMetaData.Table.COLUMN_STATS};

	/**
	 * Constructor
	 * @param context
	 */
	public ListOfAppsController(Context context) {
		_context = context;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//METHODS TO CALL
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Clear list of apps table
	 * @return Rows
	 */
	public int clearListOfAppsTable() {
		return _context.getContentResolver().delete(ListOfAppsMetaData.CONTENT_URI, null, null);
	}

	/**
	 * Remove list of apps
	 * @param appId App id
	 * @param profileId Profile Id
	 * @return Rows
	 */
	public int removeListOfApps(long appId, long profileId) {
		return _context.getContentResolver().delete(ListOfAppsMetaData.CONTENT_URI, ListOfAppsMetaData.Table.COLUMN_IDAPP + " = '" + appId + "' AND " + ListOfAppsMetaData.Table.COLUMN_IDPROFILE + " = '" + profileId + "'", null);
	}
	
	/**
	 * Insert list of apps
	 * @param listOfApps List of apps
	 * @return Rows
	 */
	public int insertListOfApps(ListOfApps listOfApps) {
		ContentValues cv = new ContentValues();
		cv.put(ListOfAppsMetaData.Table.COLUMN_IDAPP, listOfApps.getIdApp());
		cv.put(ListOfAppsMetaData.Table.COLUMN_IDPROFILE, listOfApps.getIdProfile());
		cv.put(ListOfAppsMetaData.Table.COLUMN_SETTINGS, Setting.toStringSetting(listOfApps.getSetting()));
		cv.put(ListOfAppsMetaData.Table.COLUMN_STATS, Stat.toStringStat(listOfApps.getStat()));
		Uri uri = _context.getContentResolver().insert(ListOfAppsMetaData.CONTENT_URI, cv);
		return Integer.parseInt(uri.getPathSegments().get(1));
	}

	/**
	 * Modify list of apps
	 * @param listOfApps List of apps
	 * @return Rows
	 */
	public int modifyListOfApps(ListOfApps listOfApps) {
		ContentValues cv = new ContentValues();
		cv.put(ListOfAppsMetaData.Table.COLUMN_IDAPP, listOfApps.getIdApp());
		cv.put(ListOfAppsMetaData.Table.COLUMN_IDPROFILE, listOfApps.getIdProfile());
		cv.put(ListOfAppsMetaData.Table.COLUMN_SETTINGS, Setting.toStringSetting(listOfApps.getSetting()));
		cv.put(ListOfAppsMetaData.Table.COLUMN_STATS, Stat.toStringStat(listOfApps.getStat()));
		return _context.getContentResolver().update(ListOfAppsMetaData.CONTENT_URI, cv, ListOfAppsMetaData.Table.COLUMN_IDAPP + " = '" + listOfApps.getIdApp() + "'" + " AND " + ListOfAppsMetaData.Table.COLUMN_IDPROFILE + " = '" + listOfApps.getIdProfile() + "'", null);
	}
	
	/**
	 * Get list of apps
	 * @return List of list of apps
	 */
	public List<ListOfApps> getListOfApps() {
		List<ListOfApps> listOfApps = new ArrayList<ListOfApps>();
		Cursor c = _context.getContentResolver().query(ListOfAppsMetaData.CONTENT_URI, columns, null, null, null);
		
		if (c != null) {
			listOfApps = cursorToListOfApps(c);
			c.close();
		}
		
		return listOfApps;
	}

	/**
	 * Get list of apps by ids
	 * @param appId App id
	 * @param profileId Profile id
	 * @return List of apps
	 */
	public ListOfApps getListOfAppByIds(long appId, long profileId) {
		ListOfApps listOfApp = null;
		
		Cursor c = _context.getContentResolver().query(ListOfAppsMetaData.CONTENT_URI, columns, ListOfAppsMetaData.Table.COLUMN_IDAPP + " = '" + appId + "' AND " + ListOfAppsMetaData.Table.COLUMN_IDPROFILE + " = '" + profileId + "'", null, null);
		if (c != null) {
			if (c.moveToFirst()) {
				listOfApp = cursorToListOfApp(c);
			}
			c.close();
		}
		
		return listOfApp;
	}
	
	/**
	 * Get list of apps by profile id
	 * @param profileId Profile id
	 * @return List of list of apps
	 */
	public List<ListOfApps> getListOfAppsByProfileId(long profileId) {
		List<ListOfApps> listOfApps = new ArrayList<ListOfApps>();
		
		Cursor c = _context.getContentResolver().query(ListOfAppsMetaData.CONTENT_URI, columns, ListOfAppsMetaData.Table.COLUMN_IDPROFILE + " = '" + profileId + "'", null, null);
		if (c != null) {
			if (c.moveToFirst()) {
				listOfApps = cursorToListOfApps(c);
			}
			c.close();
		}
		
		return listOfApps;
	}
	
	/**
	 * Get setting by app and child id
	 * @param app App
	 * @param child profile
	 * @return Setting
	 */
	public Setting<String, String, String> getSettingByAppIdAndByChildId(App app, Profile child) {
		Setting<String, String, String> setting = null;
		
		Cursor c = _context.getContentResolver().query(ListOfAppsMetaData.CONTENT_URI, columns, ListOfAppsMetaData.Table.COLUMN_IDAPP + " = '" + app.getId() + "'" + ListOfAppsMetaData.Table.COLUMN_IDPROFILE + " = '" + child.getId() + "'", null, null);
		if (c != null) {
			if (c.moveToFirst()) {
				setting = Setting.toSetting(c.getString(c.getColumnIndex(ListOfAppsMetaData.Table.COLUMN_SETTINGS)));
			}
			c.close();
		}

		return setting;
	}

	/**
	 * Get stat by app id and by child id
	 * @param app app
	 * @param child profile
	 * @return stat
	 */
	public Stat<String, String, String> getStatByAppIdAndByChildId(App app, Profile child) {
		Stat<String, String, String> stat = null;
		
		Cursor c = _context.getContentResolver().query(ListOfAppsMetaData.CONTENT_URI, columns, ListOfAppsMetaData.Table.COLUMN_IDAPP + " = '" + app.getId() + "'" + ListOfAppsMetaData.Table.COLUMN_IDPROFILE + " = '" + child.getId() + "'", null, null);
		if (c != null) {
			if (c.moveToFirst()) {
				stat = Stat.toStat(c.getString(c.getColumnIndex(ListOfAppsMetaData.Table.COLUMN_SETTINGS)));
			}
			c.close();
		}

		return stat;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//PRIVATE METHODS - Keep out!
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Cursor to list of apps
	 * @param cursor Cursor
	 * @return List of list of apps
	 */
	private List<ListOfApps> cursorToListOfApps(Cursor cursor) {
		List<ListOfApps> listOfApps = new ArrayList<ListOfApps>();

		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				listOfApps.add(cursorToListOfApp(cursor));
				cursor.moveToNext();
			}
		}

		return listOfApps;
	}

	/**
	 * Cursor to list of apps
	 * @param cursor Cursor
	 * @return List of apps
	 */
	private ListOfApps cursorToListOfApp(Cursor cursor) {
		String temp;
		ListOfApps listOfApp = new ListOfApps();
		listOfApp.setIdApp(cursor.getLong(cursor.getColumnIndex(ListOfAppsMetaData.Table.COLUMN_IDAPP)));
		listOfApp.setIdProfile(cursor.getLong(cursor.getColumnIndex(ListOfAppsMetaData.Table.COLUMN_IDPROFILE)));
		
		temp = cursor.getString(cursor.getColumnIndex(ListOfAppsMetaData.Table.COLUMN_SETTINGS));
		if (temp != null)
			listOfApp.setSetting(Setting.toSetting(temp));
		
		temp = cursor.getString(cursor.getColumnIndex(ListOfAppsMetaData.Table.COLUMN_STATS));
		if (temp != null)
			listOfApp.setStat(Stat.toStat(temp));
		
		return listOfApp;
	}
}
