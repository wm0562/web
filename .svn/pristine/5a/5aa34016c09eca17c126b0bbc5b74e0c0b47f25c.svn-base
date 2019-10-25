package com.vortex.cloud.ums.dataaccess.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.vortex.cloud.ums.dataaccess.dao.ITenantRoleDao;
import com.vortex.cloud.ums.dataaccess.dao.ITenantUserRoleDao;
import com.vortex.cloud.ums.dataaccess.service.ITenantUserRoleService;
import com.vortex.cloud.ums.dto.TenantUserRoleDto;
import com.vortex.cloud.ums.dto.TenantUserRoleSearchDto;
import com.vortex.cloud.ums.model.TenantRole;
import com.vortex.cloud.ums.model.TenantUserRole;
import com.vortex.cloud.vfs.common.exception.ServiceException;
import com.vortex.cloud.vfs.common.lang.StringUtil;
import com.vortex.cloud.vfs.data.hibernate.repository.HibernateRepository;
import com.vortex.cloud.vfs.data.hibernate.service.SimplePagingAndSortingService;
import com.vortex.cloud.vfs.data.support.SearchFilter;
import com.vortex.cloud.vfs.data.support.SearchFilter.Operator;

@Service("tenantUserRoleService")
@Transactional
public class TenantUserRoleServiceImpl extends SimplePagingAndSortingService<TenantUserRole, String>
		implements ITenantUserRoleService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private ITenantUserRoleDao tenantUserRoleDao;

	@Resource
	private ITenantRoleDao tenantRoleDao;

	@Override
	public HibernateRepository<TenantUserRole, String> getDaoImpl() {
		return tenantUserRoleDao;
	}

	@Override
	public void addRoles(String userId, String[] roleIdArr) {
		if (StringUtils.isBlank(userId)) {
			String msg = "用户ID为空！";
			logger.error(msg);
			throw new ServiceException(msg);
		}

		// 获取用户已经绑定的角色
		List<SearchFilter> filterList = Lists.newArrayList();
		filterList.add(new SearchFilter("userId", Operator.EQ, userId));
		List<TenantUserRole> oldList = this.findListByFilter(filterList, null);
		// 删除之前的所有角色
		if (CollectionUtils.isNotEmpty(oldList)) {
			tenantUserRoleDao.delete(oldList);
		}

		// 新增现在的人员角色关系
		if (ArrayUtils.isNotEmpty(roleIdArr)) {
			List<TenantUserRole> list = Lists.newArrayList();
			TenantUserRole userRole = null;
			for (String roleId : roleIdArr) {
				userRole = new TenantUserRole();
				userRole.setUserId(userId);
				userRole.setRoleId(roleId);
				list.add(userRole);
			}
			this.save(list);
		}
	}

	@Override
	public Page<TenantUserRoleDto> findPageBySearchDto(Pageable pageable, TenantUserRoleSearchDto searchDto) {
		return tenantUserRoleDao.findPageBySearchDto(pageable, searchDto);
	}

	@Override
	public List<TenantRole> getRolesByUserId(String userId) {
		if (StringUtils.isBlank(userId)) {
			logger.error("getRolesByUserId(),入参用户Id为空");
			throw new ServiceException("入参用户Id为空");
		}
		List<SearchFilter> filterList = Lists.newArrayList();
		filterList.add(new SearchFilter("userId", Operator.EQ, userId));
		List<TenantUserRole> list = this.findListByFilter(filterList, null);
		if (CollectionUtils.isEmpty(list)) {
			return Lists.newArrayList();
		}
		filterList.clear();
		List<String> roleIds = Lists.newArrayList();
		for (TenantUserRole entity : list) {
			if (StringUtil.isNullOrEmpty(entity.getRoleId())) {
				continue;
			}
			if (roleIds.contains(entity.getRoleId())) {
				continue;
			}
			roleIds.add(entity.getRoleId());
		}
		if (CollectionUtils.isEmpty(roleIds)) {
			return Lists.newArrayList();
		}
		filterList.add(new SearchFilter("id", Operator.IN, (String[]) roleIds.toArray(new String[roleIds.size()])));
		return tenantRoleDao.findListByFilter(filterList, null);
	}

	@Override
	public void deleteByUserId(String userId) {
		this.tenantUserRoleDao.deleteByUserId(userId);
	}
}
