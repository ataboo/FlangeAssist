package com.atasoft.flangeassist.fragments;

import android.content.*;
import android.os.*;
import android.app.*;
import android.text.format.*;
import android.view.*;
import android.view.View.*;
import com.atasoft.flangeassist.*;
import android.app.TimePickerDialog;
import android.widget.*;


public class CashCounter extends Fragment implements OnClickListener {
	
	View thisFrag;
	Context context;
	
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
			case R.id.cash_testButton:
				pickStartTime();
				break;
        }
    }
	
	Time shiftStart;
	Time shiftEnd;
	Time timeNow;
	private void setupViews(){
		shiftStart = new Time();
		this.timeNow = new Time(Time.getCurrentTimezone());
		timeNow.setToNow();
		shiftStart.hour = 0;
		shiftStart.minute = 30;
		
	}
	
	private void pickStartTime(){
		TimePickerDialog picker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
				setTime(selectedHour, selectedMinute);
			}
		}, 12, 30, true);
		picker.setTitle("Select Time");
		picker.show();
	}
	
	public void setTime(int selHour, int selMin){
		Toast.makeText(context, String.format("Set Time to: %d:%d", selHour, selMin), Toast.LENGTH_SHORT).show();
	}
}
