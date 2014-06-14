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
	}
	
	@Override
	public void onPause(){
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
	int[] shiftStartVal = {6, 30};
	Button setExpand;
	AtaTimePicker startAtaPicker;
	EditText wageEdit;
	TextView wageLabel;
	LinearLayout setLay;
	CheckBox nightToggle;
	CheckBox holidayToggle;
	CheckBox fourTenToggle;
	EditText[] weekdayEdits = new EditText[3];
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
		nightToggle.setOnClickListener(this);
		this.holidayToggle = (CheckBox) thisFrag.findViewById(R.id.cash_holidayCheck);
		holidayToggle.setOnClickListener(this);
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
		startAtaPicker = new AtaTimePicker(setLay, context, shiftStartVal, "Start Time:");
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
	
	int[] currentTimeArr = new int[3];
	int[] timeDifference = new int[3];
	int[] shiftEnd = new int[3];
	private void updateValues(){
		
		
		timeNow.setToNow();
		currentTimeArr = new int[]{timeNow.hour, timeNow.minute, timeNow.second};
		shiftEnd = getShiftEnd(currentTimeArr, 10);
		timeDifference = getTimeDelta(currentTimeArr, new int[]{shiftStartVal[0], shiftStartVal[1], 0});
		
		int[] newCountVals = {1,2,3,4,5,6};
		newCountVals[2] = timeNow.second/10;
		newCountVals[3] = timeNow.second - (newCountVals[2] * 10);
		updateCounter(newCountVals);
		this.shiftStartVal = startAtaPicker.getVals();
		this.wageRate = ataParseFloat(wageEdit.getText().toString());
		//update wagePref
	}
	
	private int[] getTimeDelta(int[] time1, int[] time2){
		int[] deltaTime = new int[3];
		deltaTime[0] = time2[0] - time1[0];
		if(deltaTime[0] < 0) deltaTime[0] += 24;
		
		return null;
	}
	
	private int[] getShiftEnd(int[] time1, float shiftLength){
		
		return null;
	}
	
	boolean changeFlag = false;  //true when counter is changing to prevent multiple calls
	private void updateCounter(int[] newVals){
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
			Toast.makeText(context, "doubleTap", Toast.LENGTH_SHORT).show();
		}
	}
}
