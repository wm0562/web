package com.vortex.cloud.ums.dataaccess.dao;

import java.util.List;

import com.vortex.cloud.ums.dto.UmsLoginReturnInfoDto;
import com.vortex.cloud.ums.model.CloudPortalUser;
import com.vortex.cloud.vfs.data.hibernate.repository.HibernateRepository;

/**
 * @ClassName: ICloudPortalUserDao
 * @Description: 门户网站人员
 * @author ZQ shan
 * @date 2018年1月29日 下午2:12:34
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface ICloudPortalUserDao extends HibernateRepository<CloudPortalUser, String> {

	/**
	* @Title: getLoginInfo
	* 
	* @Description:根据用户名获取用户信息
	* 
	* @return List<UmsLoginReturnInfoDto> 
	* 
	* @throws
	*/
	public List<UmsLoginReturnInfoDto> getLoginInfo(String userName,String tenantId);
}
