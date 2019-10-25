package com.vortex.cloud.ums.dataaccess.dao.impl;

import java.util.ArrayList;
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
import com.vortex.cloud.ums.dataaccess.dao.ICloudTenantRelationDao;
import com.vortex.cloud.ums.dto.TenantDto;
import com.vortex.cloud.ums.dto.tenantgroup.CloudTenantRelationDto;
import com.vortex.cloud.ums.dto.tenantgroup.TenantInfoDto;
import com.vortex.cloud.ums.model.CloudTenantRelation;
import com.vortex.cloud.vfs.data.hibernate.repository.SimpleHibernateRepository;
import com.vortex.cloud.vfs.data.model.BakDeleteModel;
import com.vortex.cloud.vfs.data.support.SearchFilter;
import com.vortex.cloud.vfs.data.support.SearchFilter.Operator;

@Repository("cloudTenantRelationDao")
public class CloudTenantRelationDaoImpl extends SimpleHibernateRepository<CloudTenantRelation, String> implements ICloudTenantRelationDao {
	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public DetachedCriteria getDetachedCriteria() {
		return defaultCriteria();
	}

	private DetachedCriteria defaultCriteria() {
		DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass(), "cloudTenantRelation");
		criteria.add(Restrictions.eq("beenDeleted", BakDeleteModel.NO_DELETED));
		return criteria;
	}

	@Override
	public List<TenantInfoDto> listExceptViceTenant() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT t.id tenantId,t.tenantCode,t.tenantName ");
		sql.append(" from cloud_management_tenant t ");
		sql.append(" where t.beenDeleted=? ");
		sql.append("   and not EXISTS (select 1 from cloud_tenant_relation r where r.beenDeleted=0 and (r.viceTenantId=t.id or r.mainTenantId=t.id)) ");
		sql.append(" ORDER BY t.createTime DESC ");

		List<Object> args = Lists.newArrayList();
		args.add(BakDeleteModel.NO_DELETED);

		return jdbcTemplate.query(sql.toString(), args.toArray(), BeanPropertyRowMapper.newInstance(TenantInfoDto.class));
	}

	@Override
	public List<TenantInfoDto> listViceTenant(String mainTenantId) throws Exception {
		if (StringUtils.isEmpty(mainTenantId)) {
			return null;
		}

		StringBuffer sql = new StringBuffer();
		sql.append(" select t.id tenantId,t.tenantCode,t.tenantName ");
		sql.append(" from cloud_management_tenant t,cloud_tenant_relation r ");
		sql.append(" where t.id=r.viceTenantId ");
		sql.append("   and r.beenDeleted=? ");
		sql.append("   and r.mainTenantId=? ");
		sql.append(" ORDER BY t.createTime DESC ");

		List<Object> args = Lists.newArrayList();
		args.add(BakDeleteModel.NO_DELETED);
		args.add(mainTenantId);

		return jdbcTemplate.query(sql.toString(), args.toArray(), BeanPropertyRowMapper.newInstance(TenantInfoDto.class));
	}

	@Override
	public List<TenantDto> listViceTenantDto(String mainTenantId) throws Exception {
		if (StringUtils.isEmpty(mainTenantId)) {
			return null;
		}

		StringBuffer sql = new StringBuffer();
		sql.append(" select t.* ");
		sql.append(" from cloud_management_tenant t,cloud_tenant_relation r ");
		sql.append(" where t.id=r.viceTenantId ");
		sql.append("   and r.beenDeleted=? ");
		sql.append("   and r.mainTenantId=? ");
		sql.append(" ORDER BY t.createTime DESC ");

		List<Object> args = Lists.newArrayList();
		args.add(BakDeleteModel.NO_DELETED);
		args.add(mainTenantId);

		List<TenantDto> rst = jdbcTemplate.query(sql.toString(), args.toArray(), BeanPropertyRowMapper.newInstance(TenantDto.class));
		if (CollectionUtils.isNotEmpty(rst)) {
			for (TenantDto tenantDto : rst) {
				tenantDto.setParentId(mainTenantId);
			}
		}

		return rst;
	}

	@Override
	public void deleteRelation(String mainTenantId) throws Exception {
		if (StringUtils.isEmpty(mainTenantId)) {
			return;
		}
		jdbcTemplate.execute("delete from cloud_tenant_relation where mainTenantId='" + mainTenantId + "'");
	}

	@Override
	public boolean isInSameGroupCompany(String tenantId1, String tenantId2) {
		// 先判断是否互为主副
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(1) from cloud_tenant_relation a ");
		sql.append(" where a.mainTenantId=? and a.viceTenantId=? ");
		sql.append(" or a.mainTenantId=? and a.viceTenantId=? ");
		List<Object> args = Lists.newArrayList();
		args.add(tenantId1);
		args.add(tenantId2);
		args.add(tenantId2);
		args.add(tenantId1);

		Integer count = this.jdbcTemplate.queryForObject(sql.toString(), args.toArray(), Integer.class);
		if (count > 0) {
			return true;
		}

		// 再判断同主的两个子租户
		sql = new StringBuffer();
		args = Lists.newArrayList();
		sql.append(" select count(1) from ");
		sql.append(" (select * from cloud_tenant_relation a where a.viceTenantId=?) m, ");
		sql.append(" (select * from cloud_tenant_relation b where b.viceTenantId=?) n ");
		sql.append(" where m.mainTenantId = n.mainTenantId ");
		args.add(tenantId1);
		args.add(tenantId2);
		count = this.jdbcTemplate.queryForObject(sql.toString(), args.toArray(), Integer.class);

		if (count > 0) {
			return true;
		}

		return false;
	}

	@Override
	public String getMainTenantId(String viceTenantId) throws Exception {
		if (StringUtils.isEmpty(viceTenantId)) {
			return null;
		}

		List<SearchFilter> filterList = new ArrayList<SearchFilter>();
		filterList.add(new SearchFilter("viceTenantId", Operator.EQ, viceTenantId));
		List<CloudTenantRelation> list = findListByFilter(filterList, null);

		if (CollectionUtils.isNotEmpty(list)) {
			return list.get(0).getMainTenantId();
		} else {
			return null;
		}
	}

	@Override
	public CloudTenantRelationDto getRelationByTenantId(String id) throws Exception {
		if (StringUtils.isEmpty(id)) {
			return null;
		}

		String sql = "SELECT * from cloud_tenant_relation a where a.mainTenantId=? or a.viceTenantId=?";
		List<Object> args = Lists.newArrayList();
		args.add(id);
		args.add(id);

		List<CloudTenantRelationDto> list = jdbcTemplate.query(sql.toString(), args.toArray(), BeanPropertyRowMapper.newInstance(CloudTenantRelationDto.class));

		if (CollectionUtils.isNotEmpty(list)) {
			return list.get(0);
		} else {
			return null;
		}
	}
}
