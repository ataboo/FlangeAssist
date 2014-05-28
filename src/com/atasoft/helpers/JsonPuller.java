package com.atasoft.helpers;

import android.util.*;
import android.view.*;
import java.io.*;
import java.util.*;
import org.json.*;

public class JsonPuller
{
	View pView;
	public JsonPuller(View parentView) {
		this.pView = parentView;
		populateValues();
		return;
	}
	
	public String[] getValArray(int sizeIndex, int rateIndex){
		
		
		return null;
	}
	
	public static final int RATE_150 = 0;
	public static final int RATE_300 = 1;
	public static final int RATE_400 = 2;
	public static final int RATE_600 = 3;
	public static final int RATE_900 = 4;
	public static final int RATE_1500 = 5;
	public static final int STUD_ARRAY_LENGTH = 4;
	public static final int RATE_ARRAY_LENGTH = 4;
	private static final String jFileName = "FlangeValues.json";
	
	private boolean failFlag = false;
	private JSONObject masterObj;
	private String[] fSizes;
	private String[] fRatings;
	private String[] studSizeOrdered;
	private HashMap<String, String[]> studStats;
	private HashMap<String, String[]> fStats150;
	private HashMap<String, String[]> fStats300;
	private HashMap<String, String[]> fStats400;
	private HashMap<String, String[]> fStats600;
	private HashMap<String, String[]> fStats900;
	private HashMap<String, String[]> fStats1500;
	private void populateValues(){
		this.masterObj = loadJSON();
		if(this.masterObj == null) {
			Log.e("JSON Puller", "masterObj is null. oops.");
			this.failFlag = true;
			return;
		}
		this.fSizes = getJSONStringArray(masterObj, "fSizes");
		this.fRatings = getJSONStringArray(masterObj, "fRatings");
		this.studSizeOrdered = getJSONStringArray(masterObj, "studSizeOrdered");
		this.studStats = makeHash("studSizes", studSizeOrdered, STUD_ARRAY_LENGTH);
		this.fStats150 = makeHash("fStats150", fSizes, RATE_ARRAY_LENGTH);
		this.fStats300 = makeHash("fStats300", fSizes, RATE_ARRAY_LENGTH);
		this.fStats400 = makeHash("fStats400", fSizes, RATE_ARRAY_LENGTH);
		this.fStats600 = makeHash("fStats600", fSizes, RATE_ARRAY_LENGTH);
		this.fStats900 = makeHash("fStats900", fSizes, RATE_ARRAY_LENGTH);
		this.fStats1500 = makeHash("fStats1500", fSizes, RATE_ARRAY_LENGTH);
	}
	
	public String[] pullFlangeVal(int size, int rating){
		if(failFlag) return null;
		String[] retString = new String[RATE_ARRAY_LENGTH];
		HashMap checkMap;
		switch(rating) {
			case RATE_150:
				checkMap = fStats150;
				break;
			case RATE_300:
				checkMap = fStats300;
				break;
			case RATE_400:
				checkMap = fStats400;
				break;
			case RATE_600:
				checkMap = fStats600;
				break;
			case RATE_900:
				checkMap = fStats900;
				break;
			default:  //1500
				checkMap = fStats1500;
				break;
			}
		retString = (String[]) checkMap.get(fRatings[rating]);
		return retString;
	}
	
	public String[] pullStudVal(String studSize){
		if(failFlag) return null;
		String[] retString;
		retString = studStats.get(studSize);
		return retString;
	}
	
	private String[] getJSONStringArray(JSONObject parentObj, String jKey){
		try{
			JSONArray jStringArr = parentObj.getJSONArray(jKey);
			
			String[] retStr = jArrayToString(jStringArr);
			return retStr;
		} catch(JSONException jE){
			jE.printStackTrace();
			Log.e("JSON Puller", jKey + " was out to lunch.");
			return null;			
		}
	}
	
	private String[] jArrayToString(JSONArray jArray) {
		String[] retStr = new String[jArray.length()];
		try{
			for(int i = 0; i < retStr.length; i++) {
				retStr[i] = jArray.getString(i);
			}
		} catch(JSONException jE) {
			jE.printStackTrace();
			Log.e("JSON Puller", "jArrayToString out to lunch.");
			return null;
		}
		return retStr;
	}
	
	private JSONObject getJObject(String jKey){
		JSONObject retObj = null;
		try{
			retObj = masterObj.getJSONObject(jKey);
		} catch(JSONException jE){
			jE.printStackTrace();
			Log.e("JSON Puller", "getJObject JSON exception. looking for: " + jKey);
			return null;
		}
		return retObj;
	}
	
	private HashMap<String, String[]> makeHash(String arrayKey, String[] hashKey, int arrayLength){
		HashMap<String, String[]> retHash = new HashMap<String, String[]>(arrayLength);
		
		JSONObject arrayMaster = getJObject(arrayKey);
		for(int i = 0; i < hashKey.length; i++){
			String[] fullStringArr = getJSONStringArray(arrayMaster, hashKey[i]);
			retHash.put(hashKey[i], fullStringArr);
		}
		return retHash;
	}
		
	private JSONObject loadJSON() {
		JSONObject jObj = null;		
		try {
			String jStr = null;
			InputStream inStr = pView.getContext().getAssets().open(jFileName);
			int size = inStr.available();
			byte[] buffer = new byte[size];
			inStr.read(buffer);
			inStr.close();
			jStr = new String(buffer, "UTF-8");

			jObj = new JSONObject(jStr);			
			return jObj;
		} catch (IOException ex) {
			ex.printStackTrace();
			Log.e("JSON Puller", "IOException on file load");
			return null;
		}
		catch (JSONException jE) {
			jE.printStackTrace();
			Log.e("JSON Puller", "JSONException on file load");
			return null;
		}
	}
}
