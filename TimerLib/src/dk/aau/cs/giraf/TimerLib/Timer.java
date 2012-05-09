package dk.aau.cs.giraf.TimerLib;

import java.util.HashMap;

import dk.aau.cs.giraf.oasis.lib.models.Media;

public class Timer extends Attachment {
	
	private formFactor _form;
	private int _bgColor = -1;
	private int _frameColor = -1;
	private int _timeLeftColor = -1;
	private int _timeSpentColor = -1;
	private boolean _gradient = false;
	private int _time;

	public Timer(SubProfile p){
		this._form = p.formType();
		this._bgColor = p.bgcolor;
		this._frameColor = p.frameColor;
		this._timeLeftColor = p.timeLeftColor;
		this._timeSpentColor = p.timeSpentColor;
		this._gradient = p.gradient;
		this._time = p.get_totalTime();
	}

	public Timer getAttachment(){
		return this;
	}
	
	public formFactor getForm(){
		return formFactor.Timer;
	}
	
	public formFactor formType(){
		return this._form;
	}
	
	void changeTime(int t){
		this._time = t;
	}
	
	public HashMap getHashMap(HashMap map){
		//Defines what kind of attachment it is
		map.put("AttachmentForm", String.valueOf(this.getForm()));
		
		//Timer
		map.put("timerForm", String.valueOf(this._form));
		map.put("_bgColor", String.valueOf(this._bgColor));
		map.put("_frameColor", String.valueOf(this._frameColor));
		map.put("_timeLeftColor", String.valueOf(this._timeLeftColor));
		map.put("_timeSpentColor", String.valueOf(this._timeSpentColor));
		map.put("_gradient", String.valueOf(this._gradient));
		
		//SingleImg
		map.put("singleImgId", String.valueOf(-1));
		
		//SplitImg
		map.put("leftImgId", String.valueOf(-1));
		map.put("rightImgId", String.valueOf(-1));
		
		return map;
	}
}
