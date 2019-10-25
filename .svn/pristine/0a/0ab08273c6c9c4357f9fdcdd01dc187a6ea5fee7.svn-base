package com.vortex.cloud.ums.dataaccess.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vortex.cloud.lbs.dto.BasicLocation;
import com.vortex.cloud.lbs.enums.MapTypeEnum;
import com.vortex.cloud.lbs.ui.service.ILbsFeignClient;
import com.vortex.cloud.ums.dataaccess.dao.ICloudStaffDao;
import com.vortex.cloud.ums.dataaccess.dao.ITenantDao;
import com.vortex.cloud.ums.dataaccess.dao.ITenantDivisionDao;
import com.vortex.cloud.ums.dataaccess.service.ICloudOrganizationService;
import com.vortex.cloud.ums.dataaccess.service.IMapUtilService;
import com.vortex.cloud.ums.dataaccess.service.ITenantDivisionService;
import com.vortex.cloud.ums.dto.CloudTenantLbsDto;
import com.vortex.cloud.ums.dto.IdNameDto;
import com.vortex.cloud.ums.dto.TenantDivisionDto;
import com.vortex.cloud.ums.enums.CloudDivisionLevelEnum;
import com.vortex.cloud.ums.enums.KafkaTopicEnum;
import com.vortex.cloud.ums.enums.Map2CoordEnum;
import com.vortex.cloud.ums.enums.SyncFlagEnum;
import com.vortex.cloud.ums.model.CloudStaff;
import com.vortex.cloud.ums.model.Tenant;
import com.vortex.cloud.ums.model.TenantDivision;
import com.vortex.cloud.ums.util.utils.kafka.KafkaProducer;
import com.vortex.cloud.vfs.common.exception.ServiceException;
import com.vortex.cloud.vfs.common.exception.VortexException;
import com.vortex.cloud.vfs.common.mapper.JsonMapper;
import com.vortex.cloud.vfs.data.dto.RestResultDto;
import com.vortex.cloud.vfs.data.hibernate.repository.HibernateRepository;
import com.vortex.cloud.vfs.data.hibernate.service.SimplePagingAndSortingService;
import com.vortex.cloud.vfs.data.support.SearchFilter;
import com.vortex.cloud.vfs.data.support.SearchFilter.Operator;

@SuppressWarnings("all")
@Service("tenantDivisionService")
@Transactional
public class TenantDivisionServiceImpl extends SimplePagingAndSortingService<TenantDivision, String> implements ITenantDivisionService {

	private Logger logger = LoggerFactory.getLogger(TenantDivisionServiceImpl.class);

	@Resource
	private ITenantDivisionDao tenantDivisionDao;

	@Resource
	private ICloudOrganizationService cloudOrganizationService;

	@Resource
	private ICloudStaffDao cloudStaffDao;

	@Resource
	private ITenantDao tenantDao;

	@Resource
	private ILbsFeignClient lbsFeignClient;

	@Resource
	private IMapUtilService mapUtilService;

	@Override
	public HibernateRepository<TenantDivision, String> getDaoImpl() {
		return tenantDivisionDao;
	}

