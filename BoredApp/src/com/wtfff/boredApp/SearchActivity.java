package com.wtfff.boredApp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.wtfff.wheel.listener.WheelScrollListener;
import com.wtfff.wheel.WheelView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wtfff.boredApp.utility.HTTPConnect;
import com.wtfff.boredApp.utility.LocationTake;
import com.wtfff.boredApp.utility.StatusRecord;
import com.wtfff.boredApp.utility.StringPool;
import com.wtfff.boredApp.utility.Utils;
import com.wtfff.boredApp.R;

public class SearchActivity extends AbstractActivity {
	private static final String TAG="WTFFF_SearchActivity";
	private Button btn_like,btn_change,btn_try,image_face_field;
	private ImageView image_face;
	private TextView app_title_tv,tip_location_tv,tip_record_tv,tips1_tv=null,tips2_tv=null,tips3_tv=null,fun_tip;
	private boolean tip2_shine=false,tip3_shine=false;
	private WheelView slot;
	private SlotMachine mSlotMachine = new SlotMachine();
	private WheelScrollListener wheelScrollListener=null;
	private String lang="zh_TW";
	private int select_id=0;
	private String url="";
//    private double lat=25.0402555, lng=121.512377;
    private double lat=0, lng=0;
    private android.location.Location location_now=null;
    private ProgressDialog dialog_process;
    private List<String> watch_temp = new ArrayList<String>();
    private LocationListener mLocationListener;
    private Dialog tipsDialog=null;
    private LinearLayout layout_loading,layout_main,layout_fun;
    private RelativeLayout layout_slot;
    private int number_click=0,number_click_con=1,number_click_con_max=1;
    private long last_touchTime=0;

//	void setTest(){
//		 try {
//			   String line;
//			   AssetManager assetManager = getAssets();
//			   InputStream inputStream;
//				inputStream = assetManager.open("test.txt");
//				BufferedReader mStr = new BufferedReader(new InputStreamReader(inputStream));
//				   StringBuilder builder = new StringBuilder();
//				   while((line = mStr.readLine()) != null) {
//				   builder.append(line);
//			   }
//			   JSONArray jArray= new JSONArray(builder.toString());
//			   if (jArray != null) {
//				   for (int i=0;i<jArray.length();i++){ 
//					   JSONObject responseObject;
//					try {
//						responseObject = jArray.getJSONObject(i);
//						SelectListInfo mSelectListInfo = new SelectListInfo();
//						mSelectListInfo.setEventId(responseObject.getInt("event_id"));
//						mSelectListInfo.setTitle(responseObject.getString("title"));
//						StatusRecord.do_list_slot.add(mSelectListInfo);
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				   } 
//				}
//			   search_number=search_number+1;
//			   inputStream.close(); 
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	private void getList(){
//		setTest();
		new getInfoFromServer().execute();
	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);
        if(StatusRecord.sharep.getStringPreferences(SearchActivity.this, StringPool.TAG_HAS_SCORE, "").contentEquals(""))
        {
        	tips1_tv=(TextView) findViewById(R.id.tips1_tv);
        	tips1_tv.setVisibility(View.GONE);
        	tips2_tv=(TextView) findViewById(R.id.tips2_tv);
        	tips3_tv=(TextView) findViewById(R.id.tips3_tv);
	        tipsDialog = new Dialog(SearchActivity.this,R.style.MyDialog);
	        tipsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	        tipsDialog.setContentView(R.layout.dialog_tips1);
	        btn_try=(Button) tipsDialog.findViewById(R.id.btn_try);
	        btn_try.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					tipsDialog.dismiss();
				}
	        });
        }
        if(StatusRecord.sharep.getStringPreferences(SearchActivity.this, StringPool.TAG_DEVICE_ID, "").contentEquals(""))
        {
            TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        	StatusRecord.DeviceID=tm.getDeviceId();
        	StatusRecord.sharep.setStringPreference(SearchActivity.this,StringPool.TAG_DEVICE_ID,StatusRecord.DeviceID);
        	StatusRecord.sharep.setStringPreference(SearchActivity.this,StringPool.TAG_CONNECT_NUMBER,"0");
        }
        else
        {
        	StatusRecord.sharep.initUserInfo(SearchActivity.this);
        }
        mLocationListener = new LocationListener(){
        	@Override
        	public void onProviderDisabled(String arg0) {
        		// TODO Auto-generated method stub
        	}
        	
        	@Override
        	public void onProviderEnabled(String arg0) {
        		// TODO Auto-generated method stub
        	}
        	
        	@Override
        	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        		// TODO Auto-generated method stub
        	}
        	@Override
        	public void onLocationChanged(android.location.Location location) {
        		// TODO Auto-generated method stub
        	}
        };
        layout_fun=(LinearLayout) findViewById(R.id.layout_fun);
        layout_loading=(LinearLayout) findViewById(R.id.layout_loading);
        layout_main=(LinearLayout) findViewById(R.id.layout_main);
        layout_slot=(RelativeLayout) findViewById(R.id.layout_slot);
        btn_change=(Button) findViewById(R.id.btn_change);
		btn_like=(Button) findViewById(R.id.btn_like);
		image_face_field=(Button) findViewById(R.id.image_face_field);
		image_face=(ImageView) findViewById(R.id.image_face);
		app_title_tv=(TextView) findViewById(R.id.app_title_tv);
		tip_location_tv=(TextView) findViewById(R.id.tip_location_tv);
		tip_record_tv=(TextView) findViewById(R.id.tip_record_tv);
		fun_tip=(TextView) findViewById(R.id.fun_tip);
		btn_change.setText(getString(R.string.help_me));
        slot=(WheelView) findViewById(R.id.slot);
        mSlotMachine.initWheel(slot, SearchActivity.this,density,layout_slot.getHeight());
        dialog_process = new ProgressDialog(SearchActivity.this);
        dialog_process.setMessage(getString(R.string.wait));
        dialog_process.setCancelable(false);
        number_click=StatusRecord.sharep.getIntPreferences(SearchActivity.this, StringPool.TAG_CLICK, 0);
        number_click_con_max=StatusRecord.sharep.getIntPreferences(SearchActivity.this, StringPool.TAG_CLICK_CON, 0);
        image_face_field.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				number_click=number_click+1;
				long touchTime = System.currentTimeMillis();
				if(touchTime-last_touchTime<800)
				{
					number_click_con=number_click_con+1;
					if(number_click_con>number_click_con_max)
					{
						number_click_con_max=number_click_con;
						StatusRecord.sharep.setIntPreference(SearchActivity.this,StringPool.TAG_CLICK_CON,number_click_con_max);
					}
				}
				else
					number_click_con=1;
				last_touchTime=touchTime;
			}
        });
        image_face_field.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN)
					image_face.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_face_click));
				else
					image_face.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_face));
					
				return false;
			}	
        });
        LocationTake mLocationTake = new LocationTake();
        location_now=mLocationTake.updateStat(SearchActivity.this,mLocationListener);
		if(location_now!=null)
		{
			lat=location_now.getLatitude();
			lng=location_now.getLongitude();
			StatusRecord.Latitude=String.valueOf(lat);
			StatusRecord.Longitude=String.valueOf(lng);
		    StatusRecord.sharep.setStringPreference(SearchActivity.this, StringPool.TAG_LATITUDE, StatusRecord.Latitude);
		    StatusRecord.sharep.setStringPreference(SearchActivity.this, StringPool.TAG_LONGITUDE, StatusRecord.Longitude);
		}
        getList();
    }
    @Override
    public void onResume(){
    	super.onResume();
		if(StatusRecord.sharep.getStringPreferences(SearchActivity.this, StringPool.TAG_HAS_SCORE, "").contentEquals("false"))
			showRateDialog();
    	if(!Locale.getDefault().getLanguage().contentEquals("") && !Locale.getDefault().getCountry().contentEquals(""))
    		lang =Locale.getDefault().getLanguage()+"_"+Locale.getDefault().getCountry();
    }
    public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(dialog_process!=null)
		{
			dialog_process.dismiss();
			dialog_process=null;
		}
		mHandler.removeCallbacks(setTip2Text);
		mHandler.removeCallbacks(setTip3Text);
		Utils.WriteFileToExternal(StatusRecord.FILENAME_WATCH_LIST, watch_temp);
		StatusRecord.sharep.setStringPreference(SearchActivity.this,StringPool.TAG_CONNECT_NUMBER,String.valueOf(StatusRecord.Conn_Number));
	}
    private void mixWheel() {
    	slot.scroll(-350 + (int)(Math.random() * 50), 1000);
    }
   public class getInfoFromServer extends AsyncTask<Void, Void, Void>
   {
	   @Override
	   protected void onPreExecute() {
		   // TODO Auto-generated method stub
		   super.onPreExecute();
		   if(layout_main.isShown())
			   dialog_process.show();
	   }
	   
	   @Override
	   protected Void doInBackground(Void... params) {
		   // TODO Auto-generated metod stub
		   HTTPConnect httpConnect = new HTTPConnect();
		   if(lat==0 && lng==0)
		   {
			   httpConnect.ConnectToGetLocation(SearchActivity.this);
			   lat=Double.valueOf(StatusRecord.Latitude);
			   lng=Double.valueOf(StatusRecord.Longitude);
		   }
		   httpConnect.ConnectToGetWeather();
		   StatusRecord.Conn_Number=httpConnect.ConnectToServerGetList(lang,StatusRecord.Conn_Number,mHandler);
		   return null;
	   }
	   
	   @Override
	   protected void onPostExecute(Void result) {
		   // TODO Auto-generated method stub
		   super.onPostExecute(result);
		   mHandler.post(funThd);
		   if(!layout_main.isShown())
		   {
			   layout_main.setVisibility(View.VISIBLE);
			   layout_loading.setVisibility(View.GONE);
			   if(tipsDialog!=null)
				   tipsDialog.show();
		   }
		   if(StatusRecord.do_list_slot.size()>0){
			    mSlotMachine.initWheel(slot,SearchActivity.this,density,layout_slot.getHeight());
		        btn_change.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(tips1_tv!=null && tips1_tv.isShown())
							tips1_tv.setVisibility(View.INVISIBLE);
						if(network)
						{
							if(app_title_tv.isShown())
							{
								app_title_tv.setVisibility(View.GONE);
								slot.setVisibility(View.VISIBLE);
							}
							if(StatusRecord.do_list_slot.size()<10)
								getList();
							else
								StatusRecord.do_list_slot.remove(slot.getCurrentItem());
							mSlotMachine.initWheel(slot,SearchActivity.this,density,layout_slot.getHeight());
							mixWheel();
							if(tips1_tv!=null && tips1_tv.getVisibility()==View.GONE)
								tips1_tv.setVisibility(View.VISIBLE);
						}
						else
						{
							getList();
						}
					}
		        });
		        btn_like.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						select_id=StatusRecord.do_list_slot.get(slot.getCurrentItem()).getEventId();
						if(StatusRecord.sharep.getStringPreferences(SearchActivity.this, StringPool.TAG_HAS_SCORE, "").contentEquals(""))
							StatusRecord.sharep.setStringPreference(SearchActivity.this, StringPool.TAG_HAS_SCORE, "false");slot.getCurrentItem();
						url=StringPool.SERVER_URL+"event/"+String.valueOf(select_id)+"?device_id="+StatusRecord.DeviceID+"&lang="+lang;
