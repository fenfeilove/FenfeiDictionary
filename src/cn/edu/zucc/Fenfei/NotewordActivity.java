package cn.edu.zucc.Fenfei;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import cn.edu.zucc.Fenfei.Dao.LocalWordDao;
import cn.edu.zucc.Fenfei.Dao.impl.WordDao;
import cn.edu.zucc.Fenfei.bean.t_words;

public class NotewordActivity extends Activity {
	private ListView notewordlist;
	private List<t_words> wordlist;
	private int noteid = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_noteword);
		Intent intent = getIntent();
		noteid = intent.getIntExtra("noteid", 1);
//		Log.d("note", "" + noteid);
		Init();
	}

	public void Init() {
		this.notewordlist = (ListView) this.findViewById(R.id.notewordListview);
		Thread nt = new notesThread();
		nt.start();
	}
	public void showNoteWord()
	{
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < wordlist.size(); i++) {
			//Log.d("word", wordlist.get(i).getEnglish()+"[]"+wordlist.get(i).getChinese());
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("noteword_english", wordlist.get(i).getEnglish());
			map.put("noteword_chinese", wordlist.get(i).getChinese());
			listItem.add(map);
		}

		SimpleAdapter mSchedule = new SimpleAdapter(this, //没什么解释  
                listItem,//数据来源   
                R.layout.notewordlist_items,//ListItem的XML实现  
                  
                //动态数组与ListItem对应的子项          
                new String[] {"noteword_english", "noteword_chinese"},   
                  
                //ListItem的XML文件里面的两个TextView ID  
                new int[] {R.id.noteword_english,R.id.noteword_chinese});
		notewordlist.setAdapter(mSchedule);
		// 添加点击
		notewordlist.setOnItemClickListener(new ItemClick());
	}
	// 执行接收到的消息，执行的顺序是按照队列进行，即先进先出
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 8:
				wordlist = (List<t_words>) msg.obj;
				showNoteWord();
				break;
			default:
				break;
			}
		}
	};

	public class ItemClick implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
//			Log.d("notelist", "点击第" + position + "个项目");
		}
	}

	public class notesThread extends Thread {
		List<t_words> wordlist;

		public void run() {
			WordDao wd = new LocalWordDao();
			wordlist = wd.searchBynoteid(noteid);
			// Log.d("cnt", "success "+notestalist.size());
			handler.obtainMessage(8, wordlist).sendToTarget();
		}
	}
}
