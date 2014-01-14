package com.gnedwards.bbc.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.gnedwards.mostpopular.R;

public class ActionBarSetupActivity extends ActionBarActivity implements TabListener{
	private int noOfTabs = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActionBar titleBar = getSupportActionBar();
//		titleBar.setDisplayHomeAsUpEnabled(true); 
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
		
//		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		DrawerLayout drawerLayout =  (DrawerLayout) inflater.inflate(R.id.drawer_layout, null);
//		ListView lvNewsProviders = (ListView) drawerLayout.findViewById(R.id.lvNewsProviders);
//		
//		lvNewsProviders.setAdapter(new ArrayAdapter<String>(this,R.id.tvNewsProviderItem,newsProviders));
//		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, 
//                R.string.drawer_open, R.string.drawer_close);
//		drawerLayout.setDrawerListener(drawerToggle);
//		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
//		lvNewsProviders.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
//					long arg3) {
//				Toast.makeText(getBaseContext(), ((TextView)view).getText(), Toast.LENGTH_LONG).show();
//				
//			}
//		});
	}
	
	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		
		Log.i("MainActivity",arg0.getText() + " selected");
		
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}
	
//	@Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        // Sync the toggle state after onRestoreInstanceState has occurred.
//         drawerToggle.syncState();
//    }
//
//	@Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        drawerToggle.onConfigurationChanged(newConfig);
//    }
//
//	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		
//		 // call ActionBarDrawerToggle.onOptionsItemSelected(), if it returns true
//        // then it has handled the app icon touch event
//
//		if (drawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
//		return super.onOptionsItemSelected(item);
//	}
}
