package com.vortex.cloud.ums.dataaccess.dao;

import java.util.List;

import com.vortex.cloud.ums.dto.CloudUserFingerprintDto;
import com.vortex.cloud.ums.model.CloudUserFingerprint;
import com.vortex.cloud.vfs.data.hibernate.repository.HibernateRepository;

public interface ICloudUserFingerprintDao extends HibernateRepository<CloudUserFingerprint, String> {
	CloudUserFingerprint findByUserId(String userId);

	/**
	 * 根据时间戳同步租户指纹信息。不包含syncTime，syncTime=0查全部
	 * 
	 * @param tenantId
	 * @param pageSize
	 * @param syncTime
	 * @return
	 * @throws Exception
	 */
	public List<CloudUserFingerprintDto> syncFingerprint(String tenantId, Integer pageSize, Long syncTime) throws Exception;
}
