package com.gizwits.airpurifier.activity.advanced;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gizwits.aircondition.R;
import com.gizwits.framework.adapter.AlarmListAdapter;
import com.gizwits.framework.entity.DeviceAlarm;

/**
 * 警报
 * 
 * @author hao
 * 
 */
public class AlarmFragment extends Fragment {

	private ListView alarms_lv;
	private AlarmListAdapter adapter;
	private List<DeviceAlarm> infos = new ArrayList<DeviceAlarm>();
	
	public AlarmFragment(AdvancedActivity activity) {
		// TODO Auto-generated constructor stub
		adapter = new AlarmListAdapter(activity, infos);
	}

	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_alarm_layout, null);
		alarms_lv = (ListView) v.findViewById(R.id.alarms_lv);
		alarms_lv.setAdapter(adapter);
		return v;
	}
	
	public void addInfos(List<DeviceAlarm> infos){
		this.infos.clear();
		for (DeviceAlarm deviceAlarm : infos) {
			this.infos.add(deviceAlarm);
		}
		adapter.notifyDataSetInvalidated();
	}
}
