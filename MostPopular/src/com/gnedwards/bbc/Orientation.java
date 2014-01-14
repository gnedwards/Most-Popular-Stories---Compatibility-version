package com.gnedwards.bbc;

import android.app.Activity;

public class Orientation {
	/** Orientation of activity currentActivity **/
	public static String getOrientation(Activity currentActivity) {
		int width = currentActivity.getResources().getDisplayMetrics().widthPixels;
		int height = currentActivity.getResources().getDisplayMetrics().heightPixels;
	    if(width>height){ 
	        return "landscape";
	    }
	    else {
	        return "portrait";
	    }    
	}
}
