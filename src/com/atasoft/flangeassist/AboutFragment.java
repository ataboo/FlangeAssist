package com.atasoft.flangeassist;

import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;


public class AboutFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
						Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.about, container, false);
		return rootView;
	}
}

