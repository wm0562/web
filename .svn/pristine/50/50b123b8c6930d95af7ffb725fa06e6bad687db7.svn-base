package com.vortex.cloud.ums.web.rest.np;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vortex.cloud.ums.dataaccess.service.ICloudTenantResourceService;
import com.vortex.cloud.ums.support.ManagementConstant;
import com.vortex.cloud.vfs.common.exception.VortexException;
import com.vortex.cloud.vfs.common.lang.StringUtil;
import com.vortex.cloud.vfs.common.mapper.JsonMapper;
import com.vortex.cloud.vfs.common.web.springmvc.SpringmvcUtils;
import com.vortex.cloud.vfs.data.dto.RestResultDto;

/**
 * @ClassName: CloudTenantResourceRestNpController
 * @Description: 租户资源表
 * @author ZQ shan
 * @date 2017年12月19日 下午3:59:51
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("cloud/management/rest/np/tenantResource")
public class CloudTenantResourceRestNpController {
	private static final Logger logger = LoggerFactory.getLogger(CloudTenantResourceRestNpController.class);

	@Resource
	private ICloudTenantResourceService cloudTenantResourceService;

	/**
	 * @Title: save @Description: 新增 @return RestResultDto<?> @throws
	 */
	@RequestMapping(value = "save" + ManagementConstant.PERMISSION_SUFFIX_SA)
	public RestResultDto<?> save(HttpServletRequest request) {
		try {
			Map<String, String> paramMap = new JsonMapper()
					.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), Map.class);

			if (MapUtils.isEmpty(paramMap)) {
				throw new VortexException("参数不能为空");
			}
			String tenantId = paramMap.get("tenantId");
			String tenantCode = paramMap.get("tenantCode");
			String roleId = paramMap.get("roleId");
			String roleCode = paramMap.get("roleCode");
			String resourceTypeCode = paramMap.get("resourceTypeCode");
			String resourceIdList = paramMap.get("resourceIdList");
			if (StringUtil.isNullOrEmpty(tenantId)) {
				throw new VortexException("租户id不能为空");
			}
			if (StringUtil.isNullOrEmpty(roleId)) {
				throw new VortexException("角色id不能为空");
			}
			if (StringUtil.isNullOrEmpty(resourceTypeCode) && StringUtil.isNullOrEmpty(resourceIdList)) {
				throw new VortexException("资源类型code或资源ids不能为空");
			}
			cloudTenantResourceService.saveList(tenantId, tenantCode, roleId, roleCode, resourceTypeCode,
					resourceIdList);
			return RestResultDto.newSuccess(true, "新增成功！");
		} catch (Exception e) {
			logger.error("save", e);
			return RestResultDto.newFalid("新增失败", e.getMessage());
		}
	}

	/**
	 * @Title: hasResourceByRoleId @Description: 判断是否有资源(根据角色id) @return
	 *         RestResultDto<?> @throws
	 */
	@RequestMapping(value = "hasResourceByRoleId" + ManagementConstant.PERMISSION_SUFFIX_SA)
	public RestResultDto<?> hasResourceByRoleId(HttpServletRequest request) {
		try {
			Map<String, String> paramMap = new JsonMapper()
					.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), Map.class);

			if (MapUtils.isEmpty(paramMap)) {
				throw new VortexException("参数不能为空");
			}
			String roleId = paramMap.get("roleId");
			String resourceId = paramMap.get("resourceId");
			String resourceTypeCode = paramMap.get("resourceTypeCode");
			if (StringUtil.isNullOrEmpty(roleId)) {
				throw new VortexException("角色id不能为空");
			}
			if (StringUtil.isNullOrEmpty(resourceId)) {
				throw new VortexException("资源id不能为空");
			}
			if (StringUtil.isNullOrEmpty(resourceTypeCode)) {
				throw new VortexException("资源类型编号不能为空");
			}
			return RestResultDto.newSuccess(
					cloudTenantResourceService.hasResourceByRoleId(roleId, resourceTypeCode, resourceId), "查询成功！");
		} catch (Exception e) {
			logger.error("save", e);
			return RestResultDto.newFalid("查询失败", e.getMessage());
		}
	}

	/**
	 * @Title: hasResourceByUserId @Description: 判断是否有资源(根据用户id) @return
	 *         RestResultDto<?> @throws
	 */
	@RequestMapping(value = "hasResourceByUserId" + ManagementConstant.PERMISSION_SUFFIX_SA)
	public RestResultDto<?> hasResourceByUserId(HttpServletRequest request) {
		try {
			Map<String, String> paramMap = new JsonMapper()
					.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), Map.class);

			if (MapUtils.isEmpty(paramMap)) {
				throw new VortexException("参数不能为空");
			}
			String userId = paramMap.get("userId");
			String resourceId = paramMap.get("resourceId");
			String resourceTypeCode = paramMap.get("resourceTypeCode");
			if (StringUtil.isNullOrEmpty(userId)) {
				throw new VortexException("用户id不能为空");
			}
			if (StringUtil.isNullOrEmpty(resourceId)) {
				throw new VortexException("资源id不能为空");
			}
			if (StringUtil.isNullOrEmpty(resourceTypeCode)) {
				throw new VortexException("资源类型编号不能为空");
			}
			return RestResultDto.newSuccess(
					cloudTenantResourceService.hasResourceByUserId(userId, resourceTypeCode, resourceId), "查询成功！");
		} catch (Exception e) {
			logger.error("save", e);
			return RestResultDto.newFalid("查询失败", e.getMessage());
		}
	}

	/**
	 * @Title: delByRoleId @Description: 根据角色id删除数据 @return RestResultDto<?> @throws
	 */
	@RequestMapping(value = "delByRoleId" + ManagementConstant.PERMISSION_SUFFIX_SA, method = RequestMethod.POST)
	public RestResultDto<?> delByRoleId(HttpServletRequest request) {
		try {
			Map<String, String> paramMap = new JsonMapper()
					.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), Map.class);

			if (MapUtils.isEmpty(paramMap)) {
				throw new VortexException("参数不能为空");
			}
			String roleId = paramMap.get("roleId");
			if (StringUtil.isNullOrEmpty(roleId)) {
				throw new VortexException("角色id不能为空");
			}
			cloudTenantResourceService.delByRoleId(roleId);
			return RestResultDto.newSuccess(true, "删除成功！");
		} catch (Exception e) {
			logger.error("save", e);
			return RestResultDto.newFalid("删除失败", e.getMessage());
		}
	}

	/**
	 * @Title: delByIds @Description: 根据角色id删除数据 @return RestResultDto<?> @throws
	 */
	@RequestMapping(value = "delByIds" + ManagementConstant.PERMISSION_SUFFIX_SA, method = RequestMethod.POST)
	public RestResultDto<?> delByIds(HttpServletRequest request) {
		try {
			Map<String, String> paramMap = new JsonMapper()
					.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), Map.class);

			if (MapUtils.isEmpty(paramMap)) {
				throw new VortexException("参数不能为空");
			}
			String ids = paramMap.get("ids");
			if (StringUtil.isNullOrEmpty(ids)) {
				throw new VortexException("ids不能为空");
			}
			cloudTenantResourceService.delByIds(ids);
			return RestResultDto.newSuccess(true, "删除成功！");
		} catch (Exception e) {
			logger.error("save", e);
			return RestResultDto.newFalid("删除失败", e.getMessage());
		}
	}

	/**
	 * @Title: getResourceIdListByUserId @Description:
	 *         根据userId、resourceTypeCode获取资源列表 @return RestResultDto<?> @throws
	 */
	@RequestMapping(value = "getResourceIdListByUserId" + ManagementConstant.PERMISSION_SUFFIX_SA)
	public RestResultDto<?> getResourceIdListByUserId(HttpServletRequest request) {
		try {
			Map<String, String> paramMap = new JsonMapper()
					.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), Map.class);

			if (MapUtils.isEmpty(paramMap)) {
				throw new VortexException("参数不能为空");
			}
			String userId = paramMap.get("userId");
			String resourceTypeCode = paramMap.get("resourceTypeCode");
			if (StringUtil.isNullOrEmpty(userId)) {
				throw new VortexException("用户id不能为空");
			}
			if (StringUtil.isNullOrEmpty(resourceTypeCode)) {
				throw new VortexException("资源类型编号不能为空");
			}
			return RestResultDto.newSuccess(
					cloudTenantResourceService.getResourceIdListByUserId(userId, resourceTypeCode), "查询成功！");
		} catch (Exception e) {
			logger.error("save", e);
			return RestResultDto.newFalid("查询失败", e.getMessage());
		}
	}

	/**
	 * @Title: getResourceIdListByRoleId @Description:
	 *         根据roleId、resourceTypeCode获取资源列表 @return RestResultDto<?> @throws
	 */
	@RequestMapping(value = "getResourceIdListByRoleId" + ManagementConstant.PERMISSION_SUFFIX_SA)
	public RestResultDto<?> getResourceIdListByRoleId(HttpServletRequest request) {
		try {
			Map<String, String> paramMap = new JsonMapper()
					.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), Map.class);

			if (MapUtils.isEmpty(paramMap)) {
				throw new VortexException("参数不能为空");
			}
			String roleId = paramMap.get("roleId");
			String resourceTypeCode = paramMap.get("resourceTypeCode");
			if (StringUtil.isNullOrEmpty(roleId)) {
				throw new VortexException("角色id不能为空");
			}
			if (StringUtil.isNullOrEmpty(resourceTypeCode)) {
				throw new VortexException("资源类型编号不能为空");
			}
			return RestResultDto.newSuccess(
					cloudTenantResourceService.getResourceIdListByRoleId(roleId, resourceTypeCode), "查询成功！");
		} catch (Exception e) {
			logger.error("save", e);
			return RestResultDto.newFalid("查询失败", e.getMessage());
		}
	}

	/**
	 * @Title: delByParam
	 * 
	 * @Description: 根据参数删除数据
	 * 
	 * @return com.vortex.cloud.management.dto.rest.RestResultDto<?>
	 * 
	 * @throws
	 */
	@RequestMapping(value = "delByParam" + ManagementConstant.PERMISSION_SUFFIX_SA, method = RequestMethod.POST)
	public RestResultDto<?> delByParam(HttpServletRequest request) {
		try {
			Map<String, String> paramMap = new JsonMapper()
					.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), Map.class);

			if (MapUtils.isEmpty(paramMap)) {
				throw new VortexException("参数不能为空");
			}
			String roleId = paramMap.get("roleId");
			String resourceTypeCode = paramMap.get("resourceTypeCode");
			String resourceIdList = paramMap.get("resourceIdList");
			if (StringUtil.isNullOrEmpty(roleId)) {
				throw new VortexException("角色id不能为空");
			}
			if (StringUtil.isNullOrEmpty(resourceTypeCode)) {
				throw new VortexException("资源类型编号不能为空");
			}
			if (StringUtil.isNullOrEmpty(resourceIdList)) {
				throw new VortexException("资源ids不能为空");
			}
			cloudTenantResourceService.delByParam(paramMap);
			return RestResultDto.newSuccess(true, "删除成功！");
		} catch (Exception e) {
			logger.error("save", e);
			return RestResultDto.newFalid("删除失败", e.getMessage());
		}
	}
}
