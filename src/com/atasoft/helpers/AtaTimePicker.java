package com.atasoft.helpers;

//Used by CashCounter to make choose times
//----------------------AtaTimePick class--------------------
import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;

public class AtaTimePicker {
	
	private String[] HOURS;
	private String[] MINUTES;
	NumberPicker hourPick;
	NumberPicker minPick;
	LinearLayout pickLay;
	
	public AtaTimePicker(ViewGroup parent, Context ctx, int[] defTime, String labelName){
		this.HOURS = makeStringsFromRange(0, 23);
		this.MINUTES = makeStringsFromRange(0,59);
		defTime = validateTime(defTime);
		this.pickLay = new LinearLayout(ctx);
		pickLay.setGravity(Gravity.CENTER_VERTICAL);
		LinearLayout.LayoutParams layPar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 
															LinearLayout.LayoutParams.WRAP_CONTENT);
		pickLay.setLayoutParams(layPar);
		pickLay.setOrientation(LinearLayout.HORIZONTAL);
		
		//Label for time
		TextView label = new TextView(ctx);
		label.setText(labelName);
		label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		pickLay.addView(label);
		
		//Hour number picker
		this.hourPick = makePicker(HOURS, ctx);
		pickLay.addView(hourPick);
		
		//Seperates hours from minutes
		TextView seperator = new TextView(ctx);
		seperator.setText(":");
		seperator.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
		seperator.setPadding(8, 36, 8, 8);
		pickLay.addView(seperator);
		
		//Minute number picker
		this.minPick = makePicker(MINUTES, ctx);
		pickLay.addView(minPick);
		parent.addView(pickLay);
	}

	public boolean isHidden = false;
	public void toggleHide(){
		this.isHidden = !isHidden;
		int visCode = (isHidden) ? View.GONE : View.VISIBLE;
		pickLay.setVisibility(visCode);
	}

	private int[] validateTime(int[] timeArr){
		timeArr[0] = (timeArr[0] >= 0 && timeArr[0] <= 23) ? timeArr[0] : 0;
		timeArr[1] = (timeArr[1] >= 0 && timeArr[1] <= 59) ? timeArr[1] : 0;
		return timeArr;
	}

	private String[] makeStringsFromRange(int floor, int ceiling){
		String[] retString = new String[ceiling - floor + 1];
		for(int i=floor; i <= ceiling; i++){
			retString[i] = Integer.toString(i);
			if(i < 10) retString[i] = "0" + retString[i];
		}
		return retString;
	}

	private NumberPicker makePicker(String[] values, Context ctx){
		NumberPicker picker = new NumberPicker(ctx);
		picker.setLayoutParams(new NumberPicker.LayoutParams(NumberPicker.LayoutParams.WRAP_CONTENT, 
															 NumberPicker.LayoutParams.WRAP_CONTENT));
		picker.setDisplayedValues(values);
		picker.setMaxValue(values.length-1);
		picker.setMinValue(0);

		return picker;
	}
	
	public int[] getVals(){
		int[] retInt = {0,0};
		retInt[0] = hourPick.getValue();
		retInt[1] = minPick.getValue();
		return retInt;
	}
}
