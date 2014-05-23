package com.atasoft.helpers;

import android.util.*;
import android.view.*;
import java.io.*;
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
	
	private boolean failFlag = false;
	private JSONObject masterObj;
	private String[] fSizes;
	private String[] fRatings;
	private String[] studSizeOrdered;
	private String[][] studStats;
	private String[][] fStats150;
	private String[][] fStats300;
	private String[][] fStats400;
	private String[][] fStats600;
	private String[][] fStats900;
	private String[][] fStats1500;
	private void populateValues(){
		this.masterObj = loadJSON();
		if(this.masterObj == null) {
			Log.e("JSON Puller", "masterObj is null. oops.");
			this.failFlag = true;
			return;
		}
		this.fSizes = getJSONStringArray("fSizes");
		this.fRatings = getJSONStringArray("fRatings");
		this.studSizeOrdered = getJSONStringArray("studSizeOrdered");
		this.studStats = getMultiArray("studSizes", studSizeOrdered, 4);
		this.fStats150 = getMultiArray("fStats150", fSizes, 4);
		this.fStats300 = getMultiArray("fStats300", fSizes, 4);
		this.fStats400 = getMultiArray("fStats400", fSizes, 4);
		this.fStats600 = getMultiArray("fStats600", fSizes, 4);
		this.fStats900 = getMultiArray("fStats900", fSizes, 4);
		this.fStats1500 = getMultiArray("fStats1500", fSizes, 4);
	}
	
	private String[] getJSONStringArray(String jKey){
		try{
			JSONArray jStringArr = masterObj.getJSONArray(jKey);
			
			String[] retStr = jArrayToString(jStringArr);
			return retStr;
		} catch(JSONException jE){
			jE.printStackTrace();
			Log.e("JSON Puller", jKey + " was out to lunch.");
			return null;			
		}
	}
	
	private String[][] getMultiArray(String objName, String[] keys, int length){
		String[][] multiRet = new String[keys.length][length + 1];
		try{
			JSONObject subObj =  masterObj.getJSONObject(objName);
			for(int i=0; i < keys.length; i++){
				JSONArray jArray =  subObj.getJSONArray(keys[i]);
				multiRet[i][0] = keys[i]; //key is first value
				for(int j=0; j < jArray.length(); j++){
					multiRet[i][j + 1] = jArray.getString(j);
				}
			}
			
		} catch(JSONException jE) {
			jE.printStackTrace();
			return null;
		}
		
		return multiRet;
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
	
	private JSONObject loadJSON() {
		JSONObject jObj = null;		
		try {
			String jStr = null;
			InputStream inStr = pView.getContext().getAssets().open("FlangeValues.json");
			int size = inStr.available();
			byte[] buffer = new byte[size];
			inStr.read(buffer);
			inStr.close();
			jStr = new String(buffer, "UTF-8");

			jObj = new JSONObject(jStr);			
			return jObj;
		} catch (IOException ex) {
			ex.printStackTrace();
			Log.e("JSON Puller", "IOException");
			return null;
		}
		catch (JSONException jE) {
			jE.printStackTrace();
			Log.e("JSON Puller", "JSONException");
			return null;
		}
	}
	
	
}
