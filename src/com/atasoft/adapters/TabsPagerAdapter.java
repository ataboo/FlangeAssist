package com.atasoft.adapters;

import com.atasoft.flangeassist.fragments.*;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	public static final String[] TABS = {"Flange\nTables", "Torque\nPattern", "CPI Raise\nEstimator", "Unit\nConverter"};
	public static int TAB_COUNT = TABS.length;
	
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
			case 0:
				// Flange Tables
				return new FlangeFragment();
			case 1:
				// Torque Pattern
				return new TorqueFragment();
			case 2:
				// CPI Raise Estimator
				return new WageCPIEstimate();
			case 3:
				// Unit Converter
				return new UnitConFragment();
		}

        return null;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }
}
