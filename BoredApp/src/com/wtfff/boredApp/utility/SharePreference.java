package com.wtfff.boredApp.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreference {
	
	public void setStringPreference(Context context, String tag_string, String content_string)
	{
		SharedPreferences preference = context.getSharedPreferences(StringPool.TAG_USER_PREFERENCE, context.MODE_PRIVATE);
		preference.edit().putString(tag_string, content_string).commit();
	}
	public String getStringPreferences(Context context, String key, String defaultValue)
	{
		SharedPreferences preference = context.getSharedPreferences(StringPool.TAG_USER_PREFERENCE, context.MODE_PRIVATE);
		return ( null == preference ) ? defaultValue : preference.getString(key, defaultValue);
	}
	public void setIntPreference(Context context, String tag_string, int num)
	{
		SharedPreferences preference = context.getSharedPreferences(StringPool.TAG_USER_PREFERENCE, context.MODE_PRIVATE);
		preference.edit().putInt(tag_string, num).commit();
	}
	public int getIntPreferences(Context context, String key, int defaultValue)
	{
		SharedPreferences preference = context.getSharedPreferences(StringPool.TAG_USER_PREFERENCE, context.MODE_PRIVATE);
		return ( null == preference ) ? defaultValue : preference.getInt(key, defaultValue);
	}
	public void setLongPreference(Context context, String tag_string, long content_string)
	{
		SharedPreferences preference = context.getSharedPreferences(StringPool.TAG_USER_PREFERENCE, context.MODE_PRIVATE);
		preference.edit().putLong(tag_string, content_string).commit();
	}
	public long getLongPreferences(Context context, String key, long defaultValue)
	{
		SharedPreferences preference = context.getSharedPreferences(StringPool.TAG_USER_PREFERENCE, context.MODE_PRIVATE);
		return ( null == preference ) ? defaultValue : preference.getLong(key, defaultValue);
	}
	public void initUserInfo(Context context)
	{
		StatusRecord.DeviceID=getStringPreferences(context, StringPool.TAG_DEVICE_ID, "");
		StatusRecord.Conn_Number=Integer.valueOf(getStringPreferences(context, StringPool.TAG_CONNECT_NUMBER, ""));
		StatusRecord.Latitude=getStringPreferences(context, StringPool.TAG_LATITUDE, "");
		StatusRecord.Longitude=getStringPreferences(context, StringPool.TAG_LONGITUDE, "");
	}

}
