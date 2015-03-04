package com.gizwits.airpurifier.activity.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

import com.gizwits.aircondition.R;
import com.gizwits.airpurifier.activity.advanced.AdvancedActivity;
import com.gizwits.airpurifier.activity.slipbar.SlipBarActivity;
import com.gizwits.framework.Interface.OnDialogOkClickListenner;
import com.gizwits.framework.activity.BaseActivity;
import com.gizwits.framework.config.JsonKeys;
import com.gizwits.framework.entity.AdvanceType;
import com.gizwits.framework.entity.DeviceAlarm;
import com.gizwits.framework.utils.DialogManager;
import com.gizwits.framework.utils.DialogManager.OnTimingChosenListener;
import com.gizwits.framework.utils.PxUtil;
import com.gizwits.framework.webservice.GetPMService;
import com.gizwits.framework.webservice.LocationService;
import com.xpg.common.useful.DateUtil;
import com.xtremeprog.xpgconnect.XPGWifiDevice;

public class AirPurActivity extends BaseActivity implements OnClickListener,OnTouchListener {
	private final String TAG = "AirPurActivity";
	
	private LinearLayout timingOn_layout;//定时开机layout
	private TextView timingOn_tv;//定时开机文字显示
	private Dialog timeDialog;//设置时间dialog
	private ImageView push_iv;//底部箭头
	private ImageView ivTitleRight;//左上角菜单按钮
	private TextView tvTitle;//顶部文字
	private ImageView ivTitleLeft;//右上角菜单按钮
	private TextView homeQualityResult_tv;
	private ImageView homeQualityResult_iv;
	private ImageView bottom_push;//底部pushIv
	private LinearLayout functions_layout;
	private ImageView plasma_iv;//离子开关显示iv
	private ImageView childLock_iv;//童锁开关显示iv
	private ImageView qualityLight_iv;//空气灯开关显示iv
	private TextView outdoorQuality_tv;//室外空气质量指数
	private TextView pm25_tv;//展示pm2.5
	private TextView pm10_tv;//展示pm10
	private LinearLayout palasmaO_ll;//离子开关按钮timingOff_ll
	private ImageView palasmaO_iv;//离子开关按钮ImageView
	private LinearLayout childLockO_ll;//童锁开关按钮timingOff_ll
	private ImageView childLockO_iv;//童锁开关按钮ImageView
	private LinearLayout qualityLightO_ll;//空气灯开关按钮timingOff_ll
	private ImageView qualityLightO_iv;//空气灯开关按钮ImageView
	private LinearLayout timingOff_ll;//定时关机按钮timingOff_ll
	private ImageView timingOff_iv;//定时关机按钮ImageView
	private TextView timingOff_tv;//定时关机按钮TextView
	private ImageView silent_iv;//睡眠
	private ImageView standar_iv;//标准
	private ImageView strong_iv;//强力
	private ImageView auto_iv;//自动
	private RelativeLayout turnOff_layout;//关机界面
	private ImageView turnOn_iv;//开机按钮
	private RelativeLayout back_layout;//打开底部隐藏菜单后，半透明黑色遮罩层
	private Button back_btn;//灰色遮罩层按钮，可点击，退出底部菜单
	private ImageView homeQualityTip_iv;//空气质量显示横条中标志
	private static View mView;//侧拉菜单拉出后右边显示本界面
	private RelativeLayout rlAlarmTips;//警报条数栏
	private TextView tvAlarmTipsCount;//警报条数显示
	private RelativeLayout main_layout;//主界面
	
	float mL = 0;
	float mR = 0;
	float mW = 0;
	float mW100 = 0;
	/** The is click. */
	private boolean isClick;
	/** The device data map. */
	private ConcurrentHashMap<String, Object> deviceDataMap;
	/** The m fault dialog. */
	private Dialog mFaultDialog;
	/** The statu map. */
	private ConcurrentHashMap<String, Object> statuMap;
	/** The alarm list. */
	private ArrayList<DeviceAlarm> alarmList;
	/** The already alarm list. */
	private ArrayList<String> isAlarmList;
	/** The already alarm check. */
	private boolean isAlarm;//每一个alarm本页中只允许显示一次
	/** The timing off. */
	private int timingOn, timingOff;
	/** power off dialog; */
	Dialog powerDialog=null;
	
