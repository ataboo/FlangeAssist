package com.atasoft.adapters;

import com.atasoft.flangeassist.AboutFragment;
import com.atasoft.flangeassist.MoviesFragment;
import com.atasoft.flangeassist.TopRatedFragment;
import com.atasoft.flangeassist.TorqueFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
			case 0:
				// Pay
				return new TopRatedFragment();
			case 1:
				// Flange Tables
				return new AboutFragment();
			case 2:
				// Torque Pattern
				return new TorqueFragment();
			case 3:
				// Hall Links
				return new TopRatedFragment();
			case 4:
				// About
				return new AboutFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 5;
    }

}
