package com.dfkyun.microweather.service;


import com.dfkyun.microweather.vo.City;

import java.util.List;

/**
 * 城市数据服务接口.
 */
public interface CityDataService {

	/**
	 * 获取城市列表.
	 * 
	 * @return
	 * @throws Exception
	 */
	List<City> listCity() throws Exception;
}