	private enum handler_key {

		/** The update ui. */
		UPDATE_UI,

		/** The alarm. */
		ALARM,

		/** The disconnected. */
		DISCONNECTED,

		/** The received. */
		RECEIVED,

		/** The get statue. */
		GET_STATUE,
	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_airpur_control);
		initUI();
		initCity();
		statuMap = new ConcurrentHashMap<String, Object>();//设备状态数据
		alarmList = new ArrayList<DeviceAlarm>();//警报状态数据
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mXpgWifiDevice.setListener(deviceListener);//设置在本界面中可回调数据
		handler.sendEmptyMessage(handler_key.GET_STATUE.ordinal());
		isAlarmList=new ArrayList<String>();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		//注销设备
		if (mXpgWifiDevice != null && mXpgWifiDevice.isConnected()) {
			mCenter.cDisconnect(mXpgWifiDevice);
			mXpgWifiDevice = null;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		isClick = false;//左侧菜单可打开
	}

	@Override
	protected void didDisconnected(XPGWifiDevice device) {
		super.didDisconnected(device);
	}
	
	@Override
	protected void didReceiveData(XPGWifiDevice device,
			ConcurrentHashMap<String, Object> dataMap, int result) {
		Log.e(TAG, "didReceiveData");
		this.deviceDataMap = dataMap;
		handler.sendEmptyMessage(handler_key.RECEIVED.ordinal());
	}
	
