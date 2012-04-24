package dk.aau.cs.giraf.wombat.drawlib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import dk.aau.cs.giraf.TimerLib.SubProfile;

public class DrawWatch extends View {
	private SubProfile sp;

	private int background;
	private int frame;
	private int timeleft;
	private int timeleft2;
	private int timespent;
	private int totalTime;
	private double endTime;

	private double rotation;

	private Paint paint = new Paint();
	private Rect r;
	private ColorDrawable col;

	private int frameWidth;
	private int frameHeight;

	private int width;
	private int height;
	private int left;
	private int right;
	private int top;
	private int bottom;

	public DrawWatch(Context context, SubProfile sub) {
		super(context);

		/* Get the window hight assigned by the draw activity */
		frameWidth = DrawLibActivity.frameWidth;
		frameHeight = DrawLibActivity.frameHeight;

		if (frameWidth > frameHeight)
			width = (int) (frameHeight / 1.5);
		else
			width = (int) (frameWidth / 1.5);
		height = width;

		sp = sub;

		background = sp.bgcolor;
		frame = sp.frameColor;
		timeleft = sp.timeLeftColor;
		timeleft2 = sp.timeLeftColor;
		timespent = sp.timeSpentColor;
		totalTime = (sp.get_totalTime() - 1) * 1000;
		endTime = System.currentTimeMillis() + totalTime;

		if (sp.gradient) {
			timeleft2 = timespent;
			timespent = background;
		}
	}

	@Override
	protected void onDraw(Canvas c) {
		super.onDraw(c);
		
		double timenow = (endTime - System.currentTimeMillis());

		
		/* Fill the canvas with the background color */
		paint.setColor(background);
		c.drawPaint(paint);

		/* Draw the frame of the progressbar */
		paint.setAntiAlias(true);
		paint.setColor(frame);

		left = frameWidth / 2;
		top = frameHeight / 2;

		/* Draw the outer circle/border */
		c.drawCircle(left, top, width / 2, paint);

		/* Draw the inner circle */
		paint.setColor(timespent);
		c.drawCircle(left, top, (width / 2) - 3, paint);

		left = ((frameWidth - width) / 2) + 3;
		right = (((frameWidth - width) / 2) + width) - 3;
		top = ((frameHeight - height) / 2) + 3;
		bottom = (((frameHeight - height) / 2) + height) - 3;


		double percent = timenow / (totalTime/1000);
		
		// 0.1 is what 1 second corresponds to in degrees
		rotation = (0.1 * (endTime - System.currentTimeMillis()) / 1000) + 0.999;

		// Draw the timer
		paint.setColor(timeleft2);
		RectF rf = new RectF(left, top, right, bottom);
		c.drawArc(rf, 270 - (int) rotation, (int) rotation, true, paint);

		// Draw the timer gradient
		col = new ColorDrawable(timeleft);
		col.setAlpha((int) (255 * percent));
		paint.setColor(col.getColor());
		c.drawArc(rf, 270 - (int) rotation, (int) rotation, true, paint);

		/* Draw the center */
		paint.setColor(frame);
		c.drawCircle(frameWidth/2, frameHeight/2, 8, paint);
		
		/* Draw the indicators 0, 3, 6, 9 */
		r = new Rect(frameWidth / 2 - 4, ((frameHeight - height) / 2) + 15,
				frameWidth / 2 + 4, ((frameHeight - height) / 2) + 15 + 40);

		for (int i = 0; i < 4; i++) {
			c.rotate(90, frameWidth / 2, frameHeight / 2);
			c.drawRect(r, paint);
		}

		/* Draw the small indicators */
		r = new Rect(frameWidth / 2 - 2, ((frameHeight - height) / 2) + 17,
				frameWidth / 2 + 2, ((frameHeight - height) / 2) + 15 + 35);

		for (int i = 0; i < 12; i++) {
			c.rotate(30, frameWidth / 2, frameHeight / 2);
			c.drawRect(r, paint);
		}

		if (endTime >= System.currentTimeMillis()) {
			/*************** IMPORTANT ***************/
			/* Recalls Draw! */
			invalidate();
		}
	}
}
