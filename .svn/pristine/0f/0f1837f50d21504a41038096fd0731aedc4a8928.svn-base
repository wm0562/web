package com.vortex.cloud.ums.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.vortex.cloud.ums.annotation.FunctionCode;
import com.vortex.cloud.ums.dataaccess.service.ICloudRoleService;
import com.vortex.cloud.ums.dataaccess.service.ICloudUserRoleService;
import com.vortex.cloud.ums.dataaccess.service.ICloudUserService;
import com.vortex.cloud.ums.dataaccess.service.ITenantParamSettingService;
import com.vortex.cloud.ums.dataaccess.service.ITenantUserRoleService;
import com.vortex.cloud.ums.dto.CloudUserRoleDto;
import com.vortex.cloud.ums.dto.CloudUserRoleSearchDto;
import com.vortex.cloud.ums.dto.TenantUserRoleDto;
import com.vortex.cloud.ums.dto.TenantUserRoleSearchDto;
import com.vortex.cloud.ums.enums.ResponseType;
import com.vortex.cloud.ums.model.CloudRole;
import com.vortex.cloud.ums.model.TenantPramSetting;
import com.vortex.cloud.ums.model.TenantRole;
import com.vortex.cloud.ums.util.support.ForeContext;
import com.vortex.cloud.ums.util.utils.PropertyUtils;
import com.vortex.cloud.ums.web.basic.BaseController;
import com.vortex.cloud.vfs.common.lang.StringUtil;
import com.vortex.cloud.vfs.common.tree.ITreeService;
import com.vortex.cloud.vfs.common.web.springmvc.SpringmvcUtils;
import com.vortex.cloud.vfs.data.dto.DataStore;
import com.vortex.cloud.vfs.data.dto.OperateInfo;
import com.vortex.cloud.vfs.data.dto.RestResultDto;

