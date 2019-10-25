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
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.vortex.cloud.ums.dataaccess.service.ITenantRoleGroupService;
import com.vortex.cloud.ums.dataaccess.service.ITenantRoleService;
import com.vortex.cloud.ums.dto.TenantRoleGroupDto;
import com.vortex.cloud.ums.model.TenantRole;
import com.vortex.cloud.ums.model.TenantRoleGroup;
import com.vortex.cloud.ums.support.ManagementConstant;
import com.vortex.cloud.ums.tree.TenantRoleGroupTree;
import com.vortex.cloud.ums.util.support.ForeContext;
import com.vortex.cloud.ums.web.basic.BaseController;
import com.vortex.cloud.vfs.common.exception.VortexException;
import com.vortex.cloud.vfs.common.lang.StringUtil;
import com.vortex.cloud.vfs.common.tree.ITreeService;
import com.vortex.cloud.vfs.common.web.springmvc.SpringmvcUtils;
import com.vortex.cloud.vfs.data.dto.DataStore;
import com.vortex.cloud.vfs.data.dto.RestResultDto;
import com.vortex.cloud.vfs.data.support.SearchFilter;
import com.vortex.cloud.vfs.data.support.SearchFilter.Operator;

/**
 * @ClassName: TenantRoleGroupController
 * @Description: 租户角色组管理
 * @author ZQ shan
 * @date 2018年1月23日 下午3:34:51
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@RestController
@RequestMapping("cloud/management/tenant/rolegroup")
public class TenantRoleGroupController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(TenantRoleGroupController.class);
	private static final String FORE_DYNAMIC_SUFFIX = ManagementConstant.PERMISSION_SUFFIX_SA;
	private static final String BACK_DYNAMIC_SUFFIX = ManagementConstant.PERMISSION_SUFFIX_SA;
	@Resource
	private ITreeService treeService;
	@Resource
	private ITenantRoleGroupService tenantRoleGroupService;

	@Resource
	private ITenantRoleService tenantRoleService;

	@RequestMapping(value = "loadTree" + FORE_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	public RestResultDto<?> loadTree(HttpServletRequest request, HttpServletResponse response) {
		try {
			TenantRoleGroupTree roleGroupTree = TenantRoleGroupTree.getInstance();
			roleGroupTree.reloadRoleGroupTree(StringUtils.isEmpty(SpringmvcUtils.getParameter("tenantId")) ? super.getTenantId(request) : SpringmvcUtils.getParameter("tenantId"));
			String jsonStr = treeService.generateJsonCheckboxTree(roleGroupTree, false);
			return RestResultDto.newSuccess(jsonStr);
		} catch (Exception e) {
			String err = "加载角色组树失败";
			logger.error(err, e);
			return RestResultDto.newFalid(err, e.getMessage());
		}
	}

	@RequestMapping(value = "pageList" + FORE_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	// @FunctionCode(value = "CF_MANAGE_TENANT_RG_LIST", type =
	// ResponseType.Json)
	public RestResultDto<?> pageList(HttpServletRequest request, HttpServletResponse response) {
		try {
			String parentId = SpringmvcUtils.getParameter("parentId");

			if (StringUtil.isNullOrEmpty(parentId)) {
				return RestResultDto.newSuccess(new DataStore<TenantRoleGroup>());
			}

			List<SearchFilter> filterList = Lists.newArrayList();
			filterList.add(new SearchFilter("parentId", Operator.EQ, parentId));
			filterList.add(new SearchFilter("tenantId", Operator.EQ, StringUtils.isEmpty(SpringmvcUtils.getParameter("tenantId")) ? super.getTenantId(request) : SpringmvcUtils.getParameter("tenantId")));
			Sort sort = new Sort(Direction.ASC, "orderIndex");
			Pageable pageable = ForeContext.getPageable(request, sort);
			Page<TenantRoleGroup> page = tenantRoleGroupService.findPageByFilter(pageable, filterList);
			DataStore<TenantRoleGroup> dataStore = null;
			if (null != page) {
				List<TenantRoleGroup> result = page.getContent();
				dataStore = new DataStore<TenantRoleGroup>(page.getTotalElements(), result);
			} else {
				dataStore = new DataStore<TenantRoleGroup>();
			}
			return RestResultDto.newSuccess(dataStore);
		} catch (Exception e) {
			String err = "分页获取数据失败";
			logger.error(err, e);
			return RestResultDto.newFalid(err, e.getMessage());
		}
	}

	/**
	 * 保存角色组信息
	 * 
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "save" + BACK_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	// @FunctionCode(value = "CF_MANAGE_TENANT_RG_ADD", type =
	// ResponseType.Json)
	public RestResultDto<?> saveRoleGroupInfo(HttpServletRequest request, TenantRoleGroupDto dto) {
		try {
			dto.setTenantId(StringUtils.isEmpty(SpringmvcUtils.getParameter("tenantId")) ? super.getTenantId(request) : SpringmvcUtils.getParameter("tenantId"));
			tenantRoleGroupService.saveRoleGroup(dto);
			return RestResultDto.newSuccess(true);
		} catch (Exception e) {
			String err = "保存失败";
			logger.error(err, e);
			return RestResultDto.newFalid(err, e.getMessage());
		}
	}

	/**
	 * 根据ID获取数据
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "loadById" + FORE_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	public RestResultDto<?> loadById(String id) {
		try {
			return RestResultDto.newSuccess(tenantRoleGroupService.findRoleGroupById(id));
		} catch (Exception e) {
			String err = "根据id获取数据失败";
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
	// @FunctionCode(value = "CF_MANAGE_TENANT_RG_UPDATE", type =
	// ResponseType.Json)
	public RestResultDto<?> updateRoleGroupInfo(HttpServletRequest request, TenantRoleGroupDto dto) {
		try {
			dto.setTenantId(StringUtils.isEmpty(SpringmvcUtils.getParameter("tenantId")) ? super.getTenantId(request) : SpringmvcUtils.getParameter("tenantId"));
			tenantRoleGroupService.updateRoleGroup(dto);
			return RestResultDto.newSuccess(true);
		} catch (Exception e) {
			String err = "更新失败";
			logger.error(err, e);
			return RestResultDto.newFalid(err, e.getMessage());
		}
	}

	/**
	 * 删除记录
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "delete/{id}" + BACK_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	// @FunctionCode(value = "CF_MANAGE_TENANT_RG_DEL", type =
	// ResponseType.Json)
	public RestResultDto<?> delete(HttpServletRequest request, @PathVariable("id") String id) {

		try {
			this.checkForDelete(id);
			tenantRoleGroupService.deleteRoleGroup(id);
			return RestResultDto.newSuccess(true);
		} catch (Exception e) {
			String err = "删除失败";
			logger.error(err, e);
			return RestResultDto.newFalid(err, e.getMessage());
		}
	}

	/**
	 * 角色组删除校验，删除条件为： 当且仅当组为空组，即没有子组且组下没有直属角色
	 * 
	 * @param oi
	 * @param id
	 */
	private void checkForDelete(String id) {
		List<SearchFilter> sfList = Lists.newArrayList();
		sfList.add(new SearchFilter("parentId", Operator.EQ, id));

		List<TenantRoleGroup> sonList = tenantRoleGroupService.findListByFilter(sfList, null);

		if (CollectionUtils.isNotEmpty(sonList)) {
			throw new VortexException("失败：记录下存在角色组");
		} else {
			sfList.clear();
			sfList.add(new SearchFilter("groupId", Operator.EQ, id));

			List<TenantRole> roleList = tenantRoleService.findListByFilter(sfList, null);

			if (CollectionUtils.isNotEmpty(roleList)) {
				throw new VortexException("失败：记录下存在角色");
			}
		}
	}

	@RequestMapping(value = "checkForm/{param}" + FORE_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	public RestResultDto<?> checkForm(@PathVariable("param") String param, HttpServletRequest request) {
		try {
			boolean flag = false;
			String value = SpringmvcUtils.getParameter(param);
			String id = SpringmvcUtils.getParameter("id");
			List<SearchFilter> searchFilters = Lists.newArrayList();
			searchFilters.add(new SearchFilter("tenantId", Operator.EQ, StringUtils.isEmpty(SpringmvcUtils.getParameter("tenantId")) ? super.getTenantId(request) : SpringmvcUtils.getParameter("tenantId")));

			if ("name".equals(param)) {
				if (StringUtil.isNullOrEmpty(value)) {
					flag = false;
				}
				searchFilters.add(new SearchFilter("name", Operator.EQ, value));

				List<TenantRoleGroup> list = tenantRoleGroupService.findListByFilter(searchFilters, null);

				if (CollectionUtils.isEmpty(list)) {
					flag = true;
				}
				if (!StringUtil.isNullOrEmpty(id) && list.size() == 1 && list.get(0).getId().equals(id)) {
					flag = true;
				}
			} else if ("code".equals(param)) {
				if (StringUtil.isNullOrEmpty(value)) {
					flag = false;
				}
				searchFilters.add(new SearchFilter("code", Operator.EQ, value));
				List<TenantRoleGroup> list = tenantRoleGroupService.findListByFilter(searchFilters, null);
				if (CollectionUtils.isEmpty(list)) {
					flag = true;
				}
				if (!StringUtil.isNullOrEmpty(id) && list.size() == 1 && list.get(0).getId().equals(id)) {
					flag = true;
				}
			}
			return RestResultDto.newSuccess(flag);
		} catch (Exception e) {
			logger.error("校验参数出错", e);
			return RestResultDto.newFalid("校验参数出错", e.getMessage());
		}
	}

}
