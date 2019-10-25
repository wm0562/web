package com.vortex.cloud.ums.dataaccess.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.vortex.cloud.ums.dto.OrgInfoDto;
import com.vortex.cloud.ums.dto.OrgInfoQueryDto;

/**
 * 厦门二期部门操作
 * 
 * @author XY
 *
 */
public interface IOrgInfoService {
	/**
	 * 一步保存department表和cloudOrgInfo表
	 * 
	 * @param info
	 * @throws Exception
	 */
	public void saveDeptInfo(OrgInfoDto info) throws Exception;

	/**
	 * 一步保存org和orgInfo表
	 * 
	 * @param info
	 * @throws Exception
	 */
	public void saveOrgInfo(OrgInfoDto info) throws Exception;

	/**
	 * 更新dept和orgInfo表
	 * 
	 * @param info
	 * @throws Exception
	 */
	public void updateDeptInfo(OrgInfoDto info) throws Exception;

	/**
	 * 更新org和orgInfo表
	 * 
	 * @param info
	 * @throws Exception
	 */
	public void updateOrgInfo(OrgInfoDto info) throws Exception;

	/**
	 * 删除dept和org，同步删除orgInfo
	 * 
	 * @param ids
	 *            部门或机构id列表
	 * @throws Exception
	 */
	public void deleteOrgs(List<String> ids) throws Exception;

	/**
	 * org更新父节点id；树转枝，此功能比较复杂，涉及到nodecode和childSerialNumer两个字段的刷新
	 * 
	 * @param id
	 *            部门或机构id
	 * @param npid
	 *            新的父节点id
	 * @throws Exception
	 */
	public void changeParent(String id, String npid) throws Exception;

	/**
	 * 查询code是否存在
	 * 
	 * @param code
	 * @param tenantId
	 * @param flag
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean isCodeExists(String code, String tenantId, String flag, String id) throws Exception;

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
	 * 部门分页查询
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public Page<OrgInfoDto> queryOrgInfo(OrgInfoQueryDto dto) throws Exception;

	/**
	 * 得到机构类型枚举
	 * 
	 * @return
	 */
	public Map<String, String> getOrgTypeEnums();

	/**
	 * 查询租户下org列表
	 * 
	 * @param tenantId
	 * @param parentId
	 * @param orgType
	 * @return
	 * @throws Exception
	 */
	public LinkedHashMap<String, String> getOrgsByType(String tenantId, String parentId, String orgType) throws Exception;

	/**
	 * 根据部门名称列表，查询部门id列表，返回name-id键值对
	 * 
	 * @param tenantId
	 * @param names
	 * @return
	 * @throws Exception
	 */
	public LinkedHashMap<String, String> getOrgsByNames(String tenantId, List<String> names) throws Exception;
}
