package com.vortex.cloud.ums.dataaccess.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vortex.cloud.ums.dataaccess.dao.ICloudDepartmentDao;
import com.vortex.cloud.ums.dataaccess.dao.ICloudRoleDao;
import com.vortex.cloud.ums.dataaccess.dao.ICloudRoleGroupDao;
import com.vortex.cloud.ums.dataaccess.dao.ICloudStaffDao;
import com.vortex.cloud.ums.dataaccess.dao.ICloudSystemDao;
import com.vortex.cloud.ums.dataaccess.dao.ICloudTenantRelationDao;
import com.vortex.cloud.ums.dataaccess.dao.ICloudUserDao;
import com.vortex.cloud.ums.dataaccess.dao.ICloudUserRoleDao;
import com.vortex.cloud.ums.dataaccess.dao.ITenantDao;
import com.vortex.cloud.ums.dataaccess.service.IGroupCompanyUserService;
import com.vortex.cloud.ums.dto.tenantgroup.SystemListDto;
import com.vortex.cloud.ums.dto.tenantgroup.TenantInfoDto;
import com.vortex.cloud.ums.model.CloudDepartment;
import com.vortex.cloud.ums.model.CloudRole;
import com.vortex.cloud.ums.model.CloudRoleGroup;
import com.vortex.cloud.ums.model.CloudStaff;
import com.vortex.cloud.ums.model.CloudSystem;
import com.vortex.cloud.ums.model.CloudUser;
import com.vortex.cloud.ums.model.CloudUserRole;
import com.vortex.cloud.ums.model.Tenant;
import com.vortex.cloud.ums.support.ManagementConstant;
import com.vortex.cloud.vfs.common.digest.MD5;
import com.vortex.cloud.vfs.common.exception.VortexException;
import com.vortex.cloud.vfs.data.model.BakDeleteModel;

@Transactional
@Service("groupCompanyUserService")
public class GroupCompanyUserServiceImpl implements IGroupCompanyUserService {
	private Logger loger = LoggerFactory.getLogger(GroupCompanyUserServiceImpl.class);
	@Resource
	private ICloudUserDao cloudUserDao;
	@Resource
	private ICloudStaffDao cloudStaffDao;
	@Resource
	private ICloudTenantRelationDao cloudTenantRelationDao;
	@Resource
	private ICloudSystemDao cloudSystemDao;
	@Resource
	private ITenantDao tenantDao;
	@Resource
	private ICloudRoleGroupDao cloudRoleGroupDao;
	@Resource
	private ICloudUserRoleDao cloudUserRoleDao;
	@Resource
	private ICloudRoleDao cloudRoleDao;
	@Resource
	private ICloudDepartmentDao cloudDepartmentDao;

	@Override
	public List<SystemListDto> getSystemList(String userId) throws Exception {
		if (StringUtils.isEmpty(userId)) {
			return null;
		}

		CloudUser user = cloudUserDao.findOne(userId);
		if (user == null || StringUtils.isEmpty(user.getStaffId())) {
			return null;
		}

		CloudStaff staff = cloudStaffDao.findOne(user.getStaffId());

		if (staff == null || StringUtils.isEmpty(staff.getTenantId())) {
			return null;
		}

		if (user.getUserName().indexOf(ManagementConstant.USER_NAME_SPLIT) > -1) { // 子租户
			return this.listSystem(this.cloudTenantRelationDao.getMainTenantId(staff.getTenantId()));
		} else { // 集团租户
			return this.listSystem(staff.getTenantId());
		}
	}

