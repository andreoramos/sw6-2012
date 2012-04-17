package dk.aau.cs.giraf.oasis.lib.models;

public class HasGuardian {

	private long idGuardian;
	private long idChild;
	private static String _output = "{0}, {1}";
	
	/**
	 * @return the idGuardian
	 */
	public long getIdGuardian() {
		return idGuardian;
	}
	
	/**
	 * @param idGuardian the idGuardian to set
	 */
	public void setIdGuardian(long idGuardian) {
		this.idGuardian = idGuardian;
	}
	
	/**
	 * @return the idChild
	 */
	public long getIdChild() {
		return idChild;
	}
	
	/**
	 * @param idChild the idChild to set
	 */
	public void setIdChild(long idChild) {
		this.idChild = idChild;
	}

	/**
	 * @param _output the _output to set
	 */
	public static void set_output(String _output) {
		HasGuardian._output = _output;
	}
	
	@Override
	public String toString() {

		String localOutput = _output;
		localOutput = localOutput.replace("{0}", String.valueOf(getIdChild()));
		localOutput = localOutput.replace("{1}", String.valueOf(getIdGuardian()));
		
		return localOutput;
	}
}
