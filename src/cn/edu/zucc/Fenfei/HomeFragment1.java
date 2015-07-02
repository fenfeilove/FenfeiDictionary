package cn.edu.zucc.Fenfei;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

public class HomeFragment1 extends Fragment {
	private AutoCompleteTextView edit_search;
	private Button btn_search;
	private TextView text_word;
	private TextView text_pron;
	private Button btn_aduio;
	private Button btn_add;
	private String[] strAutoArray = null;

	private String YouDaoBaseUrl = "http://fanyi.youdao.com/openapi.do";
	private String YouDaoKeyFrom = "fenfeilove";
	private String YouDaoKey = "1024697229";
	private String YouDaoType = "data";
	private String YouDaoDoctype = "json";
	private String YouDaoVersion = "1.1";
	
	//执行接收到的消息，执行的顺序是按照队列进行，即先进先出
	Handler logHandler = new Handler() {
		public void handleMessage(Message msg) {
			text_word.setText(((String)msg.obj).toString());
		}
	};
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View messageLayout = inflater.inflate(R.layout.fragment_home,
				container, false);

		init(messageLayout);

		return messageLayout;
	}

	public void init(View messageLayout) {
		this.edit_search = (AutoCompleteTextView) messageLayout
				.findViewById(R.id.edit_search);
		//this.edit_search.setOnClickListener(new changeListener());
		
		this.btn_search = (Button) messageLayout.findViewById(R.id.btn_search);
		this.btn_search.setOnClickListener(new searchListener());
		this.text_word = (TextView) messageLayout.findViewById(R.id.text_word);
		this.text_pron = (TextView) messageLayout.findViewById(R.id.text_pron);
		this.btn_aduio = (Button) messageLayout.findViewById(R.id.btn_aduio);
		this.btn_add = (Button) messageLayout.findViewById(R.id.btn_add);

	}

	private class searchListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			String YouDaoSearchContent = edit_search.getText().toString().trim();
			String YouDaoUrl = YouDaoBaseUrl + "?keyfrom=" + YouDaoKeyFrom
					+ "&key=" + YouDaoKey + "&type=" + YouDaoType + "&doctype="
					+ YouDaoDoctype + "&type=" + YouDaoType + "&version="
					+ YouDaoVersion + "&q=" + YouDaoSearchContent;
			Thread myt = new MyThread(YouDaoUrl);
			myt.start();
		}
	}
	public class MyThread extends Thread{
		private String url;
		private String msg;
		public MyThread(String url)
		{
			this.url=url;
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
			logHandler.obtainMessage(0, msg).sendToTarget();
		}
	}
}
