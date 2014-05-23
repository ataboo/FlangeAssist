package com.atasoft.helpers;

import android.support.v4.app.*;
import java.io.*;
import org.json.*;
import android.util.*;

public class JsonPuller
{
	Fragment pFrag;
	public JsonPuller(Fragment parentFrag) {
		this.pFrag = parentFrag;
		populateValues();
		return;
	}
	
	private boolean failFlag = false;
	private JSONObject masterObj;
	private void populateValues(){
		this.masterObj = loadJSON();
		if(this.masterObj == null) {
			Log.e("JSON Puller", "masterObj is null. oops.");
			this.failFlag = true;
			return;
		}
		String[] fSizes = getJSONStringArray("fSizes");;
	}
	
	private String[] getJSONStringArray(String jKey){
		try{
			JSONArray jStringArr = masterObj.getJSONArray("jKey");
			
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
		}
		return retStr;
	}
	
	private JSONObject loadJSON() {
		JSONObject jObj = null;		
		try {
			String jStr = null;
			InputStream inStr = pFrag.getActivity().getAssets().open("FlangeValues.json");
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
