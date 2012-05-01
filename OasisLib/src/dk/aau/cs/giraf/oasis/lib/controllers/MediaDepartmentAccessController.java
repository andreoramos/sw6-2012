package dk.aau.cs.giraf.oasis.lib.controllers;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import dk.aau.cs.giraf.oasis.lib.metadata.MediaDepartmentAccessMetaData;
import dk.aau.cs.giraf.oasis.lib.metadata.MediaProfileAccessMetaData;
import dk.aau.cs.giraf.oasis.lib.models.Department;
import dk.aau.cs.giraf.oasis.lib.models.Media;
import dk.aau.cs.giraf.oasis.lib.models.MediaDepartmentAccess;

/**
 * Media department access controller
 * @author Admin
 *
 */
class MediaDepartmentAccessController {

	private Context _context;
	private String[] columns = new String[] { 
			MediaDepartmentAccessMetaData.Table.COLUMN_IDMEDIA, 
			MediaDepartmentAccessMetaData.Table.COLUMN_IDDEPARTMENT};

	/**
	 * Constructor
	 * @param context
	 */
	public MediaDepartmentAccessController(Context context) {
		_context = context;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//METHODS TO CALL
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//	/**
//	 * Clear media department access
//	 * @return Rows
//	 */
//	public int clearMediaDepartmentAccessTable() {
//		return _context.getContentResolver().delete(MediaDepartmentAccessMetaData.CONTENT_URI, null, null);
//	}
	
	/**
	 * Remove media profiles access
	 * @param media Media to remove
	 * @param department Department to remove from
	 * @return Rows
	 */
	public int removeMediaProfileAccess(Media media, Department department) {
		return _context.getContentResolver().delete(MediaDepartmentAccessMetaData.CONTENT_URI, 
				MediaDepartmentAccessMetaData.Table.COLUMN_IDMEDIA + " = '" + media.getId() + "'" +
						MediaDepartmentAccessMetaData.Table.COLUMN_IDDEPARTMENT + " = '" + department.getId() + "'", null);
	}

	/**
	 * Insert media department access
	 * @param mda Media department access
	 * @return 0
	 */
	public long insertMediaDepartmentAccess(MediaDepartmentAccess mda) {
		ContentValues cv = getContentValues(mda);
		_context.getContentResolver().insert(MediaDepartmentAccessMetaData.CONTENT_URI, cv);

		return 0;
	}

//	/**
//	 * Get media department accesses
//	 * @return List of media department access
//	 */
//	public List<MediaDepartmentAccess> getMediaDepartmentAccesses() {
//		List<MediaDepartmentAccess> list = new ArrayList<MediaDepartmentAccess>();
//
//		Cursor c = _context.getContentResolver().query(MediaDepartmentAccessMetaData.CONTENT_URI, columns, null, null, null);
//
//		if (c != null) {
//			list = cursorToMediaDepartmentAccessList(c);
//
//			c.close();
//		}
//
//		return list;
//	}

	/**
	 * Get media by department
	 * @param department Department
	 * @return List of media department access
	 */
	public List<MediaDepartmentAccess> getMediaByDepartment(Department department) {
		List<MediaDepartmentAccess> list= new ArrayList<MediaDepartmentAccess>();

		Cursor c = _context.getContentResolver().query(MediaDepartmentAccessMetaData.CONTENT_URI, columns, 
				MediaDepartmentAccessMetaData.Table.COLUMN_IDDEPARTMENT + " = '" + department.getId() + "'", null, null);

		if (c != null) {
			list = cursorToMediaDepartmentAccessList(c);

			c.close();
		}

		return list;
	}
	
	/**
	 * Modify media department access
	 * @param mda Media department access
	 * @return Rows
	 */
	public int modifyMediaDepartmentAccess(MediaDepartmentAccess mda) {
		ContentValues cv = getContentValues(mda);
		return _context.getContentResolver().update(MediaProfileAccessMetaData.CONTENT_URI, cv, 
				MediaDepartmentAccessMetaData.Table.COLUMN_IDMEDIA + " = '" + mda.getIdMedia() + "' AND " +
				MediaDepartmentAccessMetaData.Table.COLUMN_IDDEPARTMENT + " = '" + mda.getIdDepartment() + "'", null);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//PRIVATE METHODS - keep out!
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Cursor to media department access list
	 * @param cursor Cursor
	 * @return List of media department acceses
	 */
	private List<MediaDepartmentAccess> cursorToMediaDepartmentAccessList(Cursor cursor) {
		List<MediaDepartmentAccess> list = new ArrayList<MediaDepartmentAccess>();

		if(cursor.moveToFirst()) {
			while(!cursor.isAfterLast()) {
				list.add(cursorToMediaDepartmentAccess(cursor));
				cursor.moveToNext();
			}
		}

		return  list;
	}

	/**
	 * Cursor to media department access
	 * @param cursor Cursor
	 * @return Media department access
	 */
	private MediaDepartmentAccess cursorToMediaDepartmentAccess(Cursor cursor) {
		MediaDepartmentAccess mda = new MediaDepartmentAccess();
		mda.setIdMedia(cursor.getLong(cursor.getColumnIndex(MediaDepartmentAccessMetaData.Table.COLUMN_IDMEDIA)));
		mda.setIdDepartment(cursor.getLong(cursor.getColumnIndex(MediaDepartmentAccessMetaData.Table.COLUMN_IDDEPARTMENT)));
		return mda;
	}

	/**
	 * Content values
	 * @param mda Media department access
	 * @return Content values
	 */
	private ContentValues getContentValues(MediaDepartmentAccess mda) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(MediaDepartmentAccessMetaData.Table.COLUMN_IDMEDIA, mda.getIdMedia());
		contentValues.put(MediaDepartmentAccessMetaData.Table.COLUMN_IDDEPARTMENT, mda.getIdDepartment());
		return contentValues;
	}
}