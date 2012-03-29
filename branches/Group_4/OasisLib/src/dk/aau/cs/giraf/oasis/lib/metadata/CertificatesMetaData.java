package dk.aau.cs.giraf.oasis.lib.metadata;

import android.net.Uri;
import android.provider.BaseColumns;

public class CertificatesMetaData {

	public static final Uri CONTENT_URI = Uri.parse("content://sw6.oasis.provider.AutismProvider/certificates");

	public class Table implements BaseColumns {
		public static final String TABLE_NAME = "tbl_certificates";

		public static final String COLUMN_ID = "_id";
		public static final String COLUMN_AUTHKEY = "certificates_authkey";
	}
}
