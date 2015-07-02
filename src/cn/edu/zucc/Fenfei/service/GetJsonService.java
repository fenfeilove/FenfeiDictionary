package cn.edu.zucc.Fenfei.service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;
import cn.edu.zucc.Fenfei.bean.TransWord;
import cn.edu.zucc.Fenfei.bean.WebData;
import cn.edu.zucc.Fenfei.util.HttpUtils;
import cn.edu.zucc.Fenfei.util.StreamTool;

public class GetJsonService {
	
	/**
	 * 从有道服务器获取json数据 add by fenfei
	 * @param queryword
	 * @return
	 */
	public static String getJsonData(String queryword){
		String jsondata="";
		try {
			HttpURLConnection conn=HttpUtils.getJson(queryword);
			if(conn.getResponseCode()==200){
				InputStream json=conn.getInputStream();
				byte[] data=StreamTool.read(json);
				jsondata=new String(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsondata;
	}
	
	/**
	 * 解析json数据 add by fenfei 
	 * @param queryword
	 * @return
	 * @throws Exception
	 */
	public static TransWord parseJson(String queryword) throws Exception{
		//Log.d("qry",queryword);
		TransWord tw=new TransWord();
		String tt="";
		String temp=getJsonData(queryword);
		//Log.d("date",temp);
		JSONObject  dataJson=new JSONObject(temp);
		tw.setErrorCode(dataJson.getString("errorCode"));
		//解析常见释义
		JSONArray data=dataJson.getJSONArray("translation");
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<data.length();i++)
		{
			sb.append(data.getString(i)+",");
		}
		tt=sb.toString();
		tw.setSimpletrans(tt.substring(0, tt.length()-1));
		if(dataJson.has("query")){
			tw.setQueryword(dataJson.getString("query"));
		}
		//解析基本释义
		if(dataJson.has("basic")){
			JSONObject basicdata=dataJson.getJSONObject("basic");
			JSONArray basicdatas=basicdata.getJSONArray("explains");
			StringBuilder ss=new StringBuilder();
			for(int i=0;i<basicdatas.length();i++){
				ss.append(basicdatas.getString(i)+"\n");
			}
			tw.setBasictrans(ss.toString());
		}
		//解析网络释义
		if(dataJson.has("web")){
			List<WebData> listwd=new ArrayList<WebData>();
			WebData wd=null;
			JSONArray webdatas=dataJson.getJSONArray("web");
			for(int i=0;i<webdatas.length();i++){
				wd=new WebData();
				JSONObject webobj=webdatas.getJSONObject(i);
				wd.setKey(webobj.getString("key"));
				JSONArray webvalue=webobj.getJSONArray("value");
				StringBuilder sss=new StringBuilder();
				for(int j=0;j<webvalue.length();j++){
					sss.append(webvalue.getString(j)+"，");
				}
				tt=sss.toString();
				wd.setValue(tt.substring(0, tt.length()-1));
				listwd.add(wd);
			}
			tw.setWebtrans(listwd);
		}
		//Log.d("check", "success");
		return tw;
	}
}
