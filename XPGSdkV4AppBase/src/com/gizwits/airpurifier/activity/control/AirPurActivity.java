package com.gizwits.airpurifier.activity.control;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gizwits.aircondition.R;
import com.gizwits.airpurifier.activity.slipbar.SlipBarActivity;
import com.gizwits.framework.Interface.OnDialogOkClickListenner;
import com.gizwits.framework.activity.BaseActivity;
import com.gizwits.framework.utils.PxUtil;

public class AirPurActivity extends BaseActivity implements OnClickListener,OnTouchListener {
	private final String TAG = "YouAoControlFrament";
	
	private LinearLayout timingOn_layout;
	private ImageView timingOn_iv;
	private TextView timingOn_tv;
	private Dialog timeDialog;
	private RelativeLayout disconnected_layout;
	private Button reConn_btn;
	private ImageView push_iv;//底部箭头
	private RelativeLayout alarmTips_layout;
	private TextView alarmCounts_tv;
	private ImageView ivTitleRight;//左上角菜单按钮
	private ImageView ivTitleLeft;//右上角菜单按钮
	private TextView tvTitle;//顶部设备名称显示
	private TextView homeQualityResult_tv;
	private ImageView homeQualityResult_iv;
	private ImageView setTimeOff_iv;
	private LinearLayout functions_layout;
	private ImageView plasma_iv;//离子开关显示iv
	private ImageView childLock_iv;//童锁开关显示iv
	private ImageView qualityLight_iv;//空气灯开关显示iv
	private TextView outdoorQuality_tv;//室外空气质量指数
	private TextView pm25_tv;//展示pm2.5
	private TextView pm10_tv;//展示pm10
	private ImageView palasmaO_iv;//离子功能按钮
	private ImageView childLockO_iv;//童锁功能按钮
	private ImageView qualityLightO_iv;//空气灯功能按钮
	private ImageView silent_iv;//睡眠
	private ImageView standar_iv;//标准
	private ImageView strong_iv;//强力
	private ImageView auto_iv;//自动
	private RelativeLayout turnOff_layout;//关机界面
	private ImageView turnOn_iv;
	private RelativeLayout back_layout;//打开底部隐藏菜单后，半透明黑色遮罩层
	private Button back_btn;//灰色遮罩层按钮，可点击，退出底部菜单
	private ImageView timingOff_iv;
	private ImageView homeQualityTip_iv;//空气质量显示横条中标志
	private static View mView;//侧拉菜单拉出后右边显示本界面
	
