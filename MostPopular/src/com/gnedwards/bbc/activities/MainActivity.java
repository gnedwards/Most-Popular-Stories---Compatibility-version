package com.gnedwards.bbc.activities;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gnedwards.bbc.GlobalData;
import com.gnedwards.bbc.fragments.LoadingFrag;
import com.gnedwards.bbc.fragments.StoryHeadersFrag;
import com.gnedwards.bbc.interfaces.Communicator;
import com.gnedwards.bbc.startup.DownloadAsync;
import com.gnedwards.mostpopular.R;

public class MainActivity extends ActionBarActivity implements Communicator, TabListener,
DownloadAsync.Callback{
	
	private FragmentManager fragMan;
	private ActionBarDrawerToggle drawerToggle;
	private int noOfTabs = 2;
	private String tabName;
	private LoadingFrag loadingFrag;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		GlobalData.progressBar = (ProgressBar) findViewById(R.id.progressBar);
		GlobalData.progressBar.setMax(GlobalData.totalToDownload);
		GlobalData.progressBar.setProgress(GlobalData.progress);
		GlobalData.progressBar.setVisibility(GlobalData.progressBarVisibility);
		GlobalData.mainActivity = this;
		
		ActionBar titleBar = getSupportActionBar();
		titleBar.setDisplayHomeAsUpEnabled(true); 
		ActionBar.Tab[] tabs = new ActionBar.Tab[noOfTabs];
		String[] titles = {"Most Read","Most Shared"};
		for (int i = 0; i < tabs.length; i++) {
			tabs[i] = titleBar.newTab();
			tabs[i].setText(titles[i]);
			tabs[i].setTabListener(this);
			titleBar.addTab(tabs[i]);
		}
		titleBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		titleBar.setTitle(getApplicationInfo().labelRes);
		titleBar.setSubtitle("BBC News");
		
		fragMan = getSupportFragmentManager();
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

		DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		ListView lvNewsProviders = (ListView) drawerLayout.findViewById(R.id.lvNewsProviders);
		final HashMap<String,Integer> newsProvidersToDrawable = new HashMap<String,Integer>();
		newsProvidersToDrawable.put("BBC News", R.drawable.bbcnews);
		newsProvidersToDrawable.put("Sky News", R.drawable.skynews);
		newsProvidersToDrawable.put("Guardian", R.drawable.guardian);
		newsProvidersToDrawable.put("Telegraph", R.drawable.telegraph);
		final ArrayList<String> newsProviders = new ArrayList<String>(newsProvidersToDrawable.keySet());
		
		
		lvNewsProviders.setAdapter(new ArrayAdapter<String>(this,R.layout.tv_news_provider_item,newsProviders) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView tvNewsProviderItem;
				if (convertView != null) {
					tvNewsProviderItem = (TextView) convertView;
				} else {
					tvNewsProviderItem = (TextView) inflater.inflate(R.layout.tv_news_provider_item, parent,false);
				}
				int id = newsProvidersToDrawable.get(newsProviders.get(position));
				Drawable dr = getResources().getDrawable(id);
				Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();

				Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 40, 40, true));
				d.setBounds(0,40,40,0);
				tvNewsProviderItem.setCompoundDrawablesWithIntrinsicBounds(d,null,null,null);
				tvNewsProviderItem.setText(newsProviders.get(position));
				return tvNewsProviderItem;
			}
			
		});
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, 
                R.string.drawer_open, R.string.drawer_close);
		drawerLayout.setDrawerListener(drawerToggle);
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		lvNewsProviders.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				CharSequence lvItem = ((TextView)view).getText();
				if (lvItem.equals("BBC News")) {
					Toast.makeText(getBaseContext(), "Already showing!", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getBaseContext(), "Not implemented yet!", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public FragmentManager getStorySupportFragmentManager() {
		return getSupportFragmentManager();
	}

	@Override
	public void asynctaskFinished() {
		fragMan = getSupportFragmentManager();
		StoryHeadersFrag storyHeadersFrag = new StoryHeadersFrag();
		FragmentTransaction storyHeadersFragTrans = fragMan.beginTransaction();
		Bundle args = new Bundle();
		args.putString("tabName", tabName);
		storyHeadersFrag.setArguments(args);
		storyHeadersFragTrans.replace(R.id.StoryHeaders,storyHeadersFrag);
		
		storyHeadersFragTrans.commit();	
	}
	@Override
  protected void onPostCreate(Bundle savedInstanceState) {
      super.onPostCreate(savedInstanceState);
      // Sync the toggle state after onRestoreInstanceState has occurred.
       drawerToggle.syncState();
  }

	@Override
  public void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);
      drawerToggle.onConfigurationChanged(newConfig);
  }

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		 // call ActionBarDrawerToggle.onOptionsItemSelected(), if it returns true
      // then it has handled the app icon touch event

		if (drawerToggle.onOptionsItemSelected(item)) {
          return true;
      }
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		tabName = (String) arg0.getText();
		fragMan = getSupportFragmentManager();
		StoryHeadersFrag storyHeadersFrag = new StoryHeadersFrag();
		Bundle args = new Bundle();
		args.putString("tabName", tabName);
		int position = getIntent().getIntExtra("position",0);
		args.putInt("position", position);
		storyHeadersFrag.setArguments(args);
		FragmentTransaction storyHeadersFragTrans = fragMan.beginTransaction();
		storyHeadersFragTrans.replace(R.id.StoryHeaders,storyHeadersFrag);
		
		storyHeadersFragTrans.commit();	
		
		
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}
}
