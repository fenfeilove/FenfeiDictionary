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
		// ��һ��������HttpGet����
		HttpGet httpGet = new HttpGet(url);
		// �ڶ�����ʹ��execute��������HTTP GET���󣬲�����HttpResponse����
		HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
		if (httpResponse.getStatusLine().getStatusCode() == 200)
		{
			// ��������ʹ��getEntity������÷��ؽ��
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
						msg="Ҫ������ı�����";
					}
					else if (errorCode.equals("30 "))
					{
						msg="�޷�������Ч�ķ���";
					}
					else if (errorCode.equals("40"))
					{
						msg="��֧�ֵ���������";
					}
					else if (errorCode.equals("50"))
					{
						msg="��Ч��key";
					}
					else
					{
						// Ҫ���������
						String query = jsonObject.getString("query");
						message = query;
						// ��������
						String translation = jsonObject.getString("translation");
						message += "\t" + translation;
						// �е��ʵ�-�����ʵ�
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
						// �е��ʵ�-��������
						if (jsonObject.has("web"))
						{
							String web = jsonObject.getString("web");
							JSONArray webString = new JSONArray("[" + web + "]");
							message += "\n�������壺";
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
		else msg="��ȡʧ��";
		handler.obtainMessage(0, msg).sendToTarget();
	}
}
