package com.vortex.cloud.ums.xm.api;

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
import com.vortex.cloud.ums.support.ManagementConstant;
import com.vortex.cloud.ums.xm.dataaccess.service.ISysOrgXmService;
import com.vortex.cloud.ums.xm.dto.SysOrgQueryDto;
import com.vortex.cloud.ums.xm.dto.SysOrgXmDto;
import com.vortex.cloud.ums.xm.tree.SysOrgDominaTree;
import com.vortex.cloud.ums.xm.tree.SysOrgTree;
import com.vortex.cloud.ums.xm.tree.SysOrgXmTree;
import com.vortex.cloud.vfs.common.exception.VortexException;
import com.vortex.cloud.vfs.common.mapper.JsonMapper;
import com.vortex.cloud.vfs.common.tree.ITreeService;
import com.vortex.cloud.vfs.common.web.springmvc.SpringmvcUtils;
import com.vortex.cloud.vfs.data.dto.DataStore;
import com.vortex.cloud.vfs.data.dto.RestResultDto;

@SuppressWarnings("all")
@RestController
@RequestMapping("cloud/management/rest/np/xm/sysorg")
public class SysOrgXmApi {
	private static final Logger logger = LoggerFactory.getLogger(SysOrgXmApi.class);

	@Resource
	private ISysOrgXmService sysOrgXmService;
	@Resource
	private ITreeService treeService;

	@RequestMapping(value = "saveOrg", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto saveOrgInfo(HttpServletRequest request) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			JsonMapper jm = new JsonMapper();
			SysOrgXmDto dto = jm.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), SysOrgXmDto.class);
			this.sysOrgXmService.saveOrg(dto);
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

	@RequestMapping(value = "updateOrg", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto updateOrg(HttpServletRequest request) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			JsonMapper jm = new JsonMapper();
			SysOrgXmDto dto = jm.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), SysOrgXmDto.class);
			this.sysOrgXmService.updateOrg(dto);
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

	@RequestMapping(value = "loadOrg", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto loadOrg(HttpServletRequest request) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			JsonMapper jm = new JsonMapper();
			Map<String, String> paramMap = jm.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS),
					Map.class);
			String id = paramMap.get("id");
			data = this.sysOrgXmService.loadOrg(id);
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
			Map<String, String> paramMap = jm.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS),
					Map.class);
			String id = paramMap.get("id");
			String npid = paramMap.get("npid");
			this.sysOrgXmService.changeParent(id, npid);
			msg = "操作成功";
		} catch (Exception e) {
			e.printStackTrace();
			result = ManagementConstant.REST_RESULT_FAIL;
			msg = "操作失败！";
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

	@RequestMapping(value = "checkCode", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto checkCode(HttpServletRequest request) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			JsonMapper jm = new JsonMapper();
			Map<String, String> paramMap = jm.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS),
					Map.class);
			String id = paramMap.get("id");
			String code = paramMap.get("org_code");
			data = !this.sysOrgXmService.isCodeExists(code, id);
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

	@RequestMapping(value = "deleteOrgs", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto deleteOrgs(HttpServletRequest request) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			JsonMapper jm = new JsonMapper();
			Map<String, String> paramMap = jm.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS),
					Map.class);
			String ids = paramMap.get("ids");
			String[] arr = ids.split(",");
			List<String> list = Lists.newArrayList(arr);
			this.sysOrgXmService.deleteOrgs(list);
			msg = "删除成功";
		} catch (Exception e) {
			e.printStackTrace();
			result = ManagementConstant.REST_RESULT_FAIL;
			msg = "删除失败！" + e.getMessage();
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
			SysOrgQueryDto dto = jm.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS),
					SysOrgQueryDto.class);
			if (dto == null) {
				throw new VortexException("参数不能为空");
			}

			Page<SysOrgXmDto> page = this.sysOrgXmService.queryOrgInfo(dto);
			DataStore<SysOrgXmDto> dataStore = new DataStore<>();
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

	@RequestMapping(value = "getDeptOrgTree", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto getDeptOrgTree(HttpServletRequest request) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			JsonMapper jm = new JsonMapper();
			Map<String, Object> paramMap = jm.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS),
					Map.class);
			if (paramMap == null) {
				throw new VortexException("参数不能为空");
			}

			SysOrgTree tree = SysOrgTree.getInstance();
			tree.reloadTree(paramMap);
			data = treeService.generateJsonCheckboxTree(tree, false);
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

	@RequestMapping(value = "getSysOrgTree", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto getSysOrgTree(HttpServletRequest request) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			JsonMapper jm = new JsonMapper();
			Map<String, Object> paramMap = jm.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS),
					Map.class);
			if (paramMap == null) {
				throw new VortexException("参数不能为空");
			}

			SysOrgXmTree tree = SysOrgXmTree.getInstance();
			tree.reloadTree(paramMap);
			data = treeService.generateJsonCheckboxTree(tree, false);
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

	@RequestMapping(value = "listByType", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto listByType(HttpServletRequest request) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			JsonMapper jm = new JsonMapper();
			Map<String, String> paramMap = jm.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS),
					Map.class);
			String orgType = paramMap.get("orgType");
			data = this.sysOrgXmService.listByType(orgType);
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

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getNamesByIds", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto getNamesByIds(HttpServletRequest request) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			JsonMapper jm = new JsonMapper();
			Map<String, Object> paramMap = jm.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS),
					Map.class);
			List<String> ids = (List<String>) paramMap.get("ids");
			data = this.sysOrgXmService.getNamesByIds(ids);
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

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getIdsByNames", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto getIdsByNames(HttpServletRequest request) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			JsonMapper jm = new JsonMapper();
			Map<String, Object> paramMap = jm.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS),
					Map.class);
			List<String> names = (List<String>) paramMap.get("names");
			data = this.sysOrgXmService.getIdsByNames(names);
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

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "hasChild", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto hasChild(HttpServletRequest request) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			JsonMapper jm = new JsonMapper();
			Map<String, String> paramMap = jm.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS),
					Map.class);
			String id = paramMap.get("id");
			data = this.sysOrgXmService.hasChild(id);
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

	

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getSysOrgDominaTree", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto getSysOrgDominaTree(HttpServletRequest request) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			JsonMapper jm = new JsonMapper();
			SysOrgDominaTree tree = SysOrgDominaTree.getInstance();
			tree.reloadSysOrgDominaTree();
			data = treeService.generateJsonCheckboxTree(tree, false);
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
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getAllSysOrg", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto getAllSysOrg(HttpServletRequest request) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			
			
			data = sysOrgXmService.getAllSysOrg();
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
