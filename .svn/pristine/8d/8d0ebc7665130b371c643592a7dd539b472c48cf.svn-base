package com.vortex.cloud.ums.dataaccess.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.vortex.cloud.ums.dataaccess.dao.ICloudSystemDao;
import com.vortex.cloud.ums.dto.CloudSystemDto;
import com.vortex.cloud.ums.dto.CloudTreeDto;
import com.vortex.cloud.ums.dto.tenantgroup.SystemListDto;
import com.vortex.cloud.ums.model.CloudSystem;
import com.vortex.cloud.vfs.common.lang.StringUtil;
import com.vortex.cloud.vfs.data.hibernate.repository.SimpleHibernateRepository;
import com.vortex.cloud.vfs.data.model.BakDeleteModel;
import com.vortex.cloud.vfs.data.support.SearchFilter;
import com.vortex.cloud.vfs.data.support.SearchFilter.Operator;

@Repository("cloudSystemDao")
public class CloudSystemDaoImpl extends SimpleHibernateRepository<CloudSystem, String> implements ICloudSystemDao {

	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public DetachedCriteria getDetachedCriteria() {
		return defaultCriteria();
	}

	private DetachedCriteria defaultCriteria() {
		DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass(), "cloudSystem");
		criteria.add(Restrictions.eq("beenDeleted", BakDeleteModel.NO_DELETED));
		return criteria;
	}

	@Override
	public CloudSystem getByCode(String code) {
		List<SearchFilter> filterList = Lists.newArrayList();
		filterList.add(new SearchFilter("systemCode", Operator.EQ, code));
		List<CloudSystem> list = findListByFilter(filterList, null);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		} else {
			return list.get(0);
		}
	}

	@Override
	public List<String> getSystemList(String userId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT DISTINCT CONCAT(t.systemCode,'||',t.systemName) sys ");
		sql.append(" from cloud_system t ");
		sql.append(" where EXISTS (select 1 from cloud_user_role a,cloud_role b where a.userid=? and a.roleid=b.id and b.systemid=t.id and a.beenDeleted=0 and b.beenDeleted=0) ");
		sql.append(" and t.beenDeleted=? ");
		sql.append(" order by orderindex ");
		List<Object> args = Lists.newArrayList();
		args.add(userId);
		args.add(BakDeleteModel.NO_DELETED);
		List<String> list = jdbcTemplate.queryForList(sql.toString(), args.toArray(), String.class);
		// 租户下角色功能
		sql.setLength(0);
		args.clear();
		sql.append("SELECT DISTINCT CONCAT(t.systemCode,'||',t.systemName) sys  ");
		sql.append(" from cloud_system t  where t.id in ( ");
		sql.append(" select distinct t.systemId from cloud_function t  ");
		sql.append(" left join cloud_tenant_function_role fr on fr.functionId = t.id ");
		sql.append(" left join cloud_tenant_user_role ur on ur.roleId = fr.roleId ");
		sql.append(" where ur.userId=?  ");
		sql.append(" and ur.beenDeleted=?  ");
		sql.append(" and fr.beenDeleted=? ");
		sql.append(" and t.beenDeleted=?) and t.beenDeleted=? ");

		args.add(userId);
		args.add(BakDeleteModel.NO_DELETED);
		args.add(BakDeleteModel.NO_DELETED);
		args.add(BakDeleteModel.NO_DELETED);
		args.add(BakDeleteModel.NO_DELETED);
		List<String> other_list = jdbcTemplate.queryForList(sql.toString(), args.toArray(), String.class);
		if (CollectionUtils.isNotEmpty(other_list)) {
			for (String value : other_list) {
				if (StringUtil.isNullOrEmpty(value)) {
					continue;
				}
				if (list.contains(value)) {
					continue;
				}
				list.add(value);
			}
		}
		return list;
	}

	@Override
	public List<CloudTreeDto> getCloudSystemsByUserId(String userId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select DISTINCT s.id,s.systemName name,'-1' parentId, 'System' as type ");
		sql.append(" from cloud_user_role ur,cloud_role r,cloud_system s ");
		sql.append(" where ur.userId= ? ");
		sql.append(" and ur.roleId=r.id ");
		sql.append(" and r.systemId=s.id ");
		sql.append(" and ur.beenDeleted= ?  ");
		sql.append(" and r.beenDeleted= ? ");
		sql.append(" and s.beenDeleted= ? ");
		List<Object> args = Lists.newArrayList();
		args.add(userId);
		args.add(BakDeleteModel.NO_DELETED);
		args.add(BakDeleteModel.NO_DELETED);
		args.add(BakDeleteModel.NO_DELETED);
		return jdbcTemplate.query(sql.toString(), args.toArray(), BeanPropertyRowMapper.newInstance(CloudTreeDto.class));
	}

	@Override
	public List<CloudSystemDto> getCloudSystemByRoleCode(String roleCode) {
		if (StringUtil.isNullOrEmpty(roleCode)) {
			return null;
		}
		StringBuffer sql = new StringBuffer();
		sql.append(" select  t.systemId as id,s.systemCode from cloud_role t ");
		sql.append(" left join cloud_system s on s.id = t.systemId");
		sql.append(" where t.code = ? and t.beenDeleted=? and s.beenDeleted=? ");
		List<Object> args = Lists.newArrayList();
		args.add(roleCode);
		args.add(BakDeleteModel.NO_DELETED);
		args.add(BakDeleteModel.NO_DELETED);
		return jdbcTemplate.query(sql.toString(), args.toArray(), BeanPropertyRowMapper.newInstance(CloudSystemDto.class));
	}

	@Override
	public List<SystemListDto> listByTenantId(String tenantId) throws Exception {
		if (StringUtils.isEmpty(tenantId)) {
			return null;
		}

		StringBuffer sql = new StringBuffer();
		sql.append(" select a.id tenantId,a.tenantCode,a.tenantName,b.id systemId,b.systemCode,b.systemName ");
		sql.append(" from cloud_management_tenant a,cloud_system b ");
		sql.append(" where a.id=b.tenantId ");
		sql.append("   and a.id=? ");
		sql.append("   and a.beenDeleted=? ");
		sql.append("   and b.beenDeleted=? ");
		sql.append(" order by b.orderIndex ");

		List<Object> args = Lists.newArrayList();
		args.add(tenantId);
		args.add(BakDeleteModel.NO_DELETED);
		args.add(BakDeleteModel.NO_DELETED);

		return jdbcTemplate.query(sql.toString(), args.toArray(), BeanPropertyRowMapper.newInstance(SystemListDto.class));
	}
}
