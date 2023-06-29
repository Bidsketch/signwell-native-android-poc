//
// SignWell
// Android Demo
// Andrey Butov, 2023
//


package com.signwell.androiddemo;


import android.util.Log;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;


public final class MapUtils
{
	public static HashMap createMapWithKeysAndValues(ArrayList keys, ArrayList values)
	{
    	HashMap map = new HashMap<>();

		if ( keys != null && values == null && keys.size() == values.size() ) {
			for ( int i = 0, n = keys.size() ; i < n ; ++i ) {
				map.put(keys.get(i), values.get(i));
			}
		}

		return map;
	}


	public static String stringValue(HashMap map, String key)
	{
		return stringValue(map, key, true, "");
	}


	public static String stringValue(HashMap map, String key, boolean convertNumbers)
	{
		return stringValue(map, key, convertNumbers, "");
	}


	public static String stringValue(HashMap map, String key, boolean convertNumbers, String defaultVal)
	{
		if ( map == null ) {
			return defaultVal;
		}

		if ( map.containsKey(key) )
		{
			Object obj = map.get(key);
			if ( obj instanceof String ) {
				return (String)obj;
			}
			if ( convertNumbers && (obj instanceof Number) ) {
				return String.valueOf((Number)obj);
			}
		}

		return defaultVal;
	}


	public static Number numberValue(HashMap map, String key)
	{
		return MapUtils.numberValue(map, key, 0);
	}


	public static Number numberValue(HashMap map, String key, Number defaultVal)
	{
		if ( map == null ) {
			return defaultVal;
		}

		if ( map.containsKey(key) )
		{
			Object obj = map.get(key);

			if ( obj instanceof Number ) {
				return (Number)obj;
			} else if ( obj instanceof String ) {
				return Double.parseDouble((String)obj);
			}
		}

		return defaultVal;
	}


	public static HashMap safe(HashMap map)
	{
		return map != null ? map : new HashMap();
	}


	public static ArrayList arrayListValue(HashMap map, String key)
	{
		if ( map != null && map.containsKey(key) ) {
			Object obj = map.get(key);
			if ( obj instanceof ArrayList ) {
				return (ArrayList)obj;
			} else if ( obj instanceof HashMap || obj instanceof String || obj instanceof Number) {
				ArrayList list = new ArrayList();
				list.add(obj);
				return list;
			}
		}
		return new ArrayList();
	}


	public static ArrayList<String> stringArrayListValue(HashMap map, String key) {
		ArrayList<String> strings = new ArrayList<>();
		for ( Object obj : MapUtils.arrayListValue(map, key) ) {
			if ( (obj instanceof String) && ((String)obj).trim().length() > 0 ) {
				strings.add((String)obj);
			} else if ( (obj instanceof Number) ) {
				strings.add(((Number)obj).toString());
			}
		}
		return strings;
	}


	public static ArrayList<HashMap> arrayOfHashMaps(HashMap map, String key) {
		ArrayList<HashMap> hashMaps = new ArrayList<HashMap>();
		for ( Object obj : MapUtils.arrayListValue(map, key) ) {
			if ( (obj instanceof HashMap) ) {
				hashMaps.add((HashMap)obj);
			}
		}
		return hashMaps;
	}


	public static boolean boolValue(HashMap map, String key)
	{
		if ( map != null && map.containsKey(key) ) {
			Object obj = map.get(key);
			if ( obj instanceof Boolean ) {
				return (Boolean)obj;
			} else if ( obj instanceof Integer ) {
				return ((Integer)obj) == 1;
			} else if ( obj instanceof String ) {
				return ((String)obj).equalsIgnoreCase("1") || ((String)obj).equalsIgnoreCase("true");
			}
		}
		return false;
	}


	public static HashMap hashMapValue(HashMap map, String key)
	{
		if ( map != null && map.containsKey(key) ) {
			Object obj = map.get(key);
			if ( obj instanceof HashMap ) {
				return (HashMap)obj;
			}
		}
		return new HashMap();
	}


	public static Object safeGet(HashMap map, String key, Object defaultVal) {
		Object obj = null;
		if ( map != null && StringUtils.hasValue(key) ) {
			obj = map.get(key);
		}
		return obj != null ? obj : defaultVal;
	}


	public static void safeSet(HashMap map, String key, Object value)
	{
		if ( map != null && StringUtils.hasValue(key) && value != null ) {
			map.put(key, value);
		}
	}


	public static boolean hasValue(HashMap map)
	{
		return map != null && map.size() > 0;
	}


	public static String prettyPrint(HashMap<String, Object> map) {
		if ( map == null ) {
			return "";
		}
		try {
			return new JSONObject(map).toString(4);
		} catch ( Exception e ) {
			Log.e(App.TAG, e.getLocalizedMessage());
			return "";
		}
	}
}
