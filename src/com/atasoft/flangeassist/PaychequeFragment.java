package com.atasoft.flangeassist;

import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

public class PaychequeFragment extends Fragment implements OnClickListener
{
	float wageRates[] = new float[12];
    View thisFrag;
	Spinner sunSpin;
	Spinner monSpin;
	Spinner tueSpin;
	Spinner wedSpin;
	Spinner thuSpin;
	Spinner friSpin;
	Spinner satSpin;
	
	Spinner mealSpin;
	Spinner loaSpin;
	Spinner wageSpin;
	
	CheckBox taxVal;
	CheckBox eiVal;
	CheckBox cppVal;
	CheckBox duesVal;
	
	CheckBox monHol;
	CheckBox tueHol;
	CheckBox wedHol;
	CheckBox thuHol;
	CheckBox friHol;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.paycalc, container, false);
        thisFrag = v;
        setupSpinners();

        Button bClr = (Button) v.findViewById(R.id.clr_but);
        Button bTens = (Button) v.findViewById(R.id.tens_but);
		Button bTwelves = (Button) v.findViewById(R.id.twelves_but);
		Button bFour = (Button) v.findViewById(R.id.four_but);
		Button bNight = (Button) v.findViewById(R.id.night_but);
		Button bTravel = (Button) v.findViewById(R.id.travel_but);
		taxVal = (CheckBox) v.findViewById(R.id.tax_val);
		cppVal = (CheckBox) v.findViewById(R.id.cpp_val);
		eiVal = (CheckBox) v.findViewById(R.id.ei_val);
		duesVal = (CheckBox) v.findViewById(R.id.dues_val);
		taxVal.setChecked(true);
		cppVal.setChecked(true);
		eiVal.setChecked(true);
		duesVal.setChecked(true);
		
		monHol = (CheckBox) v.findViewById(R.id.hol_mon);
		tueHol = (CheckBox) v.findViewById(R.id.hol_tue);
		wedHol = (CheckBox) v.findViewById(R.id.hol_wed);
		thuHol = (CheckBox) v.findViewById(R.id.hol_thu);
		friHol = (CheckBox) v.findViewById(R.id.hol_fri);
		
		bClr.setOnClickListener(this);
		bTens.setOnClickListener(this);
		bTwelves.setOnClickListener(this);
		bFour.setOnClickListener(this);
		bNight.setOnClickListener(this);
		bTravel.setOnClickListener(this);
		taxVal.setOnClickListener(this);
		cppVal.setOnClickListener(this);
		eiVal.setOnClickListener(this);
		duesVal.setOnClickListener(this);
		monHol.setOnClickListener(this);
		tueHol.setOnClickListener(this);
		wedHol.setOnClickListener(this);
		thuHol.setOnClickListener(this);
		friHol.setOnClickListener(this);
		
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
			case R.id.clr_but:
				preSets(0);
				break;
			case R.id.tens_but:
			    preSets(1);
				break;
			case R.id.twelves_but:
			    preSets(2);
				break;
			default:  // Refreshes calc when other buttons called
			    pushBootan();
				break;
        }
    }

	private void setupSpinners() {
		String workHrs[] = {"0","10","12","13","9","8","7","6","5","4","3","2","1"};
        ArrayAdapter<String> weekAd = new ArrayAdapter<String>(getActivity().getApplicationContext(),
	        android.R.layout.simple_spinner_item, workHrs);
        sunSpin = (Spinner) thisFrag.findViewById(R.id.sunSpin);
		monSpin = (Spinner) thisFrag.findViewById(R.id.monSpin);
		tueSpin = (Spinner) thisFrag.findViewById(R.id.tueSpin);
		wedSpin = (Spinner) thisFrag.findViewById(R.id.wedSpin);
		thuSpin = (Spinner) thisFrag.findViewById(R.id.thuSpin);
		friSpin = (Spinner) thisFrag.findViewById(R.id.friSpin);
		satSpin = (Spinner) thisFrag.findViewById(R.id.satSpin);

		mealSpin = (Spinner) thisFrag.findViewById(R.id.meals_spin);
		loaSpin = (Spinner) thisFrag.findViewById(R.id.loa_spin);
		wageSpin = (Spinner) thisFrag.findViewById(R.id.wageSpin);
		monSpin.setAdapter(weekAd);
        tueSpin.setAdapter(weekAd);
        wedSpin.setAdapter(weekAd);
        thuSpin.setAdapter(weekAd);
        friSpin.setAdapter(weekAd);
        satSpin.setAdapter(weekAd);
        sunSpin.setAdapter(weekAd);

        ArrayAdapter<String> weekCount = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, 
																  new String[]{"0","1","2","3","4","5","6","7"});
        loaSpin.setAdapter(weekCount);
        mealSpin.setAdapter(weekCount);

		String wageArr[] = getResources().getStringArray(R.array.wage_rates);
		String wageStrings[] = new String[wageArr.length];
		for(int i=0; i<wageArr.length; i++) {
			String wageSplit[] = wageArr[i].split(",");
			wageStrings[i] = wageSplit[0];
			wageRates[i] = Float.parseFloat(wageSplit[1]);
	    }

		ArrayAdapter<String> wageAdapt = new ArrayAdapter<String>(getActivity().getApplicationContext(), 
																  android.R.layout.simple_spinner_item, wageStrings);
		wageSpin.setAdapter(wageAdapt);
		wageSpin.setSelection(4);

		sunSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view,
										   int pos, long id) {
					pushBootan();
				}
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});
		monSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view,
										   int pos, long id) {
					pushBootan();
				}
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});
		tueSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view,
										   int pos, long id) {
					pushBootan();
				}
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});
		wedSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view,
										   int pos, long id) {
					pushBootan();
				}
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});
		thuSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view,
										   int pos, long id) {
					pushBootan();
				}
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});
		friSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view,
										   int pos, long id) {
					pushBootan();
				}
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});
		satSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view,
										   int pos, long id) {
					pushBootan();
				}
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});
		loaSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view,
										   int pos, long id) {
					pushBootan();
				}
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});
		mealSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view,
										   int pos, long id) {
					pushBootan();
				}
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});
		wageSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view,
										   int pos, long id) {
					pushBootan();
				}
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});
	}

	private void pushBootan() {
		TextView sTimeText = (TextView) thisFrag.findViewById(R.id.sing_val);
		TextView hTimeText = (TextView) thisFrag.findViewById(R.id.half_val);
		TextView dTimeText = (TextView) thisFrag.findViewById(R.id.doub_val);
		TextView grossVal = (TextView) thisFrag.findViewById(R.id.gross_val);
		TextView exemptVal = (TextView) thisFrag.findViewById(R.id.exempt_val);
		TextView dedVal = (TextView) thisFrag.findViewById(R.id.deduct_val);
		TextView netVal = (TextView) thisFrag.findViewById(R.id.net_val);
		ToggleButton fourToggle = (ToggleButton) thisFrag.findViewById(R.id.four_but);
		ToggleButton nightToggle = (ToggleButton) thisFrag.findViewById(R.id.night_but);
		ToggleButton travelToggle = (ToggleButton) thisFrag.findViewById(R.id.travel_but);

		int weekends[] = {0,0};
		int weekDays[] = {0,0,0,0,0};
		int splitArr[] = {0,0,0};
		boolean fourTens = fourToggle.isChecked();
		//float wage = Float.parseFloat(String(R.string.maint_wage));
		float loaRate = Float.parseFloat(getString(R.string.loa_rate));
		float mealRate = Float.parseFloat(getString(R.string.meal_rate));
		float vacationPay = Float.parseFloat(getString(R.string.vacation_pay));
		double travelRate = Double.parseDouble(getString(R.string.travel_rate));
		int timeSum[] = {0,0,0};

		weekends[0] = Integer.parseInt(satSpin.getSelectedItem().toString());
		weekends[1] = Integer.parseInt(sunSpin.getSelectedItem().toString());
		weekDays[0] = Integer.parseInt(monSpin.getSelectedItem().toString());
		weekDays[1] = Integer.parseInt(tueSpin.getSelectedItem().toString());
		weekDays[2] = Integer.parseInt(wedSpin.getSelectedItem().toString());
		weekDays[3] = Integer.parseInt(thuSpin.getSelectedItem().toString());
		weekDays[4] = Integer.parseInt(friSpin.getSelectedItem().toString());
		int loaCount = Integer.parseInt(loaSpin.getSelectedItem().toString());
		int mealCount = Integer.parseInt(mealSpin.getSelectedItem().toString());
		float wageRate = wageRates[wageSpin.getSelectedItemPosition()];
		boolean[] weekHolidays = {monHol.isChecked(), tueHol.isChecked(), wedHol.isChecked(),thuHol.isChecked(),friHol.isChecked()};
		
		if(fourTens){
			for (int i=0; i<4; i++) {
				splitArr = hrsSplit(weekDays[i], 1);  //mon to thur time and half
				if(weekHolidays[i]) {
					timeSum[2] = timeSum[2] + splitArr[0] + splitArr[1] + splitArr[2];
				} else {
					timeSum[0] = splitArr[0] + timeSum[0];
					timeSum[1] = splitArr[1] + timeSum[1];
					timeSum[2] = splitArr[2] + timeSum[2];
				}
			}
			
			splitArr = hrsSplit(weekDays[4], 2); //Friday time and a half
			if(weekHolidays[4]) {
				timeSum[2] = timeSum[2] + splitArr[0] + splitArr[1] + splitArr[2];
			} else { 
				timeSum[0] = splitArr[0] + timeSum[0];
				timeSum[1] = splitArr[1] + timeSum[1];
				timeSum[2] = splitArr[2] + timeSum[2];
			}
			
		} else {                                     //5 8s weekday
			for (int i=0; i<5; i++) {
				splitArr = hrsSplit(weekDays[i], 0);
				if(weekHolidays[i]) {
					timeSum[2] = timeSum[2] + splitArr[0] + splitArr[1] + splitArr[2];
				} else {
					timeSum[0] = splitArr[0] + timeSum[0];
					timeSum[1] = splitArr[1] + timeSum[1];
					timeSum[2] = splitArr[2] + timeSum[2];
				}
			}				
		}

		for (int i=0; i<2; i++) {                    //weekend
			splitArr = hrsSplit(weekends[i], 3);
			timeSum[0] = splitArr[0] + timeSum[0];
			timeSum[1] = splitArr[1] + timeSum[1];
			timeSum[2] = splitArr[2] + timeSum[2];
		}

		double grossPay = wageRate * (timeSum[0] + (1.5 * timeSum[1]) + (2 * timeSum[2]));

		if(nightToggle.isChecked()) {grossPay = grossPay + (timeSum[0] + timeSum[1] + timeSum[2]) * 3;}

		double grossVac = grossPay * (vacationPay + 1);

		double[] deductions = taxCalc(grossVac, grossPay);  //returns [fed, ab, dues, cpp, ei]
		double deductionsSum = 0;
		if(taxVal.isChecked()) deductionsSum = deductions[0] + deductions[1];
		if(duesVal.isChecked()) deductionsSum += deductions[2];
		if(cppVal.isChecked()) deductionsSum += deductions[3];
		if(eiVal.isChecked()) deductionsSum += deductions[4];

		double exempt = loaCount * loaRate + mealCount * mealRate;
		if(travelToggle.isChecked()) {exempt = exempt + travelRate;}

		double netPay = grossVac - deductionsSum + exempt;

		grossVal.setText("Gross: " + String.format("%.2f", grossVac) + "$");
		exemptVal.setText("Tax Exempt: " + String.format("%.2f", exempt) + "$");
		taxVal.setText("Income Tax: " + String.format("%.2f", deductions[0] + deductions[1]) + "$");
		eiVal.setText("CPP: " + String.format("%.2f", deductions[3]) + "$");
		cppVal.setText("EI: " + String.format("%.2f", deductions[4]) + "$");
		duesVal.setText("Dues: " + String.format("%.2f", deductions[2]) + "$");
		dedVal.setText("Deductions: " + String.format("%.2f", deductionsSum) + "$");
		netVal.setText("Takehome: " + String.format("%.2f", netPay) + "$");
		sTimeText.setText("1.0x: " + Integer.toString(timeSum[0]));
		hTimeText.setText("1.5x: " + Integer.toString(timeSum[1]));
		dTimeText.setText("2.0x: " + Integer.toString(timeSum[2]));
	}
	private void preSets(int index){
        if(index == 0) {
        	sunSpin.setSelection(0, false);
        	monSpin.setSelection(0, false);
        	tueSpin.setSelection(0, false);
        	wedSpin.setSelection(0, false);
        	thuSpin.setSelection(0, false);
        	friSpin.setSelection(0, false);
        	satSpin.setSelection(0, false);
        	mealSpin.setSelection(0, false);
        	loaSpin.setSelection(0, false);
        	return;
        } else {
        	sunSpin.setSelection(index, false);
        	monSpin.setSelection(index, false);
        	tueSpin.setSelection(index, false);
        	wedSpin.setSelection(index, false);
        	thuSpin.setSelection(index, false);
        	friSpin.setSelection(index, false);
        	satSpin.setSelection(index, false);
        	mealSpin.setSelection(0, false);
        	loaSpin.setSelection(0, false);        	
        }
        if(index == 2) {
        	mealSpin.setSelection(7, false);
        }
        return;
    }

	private int[] hrsSplit(int hrs, int day) { //day 0 = 5-8s, 1 = 4-10s mon-thu, 2 = 4-10s fri, 3 = weekend
		int sTime = 0;
		int hTime = 0;
		int dTime = 0;

		switch(day) {
			case 0:
				if (hrs > 10) {
					dTime = hrs - 10;
				}
				if (hrs > 8) {
					hTime = hrs - dTime - 8;
				}
				sTime = hrs - dTime - hTime;
				break;
			case 1:
				if(hrs > 10) {
					dTime = hrs - 10;
				}
				sTime = hrs - dTime;
				break;
			case 2:
				if(hrs > 10) {
					dTime = hrs - 10;
				}
				hTime = hrs - dTime;
				break;
			case 3:
				dTime = hrs;
				break;
		}

		return new int[]{sTime, hTime, dTime};	
	}

	private double[] taxCalc(double gross, double grossNoVac){
		double anGross = gross * 52;
		double bracket[] = {0,0,0,0};
		double diff[] = {0,0,0};
		double rate[] = {0, 0, 0, 0};
		double fedConst[] = {0,0,0,0};
		double fedTax = 0;
		double abTax = 0;

		String brackStr[] = getResources().getStringArray(R.array.tax_brackets);
		String rateStr[] = getResources().getStringArray(R.array.tax_rates);
		String fedCStr[] = getResources().getStringArray(R.array.fed_const);
		double fedTaxCred = Double.parseDouble(getString(R.string.fed_taxcred));
		double abTaxCred = Double.parseDouble(getString(R.string.ab_taxcred));
		double duesRate = Double.parseDouble(getString(R.string.dues_rate));
		double cppRate = Double.parseDouble(getString(R.string.cpp_rate));
		double eiRate = Double.parseDouble(getString(R.string.ei_rate));

		for(int i=0; i<4; i++){
			bracket[i] = Double.parseDouble(brackStr[i]);
			rate[i] = Double.parseDouble(rateStr[i]);
			fedConst[i] = Double.parseDouble(fedCStr[i]);

			if(i < 3){
				diff[i] = bracket[i + 1] - bracket [i];
			}
		}

		if(anGross < bracket[1]) {
			fedTax = anGross * rate[0] - fedConst[0] - fedTaxCred;
		}
		if(anGross < bracket[2]) {
			fedTax = anGross * rate[1] - fedConst[1] - fedTaxCred;
		}
		if(anGross < bracket[3]) {
			fedTax = anGross * rate[2] - fedConst[2] - fedTaxCred;	
		}
		if(anGross >= bracket[3]) {
			fedTax = anGross * rate[3] - fedConst[3] - fedTaxCred;
		}

		abTax = (anGross * 0.1) - abTaxCred;
		double dues = grossNoVac * duesRate;

		if(abTax < 0){abTax = 0;}
		if(fedTax < 0){fedTax = 0;}

		return new double[]{fedTax / 52, 
			abTax / 52, 
			dues, 
			gross * cppRate, 
			gross * eiRate};
	}
}
