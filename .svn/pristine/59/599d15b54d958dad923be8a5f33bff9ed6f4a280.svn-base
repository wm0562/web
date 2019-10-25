package com.vortex.cloud.ums.dataaccess.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JavaType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vortex.cloud.ums.config.ManagerConfig;
import com.vortex.cloud.ums.dataaccess.service.IDeflectService;
import com.vortex.cloud.ums.util.utils.ConnectHttpService;
import com.vortex.cloud.vfs.common.mapper.JsonMapper;
import com.vortex.cloud.vfs.data.dto.RestResultDto;

@Service("deflectService")
@Transactional
public class DeflectServiceImpl implements IDeflectService {
	@Autowired
	private ManagerConfig cfg;
	private static final String REST_FUL = "/vortexapi/rest/lbs/coordconvert/v1";

	@Override
	public String deflect(String params) throws Exception {
		String result = "";

		Map<String, Object> map = Maps.newHashMap();
		map.put("location", params);
		map.put("from", "bd09");
		map.put("to", "wgs84");

		// 请求数据(获取偏转前 经纬度)
		String restResult = ConnectHttpService.callHttp(cfg.getURL_GATEWAY() + REST_FUL, ConnectHttpService.METHOD_GET, map);
		JsonMapper jm = new JsonMapper();
		Map<String, Object> resultData = jm.fromJson(restResult, HashMap.class);

		if (resultData.get("status") == RestResultDto.RESULT_SUCC) {
			List<Map<String, Object>> locations = (List<Map<String, Object>>) resultData.get("locations");

			for (Map<String, Object> location : locations) {
				result += location.get("longitudeDone") + "," + location.get("latitudeDone") + ";";
			}
			result = result.substring(0, result.length() - 1);
		} else {
			result = params;
		}

		return result;
	}

	@Override
	public String deflectToBD(String params) {
		String result = "";

		Map<String, Object> map = Maps.newHashMap();
		map.put("location", params);
		map.put("from", "wgs84");
		map.put("to", "bd09");

		// 请求数据(获取偏转前 经纬度)
		String restResult = ConnectHttpService.callHttp(cfg.getURL_GATEWAY() + REST_FUL, ConnectHttpService.METHOD_GET, map);
		JsonMapper jm = new JsonMapper();
		Map<String, Object> resultData = jm.fromJson(restResult, HashMap.class);

		if (resultData.get("status") == RestResultDto.RESULT_SUCC) {
			List<Map<String, Object>> locations = (List<Map<String, Object>>) resultData.get("locations");

			for (Map<String, Object> location : locations) {
				result += location.get("longitudeDone") + "," + location.get("latitudeDone") + ";";
			}
			result = result.substring(0, result.length() - 1);
		} else {
			result = params;
		}

		return result;
	}
}
