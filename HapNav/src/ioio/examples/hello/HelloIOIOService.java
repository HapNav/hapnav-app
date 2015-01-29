package ioio.examples.hello;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOService;

/**
 * An example IOIO service. While this service is alive, it will attempt to
 * connect to a IOIO and blink the LED. A notification will appear on the
 * notification bar, enabling the user to stop the service.
 */
public class HelloIOIOService extends IOIOService {
	
	private static final int PIN_left = 7;
	private static final int PIN_right = 12;

	private boolean left_Buzzer = false;
	private boolean right_Buzzer = false;
	
	public void setTestOne(boolean b){
		System.out.println("::HN:: Sytem set Right = " + b);
		this.right_Buzzer = b;
	}
	
	public void setTest(boolean b){
		System.out.println("::HN:: System set Left = " + b);
		this.left_Buzzer = b;
	}
	
	@Override
	protected IOIOLooper createIOIOLooper() {

		System.out.println("::HN:: Looper being Created");

		return new BaseIOIOLooper()
		{
			private DigitalOutput led_;
			private DigitalOutput left;
			private DigitalOutput right;

			@Override
			protected void setup() throws ConnectionLostException,InterruptedException {
				led_ = ioio_.openDigitalOutput(IOIO.LED_PIN,true);
				left = ioio_.openDigitalOutput(PIN_left,false);
				right = ioio_.openDigitalOutput(PIN_right,false);
			}

			@Override
			public void loop() throws ConnectionLostException,InterruptedException {
				led_.write(true);
				
				if (left_Buzzer) {
					left.write(true);
					left_Buzzer= false;
				}
				
				if (right_Buzzer) {
					right.write(true);
					right_Buzzer = false;
				}
				
				Thread.sleep(1500);
				led_.write(false);
				left.write(false);
				right.write(false);
				Thread.sleep(1500);
			}
			
		};
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return myBinder;
	}
	
	private final IBinder myBinder = new MyLocalBinder();
	
	public class MyLocalBinder extends Binder {
        HelloIOIOService getService( ) {
            return HelloIOIOService.this;
        }
	}
}
