package com.wtfff.boredApp.utility;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class ImageLoader extends AsyncTask<Drawable, Void, Drawable> {
	
	private static final String TAG = "WTFFF_ImageLoader";
	Drawable drawable = null;
	String con_url;
	ImageView imageView;
	ProgressBar progressBar;
	private int photoheight=300;
	
	public ImageLoader(ImageView imageView,ProgressBar progressBar, String con_url, int photoheight) 
	{  
        super();  
        this.imageView=imageView;
		this.con_url=con_url;
		this.progressBar=progressBar;
		this.photoheight=photoheight;
    }  

    @Override  
    protected Drawable doInBackground(Drawable... params) {
    	HttpURLConnection connection = null;
    	int filenameLocation = con_url.indexOf(".jpg");
    	int httpLocation = con_url.lastIndexOf("http://");
    	if(httpLocation==-1)
    		httpLocation = con_url.lastIndexOf("https://");
    	if(filenameLocation!=-1 && httpLocation!=-1)
    		con_url=con_url.substring(httpLocation,filenameLocation+4);
    	else if(filenameLocation!=-1)
    		con_url=con_url.substring(0,filenameLocation+4);
    	else if (httpLocation!=-1)
    		con_url=con_url.substring(httpLocation,con_url.length());
		InputStream input = null;
		try {
			   URL url = new URL(con_url);
			   connection =(HttpURLConnection) url.openConnection();
		       connection.setDoInput(true);
		       connection.connect();
			   input = connection.getInputStream();
			   try{
				   BitmapFactory.Options opts = new BitmapFactory.Options();
				   opts.inPreferredConfig = Bitmap.Config.RGB_565;
				   opts.inPurgeable = true;
				   opts.inInputShareable = true;
				   opts.inSampleSize = 1;
				   Bitmap bitmap = BitmapFactory.decodeStream(input, null, opts);
				   drawable = new BitmapDrawable(bitmap);
			   }catch (OutOfMemoryError outOfMemoryError){
				   try{
					   BitmapFactory.Options opts = new BitmapFactory.Options();
					   opts.inPreferredConfig = Bitmap.Config.RGB_565;
					   opts.inPurgeable = true;
					   opts.inInputShareable = true;
					   opts.inSampleSize = 4;
					   Bitmap bitmap = BitmapFactory.decodeStream(input, null, opts);
					   drawable = new BitmapDrawable(bitmap);
				   }catch (OutOfMemoryError outOfMemoryError2){
					   Log.d(TAG,"OutOfMemory again!!!");
				   }
				}catch(Exception e)
			   {
				   e.printStackTrace();
				   Log.d(TAG,"e="+e);
			   }
			   input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			if(connection != null)
			{
				connection.disconnect();
				connection = null;
			}
		}
		return drawable;  
    }  

    @Override  
    protected void onPostExecute(Drawable drawable) {
//    	float parameter = drawable.getIntrinsicWidth()/drawable.getIntrinsicHeight();
//    	int photowidth = Math.round(parameter*photoheight);
//    	imageView.setLayoutParams(new RelativeLayout.LayoutParams(photowidth,photoheight));
    	if(drawable != null)
    	{
	    	LayoutParams para = imageView.getLayoutParams();
	    	int width = Math.round(((float)photoheight/drawable.getIntrinsicHeight())*drawable.getIntrinsicWidth());
	    	para.height=photoheight;
	    	para.width=width;
	    	imageView.setLayoutParams(para);
    		imageView.setBackgroundDrawable(drawable);
    		progressBar.setVisibility(View.INVISIBLE);
    	}
    }  
}
