package com.wtfff.boredApp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.wtfff.boredApp.utility.HTTPConnect;
import com.wtfff.boredApp.utility.ImageLoader;
import com.wtfff.boredApp.utility.StatusRecord;
import com.wtfff.boredApp.utility.StringPool;
import com.wtfff.boredApp.utility.Utils;
import com.wtfff.boredApp.utility.WeatherTranslation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
//import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ContentActivity extends AbstractActivity{
	private static final String TAG="WTFFF_ContentActivity";
	private Button btn_finish,btn_contact_us,btn_share;
	private JSONObject contentObject=null;
	private TextView description_title,weather_tv;
	private LinearLayout description_content;//,layout_loading,layout_main;
	private ImageView description_img;
	private String url,content_id;
	private ProgressDialog dialog_process;
	private ProgressBar description_progress;
	
	
//	void setTest2(){
//		try {
//			String line;
//			AssetManager assetManager = getAssets();
//			InputStream inputStream;
//			inputStream = assetManager.open("test2.txt");
//			BufferedReader mStr = new BufferedReader(new InputStreamReader(inputStream));
//			StringBuilder builder = new StringBuilder();
//			while((line = mStr.readLine()) != null) {
//				builder.append(line);
//			}
//			contentObject = new JSONObject(builder.toString());
//			inputStream.close(); 
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				Log.e(TAG,"Exception 61 "+e);
//			}
//	}
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        dialog_process = new ProgressDialog(ContentActivity.this);
        dialog_process.setMessage(getString(R.string.wait));
        dialog_process.setCancelable(false);
//        layout_loading=(LinearLayout) findViewById(R.id.layout_loading);
//        layout_main=(LinearLayout) findViewById(R.id.layout_main);
        description_content=(LinearLayout) findViewById(R.id.description_content);
        description_title=(TextView) findViewById(R.id.description_title);
        weather_tv=(TextView) findViewById(R.id.weather_tv);
        description_progress=(ProgressBar) findViewById(R.id.description_progress);
        description_img=(ImageView) findViewById(R.id.description_img);
        url=this.getIntent().getExtras().getString(StringPool.TAG_URL);
        content_id=this.getIntent().getExtras().getString(StringPool.TAG_CONTENT_ID);
        weather_tv.setText(getString(R.string.weather)+WeatherTranslation.weatherTranslation(StatusRecord.weatherInfo.getWeather())+" "+StatusRecord.weatherInfo.getTemperature()+"℃");
//        setTest2();
        new getInfoFromServer().execute();
        btn_finish=(Button) findViewById(R.id.btn_finish);
        btn_finish.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent search_intent=new Intent();
				search_intent.setClass(ContentActivity.this, SearchActivity.class);
				startActivity(search_intent);
				finish();
			}
        });
        btn_contact_us=(Button) findViewById(R.id.btn_contact_us);
        btn_contact_us.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Uri wtf_uri=Uri.parse("http://www.wtfffstudio.com");
				Intent browser = new Intent(Intent.ACTION_VIEW,wtf_uri);
				startActivity(browser);
			}
        });
        btn_share=(Button) findViewById(R.id.btn_share);
        btn_share.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Aut9o-generated method stub
				shareDialog("我現在要去"+description_title.getText().toString()+"了～ \n無聊ＡＰＰ，解決您的無聊！\n https://play.google.com/store/apps/details?id=com.wtfff.boredApp&hl=zh_TW");
			}
        });
	}
	private void shareDialog(String shareText) {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);  
		shareIntent.setType("text/plain"); //文字檔類型  
		shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);//傳送文字  
		shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);  
		startActivity(Intent.createChooser(shareIntent, getString(R.string.btn_share)));  
	 }
	
	public class getInfoFromServer extends AsyncTask<Void, Void, Void>
	{
		   @Override
		   protected void onPreExecute() {
			   // TODO Auto-generated method stub
			   super.onPreExecute();
//			   if(layout_main.isShown())
				   dialog_process.show();
		   }
		   
		   @Override
		   protected Void doInBackground(Void... params) {
			   // TODO Auto-generated metod stub
			   //Write file before get info from server
			   List<String> temp = new ArrayList<String>();
			   long timestamp = new Date().getTime();
			   temp.add(content_id+"="+String.valueOf(timestamp));
			   Utils.WriteFileToExternal(StatusRecord.FILENAME_CHOOSE_LIST, temp);
			   HTTPConnect httpConnect = new HTTPConnect();
			   
			   JSONArray Jtemp = httpConnect.ConnectToServerGetInfo(url,mHandler);
			   if(Jtemp!=null)
			   {
				   try {
					contentObject=Jtemp.getJSONObject(0);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			   }
			   new PostLog2Server().execute();
			   return null;
		   }
		   
		   @Override
		   protected void onPostExecute(Void result) {
			   // TODO Auto-generated method stub
			   super.onPostExecute(result);
			   if(dialog_process.isShowing())
			   {
				   dialog_process.dismiss();
			   }
//			   if(!layout_main.isShown())
//			   {
//				   layout_main.setVisibility(View.VISIBLE);
//				   layout_loading.setVisibility(View.GONE);
//			   }
			   if(contentObject!=null)
			   {
					try {
						String title = contentObject.getString("title");
						String img_url = contentObject.getString("img_url");
						ImageLoader mImageLoader= new ImageLoader(description_img,description_progress,img_url,description_img.getHeight());
						try {
							mImageLoader.execute();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(title.length()>10)
						{
							description_title.setTextSize(75/density);
							if(title.getBytes().length>51)
								title=title.substring(0,17)+"...";
						}
						else
							description_title.setTextSize(90/density);
						description_title.setText(title);
						JSONArray jArray =  contentObject.getJSONArray("description");
						if(jArray!=null)
						{
							for(int i=0;i<jArray.length();i++)
							{
								JSONObject descriptionObject = jArray.getJSONObject(i);
								if(descriptionObject.getString("type").contentEquals("text"))
								{
									TextView description_tv = new TextView(ContentActivity.this);
									description_tv.setText(descriptionObject.getString("text"));
									description_tv.setTextSize(60/density);
									description_tv.setTextColor(Color.BLACK);
									description_tv.setGravity(Gravity.LEFT);
									description_content.addView(description_tv);
								}
								else if(descriptionObject.getString("type").contentEquals("link"))
								{
									final String link_url=descriptionObject.getString("url");
									Button description_btn = new Button(ContentActivity.this);
									SpannableString link_text = new SpannableString(descriptionObject.getString("text"));
									link_text.setSpan(new UnderlineSpan(), 0, link_text.length(), 0);
									description_btn.setText(link_text);
									description_btn.setTextSize(60/density);  //20dp
									description_btn.setTextColor(Color.BLUE);
									description_btn.setBackgroundDrawable(null);
									description_btn.setOnClickListener(new OnClickListener(){
										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											Uri wtf_uri;
											wtf_uri=Uri.parse(link_url);
											Intent browser = new Intent(Intent.ACTION_VIEW,wtf_uri);
											startActivity(browser);
										}
							        });
									description_content.addView(description_btn);
								}
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
//						Log.e(TAG,"Exception 211 "+e);
					}
			   }
		   }
	}
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(dialog_process!=null)
		{
			dialog_process.dismiss();
			dialog_process=null;
		}
	}

}
