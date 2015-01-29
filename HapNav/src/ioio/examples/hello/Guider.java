package ioio.examples.hello;

import android.content.Context;
import android.content.Intent;

public class Guider {

	static String maneuver;
	private static Context mContext;

	public static String sendSignal(String val) {
		if (val == "turn-left") {
			maneuver = "left";
			//s.setLeftCycle(1000);
		} else if (val == "turn-right") {
			maneuver = "right";
			//s.setRightCycle(1000);
		} else {
			maneuver = "nothing";
		}
		return maneuver;
	}

	private Context getApplicationContext() {
		return mContext;
	}
}