	@Override
	public TenantDivision save(TenantDivisionDto dto) {
		Tenant tenant = this.tenantDao.findOne(dto.getTenantId());
		if (tenant == null) {
			throw new VortexException("新增租户行政区划时，传入租户id对应的租户信息为空！");
		}

		Double longitude = null;
		Double latitude = null;
		String tenantMapType = null; // 租户默认地图类型
		String tenantMapcoord = null; // 租户默认坐标系
		String currMapType = null; // 当前地图类型
		String currCoord = null; // 当前坐标系
		if (longitude != null && latitude != null || StringUtils.isNoneEmpty(dto.getScope())) {
			longitude = dto.getLongitude();
			latitude = dto.getLatitude();
			tenantMapType = mapUtilService.getMapType(tenant.getMapDefJson());
			tenantMapcoord = mapUtilService.getCoordType(tenant.getMapDefJson());
			currMapType = dto.getMapType();

			if (MapTypeEnum.ARCGIS.getKey().equals(currMapType)) {
				currCoord = mapUtilService.getArcgisCoord(tenant.getMapDefJson());
				if (StringUtils.isEmpty(currCoord)) {
					logger.error("租户中(id=" + tenant.getId() + ")定义的arcgis坐标系为空！");
					throw new VortexException("租户中(id=" + tenant.getId() + ")定义的arcgis坐标系为空！");
				}
			} else {
				currCoord = Map2CoordEnum.getValueByKey(currMapType);
			}
		}

		// 处理中心点的坐标转化
		if (longitude != null && latitude != null) {
			try {
				if (!tenantMapType.equals(dto.getMapType())) { // 如果上来的数据的地图类型和租户默认的地图类型不一致，则调用lbs转化点
					RestResultDto<List<BasicLocation>> rst = lbsFeignClient.coordconvert2(longitude + "," + latitude, currCoord, tenantMapcoord);
					dto.setLngLats(rst.getData().get(0).getLongitudeDone() + "," + rst.getData().get(0).getLatitudeDone());
				}
			} catch (Exception e) {
				logger.error("保存行政区划时，lbs转化中心点出错！");
			}

			dto.setLngLats(longitude + "," + latitude);
		}

		// 转化scope字段
		if (StringUtils.isNotEmpty(dto.getScope())) {
			// 将scope转为默认地图，为了不影响老功能和老接口的使用者
			try {
				if (!tenantMapType.equals(currMapType)) {
					RestResultDto<List<BasicLocation>> rst = lbsFeignClient.coordconvert2(dto.getScope(), currCoord, tenantMapcoord);
					List<BasicLocation> list = rst.getData();
					if (CollectionUtils.isNotEmpty(list)) {
						StringBuffer lngLat = new StringBuffer();
						for (int i = 0; i < list.size(); i++) {
							if (i == 0) {
								lngLat.append(list.get(i).getLongitudeDone() + "," + list.get(i).getLatitudeDone());
							} else {
								lngLat.append(";" + list.get(i).getLongitudeDone() + "," + list.get(i).getLatitudeDone());
							}
						}
						dto.setScope(lngLat.toString());
					}
				}
			} catch (Exception e) {
				logger.error("保存行政区划时，lbs转化区域点出错！");
			}
		}

		TenantDivision entity = new TenantDivision();
		BeanUtils.copyProperties(dto, entity);

		entity = tenantDivisionDao.save(entity);

		// 发送kafka消息
		try {
			this.sendMsgToKafka(KafkaTopicEnum.UMS_TENANT_DIVISION_SYNC.getKey(), SyncFlagEnum.ADD.getKey(), entity);
		} catch (Exception e) {
			logger.error("新增租户行政区划时发送kafka消息出错：" + entity.toString());
		}

		return entity;
	}

	/**
	 * 添删改的时候发送kafka消息
	 * 
	 * @param topic
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	private void sendMsgToKafka(String topic, String key, Object value) throws Exception {
		KafkaProducer.getInstance().produce(topic, key, value);
	}

	@Override
	public TenantDivisionDto getById(String id) {
		TenantDivision entity = tenantDivisionDao.findOne(id);
		if (entity == null) {
			return null;
		}

		TenantDivisionDto dto = new TenantDivisionDto();
		BeanUtils.copyProperties(entity, dto);

		this.setLevelText(dto);

		// 分离出经纬度
		this.setLngLat(dto);

		return dto;
	}

	/**
	 * 设置行政级别的描述文本
	 * 
	 * @param dto
	 */
	private void setLevelText(TenantDivisionDto dto) {
		dto.setLevelText(CloudDivisionLevelEnum.getTextByValue(dto.getLevel()));
	}

	/**
	 * 分离出经纬度
	 * 
	 * @param dto
	 */
	private void setLngLat(TenantDivisionDto dto) {
		String lngLats = dto.getLngLats();
		if (StringUtils.isNotBlank(lngLats)) {

			String[] lngLatArr = lngLats.split(",");
			if (ArrayUtils.isEmpty(lngLatArr) || lngLatArr.length != 2) {
				logger.error("getById(),经纬度为非法字符串，不能解析");
				throw new ServiceException("经纬度为非法字符串，不能解析");
			}
			String lng = lngLatArr[0];
			String lat = lngLatArr[1];
			if (StringUtils.isNotBlank(lng) && StringUtils.isNotBlank(lat)) {
				dto.setLongitude(Double.parseDouble(lng));
				dto.setLatitude(Double.parseDouble(lat));
			}
		}
	}

	@Override
	public List<TenantDivision> getAllChildren(TenantDivision parent) {
		return tenantDivisionDao.getAllChildren(parent);
	}

