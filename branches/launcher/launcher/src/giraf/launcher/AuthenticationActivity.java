package giraf.launcher;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivity;

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
	    }
	 
	 	private void changeCamerafeedBorderColor(int color) {
	 		ViewGroup cameraFeedView = (ViewGroup)this.findViewById(R.id.camerafeed);
    		//Drawable borderDrawable = cameraFeedView.getBackground();
    		//ShapeDrawable solid = (ShapeDrawable)this.findViewById(R.id.camerabordersolid);
    		//ShapeDrawable border = new ShapeDrawable();
    		//border.setPadding(10,10,10,10);
    		RectF rectf = new RectF(10,10,10,10);
    		RoundRectShape rect = new RoundRectShape( new float[] {15,15, 15,15, 15,15, 15,15}, rectf, null); // 15,15, 15,15, 15,15, 15,15 // 30,30, 30,30, 30,30, 30,30
    		ShapeDrawable bg = new ShapeDrawable(rect);
    		bg.getPaint().setColor(color);
    		//bg.getPaint().setColor(0x99FFFFFF);
    		//bg.getPaint().setColor(0xFF2FE449);
    		cameraFeedView.setBackgroundDrawable(bg);
	 	}
	    
	    @SuppressWarnings("unused")
		@Override
	    public void handleDecode(Result rawResult, Bitmap barcode)
	    {
	    	Helper helper = new Helper(this);
	    	Profile profile = helper.profilesHelper.authenticateProfile(rawResult.getText());
	    	if (profile != null) {
	    		this.changeCamerafeedBorderColor(0x992FE449);
	    		ProgressDialog dialog = ProgressDialog.show(AuthenticationActivity.this, "", "Hej: " + rawResult.getText(), true, true);
	    	} else {
	    		ProgressDialog dialog = ProgressDialog.show(AuthenticationActivity.this, "", "INVALID profile: " + rawResult.getText(), true, true);

	    		this.changeCamerafeedBorderColor(0x99E92A2A);
	    	}
	    	
	    
	    this.getHandler().sendEmptyMessageDelayed(R.id.restart_preview, 500);
	    }

}
