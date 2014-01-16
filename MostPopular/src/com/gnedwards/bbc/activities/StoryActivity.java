package com.gnedwards.bbc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.gnedwards.bbc.Orientation;
import com.gnedwards.bbc.fragments.StoryViewPagerFrag;
import com.gnedwards.mostpopular.R;

//  public class StoryActivity extends ActionBarSetupActivity implements Communicator{
public class StoryActivity extends ActionBarSetupActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//set screen to show only the stories ViewPager fragment
		findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
		StoryViewPagerFrag storyViewPagerFrag = new StoryViewPagerFrag();
		FragmentTransaction storyViewPagerFragTrans = getStorySupportFragmentManager().beginTransaction();
		storyViewPagerFragTrans.replace(R.id.StoryHeaders, storyViewPagerFrag);
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

}
