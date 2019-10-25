package com.vortex.cloud.ums.dataaccess.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.vortex.cloud.ums.dataaccess.dao.ICloudMessageAuthCodeDao;
import com.vortex.cloud.ums.dataaccess.dao.ICloudPortalUserDao;
import com.vortex.cloud.ums.dataaccess.service.ICloudPortalUserService;
import com.vortex.cloud.ums.dataaccess.service.IMcsService;
import com.vortex.cloud.ums.dataaccess.service.ITenantService;
import com.vortex.cloud.ums.dto.CloudPortalUserDto;
import com.vortex.cloud.ums.enums.MsgAuthCodeErrorEnum;
import com.vortex.cloud.ums.enums.MsgAuthCodeTypeEnum;
import com.vortex.cloud.ums.model.CloudMessageAuthCode;
import com.vortex.cloud.ums.model.CloudPortalUser;
import com.vortex.cloud.ums.model.Tenant;
import com.vortex.cloud.ums.support.ManagementConstant;
import com.vortex.cloud.vfs.common.digest.MD5;
import com.vortex.cloud.vfs.common.exception.ServiceException;
import com.vortex.cloud.vfs.common.lang.StringUtil;
import com.vortex.cloud.vfs.data.dto.RestResultDto;
import com.vortex.cloud.vfs.data.hibernate.repository.HibernateRepository;
import com.vortex.cloud.vfs.data.hibernate.service.SimplePagingAndSortingService;
import com.vortex.cloud.vfs.data.support.SearchFilter;
import com.vortex.cloud.vfs.data.support.SearchFilter.Operator;

