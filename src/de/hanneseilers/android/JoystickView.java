package de.hanneseilers.android;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import de.hanneseilers.joystick.R;

/**
 * Joystick View class.
 * Shows a joystick on screen.
 * @author H. Eilers
 *
 */
public class JoystickView extends View {
	
	private boolean mShowOuterBorder;
	private float mOuterBorderWidth;
	private int mOuterBorderColor;
	private boolean mShowCross;
	private float mCrossWidth;
	private int mCrossColor;
	private float mStickSize;
	private int mStickColor;
	private int mStickInnerColor;
	private boolean mStickUseGradient;
	private int mStickGradientOuterColor;
	private int mStickGradientInnerColor;
	private boolean mShowStickBorder;
	private float mStickBorderWidth;
	private int mStickBorderColor;

	public JoystickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		// get attributes
		TypedArray vAttr = context.getTheme().obtainStyledAttributes(
				attrs, R.styleable.JoystickView, 0 , 0);
		
		try{
			
			mShowOuterBorder = vAttr.getBoolean(R.styleable.JoystickView_showOuterBorder, true);
			mOuterBorderWidth = vAttr.getDimension(R.styleable.JoystickView_outerBorderWidth, 1.5f);
			mOuterBorderColor = vAttr.getColor(R.styleable.JoystickView_outerBorderColor, Color.DKGRAY);
			mShowCross = vAttr.getBoolean(R.styleable.JoystickView_showCross, true);
			mCrossWidth = vAttr.getDimension(R.styleable.JoystickView_crossWidth, 1.0f);
			mCrossColor = vAttr.getColor(R.styleable.JoystickView_crossColor, Color.DKGRAY);
			mStickSize = vAttr.getDimension(R.styleable.JoystickView_stickSize, 5.0f);
			mStickColor = vAttr.getColor(R.styleable.JoystickView_stickColor, Color.LTGRAY);
			mStickInnerColor = vAttr.getColor(R.styleable.JoystickView_stickInnerColor, Color.GRAY);
			mStickUseGradient = vAttr.getBoolean(R.styleable.JoystickView_stickUseGradient, false);
			mStickGradientInnerColor = vAttr.getColor(R.styleable.JoystickView_stickInnerColor, Color.LTGRAY);
			mStickGradientOuterColor = vAttr.getColor(R.styleable.JoystickView_stickGradientOuterColor, Color.GRAY);
			mShowStickBorder = vAttr.getBoolean(R.styleable.JoystickView_showStickBorder, true);
			mStickBorderWidth = vAttr.getDimension(R.styleable.JoystickView_stickBorderWidth, 1.0f);
			mStickBorderColor = vAttr.getColor(R.styleable.JoystickView_stickBorderColor, Color.DKGRAY);
			
		} finally {
			vAttr.recycle();
		}
	}

}
