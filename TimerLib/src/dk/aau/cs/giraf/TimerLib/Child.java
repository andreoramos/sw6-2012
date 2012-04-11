package dk.aau.cs.giraf.TimerLib;

import java.util.ArrayList;
import java.util.Collections;

public class Child implements Comparable<Child>{
	private ArrayList<SubProfile> profileList;
	Guardian guard = Guardian.getInstance();
	public String _name;
	public Child(String name){
		profileList = new ArrayList<SubProfile>();
		this._name = name;
	}
	
	public ArrayList<SubProfile> SubProfiles(){
		return profileList;
	}
	
	public void select(){
		guard.setSelected(this);
	}
	
	public void sortChild(){
		Collections.sort(profileList);
	}
	
	public void save(SubProfile p){
		//save profile on child
		//Wait for admin
		
		//Add to child list
		p.resetLock();
		this.SubProfiles().add(p);
	}
	
	 public boolean equals(Child o) {
	        if (!(o instanceof Child))
	            return false;
	        Child n = (Child) o;
	        return n._name.equals(_name) && n._name.equals(_name);
	    }

	    public int hashCode() {
	        return 31*_name.hashCode() + _name.hashCode();
	    }

	    public String toString() {
		return _name + " " + _name;
	    }

	    public int compareTo(Child n) {
	        int lastCmp = _name.compareTo(n._name);
	        return (lastCmp != 0 ? lastCmp : _name.compareTo(n._name));
	    }
}
