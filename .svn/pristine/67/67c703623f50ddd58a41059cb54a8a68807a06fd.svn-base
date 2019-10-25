package com.vortex.cloud.ums.dataaccess.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.vortex.cloud.ums.dataaccess.dao.ITenantRoleDao;
import com.vortex.cloud.ums.dataaccess.dao.ITenantRoleGroupDao;
import com.vortex.cloud.ums.dataaccess.service.ITenantRoleGroupService;
import com.vortex.cloud.ums.dto.TenantRoleGroupDto;
import com.vortex.cloud.ums.model.TenantRole;
import com.vortex.cloud.ums.model.TenantRoleGroup;
import com.vortex.cloud.vfs.common.exception.ServiceException;
import com.vortex.cloud.vfs.data.hibernate.repository.HibernateRepository;
import com.vortex.cloud.vfs.data.hibernate.service.SimplePagingAndSortingService;
import com.vortex.cloud.vfs.data.support.SearchFilter;
import com.vortex.cloud.vfs.data.support.SearchFilter.Operator;

@Transactional
@Service("tenantRoleGroupService")
public class TenantRoleGroupServiceImpl extends SimplePagingAndSortingService<TenantRoleGroup, String>
		implements ITenantRoleGroupService {
	@Resource
	private ITenantRoleGroupDao tenantRoleGroupDao;

	@Resource
	private ITenantRoleDao tenantRoleDao;

	private Logger loger = LoggerFactory.getLogger(this.getClass());

	@Override
	public HibernateRepository<TenantRoleGroup, String> getDaoImpl() {
		return tenantRoleGroupDao;
	}

	@Override
	public void deleteAllById(String id) {
		if (StringUtils.isEmpty(id)) {
			loger.error("主键不能为空");
			throw new ServiceException("主键不能为空");
		}
		List<String> childrenIds = this.getAllChildrenId(id);
		if (!CollectionUtils.isEmpty(childrenIds)) {
			List<TenantRoleGroup> list = tenantRoleGroupDao
					.findAllByIds(childrenIds.toArray(new String[childrenIds.size()]));
			tenantRoleGroupDao.deleteInBatch(list);
		}
		tenantRoleGroupDao.delete(id);

	}

	@Transactional(readOnly = true)
	@Override
	public List<String> getAllChildrenId(String id) {
		if (StringUtils.isEmpty(id)) {
			loger.error("主键不能为空");
			throw new ServiceException("主键不能为空");
		}
		List<String> results = null;

		List<SearchFilter> searchFilters = Lists.newArrayList();
		searchFilters.add(new SearchFilter("parentId", Operator.EQ, id));
		List<TenantRoleGroup> list = tenantRoleGroupDao.findListByFilter(searchFilters, null);
		if (!CollectionUtils.isEmpty(list)) {
			results = Lists.newArrayList();
			for (TenantRoleGroup actionGroup : list) {
				results.add(actionGroup.getId());

				List<String> childrens = this.getAllChildrenId(actionGroup.getId());
				if (childrens != null) {
					results.addAll(childrens);
				}
			}
		}
		return results;
	}

	@Transactional(readOnly = true)
	@Override
	public List<String> getAllParentId(String id) {
		if (StringUtils.isEmpty(id)) {
			loger.error("主键不能为空");
			throw new ServiceException("主键不能为空");
		}
		List<String> results = null;
		TenantRoleGroup actionGroup = tenantRoleGroupDao.findOne(id);

		if (actionGroup != null) {
			results = Lists.newArrayList();
			List<String> parentIds = this.getAllParentId(actionGroup.getParentId());
			if (parentIds != null) {
				results.add(actionGroup.getParentId());
				results.addAll(parentIds);
			}
		}
		return results;
	}

	@Override
	public void saveRoleGroup(TenantRoleGroupDto dto) {
		if (null == dto) {
			loger.error("dto不能为空");
			throw new ServiceException("dto不能为空");
		}
		if (StringUtils.isEmpty(dto.getCode())) {
			loger.error("编码不能为空");
			throw new ServiceException("编码不能为空");
		}
		if (StringUtils.isEmpty(dto.getName())) {
			loger.error("名称不能为空");
			throw new ServiceException("名称不能为空");
		}

		TenantRoleGroup entity = new TenantRoleGroup();
		BeanUtils.copyProperties(dto, entity);
		tenantRoleGroupDao.save(entity);
	}

	@Override
	public TenantRoleGroupDto findRoleGroupById(String id) {
		if (StringUtils.isEmpty(id)) {
			loger.error("主键不能为空");
			throw new ServiceException("主键不能为空");
		}
		TenantRoleGroup TenantRoleGroup = tenantRoleGroupDao.findOne(id);
		if (TenantRoleGroup == null) {
			loger.error("不存在id为" + id + "的记录");
			throw new ServiceException("不存在id为" + id + "的记录");

		}
		TenantRoleGroupDto dto = new TenantRoleGroupDto();
		BeanUtils.copyProperties(TenantRoleGroup, dto);
		return dto;

	}

	@Override
	public void updateRoleGroup(TenantRoleGroupDto dto) {
		if (null == dto) {
			loger.error("dto不能为空");
			throw new ServiceException("dto不能为空");
		}
		if (StringUtils.isEmpty(dto.getId())) {
			loger.error("id不能为空");
			throw new ServiceException("id不能为空");
		}
		if (StringUtils.isEmpty(dto.getCode())) {
			loger.error("编码不能为空");
			throw new ServiceException("编码不能为空");
		}
		if (StringUtils.isEmpty(dto.getName())) {
			loger.error("名称不能为空");
			throw new ServiceException("名称不能为空");
		}

		TenantRoleGroup entity = findOne(dto.getId());
		if (null == entity) {
			loger.error("不存在id为" + dto.getId() + "的数据");
			throw new ServiceException("不存在id为" + dto.getId() + "的数据");
		}
		entity.setCode(dto.getCode());
		entity.setName(dto.getName());
		entity.setTenantId(dto.getTenantId());
		entity.setParentId(dto.getParentId());
		entity.setDescription(dto.getDescription());
		entity.setOrderIndex(dto.getOrderIndex());
		tenantRoleGroupDao.update(entity);

	}

	@Override
	public void deleteRoleGroup(String rgId) {
		if (StringUtils.isEmpty(rgId)) {
			loger.error("删除角色组时，传入的id为空！");
			throw new ServiceException("删除角色组时，传入的id为空！");
		}

		TenantRoleGroup rg = tenantRoleGroupDao.findOne(rgId);
		if (rg == null) {
			loger.error("根据id[" + rgId + "]未找到角色组！");
			throw new ServiceException("根据id[" + rgId + "]未找到角色组！");
		}

		// 有子节点的不能删除
		List<SearchFilter> searchFilters = Lists.newArrayList();
		searchFilters.add(new SearchFilter("parentId", SearchFilter.Operator.EQ, rgId));
		List<TenantRoleGroup> children = tenantRoleGroupDao.findListByFilter(searchFilters, null);
		if (CollectionUtils.isNotEmpty(children)) {
			loger.error("当前角色组有子节点，无法删除！");
			throw new ServiceException("当前角色组有子节点，无法删除！");
		}

		// 节点上挂有角色的，也不能删除
		searchFilters = Lists.newArrayList();
		searchFilters.add(new SearchFilter("groupId", SearchFilter.Operator.EQ, rgId));
		List<TenantRole> rl = tenantRoleDao.findListByFilter(searchFilters, null);
		if (CollectionUtils.isNotEmpty(rl)) {
			loger.error("当前角色组下面有角色，无法删除！");
			throw new ServiceException("当前角色组下面有角色，无法删除！");
		}
		tenantRoleGroupDao.delete(rg);
	}
}
