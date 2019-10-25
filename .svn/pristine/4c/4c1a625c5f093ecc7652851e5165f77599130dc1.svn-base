package com.vortex.cloud.ums.dataaccess.dao.xm;

import java.util.List;

import org.springframework.data.domain.Page;

import com.vortex.cloud.ums.dto.IdNameDto;
import com.vortex.cloud.ums.xm.dto.SysOrgQueryDto;
import com.vortex.cloud.ums.xm.dto.SysOrgXmDto;
import com.vortex.cloud.ums.xm.model.SysOrgXm;
import com.vortex.cloud.vfs.data.hibernate.repository.HibernateRepository;

public interface ISysOrgXmDao extends HibernateRepository<SysOrgXm, String> {
	public List<SysOrgXmDto> listAll();

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
	public List<IdNameDto> getNamesByIds(List<String> ids) throws Exception;

	/**
	 * 根据名称列表，获取ids
	 * 
	 * @param names
	 * @return
	 * @throws Exception
	 */
	public List<IdNameDto> getIdsByNames(List<String> names) throws Exception;

	/**
	 * 代码是否存在
	 * 
	 * @param code
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean isCodeExists(String code, String id) throws Exception;

	/**
	 * 是否有子节点
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean hasChild(String id) throws Exception;
}
