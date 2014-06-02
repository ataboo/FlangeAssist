package com.atasoft.flangeassist.fragments;

import android.os.*;
import android.support.v4.app.*;
import android.util.Log;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import com.atasoft.flangeassist.*;
import com.atasoft.helpers.*;

public class UnitConFragment extends Fragment implements OnClickListener
{
	View thisFrag;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

        thisFrag = inflater.inflate(R.layout.unit_conv , container, false);
        setupSpinners();
        return thisFrag;
    }
	
	@Override
    public void onClick(View v) {
        switch (v.getId()) {
			case R.id.unit_conv_go_button:
				goPush();
				break;
        }
	}
	
	Spinner typeSpin;
	Spinner unitSpin1;
	Spinner unitSpin2;
	EditText inBox;
	TextView outBox;
	Button goButton;
	ConvDataHold dataHold;
	private void setupSpinners(){
		this.typeSpin = (Spinner) thisFrag.findViewById(R.id.unit_conv_type_spinner);
		this.unitSpin1 = (Spinner) thisFrag.findViewById(R.id.unit_conv_unit1_spinner);
		this.unitSpin2 = (Spinner) thisFrag.findViewById(R.id.unit_conv_unit2_spinner);
		this.inBox = (EditText) thisFrag.findViewById(R.id.unit_conv_text_input);
		this.outBox = (TextView) thisFrag.findViewById(R.id.unit_conv_text_output);
		this.goButton = (Button) thisFrag.findViewById(R.id.unit_conv_go_button);
		this.dataHold = new ConvDataHold();
		
		goButton.setOnClickListener(this);
		
		ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, ConvDataHold.typeStrings);
		typeSpin.setAdapter(typeAdapter);
		
		typeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		        public void onItemSelected(AdapterView<?> parent, View view,
										   int pos, long id) {refreshUnits();}
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});
		refreshUnits();
			
		return;
	}
	
	String oldType = null;
	//Checks if selected type has changed and populate units accordingly
	private void refreshUnits(){
		String type = (String) typeSpin.getSelectedItem();
		if(type != oldType){
			String[] unitStrings = dataHold.getUnitNames(type);
			ArrayAdapter<String> unitAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, unitStrings);
			unitSpin1.setAdapter(unitAdapter);
			unitSpin2.setAdapter(unitAdapter);
			if(unitStrings.length > 1){  //Do(d)ge index out of range... Such Index.
				unitSpin2.setSelection(1);
			}
		}
		this.oldType = type;
	}
	
	private void goPush(){
		String inText = inBox.getText().toString();
		double inVal = 0;
		try{
			inVal = Double.parseDouble(inText);
		} catch (NumberFormatException e) {
			Log.e("UnitConFrag", "failed to parse input.");
		}
		String unit1 = unitSpin1.getSelectedItem().toString();
		String unit2 = unitSpin2.getSelectedItem().toString();
		String unitShorthand = dataHold.getUnit(oldType, unit2);
		double result = dataHold.convertValue(inVal, oldType, unit1, unit2);
		//Toast.makeText(getActivity(), String.format("Result is: %.2f.", result), Toast.LENGTH_SHORT).show();
		outBox.setText(String.format("%s %s", result, unitShorthand));
		dataHold.nearestSixteenth(2.8d);
		dataHold.nearestSixteenth(-2.13d);
		//TODO fraction output
		
	}
	
	
}
