package com.atasoft.flangeassist;
import android.annotation.*;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class TorquePattern extends Activity {
    @SuppressLint("NewApi")
	
	int[] patBase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.torquepat);

        // Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
		setupSpinners();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
			case android.R.id.home:
				super.finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
        }
	}
	
	public void setupSpinners() {
		String[] patterns = {"4-point", "8-point"};
		Spinner patSpin = (Spinner) findViewById(R.id.patSpin);
		ArrayAdapter<String> patAd = new ArrayAdapter<String>(this,
		    android.R.layout.simple_spinner_item, patterns);
		patSpin.setAdapter(patAd);
	}
	
	public void submit(View view) {
		Spinner patSpin = (Spinner) findViewById(R.id.patSpin);
		if(patSpin.getSelectedItemPosition() == 1) {
			patBase = new int[] {1,5,3,7,2,6,4,8};
		} else {
			patBase = new int[] {1,3,2,4};
		}
		String torqueString = genTorquePattern(patBase);
		
		TextView patReturn = (TextView) findViewById(R.id.torqueReturn);
		patReturn.setText(torqueString);
	}
	
	private String genTorquePattern(int[] patBase) {
		String errInt = getString(R.string.errInt);
		String errEven = getString(R.string.errEven);
		String retString = "";
		EditText studBox = (EditText) findViewById(R.id.studBox);
		int studInt = parseInputInt(studBox);
		
		if (studInt < 8 || studInt > 250) return errInt;
		int rem = studInt % 2;
		if (rem > 0) return errEven;

		for(int i = 0; i < patBase.length; i++) { //Muchos elegante
			for(int j = 0; j + patBase[i] <= studInt; j = j + patBase[patBase.length - 1]) {
				retString = retString + ", " + Integer.toString(patBase[i] + j);
			}
		}
		return retString.substring(2);
	}
	
	//Given res for editfield parses to int
	private int parseInputInt(EditText editText) { 
		try {
			return Integer.parseInt(editText.getText().toString());
		} catch (NumberFormatException e) {
			return 0;
		}
	}
}
