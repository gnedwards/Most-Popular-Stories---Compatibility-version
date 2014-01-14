package com.gnedwards.bbc;

import java.util.ArrayList;
import java.util.HashMap;

import android.text.Spanned;
import android.widget.ProgressBar;

import com.gnedwards.bbc.activities.MainActivity;

public class GlobalData {
	public static HashMap<String, Spanned> mostSharedUrlToTitles;
	public static ArrayList<String> mostSharedUrls;
	public static HashMap<String, Spanned> mostSharedUrlToContents;
	public static ArrayList<String> mostReadUrls;
	public static HashMap<String, Spanned> mostReadUrlToTitles;
	public static HashMap<String, Spanned> mostReadUrlToContents;
	public static MainActivity mainActivity;
	public static ProgressBar progressBar;
	public static int progress;
	public static int totalToDownload;
	public static int progressBarVisibility;
}
