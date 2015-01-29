package ioio.examples.hello;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

public class Router {
	
	String json_response;
	ArrayList<Map<String,String>> parsed;

	public Router(String origin, String destination) throws ClientProtocolException, IOException {
		// Set up data structure and source/destination locations
		parsed = new ArrayList<Map<String,String>>();
		origin = URLEncoder.encode(origin, "UTF-8");
		destination = URLEncoder.encode(destination, "UTF-8");
		
		// Grab the API url and prepare it for reading
		String url_encode = "http://maps.googleapis.com/maps/api/directions/json?origin=" + origin + "&destination=" + destination + "&mode=bicycling";		URL url = new URL(url_encode);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

		try {
			// Start reading the JSON output from the API call
			BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			this.json_response = response.toString();
			this.parseJSON();
		} finally {
			urlConnection.disconnect();
		}
	}
	
	public ArrayList<Map<String,String>> getRoute() {
		return parsed;
	}
	
	private void parseJSON() {
		try {
			JSONObject obj = new JSONObject(json_response);
		    JSONArray arr = obj.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
			for (int i = 0; i < arr.length(); i++) {
				Map<String,String> tmp = new HashMap<String,String>();
				try {
					tmp.put("TurnType",arr.getJSONObject(i).getString("maneuver"));
					
				} catch (Exception e) {
					tmp.put("TurnType","straight");
				}

				Double tmp1 = arr.getJSONObject(i).getJSONObject("end_location").getDouble("lat");
				Double tmp2 = arr.getJSONObject(i).getJSONObject("end_location").getDouble("lng");
				tmp.put("Latitude",tmp1.toString());
				tmp.put("Longitude",tmp2.toString());
			    parsed.add(i, tmp);
			}
		} catch (Exception e) {
			System.out.println(":HN: EXCEPTION SHIIIIIIIIT! " + e.toString());
		}
	}

}
