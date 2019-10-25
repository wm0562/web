package com.vortex.cloud.ums.web.rest.np;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vortex.cloud.ums.dataaccess.service.ICloudUserRoleService;
import com.vortex.cloud.ums.support.ManagementConstant;
import com.vortex.cloud.vfs.common.mapper.JsonMapper;
import com.vortex.cloud.vfs.common.web.springmvc.SpringmvcUtils;
import com.vortex.cloud.vfs.data.dto.RestResultDto;

@RequestMapping("cloud/management/rest/np/userrole")
@RestController
public class UserRoleRestNpController {
	private static final Logger logger = LoggerFactory.getLogger(UserRoleRestNpController.class);
	@Resource
	private ICloudUserRoleService cloudUserRoleService;

	/**
	 * 返回人员拥有的角色关系
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "dataList", method = RequestMethod.POST)
	public RestResultDto dataList(HttpServletRequest request) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			Map<String, String> paramMap = new JsonMapper().fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), Map.class);
			String userId = paramMap.get("userId");
			data = cloudUserRoleService.getRolesByUserId(userId);
		} catch (Exception e) {
			e.printStackTrace();
			result = ManagementConstant.REST_RESULT_FAIL;
			msg = "查询失败！";
			exception = e.getMessage();
			data = null;
			logger.error(msg, e);
		} finally {
			restResultDto.setResult(result);
			restResultDto.setMsg(msg);
			restResultDto.setData(data);
			restResultDto.setException(exception);
		}

		return restResultDto;
	}

	/**
	 * 新增人员角色关系表
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public RestResultDto add(HttpServletRequest request) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			Map<String, String> paramMap = new JsonMapper().fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), Map.class);
			String userId = paramMap.get("userId");
			String roleIds = paramMap.get("roleIds");
			String[] roleIdArr = StringUtils.isEmpty(roleIds) ? null : paramMap.get("roleIds").split(",");
			cloudUserRoleService.addRoles(userId, roleIdArr);
		} catch (Exception e) {
			e.printStackTrace();
			result = ManagementConstant.REST_RESULT_FAIL;
			msg = "查询失败！";
			exception = e.getMessage();
			data = null;
			logger.error(msg, e);
		} finally {
			restResultDto.setResult(result);
			restResultDto.setMsg(msg);
			restResultDto.setData(data);
			restResultDto.setException(exception);
		}

		return restResultDto;
	}
}
