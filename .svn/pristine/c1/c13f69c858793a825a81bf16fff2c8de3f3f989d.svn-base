package com.vortex.cloud.ums.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.vortex.cloud.ums.dataaccess.service.ITenantFunctionRoleService;
import com.vortex.cloud.ums.dataaccess.service.ITenantRoleService;
import com.vortex.cloud.ums.dataaccess.service.ITenantUserRoleService;
import com.vortex.cloud.ums.dto.TenantRoleDto;
import com.vortex.cloud.ums.model.TenantFunctionRole;
import com.vortex.cloud.ums.model.TenantRole;
import com.vortex.cloud.ums.model.TenantUserRole;
import com.vortex.cloud.ums.support.ManagementConstant;
import com.vortex.cloud.ums.tree.TenantRoleGroupTree;
import com.vortex.cloud.ums.util.support.ForeContext;
import com.vortex.cloud.ums.web.basic.BaseController;
import com.vortex.cloud.vfs.common.lang.StringUtil;
import com.vortex.cloud.vfs.common.tree.ITreeService;
import com.vortex.cloud.vfs.common.web.springmvc.SpringmvcUtils;
import com.vortex.cloud.vfs.data.dto.DataStore;
import com.vortex.cloud.vfs.data.dto.RestResultDto;
import com.vortex.cloud.vfs.data.support.SearchFilter;
import com.vortex.cloud.vfs.data.support.SearchFilter.Operator;

