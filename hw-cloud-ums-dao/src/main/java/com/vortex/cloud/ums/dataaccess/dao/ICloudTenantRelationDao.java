package com.vortex.cloud.ums.dataaccess.dao;

import java.util.List;

import com.vortex.cloud.ums.dto.TenantDto;
import com.vortex.cloud.ums.dto.tenantgroup.CloudTenantRelationDto;
import com.vortex.cloud.ums.dto.tenantgroup.TenantInfoDto;
import com.vortex.cloud.ums.model.CloudTenantRelation;
import com.vortex.cloud.vfs.data.hibernate.repository.HibernateRepository;

public interface ICloudTenantRelationDao extends HibernateRepository<CloudTenantRelation, String> {
	/**
	 * 得到租户列表（除去租户关系表中的主副租户）
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
	 * 主租户下属的子租户列表
	 * 
	 * @param mainTenantId
	 * @return
	 * @throws Exception
	 */
	public List<TenantDto> listViceTenantDto(String mainTenantId) throws Exception;

	/**
	 * 删除关联关系
	 * 
	 * @param mainTeanantId
	 * @throws Exception
	 */
	public void deleteRelation(String mainTenantId) throws Exception;

	/**
	 * 判断两个租户是否在同一组集团—子公司关系内
	 * 
	 * @param tenantId1
	 * @param tenantId2
	 * @return
	 */
	public boolean isInSameGroupCompany(String tenantId1, String tenantId2);

	/**
	 * 根据子租户id获取主租户id
	 * 
	 * @param viceTenantId
	 * @return
	 * @throws Exception
	 */
	public String getMainTenantId(String viceTenantId) throws Exception;

	/**
	 * 根据租户id，得到租户关联关系
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public CloudTenantRelationDto getRelationByTenantId(String id) throws Exception;
}
