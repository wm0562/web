package com.vortex.cloud.ums.web.rest.np;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vortex.cloud.ums.dataaccess.service.ICloudDepartmentService;
import com.vortex.cloud.ums.dataaccess.service.ICloudOrganizationService;
import com.vortex.cloud.ums.dataaccess.service.ICloudStaffUserTempService;
import com.vortex.cloud.ums.dataaccess.service.ICloudStaffXmService;
import com.vortex.cloud.ums.dataaccess.service.ICommonUtilsService;
import com.vortex.cloud.ums.dto.CloudStaffDto;
import com.vortex.cloud.ums.dto.CloudStaffSearchDto;
import com.vortex.cloud.ums.enums.CompanyTypeEnum;
import com.vortex.cloud.ums.model.CloudStaff;
import com.vortex.cloud.ums.model.upload.CloudStaffUserTemp;
import com.vortex.cloud.ums.support.ManagementConstant;
import com.vortex.cloud.ums.util.FileOperateUtil;
import com.vortex.cloud.ums.util.UploadUtil;
import com.vortex.cloud.ums.util.support.ForeContext;
import com.vortex.cloud.vfs.common.lang.StringUtil;
import com.vortex.cloud.vfs.common.mapper.JsonMapper;
import com.vortex.cloud.vfs.common.tree.ITreeService;
import com.vortex.cloud.vfs.common.web.springmvc.SpringmvcUtils;
import com.vortex.cloud.vfs.data.dto.DataStore;
import com.vortex.cloud.vfs.data.dto.RestResultDto;
import com.vortex.cloud.vfs.data.model.BakDeleteModel;
import com.vortex.cloud.vfs.data.support.SearchFilter;
import com.vortex.cloud.vfs.data.support.SearchFilter.Operator;
import com.vortex.cloud.vfs.data.support.SearchFilters;

