package com.atasoft.flangeassist;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.support.v4.app.NavUtils;

public class AsmeFlange extends Activity {
    @Override
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flanges);

        // Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

		// int fRates[] = {150, 300, 600, 900, 1500};
		// String fSizes[] = {"1/2in", "1in", "2in", "3in", "4in"};
		// String fRates[] = {"150", "300", "600", "900", "1500"};
		String fSizes[] = getResources().getStringArray(R.array.f_sizes);
		String fRates[] = getResources().getStringArray(R.array.f_ratings);

		Spinner rateS = (Spinner) findViewById(R.id.rateSpinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
																android.R.layout.simple_spinner_item, fRates);
		rateS.setAdapter(adapter);

		Spinner sizeS = (Spinner) findViewById(R.id.sizeSpinner);
		ArrayAdapter<String> adaptor2 = new ArrayAdapter<String>(this,
																 android.R.layout.simple_spinner_item, fSizes);
		sizeS.setAdapter(adaptor2);

		rateS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		        public void onItemSelected(AdapterView<?> parent, View view,
										   int pos, long id) {
					spinSend(view);
				}

				public void onNothingSelected(AdapterView<?> parent) {
				}
			});
		sizeS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view,
										   int pos, long id) {
					spinSend(view);
				}

				public void onNothingSelected(AdapterView<?> parent) {
				}
			});	
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
	
	public void spinSend(View view) {
		Spinner rateS = (Spinner) findViewById(R.id.rateSpinner);
		Spinner sizeS = (Spinner) findViewById(R.id.sizeSpinner);

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
		TextView sDiamVal = (TextView) findViewById(R.id.sDiamVal);
		TextView wrenchVal = (TextView) findViewById(R.id.wrenchVal);
		TextView driftVal = (TextView) findViewById(R.id.driftVal);
		TextView sCountVal = (TextView) findViewById(R.id.sCountVal);
		TextView sLengthVal = (TextView) findViewById(R.id.sLengthVal);

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
		// studSplit [diam string, wrench size, drift size]

		sDiamVal.setText(statSplit[0] + "\"");
		wrenchVal.setText(studSplit[1]);
		driftVal.setText(studSplit[2]);
		sCountVal.setText(statSplit[2]);
		sLengthVal.setText(statSplit[3] + "\"");
	}
}
