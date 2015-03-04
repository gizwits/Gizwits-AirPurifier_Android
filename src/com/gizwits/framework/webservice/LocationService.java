package com.gizwits.framework.webservice;

import org.json.JSONObject;

import com.gizwits.framework.config.Configs;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 定位服务，通过百度的lbs 的ip定位服务，获取当前用户的所在城市
 * 
 * @link 
 *       http://api.map.baidu.com/location/ip
 * @author StephenC
 * 
 */
public abstract class LocationService {

	private final String requestUrl = "http://api.map.baidu.com/location/ip?ak="+Configs.LOCATIONAK;

	
	public abstract void onSuccess(JSONObject data);
	public abstract void onFailed();
	
	/**
	 * 启动定位服务
	 */
	public void startLocation(){
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(requestUrl, new JsonHttpResponseHandler(){
			
			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				super.onFailure(arg0, arg1);
				onFailed();
			}
			
			@Override
			public void onSuccess(JSONObject json) {
				LocationService.this.onSuccess(json);
			}
		});
	}
	
}