	float mL = 0;
	float mR = 0;
	float mW = 0;
	float mW100 = 0;
	private float mBgW = 0;
	private int select_id;//选中的id
	/** The is click. */
	private boolean isClick;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.youao_control);
		initUI();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	private void initUI(){
		mView = findViewById(R.id.main_layout);
		homeQualityTip_iv = (ImageView) findViewById(R.id.homeQualityTipArrow_iv);
		back_btn = (Button) findViewById(R.id.back_btn);
		back_btn.setOnTouchListener(this);
//		timmingOff_tv = (TextView) findViewById(R.id.timmingOff_tv);
//		timmingOff_tv.setOnClickListener(this);
//		alarmCounts_tv = (TextView) findViewById(R.id.alarmCounts_tv);
//		alarmTips_layout = (RelativeLayout) findViewById(R.id.alarmTips_layout);

		back_layout = (RelativeLayout) findViewById(R.id.back_layout);
//		push_iv = (ImageView) findViewById(R.id.push_iv);
//		push_iv.setOnClickListener(this);
		// connectType_tv = (TextView) findViewById(R.id.connectType_tv);
//		timingOff_iv = (ImageView) findViewById(R.id.timingOff_iv);
//		timingOff_iv.setOnClickListener(this);
		homeQualityResult_tv = (TextView) findViewById(R.id.homeQualityResult_tv);
		homeQualityResult_iv = (ImageView) findViewById(R.id.homeQualityResult_iv);
		setTimeOff_iv = (ImageView) findViewById(R.id.setTimeOff_iv);
		setTimeOff_iv.setOnTouchListener(onTouchListener);
		functions_layout = (LinearLayout) findViewById(R.id.functions_layout);
		//********************效果显示********************
		plasma_iv = (ImageView) findViewById(R.id.plasama_iv);
		childLock_iv = (ImageView) findViewById(R.id.childLock_iv);
		qualityLight_iv = (ImageView) findViewById(R.id.qualityLight_iv);
		outdoorQuality_tv = (TextView) findViewById(R.id.outdoorQuality_tv);
		//********************pm25 10的值********************
		pm25_tv = (TextView) findViewById(R.id.pm25_tv);
		pm10_tv = (TextView) findViewById(R.id.pm10_tv);
		//********************隐藏的功能键********************
		setTimeOff_iv = (ImageView) findViewById(R.id.setTimeOff_iv);
		setTimeOff_iv.setOnClickListener(this);
		palasmaO_iv = (ImageView) findViewById(R.id.plasmaO_iv);
		palasmaO_iv.setOnClickListener(this);
		childLockO_iv = (ImageView) findViewById(R.id.childLockO_iv);
		childLockO_iv.setOnClickListener(this);
		qualityLightO_iv = (ImageView) findViewById(R.id.qualityLightO_iv);
		qualityLightO_iv.setOnClickListener(this);
		//********************风速档位********************
		auto_iv = (ImageView) findViewById(R.id.auto_iv);
		auto_iv.setOnClickListener(this);
		standar_iv = (ImageView) findViewById(R.id.standar_iv);
		standar_iv.setOnClickListener(this);
		strong_iv = (ImageView) findViewById(R.id.strong_iv);
		strong_iv.setOnClickListener(this);
		silent_iv = (ImageView) findViewById(R.id.silent_iv);
		silent_iv.setOnClickListener(this);
		//********************顶部按钮********************
		ivTitleRight = (ImageView) findViewById(R.id.ivPower);
		ivTitleRight.setOnClickListener(this);
		ivTitleLeft = (ImageView) findViewById(R.id.ivMenu);
		ivTitleLeft.setOnClickListener(this);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
	}

	/**
	 * 初始化 空气质量指示条
	 */
	private void initQualityTips() {

		ViewTreeObserver vto = homeQualityResult_iv.getViewTreeObserver();

		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				float w = homeQualityResult_iv.getWidth();
				float l = homeQualityResult_iv.getLeft();
				float r = homeQualityResult_iv.getRight();

				// 提示条分为100份
				mL = l + w / 50;
				mR = r - w / 10;
				mW = mR - mL;
				mW100 = mW / 100;
			}
		});

	}

	/**
	 * 更新空气质量指示条 指示位置
	 * 
	 * @param position
	 */
	private void updateTips(final float position) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				homeQualityTip_iv.setX(mL + position);
			}
		});

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.reConn_btn:
			break;
		case R.id.childLockO_iv:
			break;
		case R.id.plasmaO_iv:
			break;
		case R.id.qualityLightO_iv:
			break;
		case R.id.turnOn_iv:
			break;
		case R.id.timingOn_layout:
		case R.id.timingOn_iv:
		case R.id.timingOn_tv:
			break;
		case R.id.ivMenu:
			if (!isClick) {
				isClick = true;
				startActivityForResult(new Intent(AirPurActivity.this,
						SlipBarActivity.class), Activity.RESULT_FIRST_USER);
				overridePendingTransition(0, 0);
			}
			break;
		case R.id.ivPower:
			mCenter.cSwitchOn(mXpgWifiDevice, false);
			break;
		case R.id.auto_iv:
			break;
		case R.id.silent_iv:
			break;
		case R.id.standar_iv:
			break;
		case R.id.strong_iv:
			break;
