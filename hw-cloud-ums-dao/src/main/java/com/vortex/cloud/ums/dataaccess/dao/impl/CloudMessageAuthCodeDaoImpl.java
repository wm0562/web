package com.vortex.cloud.ums.dataaccess.dao.impl;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.vortex.cloud.ums.dataaccess.dao.ICloudMessageAuthCodeDao;
import com.vortex.cloud.ums.model.CloudMessageAuthCode;
import com.vortex.cloud.vfs.data.hibernate.repository.SimpleHibernateRepository;

@Repository("cloudMessageAuthCodeDao")
public class CloudMessageAuthCodeDaoImpl extends SimpleHibernateRepository<CloudMessageAuthCode, String>
		implements ICloudMessageAuthCodeDao {

	@Override
	public DetachedCriteria getDetachedCriteria() {
		return defaultCriteria();
	}

	private DetachedCriteria defaultCriteria() {
		DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass(), "cloudMessageAuthCode");
		criteria.add(Restrictions.eq("beenDeleted", 0));
		return criteria;
	}

}
