package dk.aau.cs.giraf.parrot;



import parrot.Package.R;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.GridView;


public class PARROTActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
 	    /*

        */
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);	//TODO figure out what this does
        
        Tab tab = actionBar.newTab()
        		.setText(R.string.firstTab)		//TODO rename this tab
        		.setTabListener(new TabListener<SpeechBoardFragment>(this,"speechboard",SpeechBoardFragment.class));
        actionBar.addTab(tab);
        
        tab = actionBar.newTab()
        		.setText(R.string.secondTab)		//TODO rename this tab
        		.setTabListener(new TabListener<OptionsFragment>(this,"options",OptionsFragment.class));
        actionBar.addTab(tab);
       
    }
}