package dk.aau.cs.giraf.oasis.lib.models;

public class Tag {
	private long id;
	private String caption;
	private static String _output = "{0}, {1}";
	
	/**
	 * Constructor with parameters
	 * @param caption the caption of the tag
	 */
	public Tag(String caption) {
		this.caption = caption;
	}
	
	/**
	 * Empty constructor
	 */
	public Tag() {
		
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
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}
	/**
	 * @param caption the caption to set
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	/**
	 * {0} = ID
	 * {1} = caption
	 * @param output String to set
	 */
	public void setOutput(String output) {
		_output = output;
	}
	
	public String toString() {
		String localOutput = _output;
		localOutput = localOutput.replace("{0}", String.valueOf(getId()));
		localOutput = localOutput.replace("{1}", getCaption());

		return localOutput;
	}
}
