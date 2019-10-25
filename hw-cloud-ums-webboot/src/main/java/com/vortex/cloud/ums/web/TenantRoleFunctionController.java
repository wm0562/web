package com.vortex.cloud.ums.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vortex.cloud.ums.dataaccess.service.ITenantFunctionRoleService;
import com.vortex.cloud.ums.dto.TenantFunctionRoleDto;
import com.vortex.cloud.ums.support.ManagementConstant;
import com.vortex.cloud.ums.util.orm.Page;
import com.vortex.cloud.ums.util.support.ForeContext;
import com.vortex.cloud.ums.web.basic.BaseController;
import com.vortex.cloud.vfs.common.exception.VortexException;
import com.vortex.cloud.vfs.common.web.springmvc.SpringmvcUtils;
import com.vortex.cloud.vfs.data.dto.DataStore;
import com.vortex.cloud.vfs.data.dto.RestResultDto;

/**
 * @ClassName: TenantRoleFunctionController
 * @Description: 租户角色绑定功能管理
 * @author ZQ shan
 * @date 2018年1月23日 下午3:35:30
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@RestController
@RequestMapping("cloud/management/tenant/functionrole")
public class TenantRoleFunctionController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(FunctionGroupController.class);

	private static final String FORE_DYNAMIC_SUFFIX = ManagementConstant.PERMISSION_SUFFIX_SA;
	private static final String BACK_DYNAMIC_SUFFIX = ManagementConstant.PERMISSION_SUFFIX_SA;

	@Resource
	private ITenantFunctionRoleService tenantFunctionRoleService;

	/**
	 * 保存功能角色信息
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "save/{roleId}" + BACK_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	// @FunctionCode(value = "CF_MANAGE_TENANT_ROLE_FUN_ADD", type =
	// ResponseType.Json)
	public RestResultDto<Boolean> saveFunctionGroupInfo(HttpServletRequest request, @PathVariable String roleId) {
		try {
			String functionIds = SpringmvcUtils.getParameter("functionIds");


			if (StringUtils.isBlank(functionIds)) {
				throw new VortexException("未指定功能！");
			}
			String[] functionIdArr = functionIds.split(",");

			tenantFunctionRoleService.saveFunctionsForRole(roleId, functionIdArr);

			return RestResultDto.newSuccess(true);
		} catch (Exception e) {
			String err = "添加失败";
			logger.error(err, e);
			return RestResultDto.newFalid(err, e.getMessage());
		}
	}

	/**
	 * 删除功能角色信息
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "delete/{id}" + BACK_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	// @FunctionCode(value = "CF_MANAGE_TENANT_ROLE_FUN_DEL", type =
	// ResponseType.Json)
	public RestResultDto<Boolean> deleteFunctionGroupInfo(HttpServletRequest request, @PathVariable String id) {
		try {
			tenantFunctionRoleService.deleteFunctionRole(id);
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
	@RequestMapping(value = "pageList" + FORE_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	// @FunctionCode(value = "CF_MANAGE_TENANT_ROLE_FUN_LIST", type =
	// ResponseType.Json)
	public RestResultDto<?> pageList(HttpServletRequest request, HttpServletResponse response) {
		try {
			String roleId = SpringmvcUtils.getParameter("roleId");

			if (StringUtils.isBlank(roleId)) {
				logger.error("pageList(), 入参为空");
				throw new VortexException("pageList(), 入参为空");
			}

			Pageable pageable = ForeContext.getPageable(request, null);
			Page<TenantFunctionRoleDto> page = null;

			page = tenantFunctionRoleService.getPageByRole(roleId, pageable);

			DataStore<TenantFunctionRoleDto> dataStore = null;
			if (null != page) {
				List<TenantFunctionRoleDto> result = page.getResult();
				dataStore = new DataStore<TenantFunctionRoleDto>(page.getTotalRecords(), result);
			} else {
				dataStore = new DataStore<TenantFunctionRoleDto>();
			}
			return RestResultDto.newSuccess(dataStore);
		} catch (Exception e) {
			String err = "分页获取数据失败";
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
	@RequestMapping(value = "dataList" + FORE_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	public RestResultDto<?> dataList(HttpServletRequest request, HttpServletResponse response) {
		try {
			String roleId = SpringmvcUtils.getParameter("roleId");

			if (StringUtils.isBlank(roleId)) {
				logger.error("dataList(), 入参为空");
				throw new VortexException("dataList(), 入参为空");
			}
			return RestResultDto.newSuccess(tenantFunctionRoleService.getListByRole(roleId));
		} catch (Exception e) {
			String err = "加载列表出错";
			logger.error(err, e);
			return RestResultDto.newFalid(err, e.getMessage());
		}
	}

}
