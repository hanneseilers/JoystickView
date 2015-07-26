package de.hanneseilers.android;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
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
	
	// Orientation Values
	public final static int ORIENTATION_NORTH = 0;
	public final static int ORIENTATION_NORTH_EAST = 1;
	public final static int ORIENTATION_EAST = 2;
	public final static int ORIENTATION_SOUTH_EAST = 3;
	public final static int ORIENTATION_SOUTH = 4;
	public final static int ORIENTATION_SOUT_WEST = 5;
	public final static int ORIENTATION_WEST = 6;
	public final static int ORIENTATION_NORTH_WEST = 7;
	
	// Attributes
	private boolean mShowOuterBorder, mShowCross, mStickUseGradient, mShowStickBorder;
	private boolean mInvertXAxis, mInvertYAxis;
	private float mOuterBorderWidth, mCrossWidth, mStickBorderWidth;
	private float mStickSize;
	
	private int mOuterBorderColor, mCrossColor;	
	private int mStickColor, mStickBorderColor, mStickInnerColor;
	private int mStickGradientOuterColor, mStickGradientInnerColor;
	private int mBackgroundColor;
	
	private int mBackgroundStyle;	
	
	// Drawing objects
	private Paint mPaintBackground;
	private Paint mPaintOuterBorder, mPaintStickBorder;
	private Paint mPaintCross;
	private Paint mPaintStickCircle, mPaintStickInnerCircle;
	
	// Dimensions
	private float mViewCenterX, mViewCenterY;
	private float mOuterBorderRadius;	
	private float[] mCrossLines;
	private float mStickCenterX, mStickCenterY;
	private float mStickRadius, mStickInnerCircleRadius;
	private Shader mStickCircleShader;
	
	// Active touch pointer
	private int mActivePointer = MotionEvent.INVALID_POINTER_ID;
	
	// Listeners
	

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
		final int vAction = MotionEventCompat.getActionMasked(event);
		int vPointerIndex;
		float xPos;
		float yPos;
		
		switch(vAction){
		case MotionEvent.ACTION_DOWN:			
			// get touch position
			vPointerIndex = MotionEventCompat.getActionIndex(event);
			xPos = MotionEventCompat.getX(event, vPointerIndex);
			yPos = MotionEventCompat.getY(event, vPointerIndex);
			
			// calculate distance to stick
			float vRadiusToStick = (float) Math.sqrt(
					Math.pow(xPos - mStickCenterX, 2)
					+ Math.pow(yPos - mStickCenterY, 2) );
			
			// check if touch is in stick radius (+10%)
			if( vRadiusToStick <= (mStickRadius + 0.1*mStickRadius) ){
				// set pointer active and move stick
				mActivePointer = MotionEventCompat.getPointerId(event, vPointerIndex);
				setStickCenter(xPos, yPos);
			}
			
			break;
			
		case MotionEvent.ACTION_MOVE:
			// check if move is active
			if( mActivePointer != MotionEvent.INVALID_POINTER_ID ){
				
				// get current pointer position and move stick
				vPointerIndex = MotionEventCompat.findPointerIndex(event, mActivePointer);
				xPos = MotionEventCompat.getX(event, vPointerIndex);
				yPos = MotionEventCompat.getY(event, vPointerIndex);
				
				setStickCenter(xPos, yPos);
				
			}
			break;
			
		case MotionEvent.ACTION_UP:
			mActivePointer = MotionEvent.INVALID_POINTER_ID;
			setStickCenter(mViewCenterX, mViewCenterY);
			break;
			
		case MotionEvent.ACTION_CANCEL:
			mActivePointer = MotionEvent.INVALID_POINTER_ID;
			break;
			
		case MotionEvent.ACTION_POINTER_UP:			
			vPointerIndex = MotionEventCompat.getActionIndex(event);
			
			if( MotionEventCompat.getPointerId(event, vPointerIndex) == mActivePointer ){			
				mActivePointer = MotionEvent.INVALID_POINTER_ID;
				setStickCenter(mViewCenterX, mViewCenterY);
			}
			break;
		}
		
		
		return true;
	}
	
	/**
	 * Sets the position of the stick.
	 * If the new stick position is outside the outer border circle,
	 * the position will be adjusted to be inside the outer border circle.
	 * @param centerX	X position of stick center.
	 * @param centerY	Y position of stick center.
	 */
	private void setStickCenter(float centerX, float centerY){
		// calculate radius to view center
		float vX = centerX - mViewCenterX;
		float vY = centerY - mViewCenterY;
		float vDist = (float) Math.sqrt(
				Math.pow(vX, 2) + Math.pow(vY, 2));
		
		// check if new stick position is outside cotrol radius
		if( vDist > mOuterBorderRadius ){
			// adjust position
			float vScale = mOuterBorderRadius / vDist;
			centerX = mViewCenterX + vScale * vX;
			centerY = mViewCenterY + vScale * vY;
		}
		
		// set new stick position
		mStickCenterX = centerX;
		mStickCenterY = centerY;
		invalidate();
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
	
	/**
	 * Interface for listening to {@link JoystickView} position changes.
	 * @author H. Eilers
	 *
	 */
	public interface JoystickPositionChangedListener{
		public void onJoyStickPositionChanged(float x, float y, int orientation);
	}

}
