package com.atasoft.flangeassist;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.*;
import android.os.*;
import android.view.*;

public class AboutPage extends Activity {
    @SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        // Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
		
	//	TextView contact = (TextView) findViewById(R.id.emaillink);
	//    contact.setMovementMethod(LinkMovementMethod.getInstance());
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
