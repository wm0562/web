package com.vortex.cloud.ums.dataaccess.service;

import com.vortex.cloud.ums.dto.UmsLoginReturnInfoDto;

public interface ILoginService {
	/**
	 * 用户登录
	 * 
	 * @param tenantCode
	 * @param systemCode
	 * @param userName
	 * @param password
	 * @return
	 */
	public UmsLoginReturnInfoDto login(String tenantCode, String systemCode, String userName, String password,
			String ip);

	/**
	 * 用户登录,并且更新他的推送id
	 * 
	 * @param tenantCode
	 * @param systemCode
	 * @param userName
	 * @param password
	 * @param mobilePushMsgId
	 * @return
	 */
	UmsLoginReturnInfoDto login(String tenantCode, String systemCode, String userName, String password,
			String mobilePushMsgId, String ip) throws Exception;

	/**
	 * 根据userName，获取登录token
	 * 
	 * @param userName
	 * @return
	 */
	public String getToken(String userName) throws Exception;

	/**
	 * 
	 * @param userName
	 *            用户名
	 * @param evidence
	 *            密码或者token
	 * @param ip
	 *            登录ip
	 * @return
	 */
	public UmsLoginReturnInfoDto loginByPasswordOrToken(String tenantCode, String systemCode, String userName,
			String evidence, String ip) throws Exception;

	/**
	 * @Title: loginThirdPartyApp
	 * 
	 * @Description: 第3方app登录
	 * 
	 * @return UmsLoginReturnInfoDto
	 * 
	 * @throws
	 */
	UmsLoginReturnInfoDto loginFromThirdPartyApp(String appKey, String appSecret, String ip) throws Exception;
	
	
	/**
	 * @Title: loginPortalUser
	 * 
	 * @Description: 门户登录
	 * 
	 * @return UmsLoginReturnInfoDto
	 * 
	 * @throws
	 */
	UmsLoginReturnInfoDto loginPortalUser(String userName, String password, String ip,String tenantId) throws Exception;
}
