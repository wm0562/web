package com.vortex.cloud.ums.dataaccess.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.vortex.cloud.ums.dataaccess.dao.ITenantFunctionRoleDao;
import com.vortex.cloud.ums.dataaccess.service.ITenantFunctionRoleService;
import com.vortex.cloud.ums.dto.TenantFunctionRoleDto;
import com.vortex.cloud.ums.model.TenantFunctionRole;
import com.vortex.cloud.ums.util.orm.Page;
import com.vortex.cloud.vfs.common.exception.ServiceException;
import com.vortex.cloud.vfs.data.hibernate.repository.HibernateRepository;
import com.vortex.cloud.vfs.data.hibernate.service.SimplePagingAndSortingService;

/**
 * 角色功能关系
 * 
 * @author lsm
 * @date 2016年4月7日
 */
@Transactional
@Service("tenantFunctionRoleService")
public class TenantFunctionRoleServiceImpl extends SimplePagingAndSortingService<TenantFunctionRole, String>
		implements ITenantFunctionRoleService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private ITenantFunctionRoleDao tenantFunctionRoleDao;

	@Override
	public HibernateRepository<TenantFunctionRole, String> getDaoImpl() {
		return tenantFunctionRoleDao;
	}

	@Override
	public void saveFunctionsForRole(String roleId, String[] functionIdArray) {
		if (StringUtils.isBlank(roleId)) {
			throwExpectionAndLog("saveFunctionsForRole", "入参不能为空");
		}

		if (ArrayUtils.isEmpty(functionIdArray)) {
			return;
		}

		// 删除现在存在的关系
		List<TenantFunctionRoleDto> dbList = this.getListByRole(roleId);
		for (TenantFunctionRoleDto functionRoleDto : dbList) {
			tenantFunctionRoleDao.delete(functionRoleDto.getId());
		}

		if (ArrayUtils.isNotEmpty(functionIdArray)) { // 数组不为空就保存
			List<TenantFunctionRole> list = Lists.newArrayList();

			TenantFunctionRole entity = null;
			for (String functionId : functionIdArray) {
				entity = new TenantFunctionRole();
				entity.setRoleId(roleId);
				entity.setFunctionId(functionId);
				list.add(entity);
			}

			tenantFunctionRoleDao.save(list);
		}
	}

	@Override
	public void deleteFunctionRole(String id) {
		if (StringUtils.isEmpty(id)) {
			throwExpectionAndLog("deleteFunctionRole", "id不能为空");
		}
		if (canBeDelete(id)) {
			tenantFunctionRoleDao.delete(id);
		}
	}

	/**
	 * 角色与功能之间的关系等否删除
	 * 
	 * @param id
	 *            功能id
	 * @return
	 */
	private boolean canBeDelete(String id) {
		return true;
	}

	@Override
	public Page<TenantFunctionRoleDto> getPageByRole(String roleId, Pageable pageable) {
		if (StringUtils.isEmpty(roleId)) {
			throwExpectionAndLog("getPageBySystem", "角色id不能为空");
		}

		return tenantFunctionRoleDao.getPageByRole(roleId, pageable);
	}

	@Override
	public List<TenantFunctionRoleDto> getListByRole(String roleId) {
		if (StringUtils.isBlank(roleId)) {
			throwExpectionAndLog("getListBySystem", "入参不能为空");
		}

		return tenantFunctionRoleDao.getListByRole(roleId);
	}

	/**
	 * 记录log和抛异常
	 * 
	 * @param methodName
	 *            当前方法名
	 * @param msg
	 *            要记录的信息
	 */
	private void throwExpectionAndLog(String methodName, String msg) {
		String message = "TenantFunctionRoleServiceImpl." + msg;
		logger.error(message);
		throw new ServiceException(message);
	}
}
