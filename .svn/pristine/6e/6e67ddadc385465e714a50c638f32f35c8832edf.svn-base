package com.vortex.cloud.ums.dataaccess.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vortex.cloud.ums.dataaccess.dao.ICloudMessageAuthCodeDao;
import com.vortex.cloud.ums.dataaccess.service.ICloudMessageAuthCodeService;
import com.vortex.cloud.ums.model.CloudMessageAuthCode;
import com.vortex.cloud.vfs.data.hibernate.repository.HibernateRepository;
import com.vortex.cloud.vfs.data.hibernate.service.SimplePagingAndSortingService;

@Transactional
@Service("cloudMessageAuthCodeService")
public class CloudMessageAuthCodeServiceImpl extends SimplePagingAndSortingService<CloudMessageAuthCode, String>
		implements ICloudMessageAuthCodeService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private ICloudMessageAuthCodeDao cloudMessageAuthCodeDao;

	@Override
	public HibernateRepository<CloudMessageAuthCode, String> getDaoImpl() {
		return cloudMessageAuthCodeDao;
	}

}
