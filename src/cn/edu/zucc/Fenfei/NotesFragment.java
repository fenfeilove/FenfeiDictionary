package cn.edu.zucc.Fenfei;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import cn.edu.zucc.Fenfei.Dao.LocalNoteDao;
import cn.edu.zucc.Fenfei.Dao.impl.NoteDao;
import cn.edu.zucc.Fenfei.bean.db_noteStatic;

public class NotesFragment extends Fragment {
	private ImageButton btn_add;
	private ListView notelistview;
	private List<db_noteStatic> notestalist;
	ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
	private View NoteLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		NoteLayout = inflater
				.inflate(R.layout.fragment_notes, container, false);
		Init(NoteLayout);
		return NoteLayout;
	}

	public void Init(View v) {
		btn_add = (ImageButton) v.findViewById(R.id.btn_add);
		btn_add.setOnClickListener(new AddListener());
		notelistview = (ListView) v.findViewById(R.id.noteListview);
		Thread nt=new notesThread();
		nt.start();
	}

	// ִ�н��յ�����Ϣ��ִ�е�˳���ǰ��ն��н��У����Ƚ��ȳ�
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 8:
				notestalist = (List<db_noteStatic>) msg.obj;

				for (int i = 0; i < notestalist.size(); i++) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("notename", notestalist.get(i).getNotename());
					map.put("notewordnum", "����"
							+ notestalist.get(i).getNotewordnum() + "����");
					listItem.add(map);
				}
				// ������������Item�Ͷ�̬�����Ӧ��Ԫ��
				SimpleAdapter listItemAdapter = new SimpleAdapter(
						NoteLayout.getContext(), listItem, R.layout.list_items,
						new String[] { "notename", "notewordnum" }, new int[] {
								R.id.notename, R.id.notewordnum });
				//Log.d("listitem", listItemAdapter.toString());
				notelistview.setAdapter(listItemAdapter);
				// ��ӵ��
				notelistview.setOnItemClickListener(new ItemClick());
				notelistview.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {  
		              
		            @Override  
		            public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
		                menu.setHeaderTitle("ɾ�����ʱ���");     
		                menu.add(0, 0, 0, "ɾ��");
		            }  
		        });
				break;
			default:
				break;
			}
		}
	};

	public class AddListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// Toast.makeText(v.getContext(), "success",
			// Toast.LENGTH_SHORT).show();

			LayoutInflater inflater = LayoutInflater.from(v.getContext());
			View layout = inflater.inflate(R.layout.activity_addnote, null);
			AlertDialog.Builder builder = new AlertDialog.Builder(
					v.getContext());
			builder.setView(layout).setTitle("������ʱ�").setCancelable(true)
					.create();
			builder.show();
			EditText notename = (EditText) layout.findViewById(R.id.notename);

			Button btn_ok = (Button) layout.findViewById(R.id.btn_ok);
			Button btn_cancel = (Button) layout.findViewById(R.id.btn_cancel);

			btn_ok.setOnClickListener(new btn_okListener(notename.getText()
					.toString()));
			btn_cancel.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {// ������Ҫʵ�ֵĴ���
					//Log.d("btn_ce", "click");
				}
			});

			//
			// //��̬���ز�������View����
			// LayoutInflater layoutInflater =
			// LayoutInflater.from(v.getContext());
			// View longinDialogView =
			// layoutInflater.inflate(R.layout.activity_addnote, null);
			// //��ȡ�����еĿؼ�
			// EditText notename =
			// (EditText)longinDialogView.findViewById(R.id.notename);
			//
			// //����һ��AlertDialog�Ի���
			// AlertDialog longinDialog = new
			// AlertDialog.Builder(v.getContext())
			// .setTitle("������ʱ�")
			// .setView(longinDialogView) //�����Զ���ĶԻ���ʽ��
			// .create();
			// longinDialog.show();
		}
	}

	private class btn_okListener implements OnClickListener {
		private String notename;

		public btn_okListener(String notename) {
			this.notename = notename;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
//			if (notename == null)
//				Log.d("btn_ok", "fail");
//			if (notename.equals(""))
//				Log.d("btn_ok", "emp");
			Toast.makeText(v.getContext(), notename, Toast.LENGTH_SHORT).show();
//			Log.d("btn_ok", notename);
//			Log.d("btn_ok", "click");
		}
	}

	public class notesThread extends Thread {
		List<db_noteStatic> notestalist;
		public void run() {
			NoteDao nd = new LocalNoteDao();
			notestalist = nd.db_noteSta();
			//Log.d("cnt", "success "+notestalist.size());
			handler.obtainMessage(8, notestalist).sendToTarget();
		}
	}
	public class ItemClick implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
//			Log.d("notelist", "�����"+position+"����Ŀ");
			Intent intent=new Intent();
			intent.putExtra("noteid",notestalist.get(position).getNoteid());
			intent.setClass(notelistview.getContext(), NotewordActivity.class);
			startActivity(intent);
		}
	}
	@Override
    public boolean onContextItemSelected(MenuItem item) {
//        Log.d("menu","����˳����˵�����ĵ�"+item.getItemId()+"����Ŀ");
        return super.onContextItemSelected(item);
    }
}
