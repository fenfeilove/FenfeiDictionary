package cn.edu.zucc.Fenfei;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;

public class AboutDialog extends AlertDialog {  
    @SuppressWarnings("deprecation")
	public AboutDialog(Context context) {  
        super(context);  
        final View view = getLayoutInflater().inflate(R.layout.activity_about,null);
        setButton(context.getText(R.string.close), (OnClickListener) null);  
        //setIcon(R.drawable.icon_about);
        setTitle("·Ü·É´Êµä   v1.0.0" );
        setView(view);
    }
} 
