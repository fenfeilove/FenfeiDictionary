package cn.edu.zucc.Fenfei.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import cn.edu.zucc.Fenfei.R;
import cn.edu.zucc.Fenfei.impl.OpenDatabase;

public class OpenSdDatabase implements OpenDatabase {

	public final String DATABASE_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+ "/dictionary";
	public final String DATABASE_FILENAME = "dictionary.db";
	private Context ctx = null;
	public OpenSdDatabase(Context ctx) {
		// TODO 自动生成的构造函数存根
		this.ctx=ctx;
	}

	@Override
	public SQLiteDatabase Open() {
		// TODO 自动生成的方法存根
		try
		{
			String databaseFilename =  this.DATABASE_PATH+ "/" + DATABASE_FILENAME;
			File dir = new File(this.DATABASE_PATH);
			if (!dir.exists())
				dir.mkdir();
			if (!(new File(databaseFilename)).exists())
			{				
				InputStream is = this.ctx.getResources().openRawResource(R.raw.dictionary);
				FileOutputStream fos = new FileOutputStream(databaseFilename);
				byte[] buffer = new byte[8192];
				int count = 0;
				while ((count = is.read(buffer)) > 0)
				{
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}
			SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
			return database;
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
