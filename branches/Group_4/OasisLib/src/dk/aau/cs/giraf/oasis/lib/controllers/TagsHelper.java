package dk.aau.cs.giraf.oasis.lib.controllers;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import dk.aau.cs.giraf.oasis.lib.metadata.TagsMetaData;
import dk.aau.cs.giraf.oasis.lib.models.Tag;

public class TagsHelper {

	private Context _context;
	private String[] columns = new String[] { 
			TagsMetaData.Table.COLUMN_ID, 
			TagsMetaData.Table.COLUMN_CAPTION};

	public TagsHelper(Context context) {
		_context = context;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//METHODS TO CALL
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public int clearTagsTable() {
		return _context.getContentResolver().delete(TagsMetaData.CONTENT_URI, null, null);
	}
	
	public long insertTag(Tag tag) {
		long id = 0;
		
		Cursor c = _context.getContentResolver().query(TagsMetaData.CONTENT_URI, columns, TagsMetaData.Table.COLUMN_CAPTION + " = '" + tag.getCaption() + "'", null, null);
		
		if (c != null) {
			if (c.moveToFirst()) {
				id = c.getLong(c.getColumnIndex(TagsMetaData.Table.COLUMN_ID));
			}
			c.close();
		} else {
			ContentValues cv = getContentValues(tag);
			Uri uri = _context.getContentResolver().insert(TagsMetaData.CONTENT_URI, cv);
			id = Integer.parseInt(uri.getPathSegments().get(1));
		}
		
		return id;
	}

	public int modifyTag(Tag tag) {
		Uri uri = ContentUris.withAppendedId(TagsMetaData.CONTENT_URI, tag.getId());
		ContentValues cv = getContentValues(tag);
		return _context.getContentResolver().update(uri, cv, null, null);
	}

	public List<Tag> getTags() {
		List<Tag> tags = new ArrayList<Tag>();

		Cursor c = _context.getContentResolver().query(TagsMetaData.CONTENT_URI, columns, null, null, null);

		if (c != null) {
			tags = cursorToTagList(c);
			c.close();
		}

		return tags;
	}
	
	public List<Tag> getTagsByCaption(String name) {
		List<Tag> tags = new ArrayList<Tag>();

		Cursor c = _context.getContentResolver().query(TagsMetaData.CONTENT_URI, columns, TagsMetaData.Table.COLUMN_CAPTION + " LIKE '%" + name + "%'", null, null);

		if (c != null) {
			tags = cursorToTagList(c);
			c.close();
		}

		return tags;
	}
	
	public Tag getTagById(Long id) {
		Tag tag = null;
		
		Uri uri = ContentUris.withAppendedId(TagsMetaData.CONTENT_URI, id);
		
		Cursor c = _context.getContentResolver().query(uri, columns, null, null, null);

		if (c != null) {
			tag = cursorToTag(c);
			c.close();
		}

		

		return tag;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//PRIVATE METHODS - keep out!
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Cursor to Tags
	 * @param cursor Input cursor
	 * @return Output List of Tags
	 */
	private List<Tag> cursorToTagList(Cursor cursor) {
		List<Tag> tags = new ArrayList<Tag>();

		if(cursor.moveToFirst()) {
			while(!cursor.isAfterLast()) {
				tags.add(cursorToTag(cursor));
				cursor.moveToNext();
			}
		}


		return tags;
	}

	private Tag cursorToTag(Cursor cursor) {
		Tag tag = new Tag();
		tag.setId(cursor.getLong(cursor.getColumnIndex(TagsMetaData.Table.COLUMN_ID)));
		tag.setCaption(cursor.getString(cursor.getColumnIndex(TagsMetaData.Table.COLUMN_CAPTION)));
		return tag;
	}

	private ContentValues getContentValues(Tag tag) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(TagsMetaData.Table.COLUMN_CAPTION, tag.getCaption());
		return contentValues;
	}
}