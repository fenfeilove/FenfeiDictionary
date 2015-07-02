package cn.edu.zucc.Fenfei;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("NewApi")
public class HomeFragment extends Fragment {
	private EditText edit_search;
	private Button btn_search;
	private TextView text_word;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View messageLayout = inflater.inflate(R.layout.fragment_home,
				container, false);

		init(messageLayout);
		
		return messageLayout;
	}
	//ִ�н��յ�����Ϣ��ִ�е�˳���ǰ��ն��н��У����Ƚ��ȳ�
	@SuppressLint("HandlerLeak")
	Handler logHandler = new Handler() {
		public void handleMessage(Message msg){
			text_word.setText(((String)msg.obj).toString());
		}
	};
	public void init(View messageLayout) {
		this.edit_search = (EditText) messageLayout
				.findViewById(R.id.edit_search);
		this.edit_search.setOnClickListener(new OnclickListener(this.getActivity()));
		
		this.btn_search = (Button) messageLayout.findViewById(R.id.btn_search);
		this.btn_search.setOnClickListener(new OnclickListener(this.getActivity()));
		this.text_word=(TextView)messageLayout.findViewById(R.id.text_word);
		Thread load=new LoadDayly();
		load.start();
	}

	private class OnclickListener implements OnClickListener {
		@SuppressLint("NewApi")
		private Activity activity;
		public OnclickListener(Activity activity)
		{
			this.activity=activity;
		}
		@Override
		public void onClick(View v) {
			Intent it = new Intent();
			it.setClass(activity,SearchActivity.class);//����Class
			startActivity(it);
		}
	}
	public class LoadDayly extends Thread
    {
        private String url = "http://open.iciba.com/dsapi/";
        private String msg;
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
				JSONArray jsonArray = new JSONArray("[" + result + "]");
				//Log.d("len",""+jsonArray.length());
				String message = "\n\n    ";
				for (int i = 0; i < jsonArray.length(); i++)
				{
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					if (jsonObject != null)
					{
						String content = jsonObject.getString("content");
						String note=jsonObject.getString("note");
						message+=content+"\n\n    "+note+"\n";
					}
				}
				msg=message;
			}
			else
			{
				msg="    However mean your life is, meet it and live it;"
						+ " do not shun it and call it hard names.\n\n"
						+ "    ������������ô��⣬��Ҫ��ԣ�Ҫ�ú������Ҫ������������ö�����������\n";
			}
			logHandler.obtainMessage(0, msg).sendToTarget();
		}
    }
}
