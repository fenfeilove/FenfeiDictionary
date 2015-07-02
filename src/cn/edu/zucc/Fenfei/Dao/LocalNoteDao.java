package cn.edu.zucc.Fenfei.Dao;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import cn.edu.zucc.Fenfei.LogoActivity;
import cn.edu.zucc.Fenfei.Dao.impl.NoteDao;
import cn.edu.zucc.Fenfei.bean.db_note;
import cn.edu.zucc.Fenfei.bean.db_noteStatic;

public class LocalNoteDao implements NoteDao {
	private SQLiteDatabase database=LogoActivity.database;
	@Override
	public void db_noteAdd(String notename) {
		// TODO Auto-generated method stub
		int maxid=0;
		String sql="SELECT max(noteid) FROM db_note";
		Cursor cursor=database.rawQuery(sql,null);
		if(cursor.moveToNext())
			maxid=cursor.getInt(0);
		maxid+=1;
		sql="insert into db_note values("+maxid+",'"+notename+"')";
		database.execSQL(sql);
	}
	public void db_noteDel(int noteid)
	{
		String sql="delete from db_note where noteid="+noteid;
		database.execSQL(sql);
	}
	public List<db_note> db_noteAllnote()
	{
		String sql="select * from db_note";
		Cursor cursor=database.rawQuery(sql, null);
		List<db_note> notelist=new ArrayList<db_note>();
		while(cursor.moveToNext())
		{
			db_note note=new db_note();
			note.setNoteid(cursor.getInt(0));
			note.setNotename(cursor.getString(1));
			//Log.d("noteList", cursor.getInt(0)+"[]"+cursor.getString(1));
			notelist.add(note);
		}
		return notelist;
	}
	@Override
	public String db_notewordAdd(String note_english, String note_chinese,
			int noteid) {
		// TODO Auto-generated method stub
		String sql="select * from db_notewords where noteid="+noteid
				+" and note_english='"+note_english
				+"' and note_chinese='"+note_chinese+"'";
		Cursor cursor =database.rawQuery(sql, null);
		if(cursor.moveToNext()) return "word exists";
		
		int maxid=0;
		sql="select max(id) from db_notewords";
		cursor=database.rawQuery(sql,null);
		if(cursor.moveToNext())
			maxid=cursor.getInt(0);
		maxid+=1;
		sql="insert into db_notewords values("+maxid+","+noteid+",'"+note_english+"','"+note_chinese+"')";
		database.execSQL(sql);
		return "add success";
	}
	@Override
	public List<db_noteStatic> db_noteSta() {
		// TODO Auto-generated method stub
		List<db_noteStatic> notestalist=new ArrayList<db_noteStatic>();
		String sql = "select * from db_note";
		Cursor cursor=database.rawQuery(sql, null);
		while(cursor.moveToNext())
		{
			db_noteStatic tmp=new db_noteStatic();
			tmp.setNoteid(cursor.getInt(0));
			tmp.setNotename(cursor.getString(1));
			notestalist.add(tmp);
//			Log.d("db_note", cursor.getString(1));
		}
		for(int i=0;i<notestalist.size();i++)
		{
			sql="select count(id) from db_notewords where noteid="+notestalist.get(i).getNoteid();
			cursor=database.rawQuery(sql, null);
			if(cursor.moveToNext())
				notestalist.get(i).setNotewordnum(cursor.getInt(0));
		}
		return notestalist;
	}
}
