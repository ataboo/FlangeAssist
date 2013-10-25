package com.atasoft.flangeassist;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.text.method.*;

public class PayCalculator extends Activity {
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paycalc);

        // Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
}