package cn.edu.zucc.Fenfei.util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

import android.util.Log;

public class HttpUtils {
	public static final String key="1024697229";
	public static final String keyfrom="fenfeilove";
	
	public static HttpURLConnection getJson(String queryword) throws Exception{
		String path="http://fanyi.youdao.com/openapi.do?keyfrom="+keyfrom+"&key="+key+"&type=data&doctype=json&version=1.1&q="+URLDecoder.decode(queryword,"UTF-8");
		//Log.d("url", path);
		//Log.d("url2",URLDecoder.decode(path,"UTF-8"));
		HttpURLConnection conn=(HttpURLConnection)new URL(path).openConnection();
		conn.setConnectTimeout(3000);
		conn.setRequestMethod("GET");
		return conn;
	}
}
