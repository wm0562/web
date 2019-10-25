package com.vortex.cloud.ums.dataaccess.dao;

import java.util.List;

import org.springframework.data.domain.Page;

import com.vortex.cloud.ums.dto.IdNameDto;
import com.vortex.cloud.ums.dto.OrgInfoDto;
import com.vortex.cloud.ums.dto.OrgInfoQueryDto;
import com.vortex.cloud.ums.model.CloudOrgInfo;
import com.vortex.cloud.vfs.data.hibernate.repository.HibernateRepository;

public interface ICloudOrgInfoDao extends HibernateRepository<CloudOrgInfo, String> {
	/**
	 * 根据deptid或者orgid，找到附属信息表
	 * 
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	public CloudOrgInfo findByOrgId(String orgId) throws Exception;

	/**
	 * 根据orgids找到附属信息ids
	 * 
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public List<String> listIdsByOrgids(List<String> ids) throws Exception;

	/**
	 * 部门分页查询
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public Page<OrgInfoDto> queryOrgInfo(OrgInfoQueryDto dto) throws Exception;

	/**
	 * 根据id加载dept和orginfo表信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public OrgInfoDto loadDeptInfo(String id) throws Exception;

	/**
	 * 根据id加载org和orginfo表信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public OrgInfoDto loadOrgInfo(String id) throws Exception;

	/**
	 * 查询租户下org列表
	 * 
	 * @param tenantId
	 * @param parentId
	 * @param orgType
	 * @return
	 * @throws Exception
	 */
	public List<IdNameDto> getOrgsByType(String tenantId, String parentId, String orgType) throws Exception;

	/**
	 * 根据部门名称列表，得到名称-id的对应关系；导入的时候用到
	 * 
	 * @param tenantId
	 * @param names
	 * @return
	 * @throws Exception
	 */
	public List<IdNameDto> getOrgsByNames(String tenantId, List<String> names) throws Exception;
}
