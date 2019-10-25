package com.vortex.cloud.ums.dataaccess.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.vortex.cloud.ums.dataaccess.dao.ITenantRoleDao;
import com.vortex.cloud.ums.model.TenantRole;
import com.vortex.cloud.vfs.data.hibernate.repository.SimpleHibernateRepository;
import com.vortex.cloud.vfs.data.model.BakDeleteModel;

/**
 * @ClassName: TenantRoleDaoImpl
 * @Description: 租户角色（不挂在业务系统下）Dao
 * @author ZQ shan
 * @date 2018年1月23日 上午10:18:02
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Repository("tenantRoleDao")
public class TenantRoleDaoImpl extends SimpleHibernateRepository<TenantRole, String> implements ITenantRoleDao {
	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public DetachedCriteria getDetachedCriteria() {
		return defaultCriteria();
	}

	private DetachedCriteria defaultCriteria() {
		DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass(), "tenantRole");
		criteria.add(Restrictions.eq("beenDeleted", 0));
		return criteria;
	}

	@Override
	public List<String> getUserIdsByRole(String tenantId, String roleCode) {

		StringBuffer sql = new StringBuffer();
		List<Object> argList =Lists.newArrayList();

		sql.append("                SELECT                                                        ");
		sql.append("                	 ur.userId                                                ");
		sql.append("                FROM                                                          ");
		sql.append("                	cloud_tenant_user_role ur,                                       ");
		sql.append("                	cloud_tenant_role r                                        ");
		sql.append("                WHERE                                                         ");
		sql.append("                	ur.roleId = r.id                                          ");
		sql.append("                AND ur.beenDeleted =?                                         ");
		sql.append("                AND r.beenDeleted = ?                                         ");
		sql.append("                AND r.`code` = ?                                              ");
		sql.append("                AND r.tenantId = ?                                            ");
		argList.add(BakDeleteModel.NO_DELETED);
		argList.add(BakDeleteModel.NO_DELETED);
		argList.add(roleCode);
		argList.add(tenantId);
		return jdbcTemplate.queryForList(sql.toString(), argList.toArray(), String.class);
	}

}
