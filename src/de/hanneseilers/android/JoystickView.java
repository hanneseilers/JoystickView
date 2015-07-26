package de.hanneseilers.android;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import de.hanneseilers.joystick.R;

/**
 * Joystick View class.
 * Shows a joystick on screen.
 * @author H. Eilers
 *
 */
public class JoystickView extends View {
	
	// Attributes
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
	private int mBackgroundStyle;
	private int mBackgroundColor;
	
	// Drawing objects
	private Paint mPaintBackground;
	private Paint mPaintOuterBorder;
	private Paint mPaintStickBorder;
	private Paint mPaintCross;
	private Paint mPaintStickCircle;
	private Paint mPaintStickInnerCircle;
	
	// Dimensions
	private float mViewCenterX;
	private float mViewCenterY;
	private float mOuterBorderRadius;	
	private float[] mCrossLines;
	private float mStickCenterX;
	private float mStickCenterY;
	private float mStickRadius;
	private Shader mStickCircleShader;
	private float mStickInnerCircleRadius;
	
	// Gesture listening objects
	

	/**
	 * Constructor
	 * @param context	{@link Context}.
	 * @param attrs		{@link AttributeSet}.
	 */
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
			
			mStickSize = vAttr.getFloat(R.styleable.JoystickView_stickSize, 15.0f);
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
			
			mBackgroundStyle = vAttr.getInteger(R.styleable.JoystickView_backgroundStyle, 0);
			mBackgroundColor = vAttr.getColor(R.styleable.JoystickView_backgroundColor, Color.BLACK);
			
		} finally {
			vAttr.recycle();
		}
		
		// init drawing objects
		init();
	}
	
	/**
	 * Initiates drawing objects.
	 */
	private void init(){
		mPaintBackground = new Paint( Paint.ANTI_ALIAS_FLAG );
		mPaintBackground.setStyle( Style.FILL );
		mPaintBackground.setColor( mBackgroundColor );
		
		mPaintOuterBorder = new Paint( Paint.ANTI_ALIAS_FLAG );
		mPaintOuterBorder.setColor( mOuterBorderColor );
		mPaintOuterBorder.setStyle( Style.STROKE );
		mPaintOuterBorder.setStrokeWidth( mOuterBorderWidth );
		
		mPaintCross = new Paint( Paint.ANTI_ALIAS_FLAG );
		mPaintCross.setStrokeWidth( mCrossWidth );
		mPaintCross.setColor( mCrossColor );
		
		mPaintStickBorder = new Paint( Paint.ANTI_ALIAS_FLAG );
		mPaintStickBorder.setStyle( Style.STROKE );
		mPaintStickBorder.setColor( mStickBorderColor );
		mPaintStickBorder.setStrokeWidth( mStickBorderWidth );
		
		mPaintStickCircle = new Paint( Paint.ANTI_ALIAS_FLAG );
		mPaintStickCircle.setStyle( Style.FILL );
		mPaintStickCircle.setColor( mStickColor );
		
		mPaintStickInnerCircle = new Paint( Paint.ANTI_ALIAS_FLAG );
		mPaintStickInnerCircle.setStyle( Style.FILL );
		mPaintStickInnerCircle.setColor( mStickInnerColor );
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		// View
		mViewCenterX = w / 2.0f;
		mViewCenterY = h / 2.0f;
		
		int vShortestSide = w;
		if( w > h )
			vShortestSide = h;
		
		// Stick
		mStickCenterX = mViewCenterX;
		mStickCenterY = mViewCenterY;
		mStickRadius = vShortestSide * (mStickSize/100.0f);
		mStickInnerCircleRadius = mStickRadius * 0.6f;
		
		mStickCircleShader = new RadialGradient(
				mViewCenterX, mViewCenterY, mStickRadius,
				mStickGradientInnerColor, mStickGradientOuterColor,
				Shader.TileMode.MIRROR );
		mPaintStickCircle.setShader( mStickCircleShader );
		
		// Outer Border				
		mOuterBorderRadius = ((vShortestSide - getPaddingStart() - getPaddingEnd()) / 2.0f)
				- mStickRadius - mOuterBorderWidth;
		
		// Cross
		mCrossLines = new float[]{
				mViewCenterX - mOuterBorderRadius, mViewCenterY,
				mViewCenterX + mOuterBorderRadius, mViewCenterY,
				mViewCenterX, mViewCenterY + mOuterBorderRadius,
				mViewCenterX, mViewCenterY - mOuterBorderRadius
		};
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// Background
		if( mBackgroundStyle > 0 )
			canvas.drawCircle(mViewCenterX, mViewCenterY, mOuterBorderRadius, mPaintBackground);
		
		// Outer Border
		canvas.drawCircle(mViewCenterX, mViewCenterY, mOuterBorderRadius, mPaintOuterBorder);
		
		// Cross
		if( isCross() ){
			canvas.drawLines(mCrossLines, mPaintCross);
		}
		
		// Stick
		canvas.drawCircle(mStickCenterX, mStickCenterY, mStickRadius, mPaintStickCircle);
		canvas.drawCircle(mStickCenterX, mStickCenterY, mStickInnerCircleRadius, mPaintStickInnerCircle);
		if( isStickBorder() )
			canvas.drawCircle(mStickCenterX, mStickCenterY, mStickRadius, mPaintStickBorder);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {		
		// TODO
		return true;
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

	public boolean isInvertXAxis() {
		return mInvertXAxis;
	}

	public void setInvertXAxis(boolean aInvertXAxis) {
		mInvertXAxis = aInvertXAxis;
	}

	public boolean isInvertYAxis() {
		return mInvertYAxis;
	}

	public void setInvertYAxis(boolean aInvertYAxis) {
		mInvertYAxis = aInvertYAxis;
	}

}
