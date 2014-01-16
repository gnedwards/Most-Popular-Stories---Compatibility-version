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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gnedwards.bbc.fragments.StoryHeadersFrag;
import com.gnedwards.bbc.interfaces.Communicator;
import com.gnedwards.mostpopular.R;

public class ActionBarSetupActivity extends ActionBarActivity implements TabListener, Communicator {
	private int noOfTabs = 2;
	private ActionBarDrawerToggle drawerToggle;
	private String tabName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//set up tabs
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
			
		//set up navigator
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
				//setting each icon to the same small size
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
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// Do nothing
		
	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		//add list of stories to screen
		setTabName((String) arg0.getText());
		FragmentManager fragMan = getSupportFragmentManager();
		StoryHeadersFrag storyHeadersFrag = new StoryHeadersFrag();
		Bundle args = new Bundle();
		args.putString("tabName", getTabName());
		int position = getIntent().getIntExtra("position",0);
		args.putInt("position", position);
		storyHeadersFrag.setArguments(args);
		FragmentTransaction storyHeadersFragTrans = fragMan.beginTransaction();
		storyHeadersFragTrans.replace(R.id.StoryHeaders,storyHeadersFrag);
		
		storyHeadersFragTrans.commit();	
		
		
	}

	@Override
	public FragmentManager getStorySupportFragmentManager() {
		return getSupportFragmentManager();
	}
	
	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// Do nothing
		
	}
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);     
        drawerToggle.syncState();
    }

	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
		return super.onOptionsItemSelected(item);
	}

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}
}
