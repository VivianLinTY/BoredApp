package com.wtfff.boredApp.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;

import android.util.Log;


public class Utils {
	private static final String TAG="WTFFF_Utils";
	
	public static void WriteFileToExternal(String file_path, List<String> content){
		File file = new File(file_path);
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		List<String> list_read = ReadFileFromExternal(file_path);
		try{
			for(int i=0;i<content.size();i++)
			{
//				boolean same = false;
//				for(int j=0;j<list_read.size();j++)
//				{
//					String id = (list_read.get(j).split("="))[0];
//					int number = Integer.valueOf((list_read.get(j).split("="))[1]);
//					if(content.get(i).contentEquals(id))
//					{
//						list_read.remove(j);
//						number=number+1;
//						list_read.add(id+"="+String.valueOf(number));
//						same=true;
//						break;
//					}
//				}
//				if(!same)
//				{
					list_read.add(content.get(i));
//				}
			}
			FileOutputStream fos = new FileOutputStream(file_path);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			for (int k=0;k<list_read.size();k++)
			{
				bw.write(list_read.get(k));
				bw.newLine();
			}
			bw.close();
			fos.close();
			bw=null;
			fos=null;
	    }catch(Exception e){
	       e.printStackTrace();
	       Log.e(TAG,"Exception 22 :"+e);
	    }
	}
	public static List<String> ReadFileFromExternal(String file_path){
		List<String> rv=new ArrayList<String>();;
		try{
			FileInputStream fis = new FileInputStream(file_path);
			BufferedReader mStr = new BufferedReader(new InputStreamReader(fis));
			String myLine;
			while((myLine=mStr.readLine())!=null) 
			{
				rv.add(myLine);
			}
	    }catch(Exception e){
	       e.printStackTrace();
	       Log.e(TAG,"Exception 44 :"+e);
	    }
		return rv;
	}
	public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr); 
                        if (useIPv4) {
                            if (isIPv4) 
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%');
                                return delim<0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { }
        return "";
    }

}