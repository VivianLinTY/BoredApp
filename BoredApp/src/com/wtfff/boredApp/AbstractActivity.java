package com.wtfff.boredApp;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.wtfff.boredApp.utility.StatusRecord;
import com.wtfff.boredApp.utility.StringPool;
import com.wtfff.boredApp.utility.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

public class AbstractActivity extends Activity{
	
	private static final String TAG="WTFFF_AbstractActivity";
	protected float dpi=1,density=1;
	protected int screenWidth=720;
	protected int screenHeight=1080;
	File watch_list,choose_list;
	protected static final int connect_failed=1;
	protected static final int connect_success=0;
	protected boolean network=true;
	protected Toast toast;

	protected Handler mHandler = new Handler()
	{
        public void handleMessage(Message msg)
        {
        	switch(msg.what)
            {
                case connect_failed:
                	Toast.makeText(AbstractActivity.this, getString(R.string.check_network), Toast.LENGTH_LONG).show();
                	network=false;
//                	network=true;
                	break;
                case connect_success:
                	network=true;
                	break;
            }
        }
	};
	
	@Override 
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        DisplayMetrics dm = new DisplayMetrics(); 
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		dpi=dm.densityDpi;
		density=dpi/160;
//		if (getResources().getConfiguration().orientation ==Configuration.ORIENTATION_LANDSCAPE)        
//		{
//			screenWidth = (int) dm.heightPixels;
//			screenHeight = (int) dm.widthPixels;
//        }else{
//        	screenWidth = (int) dm.widthPixels;
//			screenHeight = (int) dm.heightPixels;
//        }
		toast = Toast.makeText(AbstractActivity.this, getString(R.string.check_network), Toast.LENGTH_SHORT);
    }
	 protected void onResume()
	{
        super.onResume();
        watch_list = new File(StatusRecord.FILENAME_WATCH_LIST);
        choose_list = new File(StatusRecord.FILENAME_CHOOSE_LIST);
        if(watch_list.exists() || choose_list.exists())
        {
//        	long time = new Date().getTime();
//        	if(time-StatusRecord.sharep.getLongPreferences(AbstractActivity.this, StringPool.TAG_POST_TIMESTAMP, 0)>86400000)//一天
//        		new PostLog2Server().execute();
        }
    }
	public class PostLog2Server extends AsyncTask<Void, Void, Void>
    {
	   @Override
	   protected void onPreExecute() {
		   // TODO Auto-generated method stub
		   super.onPreExecute();
	   }
	   
	   @Override
	   protected Void doInBackground(Void... params) {
		   // TODO Auto-generated metod stub
		   String url = StringPool.SERVER_URL+"log";
//		   Log.d(TAG,"log_url="+url);
		   try {
	           JSONArray EIDjArray = new JSONArray();
	           if(watch_list.exists())
	           {
	        	   List<String> list_watch = Utils.ReadFileFromExternal(StatusRecord.FILENAME_WATCH_LIST);
		           for(int i=0;i<list_watch.size();i++)
		           {
		        	   int watch_content_id = Integer.valueOf(list_watch.get(i).split("=")[0]);
		        	   long watch_content_timestamp = Long.valueOf(list_watch.get(i).split("=")[1]);
		        	   JSONObject jsonEID = new JSONObject();
		        	   jsonEID.put(StringPool.TAG_ACTION,"watch");
		        	   jsonEID.put(StringPool.TAG_TIMESTAMP,watch_content_timestamp);
		        	   jsonEID.put(StringPool.TAG_EVENT_ID,watch_content_id);
		        	   EIDjArray.put(jsonEID);
		           }
	           }
	           if(choose_list.exists())
	           {
	        	   List<String> list_choose = Utils.ReadFileFromExternal(StatusRecord.FILENAME_CHOOSE_LIST);
		           for(int i=0;i<list_choose.size();i++)
		           {
		        	   int choose_content_id = Integer.valueOf(list_choose.get(i).split("=")[0]);
		        	   long choose_content_timestamp = Long.valueOf(list_choose.get(i).split("=")[1]);
		        	   JSONObject jsonEID = new JSONObject();
		        	   jsonEID.put(StringPool.TAG_ACTION,"choose");
		        	   jsonEID.put(StringPool.TAG_TIMESTAMP,choose_content_timestamp);
		        	   jsonEID.put(StringPool.TAG_EVENT_ID,choose_content_id);
		        	   EIDjArray.put(jsonEID);
		           }
	           }
	           JSONObject jsonContent = new JSONObject();
	           jsonContent.put(StringPool.TAG_DEVICE_ID,StatusRecord.DeviceID);
	           jsonContent.put(StringPool.TAG_DATA,EIDjArray);
	           String outputStr=jsonContent.toString();
//	           Log.d(TAG,outputStr);
	           
	           List<NameValuePair> temp =new ArrayList<NameValuePair>();
	           temp.add(new BasicNameValuePair("json", outputStr));
	           HttpClient httpclient=new DefaultHttpClient();
	           HttpPost httppost = new HttpPost(url);
	           httppost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded;charset=UTF-8");
	           httppost.setEntity(new UrlEncodedFormEntity(temp, "UTF-8"));
	           HttpResponse response = httpclient.execute(httppost);
	           if(response.getStatusLine().getStatusCode()==200){
	        	   String response_str=EntityUtils.toString(response.getEntity());
//	        	   Log.d(TAG,response_str);
	        	   long time_post = new Date().getTime();
	        	   StatusRecord.sharep.setLongPreference(AbstractActivity.this, StringPool.TAG_POST_TIMESTAMP, time_post);
	        	   File file_watch=new File(StatusRecord.FILENAME_WATCH_LIST);
	        	   if(file_watch.exists())
	        		   file_watch.delete();
	        	   File file_choose=new File(StatusRecord.FILENAME_CHOOSE_LIST);
	        	   if(file_choose.exists())
	        		   file_choose.delete();
	           }
//	           con = (HttpURLConnection) new URL(url).openConnection();
//	           con.setConnectTimeout(10000);
//	           con.setReadTimeout(10000);
//	           con.setDoInput(true);
//	           con.setDoOutput(true);
//	           con.setRequestMethod("POST");
//	           con.setUseCaches(false); //no caches
//	           con.setRequestProperty("Content-Type", "application/json");
//	           con.connect();
//	           
//	           OutputStream out = con.getOutputStream();
//	           OutputStreamWriter wr = new OutputStreamWriter(out);
//	           wr.write(outputStr);
//	           wr.flush();
//	           wr.close();
//	           if (out != null)
//	           {
//	        	   out.close();
//	        	   out=null;
//	           }
//	           
//	           InputStream is = con.getInputStream();
//	           String line;
//				StringBuilder builder = new StringBuilder();
//	           BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//				while((line = reader.readLine()) != null) {
//					builder.append(line);
//				}
//	           Log.d(TAG,"builder="+builder.toString());
////	           int responseCode=con.getResponseCode();
//	           Log.d(TAG,"getResponseCode="+con.getResponseCode());
//	           if(con.getResponseCode()==200)
//	           {
//	        	   long time_post = new Date().getTime();
//	        	   StatusRecord.sharep.setLongPreference(AbstractActivity.this, StringPool.TAG_POST_TIMESTAMP, time_post);
//	        	   File file_watch=new File(StatusRecord.FILENAME_WATCH_LIST);
//	        	   if(file_watch.exists())
//	        		   file_watch.delete();
//	        	   File file_choose=new File(StatusRecord.FILENAME_CHOOSE_LIST);
//	        	   if(file_choose.exists())
//	        		   file_choose.delete();
//	           }
		   } catch (Exception e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
		   } finally{

		   }
			   return null;
	   }
	   
	   @Override
	   protected void onPostExecute(Void result) {
		   // TODO Auto-generated method stub
		   super.onPostExecute(result);
		   
	   }
    }
	protected void onDestroy()
	{
        super.onDestroy();
//        new PostLog2Server().execute();
    }
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) 
        {
        	AlertDialog.Builder alert = new AlertDialog.Builder(this);
        	alert.setTitle(getString(R.string.exit_title))
    	       .setMessage(getString(R.string.exit_info))
    	       .setCancelable(false)
    	       .setPositiveButton(getString(R.string.exit_yes), 
    	       new DialogInterface.OnClickListener() 
    	       {
    	    
    	          @Override
    	          public void onClick(DialogInterface dialog, int which) 
    	          {
    	              // TODO Auto-generated method stub
    	        	  Intent PhoneHome = new Intent(Intent.ACTION_MAIN);  
    	        	  PhoneHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
    	        	  PhoneHome.addCategory(Intent.CATEGORY_HOME);  
    	        	  startActivity(PhoneHome);
    	        	  System.exit(0);
    	          }
    	       }
    	       )
    	       .setNegativeButton(getString(R.string.exit_no), 
    	       new DialogInterface.OnClickListener() 
    	       {
    	    
    	          @Override
    	          public void onClick(DialogInterface dialog, int which) 
    	          {
    	              // TODO Auto-generated method stub
    	          }
	        });
        	alert.show();
        }
        return super.onKeyDown(keyCode, event);
    }
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction()==MotionEvent.ACTION_UP)
		{
			if(Utils.getIPAddress(true).contentEquals(""))
				toast.show();
			else
				toast.cancel();
		}
		return super.onTouchEvent(event);
	}

}