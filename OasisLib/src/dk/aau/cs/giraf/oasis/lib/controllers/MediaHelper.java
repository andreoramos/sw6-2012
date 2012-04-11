package dk.aau.cs.giraf.oasis.lib.controllers;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.Tag;
import dk.aau.cs.giraf.oasis.lib.metadata.HasTagMetaData;
import dk.aau.cs.giraf.oasis.lib.metadata.MediaDepartmentAccessMetaData;
import dk.aau.cs.giraf.oasis.lib.metadata.MediaMetaData;
import dk.aau.cs.giraf.oasis.lib.metadata.MediaProfileAccessMetaData;
import dk.aau.cs.giraf.oasis.lib.metadata.TagsMetaData;
import dk.aau.cs.giraf.oasis.lib.models.Department;
import dk.aau.cs.giraf.oasis.lib.models.Media;
import dk.aau.cs.giraf.oasis.lib.models.Profile;

/**
 * Helper class for Media
 * @author Admin
 *
 */
public class MediaHelper {

	private static Context _context;
	/**
	 * Constructor
	 * @param context Current context
	 */
	public MediaHelper(Context context){
		_context = context;
	}

	/**
	 * Insert media
	 * @param media Media containing data
	 */
	public void insertMedia(Media media) {
		ContentValues cv = getContentValues(media);
		_context.getContentResolver().insert(MediaMetaData.CONTENT_URI, cv);
	}

	/**
	 * Modify media
	 * @param media Media containing data to modify
	 */
	public void modifyMedia(Media media) {
		Uri uri = ContentUris.withAppendedId(MediaMetaData.CONTENT_URI, media.getId());
		ContentValues cv = getContentValues(media);
		_context.getContentResolver().update(uri, cv, null, null);
	}
	
	/**
	 * Get media by id
	 * @param id the id of the media
	 * @return the media or null
	 */
	public Media getSingleMediaById(long id) {
		Uri uri = ContentUris.withAppendedId(MediaMetaData.CONTENT_URI, id);
		Cursor c = _context.getContentResolver().query(uri, columns, null, null, null);
		Media media = null;
		
		if(c.moveToFirst()) {
			media = cursorToSingleMedia(c);			
		}
		
		c.close();
		return media;
	}
	
	/**
	 * Get media by name
	 * @param name the name of the media
	 * @return List<Media>, containing all media of that name
	 */
	public List<Media> getMediaByName(String name) {
		Cursor c = _context.getContentResolver().query(Uri.withAppendedPath(MediaMetaData.CONTENT_URI, name), columns, null, null, null);
		
		List<Media> media = new ArrayList<Media>();
		media = cursorToMedia(c);

		c.close();
		return media;
	}
	
	/**
	 * Get all media
	 * @return List<Media>, containing all media
	 */
	public List<Media> getMedia() {
		Cursor c = _context.getContentResolver().query(MediaMetaData.CONTENT_URI, columns, null, null, null);
		
		List<Media> media = new ArrayList<Media>();
		media = cursorToMedia(c);

		c.close();
		return media;
	}
	
	public List<Media> getMediaByTags(String[] tags) {
		List<Media> mediaList = new ArrayList<Media>();
		
		for (String tag : tags) {
			String[] tagColumns = {TagsMetaData.Table.COLUMN_ID, TagsMetaData.Table.COLUMN_CAPTION};
			Cursor cTag = _context.getContentResolver().query(TagsMetaData.CONTENT_URI, tagColumns, tagColumns[1] + " = '" + tag + "'", null, null);
			if (cTag != null) {
				if (cTag.moveToFirst()) {
					long tagId = cTag.getLong(cTag.getColumnIndex(tagColumns[0]));
					List<Long> mediaIdList = new ArrayList<Long>();
					String[] hasTagColumns = {HasTagMetaData.Table.COLUMN_IDTAG, HasTagMetaData.Table.COLUMN_IDMEDIA};
					Cursor cHasTag = _context.getContentResolver().query(HasTagMetaData.CONTENT_URI, hasTagColumns, 
							hasTagColumns[0] + " = '" + tagId + "'", null, null);
					if (cHasTag != null) {
						if (cHasTag.moveToFirst()) {
							while (!cHasTag.isAfterLast()) {
								long mediaId = cHasTag.getLong(cHasTag.getColumnIndex(hasTagColumns[1]));
								mediaIdList.add(mediaId);
							}
						}
						for (long mId : mediaIdList) {
							Cursor cMedia = _context.getContentResolver().query(MediaMetaData.CONTENT_URI, columns, 
									MediaMetaData.Table.COLUMN_ID + " = '" + mId + "'", null, null);
							if (cMedia != null) {
								if (cMedia.moveToFirst()) {
									while (!cMedia.isAfterLast()) {
										mediaList.add(cursorToSingleMedia(cMedia));
									}
								}
							}
							cMedia.close();
						}
					}
					cHasTag.close();
				}
			}
			cTag.close();
		}
		
		return mediaList;
	}
	
