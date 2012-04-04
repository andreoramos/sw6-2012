package dk.aau.cs.giraf.TimerLib;

import dk.aau.cs.giraf.TimerLib.R;

public class ProgressBar extends SubProfile {
	
	public String _direction;
	
	public ProgressBar(String name, String description, int size, int bgcolor, int timeLeftColor, int timeSpentColor, int frameColor, int totalTime, boolean changeColor, String direction){
		super(name, description, size, bgcolor, timeLeftColor, timeSpentColor, frameColor, totalTime, changeColor);
		this._direction = direction;
	}
	
	public ProgressBar(ProgressBar obj){
		super(obj._name, obj._desc,obj._size,obj._bgcolor, obj._timeLeftColor,obj._timeSpentColor,obj._frameColor,obj._totalTime,obj._gradient);
		this._direction = obj._direction;
	}
	
	public ProgressBar copy(){
		ProgressBar copyP = new ProgressBar(this);
		return copyP;
	}
	
	public int formType(){
		return formFactor.ProgressBar.ordinal();
	}
}