/**
 * @author lishijun 用户角色配置管理
 *
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("cloud/management/user/{userId}/role")
public class UserRoleController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(UserRoleController.class);

	private static final String FORE_DYNAMIC_SUFFIX = com.vortex.cloud.ums.support.ManagementConstant.PERMISSION_SUFFIX_SA;
	private static final String BACK_DYNAMIC_SUFFIX = com.vortex.cloud.ums.support.ManagementConstant.PERMISSION_SUFFIX_SA;

	@Resource
	private ICloudRoleService cloudRoleService;
	@Resource
	private ICloudUserRoleService cloudUserRoleService;
	@Resource
	private ITenantUserRoleService tenantUserRoleService;
	@Resource
	private ICloudUserService cloudUserService;
	@Resource
	private ITreeService treeService;
	@Resource
	private ITenantParamSettingService tenantParamSettingService;

	@ModelAttribute
	public void initModel(@PathVariable("userId") String userId, Model model) {
		model.addAttribute("user", cloudUserService.getById(userId));
	}

	// @RequestMapping(value = "add/{roleIds}" + BACK_DYNAMIC_SUFFIX, method =
	// RequestMethod.POST)
	// @FunctionCode(value = "CF_MANAGE_USER_ROLE_ADD", type =
	// ResponseType.Json)
	// public RestResultDto<Boolean> add(HttpServletRequest request,
	// @ModelAttribute("user") CloudUserDto user,
	// @PathVariable("roleIds") String roleIds) {
	// try {
	// if (StringUtils.isBlank(roleIds)) {
	// return RestResultDto.newSuccess(false, "未指定角色");
	// }
	// String[] roleIdArr = roleIds.split(",");
	// cloudUserRoleService.addRoles(user.getId(), roleIdArr);
	// return RestResultDto.newSuccess(true, "保存成功");
	// } catch (Exception e) {
	// logger.error("UserRoleController.add", e);
	// return RestResultDto.newFalid("保存失败", e.getMessage());
	// }
	//
	// }
	@RequestMapping(value = "getUserRoleStyle" + BACK_DYNAMIC_SUFFIX)
	// @FunctionCode(value = "CF_MANAGE_USER_ROLE_GETROLESTYLE", type =
	// ResponseType.Json)
	public RestResultDto<?> getUserRoleStyle(HttpServletRequest request) {
		try {
			TenantPramSetting set = tenantParamSettingService.findOneByParamName(StringUtils.isEmpty(SpringmvcUtils.getParameter("tenantId")) ? super.getTenantId(request) : SpringmvcUtils.getParameter("tenantId"),
					PropertyUtils.getPropertyValue("TENANT_ROLE_STYLE"), PropertyUtils.getPropertyValue("TENANT_ROLE_STYLE_KEY"));
			Integer role_style = 0;
			if (null != set && !StringUtil.isNullOrEmpty(set.getParmCode())) {
				role_style = Integer.valueOf(set.getParmCode());
			}
			return RestResultDto.newSuccess(role_style);
		} catch (Exception e) {
			String err = "操作失败";
			logger.error(err, e);
			return RestResultDto.newFalid(err, e.getMessage());
		}
	}

	@RequestMapping(value = "add" + BACK_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	@FunctionCode(value = "CF_MANAGE_USER_ROLE_ADD", type = ResponseType.Json)
	public RestResultDto<?> add(HttpServletRequest request, @PathVariable("userId") String userId) {
		try {
			OperateInfo oi = new OperateInfo();
			String roleIds = SpringmvcUtils.getParameter("roleIds");
			String tenantRoleIds = SpringmvcUtils.getParameter("tenantRoleIds");

			String[] roleIdArr = StringUtils.isEmpty(roleIds) ? null : roleIds.split(",");
			String[] tenantRoleIdsArr = StringUtils.isEmpty(tenantRoleIds) ? null : tenantRoleIds.split(",");
			boolean operateSuccess = true;

			cloudUserRoleService.addRolesTenantRoles(userId, roleIdArr, tenantRoleIdsArr);
			return RestResultDto.newSuccess(true);
		} catch (Exception e) {
			String err = "操作失败";
			logger.error(err, e);
			return RestResultDto.newFalid(err, e.getMessage());
		}
	}

	@RequestMapping(value = "pageList" + FORE_DYNAMIC_SUFFIX, method = RequestMethod.POST)

	public RestResultDto<DataStore<CloudUserRoleDto>> pageList(HttpServletRequest request, @PathVariable("userId") String userId) {
		try {
			CloudUserRoleSearchDto searchDto = new CloudUserRoleSearchDto();
			searchDto.setUserId(userId);
			// 得到分页
			Pageable pageable = ForeContext.getPageable(request, null);
			Page<CloudUserRoleDto> pageResult = cloudUserRoleService.findPageBySearchDto(pageable, searchDto);

			DataStore<CloudUserRoleDto> ds = new DataStore<>();
			if (pageResult != null) {
				ds.setTotal(pageResult.getTotalElements());
				ds.setRows(pageResult.getContent());
			}
			return RestResultDto.newSuccess(ds);
		} catch (Exception e) {
			logger.error("UserRoleController.pageList", e);
			return RestResultDto.newFalid("查询失败", e.getMessage());
		}
	}

	@RequestMapping(value = "pageTenantList" + FORE_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	// @FunctionCode(value = "CF_MANAGE_TENANT_USER_ROLE_LIST", type =
	// ResponseType.Json)
	public RestResultDto<?> pageTenantList(HttpServletRequest request, @PathVariable("userId") String userId) {
		try {
			TenantUserRoleSearchDto searchDto = new TenantUserRoleSearchDto();
			searchDto.setUserId(userId);

			// 得到分页
			Pageable pageable = ForeContext.getPageable(request, null);
			Page<TenantUserRoleDto> pageResult = tenantUserRoleService.findPageBySearchDto(pageable, searchDto);

			DataStore<TenantUserRoleDto> ds = new DataStore<>();
			if (pageResult != null) {
				ds.setTotal(pageResult.getTotalElements());
				ds.setRows(pageResult.getContent());
			}
			return RestResultDto.newSuccess(ds);
		} catch (Exception e) {
			String err = "查询失败";
			logger.error(err, e);
			return RestResultDto.newFalid(err, e.getMessage());
		}
	}

	@RequestMapping(value = "delete/{id}" + BACK_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	@FunctionCode(value = "CF_MANAGE_USER_ROLE_DEL", type = ResponseType.Json)
	public RestResultDto<Boolean> delete(HttpServletRequest request, @PathVariable("id") String id) {
		try {
			cloudUserRoleService.delete(id);
			return RestResultDto.newSuccess(true, "删除成功");
		} catch (Exception e) {
			logger.error("UserRoleController.pageList", e);
			return RestResultDto.newFalid("删除失败", e.getMessage());
		}
	}

	@RequestMapping(value = "deleteTenant/{id}" + BACK_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	@ResponseBody
	// @FunctionCode(value = "CF_MANAGE_TENANT_USER_ROLE_DEL", type =
	// ResponseType.Json)
	public RestResultDto<?> deleteTenant(HttpServletRequest request, @PathVariable("id") String id) {
		try {
			tenantUserRoleService.delete(id);
			return RestResultDto.newSuccess(true);
		} catch (Exception e) {
			String err = "删除失败";
			logger.error(err, e);
			return RestResultDto.newFalid(err, e.getMessage());
		}
	}

	/**
	 * 根据当前节点id，返回下面的功能列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * 
	 */
	@RequestMapping(value = "dataList" + BACK_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	public RestResultDto<List<CloudRole>> dataList(HttpServletRequest request, HttpServletResponse response, @PathVariable("userId") String userId) {
		try {
			List<CloudRole> cloudRoleList = cloudUserRoleService.getRolesByUserId(userId);
			return RestResultDto.newSuccess(cloudRoleList);
		} catch (Exception e) {
			logger.error("UserRoleController.pageList", e);
			return RestResultDto.newFalid("查询失败", e.getMessage());
		}
	}

	/**
	 * 根据当前节点id，返回下面的功能列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * 
	 */
	@RequestMapping(value = "dataTenantList" + BACK_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	public RestResultDto<?> dataTenantList(HttpServletRequest request, HttpServletResponse response, @PathVariable("userId") String userId) {
		try {
			List<TenantRole> list = tenantUserRoleService.getRolesByUserId(userId);
			if (CollectionUtils.isEmpty(list)) {
				list = Lists.newArrayList();
			}
			return RestResultDto.newSuccess(list);
		} catch (Exception e) {
			String err = "查询失败";
			logger.error(err, e);
			return RestResultDto.newFalid(err, e.getMessage());
		}
	}
}
