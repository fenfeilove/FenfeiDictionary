package cn.edu.zucc.Fenfei;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;


public class MainActivity extends Activity implements OnClickListener {


	private HomeFragment homeFragment;
	private TranslateFragment translateFragment;
	private NotesFragment notesFragment;
	private BookFragment bookFragment;
	private SettingFragment settingFragment;
	
	private View homeLayout;
	private View translateLayout;
	private View notesLayout;
	private View bookLayout;
	private View settingLayout;

//	private ImageView homeImage;
//	private ImageView translateImage;
//	private ImageView notesImage;
//	private ImageView bookImage;
//	private ImageView settingImage;

	private TextView homeText;
	private TextView translateText;
	private TextView notesText;
	private TextView bookText;
	private TextView settingText;

	private FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		initViews();
		fragmentManager = getFragmentManager();
		
		setTabSelection(0);
	}

	private void initViews() {
		homeLayout = findViewById(R.id.search_layout);
		translateLayout = findViewById(R.id.translate_layout);
		notesLayout = findViewById(R.id.strange_layout);
		bookLayout = findViewById(R.id.book_layout);
		settingLayout = findViewById(R.id.setting_layout);
		
//		homeImage = (ImageView) findViewById(R.id.search_image);
//		translateImage = (ImageView) findViewById(R.id.translate_image);
//		notesImage = (ImageView) findViewById(R.id.translate_image);
//		bookImage = (ImageView) findViewById(R.id.book_image);
//		settingImage = (ImageView) findViewById(R.id.setting_image);
		
		homeText = (TextView) findViewById(R.id.search_text);
		translateText = (TextView) findViewById(R.id.translate_text);
		notesText = (TextView) findViewById(R.id.notes_text);
		bookText = (TextView) findViewById(R.id.book_text);
		settingText = (TextView) findViewById(R.id.setting_text);
		
		homeLayout.setOnClickListener(this);
		translateLayout.setOnClickListener(this);
		notesLayout.setOnClickListener(this);
		bookLayout.setOnClickListener(this);
		settingLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_layout:
			setTabSelection(0);
			break;
		case R.id.translate_layout:
			setTabSelection(1);
			break;
		case R.id.strange_layout:
			setTabSelection(2);
			break;
		case R.id.book_layout:
			setTabSelection(3);
			break;
		case R.id.setting_layout:
			setTabSelection(4);
			break;
		default:
			break;
		}
	}

	private void setTabSelection(int index) {
		clearSelection();
		
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		
		hideFragments(transaction);
		switch (index) {
		case 0:
			homeText.setTextColor(Color.WHITE);
			if (homeFragment == null) {
				
				homeFragment = new HomeFragment();
				transaction.add(R.id.mian, homeFragment);
			} else {
				
				transaction.show(homeFragment);
			}
			break;
		case 1:
			
			translateText.setTextColor(Color.WHITE);
			if (translateFragment == null) {
				
				translateFragment = new TranslateFragment();
				transaction.add(R.id.mian, translateFragment);
			} else {
				
				transaction.show(translateFragment);
			}
			break;
		case 2:
			
			notesText.setTextColor(Color.WHITE);
			if (notesFragment == null) {
			
				notesFragment = new NotesFragment();
				transaction.add(R.id.mian, notesFragment);
			} else {
			
				transaction.show(notesFragment);
			}
			break;
		case 3:
			
			bookText.setTextColor(Color.WHITE);
			if (bookFragment == null) {
			
				bookFragment = new BookFragment();
				transaction.add(R.id.mian, bookFragment);
			} else {
			
				transaction.show(bookFragment);
			}
			break;
		default:
			
			settingText.setTextColor(Color.WHITE);
			if (settingFragment == null) {
				
				settingFragment = new SettingFragment();
				transaction.add(R.id.mian, settingFragment);
			} else {
				
				transaction.show(settingFragment);
			}
			break;
		}
		transaction.commit();
	}

	
	private void clearSelection() {
		
		homeText.setTextColor(Color.parseColor("#82858b"));
		
		translateText.setTextColor(Color.parseColor("#82858b"));
		
		notesText.setTextColor(Color.parseColor("#82858b"));
		
		bookText.setTextColor(Color.parseColor("#82858b"));
		
		settingText.setTextColor(Color.parseColor("#82858b"));
	}

	private void hideFragments(FragmentTransaction transaction) {
		if (homeFragment != null) {
			transaction.hide(homeFragment);
		}
		if (translateFragment != null) {
			transaction.hide(translateFragment);
		}
		if (notesFragment != null) {
			transaction.hide(notesFragment);
		}
		if (bookFragment != null)
		{
			transaction.hide(bookFragment);
		}
		if (settingFragment != null) {
			transaction.hide(settingFragment);
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {
	
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MainActivity.this);
			//builder.setIcon(R.drawable.bee);
			builder.setTitle("你确定退出吗？");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							MainActivity.this.finish();
							android.os.Process.killProcess(android.os.Process
									.myPid());
							android.os.Process.killProcess(android.os.Process
									.myTid());
							android.os.Process.killProcess(android.os.Process
									.myUid());
						}
					});
			builder.setNegativeButton("返回",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							dialog.cancel();
						}
					});
			builder.show();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO 自动生成的方法存根
		if(item.getItemId()==R.id.exit)
		{
			android.os.Process.killProcess(android.os.Process.myPid());
		}
		else if(item.getItemId()==R.id.about)
		{
			new AboutDialog(this).show();
		}
		else if(item.getItemId()==R.id.shore)
		{
			Intent intent=new Intent(Intent.ACTION_SEND); 
			intent.setType("image/*"); 
			intent.putExtra(Intent.EXTRA_SUBJECT, "分享"); 
			intent.putExtra(Intent.EXTRA_TEXT, "终于可以了!!!");  
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			startActivity(Intent.createChooser(intent, getTitle())); 
		}
		return super.onMenuItemSelected(featureId, item);
	}
}
