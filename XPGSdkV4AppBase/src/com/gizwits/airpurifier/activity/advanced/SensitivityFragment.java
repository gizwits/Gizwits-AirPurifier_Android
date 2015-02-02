package com.gizwits.airpurifier.activity.advanced;

import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.gizwits.aircondition.R;

/**
 * 灵敏度
 * 
 * @author hao
 * 
 */
public class SensitivityFragment extends Fragment {
	private AdvancedActivity activity;
	
	private SeekBar sensitive_seek;
	private TextView showLevel_tv;
	private float position;
	
	public SensitivityFragment(AdvancedActivity activity){
		this.activity=activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_sensitivity_layout, null);
		sensitive_seek = (SeekBar) v.findViewById(R.id.sensitive_seek);
		showLevel_tv = (TextView) v.findViewById(R.id.showLevel_tv);
		// position
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;// 宽度height = dm.heightPixels ;//高度
		if (position == 0) {
			position = width / 4;
		}
		initSeekBar();
		changeSensi(0);
		return v;
	}

	public void initSeekBar() {
		sensitive_seek.setEnabled(true);
		sensitive_seek
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
						int prog = seekBar.getProgress();
						if (prog == 0) {
							activity.sendSensitivityLv(1);
						} else {
							activity.sendSensitivityLv(prog);
						}
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {

						handleTips(seekBar);
					}
				});

	}

	@Override
	public void onResume() {
		super.onResume();
		handleTips(sensitive_seek);
	}

	@SuppressLint("NewApi")
	private void handleTips(SeekBar seekBar) {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		boolean isExitMethod = false;
		try {
			Class<?> cl = Class.forName(SeekBar.class.getName());
			Method[] methods = cl.getMethods();
			for (int i = 0; i < methods.length; i++) {
				if ("getThumb".equals(methods[i].getName())) {
					isExitMethod = true;
					break;
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Drawable d = null;
		if (isExitMethod) {
			d = seekBar.getThumb();
		} else {
			return;
		}
		Rect r = d.getBounds();
		int progress = seekBar.getProgress();
		if (progress == 0) {
			seekBar.setProgress(1);
			showLevel_tv.setText("一档");
		} else if (progress == 1) {
			showLevel_tv.setText("一档");
		} else if (progress == 2) {
			showLevel_tv.setText("二档");
		} else if (progress == 3) {
			showLevel_tv.setText("三档");
		} else if (progress == 4) {
			showLevel_tv.setText("四档");
		}

		params.leftMargin = r.left + r.width() / 4;
		if (params.leftMargin <= 0) {
			params.leftMargin = (int) position;
		} else {
			position = params.leftMargin;
		}
		showLevel_tv.setLayoutParams(params);
	}

	public void changeSensi(int level) {
		if (sensitive_seek == null)
			return;
		sensitive_seek.setProgress(level);

	}
}
