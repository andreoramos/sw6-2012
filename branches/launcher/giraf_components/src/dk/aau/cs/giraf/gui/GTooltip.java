package dk.aau.cs.giraf.gui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class GTooltip extends Dialog {
	
	private GTooltip mTip;
	private Context mContext;
	private final int padding = 5;

	public GTooltip(Context context) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		mContext = context;
		this.setStyle();
		mTip = this;
		this.findViewById(R.id.tooltip_hitarea).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mTip.cancel();
			}
		});

	}

	private GTooltip(Context context, int theme) {
		super(context, theme);
		mContext = context;
		this.setStyle();
		// TODO Auto-generated constructor stub
	}

	private GTooltip(Context context, boolean cancelable,
			OnCancelListener cancelListener, View v) {
		super(context, cancelable, cancelListener);
		mContext = context;
		this.setStyle();
		// TODO Auto-generated constructor stub
	}
	
	public void setStyle() {
		this.setContentView(R.layout.gtooltip_layout);
	}
	
	public void setRightOf(View v) {
		View target = this.findViewById(R.id.gtooltip_arrow_view);	
		int newLeft = intToDP(v.getLeft() + v.getWidth() + padding);
		int newTop = intToDP(v.getTop() + (v.getHeight()/2) - 12);
	
		android.widget.RelativeLayout.LayoutParams p = (android.widget.RelativeLayout.LayoutParams) target.getLayoutParams();
		p.setMargins(newLeft, newTop, 0, 0);
		target.setLayoutParams(p);
		
//		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		
//		RelativeLayout contentLayout = (RelativeLayout)this.findViewById(R.id.tooltip_content);
//		contentLayout.setLayoutParams(params);
	}
	
	private int intToDP(int i) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, mContext.getResources().getDisplayMetrics());
	}

	public void addView(View v){
		ViewGroup target = (ViewGroup) this.findViewById(R.id.tooltip_content);
		target.addView(v);
		
		RelativeLayout.LayoutParams lp = (LayoutParams) target.findViewById(R.id.tooltip_content).getLayoutParams();
		int y = -(12 - (target.getHeight()/2)); 
		lp.setMargins(0, y, 0, 0);
	}
}
