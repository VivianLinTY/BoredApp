package com.wtfff.boredApp.utility;

import java.util.Locale;

public class WeatherTranslation {
	
	public static String weatherTranslation(String weather)
	{
		String rv="";
		if(Locale.getDefault().getLanguage().contains("zh"))
		{
			weather=weather.toLowerCase();
			String[] split_str=weather.split("/");
			for(int i=0;i<split_str.length;i++)
			{
				if(i!=0)
				{
					rv=rv+"/";
				}
				if(split_str[i].contentEquals("tornado")){
					rv=rv+"龍捲風";
				}else if(split_str[i].contentEquals("tropical storm")){
					rv=rv+"熱帶風暴";
				}else if(split_str[i].contentEquals("hurricane")){
					rv=rv+"颶風";
				}else if(split_str[i].contentEquals("severe thunderstorms")){
					rv=rv+"嚴重雷暴";
				}else if(split_str[i].contentEquals("thunderstorms")){
					rv=rv+"持續雷暴";
				}else if(split_str[i].contentEquals("mixed rain and snow")){
					rv=rv+"雨夾雪";
				}else if(split_str[i].contentEquals("mixed rain and sleet")){
					rv=rv+"雨霙混合";
				}else if(split_str[i].contentEquals("mixed snow and sleet")){
					rv=rv+"雪霙混合";
				}else if(split_str[i].contentEquals("freezing drizzle")){
					rv=rv+"感到寒意的毛毛雨";
				}else if(split_str[i].contentEquals("drizzle")){
					rv=rv+"毛毛雨";
				}else if(split_str[i].contentEquals("freezing rain")){
					rv=rv+"凍雨";
				}else if(split_str[i].contentEquals("showers")){
					rv=rv+"陣雨";
				}else if(split_str[i].contentEquals("light snow showers")){
					rv=rv+"輕微陣雨夾雪";
				}else if(split_str[i].contentEquals("snow flurries")){
					rv=rv+"微雪";
				}else if(split_str[i].contentEquals("blowing snow")){
					rv=rv+"高吹雪";
				}else if(split_str[i].contentEquals("snow")){
					rv=rv+"雪";
				}else if(split_str[i].contentEquals("hail")){
					rv=rv+"雹";
				}else if(split_str[i].contentEquals("sleet")){
					rv=rv+"冰雨";
				}else if(split_str[i].contentEquals("dust")){
					rv=rv+"灰塵";
				}else if(split_str[i].contentEquals("foggy")){
					rv=rv+"多霧的";
				}else if(split_str[i].contentEquals("haze")){
					rv=rv+"霾";
				}else if(split_str[i].contentEquals("smoky")){
					rv=rv+"多煙的";
				}else if(split_str[i].contentEquals("blustery")){
					rv=rv+"大風";
				}else if(split_str[i].contentEquals("windy")){
					rv=rv+"微風";
				}else if(split_str[i].contentEquals("cold")){
					rv=rv+"寒冷";
				}else if(split_str[i].contentEquals("cloudy")){
					rv=rv+"多雲";
				}else if(split_str[i].contentEquals("mostly cloudy")){
					rv=rv+"多雲";
				}else if(split_str[i].contentEquals("mostly cloudy (night)")){
					rv=rv+"多雲(夜晚)";
				}else if(split_str[i].contentEquals("mostly cloudy (day)")){
					rv=rv+"多雲(白天)";
				}else if(split_str[i].contentEquals("mostly sunny")){
					rv=rv+"晴時多雲";
				}else if(split_str[i].contentEquals("partly cloudy")){
					rv=rv+"局部有雲";
				}else if(split_str[i].contentEquals("partly cloudy (night)")){
					rv=rv+"晴時多雲(夜晚)";
				}else if(split_str[i].contentEquals("partly cloudy (day)")){
					rv=rv+"晴時多雲(白天)";
				}else if(split_str[i].contentEquals("clear (night)")){
					rv=rv+"晴(夜晚)";
				}else if(split_str[i].contentEquals("fair")){
					rv=rv+"晴";
				}else if(split_str[i].contentEquals("clear")){
					rv=rv+"晴";
				}else if(split_str[i].contentEquals("sunny")){
					rv=rv+"豔陽";
				}else if(split_str[i].contentEquals("fair (night)")){
					rv=rv+"晴(夜晚)";
				}else if(split_str[i].contentEquals("fair (day)")){
					rv=rv+"晴(白天)";
				}else if(split_str[i].contentEquals("mixed rain and hail")){
					rv=rv+"雨夾雹";
				}else if(split_str[i].contentEquals("hot")){
					rv=rv+"嚴熱";
				}else if(split_str[i].contentEquals("isolated thunderstorms")){
					rv=rv+"局部持續雷暴";
				}else if(split_str[i].contentEquals("scattered thunderstorms")){
					rv=rv+"零散持續雷暴";
				}else if(split_str[i].contentEquals("scattered thunderstorms")){
					rv=rv+"零散持續雷暴";
				}else if(split_str[i].contentEquals("scattered showers")){
					rv=rv+"零散陣雨";
				}else if(split_str[i].contentEquals("heavy snow")){
					rv=rv+"大雪";
				}else if(split_str[i].contentEquals("scattered snow showers")){
					rv=rv+"零散陣雪";
				}else if(split_str[i].contentEquals("partly cloudy")){
					rv=rv+"晴時多雲";
				}else if(split_str[i].contentEquals("thundershowers")){
					rv=rv+"雷陣雨";
				}else if(split_str[i].contentEquals("snow showers")){
					rv=rv+"陣雪";
				}else if(split_str[i].contentEquals("isolated thundershowers")){
					rv=rv+"局部持續雷陣雨";
				}else if(split_str[i].contentEquals("thunder")){
					rv=rv+"打雷";
				}
			}
		}
		else
			rv=weather;
		return rv;
	}

}
