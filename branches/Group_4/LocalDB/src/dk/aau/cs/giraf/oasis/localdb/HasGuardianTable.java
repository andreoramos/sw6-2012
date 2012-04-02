package dk.aau.cs.giraf.oasis.localdb;

import android.database.sqlite.SQLiteDatabase;

public class HasGuardianTable {
	
	private static final String TABLE_CREATE = "CREATE TABLE "
			+ HasGuardianMetaData.Table.TABLE_NAME
			+ "("
			+ HasGuardianMetaData.Table.COLUMN_MEDIAID + " INTEGER, "
			+ HasGuardianMetaData.Table.COLUMN_PROFILEID + " INTEGER, "
			+ "PRIMARY KEY(" + HasGuardianMetaData.Table.COLUMN_MEDIAID + ", " + HasGuardianMetaData.Table.COLUMN_PROFILEID + ")"
			+ ");";

	private static final String TABLE_DROP= "DROP TABLE IF EXISTS " + HasGuardianMetaData.Table.TABLE_NAME + ";";

	/**
	 * Executes sql string for creating table
	 * @param db this is an instance of a sqlite database
	 */
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
	}

	/**
	 * executes a sql string which drops the old table and then the method call the oncreate, which creates a new table
	 * @param db this is an instance of a sqlite database
	 * @param oldVersion integer refering to the old version number
	 * @param newVersion integer refering to the new version number
	 */
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(TABLE_DROP);
		onCreate(db);
	}
}
