package de.hanneseilers.joystickview.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import de.hanneseilers.joystickview.JoystickView;
import de.hanneseilers.joystickview.JoystickView.OnPositionChangedListener;
import de.hanneseilers.joystickview.JoystickView.StickOrientation;
import de.hanneseilers.joystickview.R;

public class TestActivity extends AppCompatActivity  implements OnPositionChangedListener {

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
