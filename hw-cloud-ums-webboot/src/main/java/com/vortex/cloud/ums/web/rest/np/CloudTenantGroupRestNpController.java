package com.vortex.cloud.ums.web.rest.np;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.vortex.cloud.ums.dataaccess.service.ICloudTenantRelationService;
import com.vortex.cloud.ums.dataaccess.service.IGroupCompanyUserService;
import com.vortex.cloud.ums.dto.TenantDto;
import com.vortex.cloud.ums.dto.tenantgroup.TenantInfoDto;
import com.vortex.cloud.ums.support.ManagementConstant;
import com.vortex.cloud.vfs.common.mapper.JsonMapper;
import com.vortex.cloud.vfs.common.web.springmvc.SpringmvcUtils;
import com.vortex.cloud.vfs.data.dto.RestResultDto;

@RestController
@RequestMapping("cloud/management/rest/np/tenantrelation")
public class CloudTenantGroupRestNpController {
	@Resource
	private ICloudTenantRelationService cloudTenantRelationService;
	@Resource
	private IGroupCompanyUserService groupCompanyUserService;

	@RequestMapping(value = "listExceptViceTenant", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto<List<TenantInfoDto>> listExceptViceTenant(HttpServletResponse response) {
		try {
			return RestResultDto.newSuccess(this.cloudTenantRelationService.listExceptViceTenant(), "查询成功");
		} catch (Exception e) {
			return RestResultDto.newFalid("CloudTenantGroupRestNpController.listExceptViceTenant:查询失败", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "listViceTenant", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto<List<TenantInfoDto>> listViceTenant(HttpServletResponse response) {
		try {
			Map<String, String> paramMap = new JsonMapper().fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), Map.class);
			return RestResultDto.newSuccess(this.cloudTenantRelationService.listViceTenant(paramMap.get("mainTenantId")), "查询成功");
		} catch (Exception e) {
			return RestResultDto.newFalid("CloudTenantGroupRestNpController.listViceTenant:查询失败", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "bandingRelation", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto<String> bandingRelation(HttpServletResponse response) {
		try {
			Map<String, String> paramMap = new JsonMapper().fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), Map.class);
			String mainTenantId = paramMap.get("mainTenantId");
			String vids = paramMap.get("ids");
			List<String> viceTenantIds = null;
			if (StringUtils.isNotEmpty(vids)) {
				String[] ids = vids.split(",");
				viceTenantIds = Lists.newArrayList(ids);
			}

			this.cloudTenantRelationService.bandingRelation(mainTenantId, viceTenantIds);

			return RestResultDto.newSuccess(null, "操作成功");
		} catch (Exception e) {
			return RestResultDto.newFalid("CloudTenantGroupRestNpController.bandingRelation:操作失败", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getAccount", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto<String> getAccount(HttpServletResponse response) {
		try {
			Map<String, String> paramMap = new JsonMapper().fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), Map.class);
			String userId = paramMap.get("userId");
			String targetSystemId = paramMap.get("targetSystemId");
			return RestResultDto.newSuccess(this.groupCompanyUserService.getAccount(userId, targetSystemId), "查询成功");
		} catch (Exception e) {
			return RestResultDto.newFalid("CloudTenantGroupRestNpController.getAccount:查询失败", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getGroupTenantList", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto<List<TenantDto>> getGroupTenantList(HttpServletResponse response) {
		try {
			Map<String, String> paramMap = new JsonMapper().fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), Map.class);
			String id = paramMap.get("id");
			return RestResultDto.newSuccess(this.cloudTenantRelationService.getGroupTenantList(id), "查询成功");
		} catch (Exception e) {
			return RestResultDto.newFalid("CloudTenantGroupRestNpController.getGroupTenantList:查询失败", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getChildrenTenantById", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto<List<TenantDto>> getChildrenTenantById(HttpServletResponse response) {
		try {
			Map<String, String> paramMap = new JsonMapper().fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), Map.class);
			String id = paramMap.get("id");
			return RestResultDto.newSuccess(this.cloudTenantRelationService.listViceTenantDto(id), "查询成功");
		} catch (Exception e) {
			return RestResultDto.newFalid("CloudTenantGroupRestNpController.getChildrenTenantById:查询失败", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getById", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto<TenantDto> getById(HttpServletResponse response) {
		try {
			Map<String, String> paramMap = new JsonMapper().fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), Map.class);
			String id = paramMap.get("id");
			return RestResultDto.newSuccess(this.cloudTenantRelationService.getById(id), "查询成功");
		} catch (Exception e) {
			return RestResultDto.newFalid("CloudTenantGroupRestNpController.getChildrenTenantById:查询失败", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getParentTenantById", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto<TenantDto> getParentTenantById(HttpServletResponse response) {
		try {
			Map<String, String> paramMap = new JsonMapper().fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), Map.class);
			String id = paramMap.get("id");
			return RestResultDto.newSuccess(this.cloudTenantRelationService.getParentTenantById(id), "查询成功");
		} catch (Exception e) {
			return RestResultDto.newFalid("CloudTenantGroupRestNpController.getParentTenantById:查询失败", e.getMessage());
		}
	}
}
