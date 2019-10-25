package com.vortex.cloud.ums.dataaccess.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.vortex.cloud.ums.dataaccess.dao.ICloudOrgInfoDao;
import com.vortex.cloud.ums.dto.IdNameDto;
import com.vortex.cloud.ums.dto.OrgInfoDto;
import com.vortex.cloud.ums.dto.OrgInfoQueryDto;
import com.vortex.cloud.ums.enums.OrgTypeEnum;
import com.vortex.cloud.ums.model.CloudOrgInfo;
import com.vortex.cloud.ums.util.utils.QueryUtil;
import com.vortex.cloud.vfs.data.hibernate.repository.SimpleHibernateRepository;
import com.vortex.cloud.vfs.data.model.BakDeleteModel;
import com.vortex.cloud.vfs.data.support.SearchFilter;
import com.vortex.cloud.vfs.data.support.SearchFilter.Operator;
import com.vortex.cloud.vfs.data.util.StaticDBType;

@Repository("cloudOrgInfoDao")
public class CloudOrgInfoDaoImpl extends SimpleHibernateRepository<CloudOrgInfo, String> implements ICloudOrgInfoDao {
	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public DetachedCriteria getDetachedCriteria() {
		DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass(), "cloudOrgInfo");
		criteria.add(Restrictions.eq("beenDeleted", BakDeleteModel.NO_DELETED));

