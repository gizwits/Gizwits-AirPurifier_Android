package com.gizwits.airpurifier.activity.advanced;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.SyncStateContract.Constants;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gizwits.aircondition.R;
import com.gizwits.framework.activity.BaseActivity;
import com.gizwits.framework.entity.AdvanceType;

/**
 * 高级功能
 * 
 * @author hao
 * 
 */
public class AdvancedActivity extends BaseActivity implements
		OnClickListener {
	private TextView title_tv;
	private ImageView ivLeft;

	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	private SensitivityFragment sensitivityFragment;
	private AlarmFragment alarmFragment;
	private RoseboxFragment roseboxFragment;
	private Button sensitivity_btn;
	private Button rosebox_btn;
	private Button alarm_btn;

	private enum CurrentView {
		sensitivity, rosebox, alarm
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.advanced_layout);

		initUI();
		initFragment();

	}

	@Override
	public void onResume() {
		super.onResume();
		AdvanceType at = (AdvanceType) getIntent().getSerializableExtra(
				"advanced_set");
		if (at != null) {
			switch (at) {
			case alarm:
				changeView(CurrentView.alarm);
				break;

			default:
				changeView(CurrentView.sensitivity);
				break;
			}
		} else {
			changeView(CurrentView.sensitivity);
		}

	}

	private void initUI() {
		title_tv = (TextView) findViewById(R.id.tvTitle);
		title_tv.setText("高级功能");
		ivLeft = (ImageView) findViewById(R.id.ivBack);
		ivLeft.setOnClickListener(this);
		sensitivity_btn = (Button) findViewById(R.id.sensitivity_btn);
		sensitivity_btn.setOnClickListener(this);
		rosebox_btn = (Button) findViewById(R.id.rosebox_btn);
		rosebox_btn.setOnClickListener(this);
		alarm_btn = (Button) findViewById(R.id.alarm_btn);
		alarm_btn.setOnClickListener(this);
	}

	private void changeView(CurrentView id) {
		switch (id) {
		case alarm:
			fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.content_layout, alarmFragment);
			fragmentTransaction.commit();

			break;
		case rosebox:
			fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.content_layout, roseboxFragment);
			fragmentTransaction.commit();
			break;
		case sensitivity:
			fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.content_layout,
					sensitivityFragment);
			fragmentTransaction.commit();
			break;
		}

		if (id == CurrentView.alarm) {
			alarm_btn.setBackgroundResource(R.drawable.adv_bg1);
			alarm_btn.setTextColor(getResources().getColor(R.color.white));
		} else {
			alarm_btn.setBackgroundResource(R.drawable.adv_bg2);
			alarm_btn.setTextColor(getResources().getColor(
					R.color.gray));
		}
		if (id == CurrentView.rosebox) {
			rosebox_btn.setBackgroundResource(R.drawable.adv_bg1);
			rosebox_btn.setTextColor(getResources().getColor(R.color.white));
		} else {
			rosebox_btn.setBackgroundResource(R.drawable.adv_bg2);
			rosebox_btn.setTextColor(getResources().getColor(
					R.color.gray));
		}
		if (id == CurrentView.sensitivity) {
			sensitivity_btn.setBackgroundResource(R.drawable.adv_bg1);
			sensitivity_btn
					.setTextColor(getResources().getColor(R.color.white));
		} else {
			sensitivity_btn.setBackgroundResource(R.drawable.adv_bg2);
			sensitivity_btn.setTextColor(getResources().getColor(
					R.color.gray));
		}
	}

	private void initFragment() {
		// 开启事物，添加第一个fragment
		fragmentManager = getFragmentManager();
		// fragmentTransaction = fragmentManager.beginTransaction();
		sensitivityFragment = new SensitivityFragment();
		// fragmentTransaction.replace(R.id.content_layout,
		// sensitivityFragment);
		// fragmentTransaction.commit();

		roseboxFragment = new RoseboxFragment();
		alarmFragment = new AlarmFragment();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ivBack:
			onBackPressed();
			break;
		case R.id.sensitivity_btn:
			changeView(CurrentView.sensitivity);
			break;
		case R.id.rosebox_btn:
			changeView(CurrentView.rosebox);
			break;
		case R.id.alarm_btn:
			changeView(CurrentView.alarm);
			break;
		default:
			break;
		}
	}

//	@Override
//	public void OnStatusResp(StatusResp_t resp) {
//		// TODO Auto-generated method stub
//		super.OnStatusResp(resp);
//		int sensitivity = resp.getSensitivity();// 传感器灵敏度
//		sensitivityFragment.changeSensi(sensitivity);
//	}

//	@Override
//	public void OnRoseboxResp(RoseboxResp_t resp) {
//		// TODO Auto-generated method stub
//		super.OnRoseboxResp(resp);
//
//		int usedUnit = resp.getUsedTime();// 使用时间
//		int level = resp.getUsedLevel();// 滤网数据
//		
//		RoseboxInfo info = new RoseboxInfo();
//		info.setLevel(level);
//		info.setUsedUnit(usedUnit);
//		Constants.status.setUsedTime(resp.getUsedTime());
//		Message msg = new Message();
//		msg.what = ID.RoseboxResp;
//		msg.obj = info;
//		mHandler.sendMessage(msg);
//	}
//
//	private Handler mHandler = new Handler() {
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//
//			case ID.RoseboxResp:
//				RoseboxInfo level = (RoseboxInfo) msg.obj;
//				roseboxFragment.updateStatus(level);
//				break;
//			}
//		}
//	};

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		finish();
	}
}
