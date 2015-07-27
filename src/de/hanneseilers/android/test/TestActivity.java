package de.hanneseilers.android.test;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import de.hanneseilers.android.JoystickView;
import de.hanneseilers.android.JoystickView.OnPositionChangedListener;
import de.hanneseilers.android.JoystickView.StickOrientation;
import de.hanneseilers.joystick.R;

public class TestActivity extends Activity implements OnPositionChangedListener {
	
	private JoystickView mJoystick;
	private TextView txtXPos, txtYPos, txtOrientation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.example_layout);
		
		mJoystick = (JoystickView) findViewById(R.id.joystick);
		txtXPos = (TextView) findViewById(R.id.txtXPos);
		txtYPos = (TextView) findViewById(R.id.txtYPos);
		txtOrientation = (TextView) findViewById(R.id.txtOrientation);
		
		mJoystick.setOnPositionChangedListener(this);
		onJoyStickPositionChanged(mJoystick.getXPosition(), mJoystick.getYPosition(), mJoystick.getOrientation());
	}

	@Override
	public void onJoyStickPositionChanged(float x, float y, StickOrientation orientation) {
		final float xPos = x;
		final float yPos = y;
		final StickOrientation vOrientation = orientation;
		
		runOnUiThread( new Runnable() {			
			@Override
			public void run() {
				txtXPos.setText( "X: " + xPos );
				txtYPos.setText( "Y: " + yPos );
				txtOrientation.setText( "Orientation: " + vOrientation );
			}
		} );
		
	}
}