		return criteria;
	}

	@Override
	public CloudOrgInfo findByOrgId(String orgId) throws Exception {
		List<SearchFilter> filterList = new ArrayList<SearchFilter>();
		filterList.add(new SearchFilter("orgId", Operator.EQ, orgId));
		List<CloudOrgInfo> list = findListByFilter(filterList, null);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		} else {
			return list.get(0);
		}
	}

	@Override
	public List<String> listIdsByOrgids(List<String> ids) throws Exception {
		if (CollectionUtils.isEmpty(ids)) {
			return null;
		}

		StringBuffer str = new StringBuffer();
		for (int i = 0; i < ids.size(); i++) {
			if (i == 0) {
				str.append(ids.get(i));
			} else {
				str.append("," + ids.get(i));
			}
		}

		StringBuffer sql = new StringBuffer();
		sql.append("select * from cloud_org_info where orgId in(" + str + ")");

		return jdbcTemplate.queryForList(sql.toString(), String.class);
	}

	@Override
	public Page<OrgInfoDto> queryOrgInfo(OrgInfoQueryDto dto) throws Exception {
		if (dto == null || StringUtils.isEmpty(dto.getTenantId())) {
			return null;
		}
		StringBuffer sql = new StringBuffer();
		List<Object> argList = Lists.newArrayList();

		// 得到sql
		if (StringUtils.isEmpty(dto.getParentId())) { // 不传查全部表
			sql = this.getSqlAll();
			argList.add(dto.getTenantId());
			argList.add(dto.getTenantId());
		} else if (dto.getParentId().equals("-1")) { // 传-1查dept表
			sql = this.getDeptSql();
			argList.add(dto.getTenantId());
		} else { // 否则查org表
			sql = this.getOrgSql();
			argList.add(dto.getParentId());
		}

		// 拼接查询条件
		sql.append(" where 1=1 ");
		if (StringUtils.isNotEmpty(dto.getName())) {
			sql.append(" and name like '%" + dto.getName() + "%' ");
		}
		if (StringUtils.isNotEmpty(dto.getCode())) {
			sql.append(" and code like '%" + dto.getCode() + "%' ");
		}

		// 得到总记录数
		String sqlCnt = " SELECT COUNT(1) FROM ( " + sql.toString() + " ) t ";
		long totalCnt = jdbcTemplate.queryForObject(sqlCnt, argList.toArray(), Integer.class);

		// 组合分页条件
		Integer startRow = dto.getPageNum() * dto.getPageSize();
		Integer endRow = (dto.getPageNum() + 1) * dto.getPageSize();
		String sqlString = QueryUtil.getPagingSql(sql.toString(), startRow, endRow, StaticDBType.getDbType());

		List<OrgInfoDto> pageList = jdbcTemplate.query(sqlString, argList.toArray(), BeanPropertyRowMapper.newInstance(OrgInfoDto.class));

		return new PageImpl<>(pageList, null, totalCnt);
	}

	private StringBuffer getDeptSql() {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from( ");
		sql.append(" select a.depName name,a.depCode code ,a.tenantId,a.head,a.headMobile,a.description,a.lngLats,a.address,a.email,'' parentId,'1' flag, ");
		sql.append(" b.id orgInfoId,b.orgid,b.orgCid,b.abbr,b.beginYear,b.endYear,b.orgType,b.isDzd,b.isReseau,b.isMgr ");
		sql.append(" from cloud_department a,cloud_org_info b ");
		sql.append(" where a.id=b.orgId ");
		sql.append("   and a.beenDeleted=0 ");
		sql.append("   and b.beenDeleted=0 ");
		sql.append("   and a.tenantId=? ");
		sql.append(" )t ");
		return sql;
	}

	private StringBuffer getOrgSql() {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from( ");
		sql.append(" SELECT t.orgName name,t.orgCode code ,t.tenantId,t.head,t.headMobile,t.description,t.lngLats,t.address,t.email,t.parentId,'2' flag, ");
		sql.append(" b.id orgInfoId,b.orgid,b.orgCid,b.abbr,b.beginYear,b.endYear,b.orgType,b.isDzd,b.isReseau,b.isMgr ");
		sql.append(" from cloud_organization t,cloud_org_info b ");
		sql.append(" where t.id=b.orgId ");
		sql.append("   and t.beenDeleted=0 ");
		sql.append("   and b.beenDeleted=0 ");
		sql.append("   and t.parentId=? ");
		sql.append(" )t ");
		return sql;
	}

	private StringBuffer getSqlAll() {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from  ");
		sql.append(" ((select a.depName name,a.depCode code ,a.tenantId,a.head,a.headMobile,a.description,a.lngLats,a.address,a.email,'' parentId,'1' flag, ");
		sql.append(" 				 b.id orgInfoId,b.orgid,b.orgCid,b.abbr,b.beginYear,b.endYear,b.orgType,b.isDzd,b.isReseau,b.isMgr ");
		sql.append(" from cloud_department a,cloud_org_info b ");
		sql.append(" where a.id=b.orgId ");
		sql.append("   and a.beenDeleted=0 ");
		sql.append("   and b.beenDeleted=0 ");
		sql.append("   and a.tenantId=? ");
		sql.append(" order by a.orderIndex) ");
		sql.append(" UNION ALL ");
		sql.append(" (SELECT t.orgName name,t.orgCode code ,t.tenantId,t.head,t.headMobile,t.description,t.lngLats,t.address,t.email,t.parentId,'2' flag, ");
		sql.append(" b.id orgInfoId,b.orgid,b.orgCid,b.abbr,b.beginYear,b.endYear,b.orgType,b.isDzd,b.isReseau,b.isMgr ");
		sql.append(" from cloud_organization t,cloud_org_info b ");
		sql.append(" where t.id=b.orgId ");
		sql.append("   and t.beenDeleted=0 ");
		sql.append("   and b.beenDeleted=0 ");
		sql.append("   and t.tenantId=? ");
		sql.append(" order by t.orderIndex)) t ");
		return sql;
	}

	@Override
	public OrgInfoDto loadDeptInfo(String id) throws Exception {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		List<Object> argList = Lists.newArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from( ");
		sql.append(" select a.depName name,a.depCode code ,a.tenantId,a.head,a.headMobile,a.description,a.lngLats,a.address,a.email,'' parentId,'1' flag, ");
		sql.append(" b.id orgInfoId,b.orgid,b.orgCid,b.abbr,b.beginYear,b.endYear,b.orgType,b.isDzd,b.isReseau,b.isMgr,a.orderIndex ");
		sql.append(" from cloud_department a,cloud_org_info b ");
		sql.append(" where a.id=b.orgId ");
		sql.append("   and a.beenDeleted=0 ");
		sql.append("   and b.beenDeleted=0 ");
		sql.append("   and a.id=? ");
		sql.append(" )t ");

		argList.add(id);

		List<OrgInfoDto> list = jdbcTemplate.query(sql.toString(), argList.toArray(), BeanPropertyRowMapper.newInstance(OrgInfoDto.class));
		if (CollectionUtils.isNotEmpty(list)) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public OrgInfoDto loadOrgInfo(String id) throws Exception {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		List<Object> argList = Lists.newArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from( ");
		sql.append(" SELECT t.orgName name,t.orgCode code ,t.tenantId,t.head,t.headMobile,t.description,t.lngLats,t.address,t.email,t.parentId,'2' flag, ");
		sql.append(" b.id orgInfoId,b.orgid,b.orgCid,b.abbr,b.beginYear,b.endYear,b.orgType,b.isDzd,b.isReseau,b.isMgr,t.orderIndex ");
		sql.append(" from cloud_organization t,cloud_org_info b ");
		sql.append(" where t.id=b.orgId ");
		sql.append("   and t.beenDeleted=0 ");
		sql.append("   and b.beenDeleted=0 ");
		sql.append("   and t.id=? ");
		sql.append(" )t ");

		argList.add(id);

		List<OrgInfoDto> list = jdbcTemplate.query(sql.toString(), argList.toArray(), BeanPropertyRowMapper.newInstance(OrgInfoDto.class));
		if (CollectionUtils.isNotEmpty(list)) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<IdNameDto> getOrgsByType(String tenantId, String parentId, String orgType) throws Exception {
		if (StringUtils.isEmpty(tenantId) || StringUtils.isEmpty(orgType) || StringUtils.isEmpty(parentId) && !OrgTypeEnum.ORG_QU.getKey().equals(orgType)) {
			return null;
		}
		List<Object> argList = Lists.newArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.id,a.orgName name ");
		sql.append(" from cloud_organization a,cloud_org_info b ");
		sql.append(" where a.id=b.orgId ");
		sql.append("   and a.beenDeleted=0 ");
		sql.append("   and b.beenDeleted=0 ");
		sql.append("   and a.tenantId=? ");
		sql.append("   and b.orgType=? ");
		argList.add(tenantId);
		argList.add(orgType);
		if (!OrgTypeEnum.ORG_QU.getKey().equals(orgType)) {
			sql.append(" and a.parentId=? ");
			argList.add(parentId);
		}
		sql.append(" ORDER BY a.orderIndex ");

		return jdbcTemplate.query(sql.toString(), argList.toArray(), BeanPropertyRowMapper.newInstance(IdNameDto.class));
	}

	@Override
	public List<IdNameDto> getOrgsByNames(String tenantId, List<String> names) throws Exception {
		if (StringUtils.isEmpty(tenantId) || CollectionUtils.isEmpty(names)) {
			return null;
		}

		String str = "";
		for (int i = 0; i < names.size(); i++) {
			if (i == 0) {
				str += "?";
			} else {
				str += ",?";
			}
		}

		StringBuffer sql = new StringBuffer();
		sql.append(" select id,name from ( ");
		sql.append(" select id,orgName name,orderIndex from cloud_organization where tenantId=? ");
		sql.append(" UNION all ");
		sql.append(" select id,depname,orderIndex from cloud_department where tenantId=? ");
		sql.append(" ) t where t.name in (" + str + ") GROUP BY t.name ORDER BY orderIndex ");

		List<Object> argList = Lists.newArrayList();
		argList.add(tenantId);
		argList.add(tenantId);
		argList.addAll(names);

		return jdbcTemplate.query(sql.toString(), argList.toArray(), BeanPropertyRowMapper.newInstance(IdNameDto.class));
	}
}
