package com.atasoft.flangeassist.fragments;

import android.content.*;
import android.os.*;
import android.preference.*;
import android.support.v4.app.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.atasoft.flangeassist.*;
import com.atasoft.helpers.*;

public class FlangeFragment extends Fragment {
    View thisFrag;
	Context context;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.flanges, container, false);
        thisFrag = v;
        this.context = v.getContext();
		
		setupSpinners();

        return v;
    }

	@Override
	public void onResume()
	{
		loadPrefs();
		super.onResume();
	}

	@Override
	public void onPause()
	{
		savePrefs();
		super.onPause();
	}
	
	Spinner rateS;
	Spinner sizeS;
	JsonPuller jPuller;
	TextView sDiamVal;
	TextView wrenchVal;	
	TextView driftVal;
	TextView sCountVal;
	TextView sLengthVal;
	TextView b7Val;
	TextView b7mVal;
	String[] fSizesCombined;
	String[] fSizes;
	String[] fSizesXL;
	String[] fRates;
	String[] fRatesXL;
	String[] fRatesXXL;
	SharedPreferences prefs;
	private void setupSpinners() {
		this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
		this.jPuller = new JsonPuller(thisFrag);
		this.fSizesCombined = jPuller.getSizesCombined();
		this.fSizes = jPuller.getSizes();
		this.fSizesXL = jPuller.getSizesXL();
		this.fRates= jPuller.getRates();
		this.fRatesXL = jPuller.getRatesXL();
		this.fRatesXXL = jPuller.getRatesXXL();

		this.sizeS = (Spinner) thisFrag.findViewById(R.id.sizeSpinner);
		ArrayAdapter<String> adaptorSize = new ArrayAdapter<String>(getActivity().getApplicationContext(),
																 android.R.layout.simple_spinner_item, fSizesCombined);
		sizeS.setAdapter(adaptorSize);
		
		this.rateS = (Spinner) thisFrag.findViewById(R.id.rateSpinner);
		
		loadPrefs(); //includes setup for rate adaptor
		
		rateS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		        public void onItemSelected(AdapterView<?> parent, View view,
										   int pos, long id) {
					spinSend();
				}

				public void onNothingSelected(AdapterView<?> parent) {
				}
			});
		sizeS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
										   int pos, long id) {
				updateRateSpin(sizeS.getSelectedItemPosition(), rateS.getSelectedItemPosition());
				spinSend();
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		this.sDiamVal = (TextView) thisFrag.findViewById(R.id.sDiamVal);
		this.wrenchVal = (TextView) thisFrag.findViewById(R.id.wrenchVal);
		this.driftVal = (TextView) thisFrag.findViewById(R.id.driftVal);
		this.sCountVal = (TextView) thisFrag.findViewById(R.id.sCountVal);
		this.sLengthVal = (TextView) thisFrag.findViewById(R.id.sLengthVal);
		this.b7Val = (TextView) thisFrag.findViewById(R.id.b7Val);
		this.b7mVal = (TextView) thisFrag.findViewById(R.id.b7MVal);
		
	}
	
	private static final int RATE_NORMAL = 0;
	private static final int RATE_XL = 1;
	private static final int RATE_XXL = 2;
	private int xlFlag;
	private void updateRateSpin(int sizeIndex, int rateIndex){
		//fRates, fRatesXL
		int xlState = (sizeIndex < fSizes.length) ? RATE_NORMAL: (sizeIndex < fSizesCombined.length - 6) ? RATE_XL: RATE_XXL;
		if(rateS.getSelectedItem() != null) {
			if(xlFlag == xlState) return;
		}
		this.xlFlag = xlState;
		String[] fRatesOut;
		fRatesOut = (xlFlag == RATE_NORMAL) ? fRates : (xlFlag == RATE_XL) ? fRatesXL: fRatesXXL;	
		rateIndex = (rateIndex >= fRatesOut.length) ? fRatesOut.length-1 : rateIndex;
		
		//Log.w("FlangeFrag",String.format("xlFlag is %b, sizeIndex is %d, fSizes.length is %d", xlFlag, sizeIndex, fSizes.length));
		
		
		ArrayAdapter<String> adapterRate = new ArrayAdapter<String>(getActivity().getApplicationContext(),
			android.R.layout.simple_spinner_item, fRatesOut);
		rateS.setAdapter(adapterRate);
		rateS.setSelection(rateIndex);
		return;
	}
	
	String[] flangeVals;
	String[] studVals;
	private void spinSend() {
		String fSize = (String) sizeS.getSelectedItem();
		String fRate = (String) rateS.getSelectedItem();
		fRate = (xlFlag != RATE_NORMAL) ? fRate + "XL": fRate;
		//Stud Diameter, Stud Size Index (not used), Stud Count, Stud Length  
		Log.w("FlangeFrag", String.format("rate is: %s. size is: %s. xlMode is: %b", fRate, fSize, xlFlag));
		this.flangeVals = jPuller.pullFlangeVal(fSize, fRate);
		Log.w("FlangeFrag", String.format("flangeVals[0]: %s", flangeVals[0]));
		//Wrench size, Drift pin size, B7M torque val, B7 torque val
		if(flangeVals == null){
			Log.e("FlangeFragment", "jPuller Returning null flangeVal");
			displayErr(true);
			return;
		}
		
		if(flangeVals[0].startsWith("-")){
			displayErr(false);
			return;
		}
		this.studVals = jPuller.pullStudVal(flangeVals[0], xlFlag != RATE_NORMAL);
		
		if(studVals == null){
			displayErr(true);
			Log.e("FlangeFragment","jPuller returning null studVals");
			return;
		}
		
		displayVals();
	}
	
	private void displayVals() {
		sDiamVal.setText(flangeVals[0] + "\"");
		wrenchVal.setText(studVals[0] + "\"");
		driftVal.setText(studVals[1] + "\"");
		sCountVal.setText(flangeVals[2]);
		sLengthVal.setText(flangeVals[3] + "\"");
		b7Val.setText(studVals[3] + " ft-lbs");
		b7mVal.setText(studVals[2] + " ft-lbs");
		return;
	}
	
	private void displayErr(boolean realError){
		if(realError){
			sDiamVal.setText("err\"");
			wrenchVal.setText("err\"");
			driftVal.setText("err\"");
			sCountVal.setText("err");
			sLengthVal.setText("err\"");
			b7Val.setText("err ft-lbs");
			b7mVal.setText("err ft-lbs");
		} else {
			sDiamVal.setText("-\"");
			wrenchVal.setText("-\"");
			driftVal.setText("-\"");
			sCountVal.setText("-");
			sLengthVal.setText("-\"");
			b7Val.setText("- ft-lbs");
			b7mVal.setText("- ft-lbs");	
		}
	}
	
	private void loadPrefs(){
		if(sizeS.getSelectedItem() != null) sizeS.setSelection(prefs.getInt("ATA_flangeSize", 0));
		xlFlag = prefs.getInt("ATA_flangeXLFlagInt", 0);
		updateRateSpin(prefs.getInt("ATA_flangeSize", 0), prefs.getInt("ATA_flangeRate", 0));
	}
	
	private void savePrefs(){
		SharedPreferences.Editor prefEdit = prefs.edit();
		prefEdit.putInt("ATA_flangeSize", sizeS.getSelectedItemPosition());
		prefEdit.putInt("ATA_flangeRate", rateS.getSelectedItemPosition());
		prefEdit.putInt("ATA_flangeXLFlagInt", xlFlag);
		prefEdit.apply();
	}
	
}
