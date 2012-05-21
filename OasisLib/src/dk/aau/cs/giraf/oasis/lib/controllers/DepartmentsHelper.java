package dk.aau.cs.giraf.oasis.lib.controllers;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import dk.aau.cs.giraf.oasis.lib.metadata.DepartmentsMetaData;
import dk.aau.cs.giraf.oasis.lib.models.AuthUser;
import dk.aau.cs.giraf.oasis.lib.models.Department;
import dk.aau.cs.giraf.oasis.lib.models.HasDepartment;
import dk.aau.cs.giraf.oasis.lib.models.HasSubDepartment;
import dk.aau.cs.giraf.oasis.lib.models.Media;
import dk.aau.cs.giraf.oasis.lib.models.MediaDepartmentAccess;
import dk.aau.cs.giraf.oasis.lib.models.Profile;

/**
 * Helper class for Departments
 * 
 * @author Admin
 * 
 */
public class DepartmentsHelper {

	private static Context _context;
	private AuthUsersController au;
	private HasDepartmentController hd;
	private HasSubDepartmentController hsd;
	private MediaDepartmentAccessController mda;
	private String[] columns = new String[] { 
			DepartmentsMetaData.Table.COLUMN_ID,
			DepartmentsMetaData.Table.COLUMN_NAME,
			DepartmentsMetaData.Table.COLUMN_ADDRESS,
			DepartmentsMetaData.Table.COLUMN_PHONE,
			DepartmentsMetaData.Table.COLUMN_EMAIL};

