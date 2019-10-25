package com.vortex.cloud.ums.dataaccess.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vortex.cloud.ums.dataaccess.dao.ICloudStaffDao;
import com.vortex.cloud.ums.dataaccess.dao.ICloudUserDao;
import com.vortex.cloud.ums.dataaccess.dao.ICloudUserRoleDao;
import com.vortex.cloud.ums.dataaccess.service.ICloudOrganizationService;
import com.vortex.cloud.ums.dataaccess.service.ICloudStaffXmService;
import com.vortex.cloud.ums.dataaccess.service.ICloudUserService;
import com.vortex.cloud.ums.dto.CloudStaffDto;
import com.vortex.cloud.ums.dto.CloudStaffPageDto;
import com.vortex.cloud.ums.dto.CloudStaffSearchDto;
import com.vortex.cloud.ums.dto.CloudUserDto;
import com.vortex.cloud.ums.enums.PermissionScopeEnum;
import com.vortex.cloud.ums.model.CloudStaff;
import com.vortex.cloud.ums.model.CloudUser;
import com.vortex.cloud.ums.model.CloudUserRole;
import com.vortex.cloud.ums.util.utils.pinyin4jUtil;
import com.vortex.cloud.vfs.common.digest.MD5;
import com.vortex.cloud.vfs.common.exception.ServiceException;
import com.vortex.cloud.vfs.common.exception.VortexException;
import com.vortex.cloud.vfs.data.hibernate.repository.HibernateRepository;
import com.vortex.cloud.vfs.data.hibernate.service.SimplePagingAndSortingService;
import com.vortex.cloud.vfs.data.support.SearchFilter;
import com.vortex.cloud.vfs.data.support.SearchFilter.Operator;
import com.vortex.cloud.vfs.data.support.SearchFilters;

@Transactional
@Service("cloudStaffXmService")
public class CloudStaffXmServiceImpl extends SimplePagingAndSortingService<CloudStaff, String> implements ICloudStaffXmService {

	Logger logger = LoggerFactory.getLogger(CloudStaffServiceImpl.class);
	@Resource
	private ICloudStaffDao cloudStaffDao;

	@Resource
	private ICloudUserDao cloudUserDao;
	@Resource
	private ICloudOrganizationService cloudOrganizationService;
	@Resource
	private ICloudUserService cloudUserService;
	@Resource
	private ICloudUserRoleDao cloudUserRoleDao;

	@Override
	public HibernateRepository<CloudStaff, String> getDaoImpl() {
		return cloudStaffDao;
	}

	@Override
	public Page<CloudStaffDto> findPageBySearchDto(Pageable pageable, CloudStaffSearchDto searchDto) {
		// 如果没有传orgId或者部门id，那么就直接返回null
		if (StringUtils.isEmpty(searchDto.getDepartmentId()) && StringUtils.isEmpty(searchDto.getOrgId())) {
			return null;
		}
		return cloudStaffDao.findPageBySearchDto(pageable, searchDto);
	}

	@Override
	public boolean isCodeExisted(String tenantId, String code) {
		if (StringUtils.isBlank(code)) {
			return false;
		}

		boolean result = false;

		List<SearchFilter> filterList = new ArrayList<>();
		filterList.add(new SearchFilter("tenantId", Operator.EQ, tenantId));
		filterList.add(new SearchFilter("code", Operator.EQ, code));
		List<CloudStaff> list = this.findListByFilter(filterList, null);

		if (CollectionUtils.isNotEmpty(list)) {
			result = true;
		}

		return result;
	}

	@Override
	public CloudStaff save(CloudStaffDto dto) {
		this.validateOnSave(dto);

		CloudStaff entity = new CloudStaff();
		BeanUtils.copyProperties(dto, entity);

		// 设置姓名全拼
		String pinyin = pinyin4jUtil.getPinYinNoToneAndSpace(entity.getName());
		entity.setNameInitial(pinyin.trim());

		// 保存基础信息表
		entity = cloudStaffDao.save(entity);

		// user信息校验
		CloudUserDto userdto = new CloudUserDto();
		userdto.setUserName(dto.getUserName());
		userdto.setStaffId(entity.getId());
		userdto.setPassword(dto.getPassword());
		cloudUserService.validateOnSave(userdto);

		// 保存user表
		CloudUser user = new CloudUser();
		user.setStaffId(entity.getId()); // 人员基本信息id
		user.setMobilePushMsgId(dto.getMobilePushMsgId()); // 手机推送id
		user.setRongLianAccount(dto.getRongLianAccount()); // 容联帐号
		user.setUserName(dto.getUserName()); // 用户名
		user.setPassword(MD5.getMD5(dto.getPassword())); // 密码
		user.setBeenSsoLogin(true); // 是否可以单点登录
		user.setLastSsoLoginTime(null); // 最后登录时间
		user.setIsRoot(CloudUser.IS_ROOT_NO); // 是否超级管理员
		user.setPhotoId(dto.getPhotoId()); // 头像id
		user.setPermissionScope(StringUtils.isEmpty(dto.getPermissionScope()) ? PermissionScopeEnum.ALL.getKey() : dto.getPermissionScope());// 权限范围对应PermissionScopeEnum的值
		user.setCustomScope(dto.getCustomScope());// 自定义范围 depart，orgId 用,分割
		user.setImToken(dto.getImToken());// 融云imToken
		user.setLockuser(CloudUser.LOCK_NO); // 默认不锁定
		cloudUserDao.save(user);

		return entity;
	}

