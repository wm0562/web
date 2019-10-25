package com.vortex.cloud.ums.dataaccess.service;

import java.util.List;
import java.util.Map;

import com.vortex.cloud.ums.model.CloudTenantResource;
import com.vortex.cloud.vfs.data.hibernate.service.PagingAndSortingService;

/**
 * @ClassName: ICloudTenantResourceService
 * @Description: 租户资源表
 * @author ZQ shan
 * @date 2017年12月19日 下午3:55:50
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface ICloudTenantResourceService extends PagingAndSortingService<CloudTenantResource, String> {

	/**
	 * @Title: saveList @Description: 新增 @return void @throws
	 */
	public void saveList(String tenantId, String tenantCode, String roleId, String roleCode, String resourceTypeCode,
			String resourceIdList);

	/**
	 * @Title: hasResource @Description: 判断是否有资源(根據userId) @return Boolean @throws
	 */
	public Boolean hasResourceByUserId(String userId, String resourceTypeCode, String resourceId);

	/**
	 * @Title: hasResource @Description: 判断是否有资源(根據roleId) @return Boolean @throws
	 */
	public Boolean hasResourceByRoleId(String roleId, String resourceTypeCode, String resourceId);

	/**
	 * @Title: delByRoleId @Description: 根据角色id删除数据 @return void @throws
	 */
	public void delByRoleId(String roleId);

	/**
	 * @Title: delByIds @Description: 根据ids删除数据 @return void @throws
	 */
	public void delByIds(String ids);

	/**
	 * @Title: delByParam @Description: 根据参数删除数据 @return void @throws
	 */
	public void delByParam(Map<String, String> paramMap);

	/**
	 * @Title: getResourceIdListByUserId @Description:
	 *         根据userId、resourceTypeCode获取资源列表 @return List<String> @throws
	 */
	public List<String> getResourceIdListByUserId(String userId, String resourceTypeCode);

	/**
	 * @Title: getResourceIdListByRoleId @Description:
	 *         根据roleId、resourceTypeCode获取资源列表 @return List<String> @throws
	 */
	public List<String> getResourceIdListByRoleId(String roleId, String resourceTypeCode);
}
