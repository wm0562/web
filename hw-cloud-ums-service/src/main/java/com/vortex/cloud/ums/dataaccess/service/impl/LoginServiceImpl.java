package com.vortex.cloud.ums.dataaccess.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vortex.cloud.ums.dataaccess.dao.ICloudPortalUserDao;
import com.vortex.cloud.ums.dataaccess.dao.ICloudStaffDao;
import com.vortex.cloud.ums.dataaccess.dao.ICloudSystemDao;
import com.vortex.cloud.ums.dataaccess.dao.ICloudThirdPartyAppDao;
import com.vortex.cloud.ums.dataaccess.dao.ICloudUserDao;
import com.vortex.cloud.ums.dataaccess.dao.ITenantDao;
import com.vortex.cloud.ums.dataaccess.service.ICloudLoginLogService;
import com.vortex.cloud.ums.dataaccess.service.ILoginService;
import com.vortex.cloud.ums.dataaccess.service.ITenantParamSettingService;
import com.vortex.cloud.ums.dto.UmsLoginReturnInfoDto;
import com.vortex.cloud.ums.dto.rest.CloudUserRestDto;
import com.vortex.cloud.ums.enums.LoginErrEnum;
import com.vortex.cloud.ums.enums.LoginTypeEnum;
import com.vortex.cloud.ums.model.CloudStaff;
import com.vortex.cloud.ums.model.CloudUser;
import com.vortex.cloud.ums.model.Tenant;
import com.vortex.cloud.ums.model.TenantPramSetting;
import com.vortex.cloud.ums.util.utils.PropertyUtils;
import com.vortex.cloud.vfs.common.digest.MD5;
import com.vortex.cloud.vfs.common.exception.VortexException;
import com.vortex.cloud.vfs.data.model.BakDeleteModel;

@Transactional
@Service("loginService")
public class LoginServiceImpl implements ILoginService {
	private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
	@Resource
	private ICloudThirdPartyAppDao cloudThirdPartyAppDao;
	@Resource
	private ICloudUserDao cloudUserDao;

	@Resource
	private ICloudSystemDao cloudSystemDao;

	@Resource
	private ICloudLoginLogService cloudLoginLogService;

	@Resource
	private ITenantParamSettingService tenantParamSettingService;

	@Resource
	private ICloudStaffDao cloudStaffDao;

	@Resource
	private ITenantDao tenantDao;

	@Resource
	private ICloudPortalUserDao cloudPortalUserDao;
	private LinkedHashMap<String, String> tokenMap = new LinkedHashMap<String, String>();

	@Override
	public UmsLoginReturnInfoDto login(String tenantCode, String systemCode, String userName, String password, String mobilePushMsgId, String ip) {

		// 注意：超级管理员不属于任何租户，因此无须校验入参tenantCode
		// 用户名和密码必须，租户code和系统code必须有一个
		if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
			logger.error("用户登录时，传入的参数不足！");
			throw new VortexException("用户登录时，传入的参数不足！");
		}

		// 如果登录的时候传入了tenantCode，则校验此人是否在此租户下，如果人员的租户和传入的租户对应不上，直接抛错返回。
		if (StringUtils.isNotEmpty(tenantCode)) {
			CloudUserRestDto ud = this.cloudUserDao.getUserByUserNameAndTenantCode(userName, tenantCode);
			if (ud == null) {
				logger.error(LoginErrEnum.LOGIN_ERR_NOT_FOUND.getValue());
				throw new VortexException(LoginErrEnum.LOGIN_ERR_NOT_FOUND.getKey());
			}
		}

		List<UmsLoginReturnInfoDto> list = cloudUserDao.getLoginInfo(tenantCode, systemCode, userName);
		if (CollectionUtils.isEmpty(list)) {
			logger.error(LoginErrEnum.LOGIN_ERR_NOT_FOUND.getValue());
			throw new VortexException(LoginErrEnum.LOGIN_ERR_NOT_FOUND.getKey());
		} else if (list.size() > 1) {
			logger.error(LoginErrEnum.LOGIN_ERR_FOUND_MUTI.getValue());
			throw new VortexException(LoginErrEnum.LOGIN_ERR_FOUND_MUTI.getKey());
		}

