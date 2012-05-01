package dk.aau.cs.giraf.wombat;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import dk.aau.cs.giraf.TimerLib.Guardian;

public class MainActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		long guardianId;
		long childId;
		int color;

		Bundle extras = getIntent().getExtras();
        if (extras != null) {        	
        	guardianId = extras.getLong("currentGuardianID");
        	childId = extras.getLong("currentChildID");
        	color = 0xFFFFBB55;
        } else {
        	guardianId = -1;
        	childId = -1;
        	color = 0xFFFFBB55;
        }

    	Guardian.getInstance(childId, guardianId, getApplicationContext());
    	
		// Set content view according to main, which implements two fragments
		setContentView(R.layout.main);
		
		Drawable d = getResources().getDrawable(R.drawable.background);
		d.setColorFilter(color, PorterDuff.Mode.OVERLAY);
		findViewById(R.id.mainLayout).setBackgroundDrawable(d);
	}
}
