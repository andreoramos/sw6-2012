package dk.aau.cs.giraf.launcher;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivity;

import dk.aau.cs.giraf.gui.GButton;
import dk.aau.cs.giraf.launcher.R;
import dk.aau.cs.giraf.oasis.lib.Helper;
import dk.aau.cs.giraf.oasis.lib.models.Profile;

public class AuthenticationActivity extends CaptureActivity {

	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.authentication);
	        
	        final ImageView instruct = (ImageView) findViewById(R.id.animation);
	        instruct.setBackgroundResource(R.animator.instruct_ani);
	        instruct.post(new Runnable(){
	        	@Override
	        	public void run(){
	        		AnimationDrawable anim = (AnimationDrawable) instruct.getBackground();
	        		anim.start();
	        	}
	        });
	        
	        final GButton button = (GButton) findViewById(R.id.loginGButton);
	        button.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	Intent intent = new Intent(AuthenticationActivity.this, HomeActivity.class);
	            	startActivity(intent);
	            }
	        });
	    }
	 
	 	private void changeCamerafeedBorderColor(int color) {
	 		ViewGroup cameraFeedView = (ViewGroup)this.findViewById(R.id.camerafeed);
    		RectF rectf = new RectF(10,10,10,10);
    		RoundRectShape rect = new RoundRectShape( new float[] {15,15, 15,15, 15,15, 15,15}, rectf, null); // 15,15, 15,15, 15,15, 15,15 // 30,30, 30,30, 30,30, 30,30
    		ShapeDrawable bg = new ShapeDrawable(rect);
    		bg.getPaint().setColor(color);
    		cameraFeedView.setBackgroundDrawable(bg);
	 	}
	    
	    @SuppressWarnings("unused")
		@Override
	    public void handleDecode(Result rawResult, Bitmap barcode)
	    {
	    	Helper helper = new Helper(this);
	    	Profile profile = helper.profilesHelper.authenticateProfile(rawResult.getText());
	    	Profile drazenko = new Profile("Drazenko","Banjak","middlename",0,45454545,"",null);
	    	
	    	//if (rawResult.getText().equals("GIRAFPROFILE")) {
	    	if (true) {
	    	//if (profile != null) {	
	    		this.changeCamerafeedBorderColor(0xFF3AAA35);
	    		//ProgressDialog dialog = ProgressDialog.show(AuthenticationActivity.this, "", "Hej: " + rawResult.getText(), true, true);
	    		((TextView)this.findViewById(R.id.loginname)).setText(drazenko.getFirstname() + " " + drazenko.getSurname());
	    		((GButton)this.findViewById(R.id.loginGButton)).setVisibility(View.VISIBLE);
	    	} else {
	    		this.changeCamerafeedBorderColor(0xFFFF0000);
	    		//ProgressDialog dialog = ProgressDialog.show(AuthenticationActivity.this, "", "INVALID profile: " + rawResult.getText(), true, true);
	    	}
	    	//ProgressDialog dialog = ProgressDialog.show(AuthenticationActivity.this, "", "Ø" + rawResult.getText() + "Ø", true, true);
	    
	    this.getHandler().sendEmptyMessageDelayed(R.id.restart_preview, 500);
	    }

}
