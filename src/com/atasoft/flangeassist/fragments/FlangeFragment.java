package com.atasoft.flangeassist.fragments;

import android.os.*;
import android.support.v4.app.*;
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
    private void setupSpinners() {
		this.jPuller = new JsonPuller(thisFrag);
		String[] fSizes = jPuller.getSizes();
		String[] fRates= jPuller.getRates();
		this.rateS = (Spinner) thisFrag.findViewById(R.id.rateSpinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
																android.R.layout.simple_spinner_item, fRates);
		rateS.setAdapter(adapter);

		this.sizeS = (Spinner) thisFrag.findViewById(R.id.sizeSpinner);
		ArrayAdapter<String> adaptor2 = new ArrayAdapter<String>(getActivity().getApplicationContext(),
																 android.R.layout.simple_spinner_item, fSizes);
		sizeS.setAdapter(adaptor2);

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
	private void spinSend() {
		String fSize = (String) sizeS.getSelectedItem();
		String fRate = (String) rateS.getSelectedItem();
		
		//Stud Diameter, Stud Size Index (not used), Stud Count, Stud Length  
		this.flangeVals = jPuller.pullFlangeVal(fSize, fRate);
		
		//Wrench size, Drift pin size, B7M torque val, B7 torque val
		this.studVals = jPuller.pullStudVal(flangeVals[0]);
		displayVals();
	}
	
	private void displayVals() {
		sDiamVal.setText(flangeVals[0] + "\"");
		wrenchVal.setText(studVals[0] + "\"");
		driftVal.setText(studVals[1] + "\"");
		sCountVal.setText(flangeVals[2]);
		sLengthVal.setText(flangeVals[3] + "\"");
		b7Val.setText(studVals[2] + " ft-lbs");
		b7mVal.setText(studVals[3] + " ft-lbs");
	}
}
