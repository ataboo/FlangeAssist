package com.atasoft.flangeassist;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

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
		}
	}

	private void setVals(int fSizeIndex, int statArrInt) {
		String[] statArr = getResources().getStringArray(statArrInt);

		TextView sDiamVal = (TextView) findViewById(R.id.sDiamVal);
		TextView wrenchVal = (TextView) findViewById(R.id.wrenchVal);
		TextView driftVal = (TextView) findViewById(R.id.driftVal);
		TextView sCountVal = (TextView) findViewById(R.id.sCountVal);
		TextView sLengthVal = (TextView) findViewById(R.id.sLengthVal);

		String[] statSplit = statArr[fSizeIndex].split(",");

		// statSplit[stud diam, wrench, drift, count, length]

		sDiamVal.setText(statSplit[0] + "\"");
		wrenchVal.setText(statSplit[1] + "\"");
		driftVal.setText(statSplit[2] + "\"");
		sCountVal.setText(statSplit[3]);
		sLengthVal.setText(statSplit[4] + "\"");
	}
}