	@Override
	public long deleteByIdArr(String[] ids, boolean casecade) {
		long deleted = 0;
		if (ArrayUtils.isEmpty(ids)) {
			return deleted;
		}

		for (String id : ids) {
			if (!casecade) {
				deleted += this.deleteNotWithDescendant(id);
			} else {
				deleted += this.deleteWithDescendant(id);
			}
		}

		return deleted;
	}

	/**
	 * 只删除自身，不级联删除后代节点
	 * 
	 * @param ids
	 * @return
	 */
	private long deleteNotWithDescendant(String id) {

		List<SearchFilter> filterList = new ArrayList<SearchFilter>();
		filterList.add(new SearchFilter("parentId", Operator.EQ, id));

		List<TenantDivision> childList = super.findListByFilter(filterList, null);
		if (CollectionUtils.isNotEmpty(childList)) {
			logger.warn("deleteNotWithDescendant，不是叶子节点，不能删除行政区划：id=" + id);
			return 0;
		}

		tenantDivisionDao.delete(id);
		return 1;
	}

	/**
	 * 删除自身，同时级联删除后代节点
	 * 
	 * @param ids
	 * @return
	 */
	private int deleteWithDescendant(String id) {
		List<String> childIdList = this.getChildIdList(id);
		if (CollectionUtils.isNotEmpty(childIdList)) {
			tenantDivisionDao.deleteByIds(childIdList.toArray(new String[childIdList.size()]));
		}

		tenantDivisionDao.delete(id);
		return 1;
	}

	/**
	 * 获取后代记录ID
	 * 
	 * @param parentId
	 * @return
	 */
	private List<String> getChildIdList(String parentId) {

		// 获取儿子节点
		List<SearchFilter> filterList = new ArrayList<SearchFilter>();
		filterList.add(new SearchFilter("parentId", Operator.EQ, parentId));

		List<TenantDivision> sonList = super.findListByFilter(filterList, new Sort(Direction.ASC, "orderIndex"));

		if (CollectionUtils.isEmpty(sonList)) {
			return null;
		}

		List<String> resultList = new ArrayList<String>();
		List<String> grandsonList = null;
		for (TenantDivision son : sonList) {
			// 添加儿子节点
			resultList.add(son.getId());

			// 递归：获取孙子节点
			grandsonList = this.getChildIdList(son.getId());
			if (CollectionUtils.isEmpty(grandsonList)) {
				continue;
			}
			// 添加孙子节点
			resultList.addAll(grandsonList);
		}

		return resultList;
	}

	@Override
	public TenantDivision update(TenantDivisionDto dto) {
		Tenant tenant = this.tenantDao.findOne(dto.getTenantId());
		if (tenant == null) {
			throw new VortexException("新增租户行政区划时，传入租户id对应的租户信息为空！");
		}

		Double longitude = null;
		Double latitude = null;
		String tenantMapType = null; // 租户默认地图类型
		String tenantMapcoord = null; // 租户默认坐标系
		String currMapType = null; // 当前地图类型
		String currCoord = null; // 当前坐标系
		if (longitude != null && latitude != null || StringUtils.isNoneEmpty(dto.getScope())) {
			longitude = dto.getLongitude();
			latitude = dto.getLatitude();
			tenantMapType = mapUtilService.getMapType(tenant.getMapDefJson());
			tenantMapcoord = mapUtilService.getCoordType(tenant.getMapDefJson());
			currMapType = StringUtils.isEmpty(dto.getMapType()) ? tenantMapType : dto.getMapType();

			if (MapTypeEnum.ARCGIS.getKey().equals(currMapType)) {
				currCoord = this.getArcgisCoord(tenant.getMapDefJson());
				if (StringUtils.isEmpty(currCoord)) {
					logger.error("租户中(id=" + tenant.getId() + ")定义的arcgis坐标系为空！");
					throw new VortexException("租户中(id=" + tenant.getId() + ")定义的arcgis坐标系为空！");
				}
			} else {
				currCoord = Map2CoordEnum.getValueByKey(currMapType);
			}
		}

		// 处理中心点的坐标转化
		if (longitude != null && latitude != null) {
			try {
				if (!tenantMapType.equals(dto.getMapType())) { // 如果上来的数据的地图类型和租户默认的地图类型不一致，则调用lbs转化点
					RestResultDto<List<BasicLocation>> rst = lbsFeignClient.coordconvert2(longitude + "," + latitude, currCoord, tenantMapcoord);
					dto.setLngLats(rst.getData().get(0).getLongitudeDone() + "," + rst.getData().get(0).getLatitudeDone());
				}
			} catch (Exception e) {
				logger.error("保存行政区划时，lbs转化中心点出错！");
			}

			dto.setLngLats(longitude + "," + latitude);
		}

		// 转化scope字段
		if (StringUtils.isNotEmpty(dto.getScope())) {
			// 将scope转为默认地图，为了不影响老功能和老接口的使用者
			try {
				if (!tenantMapType.equals(currMapType)) {
					RestResultDto<List<BasicLocation>> rst = lbsFeignClient.coordconvert2(dto.getScope(), currCoord, tenantMapcoord);
					List<BasicLocation> list = rst.getData();
					if (CollectionUtils.isNotEmpty(list)) {
						StringBuffer lngLat = new StringBuffer();
						for (int i = 0; i < list.size(); i++) {
							if (i == 0) {
								lngLat.append(list.get(i).getLongitudeDone() + "," + list.get(i).getLatitudeDone());
							} else {
								lngLat.append(";" + list.get(i).getLongitudeDone() + "," + list.get(i).getLatitudeDone());
							}
						}
						dto.setScope(lngLat.toString());
					}
				}
			} catch (Exception e) {
				logger.error("保存行政区划时，lbs转化区域点出错！");
			}
		}

		TenantDivision division = tenantDivisionDao.findOne(dto.getId());
		division.setAbbr(dto.getAbbr());
		division.setCommonCode(dto.getCommonCode());
		division.setLevel(dto.getLevel());
		division.setLngLats(dto.getLngLats());
		division.setName(dto.getName());
		division.setStartTime(dto.getStartTime());
		division.setScope(dto.getScope());
		division.setOrderIndex(dto.getOrderIndex());

		// 发送kafka消息
		try {
			this.sendMsgToKafka(KafkaTopicEnum.UMS_TENANT_DIVISION_SYNC.getKey(), SyncFlagEnum.UPDATE.getKey(), division);
		} catch (Exception e) {
			logger.error("更新租户行政区划时发送kafka消息出错：" + division.toString());
		}

		return tenantDivisionDao.update(division);
	}