//		case R.id.push_iv:
//			troggleBottom();
//			break;
		default:
			break;
		}
	}

	/**
	 * bottom 功能的显示和隐藏
	 */
	public void troggleBottom() {
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) functions_layout
				.getLayoutParams();
		int bM = params.bottomMargin;
		if (bM == 0) {
			params.bottomMargin = PxUtil.dip2px(this, -76);
			back_layout.setVisibility(View.GONE);
		} else {
			params.bottomMargin = 0;
			back_layout.setVisibility(View.VISIBLE);
		}
		functions_layout.setLayoutParams(params);
	}

	public void bottomFucTrogg() {
		push_iv.setVisibility(View.VISIBLE);
	}

	private int currentOn = 0;
	private OnDialogOkClickListenner okTimingOnClickListenner = new OnDialogOkClickListenner() {

		@Override
		public void onClick(View arg0) {
			timeDialog.dismiss();
			if (currentOn == 0) {
				setTimingOn(false, currentOn);
			} else {
				setTimingOn(true, currentOn);
			}

		}

		@Override
		public void callBack(Object obj) {
			currentOn = (Integer) obj;
		}
	};
	private int currentOff = 0;
	private OnDialogOkClickListenner okTimingOffClickListenner = new OnDialogOkClickListenner() {

		@Override
		public void onClick(View arg0) {
			timeDialog.dismiss();
			// cmd.setTimingOff()
			if (currentOff == 0) {
				setTimingOff(false, currentOff);
			} else {
				setTimingOff(true, currentOff);
			}
		}

		@Override
		public void callBack(Object obj) {
			currentOff = (Integer) obj;
		}
	};

	/**
	 * 设置开关状态
	 * 
	 * @param isOn
	 */
	public void setSwitch(boolean isOn) {
		if (isOn) {
			turnOff_layout.setVisibility(View.GONE);
		} else {
			turnOff_layout.setVisibility(View.VISIBLE);
			reAll();
		}

	}

	/**
	 * 运行模式
	 * 
	 * @param speed
	 */
	public void changeRUNmodeBg(int speed) {
		reAll();
		if (speed == 4) {
			setSilentAnimation();
			select_id=4;
		} 
		if (speed == 2) {
			setStandarAnimation();
			select_id=2;
		} 

		if (speed == 1) {
			setStrongAnimation();
			select_id=1;
		} 

		if (speed == 5) {
			setAutoAnimation();
			select_id=5;
		} 

	}

	/**
	 * 定时开机
	 * 
	 * @param isOn
	 */
	public void setTimingOn(boolean isOn, int time) {
		if (isOn) {
			timingOn_tv.setText(time + "小时后开机");
			timingOn_layout.setBackgroundResource(R.drawable.alarm_select);
			timingOn_iv.setTag("0");
		} else {
			timingOn_tv.setText("定时开机");
			timingOn_layout.setBackgroundResource(R.drawable.alarm);
			timingOn_iv.setTag("1");
		}
	}

	/**
	 * 定时关机
	 * 
	 * @param isOn
	 */
	public void setTimingOff(boolean isOn, int time) {

//		if (isOn) {
//			timmingOff_tv.setText(time + "小时后关机");
//			// timingOff_layout.setImageResource(R.drawable.alarm_select);
//			timingOff_iv.setTag("0");
//		} else {
//			timmingOff_tv.setText("定时关机")a;s
//			// timingOff_layout.setImageResource(R.drawable.alarm);
//			timingOff_iv.setTag("1");
//		}
	}

	public void setChildLock(boolean isOn) {
		if (isOn) {
			childLockO_iv.setImageResource(R.drawable.icon9_2);
			childLockO_iv.setTag("0");
			childLock_iv.setImageResource(R.drawable.lock_select);
		} else {
			childLockO_iv.setImageResource(R.drawable.icon9);
			childLockO_iv.setTag("1");
			childLock_iv.setImageResource(R.drawable.lock_not_select);
		}
	}

	public void setPlasma(boolean isOn) {
		if (isOn) {
			palasmaO_iv.setImageResource(R.drawable.icon8_2);
			palasmaO_iv.setTag("0");
			plasma_iv.setImageResource(R.drawable.anion_select);
		} else {
			palasmaO_iv.setImageResource(R.drawable.icon8);
			palasmaO_iv.setTag("1");
			plasma_iv.setImageResource(R.drawable.anion_not_select);
		}
	}

	public void setIndicatorLight(boolean isOn) {
		if (isOn) {
			qualityLightO_iv.setImageResource(R.drawable.icon10_2);
			qualityLightO_iv.setTag("0");
			qualityLight_iv.setImageResource(R.drawable.quality_select);
		} else {
			qualityLightO_iv.setImageResource(R.drawable.icon10);
			qualityLightO_iv.setTag("1");
			qualityLight_iv.setImageResource(R.drawable.quality_not_select);
		}
	}