		UmsLoginReturnInfoDto result = list.get(0);

		if (!password.equals(result.getPassword())) {
			logger.error(LoginErrEnum.LOGIN_ERR_PASSWORD.getValue());
			throw new VortexException(LoginErrEnum.LOGIN_ERR_PASSWORD.getKey());
		}
		// oauth2 需要密码
		// result.setPassword(null);

		// 设置系统可用列表
		result.setSystemList(cloudSystemDao.getSystemList(result.getUserId()));

		CloudStaff cloudStaff = cloudStaffDao.findOne(result.getStaffId());
		if (cloudStaff != null) {
			// 职位
			TenantPramSetting postPramSetting = tenantParamSettingService.findOneByParamCode(result.getTenantId(), PropertyUtils.getPropertyValue("STAFF_POSITION"), cloudStaff.getPostId());
			if (postPramSetting != null) {
				result.setPostName(postPramSetting.getParmName());
			}
			// 职务
			TenantPramSetting partyPostPramSetting = tenantParamSettingService.findOneByParamCode(result.getTenantId(), PropertyUtils.getPropertyValue("STAFF_POST"), cloudStaff.getPartyPostId());
			if (partyPostPramSetting != null) {
				result.setPartyPostName(partyPostPramSetting.getParmName());
			}
		}

		// 更新手机推送id
		if (StringUtils.isNotBlank(mobilePushMsgId)) {
			// 先解绑原来有这个mobilePushMsgId的账号（如果有绑定给其他人的话）
			CloudUser cloudUser = cloudUserDao.getByPushId(mobilePushMsgId);
			if (cloudUser != null && !result.getUserId().equals(cloudUser.getId())) {
				cloudUser.setMobilePushMsgId(null);
				cloudUserDao.update(cloudUser);
			}

			// 再绑定给其他人
			CloudUser cloudUser1 = cloudUserDao.findOne(result.getUserId());
			cloudUser1.setMobilePushMsgId(mobilePushMsgId);
			cloudUserDao.update(cloudUser1);
		}

		try {
			cloudLoginLogService.saveCloudLoginLog(result.getUserName(), result.getName(), ip);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 登录类型，伏泰用户
		result.setLoginType(LoginTypeEnum.VORTEX_USER.getKey());

		/**
		 * 设置地图类型字段
		 */
		CloudStaff staff = this.cloudStaffDao.findOne(result.getStaffId());
		if (staff != null && StringUtils.isNotEmpty(staff.getTenantId())) {
			Tenant tenant = this.tenantDao.findOne(staff.getTenantId());
			if (tenant != null) {
				result.setMapDefJson(tenant.getMapDefJson());
			}
		}

		return result;
	}

	@Override
	public UmsLoginReturnInfoDto login(String tenantCode, String systemCode, String userName, String password, String ip) {
		return this.login(tenantCode, systemCode, userName, password, null, ip);
	}

	@Override
	public String getToken(String userName) throws Exception {
		if (StringUtils.isEmpty(userName)) {
			throw new VortexException("获取token时未传入用户名！");
		}

		CloudUser user = this.cloudUserDao.getByAccount(userName);
		if (user == null || BakDeleteModel.DELETED.equals(user.getBeenDeleted())) {
			throw new VortexException("用户名不存在！");
		}

		String token = UUID.randomUUID().toString();
		synchronized (tokenMap) {
			tokenMap.put(userName, token);
		}
		return token;
	}

	@Override
	public UmsLoginReturnInfoDto loginByPasswordOrToken(String tenantCode, String systemCode, String userName, String evidence, String ip) throws Exception {
		if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(evidence)) {
			throw new VortexException("登录时传入的信息不足！");
		}

