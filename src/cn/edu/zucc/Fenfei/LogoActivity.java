package cn.edu.zucc.Fenfei;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import cn.edu.zucc.Fenfei.Dao.LocalBookDao;
import cn.edu.zucc.Fenfei.Dao.LocalWordDao;
import cn.edu.zucc.Fenfei.Dao.impl.WordDao;
import cn.edu.zucc.Fenfei.bean.t_words;
import cn.edu.zucc.Fenfei.impl.OpenDatabase;
import cn.edu.zucc.Fenfei.util.OpenPhoneDatabase;

public class LogoActivity extends Activity {

	public static SQLiteDatabase database = null;
	private Context ctx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		final Window win = getWindow();// 返回当前Activity的Window对象,Window类中概括了Android窗口的基本属性和基本功能

		// 隐藏状态栏
		win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题栏
		this.setContentView(R.layout.activity_logo);// 设置布局资源

		ctx = LogoActivity.this;
		new OpenDatabaseThread().start();
//		new AddThread().start();
	}

	// 执行接收到的消息，执行的顺序是按照队列进行，即先进先出
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				main_load();
				break;
			default:
				break;
			}

		}
	};

	public void main_load() {
		Intent it = new Intent();// 实例化Intent
		it.setClass(LogoActivity.this, MainActivity.class);// 设置Class
		// it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(it);// 启动Activity
		this.finish();
	}

	public class OpenDatabaseThread extends Thread {
		public void run() {
			OpenDatabase odb = new OpenPhoneDatabase(ctx);
			database = odb.Open();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handler.obtainMessage(1, null).sendToTarget();
		}
	}
	
//	public class AddThread extends Thread {
//		
//		public void run()
//		{
//			WordDao wd=new LocalWordDao();
//			List<t_words> wordlist = wd.LoadAllWords();
//			LocalBookDao lbd=new LocalBookDao();
//			lbd.insertinto(1, wordlist);
//		}
//	}
}
