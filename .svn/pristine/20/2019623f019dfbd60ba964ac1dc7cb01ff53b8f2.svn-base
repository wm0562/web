package com.vortex.cloud.ums.xm.dataaccess.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.domain.Page;

import com.vortex.cloud.ums.xm.dto.SysOrgQueryDto;
import com.vortex.cloud.ums.xm.dto.SysOrgXmDto;

public interface ISysOrgXmService {
	/**
	 * 保存sysorg
	 * 
	 * @param info
	 * @throws Exception
	 */
	public void saveOrg(SysOrgXmDto info) throws Exception;

	/**
	 * 更新sysorg
	 * 
	 * @param info
	 * @throws Exception
	 */
	public void updateOrg(SysOrgXmDto info) throws Exception;

	/**
	 * 加载sysorg，需要将父节点名称同步加载
	 * 
	 * @param id
	 * @throws Exception
	 */
	public SysOrgXmDto loadOrg(String id) throws Exception;

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
	 * 查询code是否存在；id为空的时候默认是新增的时候判重复，直接查询；id不为空的时候，默认是修改的时候判重复，需要将本身那个code剔除后判断。
	 * 
	 * @param code
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean isCodeExists(String code, String id) throws Exception;

	/**
	 * 删除sysorg
	 * 
	 * @param ids
	 *            部门或机构id列表
	 * @throws Exception
	 */
	public void deleteOrgs(List<String> ids) throws Exception;

	/**
	 * 部门分页查询
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public Page<SysOrgXmDto> queryOrgInfo(SysOrgQueryDto dto) throws Exception;

	/**
	 * 根据单位类型，查询单位列表
	 * 
	 * @param orgType
	 * @return
	 * @throws Exception
	 */
	public List<SysOrgXmDto> listByType(String orgType) throws Exception;

	/**
	 * 根据ids获取名称列表
	 * 
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public LinkedHashMap<String, String> getNamesByIds(List<String> ids) throws Exception;

	/**
	 * 根据名称列表，获取ids
	 * 
	 * @param names
	 * @return
	 * @throws Exception
	 */
	public LinkedHashMap<String, String> getIdsByNames(List<String> names) throws Exception;

	/**
	 * 是否有子节点
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean hasChild(String id) throws Exception;

	/**
	 * 获取所有的sysorg
	 * 
	 * @return
	 */
	public Object getAllSysOrg();
}
