package com.atasoft.flangeassist;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.Intent;
import android.net.Uri;

public class MainActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}
	
	public void flangeLaunch(View view){
		Intent intent = new Intent(this, AsmeFlange.class);
		startActivity(intent);
	}
	
	public void calloutEdm(View view) {
		startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://boilermakers.ca/content/callout.html")));
    }
	
	public void aboutPage(View view) {
		Intent intent = new Intent(this, AboutPage.class);
		startActivity(intent);
	}
	public void payCalc(View view) {
		Intent intent = new Intent(this, PayCalculator.class);
		startActivity(intent);
	}
}
