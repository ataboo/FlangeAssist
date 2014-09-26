package com.atasoft.flangeassist.fragments;

import android.os.*;
import android.support.v4.app.*;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.atasoft.flangeassist.*;
import com.atasoft.helpers.*;

public class FlangeFragment extends Fragment {
    View thisFrag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.flanges, container, false);
        thisFrag = v;
        setupSpinners();

        return v;
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
	String[] fSizes;
	String[] fRates;
	String[] fRatesXL;
    private void setupSpinners() {
		this.jPuller = new JsonPuller(thisFrag);
		this.fSizes = jPuller.getSizes();
		this.fRates= jPuller.getRates();
		this.fRatesXL = jPuller.getRatesXL();
		this.sizeS = (Spinner) thisFrag.findViewById(R.id.sizeSpinner);
		ArrayAdapter<String> adaptor2 = new ArrayAdapter<String>(getActivity().getApplicationContext(),
																 android.R.layout.simple_spinner_item, fSizes);
		sizeS.setAdapter(adaptor2);
		this.rateS = (Spinner) thisFrag.findViewById(R.id.rateSpinner);
		rateSpinnerUpdate();
		

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
	
	String[] flangeVals;
	String[] studVals;
	boolean xlMode = false;
	private void spinSend() {
		String fSize = (String) sizeS.getSelectedItem();
		String fRate = (String) rateS.getSelectedItem();
		rateSpinnerUpdate();
		if(xlMode){
			fRate = fRate + "XL";
		}
		//Stud Diameter, Stud Size Index (not used), Stud Count, Stud Length  
		Log.w("FlangeFrag", String.format("rate is: %s. size is: %s. xlMode is: %b", fRate, fSize, xlMode));
		this.flangeVals = jPuller.pullFlangeVal(fSize, fRate);
		
		//Wrench size, Drift pin size, B7M torque val, B7 torque val
		this.studVals = jPuller.pullStudVal(flangeVals[0], xlMode);
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
	}
	
	private void rateSpinnerUpdate(){
		boolean currentIsXL = sizeS.getSelectedItemPosition() >= fSizes.length;
		if(rateS != null && xlMode == currentIsXL) return;
		int oldPos = rateS.getSelectedItemPosition();
		String[] rateArray = (currentIsXL) ? fRatesXL : fRates;
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
				android.R.layout.simple_spinner_item, rateArray);
		rateS.setAdapter(adapter);
		oldPos = (oldPos < rateArray.length) ? oldPos: rateArray.length - 1;
		rateS.setSelection(oldPos);
		this.xlMode = currentIsXL; 
		Log.w("FlangeFrag RateSPinnerUpdate", String.format("xlMode is: %b, currentIsXl is: %b, equation is: %b", xlMode, currentIsXL, sizeS.getSelectedItemPosition() >= fSizes.length));
	}
}