	public void validateOnSave(CloudStaffDto dto) {

		this.validateForm(dto);

		// 逻辑业务校验
		if (this.isCodeExisted(dto.getTenantId(), dto.getCode())) {
			throw new ServiceException("编号已存在！");
		}
	}

	private void validateForm(CloudStaffDto dto) {
		if (StringUtils.isBlank(dto.getTenantId())) {
			throw new ServiceException("租户ID为空");
		}

		if (StringUtils.isBlank(dto.getDepartmentId())) {
			throw new ServiceException("单位ID为空");
		}

		if (StringUtils.isBlank(dto.getCode())) {
			throw new ServiceException("编码为空");
		}

		if (StringUtils.isBlank(dto.getName())) {
			throw new ServiceException("名称为空");
		}
	}

	@Override
	public CloudStaffDto getById(String id) {
		CloudStaffDto staff = cloudStaffDao.getById(id);
		if (staff == null) {
			throw new ServiceException("不存在id为" + id + "的人员");
		}

		return staff;
	}

	@Override
	public void update(CloudStaffDto dto) {
		// 入参数校验
		this.validateOnUpdate(dto);

		CloudStaff old = cloudStaffDao.findOne(dto.getId());

		BeanUtils.copyProperties(dto, old, "beenDeleted", "deletedTime", "id", "createTime", "lastChangeTime");
		// 不外包，外包公司置为空
		if (null == old.getOutSourcing() || !old.getOutSourcing()) {
			old.setOutSourcingComp("");
		}
		// 设置姓名全拼
		String pinyin = pinyin4jUtil.getPinYinNoToneAndSpace(old.getName());
		old.setNameInitial(pinyin.trim());

		cloudStaffDao.update(old);
	}

	private void validateOnUpdate(CloudStaffDto dto) {

		this.validateForm(dto);

		if (StringUtils.isBlank(dto.getId())) {
			throw new ServiceException("ID为空");
		}

		// 逻辑业务校验
		if (!this.validateCodeOnUpdate(dto.getTenantId(), dto.getId(), dto.getCode())) {
			throw new ServiceException("编号已存在！");
		}
	}

	@Override
	public boolean validateCodeOnUpdate(String tenantId, String id, String newCode) {
		CloudStaff oldOrg = cloudStaffDao.findOne(id);
		String oldCode = oldOrg.getCode();

		if (newCode.equals(oldCode)) // 没有修改
		{
			return true;
		} else {
			boolean isExisted = this.isCodeExisted(tenantId, newCode);
			if (isExisted) {
				return false;
			} else {
				return true;
			}
		}
	}

	@Override
	public Map<String, String> getStaffNamesByIds(List<String> ids) {
		List<CloudStaff> staffs = cloudStaffDao.findAllByIds(ids.toArray(new String[ids.size()]));
		Map<String, String> nameMap = Maps.newHashMap();
		if (CollectionUtils.isNotEmpty(staffs)) {
			for (CloudStaff cloudStaff : staffs) {
				nameMap.put(cloudStaff.getId(), cloudStaff.getName());
			}
		}
		return nameMap;
	}

	@Override
	public Map<String, Object> getStaffsByIds(List<String> ids) {
		List<CloudStaff> staffs = cloudStaffDao.findAllByIds(ids.toArray(new String[ids.size()]));
		Map<String, Object> map = Maps.newHashMap();
		if (CollectionUtils.isNotEmpty(staffs)) {
			for (CloudStaff cloudStaff : staffs) {
				map.put(cloudStaff.getId(), cloudStaff);
			}
		}
		return map;
	}

	@Override
	public Map<String, Object> getStaffsByUserIds(List<String> ids) {
		List<CloudStaffDto> staffs = cloudStaffDao.getStaffsByUserIds(ids);
		Map<String, Object> map = Maps.newHashMap();
		if (CollectionUtils.isNotEmpty(staffs)) {
			for (CloudStaffDto cloudStaff : staffs) {
				map.put(cloudStaff.getUserId(), cloudStaff);
			}
		}
		return map;
	}

