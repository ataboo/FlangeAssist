package com.atasoft.adapters;

import com.atasoft.flangeassist.fragments.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.atasoft.flangeassist.*;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	public static int TAB_COUNT = 6;
	
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
			case 0:
				// About
				return new AboutFragment();
			case 1:
				// Flange Tables
				return new FlangeFragment();
			case 2:
				// Torque Pattern
				return new TorqueFragment();
			case 3:
				// Paycheck Calc
				return new PaychequeFragment();
			case 4:
				// CPI Raise Estimator
				return new WageCPIEstimate();
			case 5:
				// Hall Links
				return new HallFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }
}