/**
 * 厦门二期人员维护接口 ；staff表和user表一步维护；
 * 
 * @author XY
 *
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("cloud/management/rest/np/staffxm")
public class CloudStaffRestNpController {
	private static final Logger logger = LoggerFactory.getLogger(CloudStaffRestNpController.class);

	private static final String FORE_DYNAMIC_SUFFIX = ManagementConstant.PERMISSION_SUFFIX_SA;
	private static final String BACK_DYNAMIC_SUFFIX = ManagementConstant.PERMISSION_SUFFIX_SA;

	@Resource
	private ICloudDepartmentService cloudDepartmentService;
	@Resource
	private ICloudStaffUserTempService cloudStaffUserTempService;
	@Resource
	private ITreeService treeService;
	@Resource
	private ICloudOrganizationService cloudOrganizationService;
	@Resource
	private ICloudStaffXmService cloudStaffXmService;
	@Resource
	private ICommonUtilsService commonUtilsService;
	/**
	 * 添加时的表单校验。
	 * 
	 * @param paramName
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "checkForAdd/{paramName}" + FORE_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	public RestResultDto checkForAdd(@PathVariable("paramName") String paramName, HttpServletRequest request) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = true;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			String tenantId = SpringmvcUtils.getParameter("tenantId");

			String paramVal = SpringmvcUtils.getParameter(paramName);
			if (StringUtils.isBlank(paramName) || StringUtils.isBlank(paramVal)) {
				data = false;
			}

			if ("code".equals(paramName)) {
				if (cloudStaffXmService.isCodeExisted(tenantId, paramVal)) {
					data = false;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = ManagementConstant.REST_RESULT_FAIL;
			msg = "校验失败！";
			exception = e.getMessage();
			data = false;
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
	 * 新增
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "add" + BACK_DYNAMIC_SUFFIX, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public RestResultDto add(HttpServletRequest request, CloudStaffDto dto) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			cloudStaffXmService.save(dto);
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

	/**
	 * 查询列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "pageList" + FORE_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	public RestResultDto pageList(HttpServletRequest request, HttpServletResponse response) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			CloudStaffSearchDto searchDto = this.getSearchCondition(request);

			// 得到分页
			Sort defaultSort = sortMethod(request);
			Pageable pageable = ForeContext.getPageable(request, defaultSort);
			Page<CloudStaffDto> pageResult = cloudStaffXmService.findPageBySearchDto(pageable, searchDto);

			DataStore<CloudStaffDto> ds = new DataStore<>();
			if (pageResult != null) {
				ds.setTotal(pageResult.getTotalElements());
				ds.setRows(pageResult.getContent());
			}
			data = ds;
		} catch (Exception e) {
			e.printStackTrace();
			result = ManagementConstant.REST_RESULT_FAIL;
			msg = "查询分页出错！";
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
	 * 加载详情
	 * 
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "loadStaffDtl" + FORE_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	public RestResultDto loadStaffDtl(HttpServletResponse response) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			Map<String, String> paramMap = new JsonMapper().fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), Map.class);

			String id = paramMap.get("id");
			data = cloudStaffXmService.getById(id);
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
	 * 修改时的表单校验
	 * 
	 * @param paramName
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "checkForUpdate/{paramName}" + FORE_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	public RestResultDto checkForUpdate(@PathVariable("paramName") String paramName, HttpServletRequest request) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = true;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			String tenantId = SpringmvcUtils.getParameter("tenantId");
			String id = SpringmvcUtils.getParameter("id");
			if (StringUtils.isBlank(id)) {
				logger.error("checkForUpdate(), ID is null or empty");
				data = false;
			}

			String paramVal = SpringmvcUtils.getParameter(paramName);
			if (StringUtils.isBlank(paramName) || StringUtils.isBlank(paramVal)) {
				data = false;
			}

			if ("code".equals(paramName)) {
				if (!cloudStaffXmService.validateCodeOnUpdate(tenantId, id, paramVal)) {
					data = false;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = ManagementConstant.REST_RESULT_FAIL;
			msg = "校验失败！";
			exception = e.getMessage();
			data = false;
			logger.error(msg, e);
		} finally {
			restResultDto.setResult(result);
			restResultDto.setMsg(msg);
			restResultDto.setData(data);
			restResultDto.setException(exception);
		}

		return restResultDto;
	}

	@RequestMapping(value = "update" + BACK_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	public RestResultDto update(HttpServletRequest request, @ModelAttribute("staff") CloudStaffDto staff) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			cloudStaffXmService.update(staff);
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

	/**
	 * 删除
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "deletes" + BACK_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	public RestResultDto deletes(HttpServletRequest request, @RequestBody String[] ids) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			List<String> deleteList = new ArrayList<>();
			List<String> remainList = new ArrayList<>();
			this.splitForDeletes(ids, deleteList, remainList);

			cloudStaffXmService.deletesStaffAndUser(deleteList);
			msg = "共" + ids.length + "条,删除成功" + deleteList.size() + "条," + "删除失败" + (remainList.size()) + "条";
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

	/**
	 * 锁定user
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "lock" + BACK_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	public RestResultDto lock(HttpServletResponse response) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			Map<String, String> paramMap = new JsonMapper().fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), Map.class);
			String id = paramMap.get("userId");
			cloudStaffXmService.lockUser(id);
			msg = "锁定成功！";
		} catch (Exception e) {
			e.printStackTrace();
			result = ManagementConstant.REST_RESULT_FAIL;
			msg = "锁定失败！";
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
	 * 锁定user
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "unlock" + BACK_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	public RestResultDto unlock(HttpServletResponse response) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			Map<String, String> paramMap = new JsonMapper().fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), Map.class);
			String userId = paramMap.get("userId");
			cloudStaffXmService.unlockUser(userId);
			msg = "解锁成功！";
		} catch (Exception e) {
			e.printStackTrace();
			result = ManagementConstant.REST_RESULT_FAIL;
			msg = "解锁失败！";
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
	 * 批量授权
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "empower" + BACK_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	public RestResultDto empower(HttpServletResponse response) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			Map<String, Object> paramMap = new JsonMapper().fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), Map.class);
			List<String> userIds = (List<String>) paramMap.get("userIds");
			List<String> roleIds = (List<String>) paramMap.get("roleIds");
			cloudStaffXmService.empower(userIds, roleIds);
			msg = "授权成功！";
		} catch (Exception e) {
			e.printStackTrace();
			result = ManagementConstant.REST_RESULT_FAIL;
			msg = "授权失败！";
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
	 * 批量解除权限
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "repower" + BACK_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	public RestResultDto repower(HttpServletResponse response) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			Map<String, Object> paramMap = new JsonMapper().fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), Map.class);
			List<String> userIds = (List<String>) paramMap.get("userIds");
			List<String> roleIds = (List<String>) paramMap.get("roleIds");
			cloudStaffXmService.repower(userIds, roleIds);
			msg = "授权解除成功！";
		} catch (Exception e) {
			e.printStackTrace();
			result = ManagementConstant.REST_RESULT_FAIL;
			msg = "授权解除失败！";
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
	 * 下载导入模版
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "downloadTemplate")
	public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		UploadUtil.downloadTemplate(request, response, "人员导入模版.zip");
	}

	/**
	 * 下载Excel表
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "download")
	public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {

		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("UTF-8");
		String tenantId = SpringmvcUtils.getParameter("tenantId");
		String userId = SpringmvcUtils.getParameter("userId");

		// 查询配置信息
		String title = "人员信息表";
		// 导出数据
		List<CloudStaffDto> children = Lists.newArrayList();
		// 查询条件
		SearchFilters andSearchFilters = new SearchFilters(SearchFilters.Operator.AND);
		List<SearchFilter> searchFilter = commonUtilsService.buildFromHttpRequest(request);
		searchFilter.add(new SearchFilter("tenantId", Operator.EQ, tenantId));
		searchFilter.add(new SearchFilter("beenDeleted", Operator.EQ, BakDeleteModel.NO_DELETED));
		String columnFields = SpringmvcUtils.getParameter("columnFields");
		String columnNames = SpringmvcUtils.getParameter("columnNames");
		/*
		 * columnFields = columnFields.substring(0, columnFields.lastIndexOf(","));
		 * columnNames = columnNames.substring(0, columnNames.lastIndexOf(","));
		 */
		String downloadAll = SpringmvcUtils.getParameter("downloadAll");
		String downloadIds = SpringmvcUtils.getParameter("downloadIds");

		// 当前有权限的公司
		List<String> companyIds = cloudOrganizationService.getCompanyIdsWithPermission(userId, tenantId);
		// 没有权限直接返回
		if (CollectionUtils.isEmpty(companyIds)) {
			FileOperateUtil.exportExcel(request, response, title, columnFields, columnNames, children);
		}

		List<SearchFilter> companyFilter = Lists.newArrayList();
		companyFilter.add(new SearchFilter("departmentId", Operator.IN, companyIds.toArray()));
		companyFilter.add(new SearchFilter("orgId", Operator.IN, companyIds.toArray()));
		SearchFilters orSearchFilters = new SearchFilters(companyFilter, SearchFilters.Operator.OR);
		andSearchFilters.add(orSearchFilters);
		// 排序
		Sort defSort = sortMethod(request);

		boolean isDownloadAll = (StringUtils.isEmpty(downloadAll) ? false : Boolean.valueOf(downloadAll));
		List<CloudStaff> list = null;
		if (isDownloadAll) {
			if (StringUtils.isNotEmpty(downloadIds)) {
				searchFilter.add(new SearchFilter("id", Operator.IN, StringUtil.splitComma(downloadIds)));
			}
			// searchFilter = CommonUtils.bindTenantId(searchFilter);
			andSearchFilters.addSearchFilter(searchFilter);

			list = cloudStaffXmService.findListByFilters(andSearchFilters, defSort);
		} else {

			CloudStaffSearchDto searchDto = this.getSearchCondition(request);

			Pageable pageable = ForeContext.getPageable(request, defSort);
			Page<CloudStaffDto> pageResult = cloudStaffXmService.findPageBySearchDto(pageable, searchDto);

			/*
			 * Pageable pageable = ForeContext.getPageable(request, defSort);
			 * Page<CloudStaff> pageResult = cloudStaffService.findPageByFilters(pageable,
			 * andSearchFilters.addSearchFilter(searchFilter));
			 */
			if (null != pageResult) {
				children = pageResult.getContent();
			}
		}
		if (CollectionUtils.isNotEmpty(list)) {
			children.addAll(cloudStaffXmService.transferModelToDto(list));
		}
		FileOperateUtil.exportExcel(request, response, title, columnFields, columnNames, children);
	}

	/**
	 * 上传文件(压缩包)
	 * 
	 * @author
	 * @date
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "uploadImportData")
	public void uploadImportData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		UploadUtil.uploadImportData(request, response, CloudStaffUserTemp.class, cloudStaffUserTempService);
	}

	/**
	 * 验证手机号唯一
	 * 
	 * @param paramName
	 * @return
	 */
	@RequestMapping(value = "validatePhone/{param}" + FORE_DYNAMIC_SUFFIX, method = RequestMethod.POST)
	public RestResultDto validatePhone(@PathVariable("param") String paramName) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = true;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			// 入参非空校验
			if (StringUtil.isNullOrEmpty(paramName)) {
				result = ManagementConstant.REST_RESULT_FAIL;
			}
			Map<String, Object> filter = Maps.newHashMap();
			String paramVal = SpringmvcUtils.getParameter(paramName);
			String id = SpringmvcUtils.getParameter("id"); // 更新记录时，也要校验
			String phone = null;

			if (StringUtil.isNullOrEmpty(paramVal)) {
				data = false;
			}
			if (!("phone".equals(paramName))) {
				data = false;
			} else {
				phone = paramVal;
				logger.info("validate():id=" + id + "phone=" + phone);

				// 已经存在就返回false，不存在返回true
				if (cloudStaffXmService.isPhoneExists(id, phone)) {
					data = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = ManagementConstant.REST_RESULT_FAIL;
			msg = "校验失败！";
			exception = e.getMessage();
			data = false;
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
	 * @Title: sortMethod @Description: (排序) @return Sort @throws
	 */
	private Sort sortMethod(HttpServletRequest request) {
		// 得到分页
		List<Order> orders = Lists.newArrayList();
		Sort frontSort = ForeContext.getSort(request);
		orders.add(new Order(Direction.ASC, "orderIndex"));
		orders.add(new Order(Direction.DESC, "createTime"));

		Sort defaultSort = new Sort(orders);
		if (frontSort != null) {
			defaultSort = frontSort.and(defaultSort);
		}

		return defaultSort;
	}

	private CloudStaffSearchDto getSearchCondition(HttpServletRequest request) throws Exception {
		// 查询条件
		String tenantId = SpringmvcUtils.getParameter("tenantId");
		String userId = SpringmvcUtils.getParameter("userId");
		String departmentId = SpringmvcUtils.getParameter("departmentId");
		String selectedId = SpringmvcUtils.getParameter("selectedId");
		String selectedType = SpringmvcUtils.getParameter("selectedType");

		String code = SpringmvcUtils.getParameter("code");
		String name = SpringmvcUtils.getParameter("name");
		String socialSecurityNo = SpringmvcUtils.getParameter("socialSecurityNo");
		String credentialNum = SpringmvcUtils.getParameter("credentialNum");
		String gender = SpringmvcUtils.getParameter("gender");

		String ageGroupStart = SpringmvcUtils.getParameter("ageGroupStart");
		String ageGroupEnd = SpringmvcUtils.getParameter("ageGroupEnd");
		String workYearLimitStart = SpringmvcUtils.getParameter("workYearLimitStart");
		String workYearLimitEnd = SpringmvcUtils.getParameter("workYearLimitEnd");
		String educationId = SpringmvcUtils.getParameter("educationId");
		String partyPostId = SpringmvcUtils.getParameter("partyPostId");
		String partyPostIds = SpringmvcUtils.getParameter("partyPostIds");
		String ckRange = SpringmvcUtils.getParameter("ckRange");
		String isLeave = SpringmvcUtils.getParameter("isLeave");
		String phone = SpringmvcUtils.getParameter("phone");
		logger.info("pageList(" + "tenantId=" + tenantId + ", departmentId=" + departmentId + ", selectedId=" + selectedId + ", selectedType=" + selectedType + ", code=" + code + ", name=" + name + ", socialSecurityNo=" + socialSecurityNo
				+ ", credentialNum=" + credentialNum + ")");

		CloudStaffSearchDto searchDto = new CloudStaffSearchDto();
		searchDto.setTenantId(tenantId);
		searchDto.setDepartmentId(departmentId);

		String orgId = null;
		if (CompanyTypeEnum.ORG.getKey().equals(selectedType)) {
			orgId = selectedId;
		}
		searchDto.setOrgId(orgId);
		searchDto.setUserId(userId);
		searchDto.setGender(gender);
		searchDto.setCode(code);
		searchDto.setName(name);
		searchDto.setSocialSecurityNo(socialSecurityNo);
		searchDto.setCredentialNum(credentialNum);
		searchDto.setAgeGroupEnd(ageGroupEnd);
		searchDto.setAgeGroupStart(ageGroupStart);
		searchDto.setWorkYearLimitStart(workYearLimitStart);
		searchDto.setWorkYearLimitEnd(workYearLimitEnd);
		searchDto.setEducationId(educationId);
		searchDto.setPartyPostId(partyPostId);
		searchDto.setPhone(phone);
		searchDto.setIsLeave(isLeave);
		if (StringUtils.isNotBlank(partyPostIds)) {
			searchDto.setPartyPostIds(Arrays.asList(partyPostIds.split(",")));
		}
		searchDto.setCkRange(ckRange);
		return searchDto;
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
			isAllowDel = cloudStaffXmService.canBeDeleted(id);
			if (isAllowDel) {
				deleteList.add(id);
			} else {
				remainList.add(id);
			}
		}
	}
}
