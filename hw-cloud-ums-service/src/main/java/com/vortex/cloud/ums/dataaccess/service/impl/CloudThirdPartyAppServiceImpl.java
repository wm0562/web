package com.vortex.cloud.ums.dataaccess.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vortex.cloud.ums.dataaccess.dao.ICloudThirdPartyAppDao;
import com.vortex.cloud.ums.dataaccess.service.ICloudThirdPartyAppService;
import com.vortex.cloud.ums.model.CloudThirdPartyApp;
import com.vortex.cloud.vfs.data.hibernate.repository.HibernateRepository;
import com.vortex.cloud.vfs.data.hibernate.service.SimplePagingAndSortingService;

@Transactional
@Service("cloudThirdPartyAppService")
public class CloudThirdPartyAppServiceImpl extends SimplePagingAndSortingService<CloudThirdPartyApp, String> implements ICloudThirdPartyAppService {


	@Resource
	private ICloudThirdPartyAppDao cloudThirdPartyAppDao;

	@Override
	public HibernateRepository<CloudThirdPartyApp, String> getDaoImpl() {
		return cloudThirdPartyAppDao;
	}


}
