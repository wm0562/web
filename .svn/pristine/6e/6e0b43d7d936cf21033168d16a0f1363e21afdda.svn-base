package com.vortex.cloud.ums.web.rest.np;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.vortex.cloud.ums.dataaccess.service.ICloudPortalUserService;
import com.vortex.cloud.ums.dto.CloudPortalUserDto;
import com.vortex.cloud.ums.enums.MsgAuthCodeErrorEnum;
import com.vortex.cloud.ums.model.CloudPortalUser;
import com.vortex.cloud.ums.support.ManagementConstant;
import com.vortex.cloud.vfs.common.exception.VortexException;
import com.vortex.cloud.vfs.common.lang.StringUtil;
import com.vortex.cloud.vfs.common.mapper.JsonMapper;
import com.vortex.cloud.vfs.common.web.springmvc.SpringmvcUtils;
import com.vortex.cloud.vfs.data.dto.LoginReturnInfoDto;
import com.vortex.cloud.vfs.data.dto.RestResultDto;
import com.vortex.cloud.vfs.data.support.SearchFilter;
import com.vortex.cloud.vfs.data.support.SearchFilter.Operator;

/**
 * @ClassName: CloudPortalUserRestNpController
 * @Description: 门户网站人员
 * @author ZQ shan
 * @date 2018年1月30日 下午1:48:38
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@SuppressWarnings("all")
@RequestMapping("cloud/management/rest/np/cloudPortalUser")
@RestController
public class CloudPortalUserRestNpController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private JsonMapper jm = new JsonMapper();
	@Resource
	private ICloudPortalUserService cloudPortalUserService;

	/**
	 * @Title: getUserInfoById
	 * 
	 * @Description: 根据userId获取用户信息
	 * 
	 * @return RestResultDto
	 * 
	 * @throws
	 */
	@RequestMapping(value = "getUserInfoById")
	public RestResultDto<?> getUserInfoById(String userId) throws Exception {
		try {
			if (StringUtils.isBlank(userId)) {
				throw new VortexException("请传入参数：userId");
			}
			LoginReturnInfoDto data = new LoginReturnInfoDto();
			CloudPortalUser entity = cloudPortalUserService.findOne(userId);
			if (null != entity) {
				data.setUserId(entity.getId());
				data.setPassword(entity.getPassword());
				data.setUserName(entity.getUserName());
				data.setPhotoId(entity.getProfilePhoto());
				data.setName(entity.getNickname());
				data.setPhone(entity.getPhone());
				data.setTenantId(entity.getTenantId());
			}
			return RestResultDto.newSuccess(data);
		} catch (Exception e) {
			String error_msg = "根据userId获取用户信息失败";
			logger.error(error_msg, e);
			return RestResultDto.newFalid(error_msg, e.getMessage());
		}
	}

	/**
	 * @Title: sendMessageAuthCode
	 * 
	 * @Description: 调用接口发送注册，或忘记密码短信
	 * 
	 * @return RestResultDto
	 * 
	 * @throws
	 */
	@RequestMapping(value = "sendMessageAuthCode", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto<?> sendMessageAuthCode() throws Exception {
		try {
			JsonMapper mapper = new JsonMapper();

			Map<String, Object> paramMap = jm.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS),
					Map.class);
			if (MapUtils.isEmpty(paramMap)) {
				throw new VortexException("参数不能为空");
			}
			String phone = (String) paramMap.get("phone");
			String msgType = (String) paramMap.get("msgType");
			String tenantId = (String) paramMap.get("tenantId");
			String tenantName = (String) paramMap.get("tenantName");
			if (StringUtils.isEmpty(phone)) {
				throw new VortexException("手机号不能为空");
			}
			if (StringUtils.isEmpty(msgType)) {
				throw new VortexException("发送类型不能为空");
			}
			if (StringUtils.isEmpty(tenantName)) {
				throw new VortexException("租户信息不能为空");
			}
			if (StringUtils.isEmpty(tenantId)) {
				throw new VortexException("租户id不能为空");
			}
			Integer result = cloudPortalUserService.sendMessageAuthCode(phone, msgType, tenantName, tenantId);
			if (result == RestResultDto.RESULT_SUCC) {
				return RestResultDto.newSuccess(true);
			} else {
				return new RestResultDto<Boolean>(result, "发送失败", false, MsgAuthCodeErrorEnum.getValueByKey(result));
			}
		} catch (Exception e) {
			String error_msg = "发送失败";
			logger.error(error_msg, e);
			return RestResultDto.newFalid(error_msg, e.getMessage());
		}
	}

	/**
	 * @Title: checkAuthCode
	 * 
	 * @Description: 校验验证码
	 * 
	 * @return RestResultDto
	 * 
	 * @throws
	 */
	@RequestMapping(value = "checkAuthCode", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto<?> checkAuthCode() throws Exception {
		try {
			JsonMapper mapper = new JsonMapper();

			Map<String, Object> paramMap = jm.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS),
					Map.class);
			if (MapUtils.isEmpty(paramMap)) {
				throw new VortexException("参数不能为空");
			}
			String phone = (String) paramMap.get("phone");
			String tenantId = (String) paramMap.get("tenantId");
			String msgType = (String) paramMap.get("msgType");
			String authCode = (String) paramMap.get("authCode");
			if (StringUtils.isEmpty(phone)) {
				throw new VortexException("手机号不能为空");
			}
			if (StringUtils.isEmpty(msgType)) {
				throw new VortexException("发送类型不能为空");
			}
			if (StringUtils.isEmpty(authCode)) {
				throw new VortexException("校验码不能为空");
			}
			if (StringUtils.isEmpty(tenantId)) {
				throw new VortexException("租户id不能为空");
			}
			return RestResultDto.newSuccess(cloudPortalUserService.checkAuthCode(phone, msgType, authCode, tenantId));
		} catch (Exception e) {
			String error_msg = "校验验证码失败";
			logger.error(error_msg, e);
			return RestResultDto.newFalid(error_msg, e.getMessage());
		}
	}

	/**
	 * 保存用户信息
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "save", method = RequestMethod.POST)
	// @FunctionCode(value = "CF_MANAGE_FG_ADD", type = ResponseType.Json)
	public RestResultDto<Boolean> saveDto(CloudPortalUserDto dto) {
		try {
			cloudPortalUserService.saveDto(dto);
			return RestResultDto.newSuccess(true);
		} catch (Exception e) {
			String error_msg = "保存出错";
			logger.error(error_msg, e.getMessage());
			return RestResultDto.newFalid(error_msg, e.getMessage());
		}
	}

	/**
	 * 修改用户信息
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	// @FunctionCode(value = "CF_MANAGE_FG_ADD", type = ResponseType.Json)
	public RestResultDto<Boolean> updateDto(CloudPortalUserDto dto) {
		try {
			cloudPortalUserService.updateDto(dto);
			return RestResultDto.newSuccess(true);
		} catch (Exception e) {
			String error_msg = "修改出错";
			logger.error(error_msg, e.getMessage());
			return RestResultDto.newFalid(error_msg, e.getMessage());
		}
	}

	/**
	 * @Title: getById
	 * 
	 * @Description: 根据id获取信息
	 * 
	 * @return RestResultDto
	 * 
	 * @throws
	 */
	@RequestMapping(value = "getById")
	public RestResultDto<?> getById(String id) throws Exception {
		try {
			if (StringUtils.isBlank(id)) {
				throw new VortexException("请传入参数：id");
			}
			return RestResultDto.newSuccess(cloudPortalUserService.getById(id));
		} catch (Exception e) {
			String error_msg = "根据id获取信息失败";
			logger.error(error_msg, e);
			return RestResultDto.newFalid(error_msg, e.getMessage());
		}
	}

	/**
	 * 修改密码 description: 传入的参数拼接在url后面
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "changepassword", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto<?> changePassword() throws Exception {
		try {
			JsonMapper mapper = new JsonMapper();

			Map<String, Object> paramMap = jm.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS),
					Map.class);
			if (MapUtils.isEmpty(paramMap)) {
				throw new VortexException("参数不能为空");
			}
			String userId = (String) paramMap.get("userId");
			String oldPwd = (String) paramMap.get("oldPassword");
			String newPwd = (String) paramMap.get("newPassword");
			cloudPortalUserService.changePassword(userId, oldPwd, newPwd);
			return RestResultDto.newSuccess(true);
		} catch (Exception e) {
			String error_msg = "修改密码失败";
			logger.error(error_msg, e.getMessage());
			return RestResultDto.newFalid(error_msg, e.getMessage());
		}
	}

	@RequestMapping(value = "resetPassword", method = RequestMethod.POST)
	@ResponseBody
	public RestResultDto<?> resetPassword() {
		try {
			String id = SpringmvcUtils.getParameter("id");
			cloudPortalUserService.resetPassword(id);
			return RestResultDto.newSuccess(true, "重置密码成功,请联系用户尽快修改密码");
		} catch (Exception e) {
			String error_msg = "重置密码失败";
			logger.error(error_msg, e.getMessage());
			return RestResultDto.newFalid(error_msg, e.getMessage());
		}
	}

	/**
	 * @Title: checkUserName
	 * 
	 * @Description: 校验用户名重复
	 * 
	 * @return RestResultDto
	 * 
	 * @throws
	 */
	@RequestMapping(value = "checkUserName", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto<?> checkUserName() throws Exception {
		try {
			JsonMapper mapper = new JsonMapper();

			Map<String, Object> paramMap = jm.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS),
					Map.class);
			if (MapUtils.isEmpty(paramMap)) {
				throw new VortexException("参数不能为空");
			}
			String userName = (String) paramMap.get("userName");
			String tenantId = (String) paramMap.get("tenantId");
			String id = (String) paramMap.get("id");
			if (StringUtils.isEmpty(userName)) {
				throw new VortexException("userName不能为空");
			}
			if (StringUtils.isEmpty(tenantId)) {
				throw new VortexException("tenantId不能为空");
			}
			List<SearchFilter> searchFilters = Lists.newArrayList();
			searchFilters.add(new SearchFilter("userName", Operator.EQ, userName));
			searchFilters.add(new SearchFilter("tenantId", Operator.EQ, tenantId));
			if (!StringUtil.isNullOrEmpty(id)) {
				searchFilters.add(new SearchFilter("id", Operator.NE, id));
			}
			List<CloudPortalUser> list = cloudPortalUserService.findListByFilter(searchFilters, null);
			if (CollectionUtils.isNotEmpty(list)) {
				return RestResultDto.newSuccess(false);
			}
			return RestResultDto.newSuccess(true);
		} catch (Exception e) {
			String error_msg = "校验用户名重复失败";
			logger.error(error_msg, e);
			return RestResultDto.newFalid(error_msg, e.getMessage());
		}
	}
}
