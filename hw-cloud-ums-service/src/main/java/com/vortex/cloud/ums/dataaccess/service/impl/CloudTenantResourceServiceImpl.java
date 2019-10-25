package com.vortex.cloud.ums.dataaccess.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.vortex.cloud.ums.dataaccess.dao.ICloudRoleDao;
import com.vortex.cloud.ums.dataaccess.dao.ICloudTenantResourceDao;
import com.vortex.cloud.ums.dataaccess.service.ICloudTenantResourceService;
import com.vortex.cloud.ums.model.CloudTenantResource;
import com.vortex.cloud.vfs.common.lang.StringUtil;
import com.vortex.cloud.vfs.data.hibernate.repository.HibernateRepository;
import com.vortex.cloud.vfs.data.hibernate.service.SimplePagingAndSortingService;
import com.vortex.cloud.vfs.data.support.SearchFilter;
import com.vortex.cloud.vfs.data.support.SearchFilter.Operator;

@Transactional
@Service("cloudTenantResourceService")
public class CloudTenantResourceServiceImpl extends SimplePagingAndSortingService<CloudTenantResource, String>
		implements ICloudTenantResourceService {

	@Resource
	private ICloudTenantResourceDao cloudTenantResourceDao;
	@Resource
	private ICloudRoleDao cloudRoleDao;

	@Override
	public HibernateRepository<CloudTenantResource, String> getDaoImpl() {
		return cloudTenantResourceDao;
	}

	@Override
	public void saveList(String tenantId, String tenantCode, String roleId, String roleCode, String resourceTypeCode,
			String resourceIdList) {
		if (StringUtil.isNullOrEmpty(tenantId) || StringUtil.isNullOrEmpty(roleId)
				|| StringUtil.isNullOrEmpty(resourceIdList)) {
			return;
		}
		// 去除錯誤數據
		List<String> _resourceId_list = Arrays.asList(StringUtil.splitComma(resourceIdList));
		List<String> resourceId_list = getCleanIds(_resourceId_list);
		// 先查询这个租户，这个角色，这个资源类型下已有的数据
		List<String> resourceId_list_exist = Lists.newArrayList();
		List<SearchFilter> searchFilters = Lists.newArrayList();
		searchFilters.add(new SearchFilter("roleId", Operator.EQ, roleId));
		searchFilters.add(new SearchFilter("tenantId", Operator.EQ, tenantId));
		if (!StringUtil.isNullOrEmpty(resourceTypeCode)) {
			searchFilters.add(new SearchFilter("resourceTypeCode", Operator.EQ, resourceTypeCode));
		}
		List<CloudTenantResource> list = this.findListByFilter(searchFilters, null);
		if (CollectionUtils.isNotEmpty(list)) {
			for (CloudTenantResource entity : list) {
				if (StringUtil.isNullOrEmpty(entity.getResourceId())) {
					continue;
				}
				if (resourceId_list_exist.contains(entity.getResourceId())) {
					continue;
				}
				resourceId_list_exist.add(entity.getResourceId());
			}
		}
		List<CloudTenantResource> saveList = Lists.newArrayList();
		CloudTenantResource entity = null;
		for (String resourceId : resourceId_list) {
			if (resourceId_list_exist.contains(resourceId)) {// 若资源已存在则不新增
				continue;
			}
			entity = new CloudTenantResource();
			entity.setTenantId(tenantId); // 租户id
			entity.setTenantCode(tenantCode); // 租户code
			entity.setRoleCode(roleCode); // 角色code
			entity.setRoleId(roleId); // 角色id
			entity.setResourceTypeCode(resourceTypeCode); // 资源类型code
			entity.setResourceId(resourceId); // 资源id
			saveList.add(entity);
		}
		if (CollectionUtils.isEmpty(saveList)) {
			return;
		}
		this.save(saveList);
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean hasResourceByUserId(String userId, String resourceTypeCode, String resourceId) {
		Boolean returnValue = false;
		if (StringUtil.isNullOrEmpty(userId) || StringUtil.isNullOrEmpty(resourceId)
				|| StringUtil.isNullOrEmpty(resourceTypeCode)) {
			return returnValue;
		}
		// 根据用户id，获取其所拥有的角色id
		List<String> _roleIds = cloudRoleDao.getRoleIdsByUserId(userId);
		List<String> roleIds = getCleanIds(_roleIds);
		if (CollectionUtils.isEmpty(roleIds)) {
			return returnValue;
		}
		List<SearchFilter> searchFilters = Lists.newArrayList();
		searchFilters
				.add(new SearchFilter("roleId", Operator.IN, (String[]) roleIds.toArray(new String[roleIds.size()])));
		searchFilters.add(new SearchFilter("resourceId", Operator.EQ, resourceId));
		searchFilters.add(new SearchFilter("resourceTypeCode", Operator.EQ, resourceTypeCode));
		List<CloudTenantResource> list = this.findListByFilter(searchFilters, null);
		if (CollectionUtils.isNotEmpty(list)) {
			returnValue = true;
		}
		return returnValue;
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean hasResourceByRoleId(String roleId, String resourceTypeCode, String resourceId) {
		Boolean returnValue = false;
		if (StringUtil.isNullOrEmpty(roleId) || StringUtil.isNullOrEmpty(resourceId)
				|| StringUtil.isNullOrEmpty(resourceTypeCode)) {
			return returnValue;
		}
		List<SearchFilter> searchFilters = Lists.newArrayList();
		searchFilters.add(new SearchFilter("roleId", Operator.EQ, roleId));
		searchFilters.add(new SearchFilter("resourceId", Operator.EQ, resourceId));
		searchFilters.add(new SearchFilter("resourceTypeCode", Operator.EQ, resourceTypeCode));
		List<CloudTenantResource> list = this.findListByFilter(searchFilters, null);
		if (CollectionUtils.isNotEmpty(list)) {
			returnValue = true;
		}
		return returnValue;
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> getResourceIdListByUserId(String userId, String resourceTypeCode) {
		List<String> returnValue = Lists.newArrayList();
		if (StringUtil.isNullOrEmpty(userId) || StringUtil.isNullOrEmpty(resourceTypeCode)) {
			return returnValue;
		}
		// 根据用户id，获取其所拥有的角色id
		List<String> _roleIds = cloudRoleDao.getRoleIdsByUserId(userId);
		List<String> roleIds = getCleanIds(_roleIds);
		if (CollectionUtils.isEmpty(roleIds)) {
			return returnValue;
		}
		List<SearchFilter> searchFilters = Lists.newArrayList();
		searchFilters
				.add(new SearchFilter("roleId", Operator.IN, (String[]) roleIds.toArray(new String[roleIds.size()])));
		searchFilters.add(new SearchFilter("resourceTypeCode", Operator.EQ, resourceTypeCode));
		List<CloudTenantResource> list = this.findListByFilter(searchFilters, null);
		if (CollectionUtils.isEmpty(list)) {
			return returnValue;
		}

		for (CloudTenantResource entity : list) {
			if (StringUtil.isNullOrEmpty(entity.getResourceId())) {
				continue;
			}
			if (returnValue.contains(entity.getResourceId())) {
				continue;
			}
			returnValue.add(entity.getResourceId());
		}
		return returnValue;
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> getResourceIdListByRoleId(String roleId, String resourceTypeCode) {
		List<String> returnValue = Lists.newArrayList();
		if (StringUtil.isNullOrEmpty(roleId) || StringUtil.isNullOrEmpty(resourceTypeCode)) {
			return returnValue;
		}
		List<SearchFilter> searchFilters = Lists.newArrayList();
		searchFilters.add(new SearchFilter("roleId", Operator.EQ, roleId));
		searchFilters.add(new SearchFilter("resourceTypeCode", Operator.EQ, resourceTypeCode));
		List<CloudTenantResource> list = this.findListByFilter(searchFilters, null);
		if (CollectionUtils.isEmpty(list)) {
			return returnValue;
		}

		for (CloudTenantResource entity : list) {
			if (StringUtil.isNullOrEmpty(entity.getResourceId())) {
				continue;
			}
			if (returnValue.contains(entity.getResourceId())) {
				continue;
			}
			returnValue.add(entity.getResourceId());
		}
		return returnValue;
	}

	@Override
	public void delByRoleId(String roleId) {
		if (StringUtil.isNullOrEmpty(roleId)) {
			return;
		}
		List<SearchFilter> searchFilters = Lists.newArrayList();
		searchFilters.add(new SearchFilter("roleId", Operator.EQ, roleId));
		List<CloudTenantResource> list = this.findListByFilter(searchFilters, null);
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		for (CloudTenantResource entity : list) {
			this.delete(entity);
		}
	}

	@Override
	public void delByIds(String ids) {
		if (StringUtil.isNullOrEmpty(ids)) {
			return;
		}
		List<String> _id_list = Arrays.asList(StringUtil.splitComma(ids));
		List<String> id_list = getCleanIds(_id_list);
		List<SearchFilter> searchFilters = Lists.newArrayList();
		searchFilters.add(new SearchFilter("id", Operator.IN, (String[]) id_list.toArray(new String[id_list.size()])));
		List<CloudTenantResource> list = this.findListByFilter(searchFilters, null);
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		for (CloudTenantResource entity : list) {
			this.delete(entity);
		}
	}

	/**
	 * 获取干净的ids
	 *
	 * @param ids
	 * @return
	 */
	private List<String> getCleanIds(List<String> ids) {
		List<String> _ids = Lists.newArrayList();
		if (CollectionUtils.isEmpty(ids)) {
			return null;
		}
		for (String id : ids) {
			if (StringUtil.isNullOrEmpty(id)) {
				continue;
			}
			if (_ids.contains(id)) {
				continue;
			}
			_ids.add(id);
		}

		if (CollectionUtils.isEmpty(_ids)) {
			return null;
		}
		return _ids;
	}

	@Override
	public void delByParam(Map<String, String> paramMap) {
		String roleId = paramMap.get("roleId");
		String resourceTypeCode = paramMap.get("resourceTypeCode");
		String resourceIdList = paramMap.get("resourceIdList");
		List<SearchFilter> searchFilters = Lists.newArrayList();
		if (!StringUtil.isNullOrEmpty(roleId)) {
			searchFilters.add(new SearchFilter("roleId", Operator.EQ, roleId));
		}
		if (!StringUtil.isNullOrEmpty(resourceTypeCode)) {
			searchFilters.add(new SearchFilter("resourceTypeCode", Operator.EQ, resourceTypeCode));
		}
		if (!StringUtil.isNullOrEmpty(resourceIdList)) {
			// 去除錯誤數據
			List<String> _resourceId_list = Arrays.asList(StringUtil.splitComma(resourceIdList));
			List<String> resourceId_list = getCleanIds(_resourceId_list);
			if (CollectionUtils.isNotEmpty(resourceId_list)) {
				searchFilters.add(new SearchFilter("resourceId", Operator.IN,
						(String[]) resourceId_list.toArray(new String[resourceId_list.size()])));
			}
		}
		List<CloudTenantResource> list = this.findListByFilter(searchFilters, null);
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		for (CloudTenantResource entity : list) {
			this.delete(entity);
		}
	}
}
