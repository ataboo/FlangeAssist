package com.atasoft.flangeassist.fragments;

import android.content.*;
import android.os.*;
import android.preference.*;
import android.support.v4.app.*;
import android.text.format.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.view.animation.*;
import android.widget.*;
import com.atasoft.flangeassist.*;
import com.atasoft.helpers.*;


public class CashCounter extends Fragment implements OnClickListener {
	
	boolean tickPause = true;
	View thisFrag;
	Context context;
	private SharedPreferences prefs; 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState){
								 
		View v = inflater.inflate(R.layout.cash_counter, container, false);
        this.thisFrag = v;
		this.context = getActivity().getApplicationContext();
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		setupViews();
		this.tickPause = false;
		setupTicker();
		
		return v;
	}
	
	@Override
	public void onResume(){
		ticker.run();
		super.onResume();
		recallSettings();
	}
	
	@Override
	public void onPause(){
		saveSettings();
		handler.removeCallbacks(ticker);
		super.onPause();
	}
	
	public class CounterDigit{
		TextView textView;
		int oldVal;
		int newVal;
		boolean changing = false;
		
		public CounterDigit(TextView textView, int startVal){
			this.textView = textView;
			this.oldVal = startVal;
			this.newVal = startVal;
			textView.setText(Integer.toString(startVal));
		}
		
		//returns true if changed
		public boolean changeVal(int newVal){
			this.newVal = newVal;
			this.changing = (newVal != oldVal);
			return changing;
		}
		
		public void hide(boolean isHide){
			int visCode = isHide ? TextView.GONE: TextView.VISIBLE;
			textView.setVisibility(visCode);
		}
	}
	
	@Override
    public void onClick(View v) {
		switch (v.getId()) {
			case R.id.cash_settingsBut:
				toggleSettingsHide();
				break;
			case R.id.cash_fourTensCheck:
				fourTenLock();
				break;
		}
    }
		
	//-------------------------initial functions-----------------
	Time timeNow;
	int[] shiftStartVal = {18, 30};
	Button setExpand;
	EditText wageEdit;
	TextView wageLabel;
	LinearLayout setLay;
	CheckBox nightToggle;
	CheckBox holidayToggle;
	CheckBox fourTenToggle;
	EditText[] weekdayEdits = new EditText[3];
	AtaTimePicker startAtaPicker;
	
	float[] weekdayHours = new float[3];
	
	TranslateAnimation slideInListen;
	TranslateAnimation slideOutListen;
	TranslateAnimation slideIn;
	TranslateAnimation slideOut;
	
	Button testButton;
	int[] oldCountVals = {0,0,0,0,0,0};
	CounterDigit hundredthDigit;
	CounterDigit tenthDigit;
	CounterDigit oneDigit;
	CounterDigit tenDigit;
	CounterDigit hundredDigit;
	CounterDigit thousandDigit;
	float wageRate;
	CounterDigit[] counterDigits;
	//SharedPreferences prefs;
	private void setupViews(){
		this.timeNow = new Time(Time.getCurrentTimezone());
		this.wageLabel = (TextView) thisFrag.findViewById(R.id.cash_wageLabel);
		this.wageEdit = (EditText) thisFrag.findViewById(R.id.cash_wageEdit);
		//if(wageEdit.getText().toString() == "") 
		this.wageRate = prefs.getFloat("cashcount_wage", 43.25f);
		wageEdit.setText(Float.toString(wageRate), EditText.BufferType.EDITABLE);
		
		this.setExpand = (Button) thisFrag.findViewById(R.id.cash_settingsBut);
		setExpand.setOnClickListener(this);
		this.nightToggle = (CheckBox) thisFrag.findViewById(R.id.cash_nightshiftCheck);
		this.holidayToggle = (CheckBox) thisFrag.findViewById(R.id.cash_holidayCheck);
		this.fourTenToggle = (CheckBox) thisFrag.findViewById(R.id.cash_fourTensCheck);
		fourTenToggle.setOnClickListener(this);
		this.weekdayEdits = new EditText[3];
		this.weekdayEdits[0] = (EditText) thisFrag.findViewById(R.id.cash_weekdaySingle);
		this.weekdayHours[0] = prefs.getFloat("cash_week_single", 8);
		this.weekdayEdits[1] = (EditText) thisFrag.findViewById(R.id.cash_weekdayHalf);
		this.weekdayHours[1] = prefs.getFloat("cash_week_half", 2);
		this.weekdayEdits[2] = (EditText) thisFrag.findViewById(R.id.cash_weekdayDouble);
		this.weekdayHours[2] = prefs.getFloat("cash_week_double", 2);
		for(int i=0; i<weekdayEdits.length; i++){
			weekdayEdits[i].setText(Float.toString(weekdayHours[i]));
		}
		
		this.hundredthDigit = new CounterDigit((TextView) thisFrag.findViewById(R.id.cash_hundredthsText), oldCountVals[5]);
		this.tenthDigit = new CounterDigit((TextView) thisFrag.findViewById(R.id.cash_tenthsText), oldCountVals[4]);
		this.oneDigit = new CounterDigit((TextView) thisFrag.findViewById(R.id.cash_onesText), oldCountVals[3]);
		this.tenDigit = new CounterDigit((TextView) thisFrag.findViewById(R.id.cash_tensText), oldCountVals[2]);
		this.hundredDigit = new CounterDigit((TextView) thisFrag.findViewById(R.id.cash_hundredsText), oldCountVals[1]);
		this.thousandDigit = new CounterDigit((TextView) thisFrag.findViewById(R.id.cash_thousandText), oldCountVals[0]);
		this.counterDigits = new CounterDigit[]{thousandDigit, hundredDigit, tenDigit, oneDigit, tenthDigit, hundredthDigit};
		
		
		this.slideIn = makeTranslateVertical(400f, 0f, 400);
		this.slideOut = makeTranslateVertical(0f, -400f, 400);
		this.slideInListen = makeTranslateVertical(400f, 0f, 400);
		this.slideOutListen = makeTranslateVertical(0f, -400f, 400);
		setEndListeners();
		
		setLay = (LinearLayout) thisFrag.findViewById(R.id.cash_setLin);
		NumberPicker startHourPick = (NumberPicker) thisFrag.findViewById(R.id.cash_startHour);
		NumberPicker startMinPick = (NumberPicker) thisFrag.findViewById(R.id.cash_startMin);
		startAtaPicker = new AtaTimePicker(startHourPick, startMinPick);
		recallSettings();
		toggleSettingsHide();
	}
	
	private TranslateAnimation makeTranslateVertical(float start, float end, int duration){
		//x start, x end, y start, y end
		TranslateAnimation slide = new TranslateAnimation(0f, 0f, start, end);
		slide.setDuration(duration);
		return slide;
	}
	
	private void setEndListeners(){
		slideInListen.setAnimationListener(new Animation.AnimationListener(){
			@Override
			public void onAnimationStart(Animation arg) {
			}           
			@Override
			public void onAnimationRepeat(Animation arg) {
			}           
			@Override
			public void onAnimationEnd(Animation arg) {		
				slideInEnd();
			}	
		});
		slideOutListen.setAnimationListener(new Animation.AnimationListener(){
			@Override
			public void onAnimationStart(Animation arg) {
			}           
			@Override
			public void onAnimationRepeat(Animation arg) {
			}           
			@Override
			public void onAnimationEnd(Animation arg) {		
				slideOutEnd();
			}	
		});
	}
	
	private Handler handler;
	private Runnable ticker;
	private void setupTicker(){
		handler = new Handler();
		
		ticker = new Runnable() {
            public void run() {
                long now = SystemClock.uptimeMillis();
                long next = now + 1000;
                handler.postAtTime(ticker, next);
				testClick();
            }
        };
	}
	
