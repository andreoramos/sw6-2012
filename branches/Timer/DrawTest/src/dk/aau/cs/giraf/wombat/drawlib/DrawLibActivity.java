package dk.aau.cs.giraf.wombat.drawlib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import dk.aau.cs.giraf.TimerLib.Guardian;
import dk.aau.cs.giraf.TimerLib.SubProfile;


public class DrawLibActivity extends Activity {


	public static int frameHeight;
	public static int frameWidth;
	
	private Handler mHandler;
	private Runnable mRunnable;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		  View main_layout = findViewById(android.R.id.content).getRootView();
		  main_layout.setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
//		SubProfile sub = new ProgressBar("", "", 0xff3D3D3D, 0xff000066, 0xffB8B8B8, 0xff000000, 3, false);
//		SubProfile sub2 = new ProgressBar("", "", 0xff3D3D3D, 0xff000066, 0xffB8B8B8, 0xff000000, 900, true);
//		sub.setAttachment(sub2);
		Guardian guard = Guardian.getInstance();
		SubProfile sub = guard.getSubProfile();
		
		
		// Get display size
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display disp = wm.getDefaultDisplay();
		frameHeight = disp.getHeight();
		frameWidth = disp.getWidth();
				
		
		if (sub.getAttachment() == null) {
			/* Set the drawing class (which extends View) as the content view */
			View v = genDrawView(sub);
			v.setKeepScreenOn(true);
			setContentView(v);
		} else {
			
			LinearLayout frame = new LinearLayout(this);
			frame.setKeepScreenOn(true);
			View v = null;
			View v2 = null;
			ImageView i = null;
			ImageView i2 = null;
			ImageView i3 = null;
			switch(sub.getAttachment().getForm()){
			case Timer:
				frameWidth = frameWidth/2;
				 v = genDrawView(sub);
				frame.addView(v, frameWidth, frameHeight);
				v2 = genDrawView(sub.getAttachment().genSub());
				frame.addView(v2, frameWidth, frameHeight);
				break;
			case SingleImg:
				frameWidth = frameWidth/2;
				v = genDrawView(sub);
				frame.addView(v, frameWidth, frameHeight);
				
				i = new ImageView(this);
				i.setImageResource(sub.getAttachment().getImg().getPath());
				i.setBackgroundColor(sub.bgcolor);
				frame.addView(i, frameWidth, frameHeight);
				break;
			case SplitImg:
				frameWidth = frameWidth/2;
				v = genDrawView(sub);
				frame.addView(v, frameWidth, frameHeight);
				
				frameWidth = frameWidth/2;
				frameWidth = frameWidth - 15;
				i = new ImageView(this);
				i.setImageResource(sub.getAttachment().getLeftImg().getPath());
				i.setBackgroundColor(sub.bgcolor);
				frame.addView(i, frameWidth, frameHeight);
				
				i2 = new ImageView(this);
				i2.setImageResource(sub.getAttachment().getRightImg().getPath());
				i2.setBackgroundColor(sub.bgcolor);
				frame.addView(i2, frameWidth, frameHeight);
				
				i3 = new ImageView(this);
				i3.setBackgroundColor(sub.bgcolor);
				frame.addView(i3, 30, frameHeight);
				break;
			}
			
			setContentView(frame);	
		}
		
		mHandler = new Handler();
		
		mRunnable = new Runnable() {
            public void run() {
                final Intent mainIntent = new Intent(DrawLibActivity.this, DoneScreenActivity.class);
                //TODO: putExtra data with picture and text information
                DrawLibActivity.this.startActivity(mainIntent);
                DrawLibActivity.this.finish();
            }
        };
		
		mHandler.postDelayed(mRunnable, (sub.get_totalTime()+1)*1000);
	}
	
	public void onDestroy(){
		super.onDestroy();
		mHandler.removeCallbacks(mRunnable);
	}
	
	private View genDrawView(SubProfile sub) {
		switch (sub.formType()) {
		case ProgressBar:
			return new DrawProgressBar(getApplicationContext(), sub);
		case Hourglass:
			return new DrawHourglass(getApplicationContext(), sub);
		case DigitalClock:
			return new DrawDigital(getApplicationContext(), sub);
		case TimeTimer:
			return new DrawWatch(getApplicationContext(), sub);
		default:
			return null;
		}
	}
	
}
