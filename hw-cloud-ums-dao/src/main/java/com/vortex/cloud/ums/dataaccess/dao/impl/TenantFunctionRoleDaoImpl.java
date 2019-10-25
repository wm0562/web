package com.vortex.cloud.ums.dataaccess.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vortex.cloud.ums.dataaccess.dao.ITenantFunctionRoleDao;
import com.vortex.cloud.ums.dto.TenantFunctionRoleDto;
import com.vortex.cloud.ums.model.TenantFunctionRole;
import com.vortex.cloud.ums.util.orm.Page;
import com.vortex.cloud.ums.util.utils.QueryUtil;
import com.vortex.cloud.vfs.data.hibernate.repository.SimpleHibernateRepository;
import com.vortex.cloud.vfs.data.model.BakDeleteModel;
import com.vortex.cloud.vfs.data.util.StaticDBType;

/**
 * @ClassName: TenantFunctionRoleDaoImpl
 * @Description: 功能和角色的对应表（不挂在业务系统下）Dao
 * @author ZQ shan
 * @date 2018年1月23日 上午10:17:29
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Repository("tenantFunctionRoleDao")
public class TenantFunctionRoleDaoImpl extends SimpleHibernateRepository<TenantFunctionRole, String>
		implements ITenantFunctionRoleDao {
	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public DetachedCriteria getDetachedCriteria() {
		return defaultCriteria();
	}

	private DetachedCriteria defaultCriteria() {
		DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass(), "tenantFunctionRole");
		criteria.add(Restrictions.eq("beenDeleted", 0));
		return criteria;
	}

	@Override
	public Page<TenantFunctionRoleDto> getPageByRole(String roleId, Pageable pageable) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		sql.append("	cfr.`id` id, ");
		sql.append("	cf.`name` functionName, ");
		sql.append("	cfg.`name` functionGroupName ");
		sql.append(" FROM ");
		sql.append("	cloud_tenant_function_role cfr ");
		sql.append(" LEFT JOIN cloud_function cf ON cfr.functionId = cf.id ");
		sql.append(" LEFT JOIN cloud_function_group cfg ON cf.groupId = cfg.id ");
		sql.append(" WHERE ");
		sql.append("	cfr.roleId = '" + roleId + "'  ");
		sql.append("	AND cfr.beenDeleted = '0'       ");
		sql.append(" ORDER BY cfg.orderIndex ASC, cf.orderIndex ASC ");

		// 得到总记录数
		String sqlCount = " select count(*) from (" + sql.toString() + ") a";
		int totalRecords = jdbcTemplate.queryForObject(sqlCount, Integer.class);

		// 组合分页条件
		String sqlString = QueryUtil.getPagingSql(sql.toString(), pageable.getPageNumber() * pageable.getPageSize(),
				(pageable.getPageNumber() + 1) * pageable.getPageSize(), StaticDBType.getDbType());
		List<TenantFunctionRoleDto> results = jdbcTemplate.query(sqlString,
				BeanPropertyRowMapper.newInstance(TenantFunctionRoleDto.class));

		Page<TenantFunctionRoleDto> page = new Page<TenantFunctionRoleDto>();
		page.setTotalRecords(totalRecords);
		page.setResult(results);

		return page;
	}

	@Override
	public List<TenantFunctionRoleDto> getListByRole(String roleId) {
		Map<String, Object> map = this.getSqlOfListBySystem(roleId);

		StringBuffer sql = (StringBuffer) map.get("sql");
		List<Object> args = (List<Object>) map.get("args");

		return jdbcTemplate.query(sql.toString(), args.toArray(),
				BeanPropertyRowMapper.newInstance(TenantFunctionRoleDto.class));
	}

	/**
	 * 组装SQL
	 * 
	 * @param roleId
	 * @param systemId
	 * @return
	 */
	private Map<String, Object> getSqlOfListBySystem(String roleId) {
		Map<String, Object> map = Maps.newHashMap();
		StringBuffer sql = new StringBuffer("");
		List<Object> args = Lists.newArrayList();

		sql.append(" SELECT 											");
		sql.append("	cfr.* 											");
		sql.append(" FROM 												");
		sql.append("	cloud_tenant_function_role cfr, cloud_function cf 		");
		sql.append(" WHERE 												");
		sql.append("	cfr.functionId = cf.id 							");
		sql.append("	AND cfr.roleId = ?  							");
		sql.append("	AND cfr.beenDeleted = ?       					");

		args.add(roleId);
		args.add(BakDeleteModel.NO_DELETED);

		map.put("sql", sql);
		map.put("args", args);
		return map;
	}

}
