package com.vortex.cloud.ums.dataaccess.service;

import java.util.List;

import com.vortex.cloud.ums.dto.CloudUserFingerprintDto;

public interface ICloudUserFingerprintService {
	/**
	 * 录入指纹。在原指纹字符串上面追加
	 * 
	 * @param userId
	 * @param fingerprint
	 * @throws Exception
	 */
	public void saveFingerprint(String userId, String fingerprint) throws Exception;

	/**
	 * 用新指纹代替老指纹串，如果不存在老的指纹，直接新增
	 * 
	 * @param userId
	 * @param fingerprint
	 * @throws Exception
	 */
	public void refreshFingerprint(String userId, String fingerprint) throws Exception;

	/**
	 * 根据用户id，获取该用户的指纹字符串
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public String getFingerprint(String userId) throws Exception;

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
