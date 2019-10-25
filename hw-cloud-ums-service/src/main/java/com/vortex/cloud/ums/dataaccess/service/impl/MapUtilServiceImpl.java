package com.vortex.cloud.ums.dataaccess.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vortex.cloud.lbs.dto.BasicLocation;
import com.vortex.cloud.lbs.enums.MapTypeEnum;
import com.vortex.cloud.lbs.ui.service.ILbsFeignClient;
import com.vortex.cloud.ums.dataaccess.service.IMapUtilService;
import com.vortex.cloud.ums.dto.CloudTenantLbsDto;
import com.vortex.cloud.ums.enums.Map2CoordEnum;
import com.vortex.cloud.vfs.common.exception.VortexException;
import com.vortex.cloud.vfs.data.dto.RestResultDto;

@Service("mapUtilService")
public class MapUtilServiceImpl implements IMapUtilService {
	@Resource
	private ILbsFeignClient lbsFeignClient;

	/**
	 * 判断地图类型列表中是否包含某个坐标系
	 * 
	 * @param mapTypes
	 * @param coord
	 * @return
	 */
	public boolean containsCoor(List<String> mapTypes, String coord) {
		if (CollectionUtils.isEmpty(mapTypes)) {
			return false;
		}

		for (String maptype : mapTypes) {
			if (Map2CoordEnum.getValueByKey(maptype).equals(coord)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 将坐标点转化成目标坐标系
	 * 
	 * @param params
	 * @param from
	 * @param to
	 * @return
	 */
	public String getParams(String params, String from, String to) {
		RestResultDto<List<BasicLocation>> rst = lbsFeignClient.coordconvert2(params, from, to);
		List<BasicLocation> list = rst.getData();
		StringBuffer lngLat = new StringBuffer();
		if (CollectionUtils.isNotEmpty(list)) {

			for (int i = 0; i < list.size(); i++) {
				if (i == 0) {
					lngLat.append(list.get(i).getLongitudeDone() + "," + list.get(i).getLatitudeDone());
				} else {
					lngLat.append(";" + list.get(i).getLongitudeDone() + "," + list.get(i).getLatitudeDone());
				}
			}
		}
		return lngLat.toString();
	}

	/**
	 * 根据租户的地图字符串，获取支持的地图类型列表
	 * 
	 * @param mapDefJson
	 * @return
	 */
	public List<String> getMapTypes(String mapDefJson) {
		List<String> rst = new ArrayList<>();

		if (StringUtils.isEmpty(mapDefJson)) {
			rst.add(MapTypeEnum.BMAP.getKey());
			return rst;
		}
		ObjectMapper mapper = new ObjectMapper();
		List<CloudTenantLbsDto> list = null;
		try {
			list = mapper.readValue(mapDefJson, new TypeReference<List<CloudTenantLbsDto>>() {
			});
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// List<CloudTenantLbsDto> list = new JsonMapper().fromJson(mapDefJson,
		// List.class);

		if (CollectionUtils.isEmpty(list)) {
			rst.add(MapTypeEnum.BMAP.getKey());
			return rst;
		}

		for (CloudTenantLbsDto dto : list) {
			rst.add(dto.getMapType());
		}

		return rst;
	}

	/**
	 * 根据租户的地图字符串，找出默认地图
	 * 
	 * @param mapDefJson
	 * @return
	 */
	public String getMapType(String mapDefJson) {
		if (StringUtils.isEmpty(mapDefJson)) {
			return MapTypeEnum.BMAP.getKey();
		}

		List<CloudTenantLbsDto> list = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			list = mapper.readValue(mapDefJson, new TypeReference<List<CloudTenantLbsDto>>() {
			});
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (CollectionUtils.isEmpty(list)) {
			return MapTypeEnum.BMAP.getKey();
		}

		String rst = null;
		for (CloudTenantLbsDto dto : list) {
			if (dto.getDefaultMap()) {
				rst = dto.getMapType();
			}
		}

		if (StringUtils.isEmpty(rst)) {
			rst = MapTypeEnum.BMAP.getKey();
		}

		return rst;
	}

	/**
	 * 根据租户的json字符串得到租户的地图坐标系
	 * 
	 * @param mapDefJson
	 * @return
	 */
	public String getCoordType(String mapDefJson) {
		String mapType = getMapType(mapDefJson);
		if (mapType.equals(MapTypeEnum.ARCGIS.getKey())) {
			List<CloudTenantLbsDto> list = null;
			ObjectMapper mapper = new ObjectMapper();
			try {
				list = mapper.readValue(mapDefJson, new TypeReference<List<CloudTenantLbsDto>>() {
				});
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (CloudTenantLbsDto dto : list) {
				if (MapTypeEnum.ARCGIS.getKey().equals(dto.getMapType())) {
					return dto.getCoordinate();
				}
			}

			throw new VortexException("根据租户地图配置，未查询到arcgis的坐标系信息！");
		} else {
			return Map2CoordEnum.getValueByKey(mapType);
		}
	}

	public String getArcgisCoord(String mapDefJson) {
		List<CloudTenantLbsDto> list = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			list = mapper.readValue(mapDefJson, new TypeReference<List<CloudTenantLbsDto>>() {
			});
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (CloudTenantLbsDto dto : list) {
			if (dto.getMapType().equals(MapTypeEnum.ARCGIS.getKey())) {
				return dto.getCoordinate();
			}
		}
		return null;
	}
}
