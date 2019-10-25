package com.vortex.cloud.ums.web.rest.np;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.vortex.cloud.ums.dataaccess.service.IRedisSyncService;
import com.vortex.cloud.ums.support.ManagementConstant;
import com.vortex.cloud.vfs.common.exception.VortexException;
import com.vortex.cloud.vfs.common.lang.StringUtil;
import com.vortex.cloud.vfs.common.mapper.JsonMapper;
import com.vortex.cloud.vfs.common.web.springmvc.SpringmvcUtils;
import com.vortex.cloud.vfs.data.dto.RestResultDto;

@SuppressWarnings("all")
@RestController
@RequestMapping("cloud/management/rest/np/redis")
public class RedisRestNpController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private JsonMapper jm = new JsonMapper();

	@Resource
	private IRedisSyncService redisSyncService;

	/**
	 * @Title: refreshStaff
	 * 
	 * @Description: 刷新租户人员信息
	 * 
	 * @return RestResultDto
	 * 
	 * @throws
	 */
	@RequestMapping(value = "refreshStaff", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto refreshStaff(HttpServletRequest request) throws Exception {
		String msg = null;
		Integer result = null;
		Object data = null;

		try {
			Map<String, String> paramMap = jm.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS),
					Map.class);
			if (MapUtils.isEmpty(paramMap)) {
				throw new VortexException("参数不能为空");
			}
			String _tenantIds = paramMap.get("tenantIds");
			List<String> tenantIds = Lists.newArrayList();
			if (!StringUtil.isNullOrEmpty(_tenantIds)) {
				for (String tenantId : StringUtil.splitComma(_tenantIds)) {
					if (StringUtil.isNullOrEmpty(tenantId)) {
						continue;
					}
					if (tenantIds.contains(tenantId)) {
						continue;
					}
					tenantIds.add(tenantId);
				}
			}
			redisSyncService.syncStaffByTenant(tenantIds);
			result = ManagementConstant.REST_RESULT_SUCC;
			msg = "成功刷新租户用户信息";
			data = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = ManagementConstant.REST_RESULT_FAIL;
			msg = e.getMessage();
			data = null;
			logger.error(msg, e);
		} finally {
			RestResultDto restResultDto = new RestResultDto();
			restResultDto.setData(data);
			restResultDto.setMsg(msg);
			restResultDto.setResult(result);
			return restResultDto;
		}
	}

	/**
	 * @Title: refreshDeptOrg
	 * 
	 * @Description: 刷新租户部门机构信息
	 * 
	 * @return RestResultDto
	 * 
	 * @throws
	 */
	@RequestMapping(value = "refreshDeptOrg", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto refreshDeptOrg(HttpServletRequest request) throws Exception {
		String msg = null;
		Integer result = null;
		Object data = null;

		try {
			Map<String, String> paramMap = jm.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS),
					Map.class);
			if (MapUtils.isEmpty(paramMap)) {
				throw new VortexException("参数不能为空");
			}
			String _tenantIds = paramMap.get("tenantIds");
			List<String> tenantIds = Lists.newArrayList();
			if (!StringUtil.isNullOrEmpty(_tenantIds)) {
				for (String tenantId : StringUtil.splitComma(_tenantIds)) {
					if (StringUtil.isNullOrEmpty(tenantId)) {
						continue;
					}
					if (tenantIds.contains(tenantId)) {
						continue;
					}
					tenantIds.add(tenantId);
				}
			}
			redisSyncService.syncDeptOrgByTenant(tenantIds);
			result = ManagementConstant.REST_RESULT_SUCC;
			msg = "成功刷新租户部门机构信息";
			data = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = ManagementConstant.REST_RESULT_FAIL;
			msg = e.getMessage();
			data = null;
			logger.error(msg, e);
		} finally {
			RestResultDto restResultDto = new RestResultDto();
			restResultDto.setData(data);
			restResultDto.setMsg(msg);
			restResultDto.setResult(result);
			return restResultDto;
		}
	}

}
