package com.gizwits.airpurifier.activity.advanced;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gizwits.aircondition.R;
import com.gizwits.framework.utils.DialogManager;

/**
 * 滤网
 * 
 * @author hao
 * 
 */
public class RoseboxFragment extends Fragment implements OnClickListener {
	private Button resetRosebox_btn;
	private Dialog resetDialog;
	
	private TextView statusTxt_tv;
	private ImageView statusIcon_iv;
	
	private TextView usedTime_tv;
	private TextView leftTime_tv;
	private TextView total_tv ;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		resetDialog = DialogManager.getResetRoseboxDialog(getActivity(),
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
//						cmdCenter.restRosebox();
						if (resetDialog != null)
							resetDialog.dismiss();
					}
				});
	}

	public RoseboxFragment() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_rosebox_layout, null);
		resetRosebox_btn = (Button) v.findViewById(R.id.resetRosebox_btn);
		resetRosebox_btn.setOnClickListener(this);
		statusIcon_iv = (ImageView) v.findViewById(R.id.statusIcon_iv);
		statusTxt_tv = (TextView) v.findViewById(R.id.statusTxt_tv);
		usedTime_tv =(TextView) v.findViewById(R.id.usedTime_tv);
		leftTime_tv = (TextView) v.findViewById(R.id.leftTime_tv);
		total_tv = (TextView) v.findViewById(R.id.total_tv);
		
//		updateStatus(0);
//		RoseboxInfo info = new RoseboxInfo();
//		updateStatus(info);
		return v;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.resetRosebox_btn:
			resetDialog.show();
			break;
		default:
			break;
		}
	}
	
//	public void updateStatus(RoseboxInfo info){
//		if(statusTxt_tv == null ||getActivity() == null)return;
//		// <!-- 0x00 正常(<400单位) 0x01 较弱(>=400) 0x02 很差(>=450) 03
//		// 失效停机(>=500) -->
//		int level = info.getLevel();
//		switch (level) {
//		case 0:
//			statusTxt_tv.setText("正常");
//			statusTxt_tv.setTextColor(getActivity().getResources().getColor(R.color.green));
//			statusIcon_iv.setImageResource(R.drawable.good);
//			break;
//		case 1:
//			statusTxt_tv.setText("较弱");
//			statusTxt_tv.setTextColor(getActivity().getResources().getColor(R.color.yellow));
//			statusIcon_iv.setImageResource(R.drawable.weak);
//			break;
//		case 2:
//			statusTxt_tv.setText("很差");
//			statusTxt_tv.setTextColor(getActivity().getResources().getColor(R.color.red));
//			statusIcon_iv.setImageResource(R.drawable.bad);
//			break;
//		case 3:
//			statusTxt_tv.setText("失效停机");
//			statusTxt_tv.setTextColor(getActivity().getResources().getColor(R.color.gray));
//			statusIcon_iv.setImageResource(R.drawable.stop);
//			break;
//		}
//		
//		int leftUnit = 500 -info.getUsedUnit();
//		 int time =( leftUnit*10)/24;
//		 leftTime_tv.setText(time+"天");
//		 usedTime_tv.setText(info.getUsedUnit()*10/24+"天");
//		 total_tv.setText((time+info.getUsedUnit()*10/24)+"天");
//	}

}
