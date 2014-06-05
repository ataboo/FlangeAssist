package com.atasoft.flangeassist.fragments;

import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.atasoft.flangeassist.*;

public class WeldingFrag extends Fragment implements NumberPicker.OnValueChangeListener
{
    View thisFrag;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.welding_reference, container, false);
        this.thisFrag = v;

		setupSpinners();
		return v;
    }
	
	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
    	//switch(picker.getId()){}
		updateOutputs();
		
	}
	
	public static final String[][] STRENGTHS = new String[][] {
		{"60","70","80", "90", "100", "110", "120"},
		{"60,000 PSI (CSA 43 MPa)", 
		"70,000 PSI (CSA 49 MPa)", 
		"80,000 PSI (CSA 55 MPa)",
		"90,000 PSI (CSA 62 MPa)",
		"100,000 PSI (CSA 69 MPa)",
		"110,000 PSI (CSA 76 MPa)",
		"120,000 PSI (CSA 83 MPa)"}};
		
	public static final String[][] POSITIONS = new String[][] {
		{"1","2","3"},
		{"All Positions", "Flat and Horizontal", "Flat Only"}
	};
	
	public static final String[][] COATINGS = new String[][] {
		{"0","1","2","3","4","5","6","7","8"},
		{"High Cellulose Sodium.",
		"High Cellulose Potassium.",
		"High Titania Sodium.",
		"High Titania Potassium.",
		"Iron Powder Titania.",
		"Low Hydrogen Sodium.",
		"Low Hydrogen Potassium.",
		"Iron Powder, Iron Oxide.",
		"Iron Powder Low Hydrogen."},
		{"DCRP", "AC, DCSP", "AC, DCSP", "AC, DCRP", "AC, DCRP, DCSP", "DCRP", "AC, DCRP", "AC, DCRP, DCSP", "AC, DCRP"}
	};
	
	public static final String footNotes = 
		"*DCRP = Reverse Polarity (Electrode +ve).\n*DCSP = Straight Polarity (Electrode -ve)";
	
	public static final String EXCEPTION = "AC, DCRP, DCSP"; //xx20
		
	private NumberPicker strengthPicker;
	private NumberPicker positionPicker;
	private NumberPicker coatingPicker;
	//private NumberPicker suffixPicker;
	private TextView strengthBlurb;
	private TextView positionBlurb;
	private TextView coatingBlurb;
	private TextView polarityBlurb;
	private TextView footNoteText;
	private void setupSpinners() {
		this.strengthPicker = (NumberPicker) thisFrag.findViewById(R.id.weld_strength_picker);
		this.positionPicker = (NumberPicker) thisFrag.findViewById(R.id.weld_position_picker);
		this.coatingPicker = (NumberPicker) thisFrag.findViewById(R.id.weld_coating_picker);
		//this.suffixPicker = (NumberPicker) thisFrag.findViewById(R.id.weld_suffix_picker);
		this.strengthBlurb = (TextView) thisFrag.findViewById(R.id.weld_strengthBlurb);
		this.positionBlurb = (TextView) thisFrag.findViewById(R.id.weld_positionBlurb);
		this.coatingBlurb = (TextView) thisFrag.findViewById(R.id.weld_coatingBlurb);
		this.polarityBlurb = (TextView) thisFrag.findViewById(R.id.weld_polarityBlurb);
		this.footNoteText = (TextView) thisFrag.findViewById(R.id.weld_footNotes);
		
		footNoteText.setText(footNotes);
		
		setPicker(strengthPicker, STRENGTHS[0]);
		setPicker(positionPicker, POSITIONS[0]);
		setPicker(coatingPicker, COATINGS[0]);
		
		strengthPicker.setOnValueChangedListener(this);
		positionPicker.setOnValueChangedListener(this);
		coatingPicker.setOnValueChangedListener(this);
		
		updateOutputs();
		return;
	}
	
	private void setPicker(NumberPicker picker, String[] strings){
		picker.setMinValue(0);
		picker.setMaxValue(strings.length - 1);
		picker.setDisplayedValues(strings);
		picker.setWrapSelectorWheel(false);
	}
	
	private void updateOutputs(){
		int coatingVal = coatingPicker.getValue();
		int posVal = positionPicker.getValue();
		//xx20, (posVal[1] = "2")
		String polarityString = (posVal == 1 && coatingVal == 0) ? EXCEPTION : COATINGS[2][coatingVal];
		
		strengthBlurb.setText(STRENGTHS[1][strengthPicker.getValue()]);	
		positionBlurb.setText(POSITIONS[1][posVal]);
		coatingBlurb.setText(COATINGS[1][coatingVal]);
		polarityBlurb.setText(polarityString);
	}
	
	
}
