package com.atasoft.flangeassist.fragments;

import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;
import com.atasoft.flangeassist.*;

public class FlangeFragRewrite extends Fragment {
    View thisFrag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.flanges, container, false);
        thisFrag = v;
        setupSpinners();

        return v;
    }
	
	TextView sDiamVal;
	TextView wrenchVal;
	TextView driftVal;	
	TextView sCountVal;
	TextView sLengthVal;
	TextView b7Val;
	TextView b7mVal;

    private void setupSpinners() {
		String fSizes[] = getResources().getStringArray(R.array.f_sizes);
		String fRates[] = getResources().getStringArray(R.array.f_ratings);

		Spinner rateS = (Spinner) thisFrag.findViewById(R.id.rateSpinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
																android.R.layout.simple_spinner_item, fRates);
		rateS.setAdapter(adapter);

		Spinner sizeS = (Spinner) thisFrag.findViewById(R.id.sizeSpinner);
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
	private void spinSend() {
		Spinner rateS = (Spinner) thisFrag.findViewById(R.id.rateSpinner);
		Spinner sizeS = (Spinner) thisFrag.findViewById(R.id.sizeSpinner);

		int fSizeIndex = sizeS.getSelectedItemPosition();

		switch (rateS.getSelectedItemPosition()) {
			case (0): // 150
				setVals(fSizeIndex, R.array.f_stats150);
				break;
			case (1): // 300
				setVals(fSizeIndex, R.array.f_stats300);
				break;
			case (2): // 400
				setVals(fSizeIndex, R.array.f_stats400);
				break;
			case (3): // 600
				setVals(fSizeIndex, R.array.f_stats600);
				break;
			case (4): //900
				setVals(fSizeIndex, R.array.f_stats900);
				break;
			case (5): //1500
				setVals(fSizeIndex, R.array.f_stats1500);
		}	
	}

	private void setVals(int fSizeIndex, int statArrInt) {
		String[] statArr = getResources().getStringArray(statArrInt);
        String[] studArr = getResources().getStringArray(R.array.stud_sizes);
		int studIndex = 0;
		
		String[] statSplit = statArr[fSizeIndex].split(",");  
		if(statSplit.length > 4 || statSplit.length < 1) {
			sDiamVal.setText("resource err");
			wrenchVal.setText("err");
			driftVal.setText("err");
			sCountVal.setText("err");
			sLengthVal.setText("err");
			b7Val.setText("err");
			b7mVal.setText("err");
			
			return;
		}
		// statSplit[stud string, stud index, count, length]

		try {
			studIndex = Integer.parseInt(statSplit[1]);
		} catch(NumberFormatException nfe) {
			System.out.println("Could not parse " + nfe);
		}
		String[] studSplit = studArr[studIndex].split(",");
		// studSplit [diam string, wrench size, drift size, b7 torque, b7m torque]

		sDiamVal.setText(statSplit[0] + "\"");
		wrenchVal.setText(studSplit[1] + "\"");
		driftVal.setText(studSplit[2] + "\"");
		sCountVal.setText(statSplit[2]);
		sLengthVal.setText(statSplit[3] + "\"");
		b7Val.setText(studSplit[4] + " ft-lbs");
		b7mVal.setText(studSplit[3] + " ft-lbs");
	}
}
