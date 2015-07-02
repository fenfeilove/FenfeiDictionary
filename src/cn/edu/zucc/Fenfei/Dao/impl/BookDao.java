package cn.edu.zucc.Fenfei.Dao.impl;

import java.util.List;

import cn.edu.zucc.Fenfei.bean.db_bookStatic;
import cn.edu.zucc.Fenfei.bean.t_words;

public interface BookDao {

	List<t_words> searchBybookid(int bookid);

	List<db_bookStatic> db_bookSta();
}
