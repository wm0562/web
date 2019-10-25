package com.vortex.cloud.ums.dataaccess.dao.impl;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.vortex.cloud.ums.dataaccess.dao.ICloudTenantResourceDao;
import com.vortex.cloud.ums.model.CloudTenantResource;
import com.vortex.cloud.vfs.data.hibernate.repository.SimpleHibernateRepository;
import com.vortex.cloud.vfs.data.model.BakDeleteModel;

@SuppressWarnings("all")
@Repository("cloudTenantResourceDao")
public class CloudTenantResourceDaoImpl extends SimpleHibernateRepository<CloudTenantResource, String>
		implements ICloudTenantResourceDao {

	@Override
	public DetachedCriteria getDetachedCriteria() {
		DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass(), "cloudTenantResource");
		criteria.add(Restrictions.eq("beenDeleted", BakDeleteModel.NO_DELETED));

		return criteria;
	}

}
