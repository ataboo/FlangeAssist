package com.atasoft.flangeassist.fragments;

import android.content.*;
import android.os.*;
import android.preference.*;
import android.support.v4.app.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.atasoft.flangeassist.*;

public class WageCPIEstimate extends Fragment implements OnClickListener
{
    View thisFrag;
	SharedPreferences prefs;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.wage_estimate_layout, container, false);
        this.thisFrag = v;
        
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());	
		
		setupSpinners();
		
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
			case R.id.submit:
				//Submit
				break;
        }
    }

	Spinner cpiWageSpin;
	NumberPicker cpiRatePick;
	NumberPicker wtcPricePick;
	private void setupSpinners() {
		String[][] wageRates = {{"Journeyman", "Custom"},
								{"40", "50"}};
		
		cpiWageSpin = (Spinner) thisFrag.findViewById(R.id.cpi_wagespin);
		ArrayAdapter<String> wageAdaptor = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, 
																  wageRates[0]);
        cpiWageSpin.setAdapter(wageAdaptor);
        
		
		cpiRatePick = (NumberPicker) thisFrag.findViewById(R.id.cpi_picker);
		wtcPricePick = (NumberPicker) thisFrag.findViewById(R.id.wtc_picker);
		
		setNumberPicker(cpiRatePick, "CPI Rate", 0f, 10f, 0.1f);
		setNumberPicker(wtcPricePick, "Oil Price", 30f, 200f, 1f);
		
		cpiRatePick.setValue(28); //2.7
		wtcPricePick.setValue(81); //110
		return;
	}
	
	private void setNumberPicker(NumberPicker picker, String pickName, float floor, float ceiling, float interval){
		int pickLength = (int) ((ceiling - floor) / interval + 2);
		//Log.w("FlangeAssist WageCPIEstimate", String.format("floor: %.2f, ceiling: %.2f, interval: %.2f, pickLength: %2d", floor, ceiling, interval, pickLength)); 
		pickLength = pickLength > 0 ? pickLength : 1;
		String[] pickStrings = new String[pickLength];
		pickStrings[0] = pickName;
		
		//expand later if more precision needed
		String formatString = interval < 1 ? "%.1f" : "%.0f";
		
		float floatCount = floor;
		if(pickLength > 0){
			for(int i = 1; i<pickLength; i++) {
				pickStrings[i] = String.format(formatString, floatCount);
				floatCount += interval;
				Log.w("FlangeAssist WageCPIEstimate", String.format("Added %s in position %2d", pickStrings[i], i));
			}
		}
		
		//String[] pickStrings = {"Wage", "1", "2.5", "3"};
		
		picker.setMaxValue(pickStrings.length - 1);
		picker.setMinValue(0);
		picker.setDisplayedValues(pickStrings);
		
		return;
	}
}
