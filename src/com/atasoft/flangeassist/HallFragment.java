package com.atasoft.flangeassist;

import android.os.*;
import android.support.v4.app.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.atasoft.adapters.*;
import java.security.acl.*;

public class HallFragment extends Fragment {
    View thisFrag;
	SparseArray<ExpandableGroup> groups = new SparseArray<ExpandableGroup>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
	View v = inflater.inflate(R.layout.halllinks, container, false);
    thisFrag = v;

	createData();
	ExpandableListView listView = (ExpandableListView) thisFrag.findViewById(R.id.listView);
	ExListAd adapter = new ExListAd(getActivity(),
										groups);
	listView.setAdapter(adapter);
	
    return v;					 
	}
	
    private void createData() {
		for (int j = 0; j < 5; j++) {
			ExpandableGroup group = new ExpandableGroup("Test " + j);
			for (int i = 0; i < 5; i++) {
				group.children.add("Sub Item" + i);
			}
			groups.append(j, group);
		}
		
	}	
	
} 


