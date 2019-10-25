package com.vortex.cloud.ums.dataaccess.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vortex.cloud.ums.dto.CloudStaffDto;
import com.vortex.cloud.ums.dto.CloudStaffPageDto;
import com.vortex.cloud.ums.dto.CloudStaffSearchDto;
import com.vortex.cloud.ums.model.CloudStaff;
import com.vortex.cloud.vfs.data.hibernate.service.PagingAndSortingService;

public interface ICloudStaffXmService extends PagingAndSortingService<CloudStaff, String> {
	/**
	 * 查询编号是否存在，存在返回true，传入代码为空返回false
	 * 
	 * @param tenantId
	 * @param code
	 * @return
	 */
	public boolean isCodeExisted(String tenantId, String code);

	/**
	 * 保存
	 * 
	 * @param dto
	 * @return
	 */
	public CloudStaff save(CloudStaffDto dto);

	/**
	 * 根据指定的id，获取记录
	 * 
	 * @param id
	 * @return
	 */
	public CloudStaffDto getById(String id);

	boolean validateCodeOnUpdate(String tenantId, String id, String newCode);

	/**
	 * 更新
	 * 
	 * @param dto
	 */
	public void update(CloudStaffDto dto);

	Page<CloudStaffDto> findPageBySearchDto(Pageable pageable, CloudStaffSearchDto searchDto);

	/**
	 * 根据ids获取id和 name的map
	 * 
	 * @param ids
	 * @return
	 */
	public Map<String, String> getStaffNamesByIds(List<String> ids);

	/**
	 * 根据名字获取ids
	 * 
	 * @param names
	 *            staffNames
	 * @param names
	 *            tenantId
	 * @return
	 */
	public Map<String, String> getStaffIdsByNames(List<String> names, String tenantId);

	/**
	 * 根据ids获取id和 信息的map
	 * 
	 * @param ids
	 * @return
	 */
	Map<String, Object> getStaffsByIds(List<String> ids);

	/**
	 * 根据用户ids获取id和 信息的map
	 * 
	 * @param ids
	 * @return
	 */
	Map<String, Object> getStaffsByUserIds(List<String> ids);

	/**
	 * 根据id删除对应的user和staff
	 * 
	 * @param id
	 * @throws Exception
	 */
	public void deleteStaffAndUser(String id) throws Exception;

	/**
	 * 将dto变为model
	 * 
	 * @param list
	 * @return
	 */
	public List<CloudStaffDto> transferModelToDto(List<CloudStaff> list);

	/**
	 * 校验该数据能否被删除
	 * 
	 * @param id
	 * @return
	 */
	boolean canBeDeleted(String id);

	/**
	 * 删除多条数据
	 * 
	 * @param deleteList
	 */
	public void deletesStaffAndUser(List<String> deleteList);

	/**
	 * 验证社保卡号是否存在
	 * 
	 * @param socialSecurityNo
	 * @return
	 */
	public boolean isSocialSecurityNoExist(String staffId, String socialSecurityNo);

	/**
	 * 身份证号是否存在
	 * 
	 * @param credentialNum
	 * @return
	 */
	public boolean isCredentialNumExist(String staffId, String credentialNum);

	/**
	 * 判断手机号是否存在
	 * 
	 * @param id
	 *            staffId
	 * @param phone
	 *            用户手机
	 * @return
	 */
	public boolean isPhoneExists(String id, String phone);

	/**
	 * 根据参数来获取人员信息，并且带上用户信息
	 * 
	 * @param paramMap
	 *            参数map
	 * @return
	 */
	public List<CloudStaffDto> loadStaffsByFilter(Map<String, Object> paramMap);

	/**
	 * 根据条件和人员权限过滤人员列表
	 * 
	 * @param pageable
	 * @param searchDto
	 * @return
	 */
	public Page<CloudStaffDto> findPageWithPermissionBySearchDto(Pageable pageable, CloudStaffSearchDto searchDto);

	/**
	 * 设置名字首字母
	 */
	public void setNameInitial();

	/**
	 * 同步人员信息
	 * 
	 * @param tenantId
	 * @param syncTime
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	public List<CloudStaffPageDto> syncStaffByPage(String tenantId, long syncTime, Integer pageSize, Integer pageNumber);

	/**
	 * 
	 * @param tenantId
	 * @param isDeleted
	 * @return
	 */
	public List<CloudStaffPageDto> findAllStaffByPage(String tenantId, Integer isDeleted);

	/**
	 * 锁定用户
	 * 
	 * @param userId
	 * @throws Exception
	 */
	public void lockUser(String userId) throws Exception;

	/**
	 * 解锁用户
	 * 
	 * @param userId
	 * @throws Exception
	 */
	public void unlockUser(String userId) throws Exception;

	/**
	 * 批量授权
	 * 
	 * @param userIds
	 * @param roleIds
	 * @throws Exception
	 */
	public void empower(List<String> userIds, List<String> roleIds) throws Exception;

	/**
	 * 批量删除权限
	 * 
	 * @param userIds
	 * @param roleIds
	 * @throws Exception
	 */
	public void repower(List<String> userIds, List<String> roleIds) throws Exception;

}