	/**
	 * 获取集团租户的系统列表
	 * 
	 * @param mainTenantId
	 * @return
	 * @throws Exception
	 */
	private List<SystemListDto> listSystem(String mainTenantId) throws Exception {
		// 租户本身的系统信息
		List<SystemListDto> rst = cloudSystemDao.listByTenantId(mainTenantId);
		if (CollectionUtils.isEmpty(rst)) {
			loger.error("该租户没有业务系统");
			throw new VortexException("该租户没有业务系统");
		}

		// 子租户的系统信息
		List<TenantInfoDto> viceList = this.cloudTenantRelationDao.listViceTenant(mainTenantId);
		List<SystemListDto> list = null;
		if (CollectionUtils.isNotEmpty(viceList)) {
			for (TenantInfoDto info : viceList) {
				list = cloudSystemDao.listByTenantId(info.getTenantId());
				if (CollectionUtils.isNotEmpty(list)) {
					rst.addAll(list);
				}
			}
		}

		return rst;
	}

	@Override
	public String getAccount(String userId, String targetSystemId) throws Exception {
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(targetSystemId)) {
			loger.error("切换时传入的信息不全");
			throw new VortexException("切换时传入的信息不全");
		}

		// 查询当前账号的租户id
		String utid = this.cloudUserDao.getTenantIdByUserId(userId);
		if (StringUtils.isEmpty(utid)) {
			loger.error("未找到人员对应的租户");
			throw new VortexException("未找到人员对应的租户");
		}

		CloudSystem targetSystem = this.cloudSystemDao.findOne(targetSystemId);
		if (targetSystem == null) {
			if (StringUtils.isEmpty(utid)) {
				loger.error("目标系统不存在");
				throw new VortexException("目标系统不存在");
			}
		}

		String account = this.cloudUserDao.getAccountByUserId(userId);

		// 如果人员对应的租户id和目标租户id一致，那么直接返回该人的账号
		if (utid.equals(targetSystem.getTenantId())) {
			return account;
		}

		// 判断当前租户和目标租户是否在同一组“集团—子公司”关系中，若不在，直接返回
		if (!this.cloudTenantRelationDao.isInSameGroupCompany(utid, targetSystem.getTenantId())) {
			loger.error("人员所在租户和目标系统不在同一个集团内");
			throw new VortexException("人员所在租户和目标系统不在同一个集团内");
		}