	/**
	 * Constructor
	 * @param context Current context
	 */
	public DepartmentsHelper(Context context) {
		_context = context;
		au = new AuthUsersController(_context);
		hd = new HasDepartmentController(_context);
		hsd = new HasSubDepartmentController(_context);
		mda = new MediaDepartmentAccessController(_context);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//METHODS TO CALL
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//	/**
	//	 * Clear department table
	//	 */
	//	public void clearDepartmentsTable() {
	//		_context.getContentResolver().delete(DepartmentsMetaData.CONTENT_URI,
	//				null, null);
	//	}

	/**
	 * Cascade remove department
	 * @param department the department to remove
	 * @return Rows affected
	 */
	public int removeDepartment(Department department) {
		int rows = -1;
		if (department != null) {
			rows = 0;
			AuthUser authUser = au.getAuthUserById(department.getId());

			rows += au.removeAuthUser(authUser);
			rows += hd.removeHasDepartmentByDepartmentId(department.getId());
			rows += mda.removeMediaDepartmentAccessByDepartmentId(department.getId());
			rows += hsd.removeHasSubDepartmentsByDepartmentId(department.getId());
			rows += hsd.removeHasSubDepartmentsBySubDepartmentId(department.getId());
			rows += _context.getContentResolver().delete(DepartmentsMetaData.CONTENT_URI, 
					DepartmentsMetaData.Table.COLUMN_ID + " = '" + department.getId() + "'", null);
		}
		return rows;
	}

	/**
	 * Remove profile attachment to department
	 * @param profile Profile to remove
	 * @param department Department to remove from
	 * @return Rows affected
	 */
	public int removeProfileAttachmentToDepartment(Profile profile, Department department) {
		int result = 0;
		if (profile != null && department != null) {
			result = hd.removeHasDepartment(new HasDepartment(profile.getId(), department.getId()));
		} else {
			result = -1;
		}
		return result;
	}

	/**
	 * Remove subdepartment attachment to department
	 * @param department Department to remove from
	 * @param subDepartment Subdepartment to remove
	 * @return Rows affected
	 */
	public int removeSubDepartmentAttachmentToDepartment(Department department, Department subDepartment) {
		int result = 0;
		if (department != null && subDepartment != null) {
			result = hsd.removeHasSubDepartment(new HasSubDepartment(department.getId(), subDepartment.getId()));
		} else {
			result = -1;
		}
		return result;
	}
	/**
	 * Remove media attachment to department
	 * @param media
	 * @param department
	 * @return Rows affected
	 */
	public int removeMediaAttachmentToDepartment(Media media, Department department) {
		int result = 0;
		if (media != null && department != null) {
			result = mda.removeMediaDepartmentAccess(new MediaDepartmentAccess(media.getId(), department.getId()));
		} else {
			result = -1;
		}
		return result;
	}

	/**
	 * Insert department
	 * @param department Department containing data
	 */
	public long insertDepartment(Department department) {
		long id = -1;
		if (department != null) {
			id = au.insertAuthUser(AuthUser.aRole.DEPARTMENT.ordinal());

			ContentValues cv = getContentValues(department);
			cv.put(DepartmentsMetaData.Table.COLUMN_ID, id);
			_context.getContentResolver().insert(DepartmentsMetaData.CONTENT_URI, cv);
		}
		return id;
	}

	/**
	 * Attach profile to department
	 * @param profile Profile to attach
	 * @param department Department to attach to
	 * @return Result
	 */
	public int attachProfileToDepartment(Profile profile, Department department) {
		int result = -1;
		if (profile != null && department != null) {
			HasDepartment hdModel = new HasDepartment(profile.getId(), department.getId());
			result = hd.insertHasDepartment(hdModel);
		}
		return result;
	}

	/**
	 * Attach sub department to department
	 * @param department Department to attach to
	 * @param subDepartment Subdepartment to attach
	 * @return Result
	 */
	public int attachSubDepartmentToDepartment(Department department, Department subDepartment) {
		int result = -1;
		if (department != null && subDepartment != null) {
			HasSubDepartment hsdModel = new HasSubDepartment(department.getId(), subDepartment.getId());
			result = hsd.insertHasSubDepartment(hsdModel);
		}
		return result;
	}
	/**
	 * Attach media to department
	 * @param media the media to attach
	 * @param department the department to attach to
	 * @return Rows affected
	 */
	public int attachMediaToDepartment(Media media, Department department) {
		int result = -1;
		if (media != null && department != null) {
			MediaDepartmentAccess mdaModel = new MediaDepartmentAccess(media.getId(), department.getId());
			result = mda.insertMediaDepartmentAccess(mdaModel);
		}
		return result;
	}

	/**
	 * Modify department
	 * @param department
	 * Department containing data to modify
	 */
	public int modifyDepartment(Department department) {
		int result = -1;
		if (department != null) {
			Uri uri = ContentUris.withAppendedId(DepartmentsMetaData.CONTENT_URI, department.getId());
			ContentValues cv = getContentValues(department);
			_context.getContentResolver().update(uri, cv, null, null);
			result = 0;
		}
		return result;
	}

	/**
	 * Authenticates the department
	 * @param certificate the certificate that authenticates the department
	 * @return the authenticated department or null
	 */
	public Department authenticateDepartment(String certificate) {
		Department dep = null;
		if (certificate != null) {
			long id = au.getIdByCertificate(certificate);
			if (id != -1) {
				dep = getDepartmentById(id);
			}
		}
		return dep;
	}

	/**
	 * Set a new certificate
	 * @param certificate the certificate to set
	 * @param department the department whom the certificate is updated for
	 * @return Rows affected
	 */
	public int setCertificate(String certificate, Department department) {
		int rows = -1;
		if (certificate != null && department != null) {
			rows = au.setCertificate(certificate, department.getId());
		}
		return rows;
	}

	/**
	 * Get all departments
	 * @return List of Departments, containing all departments
	 */
	public List<Department> getDepartments() {
		List<Department> departments = new ArrayList<Department>();
		Cursor c = _context.getContentResolver().query(
				DepartmentsMetaData.CONTENT_URI, columns, null, null, null);

		if (c != null) {
			if (c.moveToFirst()) {
				while (!c.isAfterLast()) {
					departments.add(cursorToDepartment(c));
					c.moveToNext();
				}
			}
			c.close();
		}

		return departments;
	}

	/**
	 * Retrieve the certificates for a department
	 * @param department
	 * @return List of Strings
	 */
	public List<String> getCertificatesByDepartment(Department department) {
		List<String> certificates = new ArrayList<String>();

		if (department != null) {
			certificates = au.getCertificatesById(department.getId());
		}

		return certificates;
	}

	/**
	 * Get departments by id
	 * @param id Id of the department
	 * @return Department
	 */
	public Department getDepartmentById(long id) {
		Department department = null;

		Uri uri = ContentUris.withAppendedId(DepartmentsMetaData.CONTENT_URI, id);
		Cursor c = _context.getContentResolver().query(uri, columns, null, null, null);

		if (c != null) {
			if(c.moveToFirst()) {
				department = cursorToDepartment(c);
			}
			c.close();
		}

		return department;
	}

	/**
	 * Get departments by name
	 * @param name Name of the appartment
	 * @return List of departments
	 */
	public List<Department> getDepartmentByName(String name) {
		List<Department> departments = new ArrayList<Department>();
		if (name != null) {
			
			Cursor c = _context.getContentResolver().query(DepartmentsMetaData.CONTENT_URI, columns, 
					DepartmentsMetaData.Table.COLUMN_NAME + " = '" + name + "'", null, null);
			
			if (c != null) {
				if (c.moveToFirst()) {
					while (!c.isAfterLast()) {
						departments.add(cursorToDepartment(c));
						c.moveToNext();
					}
				}
				c.close();
			}
			
		}
		
		return departments;
	}

	/**
	 * Get departments by profile
	 * @param profile Profile to get departments by
	 * @return List of departments
	 */
	public List<Department> getDepartmentsByProfile(Profile profile) {
		List<Department> departments = new ArrayList<Department>();
		
		if (profile != null) {
			List<HasDepartment> list = hd.getHasDepartmentsByProfile(profile);

			for (HasDepartment hdModel : list) {
				departments.add(getDepartmentById(hdModel.getIdDepartment()));
			}
		}
		return departments;
	}

	/**
	 * Get sub departments
	 * @param department Department to get sub departments by
	 * @return List of sub departments
	 */
	public List<Department> getSubDepartments(Department department) {
		List<Department> departments = new ArrayList<Department>();
		if (department != null) {
			List<HasSubDepartment> list = hsd.getSubDepartmentsByDepartment(department);

			for (HasSubDepartment hsdModel : list) {
				departments.add(getDepartmentById(hsdModel.getIdSubDepartment()));
			}
		}
		return departments;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//PRIVATE METHODS - Keep out!
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Cursor to department
	 * @param cursor Input cursor
	 * @return Output department
	 */
	private Department cursorToDepartment(Cursor cursor) {
		Department department = new Department();
		department.setId(cursor.getLong(cursor
				.getColumnIndex(DepartmentsMetaData.Table.COLUMN_ID)));
		department.setName(cursor.getString(cursor
				.getColumnIndex(DepartmentsMetaData.Table.COLUMN_NAME)));
		department.setAddress(cursor.getString(cursor
				.getColumnIndex(DepartmentsMetaData.Table.COLUMN_ADDRESS)));
		department.setEmail(cursor.getString(cursor
				.getColumnIndex(DepartmentsMetaData.Table.COLUMN_EMAIL)));
		department.setPhone(cursor.getLong(cursor
				.getColumnIndex(DepartmentsMetaData.Table.COLUMN_PHONE)));
		return department;
	}

	private ContentValues getContentValues(Department department) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DepartmentsMetaData.Table.COLUMN_NAME, department.getName());
		contentValues.put(DepartmentsMetaData.Table.COLUMN_ADDRESS, department.getAddress());
		contentValues.put(DepartmentsMetaData.Table.COLUMN_EMAIL, department.getEmail());
		contentValues.put(DepartmentsMetaData.Table.COLUMN_PHONE, department.getPhone());
		return contentValues;
	}
}
