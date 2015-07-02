package cn.edu.zucc.Fenfei;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.zucc.Fenfei.Dao.LocalNoteDao;
import cn.edu.zucc.Fenfei.Dao.LocalWordDao;
import cn.edu.zucc.Fenfei.Dao.impl.NoteDao;
import cn.edu.zucc.Fenfei.Dao.impl.WordDao;
import cn.edu.zucc.Fenfei.bean.TransWord;
import cn.edu.zucc.Fenfei.bean.WebData;
import cn.edu.zucc.Fenfei.bean.db_note;
import cn.edu.zucc.Fenfei.service.GetJsonService;

public class SearchActivity extends Activity {
	private AutoCompleteTextView actv;
	private Button btn_search;
	private TextView localtrans;
	private TextView simpletrans;
	private TextView basictrans;
	private TextView webtrans;
	private String[] strAutoArray = null;
	private String[] notenamelist = null;
	private List<db_note> notelist = null;
	private int NoteChosed = 1;
	private String queryword="";
	private String localmean="";
	private TransWord transword;
	// private TextView text_word;
	// private TextView text_pron;
	// private Button btn_aduio;
	private Button btn_add;

	// private TextView text_def;

	// private String YouDaoBaseUrl = "http://fanyi.youdao.com/openapi.do";
	// private String YouDaoKeyFrom = "fenfeilove";
	// private String YouDaoKey = "1024697229";
	// private String YouDaoType = "data";
	// private String YouDaoDoctype = "json";
	// private String YouDaoVersion = "1.1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_wordsearch);// 设置布局资源
		Init();
	}

	private void Init() {
		this.actv = (AutoCompleteTextView) this.findViewById(R.id.edit_search);
		this.actv.addTextChangedListener(new actvlistener());

		this.btn_search = (Button) this.findViewById(R.id.btn_search);
		this.btn_search.setOnClickListener(new searchListener());

		this.localtrans = (TextView) this.findViewById(R.id.localtrans);
		this.simpletrans = (TextView) this.findViewById(R.id.simpletrans);
		this.basictrans = (TextView) this.findViewById(R.id.basictrans);
		this.webtrans = (TextView) this.findViewById(R.id.webtrans);

		Thread kst = new keySearchThread("");
		kst.start();
		Thread ant = new AllNoteThread();
		ant.start();
		// this.text_word=(TextView) this.findViewById(R.id.text_word);
		// this.text_pron=(TextView) this.findViewById(R.id.text_pron);
		// this.btn_aduio=(Button) this.findViewById(R.id.btn_aduio);
		this.btn_add = (Button) this.findViewById(R.id.btn_add);
		// this.text_def=(TextView) this.findViewById(R.id.text_def);
		this.btn_add.setOnClickListener(new addnoteListener());
	}

	// 执行接收到的消息，执行的顺序是按照队列进行，即先进先出
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				localmean=msg.obj.toString();
				localtrans.setText(localmean + "\n");
				if(!localmean.equals("无本地释义"))
					btn_add.setVisibility(1);
				break;
			case 1:
				transword = (TransWord) msg.obj;
				simpletrans.setText(transword.getSimpletrans() + "\n");
				basictrans.setText(transword.getBasictrans());
				if(transword.getBasictrans()!=null&&!transword.getBasictrans().equals(""))
					btn_add.setVisibility(1);
				List<WebData> listwd = transword.getWebtrans();
				if (listwd == null)
					break;
				StringBuilder sb = new StringBuilder();
				for (WebData wd : listwd) {
					sb.append(wd.getKey() + ":" + wd.getValue() + "\n");
				}
				webtrans.setText(sb.toString());
				break;
			case 3:
				List<String> searchlist = (List<String>) msg.obj;
				//Log.d("check", "success" + searchlist.size());
				strAutoArray = new String[searchlist.size()];
				for (int i = 0; i < searchlist.size(); i++)
					strAutoArray[i] = searchlist.get(i);
				//Log.d("check2", "success");
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						SearchActivity.this,
						android.R.layout.simple_dropdown_item_1line,
						strAutoArray);
				actv.setAdapter(adapter);
				actv.setThreshold(1);
				break;
			case 4:
				
				notelist = (List<db_note>) msg.obj;
				//Log.d("check", "success" + notelist.size());
				notenamelist = new String[notelist.size()];
				for (int i = 0; i < notelist.size(); i++)
					notenamelist[i] = notelist.get(i).getNotename();
				break;
			case 5:
				int chose = (int) msg.obj;
				NoteChosed = notelist.get(chose).getNoteid();
				break;
			case 6:
				//Log.d("chose","finalchose"+NoteChosed);
				if(!localmean.equals("无本地释义"))
				{
					Thread anwt=new AddNoteWordThread(queryword,localmean,NoteChosed);
					anwt.start();
				}else if(transword.getBasictrans()!=null&&transword.getBasictrans().equals(""))
				{
					Thread anwt=new AddNoteWordThread(transword.getQueryword(),transword.getBasictrans(),NoteChosed);
					anwt.start();
				}
				break;
			case 7:
				String result=(String)msg.obj;
				NoteChosed=1;
				Toast.makeText(SearchActivity.this, result, Toast.LENGTH_SHORT)
				.show();
				break;
			default:
				break;
			}
		}
	};

	private class actvlistener implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			// Log.d("str",s.toString());

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

			// ArrayAdapter<String> adapter = new
			// ArrayAdapter<String>(SearchActivity.this,android.R.layout.simple_dropdown_item_1line,
			// strAutoArray);
			// actv.setAdapter(adapter);
			// actv.setThreshold(1);
		}

	}

	private class searchListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			queryword = actv.getText().toString().trim();
			if (queryword == null || "".equals(queryword))
				return;
			Thread wt = new WebThread(queryword);
			wt.start();
			Thread lt = new LocalThread(queryword);
			lt.start();
		}
	}

	public class WebThread extends Thread {
		private String content;

		public WebThread(String content) {
			this.content = content;
		}

		public void run() {
			TransWord tw = null;
			try {
				tw = GetJsonService.parseJson(content);
			} catch (Exception e) {
				e.printStackTrace();
			}
			handler.obtainMessage(1, tw).sendToTarget();
		}
	}

	public class LocalThread extends Thread {
		private String localtrans;
		private String content;

		public LocalThread(String content) {
			this.content = content;
		}

		public void run() {
			WordDao wd = new LocalWordDao();
			localtrans = wd.wordsearch(content);
			handler.obtainMessage(0, localtrans).sendToTarget();
		}
	}

	public class keySearchThread extends Thread {
		private List<String> searchlist;
		private String key;

		public keySearchThread(String key) {
			this.key = key;
		}

		public void run() {
			WordDao wd = new LocalWordDao();
			searchlist = wd.keysearch(key);
			handler.obtainMessage(3, searchlist).sendToTarget();
		}
	}

	public class AllNoteThread extends Thread {
		private List<db_note> notelist;

		public void run() {
			NoteDao nd = new LocalNoteDao();

			notelist = nd.db_noteAllnote();
			handler.obtainMessage(4, notelist).sendToTarget();
		}
	}

	private class addnoteListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					v.getContext());
			builder.setTitle("选择生词本");
			builder.setCancelable(false);
			builder.setSingleChoiceItems(notenamelist, 0,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// 按钮事件
							// Log.d("chose", which+"");
							handler.obtainMessage(5, which).sendToTarget();
						}
					});
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							handler.obtainMessage(6, null).sendToTarget();
						}
					});
			builder.setNegativeButton("返回",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							dialog.cancel();
						}
					});
			builder.show();
		}
	}
	public class AddNoteWordThread extends Thread {
		private String note_english,note_chinese;
		private int noteid;
		public AddNoteWordThread(String note_english,String note_chinese,int noteid)
		{
			this.note_english=note_english;
			this.note_chinese=note_chinese;
			this.noteid=noteid;
		}
		public void run()
		{
			NoteDao nd=new LocalNoteDao();
			String result = nd.db_notewordAdd(note_english, note_chinese, noteid);
			handler.obtainMessage(7,result).sendToTarget();
		}
	}
}
