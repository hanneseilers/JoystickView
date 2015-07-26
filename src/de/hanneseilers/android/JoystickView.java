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
	private boolean mInvertXAxis;
	private boolean mInvertYAxis;

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
			mInvertXAxis = vAttr.getBoolean(R.styleable.JoystickView_invertXAxis, false);
			mInvertYAxis = vAttr.getBoolean(R.styleable.JoystickView_invertYAxis, false);
			
		} finally {
			vAttr.recycle();
		}
	}

	public boolean isOuterBorder() {
		return mShowOuterBorder;
	}

	public void setShowOuterBorder(boolean aShowOuterBorder) {
		mShowOuterBorder = aShowOuterBorder;
		invalidate();
		requestLayout();
	}

	public float getOuterBorderWidth() {
		return mOuterBorderWidth;
	}

	public void setOuterBorderWidth(float aOuterBorderWidth) {
		mOuterBorderWidth = aOuterBorderWidth;
		invalidate();
		requestLayout();
	}

	public int getOuterBorderColor() {
		return mOuterBorderColor;
	}

	public void setOuterBorderColor(int aOuterBorderColor) {
		mOuterBorderColor = aOuterBorderColor;
		invalidate();
		requestLayout();
	}

	public boolean isCross() {
		return mShowCross;
	}

	public void setShowCross(boolean aShowCross) {
		mShowCross = aShowCross;
		invalidate();
		requestLayout();
	}

	public float getCrossWidth() {
		return mCrossWidth;
	}

	public void setCrossWidth(float aCrossWidth) {
		mCrossWidth = aCrossWidth;
		invalidate();
		requestLayout();
	}

	public int getCrossColor() {
		return mCrossColor;
	}

	public void setCrossColor(int aCrossColor) {
		mCrossColor = aCrossColor;
		invalidate();
		requestLayout();
	}

	public float getStickSize() {
		return mStickSize;
	}

	public void setStickSize(float aStickSize) {
		mStickSize = aStickSize;
		invalidate();
		requestLayout();
	}

	public int getStickColor() {
		return mStickColor;
	}

	public void setStickColor(int aStickColor) {
		mStickColor = aStickColor;
		invalidate();
		requestLayout();
	}

	public int getStickInnerColor() {
		return mStickInnerColor;
	}

	public void setStickInnerColor(int aStickInnerColor) {
		mStickInnerColor = aStickInnerColor;
		invalidate();
		requestLayout();
	}

	public boolean isUseGradient() {
		return mStickUseGradient;
	}

	public void setStickUseGradient(boolean aStickUseGradient) {
		mStickUseGradient = aStickUseGradient;
		invalidate();
		requestLayout();
	}

	public int getStickGradientOuterColor() {
		return mStickGradientOuterColor;
	}

	public void setStickGradientOuterColor(int aStickGradientOuterColor) {
		mStickGradientOuterColor = aStickGradientOuterColor;
		invalidate();
		requestLayout();
	}

	public int getStickGradientInnerColor() {
		return mStickGradientInnerColor;
	}

	public void setStickGradientInnerColor(int aStickGradientInnerColor) {
		mStickGradientInnerColor = aStickGradientInnerColor;
		invalidate();
		requestLayout();
	}

	public boolean isStickBorder() {
		return mShowStickBorder;
	}

	public void setShowStickBorder(boolean aShowStickBorder) {
		mShowStickBorder = aShowStickBorder;
		invalidate();
		requestLayout();
	}

	public float getStickBorderWidth() {
		return mStickBorderWidth;
	}

	public void setStickBorderWidth(float aStickBorderWidth) {
		mStickBorderWidth = aStickBorderWidth;
		invalidate();
		requestLayout();
	}

	public int getStickBorderColor() {
		return mStickBorderColor;
	}

	public void setStickBorderColor(int aStickBorderColor) {
		mStickBorderColor = aStickBorderColor;
		invalidate();
		requestLayout();
	}

}
