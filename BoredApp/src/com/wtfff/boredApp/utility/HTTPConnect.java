package com.wtfff.boredApp.utility;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import com.wtfff.boredApp.info.SelectListInfo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
//import android.util.Log;

public class HTTPConnect {
	private static final String TAG="WTFFF_HTTPConnection";
	private static final int connect_failed=1;
	private static final int connect_success=0;
	
	public int ConnectToServerGetList(String lang, int search_number, Handler mHandler){
		String url=StringPool.SERVER_URL+"activity?device_id="+StatusRecord.DeviceID+"&lat="+StatusRecord.Latitude
	    		+"&lng="+StatusRecord.Longitude+"&lang="+lang+"&num="+String.valueOf(StatusRecord.Conn_Number)+"&weather="+StatusRecord.weatherInfo.getWeather();
//		Log.d(TAG,"url="+url);
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection) new URL(url).openConnection();
			con.setConnectTimeout(10000);
			con.setReadTimeout(10000);
			con.setRequestProperty("Content-Type","text/xml;   charset=utf-8");
			con.setRequestProperty("Connection", "close");
			
			String line;
			StringBuilder builder = new StringBuilder();

			InputStream is = con.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			while((line = reader.readLine()) != null) {
				builder.append(line);
				}
//			Log.d(TAG,"builder="+builder.toString());
			JSONArray jArray= new JSONArray(builder.toString());
			if (jArray != null) {
				for (int i=0;i<jArray.length();i++){ 
					JSONObject responseObject;
					responseObject = jArray.getJSONObject(i);
					SelectListInfo mSelectListInfo = new SelectListInfo();
					mSelectListInfo.setEventId(responseObject.getInt("event_id"));
					mSelectListInfo.setTitle(responseObject.getString("title"));
					StatusRecord.do_list_slot.add(mSelectListInfo);
				} 
			}
			search_number=search_number+1;
			reader.close();
			reader=null;
			is.close();
			is=null;
			Message msg = mHandler.obtainMessage(1, "");
			msg.what=connect_success;
			mHandler.sendMessage(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			Log.e(TAG,"Exception 66 e:"+e);
			Message msg = mHandler.obtainMessage(1, "");
			msg.what=connect_failed;
			mHandler.sendMessage(msg);
		} finally{
			if(con!=null){
			con.disconnect();
			con=null;
			}
		}
		return search_number;
	}

	public JSONArray ConnectToServerGetInfo(String url, Handler mHandler){
		JSONArray contentObject=null;
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection) new URL(url).openConnection();
			con.setConnectTimeout(10000);
			con.setReadTimeout(10000);
			con.setRequestProperty("Content-Type","text/xml;   charset=utf-8");
			con.setRequestProperty("Connection", "close");
			
			String line;
			StringBuilder builder = new StringBuilder();
			InputStream is = con.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			while((line = reader.readLine()) != null) {
				builder.append(line);
			}
//			Log.d(TAG,"builder="+builder.toString());
			contentObject = new JSONArray(builder.toString());
			reader.close();
			reader=null;
			is.close();
			is=null;
			Message msg = mHandler.obtainMessage(1, "");
			msg.what=connect_success;
			mHandler.sendMessage(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			Log.e(TAG,"Exception 90 e:"+e);
			Message msg = mHandler.obtainMessage(1, "");
			msg.what=connect_failed;
			mHandler.sendMessage(msg);
		} finally{
			if(con!=null){
			con.disconnect();
			con=null;
			}
		}
		return contentObject;
	}
	
	public void ConnectToGetWeather(){
		String woeid="28752477";
		String url_woeid="http://query.yahooapis.com/v1/public/yql?q=select%20woeid%20from%20geo.placefinder%20where%20text=%22"+StatusRecord.Latitude+","+StatusRecord.Longitude+"%22%20and%20gflags=%22R%22";
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) new URL(url_woeid).openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			conn.setRequestProperty("Content-Type","text/xml;   charset=utf-8");
			conn.setRequestProperty("Connection", "close");
			conn.connect();
            if(conn.getResponseCode() == 200)
            {
	            InputStream instream = conn.getInputStream();
	            BufferedReader mStr = new BufferedReader(new InputStreamReader(instream));
	            String myLine;
	            while((myLine=mStr.readLine())!=null)
				{
	            	if(myLine.contains("woeid"))
	            	{
//	            		Log.d(TAG,"myLine="+myLine);
	            		myLine=myLine.replace(">", "<");
	            		final String[] string_split=myLine.split("<");
	            		woeid=string_split[8];
	            	}
				}
				instream.close();
				instream=null;
            }else{
//            	Log.d(TAG,"woeid connection failed con.getResponseCode()="+conn.getResponseCode());
            }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			conn.disconnect();
			conn = null;
    	}
		String url_weather="http://weather.yahooapis.com/forecastrss?w="+woeid+"&u=c";
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection) new URL(url_weather).openConnection();
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setRequestProperty("Content-Type","text/xml;   charset=utf-8");
            con.setRequestProperty("Connection", "close");
            con.connect();
            if(con.getResponseCode() == 200)
            {
	            InputStream instream = con.getInputStream();
	            BufferedReader mStr = new BufferedReader(new InputStreamReader(instream));
	            String myLine;
	            boolean getWeather = false;
	            while((myLine=mStr.readLine())!=null)
	            {
	            	if(getWeather)
	            	{
	            		int length = myLine.indexOf("<")-1;
	            		myLine=myLine.substring(0,length);
//	            		Log.d(TAG,"myLine="+myLine);
	            		String[] weather=myLine.split(", ");
	            		StatusRecord.weatherInfo.setWeather(weather[0]);
	            		StatusRecord.weatherInfo.setTemperature(weather[1]);
	            		getWeather=false;
	            	}
	            	else if(myLine.contains("Current Conditions"))
	            		getWeather=true;
	            }
				instream.close();
				instream=null;
	        }else{
//	        	Log.d(TAG,"connection failed con.getResponseCode()="+con.getResponseCode());
	        }	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			con.disconnect();
			con = null;
		}
	}
	
	public void ConnectToGetLocation(Context mContext){
		String ip_url="http://ip-api.com/json/"+Utils.getIPAddress(true)+"?fields=258047";
//		Log.d(TAG,"ip_url="+ip_url);
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection) new URL(ip_url).openConnection();
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			con.setRequestProperty("Connection", "close");
			String line;
			StringBuilder builder = new StringBuilder();
			InputStream is = con.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			while((line = reader.readLine()) != null) {
				builder.append(line);
			}
			JSONObject jObj= new JSONObject(builder.toString());
			StatusRecord.Latitude=jObj.getString("lat");
			StatusRecord.Longitude=jObj.getString("lon");
			if(StatusRecord.Latitude!=null && !StatusRecord.Latitude.contentEquals(""))
			{
				StatusRecord.sharep.setStringPreference(mContext, StringPool.TAG_LATITUDE, StatusRecord.Latitude);
				StatusRecord.sharep.setStringPreference(mContext, StringPool.TAG_LONGITUDE, StatusRecord.Longitude);
			}
			reader.close();
			reader=null;
			is.close();
			is=null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			StatusRecord.Latitude="25.0402555";
			StatusRecord.Longitude="121.512377";
		} finally{
			if(con!=null){
				con.disconnect();
				con=null;
			}
		}
	}
}
