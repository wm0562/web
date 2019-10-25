package com.vortex.cloud.ums.dataaccess.service;

import com.vortex.cloud.ums.dto.CloudPortalUserDto;
import com.vortex.cloud.ums.model.CloudPortalUser;
import com.vortex.cloud.vfs.data.hibernate.service.PagingAndSortingService;

public interface ICloudPortalUserService extends PagingAndSortingService<CloudPortalUser, String> {


	/**
	 * @Title: saveDto
	 * 
	 * @Description: 保存信息
	 * 
	 * @return Boolean
	 * 
	 * @throws
	 */
	public void saveDto(CloudPortalUserDto dto);

	/**
	 * @Title: updateDto
	 * 
	 * @Description: 修改信息
	 * 
	 * @return Boolean
	 * 
	 * @throws
	 */
	public void updateDto(CloudPortalUserDto dto);

	/**
	 * @Title: getById
	 * 
	 * @Description: 根据id获取用户
	 * 
	 * @return CloudPortalUserDto
	 * 
	 * @throws
	 */
	public CloudPortalUserDto getById(String id);

	/**
	 * @Title: changePassword
	 * 
	 * @Description: 修改用户密码
	 * 
	 * @return void
	 * 
	 * @throws
	 */
	public void changePassword(String id, String oldPwd, String newPwd) throws Exception;

	/**
	 * @Title: sendMessageAuthCode
	 * 
	 * @Description: 调用接口发送注册，或忘记密码短信
	 * 
	 * @return Integer
	 * 
	 * @throws
	 */
	public Integer sendMessageAuthCode(String phone, String msgType, String tenantName, String tenantId)throws Exception;

	
	/**
	* @Title: checkAuthCode
	* 
	* @Description: 校验验证码
	* 
	* @return boolean 
	* 
	* @throws
	*/
	public boolean checkAuthCode(String phone, String msgType,String authCode, String tenantId);
	
	/**
	* @Title: resetPassword
	* 
	* @Description: 重置用户密码
	* 
	* @return void 
	* 
	* @throws
	*/
	public void resetPassword(String id)throws Exception;

}
