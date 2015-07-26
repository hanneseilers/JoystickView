package de.hanneseilers.android.test;

import android.app.Activity;
import android.os.Bundle;
import de.hanneseilers.joystick.R;

public class TestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.example_layout);
	}
}