@Transactional
@Service("cloudPortalUserService")
public class CloudPortalUserServiceImpl extends SimplePagingAndSortingService<CloudPortalUser, String>
		implements ICloudPortalUserService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private ICloudPortalUserDao cloudPortalUserDao;
	@Resource
	private ICloudMessageAuthCodeDao cloudMessageAuthCodeDao;
	@Resource
	private ITenantService tenantService;
	@Resource
	private IMcsService mcsService;

	@Override
	public HibernateRepository<CloudPortalUser, String> getDaoImpl() {
		return cloudPortalUserDao;
	}

	@Override
	public void saveDto(CloudPortalUserDto dto) {
		checkSave(dto);
		CloudPortalUser entity = new CloudPortalUser();
		BeanUtils.copyProperties(dto, entity);
		this.save(entity);
	}

	@Override
	public void updateDto(CloudPortalUserDto dto) {
		String err_msg = null;
		checkUpdate(dto);
		CloudPortalUser entity = this.findOne(dto.getId());
		if (null == entity) {
			err_msg = "不存在id为" + dto.getId() + "的数据";
			logger.error(err_msg);
			throw new ServiceException(err_msg);
		}
		entity.setNickname(dto.getNickname()); // 昵称
		entity.setProfilePhoto(dto.getProfilePhoto()); // 头像
		entity.setPhone(dto.getPhone()); // 手机号(所有租户下唯一)
		entity.setGender(dto.getGender()); // 性别，男-M，女-F
		entity.setBirthday(dto.getBirthday()); // 生日，yyyy-MM-dd标准字符串
		this.update(entity);
	}

	@Override
	public Integer sendMessageAuthCode(String phone, String msgType, String tenantName, String tenantId)
			throws Exception {
		String err_msg = null;
		if (StringUtil.isNullOrEmpty(phone) || StringUtil.isNullOrEmpty(msgType) || StringUtil.isNullOrEmpty(tenantName)
				|| StringUtil.isNullOrEmpty(tenantId)) {
			err_msg = "发送消息时传入的参数不全！";
			logger.error(err_msg);
			throw new ServiceException(err_msg);
		}
		// 判断消息类型
		String msgTypeName = MsgAuthCodeTypeEnum.getValueByKey(msgType);
		if (StringUtil.isNullOrEmpty(msgTypeName)) {
			err_msg = "消息类型[" + msgType + "]不存在";
			logger.error(err_msg);
			throw new ServiceException(err_msg);
		}
		if (MsgAuthCodeTypeEnum.REGISTER.getKey().equals(msgType)) {// 注册
			// 判断手机号是否存在
			if (!existsPhone(phone, tenantId)) {// 若不存在，则继续
				// 查询最近一次该手机号发送的该类型消息记录
				Date last_send = getLastSendDate(phone, msgType, tenantId);
				if (canSend(last_send)) {// 超过有效时间，则发送短信
					sendMsgAuthCode(phone, msgType, tenantName, tenantId);
				} else {
					return MsgAuthCodeErrorEnum.RE_PHONE_ISSEND.getKey();// 否则返回信息，该手机号有效时间内已发送过短信
				}
			} else {
				return MsgAuthCodeErrorEnum.RE_PHONE_EXISTS.getKey();// 否则返回信息，手机号已存在
			}
		} else if (MsgAuthCodeTypeEnum.FORGET.getKey().equals(msgType)) {// 忘记密码
			// 判断手机号是否存在
			if (existsPhone(phone, tenantId)) {// 若存在，则继续
				// 查询最近一次该手机号发送的该类型消息记录
				Date last_send = getLastSendDate(phone, msgType, tenantId);
				if (canSend(last_send)) {// 超过有效时间，则发送短信
					sendMsgAuthCode(phone, msgType, tenantName, tenantId);
				} else {
					return MsgAuthCodeErrorEnum.FO_PHONE_ISSEND.getKey();// 否则返回信息，该手机号有效时间内已发送过短信
				}
			} else {
				return MsgAuthCodeErrorEnum.FO_PHONE_NOTEXISTS.getKey();// 否则返回信息，手机号不存在
			}
		}
		return RestResultDto.RESULT_SUCC;
	}

	@Override
	public void resetPassword(String id) throws Exception {
		String err_msg = null;
		CloudPortalUser entity = this.findOne(id);
		if (null == entity) {
			err_msg = "不存在id为" + id + "的用户";
			logger.error(err_msg);
			throw new ServiceException(err_msg);
		}
		String new_pass = getRandomString(RANDOMSTRING_LENS);
		entity.setPassword(MD5.getMD5(new_pass));
		this.update(entity);
		String tenantName = "";
		// 根据编号查询id
		if (!StringUtil.isNullOrEmpty(entity.getTenantId())) {
			Tenant tenant = tenantService.findOne(entity.getTenantId());
			if (null != tenant) {
				tenantName = tenant.getTenantName();
			}
		}
		// 发送消息
		// 【伏泰】您在%s的密码已重置为：%s，请重新登录后尽快修改密码。
		List<Object> params = Lists.newArrayList();
		params.add(tenantName);
		params.add(new_pass);
		mcsService.send(ManagementConstant.PHONE_TID_RESTPASSWORD, Lists.newArrayList(entity.getPhone()), params);
	}

	@Override
	@Transactional(readOnly = true)
	public CloudPortalUserDto getById(String id) {
		String err_msg = null;
		CloudPortalUser entity = this.findOne(id);
		if (null == entity) {
			err_msg = "不存在id为" + id + "的用户";
			logger.error(err_msg);
			throw new ServiceException(err_msg);
		}
		CloudPortalUserDto dto = new CloudPortalUserDto();
		BeanUtils.copyProperties(entity, dto);
		return dto;
	}

	@Override
	public void changePassword(String id, String oldPwd, String newPwd) throws Exception {
		String err_msg = null;
		if (StringUtils.isEmpty(id) || StringUtils.isEmpty(oldPwd) || StringUtils.isEmpty(newPwd)) {
			err_msg = "修改密码时传入的参数不全！";
			logger.error(err_msg);
			throw new ServiceException(err_msg);
		}

		CloudPortalUser user = this.findOne(id);
		if (user == null) {
			err_msg = "根据用户id[" + id + "]未找到用户信息！";
			logger.error(err_msg);
			throw new ServiceException(err_msg);
		}

		if (!user.getPassword().equals(oldPwd)) {
			err_msg = "旧密码不正确！";
			logger.error(err_msg);
			throw new ServiceException(err_msg);
		}

		user.setPassword(newPwd);

		this.update(user);

	}

	@Override
	@Transactional(readOnly = true)
	public boolean checkAuthCode(String phone, String msgType, String authCode, String tenantId) {
		if (StringUtil.isNullOrEmpty(phone) || StringUtil.isNullOrEmpty(msgType) || StringUtil.isNullOrEmpty(authCode)
				|| StringUtil.isNullOrEmpty(tenantId)) {// 参数不全，返回false
			logger.error("校验验证码，参数不全");
			return false;
		}
		CloudMessageAuthCode entity = getLastSendMsg(phone, msgType, tenantId);
		if (null == entity) {// 该手机号该类型未发送信息，返回false
			logger.error("校验验证码，该手机号该类型未发送信息");
			return false;
		}
		if (null == entity.getCreateTime() || StringUtil.isNullOrEmpty(entity.getAuthCode())) {// 该手机号该类型未发送信息，返回false
			logger.error("校验验证码，数据错误");
			return false;
		}
		Date nowTime = new Date();
		if (nowTime.getTime() - entity.getCreateTime().getTime() > VALID_TIME_SEC) {// 超过验证码有效期
			logger.error("校验验证码，该验证码超过有效期");
			return false;
		}
		if (authCode.equals(entity.getAuthCode())) {
			return true;
		}
		logger.error("校验验证码，该验证码错误");
		return false;
	}

	private final Integer VALID_TIME_SEC_NUM = 5;
	// 短信发送间隔
	private final Long VALID_TIME_SEC = VALID_TIME_SEC_NUM * 60 * 1000L;
	// 随机数字符串长度
	private final Integer RANDOMSTRING_LENS = 6;

	private void checkUpdate(CloudPortalUserDto dto) {
		String err_msg = null;
		if (null == dto) {
			err_msg = "dto不能为空";
			logger.error(err_msg);
			throw new ServiceException(err_msg);
		}
		if (StringUtils.isEmpty(dto.getId())) {
			err_msg = "id不能为空";
			logger.error(err_msg);
			throw new ServiceException(err_msg);
		}
		if (StringUtils.isEmpty(dto.getPhone())) {
			err_msg = "手机号不能为空";
			logger.error(err_msg);
			throw new ServiceException(err_msg);
		}
	}

	private void checkSave(CloudPortalUserDto dto) {
		String err_msg = null;
		if (null == dto) {
			err_msg = "dto不能为空";
			logger.error(err_msg);
			throw new ServiceException(err_msg);
		}
		if (StringUtils.isEmpty(dto.getUserName())) {
			err_msg = "用户名不能为空";
			logger.error(err_msg);
			throw new ServiceException(err_msg);
		}
		if (StringUtils.isEmpty(dto.getPassword())) {
			err_msg = "密码不能为空";
			logger.error(err_msg);
			throw new ServiceException(err_msg);
		}
		if (StringUtils.isEmpty(dto.getPhone())) {
			err_msg = "手机号不能为空";
			logger.error(err_msg);
			throw new ServiceException(err_msg);
		}

		if (StringUtil.isNullOrEmpty(dto.getTenantId()) && !StringUtil.isNullOrEmpty(dto.getTenantCode())) {// 有租户编号，无租户id时
			// 根据编号查询id
			Tenant tenant = tenantService.getByCode(dto.getTenantCode());
			if (null != tenant) {
				dto.setTenantId(tenant.getId());
			}
		}

		if (StringUtils.isEmpty(dto.getTenantId())) {
			err_msg = "租户id不能为空";
			logger.error(err_msg);
			throw new ServiceException(err_msg);
		}
	}

	/**
	 * @Title: canSend
	 * 
	 * @Description: 是否可以发送短信
	 * 
	 * @return Boolean
	 * 
	 * @throws
	 */
	private Boolean canSend(Date date) {
		if (null == date) {
			return true;
		}
		Date nowTime = new Date();
		if (nowTime.getTime() - date.getTime() > VALID_TIME_SEC) {
			return true;
		}
		return false;
	}

	/**
	 * @throws Exception @Title: sendMsgAuthCode
	 * 
	 * @Description: 发送消息
	 * 
	 * @return Boolean
	 * 
	 * @throws
	 */
	private void sendMsgAuthCode(String phone, String msgType, String tenantName, String tenantId) throws Exception {
		String authCode = getRandomString(RANDOMSTRING_LENS);
		List<Object> params = Lists.newArrayList();
		params.add(tenantName);
		params.add(authCode);
		params.add(VALID_TIME_SEC_NUM);
		// 发送短信
		// 【伏泰】您在%s请求的验证码是：%s，请勿泄露短信验证码，此验证码%s分钟内有效。
		mcsService.send(ManagementConstant.PHONE_TID_AUTHCODE, Lists.newArrayList(phone), params);
		// 保存数据
		CloudMessageAuthCode entity = new CloudMessageAuthCode();
		entity.setAuthCode(authCode);
		entity.setMsgType(msgType);
		entity.setPhone(phone);
		entity.setTenantId(tenantId);
		cloudMessageAuthCodeDao.save(entity);
	}

	/**
	 * @Title: existsPhone
	 * 
	 * @Description: 电话号码是否存在
	 * 
	 * @return Boolean
	 * 
	 * @throws
	 */
	private Boolean existsPhone(String phone, String tenantId) {
		List<SearchFilter> searchFilters = Lists.newArrayList();
		searchFilters.add(new SearchFilter("phone", Operator.EQ, phone));
		searchFilters.add(new SearchFilter("tenantId", Operator.EQ, tenantId));
		List<CloudPortalUser> list = this.findListByFilter(searchFilters, null);
		if (CollectionUtils.isEmpty(list)) {
			return false;
		}
		return true;
	}

	/**
	 * @Title: getLastSendDate
	 * 
	 * @Description: 获取手机号最近发送时间
	 * 
	 * @return Date
	 * 
	 * @throws
	 */
	private Date getLastSendDate(String phone, String msgType, String tenantId) {
		CloudMessageAuthCode entity = getLastSendMsg(phone, msgType, tenantId);
		if (null != entity) {
			return entity.getCreateTime();
		}
		return null;
	}

	/**
	 * @Title: getLastSendMsg
	 * 
	 * @Description: 获取最后一次发送的校验信息
	 * 
	 * @return CloudMessageAuthCode
	 * 
	 * @throws
	 */
	private CloudMessageAuthCode getLastSendMsg(String phone, String msgType, String tenantId) {
		List<SearchFilter> searchFilters = Lists.newArrayList();
		searchFilters.add(new SearchFilter("tenantId", Operator.EQ, tenantId));
		searchFilters.add(new SearchFilter("phone", Operator.EQ, phone));
		searchFilters.add(new SearchFilter("msgType", Operator.EQ, msgType));
		Pageable pageable = new PageRequest(0, 1, Direction.DESC, "createTime");
		Page<CloudMessageAuthCode> page = cloudMessageAuthCodeDao.findPageByFilter(pageable, searchFilters);
		if (CollectionUtils.isNotEmpty(page.getContent())) {
			return page.getContent().get(0);
		}
		return null;
	}

	// 获取指定位数的随机字符串(包含小写字母、大写字母、数字,0<length)
	private String getRandomString(int length) {
		// 随机字符串的随机字符库
		String KeyString = "0123456789";
		StringBuffer sb = new StringBuffer();
		int len = KeyString.length();
		for (int i = 0; i < length; i++) {
			sb.append(KeyString.charAt((int) Math.round(Math.random() * (len - 1))));
		}
		return sb.toString();
	}

}
