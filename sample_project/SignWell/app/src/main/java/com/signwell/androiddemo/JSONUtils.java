//
// SignWell
// Android Demo
// Andrey Butov, 2023
//


package com.signwell.androiddemo;


import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public final class JSONUtils
{
	static public HashMap<String, Object> jsonToHashMap(JSONObject json) {
		try {
			HashMap<String, Object> hashMap = new HashMap<>();
			Iterator<String> keysItr = json.keys();
			while( keysItr.hasNext() ) {
				String key = keysItr.next();
				Object val = json.get(key);
				if ( val instanceof JSONArray ) {
					val = jsonToArray((JSONArray)val);
				}
				else if ( val instanceof JSONObject ) {
					val = jsonToHashMap((JSONObject)val);
				}
				hashMap.put(key, val);
			}
			return hashMap;
		} catch ( Exception e ) {
			Log.e(App.TAG, e.getLocalizedMessage());
		}
		return new HashMap<>();
	}


	static public ArrayList<Object> jsonToArray(JSONArray jsonArray)
	{
		try
		{
			ArrayList<Object> arrayList = new ArrayList<>();

			for ( int i = 0; i < jsonArray.length(); i++)
			{
				Object val = jsonArray.get(i);

				if ( val instanceof JSONArray ) {
					val = jsonToArray((JSONArray)val);
				}
				else if ( val instanceof JSONObject ) {
					val = jsonToHashMap((JSONObject)val);
				}

				arrayList.add(val);
			}

			return arrayList;
		}
		catch ( Exception e )
		{
			Log.e(App.TAG, e.getLocalizedMessage());
		}

		return new ArrayList<>();
	}


	static public JSONObject jsonObjectFromBytes(byte[] bytes) {
		JSONObject obj = null;
		try {
			obj = new JSONObject(new String(bytes));
		} catch ( Exception e ) {
			Log.e(App.TAG, e.getLocalizedMessage());
		}
		return obj;
	}


	static public String stringValue(JSONObject obj, String key) {
		try {
			return obj.getString(key);
		} catch ( Exception e ) {
			Log.e(App.TAG, e.getLocalizedMessage());
		}
		return "";
	}
}
