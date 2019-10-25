package com.vortex.cloud.ums.dataaccess.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.vortex.cloud.ums.dataaccess.dao.ICloudUserFingerprintDao;
import com.vortex.cloud.ums.dto.CloudUserFingerprintDto;
import com.vortex.cloud.ums.model.CloudUserFingerprint;
import com.vortex.cloud.vfs.data.hibernate.repository.SimpleHibernateRepository;
import com.vortex.cloud.vfs.data.model.BakDeleteModel;
import com.vortex.cloud.vfs.data.support.SearchFilter;
import com.vortex.cloud.vfs.data.support.SearchFilter.Operator;

@Repository("cloudUserFingerprintDao")
public class CloudUserFingerprintDaoImpl extends SimpleHibernateRepository<CloudUserFingerprint, String> implements ICloudUserFingerprintDao {
	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public DetachedCriteria getDetachedCriteria() {
		return defaultCriteria();
	}

	private DetachedCriteria defaultCriteria() {
		DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass(), "cloudUserFingerprint");
		criteria.add(Restrictions.eq("beenDeleted", BakDeleteModel.NO_DELETED));
		return criteria;
	}

	@Override
	public CloudUserFingerprint findByUserId(String userId) {
		if (StringUtils.isEmpty(userId)) {
			return null;
		}
		List<SearchFilter> filterList = new ArrayList<>();
		filterList.add(new SearchFilter("userId", Operator.EQ, userId));
		List<CloudUserFingerprint> list = this.findListByFilter(filterList, null);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		} else {
			return list.get(0);
		}
	}

	@Override
	public List<CloudUserFingerprintDto> syncFingerprint(String tenantId, Integer pageSize, Long syncTime) throws Exception {
		if (StringUtils.isEmpty(tenantId) || pageSize == null || syncTime == null) {
			return null;
		}
		List<Object> argList = Lists.newArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append(" select f.* ");
		sql.append(" from cloud_staff s,cloud_user u,cloud_user_fingerprint f ");
		sql.append(" where s.tenantId=? ");
		sql.append("   and s.id=u.staffId ");
		sql.append("   and u.id=f.userId ");
		sql.append("   and s.beenDeleted=0 ");
		sql.append("   and u.beenDeleted=0 ");
		argList.add(tenantId);

		if (syncTime == 0) {
			sql.append(" and f.beenDeleted=0 ");
		} else {
			sql.append(" and UNIX_TIMESTAMP(f.lastChangeTime)*1000>? ");
			argList.add(syncTime);
		}
		sql.append(" ORDER BY f.lastChangeTime ");
		sql.append(" LIMIT ? ");
		argList.add(pageSize);

		return jdbcTemplate.query(sql.toString(), argList.toArray(), BeanPropertyRowMapper.newInstance(CloudUserFingerprintDto.class));
	}
}
