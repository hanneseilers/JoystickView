# JoystickView
Joystick View for Android Applications

# Installation

## Android Studio
Import JoystickView as module and add it to the main modules dependencies.

## Eclipse
Copy JoystickView.java class file and attrs.xml rescource file into your Android project.

# Usage
See test-app branch for example how to use application.
Baisically implement JoystickView.OnPositionChangedListener and register to JoystickViews position changes using follwogin callback:

    public void onJoyStickPositionChanged(float x, float y, StickOrientation orientation)

