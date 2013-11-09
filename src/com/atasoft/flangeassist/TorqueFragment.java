package com.atasoft.flangeassist;

import android.*;
import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

public class TorqueFragment extends Fragment implements OnClickListener
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		setupSpinners();					 
        View v = inflater.inflate(R.layout.about, container, false);

        Button b = (Button) v.findViewById(R.id.torqueBut);
        b.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
			case R.id.torqueBut:
			    
				break;
        }
    }
	
		
	public void setupSpinners() {
		String[] patterns = {"4-point", "8-point"};
		Spinner patSpin = (Spinner) findViewById(R.id.patSpin);
		ArrayAdapter<String> patAd = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, patterns);
		patSpin.setAdapter(patAd);
	}

	
	
	
}
