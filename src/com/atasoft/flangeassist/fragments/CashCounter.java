package com.atasoft.flangeassist.fragments;

import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.text.format.*;
import android.view.*;
import android.view.View.*;
import com.atasoft.flangeassist.*;
import com.atasoft.helpers.*;
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
			case R.id.cash_settingsBut:
				toggleSettingsHide();
				break;
        }
    }
	
	public void updateTime(int selHour, int selMin){
		Toast.makeText(context, String.format("Set Time to: %d:%d", selHour, selMin), Toast.LENGTH_SHORT).show();
	}
		
	//-------------------------initial functions-----------------
	Time shiftStart;
	Time shiftEnd;
	Time timeNow;
	int[] shiftEndVal = {8, 0};  //0830 default. will change to prefs.
	int[] shiftStartVal = {6, 30};
	Button setExpand;
	AtaTimePicker startAtaPicker;
	AtaTimePicker endAtaPicker;
	EditText wageEdit;
	TextView wageLabel;
	//SharedPreferences prefs;
	private void setupViews(){
		//---------------------------
		//shiftStart = new Time();
		this.timeNow = new Time(Time.getCurrentTimezone());
		timeNow.setToNow();
		//shiftStart.hour = 0;
		//shiftStart.minute = 30;
		//---------------------------
		
		this.wageLabel = (TextView) thisFrag.findViewById(R.id.cash_wageLabel);
		this.wageEdit = (EditText) thisFrag.findViewById(R.id.cash_wageEdit);
		if(wageEdit.getText() == null) wageEdit.setText("43.25");
		this.setExpand = (Button) thisFrag.findViewById(R.id.cash_settingsBut);
		setExpand.setOnClickListener(this);
		LinearLayout setLay = (LinearLayout) thisFrag.findViewById(R.id.cash_setLin);
		startAtaPicker = new AtaTimePicker(setLay, context, shiftStartVal, "Start Time:");
		endAtaPicker = new AtaTimePicker(setLay, context, shiftEndVal, "End Time:");
		toggleSettingsHide();
	}
	
//-------------------Updating Functions-------------------
	
	private boolean settingsHidden = false;
	private void toggleSettingsHide(){
		settingsHidden = !settingsHidden;
		startAtaPicker.toggleHide();
		endAtaPicker.toggleHide();
		int visCode = (settingsHidden) ? View.GONE : View.VISIBLE;
		wageLabel.setVisibility(visCode);
		wageEdit.setVisibility(visCode);
	}
	
	float wageRate;
	private void updateValues(){
		shiftStartVal = startAtaPicker.getVals();
		shiftEndVal = endAtaPicker.getVals();
		this.wageRate = parseWageVal(wageEdit.getText().toString());
	}
	
	private float parseWageVal(String strIn) throws NumberFormatException{
		try{
			return Float.parseFloat(strIn);
		} catch (NumberFormatException e){
			Log.e("CashCounter", "Error parsing Wage");
			return 0f;
		}
	} 
}