	@Override
	public Map<String, String> getStaffIdsByNames(List<String> names, String tenantId) {
		Map<String, String> idMap = Maps.newHashMap();
		List<CloudStaff> cloudStaffs = cloudStaffDao.getStaffIdsByNames(names, tenantId);
		for (CloudStaff cloudStaff : cloudStaffs) {
			idMap.put(cloudStaff.getName(), cloudStaff.getId());
		}
		return idMap;
	}

	@Override
	public void deleteStaffAndUser(String id) throws Exception {
		if (StringUtils.isEmpty(id)) {
			logger.error("id不能为空");
			throw new ServiceException("id不能为空");

		}
		CloudStaff cloudStaff = cloudStaffDao.findOne(id);
		if (null == cloudStaff) {
			logger.error("不存在id为" + id + "的数据");
			throw new ServiceException("不存在id为" + id + "的数据");
		}
		// 能被删除就删除
		if (canBeDeleted(id)) {
			cloudStaffDao.delete(cloudStaff);
		}
		CloudUser cloudUser = cloudUserDao.getUserByStaffId(id);
		// 该人员开通了user就连同staff一起删除
		if (null != cloudUser) {
			cloudUserDao.delete(cloudUser);
		}
	}

	/**
	 * 校验是否能被删除
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public boolean canBeDeleted(String id) {
		return true;
	}

	@Override
	public List<CloudStaffDto> transferModelToDto(List<CloudStaff> list) {
		List<String> ids = Lists.newArrayList();
		if (CollectionUtils.isEmpty(list)) {
			return Lists.newArrayList();
		}
		// staffIds
		for (CloudStaff staff : list) {
			ids.add(staff.getId());
		}
		// 根据staffIds获取用户名和staffId的map , {staffId:userName}
		Map<String, String> idNameMap = cloudUserDao.findUserNamesByStaffIds(ids);
		List<CloudStaffDto> dtos = Lists.newArrayList();
		// 将staff转为dto
		for (CloudStaff cloudStaff : list) {
			CloudStaffDto cloudStaffDto = new CloudStaffDto();
			BeanUtils.copyProperties(cloudStaff, cloudStaffDto);

			// 设置用户名
			cloudStaffDto.setUserName(idNameMap.get(cloudStaff.getId()));
			dtos.add(cloudStaffDto);
		}
		return dtos;
	}

	@Override
	public void deletesStaffAndUser(List<String> deleteList) {
		if (CollectionUtils.isEmpty(deleteList)) {
			return;
		}
		// 获取要删除的staff
		List<CloudStaff> staffs = cloudStaffDao.findAllByIds(deleteList.toArray(new String[deleteList.size()]));
		// 获取要删除的staff对应的user
		List<CloudUser> users = cloudUserDao.getUsersByStaffIds(deleteList);
		// 删除user
		cloudUserDao.delete(users);
		// 删除staff
		cloudStaffDao.delete(staffs);

	}

	@Override
	public boolean isSocialSecurityNoExist(String staffId, String socialSecurityNo) {
		if (StringUtils.isBlank(socialSecurityNo)) {
			return false;
		}

		boolean result = false;

		List<SearchFilter> filterList = new ArrayList<>();
		filterList.add(new SearchFilter("socialSecurityNo", Operator.EQ, socialSecurityNo));
		if (StringUtils.isNotBlank(staffId)) {
			filterList.add(new SearchFilter("id", Operator.NE, staffId));
		}
		List<CloudStaff> list = this.findListByFilter(filterList, null);

		if (CollectionUtils.isNotEmpty(list)) {
			result = true;
		}
		return result;

	}

	@Override
	public boolean isCredentialNumExist(String staffId, String credentialNum) {
		if (StringUtils.isBlank(credentialNum)) {
			return false;
		}

		boolean result = false;

		List<SearchFilter> filterList = new ArrayList<>();
		filterList.add(new SearchFilter("credentialNum", Operator.EQ, credentialNum));
		if (StringUtils.isNotBlank(staffId)) {
			filterList.add(new SearchFilter("id", Operator.NE, staffId));
		}
		List<CloudStaff> list = this.findListByFilter(filterList, null);

		if (CollectionUtils.isNotEmpty(list)) {
			result = true;
		}
		return result;

	}

	@Override
	public boolean isPhoneExists(String id, String phone) {
		if (StringUtils.isBlank(phone)) {
			return false;
		}

		boolean result = false;

		List<SearchFilter> filterList = new ArrayList<>();
		filterList.add(new SearchFilter("phone", Operator.EQ, phone));
		if (StringUtils.isNotBlank(id)) {
			filterList.add(new SearchFilter("id", Operator.NE, id));
		}
		List<CloudStaff> list = this.findListByFilter(filterList, null);

		if (CollectionUtils.isNotEmpty(list)) {
			result = true;
		}
		return result;
	}

	@Override
	public List<CloudStaffDto> loadStaffsByFilter(Map<String, Object> paramMap) {
		return cloudStaffDao.loadStaffsByFilter(paramMap);

	}

	@Override
	public Page<CloudStaffDto> findPageWithPermissionBySearchDto(Pageable pageable, CloudStaffSearchDto searchDto) {
		// 如果没有传orgId或者部门id，那么就直接返回null
		if (StringUtils.isEmpty(searchDto.getDepartmentId()) && StringUtils.isEmpty(searchDto.getOrgId())) {
			return null;
		}

		String userId = searchDto.getUserId();

		// 获取该用户有权限的org和department，(自定义中全选才认为有权限)
		List<String> companyIds = cloudOrganizationService.getCompanyIdsWithPermission(userId, searchDto.getTenantId());

		// 该用户下没有权限，直接返回
		if (CollectionUtils.isEmpty(companyIds)) {
			return null;
		}

		searchDto.setCompanyIds(companyIds);
		return cloudStaffDao.findPageBySearchDto(pageable, searchDto);
	}

	@Override
	public void setNameInitial() {
		SearchFilters searchFilters = new SearchFilters();
		/*
		 * searchFilters.add(new SearchFilter("nameInitial", Operator.EQ, ""));
		 * searchFilters.add(new SearchFilter("nameInitial", Operator.NULL, null));
		 */
		List<CloudStaff> staffs = cloudStaffDao.findListByFilters(searchFilters, null);

