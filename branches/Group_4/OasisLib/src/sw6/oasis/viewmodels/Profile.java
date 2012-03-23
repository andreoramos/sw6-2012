package sw6.oasis.viewmodels;

public class Profile {

	private long id;
	private String firstname;
	private String surname;
	private String middlename;
	private long role;
	private long phone;
	private String picture;
	private long departmentId;
	private String output;
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getFirstname() {
		return firstname;
	}
	/**
	 * @param firstname the name to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}
	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}
	/**
	 * @return the middlename
	 */
	public String getMiddlename() {
		return middlename;
	}
	/**
	 * @param middlename the middlename to set
	 */
	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}
	/**
	 * @return the role
	 */
	public long getRole() {
		return role;
	}
	/**
	 * @param role the role to set
	 */
	public void setRole(long role) {
		this.role = role;
	}
	/**
	 * @return the phone
	 */
	public long getPhone() {
		return phone;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(long phone) {
		this.phone = phone;
	}
	/**
	 * @return the picture
	 */
	public String getPicture() {
		return picture;
	}
	/**
	 * @param picture the picture to set
	 */
	public void setPicture(String picture) {
		this.picture = picture;
	}
	/**
	 * @return the departmentId
	 */
	public long getDepartmentId() {
		return departmentId;
	}
	/**
	 * @param departmentId the departmentId to set
	 */
	public void setDepartmentId(long departmentId) {
		this.departmentId = departmentId;
	}
	/**
	 * Set output
	 * 	{0} = Firstname
	 *  {1} = Middlename
	 *  {2} = Surname
	 *  {3} = Role
	 *  {4} = Phone
	 *  {5} = Picture
	 *  {6} = DepartmentId
	 * @param output String to set
	 */
	public void setOutput(String output) {
		this.output = output;
	}
	
	@Override
	/**
	 * toString for profile
	 */
	public String toString() {
		String localOutput = output;
		localOutput.replace("{0}", getFirstname());
		localOutput.replace("{1}", getMiddlename());
		localOutput.replace("{2}", getSurname());
		localOutput.replace("{3}", String.valueOf(getRole()));
		localOutput.replace("{4}", String.valueOf(getPhone()));
		localOutput.replace("{5}", getPicture());
		localOutput.replace("{6}", String.valueOf(getDepartmentId()));
		
		return localOutput;
	}
}
