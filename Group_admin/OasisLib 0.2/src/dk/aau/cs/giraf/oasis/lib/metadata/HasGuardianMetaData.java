package dk.aau.cs.giraf.oasis.lib.metadata;

import android.net.Uri;
import android.provider.BaseColumns;

public class HasGuardianMetaData {

	public static final Uri CONTENT_URI = Uri.parse("content://dk.aau.cs.giraf.oasis.localdb.AutismProvider/hasguardian");

	public class Table implements BaseColumns {
		public static final String TABLE_NAME = "tbl_hasguardian";

		public static final String COLUMN_PROFILEID = "hasguardian_profileid";
		public static final String COLUMN_MEDIAID = "hasguardian_mediaid";
	}
}