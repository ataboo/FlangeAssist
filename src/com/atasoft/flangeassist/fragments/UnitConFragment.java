package com.atasoft.flangeassist.fragments;

import android.os.*;
import android.support.v4.app.*;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.atasoft.flangeassist.*;
import com.atasoft.helpers.*;

import android.text.*;

public class UnitConFragment extends Fragment //implements OnClickListener
{
	View thisFrag;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

        thisFrag = inflater.inflate(R.layout.unit_conv , container, false);
        setupConvSpinners();
		setupShapeSpinners();
        setEditListeners();
		return thisFrag;
    }
	
	private void setEditListeners(){
		inBox.addTextChangedListener(new TextWatcher(){
			@Override
			public void afterTextChanged(Editable s){
				updateConversion();
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after){}
			@Override
			public void onTextChanged(CharSequence s, int start, int count, int after){}
		});
		for(int i=0; i < shapeInArr.length; i++){
			shapeInArr[i].addTextChangedListener(new TextWatcher(){
					@Override
					public void afterTextChanged(Editable s){
						updateShapeCalc();
					}
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after){}
					@Override
					public void onTextChanged(CharSequence s, int start, int count, int after){}
			});
		}
	}
	
	//------------------Converter Functions------------------
	Spinner typeSpin;
	Spinner unitSpin1;
	Spinner unitSpin2;
	EditText inBox;
	TextView outBox;
	TextView fracBox;
	Button goButton;
	ConvDataHold dataHold;
	private void setupConvSpinners(){
		this.typeSpin = (Spinner) thisFrag.findViewById(R.id.unit_conv_type_spinner);
		this.unitSpin1 = (Spinner) thisFrag.findViewById(R.id.unit_conv_unit1_spinner);
		this.unitSpin2 = (Spinner) thisFrag.findViewById(R.id.unit_conv_unit2_spinner);
		this.inBox = (EditText) thisFrag.findViewById(R.id.unit_conv_text_input);
		this.outBox = (TextView) thisFrag.findViewById(R.id.unit_conv_text_output);
		this.fracBox = (TextView) thisFrag.findViewById(R.id.unit_conv_frac_output);
		this.dataHold = new ConvDataHold();

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
	
	private void updateConversion(){
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
		String fracResult = dataHold.makeFraction(result, 16);
		
		outBox.setText(String.format("%s %s", result, unitShorthand));
		fracBox.setText(String.format("%s %s (Nearest 16th)", fracResult, unitShorthand));
	}
	
	//--------------------Shape Calc Functions----------------
		
	ShapeCalcHold shapeCalc;
	Spinner shapeTypeSpin;
	ImageView shapeImage;
	TextView shapeVolBox;
	TextView shapeSurfBox;
	EditText[] shapeInArr;
	TextView[] shapeLabelArr;
	private void setupShapeSpinners(){
		this.shapeCalc = new ShapeCalcHold();
		this.shapeTypeSpin = (Spinner) thisFrag.findViewById(R.id.shapecalc_typeSpin);
		this.shapeImage = (ImageView) thisFrag.findViewById(R.id.shapecalc_image);
		this.shapeInArr = new EditText[3];
		this.shapeInArr[0] = (EditText) thisFrag.findViewById(R.id.shapecalc_input1);
		this.shapeInArr[1] = (EditText) thisFrag.findViewById(R.id.shapecalc_input2);
		this.shapeInArr[2] = (EditText) thisFrag.findViewById(R.id.shapecalc_input3);
		this.shapeLabelArr = new TextView[3];
		this.shapeLabelArr[0] = (TextView) thisFrag.findViewById(R.id.shapecalc_label1);
		this.shapeLabelArr[1] = (TextView) thisFrag.findViewById(R.id.shapecalc_label2);
		this.shapeLabelArr[2] = (TextView) thisFrag.findViewById(R.id.shapecalc_label3);
		this.shapeVolBox = (TextView) thisFrag.findViewById(R.id.shapecalc_volbox);
		this.shapeSurfBox = (TextView) thisFrag.findViewById(R.id.shapecalc_surfbox);		

		ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, ShapeCalcHold.SHAPE_TYPES);
		shapeTypeSpin.setAdapter(typeAdapter);

		shapeTypeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
					refreshShapeFields();
					updateShapeCalc();
				}
				public void onNothingSelected(AdapterView<?> parent) {}
			});
		refreshShapeFields();
		for(int i=0; i < shapeInArr.length; i++){
			shapeInArr[i].setText("0");
		}
		
		return;
	}

	String selectedShape;
	private void refreshShapeFields(){
		this.selectedShape = (String) shapeTypeSpin.getSelectedItem();
		String[] labelStrings = shapeCalc.getLabelStrings(selectedShape);
		shapeImage.setImageResource(getImageResource(selectedShape));
		for(int i=0; i < shapeLabelArr.length; i++){
			if(i+1 > labelStrings.length) {
				shapeLabelArr[i].setText("");
				shapeInArr[i].setEnabled(false);
			} else {
				shapeInArr[i].setEnabled(true);
				shapeLabelArr[i].setText(labelStrings[i]);
			}
		}
	
	}
	
	//rather put this here and not have to pass resources to shapecalchold
	private int getImageResource(String stringType){
		int type = shapeCalc.getType(stringType);
		int resID;
		switch(type){
		case 0:  //Cylinder
			resID = R.drawable.shape_cyl;
			break;
		case 1:  //Sphere
			resID = R.drawable.shape_sph;
			break;
		case 2:  //Box
			resID = R.drawable.shape_box;
			break;
		case 3:  //Rectangle
			resID = R.drawable.shape_rec;
			break;
		default: //Circle
			resID = R.drawable.shape_circ;
			break;
	}
		return resID;
	}

	private void updateShapeCalc(){
		this.selectedShape = (String) shapeTypeSpin.getSelectedItem();
		double[] vals = new double[]{0d,0d,0d};
		
		for(int i=0; i < shapeInArr.length; i++){
			vals[i]= getDoubleFromEdit(shapeInArr[i]);
		}
		String[] outputs = shapeCalc.getValues(selectedShape, vals);
		shapeVolBox.setText(outputs[0]);
		shapeSurfBox.setText(outputs[1]);
	}

	private double getDoubleFromEdit(EditText eText){
		double retDouble;
		try{
			retDouble = Double.parseDouble(eText.getText().toString());
		} catch (NumberFormatException e){
			Log.e("UnitConFragment", "Can't parse double in shapeCalc input.");
			return 0d;
		}
		
		return retDouble;
	}	
}
