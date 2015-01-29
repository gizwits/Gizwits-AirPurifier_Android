/**
 * Project Name:XPGSdkV4AppBase
 * File Name:JsonKeys.java
 * Package Name:com.gizwits.framework.config
 * Date:2015-1-27 14:47:10
 * Copyright (c) 2014~2015 Xtreme Programming Group, Inc.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.gizwits.framework.config;

// TODO: Auto-generated Javadoc
/**
 * 
 * ClassName: Class JsonKeys. <br/>
 * Json对应字段表<br/>
 * 
 * @author Lien
 */
public class JsonKeys {
    /**
     * 产品名
     */
    public final static String PRODUCT_NAME = "智能云净化器";
    
    /** 实体字段名，代表对应的项目. */
    public final static String KEY_ACTION = "entity0";
    /**
     * 开关
     */
    public final static String ON_OFF = "Switch";
    /**
     * 倒计时开机
     */
    public final static String TIME_ON = "CountDown_On_min";
    /**
     * 倒计时关机
     */
    public final static String TIME_OFF = "CountDown_Off_min";
    /**
     * 风速
     * 0.强力, 1.标准, 2.睡眠, 3.智能
     */
    public final static String FAN_SPEED = "Wind_Velocity";
    /**
     * 等离子开关
     */
    public final static String Plasma = "Switch_Plasma";
    /**
     * 空气质量指示灯
     */
    public final static String LED = "LED_Air_Quality";
    /**
     * 儿童安全锁
     */
    public final static String Child_Lock = "Child_Security_Lock";
    /**
     * 滤网寿命
     */
    public final static String Filter_Life = "Filter_Life";
    /**
     * 空气检测灵敏度
     */
    public final static String Air_Sensitivity = "Air_Sensitivity";
   
    
    /**
     * 电机故障
     */
    public final static String Fault_Motor = "Fault_Motor";
    /**
     * 空气传感器故障
     */
    public final static String Fault_Air_Sensors = "Fault_Air_Sensors";
    /**
     * 灰尘传感器故障
     */
    public final static String Fault_Dust_Sensor = "Fault_Dust_Sensor";
    /**
     * 滤芯寿命报警
     */
    public final static String ALARM_FULL = "alert_full";
    
    
    /**
     * Timing_On
     */
    public final static String Timing_On = "" +
    		"Timing_On";
    /**
     * Timing_Off
     */
    public final static String Timing_Off = "Timing_Off";
    /**
     * Alert_Air_Quality
     */
    public final static String Alert_Air_Quality = "Alert_Air_Quality";
    /**
     * Week_Repeat
     */
    public final static String Week_Repeat = "Week_Repeat";

}
