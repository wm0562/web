package com.vortex.cloud.ums.dataaccess.service;

import java.util.List;

public interface IMapUtilService {
	/**
	 * 判断地图类型列表中是否包含某个坐标系
	 * 
	 * @param mapTypes
	 * @param coord
	 * @return
	 */
	public boolean containsCoor(List<String> mapTypes, String coord);

	/**
	 * 将坐标点转化成目标坐标系
	 * 
	 * @param params
	 * @param from
	 * @param to
	 * @return
	 */
	public String getParams(String params, String from, String to);

	/**
	 * 根据租户的地图字符串，获取支持的地图类型列表
	 * 
	 * @param mapDefJson
	 * @return
	 */
	public List<String> getMapTypes(String mapDefJson);

	/**
	 * 根据租户的地图字符串，找出默认地图
	 * 
	 * @param mapDefJson
	 * @return
	 */
	public String getMapType(String mapDefJson);

	/**
	 * 根据租户的json字符串得到租户的地图坐标系
	 * 
	 * @param mapDefJson
	 * @return
	 */
	public String getCoordType(String mapDefJson);

	public String getArcgisCoord(String mapDefJson);
}
