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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.vortex.cloud.ums.dataaccess.service.ICloudPortalUserService;
import com.vortex.cloud.ums.dto.CloudPortalUserDto;
import com.vortex.cloud.ums.model.CloudPortalUser;
import com.vortex.cloud.ums.util.support.ForeContext;
import com.vortex.cloud.ums.web.basic.BaseController;
import com.vortex.cloud.vfs.common.lang.StringUtil;
import com.vortex.cloud.vfs.common.web.springmvc.SpringmvcUtils;
import com.vortex.cloud.vfs.data.dto.DataStore;
import com.vortex.cloud.vfs.data.dto.RestResultDto;
import com.vortex.cloud.vfs.data.support.SearchFilter;
import com.vortex.cloud.vfs.data.support.SearchFilter.Operator;

@SuppressWarnings("all")
@RestController
@RequestMapping("cloud/management/portalUser")
public class CloudPortalUserController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(CloudPortalUserController.class);

	private static final String FORE_DYNAMIC_SUFFIX = com.vortex.cloud.ums.support.ManagementConstant.PERMISSION_SUFFIX_SA;
	private static final String BACK_DYNAMIC_SUFFIX = com.vortex.cloud.ums.support.ManagementConstant.PERMISSION_SUFFIX_SA;

	@Resource
	private ICloudPortalUserService cloudPortalUserService;

	/**
	 * 根据ID获取数据
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "loadById" + FORE_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	public RestResultDto<?> loadById(String id) {
		try {
			return RestResultDto.newSuccess(cloudPortalUserService.getById(id));
		} catch (Exception e) {
			String error_msg = "根据id获取功能组出错";
			logger.error(error_msg, e.getMessage());
			return RestResultDto.newFalid(error_msg, e.getMessage());
		}
	}

	@RequestMapping(value = "pageList" + FORE_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	// @FunctionCode(value = "CF_MANAGE_FG_LIST", type = ResponseType.Json)
	public RestResultDto<DataStore<CloudPortalUser>> pageList(HttpServletRequest request, HttpServletResponse response) {
		try {
			String userName = SpringmvcUtils.getParameter("userName");
			String phone = SpringmvcUtils.getParameter("phone");
			List<SearchFilter> filterList = Lists.newArrayList();
			filterList.add(new SearchFilter("tenantId", Operator.EQ, StringUtils.isEmpty(SpringmvcUtils.getParameter("tenantId")) ? super.getTenantId(request) : SpringmvcUtils.getParameter("tenantId")));
			if (!StringUtil.isNullOrEmpty(phone)) {
				filterList.add(new SearchFilter("phone", Operator.LIKE, phone));
			}
			if (!StringUtil.isNullOrEmpty(userName)) {
				filterList.add(new SearchFilter("userName", Operator.LIKE, userName));
			}
			Sort sort = new Sort(Direction.DESC, "createTime");
			Pageable pageable = ForeContext.getPageable(request, sort);
			Page<CloudPortalUser> page = cloudPortalUserService.findPageByFilter(pageable, filterList);
			DataStore<CloudPortalUser> dataStore = null;
			if (null != page) {
				List<CloudPortalUser> result = page.getContent();
				dataStore = new DataStore<CloudPortalUser>(page.getTotalElements(), result);
			} else {
				dataStore = new DataStore<CloudPortalUser>();
			}
			return RestResultDto.newSuccess(dataStore);
		} catch (Exception e) {
			String error_msg = "加载分页列表出错";
			logger.error(error_msg, e.getMessage());
			return RestResultDto.newFalid(error_msg, e.getMessage());
		}
	}

	/**
	 * 保存功能组信息
	 * 
	 * @param dto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "save" + BACK_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	// @FunctionCode(value = "CF_MANAGE_FG_ADD", type = ResponseType.Json)
	public RestResultDto<Boolean> saveFunctionGroupInfo(HttpServletRequest request, CloudPortalUserDto dto) {
		try {
			dto.setTenantId(StringUtils.isEmpty(SpringmvcUtils.getParameter("tenantId")) ? super.getTenantId(request) : SpringmvcUtils.getParameter("tenantId"));
			cloudPortalUserService.saveDto(dto);
			return RestResultDto.newSuccess(true);
		} catch (Exception e) {
			String error_msg = "保存出错";
			logger.error(error_msg, e.getMessage());
			return RestResultDto.newFalid(error_msg, e.getMessage());
		}
	}

	/**
	 * 更新功能组信息
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "update" + BACK_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	// @FunctionCode(value = "CF_MANAGE_FG_UPDATE", type = ResponseType.Json)
	public RestResultDto<Boolean> updateFunctionGroupInfo(HttpServletRequest request, CloudPortalUserDto dto) {
		try {
			dto.setTenantId(StringUtils.isEmpty(SpringmvcUtils.getParameter("tenantId")) ? super.getTenantId(request) : SpringmvcUtils.getParameter("tenantId"));
			cloudPortalUserService.updateDto(dto);
			return RestResultDto.newSuccess(true);
		} catch (Exception e) {
			String error_msg = "更新出错";
			logger.error(error_msg, e.getMessage());
			return RestResultDto.newFalid(error_msg, e.getMessage());
		}
	}

	@RequestMapping(value = "delete/{id}" + BACK_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	// @FunctionCode(value = "CF_MANAGE_FG_DEL", type = ResponseType.Json)
	public RestResultDto<Boolean> delete(HttpServletRequest request, @PathVariable("id") String id) {

		try {
			cloudPortalUserService.delete(id);
			return RestResultDto.newSuccess(true);
		} catch (Exception e) {
			String error_msg = "删除失败";
			logger.error(error_msg, e.getMessage());
			return RestResultDto.newFalid(error_msg, e.getMessage());
		}

	}

	/**
	 * 删除记录
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "deletes" + BACK_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	// @FunctionCode(value = "CF_MANAGE_RG_DEL", type = ResponseType.Json)
	public RestResultDto<Boolean> deletes(HttpServletRequest request, @RequestBody List<String> ids) {
		try {
			for (String id : ids) {
				cloudPortalUserService.delete(id);
			}
			return RestResultDto.newSuccess(true);
		} catch (Exception e) {
			String error_msg = "删除失败";
			logger.error(error_msg, e.getMessage());
			return RestResultDto.newFalid(error_msg, e.getMessage());
		}
	}

	@RequestMapping(value = "checkForm/{param}" + FORE_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	public RestResultDto<Boolean> checkForm(@PathVariable("param") String param, HttpServletRequest request) {
		try {

			String value = SpringmvcUtils.getParameter(param);
			String id = SpringmvcUtils.getParameter("id");

			List<SearchFilter> searchFilters = Lists.newArrayList();

			if ("phone".equals(param) || "userName".equals(param)) {
				if (StringUtil.isNullOrEmpty(value)) {
					return RestResultDto.newSuccess(false);
				}
				searchFilters.add(new SearchFilter(param, Operator.EQ, value));
				if (!StringUtil.isNullOrEmpty(id)) {
					searchFilters.add(new SearchFilter("id", Operator.NE, id));
				}
				List<CloudPortalUser> list = cloudPortalUserService.findListByFilter(searchFilters, null);

				if (CollectionUtils.isEmpty(list)) {
					return RestResultDto.newSuccess(true);
				}
			}
			return RestResultDto.newSuccess(false);
		} catch (Exception e) {
			String error_msg = "校验出错";
			logger.error(error_msg, e.getMessage());
			return RestResultDto.newFalid(error_msg, e.getMessage());
		}
	}

	@RequestMapping(value = "resetPassword" + BACK_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	// @FunctionCode(value = "CF_MANAGE_USER_RESET_PWD", type =
	// ResponseType.Json)
	public RestResultDto<Boolean> resetPassword(HttpServletRequest request) {
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
	 * 修改密码 description: 传入的参数拼接在url后面
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "changepassword" + BACK_DYNAMIC_SUFFIX, method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto<?> changePassword(HttpServletRequest request) throws Exception {
		try {
			String userId = StringUtils.isEmpty(SpringmvcUtils.getParameter("userId")) ? super.getUserId(request) : SpringmvcUtils.getParameter("userId");

			String oldPwd = request.getParameter("oldPassword");
			String newPwd = request.getParameter("newPassword");

			cloudPortalUserService.changePassword(userId, oldPwd, newPwd);
			return RestResultDto.newSuccess(true);
		} catch (Exception e) {
			String error_msg = "修改密码失败";
			logger.error(error_msg, e.getMessage());
			return RestResultDto.newFalid(error_msg, e.getMessage());
		}
	}
}
