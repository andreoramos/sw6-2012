package dk.aau.cs.giraf.launcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import dk.aau.cs.giraf.launcher.R;
import dk.aau.cs.giraf.oasis.lib.Helper;

public class LogoActivity extends Activity {

	protected int _splashTime = 400; 
	private Thread splashTread;
	private Context mContext;
	private Activity mActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.logo);

	    mActivity = this;
	    
	    mContext = this.getApplicationContext();
	    
	    Helper helper = new Helper(this);
	    int size = helper.profilesHelper.getProfiles().size();
	    if (size <= 0) {
	    	helper.CreateDummyData();
	    }
	    
	    Tools.updateGirafApps_DB(mContext);
	    
	    splashTread = new Thread() {
	        @Override
	        public void run() {
	            try {
	            	synchronized(this) {
	            		wait(_splashTime);
	            	}
	            } catch(InterruptedException e) {}
	            finally {
	            	Intent i;

	            	if (Tools.AuthRequired(mContext)) {
	            		i = new Intent(mContext, AuthenticationActivity.class);
	            	} else {
	            		i = new Intent(mContext, HomeActivity.class);
	            		
	            		SharedPreferences sp = getSharedPreferences(Tools.TIMERKEY, 0);
	            		i.putExtra(Tools.GUARDIANID, sp.getLong(Tools.GUARDIANID, -1));
	            		i.putExtra(Tools.SKIP, true);
	            	}
	            	
	                startActivity(i);
	                stop();
	                finish();
	            }
	        }
	    };
	    
	    splashTread.start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    if (event.getAction() == MotionEvent.ACTION_DOWN) {
	    	synchronized(splashTread){
	    		splashTread.notifyAll();
	    	}
	    }
	    return true;
	}
}