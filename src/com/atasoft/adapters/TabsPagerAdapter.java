package com.atasoft.adapters;

import com.atasoft.flangeassist.AboutFragment;
import com.atasoft.flangeassist.TopRatedFragment;
import com.atasoft.flangeassist.TorqueFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.atasoft.flangeassist.*;

public class TabsPagerAdapter extends FragmentPagerAdapter {

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
				// Hall Links
				return new TopRatedFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 5;
    }

}
