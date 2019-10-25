package com.vortex.cloud.ums.web.rest.np;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.vortex.cloud.ums.dataaccess.service.IOrgInfoService;
import com.vortex.cloud.ums.dto.OrgInfoDto;
import com.vortex.cloud.ums.dto.OrgInfoQueryDto;
import com.vortex.cloud.ums.support.ManagementConstant;
import com.vortex.cloud.vfs.common.exception.VortexException;
import com.vortex.cloud.vfs.common.mapper.JsonMapper;
import com.vortex.cloud.vfs.common.web.springmvc.SpringmvcUtils;
import com.vortex.cloud.vfs.data.dto.DataStore;
import com.vortex.cloud.vfs.data.dto.RestResultDto;

@RestController
@RequestMapping("cloud/management/rest/np/orginfo")
public class OrgInfoNpRestController {
	private static final Logger logger = LoggerFactory.getLogger(OrgInfoNpRestController.class);
	private static final String FLAG_DEPT = "1";
	private static final String FLAG_ORG = "2";
	@Resource
	private IOrgInfoService orgInfoService;

	@RequestMapping(value = "saveOrgInfo", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto saveOrgInfo(HttpServletRequest request) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			JsonMapper jm = new JsonMapper();
			OrgInfoDto dto = jm.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), OrgInfoDto.class);
			if (dto.getFlag().equals(FLAG_DEPT)) {
				this.orgInfoService.saveDeptInfo(dto);
			} else if (dto.getFlag().equals(FLAG_ORG)) {
				this.orgInfoService.saveOrgInfo(dto);
			}
			msg = "保存成功";
		} catch (Exception e) {
			e.printStackTrace();
			result = ManagementConstant.REST_RESULT_FAIL;
			msg = "保存失败！";
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

	@RequestMapping(value = "updateOrgInfo", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto updateOrgInfo(HttpServletRequest request) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			JsonMapper jm = new JsonMapper();
			OrgInfoDto dto = jm.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), OrgInfoDto.class);
			if (dto.getFlag().equals(FLAG_DEPT)) {
				this.orgInfoService.updateDeptInfo(dto);
			} else if (dto.getFlag().equals(FLAG_ORG)) {
				this.orgInfoService.updateOrgInfo(dto);
			}
			msg = "更新成功";
		} catch (Exception e) {
			e.printStackTrace();
			result = ManagementConstant.REST_RESULT_FAIL;
			msg = "更新失败！";
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

	@RequestMapping(value = "deleteOrgs", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto deleteOrgs(HttpServletRequest request) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			JsonMapper jm = new JsonMapper();
			Map<String, String> paramMap = jm.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), Map.class);
			String ids = paramMap.get("ids");
			String[] arr = ids.split(",");
			List<String> list = Lists.newArrayList(arr);
			this.orgInfoService.deleteOrgs(list);
			msg = "删除成功";
		} catch (Exception e) {
			e.printStackTrace();
			result = ManagementConstant.REST_RESULT_FAIL;
			msg = "删除失败！";
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

	@RequestMapping(value = "codeCheck", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto codeCheck(HttpServletRequest request) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			JsonMapper jm = new JsonMapper();
			Map<String, String> paramMap = jm.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), Map.class);
			String code = paramMap.get("code");
			String tenantId = paramMap.get("tenantId");
			String flag = paramMap.get("flag");
			String orgId = paramMap.get("orgId");

			data = !this.orgInfoService.isCodeExists(code, tenantId, flag, orgId);
		} catch (Exception e) {
			e.printStackTrace();
			result = ManagementConstant.REST_RESULT_FAIL;
			msg = "校验失败！";
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

	@RequestMapping(value = "loadInfo", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto loadInfo(HttpServletRequest request) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			JsonMapper jm = new JsonMapper();
			Map<String, String> paramMap = jm.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), Map.class);
			String id = paramMap.get("id");
			String flag = paramMap.get("flag");
			if (flag.equals(FLAG_DEPT)) {
				data = this.orgInfoService.loadDeptInfo(id);
			} else if (flag.equals(FLAG_ORG)) {
				data = this.orgInfoService.loadOrgInfo(id);
			}

			msg = "load成功";
		} catch (Exception e) {
			e.printStackTrace();
			result = ManagementConstant.REST_RESULT_FAIL;
			msg = "load失败";
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

	@RequestMapping(value = "pageList", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto pageList(HttpServletRequest request) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			JsonMapper jm = new JsonMapper();
			OrgInfoQueryDto dto = jm.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), OrgInfoQueryDto.class);
			if (dto == null) {
				throw new VortexException("参数不能为空");
			}

			Page<OrgInfoDto> page = this.orgInfoService.queryOrgInfo(dto);
			DataStore<OrgInfoDto> dataStore = new DataStore<>();
			if (page != null) {
				dataStore.setRows(page.getContent());
				dataStore.setTotal(page.getTotalElements());
			}
			data = dataStore;

			msg = "查询成功";
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

	@RequestMapping(value = "changeParent", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto changeParent(HttpServletRequest request) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			JsonMapper jm = new JsonMapper();
			Map<String, String> paramMap = jm.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), Map.class);
			String id = paramMap.get("id");
			String npid = paramMap.get("npid");
			this.orgInfoService.changeParent(id, npid);
			msg = "查询成功";
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
	 * 机构类型列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "listOrgType", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto listOrgType(HttpServletRequest request) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			data = this.orgInfoService.getOrgTypeEnums();
			msg = "查询成功";
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
	 * 支持机构联动下拉框查询
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getOrgsByType", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto getOrgsByType(HttpServletRequest request) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			JsonMapper jm = new JsonMapper();
			Map<String, String> paramMap = jm.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), Map.class);
			String tenantId = paramMap.get("tenantId");
			String parentId = paramMap.get("parentId");
			String orgType = paramMap.get("orgType");
			data = this.orgInfoService.getOrgsByType(tenantId, parentId, orgType);
			msg = "查询成功";
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
	 * 导入的时候根据机构名称查询机构id
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getOrgsByNames", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto getOrgsByNames(HttpServletRequest request) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			JsonMapper jm = new JsonMapper();
			Map<String, Object> paramMap = jm.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), Map.class);
			String tenantId = (String) paramMap.get("tenantId");
			List<String> names = (List<String>) paramMap.get("names");
			data = this.orgInfoService.getOrgsByNames(tenantId, names);
			msg = "查询成功";
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
