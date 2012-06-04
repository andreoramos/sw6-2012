package dk.aau.cs.giraf.launcher;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.os.Vibrator;
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
	
	private Intent mHomeIntent;
	private GButton mGLoginButton;
	private TextView mLoginNameView;
	private TextView mInfoView;
	private Context mContext;
	private Vibrator mVibrator;
	private Profile mPreviousProfile;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authentication);
		
		mContext = this;
		mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);		
		mGLoginButton = (GButton)this.findViewById(R.id.loginGButton);
		mLoginNameView = (TextView)this.findViewById(R.id.loginname);
		mInfoView = (TextView)this.findViewById(R.id.authentication_step1);
		

		mGLoginButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// If the authentication activity was not launched by the launcher...
				if (!getIntent().hasCategory("dk.aau.cs.giraf.launcher.GIRAF")) {
					Tools.attachLauncher(mContext); // should not be called
					Tools.saveLogInData(mContext, mPreviousProfile.getId());
					startActivity(mHomeIntent);
				} else {
					finish();
				}
			}
		});
		
		final ImageView instructImageView = (ImageView) findViewById(R.id.animation);
		instructImageView.setBackgroundResource(R.animator.instruct_ani);
		
		instructImageView.post(new Runnable(){
			@Override
			public void run(){
				AnimationDrawable anim = (AnimationDrawable) instructImageView.getBackground();
				anim.start();
			}
		});
	}

	/**
	 * Changes the color of the border around the camera feed of the QR code scanner.
	 * @param color The color which the border should have
	 */
	private void changeCamerafeedBorderColor(int color) {
		ViewGroup cameraFeedView = (ViewGroup)this.findViewById(R.id.camerafeed);
		
		RectF rectf = new RectF(10,10,10,10);
		RoundRectShape rect = new RoundRectShape( new float[] {15,15, 15,15, 15,15, 15,15}, rectf, null);
		ShapeDrawable shapeDrawable = new ShapeDrawable(rect);
		
		shapeDrawable.getPaint().setColor(color);
		cameraFeedView.setBackgroundDrawable(shapeDrawable);
	}

	/**
	 * Method which is ran every time the QR scanner scans a valid QR code.
	 * SuppressWarnings("unused") is used here, as eclipse cannot figure out by itself that this method is actually used.
	 * @param Result which the scanned string is saved in.
	 * @param A greyscale bitmap of the camera data which was decoded.
	 */
	@SuppressWarnings("unused")
	@Override
	public void handleDecode(Result rawResult, Bitmap barcode)
	{
		Helper helper = new Helper(this);
		Profile profile = helper.profilesHelper.authenticateProfile(rawResult.getText());

		/* If the scanned code is not a valid certificate which is
		 * attached to a profile, authenticateProfile() will return null,
		 * hence the null check here.
		 */
		if (profile != null) {	
			if (mPreviousProfile == null || !profile.toString().equals(mPreviousProfile.toString())) {
				mVibrator.vibrate(400);
			}
			mPreviousProfile = profile;
			
			this.changeCamerafeedBorderColor(0xFF3AAA35);
			mLoginNameView.setText(profile.getFirstname() + " " + profile.getSurname());
			mLoginNameView.setVisibility(View.VISIBLE);
			mGLoginButton.setVisibility(View.VISIBLE);
			mInfoView.setText(R.string.saadan);
			
			mHomeIntent = new Intent(AuthenticationActivity.this, HomeActivity.class);
			mHomeIntent.putExtra(Data.GUARDIANID, profile.getId());
		} else {
			this.changeCamerafeedBorderColor(0xFFFF0000);
			mGLoginButton.setVisibility(View.INVISIBLE);
			mLoginNameView.setVisibility(View.INVISIBLE);
			mInfoView.setText(R.string.authentication_step1);
		}
		
		/*
		 * Needed by ZXing in order to continuously scan QR codes, and not halt after first scan.
		 */
		this.getHandler().sendEmptyMessageDelayed(R.id.restart_preview, 500);
	}
}