	private String getArcgisCoord(String mapDefJson) {
		List<CloudTenantLbsDto> list = new JsonMapper().fromJson(mapDefJson, List.class);
		for (CloudTenantLbsDto dto : list) {
			if (dto.getMapType().equals(MapTypeEnum.ARCGIS.getKey())) {
				return dto.getCoordinate();
			}
		}
		return null;
	}

	@Override
	public List<TenantDivision> findTenantDivisionList(TenantDivisionDto tenantDivision) {
		// 取租户下的所有节点
		List<SearchFilter> filterList = new ArrayList<SearchFilter>();
		filterList.add(new SearchFilter("tenantId", Operator.EQ, tenantDivision.getTenantId()));
		filterList.add(new SearchFilter("enabled", Operator.EQ, TenantDivision.ENABLED_YES));
		// 不传或者传0就不包含root，传1包含root
		if (StringUtils.isBlank(tenantDivision.getContainsRoot()) || TenantDivisionDto.CONTAIN_ROOT_NO.equals(tenantDivision.getContainsRoot())) {
			filterList.add(new SearchFilter("isRoot", Operator.EQ, TenantDivision.ROOT_NOT));
		}
		if (StringUtils.isNotBlank(tenantDivision.getParentId())) {
			filterList.add(new SearchFilter("parentId", Operator.EQ, tenantDivision.getParentId()));
		}
		if (tenantDivision.getLevel() != null) {
			filterList.add(new SearchFilter("level", Operator.EQ, tenantDivision.getLevel()));
		}
		Sort sort = new Sort(Direction.ASC, "orderIndex", "commonCode");

		return this.findListByFilter(filterList, sort);
	}

	@Override
	public List<TenantDivision> findTenantDivisionListWithRoot(TenantDivision tenantDivision) {
		// 取租户下的所有节点
		List<SearchFilter> filterList = new ArrayList<SearchFilter>();
		filterList.add(new SearchFilter("tenantId", Operator.EQ, tenantDivision.getTenantId()));
		filterList.add(new SearchFilter("enabled", Operator.EQ, TenantDivision.ENABLED_YES));
		if (StringUtils.isNotBlank(tenantDivision.getParentId())) {
			filterList.add(new SearchFilter("parentId", Operator.EQ, tenantDivision.getParentId()));
		}
		if (tenantDivision.getLevel() != null) {
			filterList.add(new SearchFilter("level", Operator.EQ, tenantDivision.getLevel()));
		}
		Sort sort = new Sort(Direction.ASC, "orderIndex", "commonCode");

		return this.findListByFilter(filterList, sort);
	}