	//初始化所有控件
	private void initUI(){
		main_layout=(RelativeLayout) findViewById(R.id.main_layout);
		timingOn_layout=(LinearLayout) findViewById(R.id.timingOn_layout);
		timingOn_layout.setOnClickListener(this);
		timingOn_tv = (TextView) findViewById(R.id.timingOn_tv);
		turnOff_layout=(RelativeLayout) findViewById(R.id.turnOff_layout);
		turnOn_iv=(ImageView) findViewById(R.id.turnOn_iv);
		turnOn_iv.setOnClickListener(this);
		mView = findViewById(R.id.main_layout);
		homeQualityTip_iv = (ImageView) findViewById(R.id.homeQualityTipArrow_iv);
		back_btn = (Button) findViewById(R.id.back_btn);
		back_btn.setOnTouchListener(this);
		back_layout = (RelativeLayout) findViewById(R.id.back_layout);
		push_iv = (ImageView) findViewById(R.id.push_iv);
		push_iv.setOnClickListener(this);
		homeQualityResult_tv = (TextView) findViewById(R.id.homeQualityResult_tv);
		homeQualityResult_iv = (ImageView) findViewById(R.id.homeQualityResult_iv);
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
		bottom_push = (ImageView) findViewById(R.id.bottom_push);
		bottom_push.setOnTouchListener(onTouchListener);
//		palasmaO_tv = (TextView) findViewById(R.id.plasmaO_tv);
		palasmaO_iv = (ImageView) findViewById(R.id.plasmaO_iv);
		palasmaO_ll = (LinearLayout) findViewById(R.id.plasmaO_ll);
		palasmaO_ll.setOnClickListener(this);
//		childLockO_tv = (TextView) findViewById(R.id.childLockO_tv);
		childLockO_iv = (ImageView) findViewById(R.id.childLockO_iv);
		childLockO_ll = (LinearLayout) findViewById(R.id.childLockO_ll);
		childLockO_ll.setOnClickListener(this);
//		qualityLightO_tv = (TextView) findViewById(R.id.qualityLightO_tv);
		qualityLightO_iv = (ImageView) findViewById(R.id.qualityLightO_iv);
		qualityLightO_ll = (LinearLayout) findViewById(R.id.qualityLightO_ll);
		qualityLightO_ll.setOnClickListener(this);
		timingOff_tv = (TextView) findViewById(R.id.timingOff_tv);
		timingOff_iv = (ImageView) findViewById(R.id.timingOff_iv);
		timingOff_ll = (LinearLayout) findViewById(R.id.timingOff_ll);
		timingOff_ll.setOnClickListener(this);
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
		tvTitle=(TextView) findViewById(R.id.tvTitle);
		tvTitle.setOnClickListener(this);
		tvAlarmTipsCount = (TextView) findViewById(R.id.tvAlarmTipsCount);
		rlAlarmTips = (RelativeLayout) findViewById(R.id.rlAlarmTips);
		rlAlarmTips.setOnClickListener(this);
		
		initQualityTips();
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
	 * 更新空气质量指示条 指示位置1~15档
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
	
	/**
	 * 更新空气质量背景和文字
	 * 
	 * @param position
	 */
	private void updateBackgound(String lv) {
		if (lv.equals("0")) {
			main_layout.setBackgroundResource(R.drawable.good_bg);
			homeQualityResult_tv.setText("优");
		}else if(lv.equals("1")){
			main_layout.setBackgroundResource(R.drawable.liang_bg);
			homeQualityResult_tv.setText("良");
		}else if(lv.equals("2")){
			main_layout.setBackgroundResource(R.drawable.middle_bg);
			homeQualityResult_tv.setText("中");
		}else if(lv.equals("3")){
			main_layout.setBackgroundResource(R.drawable.bad_bg);
			homeQualityResult_tv.setText("差");
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rlAlarmTips:
			Intent intent = new Intent(this, AdvancedActivity.class);
			intent.putExtra("advanced_set", AdvanceType.alarm);
			startActivity(intent);
			break;
		case R.id.childLockO_ll:
			if (childLockO_ll.getTag().toString() == "0") {
				mCenter.cChildLock(mXpgWifiDevice, false);
				setChildLock(false);
			}else{
				mCenter.cChildLock(mXpgWifiDevice, true);
				setChildLock(true);
			}
			break;
		case R.id.plasmaO_ll:
			if (palasmaO_ll.getTag().toString() == "0") {
				mCenter.cSwitchPlasma(mXpgWifiDevice, false);
				setPlasma(false);
			}else{
				mCenter.cSwitchPlasma(mXpgWifiDevice, true);
				setPlasma(true);
			}
			break;
		case R.id.qualityLightO_ll:
			if (qualityLightO_ll.getTag().toString() == "0") {
				mCenter.cLED(mXpgWifiDevice, false);
				setIndicatorLight(false);
			}else{
				mCenter.cLED(mXpgWifiDevice, true);
				setIndicatorLight(true);
			}
			break;
		case R.id.timingOff_ll:
			DialogManager.getWheelTimingDialog(this,
					new OnTimingChosenListener() {

						@Override
						public void timingChosen(int time) {
							// 设置定时开机时间
							mCenter.cCountDownOff(mXpgWifiDevice, DateUtil.hourCastToMin(time));
							timingOff = time;
							if (time == 0) {
								timingOff_tv.setText("定时关机");
								timingOff_iv.setImageResource(R.drawable.icon_4);
							} else {
								timingOff_tv.setText(time + "小时");
								timingOff_iv.setImageResource(R.drawable.icon_4_2);
							}
						}
					}, " 定时关机", timingOff == 0 ? 24 : timingOff - 1).show();
			break;
		case R.id.turnOn_iv:
			mCenter.cSwitchOn(mXpgWifiDevice, true);
			setSwitch(true);
			break;
		case R.id.timingOn_layout:
			DialogManager.getWheelTimingDialog(this,
					new OnTimingChosenListener() {

						@Override
						public void timingChosen(int time) {
							// 设置定时开机时间
							mCenter.cCountDownOn(mXpgWifiDevice, DateUtil.hourCastToMin(time));
							timingOn = time;
							if (time != 0) {
								timingOn_tv.setText(time + "小时");
							} else {
								timingOn_tv.setText("定时开机");
							}
						}
					}, " 定时开机", timingOn == 0 ? 24 : timingOn - 1).show();
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
			powerDialog=DialogManager.getPowerOffDialog(this, new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mCenter.cSwitchOn(mXpgWifiDevice, false);
					setSwitch(false);
					powerDialog.dismiss();
				}
			});
			powerDialog.show();
			break;
		case R.id.auto_iv:
			mCenter.cSetSpeed(mXpgWifiDevice, 3);
			changeRUNmodeBg(3);
			break;
		case R.id.silent_iv:
			mCenter.cSetSpeed(mXpgWifiDevice, 2);
			changeRUNmodeBg(2);
			break;
		case R.id.standar_iv:
			mCenter.cSetSpeed(mXpgWifiDevice, 1);
			changeRUNmodeBg(1);
			break;
		case R.id.strong_iv:
			mCenter.cSetSpeed(mXpgWifiDevice, 0);
			changeRUNmodeBg(0);
			break;
		case R.id.push_iv:
			troggleBottom();
			break;
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
			params.bottomMargin = PxUtil.dip2px(this, -81);
			back_layout.setVisibility(View.GONE);
			push_iv.setImageResource(R.drawable.arrow_1);
		} else {
			params.bottomMargin = 0;
			back_layout.setVisibility(View.VISIBLE);
			push_iv.setImageResource(R.drawable.arrow_2);
		}
		functions_layout.setLayoutParams(params);
	}

	public void bottomFucTrogg() {
		push_iv.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置定时开机点击事件
	 */
	private int currentOn = 0;
	private OnDialogOkClickListenner okTimingOnClickListenner = new OnDialogOkClickListenner() {

		@Override
		public void onClick(View arg0) {
			timeDialog.dismiss();
			setTimingOn(currentOn);
		}

		@Override
		public void callBack(Object obj) {
			currentOn = (Integer) obj;
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
		if (speed == 2) {
			setSilentAnimation();
		} 
		if (speed == 1) {
			setStandarAnimation();
		} 

		if (speed == 0) {
			setStrongAnimation();
		} 

		if (speed == 3) {
			setAutoAnimation();
		} 

	}

	/**
	 * 定时开机
	 * 
	 * @param isOn
	 */
	public void setTimingOn(int time) {
		timingOn = time;
		if (time != 0) {
			timingOn_tv.setText(time + "小时");
		} else {
			timingOn_tv.setText("定时开机");
		}
	}

	/**
	 * 定时关机
	 * 
	 * @param isOn
	 */
	public void setTimingOff(int time) {
		timingOff = time;
		if (time == 0) {
			timingOff_tv.setText("定时关机");
			timingOff_iv.setImageResource(R.drawable.icon_4);
		} else {
			timingOff_tv.setText(time + "小时");
			timingOff_iv.setImageResource(R.drawable.icon_4_2);
		}
	}

	/**
	 * 设置童锁按钮以及显示iv
	 * @param isOn
	 */
	public void setChildLock(boolean isOn) {
		if (isOn) {
			childLockO_iv.setImageResource(R.drawable.icon_2_2);
			childLockO_ll.setTag("0");
			childLock_iv.setImageResource(R.drawable.lock_select);
		} else {
			childLockO_iv.setImageResource(R.drawable.icon_2);
			childLockO_ll.setTag("1");
			childLock_iv.setImageResource(R.drawable.lock_not_select);
		}
	}

	/**
	 * 设置等离子按钮以及显示iv
	 * @param isOn
	 */
	public void setPlasma(boolean isOn) {
		if (isOn) {
			palasmaO_iv.setImageResource(R.drawable.icon_3_2);
			palasmaO_ll.setTag("0");
			plasma_iv.setImageResource(R.drawable.anion_select);
		} else {
			palasmaO_iv.setImageResource(R.drawable.icon_3);
			palasmaO_ll.setTag("1");
			plasma_iv.setImageResource(R.drawable.anion_not_select);
		}
	}

	/**
	 * 设置灯光按钮以及显示iv
	 * @param isOn
	 */
	public void setIndicatorLight(boolean isOn) {
		if (isOn) {
			qualityLightO_iv.setImageResource(R.drawable.icon_1_2);
			qualityLightO_ll.setTag("0");
			qualityLight_iv.setImageResource(R.drawable.quality_select);
		} else {
			qualityLightO_iv.setImageResource(R.drawable.icon_1);
			qualityLightO_ll.setTag("1");
			qualityLight_iv.setImageResource(R.drawable.quality_not_select);
		}
	}

	/**
	 * 底部上啦菜单菜单动作
	 */
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
	 * 设置silent背景
	 */
	public void setSilentAnimation(){
		silent_iv.setBackgroundResource(R.drawable.icon_sleep_select);
	}
	
	/**
	 * 设置standar背景
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
	 * 设置重设风速功能Iv
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
	
	/**
	 * 顶部警报条数显示
	 * @param isShow
	 * @param count
	 */
	private void setTipsLayoutVisiblity(boolean isShow, int count) {
		rlAlarmTips.setVisibility(isShow ? View.VISIBLE : View.GONE);
		tvAlarmTipsCount.setText(count + "");
	}
	
	/**
	 * Input device status to maps.
	 * 
	 * @param map
	 *            the map
	 * @param json
	 *            the json
	 * @throws JSONException
	 *             the JSON exception
	 */
	private void inputDataToMaps(ConcurrentHashMap<String, Object> map,
			String json) throws JSONException {
		Log.i("revjson", json);
		JSONObject receive = new JSONObject(json);
		Iterator actions = receive.keys();
		while (actions.hasNext()) {

			String action = actions.next().toString();
			Log.i("revjson", "action=" + action);
			// 忽略特殊部分
			if (action.equals("cmd") || action.equals("qos")
					|| action.equals("seq") || action.equals("version")) {
				continue;
			}
			JSONObject params = receive.getJSONObject(action);
			Log.i("revjson", "params=" + params);
			Iterator it_params = params.keys();
			while (it_params.hasNext()) {
				String param = it_params.next().toString();
				Object value = params.get(param);
				map.put(param, value);
				Log.i(TAG, "Key:" + param + ";value" + value);
			}
		}
		handler.sendEmptyMessage(handler_key.UPDATE_UI.ordinal());
	}
	
	/**
	 * Input alarm to list.(Show number of Alarm)
	 * 
	 * @param json
	 *            the json
	 * @throws JSONException
	 *             the JSON exception
	 */
	private void inputAlarmToList(String json) throws JSONException {
		Log.i("revjson", json);
		JSONObject receive = new JSONObject(json);
		Iterator actions = receive.keys();
		while (actions.hasNext()) {
			Log.i("revjson", "action");
			String action = actions.next().toString();
			DeviceAlarm alarm = new DeviceAlarm(DateUtil.getDateCN(new Date()), action);
			alarmList.add(alarm);
		}
		handler.sendEmptyMessage(handler_key.UPDATE_UI.ordinal());
	}
	
	/**
	 * The handler by device status change
	 */
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			handler_key key = handler_key.values()[msg.what];
			switch (key) {
			case RECEIVED:
				try {
					if (deviceDataMap.get("data") != null) {
						Log.i("info", (String) deviceDataMap.get("data"));
						inputDataToMaps(statuMap,
								(String) deviceDataMap.get("data"));

					}
					alarmList.clear();
					if (deviceDataMap.get("alters") != null) {
						Log.i("info", (String) deviceDataMap.get("alters"));
						// 返回主线程处理报警数据刷新
						inputAlarmToList((String) deviceDataMap.get("alters"));
					}
					if (deviceDataMap.get("faults") != null) {
						Log.i("info", (String) deviceDataMap.get("faults"));
						// 返回主线程处理错误数据刷新
						inputAlarmToList((String) deviceDataMap.get("faults"));
					}
					// 返回主线程处理P0数据刷新
					handler.sendEmptyMessage(handler_key.UPDATE_UI.ordinal());
					handler.sendEmptyMessage(handler_key.ALARM.ordinal());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			case UPDATE_UI:
				if (statuMap != null && statuMap.size() > 0) {
					changeRUNmodeBg(Integer.parseInt(statuMap.get(JsonKeys.FAN_SPEED).toString()));
					setChildLock((Boolean)statuMap.get(JsonKeys.Child_Lock));
					setIndicatorLight((Boolean)statuMap.get(JsonKeys.LED));
					setPlasma((Boolean)statuMap.get(JsonKeys.Plasma));
					setSwitch((Boolean)statuMap.get(JsonKeys.ON_OFF));
					int hourOn = DateUtil.minCastToHour(Integer.parseInt(statuMap.get(JsonKeys.TIME_ON).toString()));
					if (DateUtil.minCastToHourMore(Integer.parseInt(statuMap.get(JsonKeys.TIME_ON).toString())) != 0) {
						hourOn = DateUtil.minCastToHour(Integer.parseInt(statuMap.get(JsonKeys.TIME_ON).toString()))+1;
					}
					setTimingOn(hourOn);
					int hourOff = DateUtil.minCastToHour(Integer.parseInt(statuMap.get(JsonKeys.TIME_OFF).toString()));
					if (DateUtil.minCastToHourMore(Integer.parseInt(statuMap.get(JsonKeys.TIME_OFF).toString())) != 0) {
						hourOff = DateUtil.minCastToHour(Integer.parseInt(statuMap.get(JsonKeys.TIME_OFF).toString()))+1;
					}
					setTimingOff(hourOff);
					updateBackgound(statuMap.get(JsonKeys.Air_Quality).toString());
					int level=0;
					if(statuMap.get(JsonKeys.Air_Quality).toString().equals("1")){
						level=5;
					}else if(statuMap.get(JsonKeys.Air_Quality).toString().equals("2")){
						level=9;
					}else if(statuMap.get(JsonKeys.Air_Quality).toString().equals("3")){
						level=15;
					}
					int result = (int) (level * 8);
					if (result > 100) {
						result = 100;
					}
					float quality = (100 - result) * mW100;
					updateTips(quality);
				}
				break;
			case ALARM:
				//**********华丽的分割线（判断当前是否有最新的alarm）***********
				for (DeviceAlarm alarm : alarmList) {
					if (!isAlarmList.contains(alarm.getDesc())) {
						isAlarmList.add(alarm.getDesc());
						isAlarm=false;
					}
				}
				isAlarmList.clear();
				for (DeviceAlarm alarm : alarmList) {
					isAlarmList.add(alarm.getDesc());
				}
				//*******************************************************
				if (alarmList != null && alarmList.size() > 0) {
					if (!isAlarm) {
						if (mFaultDialog == null) {
							mFaultDialog = DialogManager.getDeviceErrirDialog(
									AirPurActivity.this, "设备故障",
									new OnClickListener() {

										@Override
										public void onClick(View v) {
											Intent intent = new Intent(
													Intent.ACTION_CALL, Uri
															.parse("tel:10086"));
											startActivity(intent);
											mFaultDialog.dismiss();
											mFaultDialog = null;
										}
									});

						}
						mFaultDialog.show();
						isAlarm=true;
					}
					setTipsLayoutVisiblity(true, alarmList.size());
				} else {
					setTipsLayoutVisiblity(false, 0);
				}
				break;
			case DISCONNECTED:
                mCenter.cDisconnect(mXpgWifiDevice);
				break;
			case GET_STATUE:
				mCenter.cGetStatus(mXpgWifiDevice);
				break;
			}
		}
	};
	
	/**
	 * 获取本机所在城市
	 */
	public void initCity(){
		new LocationService() {
			@Override
			public void onSuccess(JSONObject data) {
				// TODO Auto-generated method stub
				try {
					String[] city = data.getString("address").split("\\|");
					Log.e("city-json", ""+data);
					getPm(city[2]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailed() {
				// TODO Auto-generated method stub
				Toast.makeText(AirPurActivity.this, "城市定位失败", Toast.LENGTH_SHORT)
						.show();
			}
		}.startLocation();
	}
	
	/**
	 * 获取PM2.5值
	 */
	public void getPm(String city){
		new GetPMService() {
			
			@Override
			public void onSuccess(JSONObject data) {
				// TODO Auto-generated method stub
				try {
					Log.e("pm", ""+data);
					JSONObject pm=data.getJSONObject("result");
					pm25_tv.setText(pm.getString("pm2_5"));
					pm10_tv.setText(pm.getString("pm10"));
					int aqi = pm.getInt("aqi");
					if (0 < aqi && aqi <= 50) {
						outdoorQuality_tv.setText("优");
					} else if (50 < aqi && aqi <= 100) {
						outdoorQuality_tv.setText("良");
					} else if (101 < aqi && aqi <= 150) {
						outdoorQuality_tv.setText("中");
					} else {
						outdoorQuality_tv.setText("差");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailed() {
				// TODO Auto-generated method stub
				Toast.makeText(AirPurActivity.this, "PM2.5获取失败", Toast.LENGTH_SHORT)
						.show();
			}
		}.GetWeather(city);
	}
}