		// 如果账号中不包含特殊分隔符，代表当前账号是集团账号，从集团切换到子公司
		if (account.indexOf(ManagementConstant.USER_NAME_SPLIT) == -1) {
			return this.getViceTenantAccount(account, targetSystem.getTenantId());
		} else {
			List<TenantInfoDto> vtList = cloudTenantRelationDao.listViceTenant(targetSystem.getTenantId());
			if (CollectionUtils.isNotEmpty(vtList)) { // 从子公司切换到集团
				// 从子公司切换到集团，直接返回集团公司的账号即可，特殊分隔符前半段就是集团账号
				return account.split(ManagementConstant.USER_NAME_SPLIT_REGEX)[0];
			} else { // 从子公司切换到其他子公司
				return this.getViceTenantAccount(account.split(ManagementConstant.USER_NAME_SPLIT_REGEX)[0], targetSystem.getTenantId());
			}
		}
	}

	/**
	 * 根据集团账号和目标子公司租户id，得到集团账号对应的目标租户的账号
	 * 
	 * @param account
	 * @param tenantId
	 * @return
	 */
	private String getViceTenantAccount(String account, String tenantId) throws Exception {
		Tenant tt = this.tenantDao.findOne(tenantId);

		// 看租户下是否有默认挂子账号的机构，没有则新建
		CloudDepartment dept = this.cloudDepartmentDao.getByCode(tenantId, ManagementConstant.DEPT_JT, BakDeleteModel.DELETED);
		if (dept == null) {
			dept = new CloudDepartment();
			dept.setTenantId(tenantId);
			dept.setDepCode(ManagementConstant.DEPT_JT);
			dept.setDepName("集团人员部门");
			dept.setDescription("用于存放集团人员，请勿修改");
			dept.setBeenDeleted(BakDeleteModel.DELETED);
			dept = this.cloudDepartmentDao.save(dept);
		}

		// 如果未找到账号，则需要新增user和staff
		CloudUser user = this.cloudUserDao.getByAccount(account + ManagementConstant.USER_NAME_SPLIT + tt.getTenantCode());
		CloudStaff staff = null;
		if (user != null) {
			staff = this.cloudStaffDao.findOne(user.getStaffId());
		} else {
			CloudUser jtuser = this.cloudUserDao.getByAccount(account);
			CloudStaff jtstaff = this.cloudStaffDao.findOne(jtuser.getStaffId());

			// 保存人员基本信息，删除标志为已删除
			staff = new CloudStaff();
			staff.setTenantId(tenantId);
			staff.setCode(jtstaff.getCode() + ManagementConstant.USER_NAME_SPLIT + tt.getTenantCode());
			staff.setName(jtstaff.getName());
			staff.setDescription("集团子账号");
			staff.setOrderIndex(9999);
			staff.setOrgId(null);
			staff.setDepartmentId(dept.getId());
			staff.setOrgName(dept.getDepName());
			staff.setBeenDeleted(BakDeleteModel.NO_DELETED);
			staff = this.cloudStaffDao.saveAndFlush(staff);

			// 保存人员账号，删除标志为已删除
			user = new CloudUser();
			user.setStaffId(staff.getId());
			user.setUserName(account + ManagementConstant.USER_NAME_SPLIT + tt.getTenantCode());
			user.setPassword(MD5.getMD5("123456"));
			user.setBeenDeleted(BakDeleteModel.NO_DELETED);
			user = this.cloudUserDao.saveAndFlush(user);
		}

		// 完善账号在租户中的各种关联关系；可能在一次切换后，目标租户又新增了系统，故仍然需要同步账号和默认角色等关系
		List<SystemListDto> systemList = cloudSystemDao.listByTenantId(staff.getTenantId());
		if (CollectionUtils.isNotEmpty(systemList)) {
			for (SystemListDto system : systemList) {
				this.syncUserRoleRelation(user.getId(), system.getSystemId());
			}
		}

		// 返回账号
		return account + ManagementConstant.USER_NAME_SPLIT + tt.getTenantCode();
	}

	/**
	 * 同步集团子账号在租户的业务系统中的默认角色
	 * 
	 * @param userId
	 * @param tenantId
	 * @throws Exception
	 */
	private void syncUserRoleRelation(String userId, String systemId) throws Exception {
		CloudRoleGroup group = cloudRoleGroupDao.findByCode(ManagementConstant.ROLE_GROUP_JT, systemId);
		if (group == null) { // 角色组不存在，创建角色组
			group = new CloudRoleGroup();
			group.setCode(ManagementConstant.ROLE_GROUP_JT);
			group.setName("集团用户角色组");
			group.setOrderIndex(9999);
			group.setDescription("集团用户使用，请勿修改");
			group.setParentId("-1");
			group.setSystemId(systemId);
			group.setNodeCode("99");
			group.setChildSerialNumer(0);
			group = this.cloudRoleGroupDao.saveAndFlush(group);
		}

		CloudRole role = this.cloudRoleDao.getRoleBySystemIdAndRoleCode(systemId, ManagementConstant.ROLE_JT);
		if (role == null) {
			role = new CloudRole();
			role.setCode(ManagementConstant.ROLE_JT);
			role.setSystemId(systemId);
			role.setGroupId(group.getId());
			role.setOrderIndex(9999);
			role.setDescription("集团用户默认角色，请勿修改");
			role.setRoleType(CloudRole.ROLE_TYPE_PRESET);
			role = this.cloudRoleDao.saveAndFlush(role);
		}

		CloudUserRole userRole = this.cloudUserRoleDao.findByUidAndRid(userId, role.getId());
		if (userRole == null) {
			userRole = new CloudUserRole();
			userRole.setUserId(userId);
			userRole.setRoleId(role.getId());
			userRole = this.cloudUserRoleDao.save(userRole);
		}
	}
}
