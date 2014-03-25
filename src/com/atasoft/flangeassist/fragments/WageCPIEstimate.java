package com.atasoft.flangeassist.fragments;

import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.atasoft.flangeassist.*;

public class WageCPIEstimate extends Fragment implements OnClickListener
{
    View thisFrag;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.wage_estimate_layout, container, false);
        this.thisFrag = v;
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

	NumberPicker wageTensPick;
	NumberPicker wageCentsPick;
	NumberPicker cpiRatePick;
	NumberPicker wtcPricePick;
	private void setupSpinners() {
		wageTensPick = (NumberPicker) thisFrag.findViewById(R.id.wage_tens_picker);
		wageCentsPick = (NumberPicker) thisFrag.findViewById(R.id.wage_cents_picker);
		cpiRatePick = (NumberPicker) thisFrag.findViewById(R.id.cpi_picker);
		wtcPricePick = (NumberPicker) thisFrag.findViewById(R.id.wtc_picker);
	}
}
