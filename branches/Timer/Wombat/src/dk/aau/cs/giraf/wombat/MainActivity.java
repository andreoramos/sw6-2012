package dk.aau.cs.giraf.wombat;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		// Set content view according to main, which implements two fragments
		setContentView(R.layout.main);
	}
}
