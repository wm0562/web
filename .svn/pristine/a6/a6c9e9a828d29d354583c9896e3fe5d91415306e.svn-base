package com.vortex.cloud.ums.web.rest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vortex.cloud.ums.dataaccess.service.ICloudSystemService;
import com.vortex.cloud.ums.dataaccess.service.ICloudUserService;
import com.vortex.cloud.ums.dataaccess.service.ICommonUtilsService;
import com.vortex.cloud.ums.dataaccess.service.IRedisValidateService;
import com.vortex.cloud.ums.dto.CloudSystemDto;
import com.vortex.cloud.vfs.common.exception.VortexException;
import com.vortex.cloud.vfs.common.web.springmvc.SpringmvcUtils;
import com.vortex.cloud.vfs.data.dto.LoginReturnInfoDto;
import com.vortex.cloud.vfs.data.dto.RestResultDto;

@RestController
@RequestMapping("cloud/management/util")
public class CloudCommonController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private IRedisValidateService redisValidateService;
	@Resource
	private ICloudUserService cloudUserService;
	@Resource
	private ICommonUtilsService commonUtilsService;
	@Resource
	private ICloudSystemService cloudSystemService;

	@RequestMapping(value = "getMenuJson.sa", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto<?> getMenuJson(HttpServletRequest request) throws Exception {

		try {
			String userId = SpringmvcUtils.getParameter("userId");
			String systemCode = SpringmvcUtils.getParameter("systemCode");
			return RestResultDto.newSuccess(redisValidateService.getBsMenuJson(userId, systemCode));
		} catch (Exception e) {
			String error_str = "获取菜单错误";
			e.printStackTrace();
			logger.error(error_str, e);
			return RestResultDto.newFalid(error_str, e.getMessage());
		}
	}

	/**
	 * 修改密码
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "changePassword.sa", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto<?> changePassword(HttpServletRequest request, HttpServletResponse response) {
		try {
			String oldPassword = SpringmvcUtils.getParameter("oldPassword");
			String newPassword = SpringmvcUtils.getParameter("newPassword");
			String userId = commonUtilsService.getUserId(request);
			cloudUserService.changePassword(userId, oldPassword, newPassword);
			return RestResultDto.newSuccess();
		} catch (Exception e) {
			String error_str = "修改密码错误";
			e.printStackTrace();
			logger.error(error_str, e);
			return RestResultDto.newFalid(error_str, e.getMessage());
		}

	}

	@RequestMapping(value = "logininfo.sa", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto<?> logininfo(HttpServletRequest request, HttpServletResponse response) {
		try {
			LoginReturnInfoDto loginReturnInfoDto = commonUtilsService.getLoginInfo(request);
			return RestResultDto.newSuccess(loginReturnInfoDto);
		} catch (Exception e) {
			String error_str = "获取用户登录信息错误";
			e.printStackTrace();
			logger.error(error_str, e);
			return RestResultDto.newFalid(error_str, e.getMessage());
		}

	}

	@RequestMapping(value = "getSystemIdByCode", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto<?> getSystemIdByCode(HttpServletRequest request, HttpServletResponse response) {
		try {
			String systemCode = SpringmvcUtils.getParameter("systemCode");
			String tenantId = StringUtils.isEmpty(SpringmvcUtils.getParameter("tenantId")) ? commonUtilsService.getTenantId() : SpringmvcUtils.getParameter("tenantId");
			CloudSystemDto sys = cloudSystemService.getCloudSystemByCode(tenantId, systemCode);
			if (null == sys) {
				throw new VortexException("未找到该租户下该系统编号");
			}
			return RestResultDto.newSuccess(sys.getId());
		} catch (Exception e) {
			String error_str = "根据系统编号回去id失败";
			e.printStackTrace();
			logger.error(error_str, e);
			return RestResultDto.newFalid(error_str, e.getMessage());
		}

	}
}
