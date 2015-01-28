package ioio.examples.hello;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONObject;

import ioio.examples.hello.R;
//import ioio.examples.hello.turnService;
import ioio.examples.hello.R.id;
import ioio.examples.hello.R.layout;
import ioio.examples.hello.HelloIOIOService.MyLocalBinder;
//import ioio.examples.hello.turnService.MyLocalBinder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import android.content.ServiceConnection;


public class Main extends Activity {

	Guider guide;

	static String turnIs;
	static String latiIs;
	static String longIs;

	boolean isBound = false;
	HelloIOIOService myService;

	// hiding soft keyboard on touch
	public void hideSoftKeyboard(View v) {
		final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Creating Service Elements
		startService(new Intent(this, HelloIOIOService.class));
		
		// UI element references from main_activity in layout
		final WebView itinerary = (WebView) findViewById(R.id.itinerary);
		final RelativeLayout itineraryLayout = (RelativeLayout) findViewById(R.id.itineraryLayout);
		final Button createRouteButton = (Button) findViewById(R.id.createRouteButton);
		final Button showItineraryButton = (Button) findViewById(R.id.showItineraryButton);
		final Button clearButton = (Button) findViewById(R.id.clearButton);
		final RelativeLayout showlog = (RelativeLayout) findViewById(R.id.showlog);

		// The itinerary listener
		showItineraryButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				itineraryLayout.setVisibility(View.VISIBLE);
				Toast.makeText(getApplicationContext(), "Button Clicked",
						Toast.LENGTH_LONG).show();
			}
		});

		// The clear route listener
		clearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clearButton.setVisibility(View.VISIBLE);
				showItineraryButton.setVisibility(View.VISIBLE);
				itineraryLayout.setVisibility(View.VISIBLE);
			}
		});

		// The onclick listener for the address text
		createRouteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				createRouteButton.setEnabled(false);
				hideSoftKeyboard(view);

				// changing input address to string for the creation of route
				EditText start = (EditText) findViewById(R.id.startTextView);
				String startAt = start.getText().toString();
				EditText end = (EditText) findViewById(R.id.endTextView);
				String endAt = end.getText().toString();

				try {
					Router router = new Router(startAt, endAt);
					ArrayList<Map<String, String>> tmp = router.getRoute();

					System.out.println("::HN:: Created router and called API" );
					System.out.println("::HN:: size=" + tmp.size());

					for (int i = 0; i < tmp.size(); i++) {
						try {
							Thread.sleep(5000);
							System.out.println("::HN:: Iteration " + i);
							System.out.println("::HN:: Turn Type= " + tmp.get(i).get("TurnType"));
							turnIs = tmp.get(i).get("TurnType");
							latiIs = tmp.get(i).get("Latitude");
							longIs = tmp.get(i).get("Longitude");
							
							if (turnIs.equals("turn-left")) {
								myService.setTest(true);
								System.out.println("::HN:: service=" + myService);

							} else if (turnIs.equals("turn-right")) {
								myService.setTestOne(true);
								System.out.println("::HN:: service=" + myService);
							}
							//Guider.sendSignal(turnIs);
						} catch (Exception e) {

						}
					}

				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	public void Guidance() {
		LocationListener locationListener = new MyLocationListener();
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 35, 10,
				locationListener);

	}

	//Service stuff:
	private ServiceConnection myConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder service) {
			MyLocalBinder binder = (MyLocalBinder) service;
			myService = binder.getService();
			isBound = true;
		}

		public void onServiceDisconnected(ComponentName arg0) {
			myService = null;
			isBound = false;
		}
	};

	@Override
	protected void onStart() {
		super.onStart();
		// Bind to LocalService
		doBindToService();
		System.out.println("::HN:: Binded to Local Service");

	}

	@Override
	protected void onStop() {
		super.onStop();
		// Unbind from the service

		doUnbindService();

	}

	private void doBindToService() {

		if (!isBound) {
			Intent bindIntent = new Intent(this, HelloIOIOService.class);
			isBound = bindService(bindIntent, myConnection,
					Context.BIND_AUTO_CREATE);
		}
	}

	private void doUnbindService() {
		unbindService(myConnection);
		isBound = false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Intent stopServiceIntent = new Intent(this, HelloIOIOService.class);
		stopService(stopServiceIntent);

	}
	
}
