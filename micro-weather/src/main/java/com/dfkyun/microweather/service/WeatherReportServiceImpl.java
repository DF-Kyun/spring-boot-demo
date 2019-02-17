package com.dfkyun.microweather.service;

import com.dfkyun.microweather.vo.Weather;
import com.dfkyun.microweather.vo.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 天气预报服务
 */
@Service
public class WeatherReportServiceImpl implements WeatherReportService {

	private final static Logger logger = LoggerFactory.getLogger(WeatherReportServiceImpl.class);

	@Autowired
	private WeatherDataService weatherDataServiceImpl;
	
	@Override
	public Weather getDataByCityId(String cityId) {
		WeatherResponse result = weatherDataServiceImpl.getDataByCityId(cityId);
		return result.getData();
	}

}
