package com.atasoft.flangeassist;

import com.atasoft.flangeassist.fragments.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;

public class FragFramer extends FragmentActivity {
	public static final int PAY_CALC = 0;
	public static final int HALL = 1;
	public static final int ABOUT = 2;
	ActionBar actionBar;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragframer);	
		actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		
		launchFrag(getIntent().getIntExtra("launch_frag", 0));
		
		
		
	}
	
	private void launchFrag(int fragInt){
		Fragment frag;
		String name;
		switch (fragInt){
		case PAY_CALC:
			frag = new PaychequeFragment();
			name = PaychequeFragment.NAME;
			break;
		case HALL:
			frag = new HallFragment();
			name = HallFragment.NAME;
			break;
		default:
			frag = new AboutFragment();
			name = AboutFragment.NAME;
			break;
		}
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.fragframe, frag);
		transaction.commit();
		
		setTitle(name);
	}
}
