package com.vortex.cloud.ums.dataaccess.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.vortex.cloud.ums.dataaccess.dao.ICloudPortalUserDao;
import com.vortex.cloud.ums.dto.UmsLoginReturnInfoDto;
import com.vortex.cloud.ums.model.CloudPortalUser;
import com.vortex.cloud.vfs.data.hibernate.repository.SimpleHibernateRepository;
import com.vortex.cloud.vfs.data.model.BakDeleteModel;

@Repository("cloudPortalUserDao")
public class CloudPortalUserDaoImpl extends SimpleHibernateRepository<CloudPortalUser, String>
		implements ICloudPortalUserDao {
	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public DetachedCriteria getDetachedCriteria() {
		return defaultCriteria();
	}

	private DetachedCriteria defaultCriteria() {
		DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass(), "cloudPortalUser");
		criteria.add(Restrictions.eq("beenDeleted", 0));
		return criteria;
	}

	@Override
	public List<UmsLoginReturnInfoDto> getLoginInfo(String userName, String tenantId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = Lists.newArrayList();
		sql.append(
				" SELECT  u.userName,u.id userId,u.password,u.profilePhoto as photoId,u.nickname as name,u.phone,u.tenantId ");
		sql.append(" FROM  cloud_portal_user u");
		sql.append("       WHERE u.beenDeleted=? and u.tenantId=? and ( u.userName = ? or   u.phone = ? ) ");

		args.add(BakDeleteModel.NO_DELETED);
		args.add(tenantId);
		args.add(userName);
		args.add(userName);

		return jdbcTemplate.query(sql.toString(), args.toArray(),
				BeanPropertyRowMapper.newInstance(UmsLoginReturnInfoDto.class));
	}

}
