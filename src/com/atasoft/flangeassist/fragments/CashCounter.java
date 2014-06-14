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
	public final static int SHIFT_START = 0;
	public final static int SHIFT_END = 1;
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
			case R.id.cash_testButton:
				testClick();
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
		//---------------------------
		//shiftStart = new Time();
		this.timeNow = new Time(Time.getCurrentTimezone());
		timeNow.setToNow();
		//shiftStart.hour = 0;
		//shiftStart.minute = 30;
		//---------------------------
		
		this.wageLabel = (TextView) thisFrag.findViewById(R.id.cash_wageLabel);
		this.wageEdit = (EditText) thisFrag.findViewById(R.id.cash_wageEdit);
		//if(wageEdit.getText().toString() == "") 
		this.wageRate = prefs.getFloat("cashcount_wage", 43.25f);
		wageEdit.setText(Float.toString(wageRate), EditText.BufferType.EDITABLE);
		
		this.setExpand = (Button) thisFrag.findViewById(R.id.cash_settingsBut);
		setExpand.setOnClickListener(this);
	
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
		
		this.testButton = (Button) thisFrag.findViewById(R.id.cash_testButton);
		testButton.setOnClickListener(this);
		
		LinearLayout setLay = (LinearLayout) thisFrag.findViewById(R.id.cash_setLin);
		startAtaPicker = new AtaTimePicker(setLay, context, shiftStartVal, "Start Time:");
		endAtaPicker = new AtaTimePicker(setLay, context, shiftEndVal, "End Time:");
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
				Toast.makeText(context, "Called slideInEnd.", Toast.LENGTH_SHORT);
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
		startAtaPicker.toggleHide();
		endAtaPicker.toggleHide();
		int visCode = (settingsHidden) ? View.GONE : View.VISIBLE;
		wageLabel.setVisibility(visCode);
		wageEdit.setVisibility(visCode);
	}
	
	private void updateValues(){
		timeNow.setToNow();
		int[] newCountVals = {1,2,3,4,5,6};
		newCountVals[2] = timeNow.second/10;
		newCountVals[3] = timeNow.second - (newCountVals[2] * 10);
		updateCounter(newCountVals);
		this.shiftStartVal = startAtaPicker.getVals();
		this.shiftEndVal = endAtaPicker.getVals();
		this.wageRate = ataParseFloat(wageEdit.getText().toString());
		//update wagePref
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
	
	private void testClick(){
		if(!changeFlag) {
			updateValues();
		} else {
			Toast.makeText(context, "doubleTap", Toast.LENGTH_SHORT).show();
		}
	}
}
