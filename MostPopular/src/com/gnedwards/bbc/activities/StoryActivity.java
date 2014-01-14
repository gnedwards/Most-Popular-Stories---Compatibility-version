package com.gnedwards.bbc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

import com.gnedwards.bbc.Orientation;
import com.gnedwards.bbc.fragments.StoryViewPagerFrag;
import com.gnedwards.bbc.interfaces.Communicator;
import com.gnedwards.mostpopular.R;

  public class StoryActivity extends ActionBarSetupActivity implements Communicator{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story);
		
		StoryViewPagerFrag storyViewPagerFrag = new StoryViewPagerFrag();
		FragmentTransaction storyViewPagerFragTrans = getStorySupportFragmentManager().beginTransaction();
		storyViewPagerFragTrans.replace(R.id.storyActivity, storyViewPagerFrag);
		int position = getIntent().getIntExtra("position",0);
		String tabName = getIntent().getStringExtra("tabName");
		Bundle args = new Bundle();
		args.putInt("position", position);
		args.putString("tabName", tabName);
		storyViewPagerFrag.setArguments(args);
		storyViewPagerFragTrans.commit();
		if (Orientation.getOrientation(this).equals("landscape")) {
			Intent MainActivity = new Intent(this, MainActivity.class);
			MainActivity.putExtra("position", position);
			startActivity(MainActivity);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.story, menu);
		return true;
	}

	@Override
	public FragmentManager getStorySupportFragmentManager() {
		return getSupportFragmentManager();
	}

}
