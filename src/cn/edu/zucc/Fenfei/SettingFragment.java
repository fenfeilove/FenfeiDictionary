package cn.edu.zucc.Fenfei;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SettingFragment extends Fragment {
	private LinearLayout layoutSet;
	private LinearLayout layoutNumber;
	private LinearLayout layoutIdea;
	private LinearLayout version;
	private LinearLayout about;
	private View setingLayout;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setingLayout = inflater.inflate(R.layout.fragment_setting,
				container, false);
		Init();
		return setingLayout;
	}
	public void Init()
	{
		layoutSet=(LinearLayout) setingLayout.findViewById(R.id.layoutSet);
		layoutNumber=(LinearLayout) setingLayout.findViewById(R.id.layoutNumber);
		layoutIdea=(LinearLayout) setingLayout.findViewById(R.id.layoutIdea);
		version=(LinearLayout) setingLayout.findViewById(R.id.version);
		about=(LinearLayout) setingLayout.findViewById(R.id.about);
		layoutSet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));  
			}
		});
		layoutNumber.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 
			}
		});
		//意见反馈
		layoutIdea.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		version.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(setingLayout.getContext().getApplicationContext(), "当前无更新版本", 1).show();
			
			}
		});
		about.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AboutDialog(setingLayout.getContext()).show();
			}
		});
	}
}