	public List<Media> getMediaByDepartment(Department department) {
		List<Media> mediaList = new ArrayList<Media>();
		String[] mediaDepColumns = {MediaDepartmentAccessMetaData.Table.COLUMN_IDDEPARTMENT, MediaDepartmentAccessMetaData.Table.COLUMN_IDMEDIA}; 
		Cursor c = _context.getContentResolver().query(MediaDepartmentAccessMetaData.CONTENT_URI, mediaDepColumns, mediaDepColumns[0] + " = '" + department.getId() + "'", null, null);
		
		if (c != null) {
			if (c.moveToFirst()) {
				while (!c.isAfterLast()) {
					Media media = getMediaById(c.getLong(c.getColumnIndex(mediaDepColumns[1])));
					mediaList.add(media);
					c.moveToNext();
				}
			}
		}
		
		c.close();
		
		return mediaList;
	}
	
	public List<Media> getMediaByProfile(Profile profile) {
		List<Media> mediaList = new ArrayList<Media>();
		String[] mediaProfileColumns = {MediaProfileAccessMetaData.Table.COLUMN_IDPROFILE, MediaProfileAccessMetaData.Table.COLUMN_IDMEDIA}; 
		Cursor c = _context.getContentResolver().query(MediaDepartmentAccessMetaData.CONTENT_URI, mediaProfileColumns, mediaProfileColumns[0] + " = '" + profile.getId() + "'", null, null);
		
		if (c != null) {
			if (c.moveToFirst()) {
				while (!c.isAfterLast()) {
					Media media = getMediaById(c.getLong(c.getColumnIndex(mediaProfileColumns[1])));
					mediaList.add(media);
					c.moveToNext();
				}
			}
		}
		
		c.close();
		
		return mediaList;
	}
	
	public Media getMediaById(long id) {
		Cursor c = _context.getContentResolver().query(MediaMetaData.CONTENT_URI, columns, MediaMetaData.Table.COLUMN_ID + " = '" + id + "'", null, null);
		
		if (c != null) {
			if (c.moveToFirst()) {
				return cursorToSingleMedia(c);
			}
		}
		return null;
	}
	
	public int addTagsToMedia(String[] tags, Media media) {
		for (String tag : tags) {
			String[] tagColumns = {TagsMetaData.Table.COLUMN_ID, TagsMetaData.Table.COLUMN_CAPTION};
			Cursor cTag = _context.getContentResolver().query(TagsMetaData.CONTENT_URI, tagColumns, tagColumns[1] + " = '" + tag + "'", null, null);
			if (cTag != null) {
				if (cTag.moveToFirst()) {
					String caption = cTag.getString(cTag.getColumnIndex(tagColumns[1]));
					Tag _tag = new Tag(caption); 
				}
			}
		}
	}
	
	private void addHasTag(Tag tag, Media media) {
		ContentValues values = new ContentValues();
		values.put(HasTagMetaData.Table.COLUMN_IDTAG, tag.getId());
		values.put(HasTagMetaData.Table.COLUMN_IDMEDIA, media.getId());
		_context.getContentResolver().insert(HasTagMetaData.CONTENT_URI, values);
	}
	
	public int attachMediaToProfile(Media media, Profile profile, Profile owner) {
		if (media.isMPublic() || media.getOwnerId() == owner.getId()) {
			ContentValues values = new ContentValues();
			values.put(MediaProfileAccessMetaData.Table.COLUMN_IDMEDIA, media.getId());
			values.put(MediaProfileAccessMetaData.Table.COLUMN_IDPROFILE, profile.getId());
			_context.getContentResolver().insert(MediaProfileAccessMetaData.CONTENT_URI, values);
			return 0;
		} else {
			return -1;
		}
	}
	
