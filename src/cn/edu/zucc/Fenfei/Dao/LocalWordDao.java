package cn.edu.zucc.Fenfei.Dao;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.edu.zucc.Fenfei.LogoActivity;
import cn.edu.zucc.Fenfei.Dao.impl.WordDao;
import cn.edu.zucc.Fenfei.bean.t_words;

public class LocalWordDao implements WordDao{
	private SQLiteDatabase database=LogoActivity.database;
	@Override
	public String wordsearch(String content) {
		// TODO Auto-generated method stub
		String sql = "select chinese from t_words where english = '"+content+"'";
		Cursor cursor=  database.rawQuery(sql, null);
		//Log.d("localsearch", "success");
		if(cursor.moveToNext())
			return cursor.getString(0);
		else
			 return "无本地释义";
	}
	@Override
	public void wordinsert(t_words tw)
	{
		String sql = "insert into t_words values("+tw.getEnglish()+","+tw.getChinese()+")";
		database.execSQL(sql);
//		Log.d("localinsert","success");
	}
	@Override
	public List<String> keysearch(String keyword) {
		// TODO Auto-generated method stub
		String sql = "select english from t_words where english like '%"+keyword+"%'";
		Cursor cursor=  database.rawQuery(sql, null);
		
		List<String> searchlist=new ArrayList<String>();
		while(cursor.moveToNext())
		{
			String word=cursor.getString(0);
			searchlist.add(word);
		}
		return searchlist;
	}
	@Override
	public List<t_words> searchBynoteid(int noteid) {
		// TODO Auto-generated method stub
		List<t_words> wordlist=new ArrayList<t_words>();
		String sql="select note_english ,note_chinese from db_notewords where noteid="+noteid;
		Cursor cursor=  database.rawQuery(sql, null);
		while(cursor.moveToNext())
		{
			t_words word=new t_words();
			word.setEnglish(cursor.getString(0));
			word.setChinese(cursor.getString(1));
			wordlist.add(word);
		}
		return wordlist;
	}
	@Override
	public List<t_words> LoadAllWords() {
		// TODO Auto-generated method stub
		List<t_words> wordlist=new ArrayList<t_words>();
		String sql="select * from t_words";
		Cursor cursor=  database.rawQuery(sql, null);
		while(cursor.moveToNext())
		{
			t_words word=new t_words();
			word.setEnglish(cursor.getString(0));
			word.setChinese(cursor.getString(1));
			wordlist.add(word);
		}
		return wordlist;
	}
}
