package com.atasoft.flangeassist.fragments;

import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.atasoft.flangeassist.*;

public class TorqueFragment extends Fragment implements OnClickListener
{
	int[] patBase;
    View thisFrag;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
    	
        View v = inflater.inflate(R.layout.torquepat, container, false);
        thisFrag = v;
        setupSpinners();
        
        Button b = (Button) v.findViewById(R.id.submit);
        b.setOnClickListener(this);
        
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
			case R.id.submit:
				submit();
				break;
        }
    }
	
		
	private void setupSpinners() {
		String[] patterns = {"4-point", "8-point"};
		Spinner patSpin = (Spinner) thisFrag.findViewById(R.id.patSpin);
		ArrayAdapter<String> patAd = new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_spinner_item, patterns);
		patSpin.setAdapter(patAd);
	}
	
	private void submit() {
		Spinner patSpin = (Spinner) thisFrag.findViewById(R.id.patSpin);
		if(patSpin.getSelectedItemPosition() == 1) {
			patBase = new int[] {1,5,3,7,2,6,4,8};
		} else {
			patBase = new int[] {1,3,2,4};
		}
		String torqueString = genTorquePattern(patBase);
		
		TextView patReturn = (TextView) thisFrag.findViewById(R.id.torqueReturn);
		patReturn.setText(torqueString);
	}
	
	private String genTorquePattern(int[] patBase) {
		String errInt = getString(R.string.errInt);
		String errEven = getString(R.string.errEven);
		String retString = "";
		EditText studBox = (EditText) thisFrag.findViewById(R.id.studBox);
		int studInt = parseInputInt(studBox);
		
		if (studInt < 8 || studInt > 200) return errInt;
		int rem = studInt % 2;
		if (rem > 0) return errEven;

		for(int i = 0; i < patBase.length; i++) { //Muchos elegante
			for(int j = 0; j + patBase[i] <= studInt; j = j + patBase[patBase.length - 1]) {
				retString = retString + ", " + Integer.toString(patBase[i] + j);
			}
		}
		return retString.substring(2);
	}
	
	private int parseInputInt(EditText editText) { 
		try {
			return Integer.parseInt(editText.getText().toString());
		} catch (NumberFormatException e) {
			return 0;
		}
	}
}