	public int attachMediaToDepartment(Media media, Department department, Profile owner) {
		if (media.isMPublic() || media.getOwnerId() == owner.getId()) {
			ContentValues values = new ContentValues();
			values.put(MediaDepartmentAccessMetaData.Table.COLUMN_IDMEDIA, media.getId());
			values.put(MediaDepartmentAccessMetaData.Table.COLUMN_IDDEPARTMENT, department.getId());
			_context.getContentResolver().insert(MediaDepartmentAccessMetaData.CONTENT_URI, values);
			return 0;
		} else {
			return -1;
		}
	}
	
	public int removeMediaAttachmentToProfile(Media media, Profile profile) {
		_context.getContentResolver().delete(MediaProfileAccessMetaData.CONTENT_URI, 
				MediaProfileAccessMetaData.Table.COLUMN_IDMEDIA + " = '" + media.getId() + "'" +
				MediaProfileAccessMetaData.Table.COLUMN_IDPROFILE + " = '" + profile.getId() + "'", null);
		return 0;
	}
	
	public int removeMediaAttachmentToDepartment(Media media, Department department) {
		_context.getContentResolver().delete(MediaDepartmentAccessMetaData.CONTENT_URI, 
				MediaDepartmentAccessMetaData.Table.COLUMN_IDMEDIA + " = '" + media.getId() + "'" +
				MediaDepartmentAccessMetaData.Table.COLUMN_IDDEPARTMENT + " = '" + department.getId() + "'", null);
		return 0;
	}
	
	/**
	 * Clear media table
	 */
	public void clearMediaTable() {
		_context.getContentResolver().delete(MediaMetaData.CONTENT_URI, null, null);
	}
	
	/**
	 * Cursor to media
	 * @param cursor Input cursor
	 * @return List<Media>, containing all the media from the cursor
	 */
	private List<Media> cursorToMedia(Cursor cursor) {
		List<Media> media = new ArrayList<Media>();
		
		if(cursor.moveToFirst()) {
			while(!cursor.isAfterLast()) {
				media.add(cursorToSingleMedia(cursor));
				cursor.moveToNext();
			}
		}
		
		return media;
	}

	/**
	 * Cursor to single media
	 * @param cursor Input cursor
	 * @return Single Media
	 */
	private Media cursorToSingleMedia(Cursor cursor) {
		Media media = new Media();
		media.setId(cursor.getLong(cursor.getColumnIndex(MediaMetaData.Table.COLUMN_ID)));
		media.setName(cursor.getString(cursor.getColumnIndex(MediaMetaData.Table.COLUMN_NAME)));
		media.setMPath(cursor.getString(cursor.getColumnIndex(MediaMetaData.Table.COLUMN_PATH)));
		media.setMType(cursor.getString(cursor.getColumnIndex(MediaMetaData.Table.COLUMN_TYPE)));
		media.setOwnerId(cursor.getLong(cursor.getColumnIndex(MediaMetaData.Table.COLUMN_OWNERID)));
		if (cursor.getLong(cursor.getColumnIndex(MediaMetaData.Table.COLUMN_PUBLIC)) == 1) {
			media.setMPublic(true);
		} else {
			media.setMPublic(false);
		}
		return media;
	}
	
	/**
	 * Getter for the content values
	 * @param media the media which values should be used
	 * @return the contentValues
	 */
	private ContentValues getContentValues(Media media) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(MediaMetaData.Table.COLUMN_PATH, media.getMPath());
		contentValues.put(MediaMetaData.Table.COLUMN_NAME, media.getName());
		contentValues.put(MediaMetaData.Table.COLUMN_PUBLIC, media.isMPublic());
		contentValues.put(MediaMetaData.Table.COLUMN_TYPE, media.getMType());
		contentValues.put(MediaMetaData.Table.COLUMN_OWNERID, media.getOwnerId());
		
		return contentValues;
	}
	
	String[] columns = new String[] { 
			MediaMetaData.Table.COLUMN_ID, 
			MediaMetaData.Table.COLUMN_PATH,
			MediaMetaData.Table.COLUMN_NAME,
			MediaMetaData.Table.COLUMN_PUBLIC,
			MediaMetaData.Table.COLUMN_TYPE,
			MediaMetaData.Table.COLUMN_OWNERID};

}