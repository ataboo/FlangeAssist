package com.atasoft.flangeassist;

import android.content.*;
import android.os.*;
import android.preference.*;
import android.view.*;

public class PreferenceHelper extends PreferenceActivity
 {
    @Override
	public void onCreate(Bundle savedInstanceState) {    
		super.onCreate(savedInstanceState);       
		addPreferencesFromResource(R.xml.preferences );       
	}
	
	/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 0, 0, "Show current settings");
        return super.onCreateOptionsMenu(menu);
    }
	*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                //startActivity(new Intent(this, ShowSettingsActivity.class));
                return true;
        }
        return false;
    }
}
