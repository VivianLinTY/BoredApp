package com.wtfff.boredApp.utility;

import java.util.ArrayList;

import com.wtfff.boredApp.info.SelectListInfo;
import com.wtfff.boredApp.info.WeatherInfo;

public class StatusRecord {

	public static String DeviceID="";
	public static String Latitude="";
	public static String Longitude="";
	public static WeatherInfo weatherInfo=new WeatherInfo();
	public static SharePreference sharep = new SharePreference();
	public static int Conn_Number=0;
	public static ArrayList<SelectListInfo> do_list_slot=new ArrayList<SelectListInfo>();
	public static final String FILENAME_CHOOSE_LIST = "/data/data/com.wtfff.boredApp/choose.txt";
	public static final String FILENAME_WATCH_LIST = "/data/data/com.wtfff.boredApp/watch.txt";
}
