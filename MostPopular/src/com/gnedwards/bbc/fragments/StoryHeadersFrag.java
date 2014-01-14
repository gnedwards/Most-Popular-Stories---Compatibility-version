package com.gnedwards.bbc.fragments;



import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gnedwards.bbc.GlobalData;
import com.gnedwards.bbc.activities.StoryActivity;
import com.gnedwards.bbc.interfaces.Communicator;
import com.gnedwards.mostpopular.R;

public class StoryHeadersFrag extends Fragment implements OnItemClickListener{
	private View listViewLayout;
	private LayoutInflater inflater;
	private ArrayList<Spanned> storyHeaders;
	private String tabName;
	private String TAG;
	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		TAG = this.getClass().getName();
		listViewLayout = inflater.inflate(R.layout.list_frag, container, false);
		return listViewLayout;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		ListView lvStoryHeaders = (ListView) listViewLayout.findViewById(R.id.lvStoryHeaders);
		//get a Spanned list of page titles and their string equivalents
		storyHeaders = new ArrayList<Spanned>();
		ArrayList<String> storyHeadersString = new ArrayList<String>();
		if (getArguments() != null) {
			tabName = "" + getArguments().getString("tabName");
		
			if (tabName.equals("Most Shared")){
				for (String url: GlobalData.mostSharedUrls){
					storyHeadersString.add(url);
					storyHeaders.add(GlobalData.mostSharedUrlToTitles.get(url));	
				}
			} else if (tabName.equals("Most Read")) {
				if (GlobalData.mostReadUrls != null) {
					for (String url: GlobalData.mostReadUrls){
						storyHeadersString.add(url);
						storyHeaders.add(GlobalData.mostReadUrlToTitles.get(url));	
					}		
				}
			}
		}
		lvStoryHeaders.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, storyHeadersString) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView returnView = (TextView) convertView;
				if (returnView == null) {
					returnView = (TextView) inflater.inflate(R.layout.tv_story_header, null);
				}
				
				returnView.setText(storyHeaders.get(position));
				
				return returnView;
			}
			
		});

		if (GlobalData.mainActivity.findViewById(R.id.Story) != null) {
		//add storyViewPagerFrag if room on the screen
			StoryViewPagerFrag storyViewPagerFrag = new StoryViewPagerFrag();
			FragmentTransaction storyViewPagerFragTrans = GlobalData.mainActivity.getStorySupportFragmentManager().beginTransaction();
			Bundle args = new Bundle();
			args.putString("tabName",tabName);
			int position = getArguments().getInt("position",0);
			args.putInt("position",position);
			storyViewPagerFrag.setArguments(args);
			storyViewPagerFragTrans.replace(R.id.Story, storyViewPagerFrag);
			storyViewPagerFragTrans.commit();
			Log.i(TAG,"storyViewPagerFrag commited");
		} 
		
		lvStoryHeaders.setOnItemClickListener(this);
	
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		if (GlobalData.mainActivity.findViewById(R.id.Story) == null) {
		//if no room on the screen, launch new activity to add fragment
			Intent storyActivity = new Intent(GlobalData.mainActivity.getBaseContext(),StoryActivity.class);
			storyActivity.putExtra("tabName", tabName);
			storyActivity.putExtra("position", position);
			startActivity(storyActivity);		
			Log.i(TAG,"launch new activity (?)");
		} else {
		//otherwise add it to the screen
			StoryViewPagerFrag storyViewPagerFrag = new StoryViewPagerFrag();
			Communicator comm = (Communicator) GlobalData.mainActivity;
			FragmentTransaction storyViewPagerFragTrans = comm.getStorySupportFragmentManager().beginTransaction();
			Bundle args = new Bundle();
			args.putInt("position", position);
			args.putString("tabName",tabName);
			storyViewPagerFrag.setArguments(args);
			storyViewPagerFragTrans.replace(R.id.Story, storyViewPagerFrag);
			storyViewPagerFrag.setArguments(args);
			storyViewPagerFragTrans.commit();
			Log.i(TAG,"added storyViewPagerFrag in landscape (?)");
		}
		
	}

	
}
