package com.gizwits.airpurifier.activity.advanced;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gizwits.aircondition.R;
import com.gizwits.framework.activity.BaseActivity;
import com.gizwits.framework.config.JsonKeys;
import com.gizwits.framework.entity.AdvanceType;
import com.gizwits.framework.entity.DeviceAlarm;
import com.xpg.common.useful.DateUtil;
import com.xtremeprog.xpgconnect.XPGWifiDevice;

/**
 * 高级功能
 * 
 * @author hao
 * 
 */
public class AdvancedActivity extends BaseActivity implements
		OnClickListener {
	private String TAG="AdvancedActivity";
	
	private TextView title_tv;
	private ImageView ivLeft;

	//三个fragment
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	private SensitivityFragment sensitivityFragment;
	private AlarmFragment alarmFragment;
	private RoseboxFragment roseboxFragment;
	
	//顶部按钮
	private Button sensitivity_btn;
	private Button rosebox_btn;
	private Button alarm_btn;
	
	/** The device data map. */
	private ConcurrentHashMap<String, Object> deviceDataMap;
	/** The statu map. */
	private ConcurrentHashMap<String, Object> statuMap;
	/** The alarm list. */
	private ArrayList<DeviceAlarm> alarmList;
	/** Get Current */
	private CurrentView currentFragment;

	private enum CurrentView {
		sensitivity, rosebox, alarm
	}
	
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.advanced_layout);
		mXpgWifiDevice.setListener(deviceListener);
		statuMap = new ConcurrentHashMap<String, Object>();//设备状态数据
		alarmList = new ArrayList<DeviceAlarm>();//警报状态数据
		initUI();
		initFragment();

	}

	@Override
	public void onResume() {
		super.onResume();
		AdvanceType at = (AdvanceType) getIntent().getSerializableExtra(
				"advanced_set");
		mCenter.cGetStatus(mXpgWifiDevice);
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
		title_tv = (TextView) findViewById(R.id.ivTitle);
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

	/**
	 * change fragment
	 * @param id
	 */
	private void changeView(CurrentView id) {
		currentFragment=id;
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

	/**
	 * init Fragment
	 */
	private void initFragment() {
		// 开启事物，添加第一个fragment
		fragmentManager = getFragmentManager();
		sensitivityFragment = new SensitivityFragment(this);
		roseboxFragment = new RoseboxFragment(this);
		alarmFragment = new AlarmFragment(this);
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
	
	//device data request
	@Override
	protected void didReceiveData(XPGWifiDevice device,
			ConcurrentHashMap<String, Object> dataMap, int result) {
		this.deviceDataMap = dataMap;
		handler.sendEmptyMessage(handler_key.RECEIVED.ordinal());
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
					sensitivityFragment.changeSensi(Integer.parseInt(statuMap.get(JsonKeys.Air_Sensitivity).toString()));
					if (currentFragment == CurrentView.rosebox) {
						roseboxFragment.updateStatus(Integer.parseInt(statuMap.get(JsonKeys.Filter_Life).toString()));
					}
					roseboxFragment.setCurrent(Integer.parseInt(statuMap.get(JsonKeys.Filter_Life).toString()));
				}
				break;
			case ALARM:
				alarmFragment.addInfos(alarmList);
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
	 * Input alarm to list.
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

			String action = actions.next().toString();
			Log.i("revjson", "action=" + action);
			DeviceAlarm alarm = new DeviceAlarm(DateUtil.getDateCN(new Date()), action);
			alarmList.add(alarm);
		}
		handler.sendEmptyMessage(handler_key.ALARM.ordinal());
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		finish();
	}
	
	/**
	 * SensitivityFragment change lv function
	 * @param level
	 */
	public void sendSensitivityLv(int lv){
		mCenter.cAirSensitivity(mXpgWifiDevice, lv);
	}
	

	/**
	 * ReseboxFragment reset function
	 * @param level
	 */
	public void resetRosebox() {
		mCenter.cResetLife(mXpgWifiDevice);
	}
}
