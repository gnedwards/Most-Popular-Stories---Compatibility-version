package com.gnedwards.bbc.startup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.View;

import com.gnedwards.bbc.GlobalData;

/**Finds most popular stories on BBC News homepage and downloads them, and puts each into a Spanned TextView. **/
public class DownloadAsync extends AsyncTask<Object, Void, Void>{
	private String TAG = this.getClass().getName();
	private ArrayList<String> mostSharedUrls;
	private ArrayList<String> mostReadUrls;
	
	@Override
	protected Void doInBackground(Object... items) {
		
		Log.i(TAG, "asynctask starting!");
		GlobalData.progress = 0;
		GlobalData.progressBar.setProgress(GlobalData.progress);
		GlobalData.progressBarVisibility = View.VISIBLE;
		GlobalData.progressBar.setVisibility(GlobalData.progressBarVisibility);
		ArrayList<String> mostPopularLinks = new ArrayList<String>();
		
        try {
            URL bbcUrl = new URL("http://www.bbc.co.uk/news");
            HttpURLConnection conn = (HttpURLConnection) bbcUrl.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla");
            conn.connect();
            InputStreamReader in = new InputStreamReader((InputStream) conn.getInputStream());
            BufferedReader buff = new BufferedReader(in);
            
            mostPopularLinks = getMostPopularLinks(buff); 
            downloadMostPopularLinks(mostPopularLinks);	
            
            in.close();
            buff.close();      
        } 
        catch (MalformedURLException e) {
        	
			Log.e(TAG,"URL invalid");
			e.printStackTrace();
			
		} 
        
        catch (IOException e) {
			
			Log.e(TAG,"Problem with reading file");
			e.printStackTrace();
			
		}	
        return null;
	} 
	

