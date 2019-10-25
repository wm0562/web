package com.vortex.cloud.ums.dataaccess.dao;

import java.util.List;

import com.vortex.cloud.ums.dto.CloudDeptOrgDto;
import com.vortex.cloud.ums.dto.TenantDeptOrgDto;
import com.vortex.cloud.ums.dto.TreeDto;
import com.vortex.cloud.ums.model.CloudOrganization;
import com.vortex.cloud.vfs.data.hibernate.repository.HibernateRepository;

/**
 * 组织机构dao
 * 
 * @author LiShijun
 *
 */
public interface ICloudOrganizationDao extends HibernateRepository<CloudOrganization, String> {
	/**
	 * 根据机构code和租户Id，得到机构信息
	 * 
	 * 
	 * @param orgCode
	 * @param tenantId
	 * @return
	 */
	CloudOrganization getOrganizationByCode(String orgCode, String tenantId);

	/**
	 * 是否存在有效的子机构
	 * 
	 * @param orgId
	 * @return
	 */
	boolean hasChild(String orgId);

	/**
	 * 是否存在有效的人员
	 * 
	 * @param orgId
	 * @return
	 */
	boolean hasStaff(String orgId);

	/**
	 * 根据部门id和nodecode查找子部门列表
	 * 
	 * @param departId
	 *            部门id
	 * @param nodeCode
	 * @param id
	 * @return
	 */
	List<TenantDeptOrgDto> findOrganizationChild(String departId, String nodeCode, String id, List<Integer> beenDeletedFlags);

	/**
	 * 根据id查找depart或者是org
	 * 
	 * @param ids
	 * @return
	 */
	List<TenantDeptOrgDto> getDepartmentsOrOrgByIds(String[] ids, List<Integer> beenDeletedFlags);

	/**
	 * id查找
	 * 
	 * @param id
	 * @param beenDeletedFlags
	 * @return
	 */
	public CloudOrganization findById(String id, List<Integer> beenDeletedFlags);

	/**
	 * 根据表名和父节点id得到最大的nodeCode
	 * 
	 * @param tabName
	 * @param parentIdColName
	 * @param parentId
	 * @return
	 */
	public String getMaxNodeCodeByParentId(String tabName, String parentIdColName, String parentId);

	/**
	 * 租户下第一层org的最大nodecode的下一个值
	 * 
	 * @param tenantId
	 * @return
	 */
	public String getNextNodeCodeByFirstLevel(String tenantId);

	/**
	 * 根据部门id，得到子机构的列表
	 * 
	 * @param parent
	 * @return
	 * @throws Exception
	 */
	public List<TreeDto> listOrgByDeptId(String deptId) throws Exception;

	/**
	 * 根据机构id，得到其下所有子机构的列表
	 * 
	 * @param deptId
	 * @return
	 * @throws Exception
	 */
	public List<TreeDto> listOrgByParentId(String parentId) throws Exception;

	/**
	 * 根据id得到部门机构信息，带父节点id
	 * 
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public List<CloudDeptOrgDto> listByIds(List<String> ids) throws Exception;
}
