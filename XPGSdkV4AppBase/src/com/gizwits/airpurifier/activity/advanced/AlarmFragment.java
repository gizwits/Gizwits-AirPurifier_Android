package com.gizwits.airpurifier.activity.advanced;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gizwits.aircondition.R;
import com.gizwits.framework.entity.DeviceAlarm;

/**
 * 警报
 * 
 * @author hao
 * 
 */
public class AlarmFragment extends Fragment {

	private ListView alarms_lv;
	private AlarmInfoAdapter adapter;
	private List<DeviceAlarm> infos = new ArrayList<DeviceAlarm>();

	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new AlarmInfoAdapter(getActivity(), infos);
		initData();
	};

	private void initData() {
		infos.clear();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_alarm_layout, null);
		alarms_lv = (ListView) v.findViewById(R.id.alarms_lv);
		alarms_lv.setAdapter(adapter);
		return v;
	}

	private class AlarmInfoAdapter extends ArrayAdapter<DeviceAlarm> {

		private LayoutInflater inflater;

		public AlarmInfoAdapter(Context context, List<DeviceAlarm> objects) {
			super(context, 0, objects);
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			DeviceAlarm info = getItem(position);
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.alarminfo_item, null);
				holder.msg_tv = (TextView) convertView
						.findViewById(R.id.msg_tv);
				holder.time_tv = (TextView) convertView
						.findViewById(R.id.time_tv);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.msg_tv.setText(info.getDesc());
			holder.time_tv.setText(info.getTime());
			return convertView;
		}

	}

	private static class ViewHolder {
		TextView time_tv;
		TextView msg_tv;
	}
}
