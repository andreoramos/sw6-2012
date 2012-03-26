package sw6.oasis.controllers.metadata;

import android.net.Uri;
import android.provider.BaseColumns;

public class AppsMetaData {
	
	public static final Uri CONTENT_URI = Uri.parse("content://sw6.oasis.provider.AutismProvider/apps");
	
	public class Table implements BaseColumns {
		public static final String TABLE_NAME = "tbl_apps";
		
		public static final String COLUMN_ID = "_id";
		public static final String COLUMN_NAME = "apps_name";
		public static final String COLUMN_VERSIONNUMBER = "apps_version";
	}
}
