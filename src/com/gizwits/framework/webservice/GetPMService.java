package com.gizwits.framework.webservice;

import org.json.JSONObject;

import com.gizwits.framework.config.Configs;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * 通过机智云接口获取PM2.5
 * 
 * @link 
 *       http://data.gizwits.com/1/pm25
 * @author StephenC
 * 
 */
public abstract class GetPMService {

	private final String requestUrl = "http://data.gizwits.com/1/pm25?area=";

	
	public abstract void onSuccess(JSONObject data);
	public abstract void onFailed();
	
	/**
	 * 启动定位服务
	 */
	public void GetWeather(String city){
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("X-XPG-Application-Id", "c79c8ef6002111e48a9b00163e0e2e0d");
		client.addHeader("X-XPG-REST-API-Key", "c79cd5c8002111e48a9b00163e0e2e0d");
		client.addHeader("Content-Type", "application/json");
		client.get(requestUrl+city, new JsonHttpResponseHandler(){
			
			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				super.onFailure(arg0, arg1);
				onFailed();
			}
			
			@Override
			public void onSuccess(JSONObject json) {
				GetPMService.this.onSuccess(json);
			}
		});
	}
	
}