//-------------------Updating Functions-------------------
	
	private boolean settingsHidden = false;
	private void toggleSettingsHide(){
		settingsHidden = !settingsHidden;
		int visCode = (settingsHidden) ? View.GONE : View.VISIBLE;
		setLay.setVisibility(visCode);
	}
	
	//hh,mm,ss
	int[] currentTimeArr = new int[3];
	int[] shiftEnd = new int[3];
	int[] shiftDuration = new int[3];
	private void updateValues(){
		
		//------Update time info-----
		for(int i=0; i<weekdayHours.length; i++){
			weekdayHours[i] = AtaMathUtils.bracketFloat(parseFromEdit(weekdayEdits[i], String.format("weekdayEdits[%s]", i)), 0, 24);
		}
		float shiftLengthFloat = AtaMathUtils.bracketFloat(weekdayHours[0] + weekdayHours[1] + weekdayHours[2], 0, 24);
		timeNow.setToNow();
		currentTimeArr = new int[]{timeNow.hour, timeNow.minute, timeNow.second};
		shiftDuration[0] = (int) shiftLengthFloat;
		shiftDuration[1] = (int) (shiftLengthFloat - (float) shiftDuration[0]) * 60;
		this.shiftStartVal = startAtaPicker.getVals();
		shiftEnd = getShiftEnd(shiftStartVal, shiftDuration);
		int[] newCountVals = {0,0,0,0,0,0};
		
		//------Update wage and schedule settings------
		this.wageRate = ataParseFloat(wageEdit.getText().toString());
		
		if(isInTimeRange(shiftStartVal, shiftEnd, currentTimeArr)){
			double earnings = getEarnings(currentTimeArr, shiftStartVal, wageRate);
			newCountVals = makeValsFromDouble(earnings);
		}
		
		//------Write to counter------
		updateCounter(newCountVals);
	}
	
	private void recallSettings(){
		//going to be via preferences eventually
		startAtaPicker.setPickerValue(shiftStartVal);
	}
	
	private void saveSettings(){
		//preferences eventually
		this.shiftStartVal = startAtaPicker.getVals();
	}
	
	private boolean isInTimeRange(int[] rangeStart, int[] rangeEnd, int[] timeCheck){
		float floatStart = getFloatTime(rangeStart);
		float floatEnd = getFloatTime(rangeEnd);
		float floatCheck = getFloatTime(timeCheck);
		if(floatStart <= floatEnd){
			if(floatCheck < floatStart || floatCheck > floatEnd) return false;
			return true;
		} else { //range stradles midnight
			if(floatCheck > floatStart || floatCheck < floatEnd) return true;
			return false;
		}
	}
	
	private double getEarnings(int[] timeNow, int[] shiftStart, float wageVal){
		//used float for comparisons but keep into for calcs incase of rounding shenanigans
		float floatNow = getFloatTime(timeNow);
		float floatStart = getFloatTime(shiftStart);
		int secondsIntoShift = timeNow[2];
		secondsIntoShift += (timeNow[1] - shiftStart[1]) * 60;
		//already checked that it's mid shift
		if(floatNow > floatStart) {
			secondsIntoShift += (timeNow[0] - shiftStart[0]) * 3600;
		} else {  //start-->now range stradles midnight
			secondsIntoShift += (timeNow[0] - shiftStart[0] + 24) * 3600;
		}
		double hoursIntoShift = secondsIntoShift / 3600d;
		double earnings = 0;
		
		boolean isFriday = false;
		boolean isWeekend = false;
		if(holidayToggle.isChecked()) isWeekend = true;
		//if() isWeekend = true;
		double[] hours = new double[3];  //single, ot, double
		if(fourTenToggle.isActivated()){
			hours[2] = AtaMathUtils.bracketDouble(hoursIntoShift - 10, 0, 24);
			if(isFriday){
				hours[1] = AtaMathUtils.bracketDouble(hoursIntoShift, 0, 10);
				hours[0] = 0;
			} else {
				hours[0] = AtaMathUtils.bracketDouble(hoursIntoShift, 0, 10);
				hours[1] = 0;
			}
		} else {
			hours[0] = AtaMathUtils.bracketDouble(hoursIntoShift, 0, weekdayHours[0]);
			hours[2] = AtaMathUtils.bracketDouble(hoursIntoShift - weekdayHours[0] - weekdayHours[1], 0, 24);
			hours[1] = AtaMathUtils.bracketDouble(hoursIntoShift - hours[0] - hours[2], 0, 24);	
		}
		if(isWeekend){
			hours[0] = 0;
			hours[1] = 0;
			hours[2] = AtaMathUtils.bracketDouble(hoursIntoShift, 0, 24);
		}
		double hoursEquivelant = 1d * hours[0] + 1.5d * hours[1] + 2d * hours[2];
		Log.w("CashCounter",String.format("weekdayhours[0]:%.3f, weekdayhours[1]:%.3f, weekdayhours[2]:%.3f", weekdayHours[0], weekdayHours[1], weekdayHours[2]));
		
		Log.w("CashCounter",String.format("hours[0]:%.3f, hours[1]:%.3f, hours[2]:%.3f, intoShift: %.3f", hours[0], hours[1], hours[2], hoursIntoShift));
		earnings = hoursEquivelant * wageVal;
		if(nightToggle.isChecked()) earnings += hoursIntoShift * 3d;
		earnings = Math.floor(earnings * 100) / 100;
		return earnings;
	}
	
	
	
	private int[] makeValsFromDouble(double earnings){
		String valString = String.format("%.2f", earnings);
		int earnLength = valString.length();
		int[] retVals = new int[]{0,0,0,0,0,0};
		if (earnLength < 4 || earnLength > 7){
			Log.e("CashCounter_MakeValseFromDouble", "Earning String out of Range.");
			return retVals;
		}
		retVals[5] = Character.getNumericValue(valString.charAt(earnLength - 1)); //hundreds
		retVals[4] = Character.getNumericValue(valString.charAt(earnLength - 2)); //tenths
		retVals[3] = Character.getNumericValue(valString.charAt(earnLength - 4)); //ones decimal is @ 3
		retVals[2] = (earnLength >= 5) ? Character.getNumericValue(valString.charAt(earnLength - 5)) : 0;
		retVals[1] = (earnLength >= 6) ? Character.getNumericValue(valString.charAt(earnLength - 6)) : 0;
		retVals[0] = (earnLength >= 7) ? Character.getNumericValue(valString.charAt(earnLength - 7)) : 0;
		
		//Log.w("CashCounter_makeCountString", String.format("retString: %s, length: %s", valString, valString.length()));
		//return retString;
		return retVals;
	}
	
	private float getFloatTime(int[] intTime){
		if(intTime.length < 2) return 0f;
		
		float retFloat = intTime[0];
		retFloat += ((float) intTime[1]) / 60;
		if(intTime.length == 3) retFloat += ((float) intTime[2]) / 3600;
		return retFloat;
	}
	
	private int[] getShiftEnd(int[] shiftStart, int[] shiftDuration){
		int[] shiftEnd = {0,0};  //hr, min
		
		shiftEnd[1] = shiftStart[1] + shiftDuration[1];
		if(shiftEnd[1] >= 60){
			shiftDuration[0]++; //adds and hour when minutes overflow
			shiftEnd[1] = shiftEnd[1] % 60;
		}
		shiftEnd[0] = shiftStart[0] + shiftDuration[0];
		shiftEnd[0] = shiftEnd[0] % 24;
		
		return shiftEnd;
	}
	
	private float parseFromEdit(EditText eText, String name) throws NumberFormatException{
		try{
			float retFloat = Float.parseFloat(eText.getText().toString());
			return retFloat;
		} catch(NumberFormatException e){
			Log.e("CashCounter", "NumberFormatException for EditText " + name + ".");
			return 0f;
		}
	}
	
	
	
	boolean changeFlag = false;  //true when counter is changing to prevent multiple calls
	private void updateCounter(int[] newVals){
		/*
		if(newString.length() <= 6 && newString.length() > 0){
			for(int i = newString.length() - 1; i>=0; i--){
				newVals[i] = newString.charAt(i);
			}
		}
		*/
		boolean firstOne = true;
		for(int i=0; i < newVals.length; i++){
			if(counterDigits[i].changeVal(newVals[i])){
				//Make sure only first changed digit has listener to prevent multiple calls
				TranslateAnimation slide = (firstOne) ? slideOutListen : slideOut;
				counterDigits[i].textView.startAnimation(slide);
				this.changeFlag = true;
				firstOne = false;
			}
		}
		if(newVals[0] == 0){
			counterDigits[0].hide(true);
			if(newVals[1] == 0){
				counterDigits[1].hide(true);
				if(newVals[2] == 0){
					counterDigits[2].hide(true);
				} else {
					counterDigits[2].hide(false);
				}
			} else {
				counterDigits[1].hide(false);
				counterDigits[2].hide(false);
			}
		} else {
			counterDigits[0].hide(false);
			counterDigits[1].hide(false);
			counterDigits[2].hide(false);
		}	
	}
	
	private float ataParseFloat(String strIn) throws NumberFormatException{
		try{
			return Float.parseFloat(strIn);
		} catch (NumberFormatException e){
			Log.e("CashCounter", "Error parsing Float");
			return 0f;
		}
	}
	
	private void slideInEnd(){  //teehee
		this.changeFlag = false;  //allows new updateValues to be called
	}
	
	private void slideOutEnd(){
		boolean isFirstOne = true;
		for(CounterDigit digit: counterDigits){
			if(digit.changing) {
				digit.textView.setText(Integer.toString(digit.newVal));
				digit.oldVal = digit.newVal;
				TranslateAnimation slide = (isFirstOne) ? slideInListen : slideIn;
				digit.textView.startAnimation(slide);
				digit.changing = false;
				isFirstOne = false;
			}
		}
	}
	
	private void fourTenLock(){
			weekdayEdits[1].setEnabled(!fourTenToggle.isChecked());
	}
	
	private void testClick(){
		if(!changeFlag) {
			updateValues();
		} else {
			//Toast.makeText(context, "doubleTap", Toast.LENGTH_SHORT).show();
		}
	}
}
