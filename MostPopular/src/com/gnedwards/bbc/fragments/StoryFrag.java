package com.gnedwards.bbc.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gnedwards.bbc.GlobalData;
import com.gnedwards.mostpopular.R;

public class StoryFrag  extends Fragment{

	private View pageFragLayout;
	private String TAG;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		TAG = this.getClass().getName();
		pageFragLayout = inflater.inflate(R.layout.page_frag, container, false);
		return pageFragLayout;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		TextView tvStory = (TextView) pageFragLayout.findViewById(R.id.tvStory);
		tvStory.setMovementMethod(ScrollingMovementMethod.getInstance());
		tvStory.setMovementMethod(LinkMovementMethod.getInstance());
		Spanned story = null;
		//determine which page to show
		Bundle args = getArguments();
		int position = args.getInt("position");
		String tabName = ""+args.getString("tabName");	
		if (tabName.equals("Most Shared")) {
			if (GlobalData.mostSharedUrls != null) {
				if (GlobalData.mostSharedUrls.get(position) != null) {
					if (GlobalData.mostSharedUrlToContents.containsKey(GlobalData.mostSharedUrls.get(position))) {
						story = GlobalData.mostSharedUrlToContents.get(GlobalData.mostSharedUrls.get(position));
					}
				}
			}
		} 
		else if (tabName.equals("Most Read")){
			if (GlobalData.mostReadUrls != null) {
				if (GlobalData.mostReadUrls.get(position) != null) {
					if (GlobalData.mostReadUrlToContents.containsKey(GlobalData.mostReadUrls.get(position))) {
						story = GlobalData.mostReadUrlToContents.get(GlobalData.mostReadUrls.get(position));
					}
				}
			}
		}
		tvStory.setText(story);
		Log.i(TAG, tabName + " story with position" + position + " displayed");
	}


}
