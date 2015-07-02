package cn.edu.zucc.Fenfei;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import cn.edu.zucc.Fenfei.bean.TransWord;
import cn.edu.zucc.Fenfei.service.GetJsonService;

public class TranslateFragment extends Fragment {

	private EditText et_value;
	private ImageButton btn_type;
	@SuppressWarnings("unused")
	private Button btn_voice;
	private Button btn_tran;
	private EditText et_result;
	private boolean Imgstate = false;
	private ProgressDialog mProgressDialog = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View TransLayout = inflater.inflate(R.layout.fragment_translate,
				container, false);
		Init(TransLayout);
		return TransLayout;
	}

	public void Init(View TransLayout) {
		this.et_value = (EditText) TransLayout.findViewById(R.id.et_value);
		this.btn_type = (ImageButton) TransLayout.findViewById(R.id.btn_type);
		this.btn_type.setOnClickListener(new changeBtn_type());

		this.btn_voice = (Button) TransLayout.findViewById(R.id.btn_voice);
		this.btn_tran = (Button) TransLayout.findViewById(R.id.btn_tran);
		this.btn_tran.setOnClickListener(new translation());
		this.et_result = (EditText) TransLayout.findViewById(R.id.et_result);
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 2:
				mProgressDialog.dismiss();
				TransWord tw = (TransWord) msg.obj;
				et_result.setText(tw.getSimpletrans());
				break;
			default:
				break;
			}
		}
	};

	private class changeBtn_type implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (Imgstate)
				btn_type.setImageDrawable(getResources().getDrawable(
						R.drawable.ecbtn));
			else
				btn_type.setImageDrawable(getResources().getDrawable(
						R.drawable.cebtn));
			Imgstate ^= true;
		}
	}

	private class translation implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String querywords = et_value.getText().toString();
			if (querywords == null || "".equals(querywords)) {
				Toast.makeText(v.getContext(), "输入不能为空", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			mProgressDialog = ProgressDialog.show(v.getContext(), null,
					"正在查询  . . . ");
			Thread wt = new WebThread(querywords);
			wt.start();
		}
	}

	public class WebThread extends Thread {
		private String querywords;

		public WebThread(String querywords) {
			this.querywords = querywords;
		}

		public void run() {
			TransWord tw = null;
			try {
				tw = GetJsonService.parseJson(querywords);
			} catch (Exception e) {
				e.printStackTrace();
			}
			handler.obtainMessage(2, tw).sendToTarget();
		}
	}
}
