package com.gizwits.airpurifier.activity.advanced;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gizwits.aircondition.R;
import com.gizwits.framework.utils.DialogManager;
import com.gizwits.framework.views.RoseBoxSeekBarView;

/**
 * 滤网
 * 
 * @author hao
 * 
 */
public class RoseboxFragment extends Fragment implements OnClickListener {
	private Dialog resetDialog;
	
	private RoseBoxSeekBarView mRoseBoxSeekBarView;
	private TextView tvStatu;
	private Button btnReset;
	
	private int lv = 1000;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		resetDialog = DialogManager.getResetRoseboxDialog(getActivity(),
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
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
		mRoseBoxSeekBarView = (RoseBoxSeekBarView) v.findViewById(R.id.roseCircleSeekbar);
		tvStatu = (TextView) v.findViewById(R.id.statues);
		btnReset = (Button) v.findViewById(R.id.reset_btn);
		return v;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	public void setCurrent(int level){
		lv = level;
	}
	
	public void updateStatus(int level) {
		switch (level) {
		case 0:
			tvStatu.setText("失效停机");
			tvStatu.setTextColor(getResources().getColor(R.color.rb_text_error));
			mRoseBoxSeekBarView.setRoseboxErr(true);
			break;
		default:
			tvStatu.setText("正常");
			tvStatu.setTextColor(getResources().getColor(R.color.rb_text_normal));
			mRoseBoxSeekBarView.setRoseboxErr(false);
			break;
		}
		mRoseBoxSeekBarView.setPercent(level);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (lv != 1000) {
			updateStatus(lv);
		}
	}

}
