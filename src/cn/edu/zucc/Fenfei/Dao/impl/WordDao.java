package cn.edu.zucc.Fenfei.Dao.impl;

import java.util.List;

import cn.edu.zucc.Fenfei.bean.TransWord;
import cn.edu.zucc.Fenfei.bean.t_words;

public interface WordDao {
	public String wordsearch(String content);
	public void wordinsert(t_words tw);
	public List<String> keysearch(String keyword);
	public List<t_words> searchBynoteid(int noteid);
	public List<t_words> LoadAllWords();
}
