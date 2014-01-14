package com.gnedwards.bbc.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnedwards.bbc.GlobalData;
import com.gnedwards.bbc.interfaces.Communicator;
import com.gnedwards.mostpopular.R;

public class StoryViewPagerFrag extends Fragment{
	private View viewPagerLayout;
	private StoryFrag[] stories;
	private int count;
	private String TAG;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		TAG = this.getClass().getName();
		viewPagerLayout = inflater.inflate(R.layout.view_pager_frag, container, false);
		return viewPagerLayout;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		ViewPager viewPagerStories = (ViewPager) viewPagerLayout.findViewById(R.id.viewPagerStories);
		Communicator comm;
		if (getActivity().getTitle().equals("StoryActivity")) {
			 comm = (Communicator) getActivity();
		} else {
			comm = (Communicator) GlobalData.mainActivity;
		}
		FragmentManager storyFragmentManager = comm.getStorySupportFragmentManager();
		if (getArguments() != null) {
			String tabName = "" + getArguments().getString("tabName");
			if (tabName.equals("Most Shared")) {
				count = 5;
			} else if (tabName.equals("Most Read")) {
				count = 10;
			}
			//array of all the either most shared or most read stories, depending on selected tab
			stories = new StoryFrag[count];
			for (int i = 0; i<count;i++) {
				StoryFrag storyFrag = new StoryFrag();
				Bundle args = new Bundle();
				args.putInt("position", i);
				args.putString("tabName",tabName);
				storyFrag.setArguments(args);
				stories[i] = storyFrag;
			}
			
			ViewPagerAdapterStories viewPagerAdapterStories = new ViewPagerAdapterStories(storyFragmentManager);
			viewPagerStories.setAdapter(viewPagerAdapterStories);
			
			Bundle args = getArguments();
			if (args != null) {
				int position = args.getInt("position", 0);
				viewPagerStories.setCurrentItem(position);
			}
			Log.i(TAG,"viewPagerStories created");
		}
	}
	
	@Override
	public void onStart() {
		
		super.onStart();
		
	}

	@Override
	public void onResume() {
		super.onResume();
		
	}

	private class ViewPagerAdapterStories extends FragmentStatePagerAdapter{

		public ViewPagerAdapterStories(FragmentManager fm) {
			super(fm);		
		}

		@Override
		public Fragment getItem(int position) {
			
			return stories[position];
		}

		@Override
		public int getCount() {		
			return count;		
		}

	}

	


}
