package com.vortex.cloud.ums.dataaccess.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vortex.cloud.ums.dataaccess.dao.ITenantUserRoleDao;
import com.vortex.cloud.ums.dto.TenantUserRoleDto;
import com.vortex.cloud.ums.dto.TenantUserRoleSearchDto;
import com.vortex.cloud.ums.model.TenantUserRole;
import com.vortex.cloud.ums.util.utils.QueryUtil;
import com.vortex.cloud.vfs.common.exception.ServiceException;
import com.vortex.cloud.vfs.data.hibernate.repository.SimpleHibernateRepository;
import com.vortex.cloud.vfs.data.model.BakDeleteModel;
import com.vortex.cloud.vfs.data.util.StaticDBType;

/**
 * @ClassName: TenantUserRoleDaoImpl
 * @Description: 用户角色dao（不挂在业务系统下）
 * @author ZQ shan
 * @date 2018年1月23日 上午10:19:48
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@SuppressWarnings("all")
@Repository("tenantUserRoleDao")
public class TenantUserRoleDaoImpl extends SimpleHibernateRepository<TenantUserRole, String>
		implements ITenantUserRoleDao {
	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public DetachedCriteria getDetachedCriteria() {
		return defaultCriteria();
	}

	private DetachedCriteria defaultCriteria() {
		DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass(), "tenantUserRole");
		criteria.add(Restrictions.eq("beenDeleted", BakDeleteModel.NO_DELETED));
		return criteria;
	}

	@Override
	public Page<TenantUserRoleDto> findPageBySearchDto(Pageable pageable, TenantUserRoleSearchDto searchDto) {
		// 校验输入的搜索条件
		this.checkSearchDto(searchDto);

		Map<String, Object> map = this.getSqlOfPageBySearchDto(searchDto);
		StringBuffer sql = (StringBuffer) map.get("sql");
		List<Object> argList = (List<Object>) map.get("argList");

		// 得到总记录数
		String sqlCnt = " SELECT COUNT(1) FROM ( " + sql.toString() + " ) t ";
		long totalCnt = jdbcTemplate.queryForObject(sqlCnt, argList.toArray(), Integer.class);

		// 组合分页条件
		Integer startRow = pageable.getPageNumber() * pageable.getPageSize();
		Integer endRow = (pageable.getPageNumber() + 1) * pageable.getPageSize();
		String sqlString = QueryUtil.getPagingSql(sql.toString(), startRow, endRow, StaticDBType.getDbType());

		List<TenantUserRoleDto> pageList = jdbcTemplate.query(sqlString, argList.toArray(),
				BeanPropertyRowMapper.newInstance(TenantUserRoleDto.class));

		return new PageImpl<TenantUserRoleDto>(pageList, pageable, totalCnt);
	}

	private void checkSearchDto(TenantUserRoleSearchDto searchDto) {
		if (StringUtils.isBlank(searchDto.getUserId())) {
			throw new ServiceException("用户ID为空！");
		}
	}

	private Map<String, Object> getSqlOfPageBySearchDto(TenantUserRoleSearchDto searchDto) {
		StringBuffer sql = new StringBuffer();
		List<Object> argList = Lists.newArrayList();

		sql.append(" SELECT ur.id id, u.id userId, u.userName userName,                       ");
		sql.append(" 	r.id roleId, r.name roleName,                                           ");
		sql.append(" 	rg.id roleGroupId, rg.name roleGroupName                                ");
		sql.append(" FROM cloud_user u, cloud_tenant_role r, cloud_tenant_user_role ur, cloud_tenant_role_group rg ");
		sql.append(" WHERE                                                                    ");
		sql.append(" 	u.id = ?                               									");
		sql.append(" 	AND u.beenDeleted = ?                                                   ");
		sql.append(" 	AND u.id = ur.userId                                                    ");
		sql.append(" 	AND r.id = ur.roleId                                                    ");
		sql.append(" 	AND r.beenDeleted = ?                                                   ");
		sql.append(" 	AND ur.beenDeleted = ?                                                  ");
		sql.append(" 	AND r.groupId = rg.id                                                   ");
		sql.append(" 	AND rg.beenDeleted = ?                                                  ");
		sql.append("order by r.orderIndex , r.name");

		argList.add(searchDto.getUserId());
		argList.add(BakDeleteModel.NO_DELETED);
		argList.add(BakDeleteModel.NO_DELETED);
		argList.add(BakDeleteModel.NO_DELETED);
		argList.add(BakDeleteModel.NO_DELETED);

		if (logger.isDebugEnabled()) {
			logger.debug("getSqlOfPageBySearchDto()," + sql.toString());
		}

		// 返回结果
		Map<String, Object> map = Maps.newHashMap();
		map.put("sql", sql);
		map.put("argList", argList);
		return map;
	}

	@Override
	public void deleteByUserId(String userId) {
		String sql = "delete from cloud_tenant_user_role where userId='" + userId + "'";
		this.jdbcTemplate.execute(sql);
	}

}
