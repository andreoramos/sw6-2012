package dk.aau.cs.giraf.oasis.lib.metadata;

import android.net.Uri;
import android.provider.BaseColumns;

public class AppsMetaData {
	
	public static final Uri CONTENT_URI = Uri.parse("content://dk.aau.cs.giraf.oasis.localdb.AutismProvider/apps");
	
	public class Table implements BaseColumns {
		public static final String TABLE_NAME = "tbl_apps1";
		
		public static final String COLUMN_ID = "_id";
		public static final String COLUMN_NAME = "apps_name";
		public static final String COLUMN_VERSION = "apps_version";
	}
}
