package com.atasoft.adapters;

import android.app.*;
import android.content.*;
import android.graphics.drawable.*;
import android.net.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.atasoft.flangeassist.*;

public class ExListAd extends BaseExpandableListAdapter {

	private final SparseArray<ExpandableGroup> groups;
	public LayoutInflater inflater;
	public Activity activity;

	public ExListAd(Activity act, SparseArray<ExpandableGroup> groups) {
		activity = act;
		this.groups = groups;
		inflater = act.getLayoutInflater();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return groups.get(groupPosition).children.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
							 boolean isLastChild, View convertView, ViewGroup parent) {
		final String children = (String) getChild(groupPosition, childPosition);
		TextView text = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listrow_details, null);
		}
		text = (TextView) convertView.findViewById(R.id.textView1);
		
		convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
				    switch (linkType(children, text)) {
						case 0:  // phone
						    
							break;
						case 1:  // link
						    
						    break;
						
						
					}
					
				}
			});
			
		text = parseLink(text, children);
		return convertView;
		
		// change pic
		// if(linkOrNot(children))
		
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return groups.get(groupPosition).children.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
							 View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listrow_group, null);
		}
		ExpandableGroup group = (ExpandableGroup) getGroup(groupPosition);
		((CheckedTextView) convertView).setText(group.string);
		((CheckedTextView) convertView).setChecked(isExpanded);
		convertView = setLocalDraw((CheckedTextView) convertView, group.string);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
	
	private int linkType(String children, TextView text) {
		boolean isCallout = false;
		if(children.startsWith("call_") {
			children = children.split("_")[1];
			isCallout = true;
		}
		if(children.startsWith("(")) {
			if(isCallout) {
                
				return 0;
			} else {
				
				return 0;
			}
		}
		if(children.startsWith("http") && isCallout) {
			
			return 1;   
		}
		
		return 1;
	}
	
	
	private CheckedTextView setLocalDraw(CheckedTextView textViewIn, String groupString) {
		groupString = "logo" + (groupString.substring(0, 3)).trim();
		Context context = textViewIn.getContext();
		int id = context.getResources().getIdentifier(groupString, "drawable", context.getPackageName());
		Drawable logo = (Drawable) context.getResources().getDrawable(R.drawable.logo128);
		textViewIn.setCompoundDrawables(logo, null, null, null);
		return textViewIn;
	}
} 
