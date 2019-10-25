package com.vortex.cloud.ums.dataaccess.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.vortex.cloud.ums.dataaccess.dao.ITenantFunctionRoleDao;
import com.vortex.cloud.ums.dataaccess.dao.ITenantRoleDao;
import com.vortex.cloud.ums.dataaccess.dao.ITenantUserRoleDao;
import com.vortex.cloud.ums.dataaccess.service.ITenantRoleService;
import com.vortex.cloud.ums.dto.TenantRoleDto;
import com.vortex.cloud.ums.model.TenantFunctionRole;
import com.vortex.cloud.ums.model.TenantRole;
import com.vortex.cloud.ums.model.TenantUserRole;
import com.vortex.cloud.vfs.common.exception.VortexException;
import com.vortex.cloud.vfs.common.lang.StringUtil;
import com.vortex.cloud.vfs.data.hibernate.repository.HibernateRepository;
import com.vortex.cloud.vfs.data.hibernate.service.SimplePagingAndSortingService;
import com.vortex.cloud.vfs.data.support.SearchFilter;
import com.vortex.cloud.vfs.data.support.SearchFilter.Operator;

@Transactional
@Service("tenantRoleService")
public class TenantRoleServiceImpl extends SimplePagingAndSortingService<TenantRole, String>
		implements ITenantRoleService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private ITenantRoleDao tenantRoleDao;

	@Resource
	private ITenantUserRoleDao tenantUserRoleDao;

	@Resource
	private ITenantFunctionRoleDao tenantFunctionRoleDao;

	@Override
	public HibernateRepository<TenantRole, String> getDaoImpl() {
		return tenantRoleDao;
	}

	@Override
	public String saveRole(TenantRoleDto dto) {
		// 校验入参
		this.checkData(dto);

		TenantRole bean = new TenantRole();
		BeanUtils.copyProperties(dto, bean);
		bean = tenantRoleDao.save(bean);

		return bean.getId();
	}

	@Override
	public void updateRole(TenantRoleDto dto) {
		// 校验入参
		this.checkData(dto);

		TenantRole bean = tenantRoleDao.findOne(dto.getId());
		if (bean == null) {
			logger.error("根据id【" + dto.getId() + "】未找到角色！");
			throw new VortexException("根据id【" + dto.getId() + "】未找到角色！");
		}

		bean.setCode(dto.getCode());
		bean.setName(dto.getName());
		bean.setGroupId(dto.getGroupId());
		bean.setTenantId(dto.getTenantId());
		bean.setOrderIndex(dto.getOrderIndex());
		bean.setDescription(dto.getDescription());

		tenantRoleDao.update(bean);
	}

	private void checkData(TenantRoleDto dto) {
		if (dto == null) {
			logger.error("保存角色时传入的参数为空！");
			throw new VortexException("保存角色时传入的参数为空！");
		}

		if (StringUtils.isEmpty(dto.getCode())) {
			logger.error("保存角色时传入的编码为空！");
			throw new VortexException("保存角色时传入的编码为空！");
		}

		if (StringUtils.isEmpty(dto.getName())) {
			logger.error("保存角色时传入的名称为空！");
			throw new VortexException("保存角色时传入的名称为空！");
		}

		if (StringUtils.isEmpty(dto.getName())) {
			logger.error("保存角色时传入的角色组id为空！");
			throw new VortexException("保存角色时传入的角色组id为空！");
		}

		if (this.isRoleCodeExists(dto.getId(), dto.getCode(), dto.getTenantId())) {
			logger.error("保存角色时同一个租户下面的code不能重复！");
			throw new VortexException("保存角色时同一个租户下面的code不能重复！");
		}
	}

	@Override
	public void deleteRole(String roleId) {
		if (StringUtils.isEmpty(roleId)) {
			logger.error("删除角色时，传入的角色id为空！");
			throw new VortexException("删除角色时，传入的角色id为空！");
		}

		List<SearchFilter> searchFilters = Lists.newArrayList();
		SearchFilter filter = new SearchFilter("roleId", SearchFilter.Operator.EQ, roleId);
		searchFilters.add(filter);
		List<TenantUserRole> urList = tenantUserRoleDao.findListByFilter(searchFilters, null);
		if (CollectionUtils.isNotEmpty(urList)) {
			logger.error("id为[" + roleId + "]的角色已经被用户使用，无法删除！");
			throw new VortexException("id为[" + roleId + "]的角色已经被用户使用，无法删除！");
		}

		searchFilters = Lists.newArrayList();
		filter = new SearchFilter("roleId", SearchFilter.Operator.EQ, roleId);
		searchFilters.add(filter);
		List<TenantFunctionRole> frList = tenantFunctionRoleDao.findListByFilter(searchFilters, null);
		if (CollectionUtils.isNotEmpty(frList)) {
			logger.error("id为[" + roleId + "]的角色已经和功能关联使用，无法删除！");
			throw new VortexException("id为[" + roleId + "]的角色已经和功能关联使用，无法删除！");
		}

		tenantRoleDao.delete(roleId);
	}

	@Override
	public boolean isRoleCodeExists(String roleId, String newCode, String tenantId) {
		boolean rst = false;
		if (StringUtils.isEmpty(newCode) || StringUtils.isEmpty(tenantId)) {
			return rst;
		}
		List<SearchFilter> searchFilters = Lists.newArrayList();
		searchFilters.add(new SearchFilter("tenantId", Operator.EQ, tenantId));
		searchFilters.add(new SearchFilter("code", Operator.EQ, newCode));
		if (!StringUtil.isNullOrEmpty(roleId)) {
			searchFilters.add(new SearchFilter("id", Operator.NE, roleId));
		}
		List<TenantRole> list = this.findListByFilter(searchFilters, null);
		if (list.size() > 0) {
			rst = true;
		}
		return rst;
	}

	@Override
	public TenantRoleDto getRoleInfoById(String roleId) {
		if (StringUtils.isEmpty(roleId)) {
			logger.error("根据角色id查询角色信息时，传入的角色id为空！");
			throw new VortexException("根据角色id查询角色信息时，传入的角色id为空！");
		}
		TenantRole role = tenantRoleDao.findOne(roleId);
		TenantRoleDto dto = new TenantRoleDto();
		BeanUtils.copyProperties(role, dto);
		return dto;
	}

	@Override
	public TenantRoleDto getRoleByCode(String code) {
		if (StringUtils.isBlank(code)) {
			String msg = "角色code为空！";
			logger.error(msg);
			throw new VortexException(msg);
		}

		List<SearchFilter> filterList = Lists.newArrayList();
		filterList.add(new SearchFilter("code", Operator.EQ, code));

		List<TenantRole> list = this.findListByFilter(filterList, null);
		if (CollectionUtils.isEmpty(list)) {
			logger.error("根据角色code未能获取到角色记录！");
			return null;
		}

		TenantRole role = list.get(0);
		TenantRoleDto dto = new TenantRoleDto();
		BeanUtils.copyProperties(role, dto);
		return dto;
	}

	@Override
	public void deletes(List<String> idList) {
		if (CollectionUtils.isEmpty(idList)) {
			return;
		}

		for (String id : idList) {
			super.delete(id);
		}
	}

	@Override
	public List<String> getUserIdsByRole(String tenantId, String roleCode) {

		if (StringUtils.isBlank(tenantId)) {
			String msg = "租户id为空！";
			logger.error(msg);
			throw new VortexException(msg);
		}
		if (StringUtils.isBlank(roleCode)) {
			String msg = "角色code为空！";
			logger.error(msg);
			throw new VortexException(msg);
		}
		List<String> userIds = tenantRoleDao.getUserIdsByRole(tenantId, roleCode);
		return userIds;
	}

}
