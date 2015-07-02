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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import cn.edu.zucc.Fenfei.Dao.LocalBookDao;
import cn.edu.zucc.Fenfei.Dao.impl.BookDao;
import cn.edu.zucc.Fenfei.bean.t_words;

public class BookwordActivity extends Activity {
	private ListView bookwordlist;
	private List<t_words> wordlist;
	private int bookid = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookword);
		Intent intent = getIntent();
		bookid = intent.getIntExtra("bookid", 1);
		
		Init();
	}

	public void Init() {
		this.bookwordlist = (ListView) this.findViewById(R.id.bookwordListview);
		Thread bt = new booksThread();
		bt.start();
	}

	public void showbookWord() {
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < wordlist.size(); i++) {
			// Log.d("word",
			// wordlist.get(i).getEnglish()+"[]"+wordlist.get(i).getChinese());
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("bookword_english", wordlist.get(i).getEnglish());
			map.put("bookword_chinese", wordlist.get(i).getChinese());
			listItem.add(map);
		}

		SimpleAdapter mSchedule = new SimpleAdapter(this, // 没什么解释
				listItem,// 数据来源
				R.layout.bookwordlist_items,// ListItem的XML实现

				// 动态数组与ListItem对应的子项
				new String[] { "bookword_english", "bookword_chinese" },

				// ListItem的XML文件里面的两个TextView ID
				new int[] { R.id.bookword_english, R.id.bookword_chinese });
		bookwordlist.setAdapter(mSchedule);
	}

	// 执行接收到的消息，执行的顺序是按照队列进行，即先进先出
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 10:
				wordlist = (List<t_words>) msg.obj;
				showbookWord();
				break;
			default:
				break;
			}
		}
	};

	public class booksThread extends Thread {
		List<t_words> wordlist;

		public void run() {
			BookDao bd = new LocalBookDao();
			wordlist = bd.searchBybookid(bookid);
			// Log.d("cnt", "success "+bookstalist.size());
			handler.obtainMessage(10, wordlist).sendToTarget();
		}
	}
}
