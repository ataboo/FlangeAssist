package com.atasoft.adapters;

import android.app.*;
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
		text.setText(children);
		// change pic
		// if(linkOrNot(children))
		convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//add logic for phone/link
					Toast.makeText(activity, children,
								   Toast.LENGTH_SHORT).show();
				}
			});
		return convertView;
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
		// change pic here
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
} 