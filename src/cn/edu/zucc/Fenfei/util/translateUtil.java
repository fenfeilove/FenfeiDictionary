package cn.edu.zucc.Fenfei.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Handler;

public class translateUtil extends Thread{
	private String url;
	private String msg;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	private Handler handler;
	public translateUtil(String url,Handler handler)
	{
		this.url=url;
		this.handler=handler;
	}
	public void run()
	{
		try {
			AnalyzingOfJson();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void AnalyzingOfJson() throws Exception
	{
		// 第一步，创建HttpGet对象
		HttpGet httpGet = new HttpGet(url);
		// 第二步，使用execute方法发送HTTP GET请求，并返回HttpResponse对象
		HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
		if (httpResponse.getStatusLine().getStatusCode() == 200)
		{
			// 第三步，使用getEntity方法活得返回结果
			String result = EntityUtils.toString(httpResponse.getEntity());
			System.out.println("result:" + result);
			JSONArray jsonArray = new JSONArray("[" + result + "]");
			String message = "";
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if (jsonObject != null)
				{
					String errorCode = jsonObject.getString("errorCode");
					if (errorCode.equals("20"))
					{
						msg="要翻译的文本过长";
					}
					else if (errorCode.equals("30 "))
					{
						msg="无法进行有效的翻译";
					}
					else if (errorCode.equals("40"))
					{
						msg="不支持的语言类型";
					}
					else if (errorCode.equals("50"))
					{
						msg="无效的key";
					}
					else
					{
						// 要翻译的内容
						String query = jsonObject.getString("query");
						message = query;
						// 翻译内容
						String translation = jsonObject.getString("translation");
						message += "\t" + translation;
						// 有道词典-基本词典
						if (jsonObject.has("basic"))
						{
							JSONObject basic = jsonObject.getJSONObject("basic");
							if (basic.has("phonetic"))
							{
								String phonetic = basic.getString("phonetic");
								message += "\n\t" + phonetic;
							}
							if (basic.has("phonetic"))
							{
								String explains = basic.getString("explains");
								message += "\n\t" + explains;
							}
						}
						// 有道词典-网络释义
						if (jsonObject.has("web"))
						{
							String web = jsonObject.getString("web");
							JSONArray webString = new JSONArray("[" + web + "]");
							message += "\n网络释义：";
							JSONArray webArray = webString.getJSONArray(0);
							int count = 0;
							while(!webArray.isNull(count)){
								
								if (webArray.getJSONObject(count).has("key"))
								{
									String key = webArray.getJSONObject(count).getString("key");
									message += "\n\t<"+(count+1)+">" + key;
								}
								if (webArray.getJSONObject(count).has("value"))
								{
									String value = webArray.getJSONObject(count).getString("value");
									message += "\n\t   " + value;
								}
								count++;
							}
						}
					}
				}
			}
			msg=message;
		}
		else msg="提取失败";
		handler.obtainMessage(0, msg).sendToTarget();
	}
}
