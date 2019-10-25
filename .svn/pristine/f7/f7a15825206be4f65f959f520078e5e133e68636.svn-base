package com.vortex.cloud.ums.dataaccess.dao;

import java.util.List;

import com.vortex.cloud.ums.dto.UmsLoginReturnInfoDto;
import com.vortex.cloud.ums.model.CloudThirdPartyApp;
import com.vortex.cloud.vfs.data.hibernate.repository.HibernateRepository;

public interface ICloudThirdPartyAppDao
	extends HibernateRepository<CloudThirdPartyApp, String> {
	public List<UmsLoginReturnInfoDto> getLoginInfo(String appKey);
	
}
