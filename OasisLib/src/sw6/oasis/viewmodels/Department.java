package sw6.oasis.viewmodels;

public class Department {
	
	private long id;
	private String name;
	private String address;
	private long phone;
	private String output;

	/**
	 * Constructor
	 * @param name Name
	 * @param address Address
	 * @param phone Phone
	 */
	public Department(String name, String address, long phone) {
		this.name = name;
		this.address = address;
		this.phone = phone;
	}
	
	/**
	 * Empty constructor
	 */
	public Department() {
		
	}
	
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
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
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
	 * 	{0} = Id
	 *  {1} = Name
	 *  {2} = Address
	 *  {3} = Phone
	 * @param output the output to set
	 */
	public void setOutput(String output) {
		this.output = output;
	}
	
	@Override
	/**
	 * toString for Department
	 */
	public String toString() {
		String localOutput = output;
		localOutput.replace("{0}", String.valueOf(getId()));
		localOutput.replace("{1}", getName());
		localOutput.replace("{2}", getAddress());
		localOutput.replace("{3}", String.valueOf(getPhone()));
		
		return localOutput;
	}
}
