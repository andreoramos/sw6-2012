package dk.aau.cs.giraf.launcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import dk.aau.cs.giraf.launcher.R;
import dk.aau.cs.giraf.oasis.lib.Helper;
import dk.aau.cs.giraf.oasis.lib.models.App;
import dk.aau.cs.giraf.oasis.lib.models.Profile;

public class LogoActivity extends Activity {

	private Thread mLogoThread;
	private Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.logo);
	    
	    mContext = this.getApplicationContext();
	    
	    Helper helper = new Helper(mContext);
	    int size = helper.profilesHelper.getProfiles().size();
	    if (size <= 0) {
	    	helper.CreateDummyData();
	    }
	    
	    // Thread used to display the logo for a set amount of time.
	    mLogoThread = new Thread() {
	        @Override
	        public void run() {
	            try {
	            	synchronized(this) {
	            		wait(Data.TIME_TO_DISPLAY_LOGO);
	            	}
	            } catch(InterruptedException e) {}
	            finally {
	            	Intent intent;

	            	if (Tools.sessionExpired(mContext)) {
	            		intent = new Intent(mContext, AuthenticationActivity.class);
	            	} else {
	            		intent = new Intent(mContext, HomeActivity.class);
	            		
	            		SharedPreferences sharedPreferences = getSharedPreferences(Data.TIMERKEY, 0);
	            		long guardianID = sharedPreferences.getLong(Data.GUARDIANID, -1);
	            		
	            		/* Following did we not have time to test due to errors in the Oasislib */
	            		
//	            		if ((new Helper(mContext)).profilesHelper.getProfileById(guardianID) != null) {
	            			intent.putExtra(Data.GUARDIANID, guardianID);
//	            		} else {
//	            			intent = new Intent(mContext, AuthenticationActivity.class);
//	            		}
	            	}
	            	
	                startActivity(intent);
	                stop();
	                finish();
	            }
	        }
	    };
	    
	    this.setOrientation();
	    
	    mLogoThread.start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    if (event.getAction() == MotionEvent.ACTION_DOWN) {
	    	synchronized(mLogoThread){
	    		mLogoThread.notifyAll();
	    	}
	    }
	    return true;
	}
	
	/**
	 * Sets the orientation based on current session information. If session has expired, then switch to portrait, else go to landscape.
	 * This is make the flow more consistent with the screens which the user will be greeted with later. (homescreen = landscape, authentication = portrait)
	 */
	private void setOrientation() {
		if (Tools.sessionExpired(mContext)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}
}