	/**Scrape the appropriate part of the web page to acquire the most popular news pages 
	 * @throws IOException **/
	private ArrayList<String> getMostPopularLinks(BufferedReader buff) throws IOException {
		
        ArrayList<String> mostPopularLinks = new ArrayList<String>();  
        String lineOfHTML = "";
        boolean inMostPopularSection = false;
        ArrayList<Integer> indices;
        int i =0;          
        mostSharedUrls = new ArrayList<String>();
		mostReadUrls = new ArrayList<String>();
        HTMLloop:
        while ((lineOfHTML = buff.readLine())!= null) {
      
        		//flag when a most popular pages section is about to be read
        		if (lineOfHTML.contains("<ol>")) {    
        			inMostPopularSection = true;         			
        		}
        		
        		//flag when reading of a most popular pages section is completed
        		else if (lineOfHTML.contains("</ol>")) {		
        			i++;	
        			
        			if (i == 2) {
        				//stop reading HTML page when required links are found 
        				break HTMLloop;
        			}
        			
            		inMostPopularSection = false;
            		
            	}            
        		
            	if (inMostPopularSection && lineOfHTML.contains("href=")) {            		
            		 indices = new ArrayList<Integer>();
            		//extract start and end indices of URL in line
            		
            		for (int index = lineOfHTML.indexOf("\""); 
            				index >= 0; 
            				index = lineOfHTML.indexOf("\"", index + 1)){
            			
            			indices.add(index);
            			
            		}
            		String url = lineOfHTML.substring(indices.get(0)+1, indices.get(1)) +"\n";
            		mostPopularLinks.add(url);
            		//add link extracted from the line
            		if (i == 0) {
            			mostSharedUrls.add(url); 	
            		} else if (i==1) {
            			mostReadUrls.add(url); 	
            		}
        	}
        	
        }   
        Log.i(TAG,"The links of the most popular have been read");
        return mostPopularLinks;	
	}
	/** downloads and extracts HTML source code from the most popular pages **/
	private void downloadMostPopularLinks(ArrayList<String> mostPopularLinks) 
		throws MalformedURLException, IOException{
		
		HashMap<String, Spanned> mostSharedUrlToTitles = new HashMap<String,Spanned>();
		HashMap<String, Spanned> mostReadUrlToTitles = new HashMap<String,Spanned>();
		HashMap<String,Spanned> mostSharedUrlToContents = new HashMap<String,Spanned>();
		HashMap<String, Spanned> mostReadUrlToContents = new HashMap<String,Spanned>();
		HttpURLConnection conn;
		URL url;
		InputStreamReader in;
		BufferedReader buff;
		String lineOfHtml = "";
		boolean paragraph = false;
		boolean storyStarted = false;
		boolean image = false;
		boolean breakAwayComponent = false;
		int rank = 0;
		GlobalData.totalToDownload = 0;
		for (String popularLink: mostPopularLinks) {	
			if (!mostSharedUrlToContents.containsKey(popularLink)) {
				GlobalData.totalToDownload++;
			}
		}
		GlobalData.progressBar.setMax(GlobalData.totalToDownload);
		
		int storyNumber = 0;
		for (String popularLink: mostPopularLinks) {	
			storyNumber++;
			rank++;
			SpannableStringBuilder htmlText = new SpannableStringBuilder();
			//only download page if not downloaded previously
			if (!mostSharedUrlToContents.containsKey(popularLink)) {	
				storyStarted = false;
				url = new URL(popularLink);
				conn = (HttpURLConnection) url.openConnection();
	            conn.setRequestProperty("User-Agent", "Mozilla");
	            conn.connect();
	            in = new InputStreamReader((InputStream) conn.getInputStream());
	            buff = new BufferedReader(in);
	            boolean newsbeatStory = popularLink.contains("newsbeat");
	            loop:
	           
	            while ((lineOfHtml = buff.readLine()) != null) {
	            	 //terminate if end of story
	            	if ((lineOfHtml.contains("<!-- / story-body -->") && !newsbeatStory) ||
	            			(lineOfHtml.contains("<h2>Bookmark with</h2>") && newsbeatStory)){
	            		break loop;
	            	}
	            	//flag if line contains story title
	            	if ((lineOfHtml.contains("<h1 class=\"story-header\">") && !newsbeatStory)|| 
	            			(lineOfHtml.contains("<h1 >") && newsbeatStory)) {

	            		Spanned processedHtml;	
	            		//note title if in most shared category
						if (storyNumber <=5) {
							processedHtml = getProcessedHTML("" + rank +": " + lineOfHtml);
			            	mostSharedUrlToTitles.put(popularLink,processedHtml);
			            //or if in most read category
						} else {
							processedHtml = getProcessedHTML("" + (rank-5) +": " + lineOfHtml);
							mostReadUrlToTitles.put(popularLink,processedHtml);
			            }
	            		htmlText.append(processedHtml);
	            		if (!newsbeatStory) {
	            			storyStarted = true;
	            		}
	            	}  
	            	if (newsbeatStory) {
	            	// if a newsbeat story look for different indicator of story beginning
	            		if (lineOfHtml.contains("<div id=\"story-body\">")) {
	            			storyStarted = true;
	            		}
	            		if (lineOfHtml.contains("<span class=\"author-name\">") || 
	            				lineOfHtml.contains("<span class=\"author-position\">")) {
	            			htmlText.append(getProcessedHTML(lineOfHtml));
	            		}
	            	}
	            	if (storyStarted) {
	            		//flat if line contains an extra side-feature
	            		if (lineOfHtml.contains("#story_continues")) {
	            			breakAwayComponent = true;
	            		}
	            		//flat when extra side-feature stops
	            		if (lineOfHtml.contains("id=\"story_continues")) {
	            			breakAwayComponent = false;
	            		}
	            		//flag if line is paragraph 
		            	if ((lineOfHtml.contains("<p>") || lineOfHtml.contains("<p >")||
		            			(lineOfHtml.contains("class=") && lineOfHtml.contains("\"introduction"))
		            			|| (lineOfHtml.contains("<p") && lineOfHtml.contains("id=\"story_continues")))
		            			 && !breakAwayComponent) {
		            		paragraph = true;
		            		
		            	}
		            	
		            	if (paragraph) {
		            		htmlText.append(getProcessedHTML(lineOfHtml));
		            	}
		            	if (lineOfHtml.contains("</p>")) {
		            		paragraph = false;
		            	}
		            	//flag if line is subtitle
		            	if (lineOfHtml.contains("<span class=\"cross-head\">")){
		            		htmlText.append(getProcessedHTML("<b>" + lineOfHtml +"</b><br/><br/>"));
		            	}
		            	//flag if line is an image
		            	if (lineOfHtml.contains("<img src=") && !breakAwayComponent){
		            		htmlText.append(getProcessedHTML(lineOfHtml +"<br/>"));
		            		 image = true;
		            	}
		            	//flag if line is image caption
		            	if (image && lineOfHtml.contains("<span style=") && !breakAwayComponent) {
		            		htmlText.append(getProcessedHTML(lineOfHtml + "<br/><br/>"));
		            		image = false;
		            	}
	            	}
	            	
	            }
	            
	            in.close();
	            buff.close();
	            GlobalData.progress++;
	           	
	            publishProgress();
	            if (storyNumber <=5)
	            	mostSharedUrlToContents.put(popularLink,htmlText);
	            else {
	            	
					mostReadUrlToContents.put(popularLink,htmlText);
	            }
			}	
			
		}
		GlobalData.mostReadUrls = mostReadUrls;
		GlobalData.mostSharedUrls = mostSharedUrls;
		GlobalData.mostReadUrlToTitles = mostReadUrlToTitles;
		GlobalData.mostReadUrlToContents = mostReadUrlToContents;
		GlobalData.mostSharedUrlToTitles = mostSharedUrlToTitles;
		GlobalData.mostSharedUrlToContents = mostSharedUrlToContents;
		Log.i(TAG,"All BBC News pages downloaded and stored as Spanned TextViews");
	}
	
	private Spanned getProcessedHTML(String line){
		return Html.fromHtml(line, getImageHTML(), null);
	}
	
	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);
		GlobalData.progressBar.setProgress(GlobalData.progress);
	}

	@Override
	protected void onPostExecute(Void v) {
		
		super.onPostExecute(v);
		Log.i(TAG, "asynctask finishing!");
		GlobalData.progressBarVisibility = View.INVISIBLE;
		GlobalData.progressBar.setVisibility(GlobalData.progressBarVisibility);
		Callback callback = (Callback) GlobalData.mainActivity;
		callback.asynctaskFinished();
	} 	

	public ImageGetter getImageHTML() {
		ImageGetter imageGetter = new ImageGetter() {
			public Drawable getDrawable(String source) {
				try {
					InputStream in = new URL(source).openStream();
					Drawable drawable = Drawable.createFromStream(in, "src name");
					//scale image keeping ratio
					drawable.setBounds(0, 0, 375, (int) Math.round(375/(0.0 + drawable.getIntrinsicWidth())*drawable.getIntrinsicHeight()));
					in = null;
					System.gc();
					return drawable;
				} catch(IOException e) {
					Log.e(TAG,"I/O problem");
					return null;
				}
				
			}
			
		};
		return imageGetter;
		
	}
	
	  public interface Callback {

	        public void asynctaskFinished();
	    }

}