//						Log.d(TAG,"url="+url);
						Intent content = new Intent();
						content.setClass(SearchActivity.this, ContentActivity.class);
						content.putExtra(StringPool.TAG_URL, url);
						content.putExtra(StringPool.TAG_CONTENT_ID, String.valueOf(select_id));
						startActivity(content);
						finish();
					}
		        });
		        if(wheelScrollListener==null)
		        {
		        	wheelScrollListener=new WheelScrollListener(){
						@Override
						public void onScrollingStarted(WheelView wheel) {
							// TODO Auto-generated method stub
							btn_change.setClickable(false);
							btn_like.setClickable(false);
						}
	
						@Override
						public void onScrollingFinished(WheelView wheel) {
							// TODO Auto-generated method stub
							btn_change.setText(getString(R.string.btn_change_tv));
							btn_like.setVisibility(View.VISIBLE);
							btn_change.setClickable(true);
							btn_like.setClickable(true);
							tip_location_tv.setVisibility(View.GONE);
							tip_record_tv.setVisibility(View.VISIBLE);
							long timestamp = new Date().getTime();
							watch_temp.add(String.valueOf(StatusRecord.do_list_slot.get(slot.getCurrentItem()).getEventId())+"="+String.valueOf(timestamp));
							if(tips2_tv!=null && !tips2_tv.isShown())
							{
								tips2_tv.setVisibility(View.VISIBLE);
								mHandler.postDelayed(setTip2Text, 1000);
							}
							else if(tips3_tv!=null && !tips3_tv.isShown() && tips2_tv.isShown())
							{
								tips3_tv.setVisibility(View.VISIBLE);
								mHandler.postDelayed(setTip3Text, 1000);
							}
						}
		        	};
		        }
		        slot.addScrollingListener(wheelScrollListener);
		   }
		   else
			   getList();
		   if(dialog_process!=null)
			   dialog_process.dismiss();
	   }
   }
   
   Runnable setTip2Text = new Runnable() {
       public void run() {
    	   if(tip2_shine)
    	   {
    		   tips2_tv.setText(getString(R.string.content_tip2_normal));
    		   tip2_shine=false;
    	   }
    	   else
    	   {
    		   tips2_tv.setText(getString(R.string.content_tip2_shine));
    		   tip2_shine=true;
    	   }
		   mHandler.postDelayed(this, 1000);
       }
   };
   Runnable setTip3Text = new Runnable() {
       public void run() {
    	   if(tip3_shine)
    	   {
    		   tips3_tv.setText(getString(R.string.content_tip3_normal));
    		   tip3_shine=false;
    	   }
    	   else
    	   {
    		   tips3_tv.setText(getString(R.string.content_tip3_shine));
    		   tip3_shine=true;
    	   }
		   mHandler.postDelayed(this, 1000);
       }
   };
   Runnable funThd = new Runnable() {
       public void run() {
    	   if(layout_fun.isShown())
    	   {
    		  layout_fun.setVisibility(View.INVISIBLE);
    		  StatusRecord.sharep.setIntPreference(SearchActivity.this,StringPool.TAG_CLICK,number_click);
    	   }
    	   else
    	   {
    		   if(number_click!=0)
    		   {
    			   fun_tip.setText("被戳了"+String.valueOf(number_click)+"下！\n最大連續戳的次數："+String.valueOf(number_click_con_max));
    		   }
    		  layout_fun.setVisibility(View.VISIBLE);
    	   }
		   mHandler.postDelayed(this, 1000);
       }
   };
   private void showRateDialog(){
		final Dialog rateDialog = new Dialog(SearchActivity.this,R.style.MyDialog);
		rateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		View mDialog_view = ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_rate, null);
		rateDialog.setContentView(mDialog_view);
		Button btn_rate = (Button) rateDialog.findViewById(R.id.btn_rate);
		btn_rate.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String score_address="market://details?id=com.wtfff.boredApp";
				try{
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(score_address)));
				}catch(ActivityNotFoundException e){
					e.printStackTrace();
				}
				rateDialog.dismiss();
			}
		});
		Button btn_leave = (Button) rateDialog.findViewById(R.id.btn_leave);
		btn_leave.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rateDialog.dismiss();
			}
		});
		rateDialog.show();
		StatusRecord.sharep.setStringPreference(SearchActivity.this, StringPool.TAG_HAS_SCORE, "true");
	}
}