		if (CollectionUtils.isNotEmpty(staffs)) {
			for (CloudStaff cloudStaff : staffs) {
				cloudStaff.setNameInitial(pinyin4jUtil.getPinYinNoToneAndSpace(cloudStaff.getName()).toLowerCase());
			}
		}
		cloudStaffDao.update(staffs);

	}

	@Override
	public List<CloudStaffPageDto> syncStaffByPage(String tenantId, long syncTime, Integer pageSize, Integer pageNumber) {
		return cloudStaffDao.syncStaffByPage(tenantId, syncTime, pageSize, pageNumber);
	}

	@Override
	public List<CloudStaffPageDto> findAllStaffByPage(String tenantId, Integer isDeleted) {
		return cloudStaffDao.findAllStaffByPage(tenantId, isDeleted);
	}

	@Override
	public void lockUser(String userId) throws Exception {
		if (StringUtils.isEmpty(userId)) {
			logger.error("锁定用户时请传入用户id");
			throw new VortexException("锁定用户时请传入用户id");
		}

		CloudUser user = this.cloudUserDao.findOne(userId);
		if (user == null) {
			logger.error("根据用户id[" + userId + "]未查询到用户信息");
			throw new VortexException("根据用户id[" + userId + "]未查询到用户信息");
		}

		user.setLockuser(CloudUser.LOCK_YES);
		cloudUserDao.update(user);
	}

	@Override
	public void unlockUser(String userId) throws Exception {
		if (StringUtils.isEmpty(userId)) {
			logger.error("锁定用户时请传入用户id");
			throw new VortexException("锁定用户时请传入用户id");
		}

		CloudUser user = this.cloudUserDao.findOne(userId);
		if (user == null) {
			logger.error("根据用户id[" + userId + "]未查询到用户信息");
			throw new VortexException("根据用户id[" + userId + "]未查询到用户信息");
		}

		user.setLockuser(CloudUser.LOCK_NO);
		cloudUserDao.update(user);
	}

	@Override
	public void empower(List<String> userIds, List<String> roleIds) throws Exception {
		if (CollectionUtils.isEmpty(userIds) || CollectionUtils.isEmpty(roleIds)) {
			return;
		}

		for (String userId : userIds) {
			for (String roleId : roleIds) {
				CloudUserRole ur = cloudUserRoleDao.findByUidAndRid(userId, roleId);
				if (ur == null) {
					ur = new CloudUserRole();
					ur.setUserId(userId);
					ur.setRoleId(roleId);
					cloudUserRoleDao.save(ur);
				}
			}
		}
	}

	@Override
	public void repower(List<String> userIds, List<String> roleIds) throws Exception {
		if (CollectionUtils.isEmpty(userIds) || CollectionUtils.isEmpty(roleIds)) {
			return;
		}

		for (String userId : userIds) {
			for (String roleId : roleIds) {
				CloudUserRole ur = cloudUserRoleDao.findByUidAndRid(userId, roleId);
				if (ur != null) {
					cloudUserRoleDao.delete(ur);
				}
			}
		}
	}
}
