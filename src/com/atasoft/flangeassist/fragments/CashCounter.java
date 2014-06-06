package com.atasoft.flangeassist.fragments;

import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.text.format.*;
import android.view.*;
import android.view.View.*;
import com.atasoft.flangeassist.*;
import android.app.TimePickerDialog;
import android.widget.*;
import android.util.*;


public class CashCounter extends Fragment implements OnClickListener {
	
	View thisFrag;
	Context context;
	public final static int SHIFT_START = 0;
	public final static int SHIFT_END = 1;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState){
		
		View v = inflater.inflate(R.layout.cash_counter, container, false);
        thisFrag = v;
		context = getActivity().getApplicationContext();
		setupViews();
		
		return v;
	}
	
	@Override
    public void onClick(View v) {
		switch (v.getId()) {
			case R.id.cash_startExpand:
				startAtaPicker.toggleHide();
				break;
        }
    }
	
	//----------------------AtaTimePick class--------------------
	public class AtaTimePick{
		private String[] HOURS;
		private String[] MINUTES;
		
		NumberPicker hourPick;
		NumberPicker minPick;
		LinearLayout pickLay;
		public AtaTimePick(ViewGroup parent, Context ctx, int[] defTime){
			this.HOURS = makeStringsFromRange(0, 23);
			this.MINUTES = makeStringsFromRange(0,59);
			defTime = validateTime(defTime);
			this.pickLay = new LinearLayout(ctx);
			pickLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 
				LinearLayout.LayoutParams.WRAP_CONTENT));
			pickLay.setOrientation(LinearLayout.HORIZONTAL);
			
			NumberPicker hourPick = makePicker(HOURS, ctx);
			pickLay.addView(hourPick);
			TextView seperator = new TextView(ctx);
			seperator.setText(":");
			seperator.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
			seperator.setPadding(8, 36, 8, 8);
			pickLay.addView(seperator);
			NumberPicker minPick = makePicker(MINUTES, ctx);
			pickLay.addView(minPick);
			parent.addView(pickLay);
			this.toggleHide();
		}
		
		private boolean isHidden = false;
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
	}
	
	//-------------------------private functions-----------------
	Time shiftStart;
	Time shiftEnd;
	Time timeNow;
	int shiftEndHrIndex = 8;  //0830 default. will change to prefs.
	int shiftEndMinIndex = 30;
	TextView shiftStartExpand;
	AtaTimePick startAtaPicker;
	private void setupViews(){
		this.shiftStartExpand = (TextView) thisFrag.findViewById(R.id.cash_startExpand);
		//shiftStart = new Time();
		this.timeNow = new Time(Time.getCurrentTimezone());
		timeNow.setToNow();
		//shiftStart.hour = 0;
		//shiftStart.minute = 30;
		shiftStartExpand.setOnClickListener(this);
		startAtaPicker = makeTimePicker(SHIFT_START);
	}
	
	
	private AtaTimePick makeTimePicker(int code){
		LinearLayout linLay;
		
		switch(code){
			//case SHIFT_START:
			default:
				linLay = (LinearLayout) thisFrag.findViewById(R.id.cash_startLin);
				break;
		}
		return new AtaTimePick(linLay, context, new int[]{8,0});
		
	}
	
	
	public void setTime(int selHour, int selMin){
		Toast.makeText(context, String.format("Set Time to: %d:%d", selHour, selMin), Toast.LENGTH_SHORT).show();
	}
}
