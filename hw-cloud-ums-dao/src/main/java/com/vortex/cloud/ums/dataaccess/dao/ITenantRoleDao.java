package com.vortex.cloud.ums.dataaccess.dao;

import java.util.List;

import com.vortex.cloud.ums.model.TenantRole;
import com.vortex.cloud.vfs.data.hibernate.repository.HibernateRepository;

/**
* @ClassName: ITenantRoleDao
* @Description: 租户角色（不挂在业务系统下）Dao
* @author ZQ shan
* @date 2018年1月23日 上午10:12:16
* @see [相关类/方法]（可选）
* @since [产品/模块版本] （可选）
*/
public interface ITenantRoleDao extends HibernateRepository<TenantRole, String> {
	/**
	 * 获取绑定该角色的人员
	 * 
	 * @param tenantId
	 * @param roleCode
	 * @return
	 */
	List<String> getUserIdsByRole(String tenantId, String roleCode);
}
