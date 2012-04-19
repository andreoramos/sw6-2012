package dk.aau.cs.giraf.wombat;


import dk.aau.cs.giraf.TimerLib.Guardian;
import dk.aau.cs.giraf.TimerLib.TimerHelper;

public class TimerLoader {
	
	public static void load(){
		Guardian guard = Guardian.getInstance();
		TimerHelper help = new TimerHelper();
		help.LoadTestData();
		help.loadPredef();
		guard.publishList();
	}
	
	public static long profileID;
	
	public static int profilePosition;
	public static int subProfileID; 	// Stores the id of the saved subprofile
	public static boolean subProfileFirstClick;	// When a subprofile is saved, this is set to true
										// When something is clicked in the subprofile list, set to false
	public static boolean profileFirstClick;
}
