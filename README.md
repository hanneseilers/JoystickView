# Android JoystickView
This custom Android View implements a simple joystick widget that allows dragging a stick inside a circle area. The joystick can be customized in case of optical and internal behaviour using xml attributes inside Android layout files.

Installation
------------
Clone this project and reference it inside you project. Include the view inside your layout xml file:

    <de.hanneseilers.android.JoystickView
            android:id="@+id/joystick"
            android:layout_width="wrap_content"
            android:layout_height="match_parent" />

You can set several attributes to influence joysticks behaviour and look. For that include xml namespace at layouts root element.

    xmlns:joystick="http://schemas.android.com/apk/res/de.hanneseilers.joystick"

Now you can use the namespace to set joystick attributes:

    <de.hanneseilers.android.JoystickView
            android:id="@+id/joystick"
            android:layout_width="wrap_content"
            android:layout_height="match_parent"
            joystick:showOuterBorder=true />

For more information about available attributes see Attributes section in this readme.
To acces the joystick inside your activity, implement a OnPositionChangedListener:

    public void onJoyStickPositionChanged(float x, float y, StickOrientation orientation);

It notifies you if the position of the joystick changed. The x and y values will be in the range of -100% to 100% in relation to the joystick views center. The orientation will be NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST or NONE (if stick in center of joystick view).

Example
-------
This project includes an example Android application. Remove the IsLibrary flag inside project properties to start the test activity.

Attributes
----------
You can change attributes of the JoystickView using xml inside your layout file (recommended) or using JoystickView setter functions.

* showOuterBorder: Set true to show an outer border around the joystick circle area (default: true)
* outerBorderWidth: Outer border size (default: 1.5px)
* outerBorderColor: Color of outer border (default: Color.DKGRAY)
* showCross: Set true to show a cross inside the joystick circle area (default: true)
* crossWidth: Width of the cross lines (default: 1.0px)
* crossColor: Color of the corss lines (default: Color.DKGRAY)
* stickSize: Float value of the views stick width/diameter in percent of the views width (default: 15.0)
* stickColor: Color if stick (if no gradient used, default: Color.LTGRAY)
* mStickInnerColor: Color of the sticks inner circle (default: Color.GRAY)
* mStickUseGradient: Set true to use a radial gradient of two color instead of a simple sticks color (default: false)
* mStickGradientInnerColor: Inner color of stick radial gradient (default: Color.LTGRAY)
* mStickGradientOuterColor: Outer color if stick radial gradient (default: Color.GRAY)
* mShowStickBorder: Set true to show a border around the views stick (default: true)
* mStickBorderWidth: Width of the sticks border (default: 1.0)
* mStickBorderColor: Color if sticks border (default: Color.DKGRAY)
* invertXAxis: Invert direction of X-Axis. Normal direction is from left to right (default: false)
* invertYAxis: invert direction of Y_Axis. Normal direction is from bottom to top (default: false)
* backgroundStyle: Background of views circle area. Could be none or fill (default: none)
* backgroundColor: Background color if views circle area (default: Color.BLACK)
* positionPrecision: Integer of internal decimal places for precision of stick position (default: 2)
