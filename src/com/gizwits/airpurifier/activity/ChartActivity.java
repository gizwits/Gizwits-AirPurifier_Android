package com.gizwits.airpurifier.activity;

import android.app.Activity;
import android.os.Bundle;

import com.gizwits.aircondition.R;
import com.gizwits.airpurifier.activity.slipbar.SlipBarActivity;
import com.gizwits.framework.activity.device.DeviceListActivity;
import com.xpg.common.system.IntentUtils;

/**
 * 图表
 * 
 * @author hao
 * 
 */
public class ChartActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart);
	}
}
