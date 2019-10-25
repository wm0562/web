package com.vortex.cloud.ums.dataaccess.service;

import java.util.List;

import com.vortex.cloud.ums.dto.TenantDto;
import com.vortex.cloud.ums.dto.tenantgroup.TenantInfoDto;

public interface ICloudTenantRelationService {
	/**
	 * 得到租户列表（除去已被当做子租户使用过的租户）
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<TenantInfoDto> listExceptViceTenant() throws Exception;

	/**
	 * 主租户下属的子租户列表
	 * 
	 * @param mainTenantId
	 * @return
	 * @throws Exception
	 */
	public List<TenantInfoDto> listViceTenant(String mainTenantId) throws Exception;

	/**
	 * 绑定主从租户关系
	 * 
	 * @param mainTeanantId
	 * @param viceTenantIds
	 * @throws Exception
	 */
	public void bandingRelation(String mainTenantId, List<String> viceTenantIds) throws Exception;

	/**
	 * 根据租户id获取当前租户所在的集团的全部树结构。  如果当前租户为集团，则返回当前租户及所有下级租户； 
	 * 如果当前租户为项目子公司，返回当前租户的上级集团及所有兄弟子公司；  如果当前租户为单节点，则返回当前租户即可
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<TenantDto> getGroupTenantList(String id) throws Exception;

	/**
	 * 主租户下属的子租户列表
	 * 
	 * @param mainTenantId
	 * @return
	 * @throws Exception
	 */
	public List<TenantDto> listViceTenantDto(String mainTenantId) throws Exception;

	/**
	 * 根据id得到租户信息，带有上级id；如果是集团，上级id为-1;如果是子公司，上级id为集团id;其他为空
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public TenantDto getById(String id) throws Exception;

	/**
	 * 获取上级租户详情
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public TenantDto getParentTenantById(String id) throws Exception;
}
