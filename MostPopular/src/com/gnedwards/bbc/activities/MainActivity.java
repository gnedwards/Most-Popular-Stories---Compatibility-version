package com.gnedwards.bbc.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar.TabListener;
import android.widget.ProgressBar;

import com.gnedwards.bbc.GlobalData;
import com.gnedwards.bbc.fragments.LoadingFrag;
import com.gnedwards.bbc.fragments.StoryHeadersFrag;
import com.gnedwards.bbc.startup.DownloadAsync;
import com.gnedwards.mostpopular.R;

public class MainActivity extends ActionBarSetupActivity implements  TabListener, /*Communicator, */
DownloadAsync.Callback{
	
	private FragmentManager fragMan;
	private LoadingFrag loadingFrag;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//set progress bar to current global progress following possible configuration change
		GlobalData.progressBar = (ProgressBar) findViewById(R.id.progressBar);
		GlobalData.progressBar.setMax(GlobalData.totalToDownload);
		GlobalData.progressBar.setProgress(GlobalData.progress);
		GlobalData.progressBar.setVisibility(GlobalData.progressBarVisibility);
		GlobalData.mainActivity = this;
		
		fragMan = getSupportFragmentManager();
		//start task to download data and retain it through configuration changes
		loadingFrag = (LoadingFrag) fragMan.findFragmentByTag("loadingFrag");
		if (loadingFrag == null) {
			loadingFrag = new LoadingFrag() {
				@Override
				public void onCreate(Bundle savedInstanceState) {
					super.onCreate(savedInstanceState);
					setRetainInstance(true);
					new DownloadAsync().execute(GlobalData.progressBar);	

				}
	
			};
			FragmentTransaction fragTrans = fragMan.beginTransaction();
			fragTrans.add(loadingFrag,"loadingFrag");
			fragTrans.commitAllowingStateLoss();

		}
	}

	@Override
	public void asynctaskFinished() {
		fragMan = getSupportFragmentManager();
		StoryHeadersFrag storyHeadersFrag = new StoryHeadersFrag();
		FragmentTransaction storyHeadersFragTrans = fragMan.beginTransaction();
		Bundle args = new Bundle();
		args.putString("tabName", getTabName());
		storyHeadersFrag.setArguments(args);
		storyHeadersFragTrans.replace(R.id.StoryHeaders,storyHeadersFrag);
		
		storyHeadersFragTrans.commit();	
	}
}