	@Override
	public Map<String, String> getDivisionNamesByIds(List<String> ids) {
		Map<String, String> map = Maps.newHashMap();
		List<TenantDivision> list = tenantDivisionDao.findAllByIds(ids.toArray(new String[ids.size()]));
		if (CollectionUtils.isNotEmpty(list)) {
			for (TenantDivision tenantDivision : list) {
				map.put(tenantDivision.getId(), tenantDivision.getName());
			}

		}
		return map;
	}

	@Override
	public Map<String, String> getDivisionIdsByNames(List<String> names, String tenantId) {
		Map<String, String> map = Maps.newHashMap();
		List<SearchFilter> searchFilters = Lists.newArrayList();
		searchFilters.add(new SearchFilter("tenantId", Operator.EQ, tenantId));
		if (CollectionUtils.isNotEmpty(names)) {
			searchFilters.add(new SearchFilter("name", Operator.IN, names.toArray()));
		}
		List<TenantDivision> list = tenantDivisionDao.findListByFilter(searchFilters, null);
		if (CollectionUtils.isNotEmpty(list)) {
			for (TenantDivision tenantDivision : list) {
				map.put(tenantDivision.getName(), tenantDivision.getId());
			}
		}
		return map;
	}

	@Override
	public List<TenantDivision> getChildren(String parentId) {
		return this.tenantDivisionDao.getAllChildren(parentId);
	}

	@Override
	public List<TenantDivision> getByLevel(String tenantId, Integer level) throws Exception {
		if (StringUtils.isEmpty(tenantId) || level == null) {
			logger.error("查询租户的某级行政区划列表时，传入的参数非法");
			throw new VortexException("查询租户的某级行政区划列表时，传入的参数非法");
		}
		List<SearchFilter> filterList = new ArrayList<SearchFilter>();
		filterList.add(new SearchFilter("tenantId", Operator.EQ, tenantId));
		filterList.add(new SearchFilter("enabled", Operator.EQ, TenantDivision.ENABLED_YES));
		filterList.add(new SearchFilter("level", Operator.EQ, level));

		Sort sort = new Sort(Direction.ASC, "orderIndex", "commonCode");

		return this.findListByFilter(filterList, sort);
	}

	@Override
	public List<TenantDivision> getAllByLevel(String tenantId, Integer level) {
		if (StringUtils.isEmpty(tenantId)) {
			String error_msg = "查询租户的某级行政区划列表时，传入的参数非法";
			logger.error(error_msg);
			throw new VortexException(error_msg);
		}
		List<SearchFilter> filterList = new ArrayList<SearchFilter>();
		filterList.add(new SearchFilter("tenantId", Operator.EQ, tenantId));
		filterList.add(new SearchFilter("enabled", Operator.EQ, TenantDivision.ENABLED_YES));
		if (null != level) {
			filterList.add(new SearchFilter("level", Operator.LTE, level));
		}
		Sort sort = new Sort(Direction.ASC, "orderIndex", "commonCode");

		return this.findListByFilter(filterList, sort);
	}

	@Override
	public LinkedHashMap<String, String> getDivisionsByNames(String tenantId, List<String> names) throws Exception {
		if (StringUtils.isEmpty(tenantId) || CollectionUtils.isEmpty(names)) {
			logger.error("根据行政区划名称列表查询id列表时，传入的参数非法");
			throw new VortexException("根据行政区划名称列表查询id列表时，传入的参数非法");
		}
		List<IdNameDto> list = this.tenantDivisionDao.getDivisionsByNames(tenantId, names);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}

		LinkedHashMap<String, String> rst = Maps.newLinkedHashMap();
		for (int i = 0; i < list.size(); i++) {
			rst.put(list.get(i).getName(), list.get(i).getId());
		}

		return rst;
	}

	@Override
	public List<TenantDivisionDto> listByPermission(String userId) throws Exception {
		if (StringUtils.isEmpty(userId)) {
			return null;
		}

		CloudStaff staff = cloudStaffDao.getStaffByUserId(userId);
		if (staff == null) {
			return null;
		}

		List<String> orgids = this.cloudOrganizationService.getCompanyIdsWithPermission(userId, staff.getTenantId());
		if (CollectionUtils.isEmpty(orgids)) {
			return null;
		}

		return this.tenantDivisionDao.listDivisionByOrgIds(orgids);
	}
}
