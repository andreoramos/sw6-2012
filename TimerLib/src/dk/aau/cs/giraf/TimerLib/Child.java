package dk.aau.cs.giraf.TimerLib;

import java.util.ArrayList;
import java.util.Collections;

public class Child implements Comparable<Child>{
	private ArrayList<SubProfile> _profileList;
	Guardian guard = Guardian.getInstance();
	public String name;
	public String img;
	private boolean _deleteCheck = true;
	private boolean _lock = false;
	private long _ProfileId;
	
	/**
	 * Default constructor for Child
	 * @param name Name of the child
	 */
	public Child(String name){
		this._profileList = new ArrayList<SubProfile>();
		this.name = name;
	}
	
	void setProfileId(long id){
		this._ProfileId = id;
	}
	
	public long getProfileId(){
		return this._ProfileId;
	}
	
	
	
//	public void delete(SubProfile p){
//		if(this.SubProfiles().contains(p)){
//			guard.delete(this, p);
//		}
//	}
	
	/**
	 * Used to check if you can save on a specific Child
	 * @return boolean, if true, you cannot save on the child. if false you can save.
	 */
	public boolean getLock(){
		return _lock;
	}
	/**
	 * Enables the lock
	 */
	void setLock(){
		this._lock = true;
	}
	/**
	 * Enable delete lock, so you cannot delete from a certain child.
	 * This will always be used on lastUsed and predefined.
	 */
	void lockDelete(){
		_deleteCheck = false;
	}
	/**
	 * Used to check if you can delete a subprofiles on a specific child
	 * @return boolean, true = you can delete: false = you cannot delete.
	 */
	public boolean deleteCheck(){
		return _deleteCheck;
	}
	
	/**
	 * Used to retrieve list of SubProfiles on a certain child
	 * @return
	 */
	public ArrayList<SubProfile> SubProfiles(){
		return _profileList;
	}
	
	/**
	 * Used to select a child
	 * @see getChild() in Guardian
	 */
	public void select(){
		guard.setChild(this);
	}
	
	/**
	 * Used to sort Child SubProfile after name
	 */
	public void sortChild(){
		Collections.sort(_profileList);
	}
	
	/**
	 * Save a specific SubProfile on a Child
	 * @param p The SubProfile you which to save
	 * @return return Id
	 */
	public SubProfile save(SubProfile p){
		//save profile on child
		//Wait for admin
			//Add to child list
			this.SubProfiles().add(p);
		
		return p;
	}
	 private boolean equals(Child o) {
	        if (!(o instanceof Child))
	            return false;
	        Child n = (Child) o;
	        return n.name.equals(name) && n.name.equals(name);
	    }

	    public int hashCode() {
	        return 31*name.hashCode() + name.hashCode();
	    }

	    public String toString() {
		return name + " " + name;
	    }

	    public int compareTo(Child n) {
	        int lastCmp = name.compareTo(n.name);
	        return (lastCmp != 0 ? lastCmp : name.compareTo(n.name));
	    }
}