//	private Handler updateHandler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			Status status = (Status) msg.obj;
//			if (homeQualityResult_tv == null)
//				return;
//			// initAlarmInfo();
//			setChildLock(status.isChildLockOn());
//			setIndicatorLight(status.isIndicatorLightOn());
//			setPlasma(status.isPlasmaOn());
//			setUV(status.isUvLithtOn());
//			// currentOn = status.getTimingOn();
//			// currentOff = status.getTimingOff();
//			if (status.isAutoRunMode()) {
//				if (select_id!=5) {
//					changeRUNmodeBg(5);
//				}
//			} else {
//				if (select_id!=status.getWindSpeed()) {
//					changeRUNmodeBg(status.getWindSpeed());
//				}
//			}
//
//			Status s = (Status) msg.obj;
//			int level = s.getCurrentAirQuality();
//			// 转换为分数
//			int result = (int) (level * 8);
//			if (result > 100) {
//				result = 100;
//			}
//			float quality = (100 - result) * mW100;
//			updateTips(quality);
//			bgScrollTo(result);
//			// top_point.setText("" + result);
//			if (level > 0 && level <= 3) {
//				homeQualityResult_tv.setText("优");
//				homeQualityResult_tv.setTextColor(Color.parseColor("#ffffff"));
//			} else if (level > 3 && level <= 7) {
//				homeQualityResult_tv.setText("良");
//				homeQualityResult_tv.setTextColor(Color.parseColor("#6cf47c"));
//			} else if (level > 7 && level < 11) {
//				homeQualityResult_tv.setText("一般");
//				homeQualityResult_tv.setTextColor(Color.parseColor("#eff160"));
//			} else if (level > 10 && level <= 15) {
//				homeQualityResult_tv.setText("差");
//				homeQualityResult_tv.setTextColor(Color.parseColor("#ff9f17"));
//			}
//			setSwitch(status.isSwitchOn());
//			int timeoff = status.getTimingOff();
//			if (timeoff == 0) {
//				setTimingOff(false, timeoff);
//			} else {
//				setTimingOff(true, timeoff);
//			}
//			currentOn = status.getTimingOn();
//			currentOff = status.getTimingOff();
//			int timeon = status.getTimingOn();
//			CustomLog.info(TAG, "timeon:" + timeon);
//			if (timeon == 0) {
//				setTimingOn(false, timeon);
//			} else {
//				setTimingOn(true, timeon);
//			}
//
//		};
//	};

	Dialog alarmDialog = null;

//	@Override
//	public void onUpdataFault(String fault, boolean isExit) {
//		CustomLog.info(TAG, "onUpdateFault,fault:" + fault + ",isExit:"
//				+ isExit);
//		if (!"无故障".equals(fault) && !isExit) {
//			if (alarmDialog != null && alarmDialog.isShowing()) {
//				alarmDialog.dismiss();
//			}
//			alarmDialog = DialogManager.getAlarmDialog(getActivity(), fault,
//					new OnClickListener() {
//
//						@Override
//						public void onClick(View arg0) {
//							if (alarmDialog != null) {
//								alarmDialog.dismiss();
//							}
//							Intent intent = new Intent(getActivity(),
//									AdvancedActivity.class);
//							intent.putExtra(ID.ADVANCE_SET, AdvanceType.alarm);
//							startActivity(intent);
//						}
//					});
//			alarmDialog.show();
//		}
//		// mHandler.sendEmptyMessage(3223);
//		initAlarmInfo();
//
//	}

	private OnTouchListener onTouchListener = new OnTouchListener() {

		private double yDown;
		private double yCurrent;
		private boolean isProgressing = false;

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				isProgressing = false;
				yDown = event.getY();

				break;

			case MotionEvent.ACTION_MOVE:
				yCurrent = event.getY();
				if (Math.abs(yDown - yCurrent) > 40) {
					if (!isProgressing) {
						troggleBottom();
						isProgressing = true;
					}
				}
				break;
			}
			return true;
		}
	};

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v.getId() == R.id.back_btn) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				troggleBottom();
			}
		}
		return true;
	}
	
	/**
	 * 设置silent的帧动画
	 */
	public void setSilentAnimation(){
		silent_iv.setBackgroundResource(R.drawable.icon_sleep_select);
	}
	
	/**
	 * 设置standar的帧动画
	 */
	public void setStandarAnimation(){
		standar_iv.setBackgroundResource(R.drawable.icon_standard_select);
	}
	
	/**
	 * 设置strong背景
	 */
	public void setStrongAnimation(){
		strong_iv.setBackgroundResource(R.drawable.icon_strong_select);
	}
	
	/**
	 * 设置auto背景
	 */
	public void setAutoAnimation(){
		auto_iv.setBackgroundResource(R.drawable.icon_intelligence_select);
	}
	
	/**
	 * 设置重设全部功能iv
	 */
	public void reAll(){
		silent_iv.setBackgroundResource(R.drawable.icon_sleep_not_select);
		standar_iv.setBackgroundResource(R.drawable.icon_standard_not_select);
		strong_iv.setBackgroundResource(R.drawable.icon_strong_not_select);
		auto_iv.setBackgroundResource(R.drawable.icon_intelligence_not_select);
	}

	/**
	 * Gets the view.
	 * 
	 * @return the view
	 */
	public static Bitmap getView() {
		// 用指定大小生成一张透明的32位位图，并用它构建一张canvas画布
		Bitmap mBitmap = Bitmap.createBitmap(mView.getWidth(),
				mView.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(mBitmap);
		// 将指定的view包括其子view渲染到这种画布上，在这就是上一个activity布局的一个快照，现在这个bitmap上就是上一个activity的快照
		mView.draw(canvas);
		return mBitmap;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		isClick = false;
	}
}
