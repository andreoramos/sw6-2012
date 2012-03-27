package sw6.autism.timer.TimerLib;

public class Hourglass extends Profile {
	
	public String _name;
	public String _description;
	public int _sand;
	
	
	public Hourglass(int id, String name, String description, int size, String bgcolor, String timeLeftColor, String timeSpentColor, int totalTime, DigitalClock digital, boolean changeColor, int sand){
		super(id, name, description, size, bgcolor, timeLeftColor, timeSpentColor, totalTime,digital,changeColor);
		this._sand = sand;
	}
}
