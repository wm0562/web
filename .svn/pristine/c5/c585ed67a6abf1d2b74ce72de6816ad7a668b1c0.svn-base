package com.vortex.cloud.ums.dataaccess.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vortex.cloud.ums.dto.CloudUserRoleDto;
import com.vortex.cloud.ums.dto.CloudUserRoleSearchDto;
import com.vortex.cloud.ums.model.CloudUserRole;
import com.vortex.cloud.vfs.data.hibernate.repository.HibernateRepository;

/**
 * 用户角色dao
 * 
 * @author lsm
 * @date 2016年4月1日
 */
public interface ICloudUserRoleDao extends HibernateRepository<CloudUserRole, String> {

	Page<CloudUserRoleDto> findPageBySearchDto(Pageable pageable, CloudUserRoleSearchDto searchDto);

	/**
	 * 根据用户id和角色id，查询有效的关联关系表数据
	 * 
	 * @param userId
	 * @param roleId
	 * @return
	 */
	public CloudUserRole findByUidAndRid(String userId, String roleId);

	/**
	 * 删除用户上面的所有角色
	 * 
	 * @param userId
	 */
	void deleteByUserId(String userId);
}
