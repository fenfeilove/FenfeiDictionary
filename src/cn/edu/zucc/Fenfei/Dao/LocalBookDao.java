package cn.edu.zucc.Fenfei.Dao;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import cn.edu.zucc.Fenfei.LogoActivity;
import cn.edu.zucc.Fenfei.Dao.impl.BookDao;
import cn.edu.zucc.Fenfei.bean.db_bookStatic;
import cn.edu.zucc.Fenfei.bean.db_bookStatic;
import cn.edu.zucc.Fenfei.bean.t_words;

public class LocalBookDao implements BookDao{
	private SQLiteDatabase database = LogoActivity.database;
	@Override
	public List<t_words> searchBybookid(int bookid) {
		List<t_words> wordlist=new ArrayList<t_words>();
		String sql="select book_english,book_chinese from db_bookwords where bookid="+bookid;
		Cursor cursor=database.rawQuery(sql, null);
		while(cursor.moveToNext())
		{
			t_words word=new t_words();
			word.setChinese(cursor.getString(1));
			word.setEnglish(cursor.getString(0));
			wordlist.add(word);
		}
		return wordlist;
	}
	@Override
	public List<db_bookStatic> db_bookSta() {
		// TODO Auto-generated method stub
		List<db_bookStatic> bookstalist=new ArrayList<db_bookStatic>();
		String sql = "select * from db_book";
		Cursor cursor=database.rawQuery(sql, null);
		while(cursor.moveToNext())
		{
			db_bookStatic tmp=new db_bookStatic();
			tmp.setBookid(cursor.getInt(0));
			tmp.setBookname(cursor.getString(1));
			bookstalist.add(tmp);
//			Log.d("db_book", cursor.getString(1));
		}
		for(int i=0;i<bookstalist.size();i++)
		{
			sql="select count(*) from db_bookwords where bookid="+bookstalist.get(i).getBookid();
			cursor=database.rawQuery(sql, null);
			if(cursor.moveToNext())
				bookstalist.get(i).setBookwordnum(cursor.getInt(0));
		}
		return bookstalist;
	}
}