		// 如果登录的时候传入了tenantCode，则校验此人是否在此租户下，如果人员的租户和传入的租户对应不上，直接抛错返回。
		if (StringUtils.isNotEmpty(tenantCode)) {
			CloudUserRestDto ud = this.cloudUserDao.getUserByUserNameAndTenantCode(userName, tenantCode);
			if (ud == null) {
				logger.error(LoginErrEnum.LOGIN_ERR_NOT_FOUND.getValue());
				throw new VortexException(LoginErrEnum.LOGIN_ERR_NOT_FOUND.getKey());
			}
		}

		synchronized (tokenMap) {
			String token = tokenMap.get(userName);

			// 如果在map中找到该用户名对应的token，则直接根据用户名找到user，然后调用正常登录方法，最后删除token
			if (StringUtils.isNotEmpty(token) && MD5.getMD5(token).equals(evidence)) {
				CloudUser user = cloudUserDao.getUserByIdAndName(null, userName);
				tokenMap.remove(userName);
				return this.login(null, null, userName, user.getPassword(), ip);
			}
		}

		return this.login(tenantCode, systemCode, userName, evidence, ip);
	}

	@Override
	public UmsLoginReturnInfoDto loginFromThirdPartyApp(String appKey, String appSecret, String ip) throws Exception {
		String error_msg = "";
		if (StringUtils.isEmpty(appKey) || StringUtils.isEmpty(appSecret)) {
			error_msg = "第3方app登录时，传入的参数不足！";
			logger.error(error_msg);
			throw new VortexException(error_msg);
		}
		List<UmsLoginReturnInfoDto> list = cloudThirdPartyAppDao.getLoginInfo(appKey);
		if (CollectionUtils.isEmpty(list)) {
			error_msg = LoginErrEnum.LOGIN_ERR_NOT_FOUND.getValue();
			logger.error(error_msg);
			throw new VortexException(error_msg);
		} else if (list.size() > 1) {
			error_msg = LoginErrEnum.LOGIN_ERR_FOUND_MUTI.getValue();
			logger.error(error_msg);
			throw new VortexException(error_msg);
		}
		UmsLoginReturnInfoDto result = list.get(0);
		if (!appSecret.equals(result.getPassword())) {
			error_msg = LoginErrEnum.LOGIN_ERR_PASSWORD.getValue();
			logger.error(error_msg);
			throw new VortexException(error_msg);
		}
		try {
			cloudLoginLogService.saveCloudLoginLog(result.getUserName(), result.getName(), ip);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 登录类型，第三方app
		result.setLoginType(LoginTypeEnum.THIRD_PARTY_APP.getKey());
		return result;
	}

	@Override
	public UmsLoginReturnInfoDto loginPortalUser(String userName, String password, String ip, String tenantId) throws Exception {
		String error_msg = "";
		if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password) || StringUtils.isEmpty(tenantId)) {
			error_msg = "门户登录时，传入的参数不足！";
			logger.error(error_msg);
			throw new VortexException(error_msg);
		}
		List<UmsLoginReturnInfoDto> list = cloudPortalUserDao.getLoginInfo(userName, tenantId);
		if (CollectionUtils.isEmpty(list)) {
			error_msg = LoginErrEnum.LOGIN_ERR_NOT_FOUND.getValue();
			logger.error(error_msg);
			throw new VortexException(error_msg);
		} else if (list.size() > 1) {
			error_msg = LoginErrEnum.LOGIN_ERR_FOUND_MUTI.getValue();
			logger.error(error_msg);
			throw new VortexException(error_msg);
		}
		UmsLoginReturnInfoDto result = list.get(0);
		if (!password.equals(result.getPassword())) {
			error_msg = LoginErrEnum.LOGIN_ERR_PASSWORD.getValue();
			logger.error(error_msg);
			throw new VortexException(error_msg);
		}
		try {
			cloudLoginLogService.saveCloudLoginLog(result.getUserName(), result.getName(), ip);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 登录类型，门户
		result.setLoginType(LoginTypeEnum.PORTAL_USER.getKey());
		return result;
	}
}