/**
 * @ClassName: TenantRoleController
 * @Description: 租户角色管理
 * @author ZQ shan
 * @date 2018年1月23日 下午3:34:35
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@RestController
@RequestMapping("cloud/management/tenant/role")
public class TenantRoleController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(TenantRoleController.class);
	private static final String FORE_DYNAMIC_SUFFIX = ManagementConstant.PERMISSION_SUFFIX_SA;
	private static final String BACK_DYNAMIC_SUFFIX = ManagementConstant.PERMISSION_SUFFIX_SA;

	@Resource
	private ITreeService treeService;

	@Resource
	private ITenantRoleService tenantRoleService;

	@Resource
	private ITenantFunctionRoleService tenantFunctionRoleService;

	@Resource
	private ITenantUserRoleService tenantUserRoleService;

	/**
	 * 根据ID获取数据
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "loadById" + FORE_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	public RestResultDto<?> loadById(String id) {
		try {
			return RestResultDto.newSuccess(tenantRoleService.getRoleInfoById(id));
		} catch (Exception e) {
			String err = "根据ID获取数据出错";
			logger.error(err, e);
			return RestResultDto.newFalid(err, e.getMessage());
		}
	}

	/**
	 * 根据当前节点id，返回下面的角色列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 
	 */
	@RequestMapping(value = "pageList" + FORE_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	// @FunctionCode(value = "CF_MANAGE_TENANT_ROLE_LIST", type =
	// ResponseType.Json)
	public RestResultDto<?> pageList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String parentId = SpringmvcUtils.getParameter("roleGroupId");
			String name = SpringmvcUtils.getParameter("name");
			String tenantId = StringUtils.isEmpty(SpringmvcUtils.getParameter("tenantId")) ? super.getTenantId(request) : SpringmvcUtils.getParameter("tenantId");
			if (StringUtil.isNullOrEmpty(parentId)) {
				return RestResultDto.newSuccess(new DataStore<TenantRole>());
			}
			List<SearchFilter> filterList = Lists.newArrayList();
			if (StringUtils.isNotEmpty(name)) {
				filterList.add(new SearchFilter("name", Operator.LIKE, name));
			}
			filterList.add(new SearchFilter("groupId", Operator.EQ, parentId));
			filterList.add(new SearchFilter("tenantId", Operator.EQ, tenantId));
			Sort sort = new Sort(Direction.ASC, "orderIndex");
			Pageable pageable = ForeContext.getPageable(request, sort);
			Page<TenantRole> page = tenantRoleService.findPageByFilter(pageable, filterList);
			DataStore<TenantRole> dataStore = null;
			if (null != page) {
				List<TenantRole> result = page.getContent();
				dataStore = new DataStore<TenantRole>(page.getTotalElements(), result);
			} else {
				dataStore = new DataStore<TenantRole>();
			}
			return RestResultDto.newSuccess(dataStore);
		} catch (Exception e) {
			String err = "分页获取数据出错";
			logger.error(err, e);
			return RestResultDto.newFalid(err, e.getMessage());
		}
	}

	/**
	 * 保存角色组信息
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "save" + BACK_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	// @FunctionCode(value = "CF_MANAGE_TENANT_ROLE_ADD", type =
	// ResponseType.Json)
	public RestResultDto<Boolean> saveroleInfo(HttpServletRequest request, TenantRoleDto dto) {
		try {
			dto.setTenantId(StringUtils.isEmpty(SpringmvcUtils.getParameter("tenantId")) ? super.getTenantId(request) : SpringmvcUtils.getParameter("tenantId"));
			tenantRoleService.saveRole(dto);
			return RestResultDto.newSuccess(true);
		} catch (Exception e) {
			String err = "保存失败";
			logger.error(err, e);
			return RestResultDto.newFalid(err, e.getMessage());
		}
	}

	/**
	 * 更新角色组信息
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "update" + BACK_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	// @FunctionCode(value = "CF_MANAGE_TENANT_ROLE_UPDATE", type =
	// ResponseType.Json)
	public RestResultDto<Boolean> updateroleInfo(HttpServletRequest request, TenantRoleDto dto) {
		try {
			dto.setTenantId(StringUtils.isEmpty(SpringmvcUtils.getParameter("tenantId")) ? super.getTenantId(request) : SpringmvcUtils.getParameter("tenantId"));
			tenantRoleService.updateRole(dto);
			return RestResultDto.newSuccess(true);
		} catch (Exception e) {
			String err = "更新失败";
			logger.error(err, e);
			return RestResultDto.newFalid(err, e.getMessage());
		}
	}

	/**
	 * 删除1~N条记录
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "deletes" + BACK_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	// @FunctionCode(value = "CF_MANAGE_TENANT_ROLE_DEL", type =
	// ResponseType.Json)
	public RestResultDto<Boolean> deletes(HttpServletRequest request, @RequestBody String[] ids) {
		try {
			List<String> deleteList = Lists.newArrayList();
			List<String> remainList = Lists.newArrayList();
			this.splitForDeletes(ids, deleteList, remainList);

			tenantRoleService.deletes(deleteList);
			return RestResultDto.newSuccess(true, "成功：删除" + deleteList.size() + "条，" + remainList.size() + "条记录被使用不允许删除");
		} catch (Exception e) {
			String err = "删除失败";
			logger.error(err, e);
			return RestResultDto.newFalid(err, e.getMessage());
		}
	}

	/**
	 * 将列表分为可以删除的，不可以删除的。
	 * 
	 * @param ids
	 * @param deleteList
	 *            可以删除的记录
	 * @param remainList
	 *            不可以删除的记录
	 */
	private void splitForDeletes(String[] ids, List<String> deleteList, List<String> remainList) {
		if (ArrayUtils.isEmpty(ids)) {
			return;
		}

		boolean isAllowDel = false;
		for (String id : ids) {
			isAllowDel = this.checkForDelete(id);
			if (isAllowDel) {
				deleteList.add(id);
			} else {
				remainList.add(id);
			}
		}
	}

	/**
	 * 角色是否允许删除 判断是否与功能关联 判断是否与用户关联
	 * 
	 * @param id
	 */
	private boolean checkForDelete(String id) {
		// 是否关联功能
		List<SearchFilter> sfList = Lists.newArrayList();
		sfList.add(new SearchFilter("roleId", Operator.EQ, id));

		List<TenantFunctionRole> funRoleList = tenantFunctionRoleService.findListByFilter(sfList, null);
		if (CollectionUtils.isNotEmpty(funRoleList)) {
			return false;
		}

		// 是否关联用户
		sfList.clear();
		sfList.add(new SearchFilter("roleId", Operator.EQ, id));
		List<TenantUserRole> userRoleList = tenantUserRoleService.findListByFilter(sfList, null);
		if (CollectionUtils.isNotEmpty(userRoleList)) {
			return false;
		}

		return true;
	}

	/**
	 * 验证角色code的唯一性
	 * 
	 * @param request
	 * 
	 * @param code
	 * @return 返回是否成功
	 */
	@RequestMapping(value = "checkForm/{param}" + FORE_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	public RestResultDto<Boolean> checkForm(@PathVariable("param") String param, HttpServletRequest request) {
		try {
			boolean success = false;
			String value = SpringmvcUtils.getParameter(param);
			String id = SpringmvcUtils.getParameter("id");
			if (param.equals("code")) {
				success = !tenantRoleService.isRoleCodeExists(id, value, StringUtils.isEmpty(SpringmvcUtils.getParameter("tenantId")) ? super.getTenantId(request) : SpringmvcUtils.getParameter("tenantId"));// 存在返回false
			}
			return RestResultDto.newSuccess(success);
		} catch (Exception e) {
			String err = "验证code唯一性失败";
			logger.error(err, e);
			return RestResultDto.newFalid(err, e.getMessage());
		}
	}

	/**
	 * 加载角色树
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "loadRoleTree" + FORE_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	public RestResultDto<?> loadRoleTree(HttpServletRequest request, HttpServletResponse response) {
		try {
			TenantRoleGroupTree roleGroupTree = TenantRoleGroupTree.getInstance();
			roleGroupTree.reloadRoleTree(StringUtils.isEmpty(SpringmvcUtils.getParameter("tenantId")) ? super.getTenantId(request) : SpringmvcUtils.getParameter("tenantId"));
			String jsonStr = treeService.generateJsonCheckboxTree(roleGroupTree, false);
			return RestResultDto.newSuccess(jsonStr);
		} catch (Exception e) {
			String err = "加载角色树失败";
			logger.error(err, e);
			return RestResultDto.newFalid(err, e.getMessage());
		}
	}

}
