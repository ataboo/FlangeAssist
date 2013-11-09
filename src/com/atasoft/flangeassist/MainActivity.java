package com.atasoft.flangeassist;

import android.annotation.SuppressLint;
import android.app.*;
import android.app.ActionBar.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v4.view.*;
import com.atasoft.adapters.*;
import android.app.FragmentTransaction;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = {"Paycheque\nCalculator", "Flange\nTables", "Torque\nPattern", "Hall\nLinks", "About"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        
		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
        }
		
		viewPager.setOnPageChangeListener (new ViewPager.OnPageChangeListener() {

				@Override
				public void onPageSelected(int position) {
					// on changing the page
					// make respected tab selected
					actionBar.setSelectedNavigationItem(position);
				}
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}

				@Override
				public void onPageScrollStateChanged(int arg0) {
				}
			});
	}
	@Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }
    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }
}

