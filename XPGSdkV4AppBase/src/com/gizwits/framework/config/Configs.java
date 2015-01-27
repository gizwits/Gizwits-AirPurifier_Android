package com.gizwits.framework.config;

import com.xtremeprog.xpgconnect.XPGWifiSDK.XPGWifiLogLevel;
/**
 * app配置参数
 * @author Lien
 * */
public class Configs {
	// 设定是否为debug版本
	public static final boolean DEBUG = true;
	// 设定AppID，参数为机智云官网中查看产品信息得到的AppID
	public static final String APPID = "31ee082710384a71bec246e1c9695920";
	// 指定该app对应设备的product_key，如果设定了过滤，会过滤出该peoduct_key对应的设备
	public static final String PRODUCT_KEY = "b3a206e380bc4a3c8c157ff4b5491633";
	// 设定日志打印级别
	public static final XPGWifiLogLevel LOG_LEVEL = XPGWifiLogLevel.XPGWifiLogLevelAll;

}
