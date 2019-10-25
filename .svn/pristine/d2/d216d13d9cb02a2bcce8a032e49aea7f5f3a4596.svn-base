package com.vortex.cloud.ums.dataaccess.service;

import java.util.List;

import com.vortex.cloud.ums.dto.tenantgroup.SystemListDto;

/**
 * 集团用户相关
 * 
 * @author XY
 *
 */
public interface IGroupCompanyUserService {
	/**
	 * 根据用户id，获得菜单框的系统切换列表
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<SystemListDto> getSystemList(String userId) throws Exception;

	/**
	 * 集团用户切换系统。
	 * 
	 * @param userId
	 *            当前账号id
	 * @param targetSystemId
	 *            目标系统id
	 * @return
	 * @throws Exception
	 */
	public String getAccount(String userId, String targetSystemId) throws Exception;
}
