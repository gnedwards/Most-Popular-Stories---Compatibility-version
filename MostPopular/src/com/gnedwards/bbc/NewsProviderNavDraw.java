package com.gnedwards.bbc;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gnedwards.mostpopular.R;

public class NewsProviderNavDraw {

	public static void setup(final Activity activity) {
		DrawerLayout drawerLayout =  (DrawerLayout) activity.findViewById(R.id.drawer_layout);
		ListView lvNewsProviders = (ListView) activity.findViewById(R.id.lvNewsProviders);
		final HashMap<String,Integer> newsProvidersToDrawable = new HashMap<String,Integer>();
		newsProvidersToDrawable.put("BBC News", R.drawable.bbcnews);
		newsProvidersToDrawable.put("Sky News", R.drawable.skynews);
		newsProvidersToDrawable.put("Guardian", R.drawable.guardian);
		newsProvidersToDrawable.put("Telegraph", R.drawable.telegraph);
		final ArrayList<String> newsProviders = new ArrayList<String>(newsProvidersToDrawable.keySet());
		final LayoutInflater inflater = activity.getLayoutInflater();
		
		lvNewsProviders.setAdapter(new ArrayAdapter<String>(activity,R.layout.tv_news_provider_item,newsProviders) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				TextView tvNewsProviderItem;
				if (convertView != null) {
					tvNewsProviderItem = (TextView) convertView;
				} else {
					tvNewsProviderItem = (TextView) inflater.inflate(R.layout.tv_news_provider_item, parent,false);
				}
				int id = newsProvidersToDrawable.get(newsProviders.get(position));
				Drawable dr = activity.getResources().getDrawable(id);
				Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();

				Drawable d = new BitmapDrawable(activity.getResources(), Bitmap.createScaledBitmap(bitmap, 40, 40, true));
				d.setBounds(0,40,40,0);
				tvNewsProviderItem.setCompoundDrawablesWithIntrinsicBounds(d,null,null,null);
				tvNewsProviderItem.setText(newsProviders.get(position));
				return tvNewsProviderItem;
			}
			
		});
		 ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(activity, drawerLayout, R.drawable.ic_drawer, 
                R.string.drawer_open, R.string.drawer_close);
		drawerLayout.setDrawerListener(drawerToggle);
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		lvNewsProviders.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				Toast.makeText(activity, ((TextView)view).getText(), Toast.LENGTH_LONG).show();
				
			}

			
		});
	}
}
