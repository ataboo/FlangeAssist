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
    private void setupSpinners() {
		this.jPuller = new JsonPuller(thisFrag);
		String fSizes[] = getResources().getStringArray(R.array.f_sizes);
		String fRates[] = getResources().getStringArray(R.array.f_ratings);

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
	}
	
	String[] valArray;
	private void spinSend() {
		int fSizeIndex = sizeS.getSelectedItemPosition();
		int fRateIndex = rateS.getSelectedItemPosition();
		
		this.valArray = jPuller.getValArray(fSizeIndex, fRateIndex);

		/*
		switch (rateS.getSelectedItemPosition()) {
			case (JsonPuller.RATE_150): // 150
				setVals(fSizeIndex, R.array.f_stats150);
				break;
			case (JsonPuller.RATE_300): // 300
				setVals(fSizeIndex, R.array.f_stats300);
				break;
			case (JsonPuller.RATE_400): // 400
				setVals(fSizeIndex, R.array.f_stats400);
				break;
			case (RATE_600): // 600
				setVals(fSizeIndex, R.array.f_stats600);
				break;
			case (RATE_900): //900
				setVals(fSizeIndex, R.array.f_stats900);
				break;
			case (RATE_1500): //1500
				setVals(fSizeIndex, R.array.f_stats1500);
		}
		*/
	}
	
	private void setVals(int fSizeIndex, int statArrInt) {
		String[] statArr = getResources().getStringArray(statArrInt);
        String[] studArr = getResources().getStringArray(R.array.stud_sizes);
		int studIndex = 0;
		TextView sDiamVal = (TextView) thisFrag.findViewById(R.id.sDiamVal);
		TextView wrenchVal = (TextView) thisFrag.findViewById(R.id.wrenchVal);
		TextView driftVal = (TextView) thisFrag.findViewById(R.id.driftVal);
		TextView sCountVal = (TextView) thisFrag.findViewById(R.id.sCountVal);
		TextView sLengthVal = (TextView) thisFrag.findViewById(R.id.sLengthVal);
		TextView b7Val = (TextView) thisFrag.findViewById(R.id.b7Val);
		TextView b7mVal = (TextView) thisFrag.findViewById(R.id.b7MVal);

		String[] statSplit = statArr[fSizeIndex].split(",");  
		if(statSplit.length > 4 || statSplit.length < 1) {
			sDiamVal.setText("resource err");
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
