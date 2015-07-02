package cn.edu.zucc.Fenfei.Dao.impl;

import java.util.List;

import cn.edu.zucc.Fenfei.bean.db_note;
import cn.edu.zucc.Fenfei.bean.db_noteStatic;

public interface NoteDao {
	public void db_noteAdd(String notename);
	public void db_noteDel(int noteid);
	public List<db_note> db_noteAllnote();
	public String db_notewordAdd(String note_english,String note_chinese,int noteid);
	public List<db_noteStatic> db_noteSta();
}
