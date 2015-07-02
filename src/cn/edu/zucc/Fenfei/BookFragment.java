package cn.edu.zucc.Fenfei;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import cn.edu.zucc.Fenfei.Dao.LocalBookDao;
import cn.edu.zucc.Fenfei.Dao.impl.BookDao;
import cn.edu.zucc.Fenfei.bean.db_bookStatic;

public class BookFragment extends Fragment {
	
	private ListView booklistview;
	private List<db_bookStatic> bookstalist;
	ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
	private View BookLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		BookLayout = inflater
				.inflate(R.layout.fragment_book, container, false);
		Init(BookLayout);
		return BookLayout;
	}

	public void Init(View v) {
		booklistview = (ListView) v.findViewById(R.id.bookListview);
		Thread nt=new booksThread();
		nt.start();
	}

	// 执行接收到的消息，执行的顺序是按照队列进行，即先进先出
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 9:
				bookstalist = (List<db_bookStatic>) msg.obj;

				for (int i = 0; i < bookstalist.size(); i++) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("bookname", bookstalist.get(i).getBookname());
					map.put("bookwordnum", "共有"
							+ bookstalist.get(i).getBookwordnum() + "单词");
					listItem.add(map);
				}
				// 生成适配器的Item和动态数组对应的元素
				SimpleAdapter listItemAdapter = new SimpleAdapter(
						BookLayout.getContext(), listItem, R.layout.list_items,
						new String[] { "bookname", "bookwordnum" }, new int[] {
								R.id.notename, R.id.notewordnum });
				//Log.d("listitem", listItemAdapter.toString());
				booklistview.setAdapter(listItemAdapter);
				// 添加点击
				booklistview.setOnItemClickListener(new ItemClick());
				break;
			default:
				break;
			}
		}
	};


	public class booksThread extends Thread {
		List<db_bookStatic> bookstalist;
		public void run() {
			BookDao bk = new LocalBookDao();
			bookstalist = bk.db_bookSta();
			//Log.d("cnt", "success "+bookstalist.size());
			handler.obtainMessage(9, bookstalist).sendToTarget();
		}
	}
	public class ItemClick implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
//			Log.d("booklist", "点击第"+position+"个项目");
			Intent intent=new Intent();
			intent.putExtra("bookid",bookstalist.get(position).getBookid());
			intent.setClass(booklistview.getContext(), BookwordActivity.class);
			startActivity(intent);
		}
	}
	@Override
    public boolean onContextItemSelected(MenuItem item) {
//        Log.d("menu","点击了长按菜单里面的第"+item.getItemId()+"个项目");
        return super.onContextItemSelected(item);
    }
}
