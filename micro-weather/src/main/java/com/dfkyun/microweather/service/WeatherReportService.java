package com.dfkyun.microweather.service;


import com.dfkyun.microweather.vo.Weather;

/**
 * 天气预报服务
 */
public interface WeatherReportService {

	/**
	 * 根据城市ID查询天气信息
	 * 
	 * @param cityId
	 * @return
	 */
	Weather getDataByCityId(String cityId);

}
