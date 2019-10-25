package com.vortex.cloud.ums.web.rest.np;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.vortex.cloud.ums.dataaccess.service.ICloudOrganizationService;
import com.vortex.cloud.ums.dto.CloudOrganizationDto;
import com.vortex.cloud.ums.model.CloudOrganization;
import com.vortex.cloud.ums.tree.OrganizationTree;
import com.vortex.cloud.ums.util.support.ForeContext;
import com.vortex.cloud.vfs.common.exception.VortexException;
import com.vortex.cloud.vfs.common.tree.ITreeService;
import com.vortex.cloud.vfs.common.web.springmvc.SpringmvcUtils;
import com.vortex.cloud.vfs.data.dto.DataStore;
import com.vortex.cloud.vfs.data.dto.RestResultDto;
import com.vortex.cloud.vfs.data.support.SearchFilter;
import com.vortex.cloud.vfs.data.support.SearchFilter.Operator;

@RestController
@RequestMapping("cloud/management/rest/np/org")
public class OrgRestNpController {

	private static final Logger logger = LoggerFactory.getLogger(OrgRestNpController.class);

	@Resource
	private ICloudOrganizationService cloudOrganizationService;

	@Resource
	private ITreeService treeService;

	@RequestMapping(value = "pageList", method = RequestMethod.POST)
	public RestResultDto<DataStore<CloudOrganizationDto>> pageList(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 查询条件
			String parentId = SpringmvcUtils.getParameter("parentId");
			String orgName = SpringmvcUtils.getParameter("orgName");

			logger.info("CloudOrganizationController.pageList(parentId=" + parentId + ", orgName=" + orgName + ")");

			if (StringUtils.isBlank(parentId)) {
				throw new VortexException("请选择父部门！");
			}

			List<SearchFilter> filterList = Lists.newArrayList();
			filterList.add(new SearchFilter("org.parentId", Operator.EQ, parentId));

			if (!StringUtils.isBlank(orgName)) {
				filterList.add(new SearchFilter("org.orgName", Operator.LIKE, orgName));
			}

			// 得到分页
			Pageable pageable = ForeContext.getPageable(request, null);
			Page<CloudOrganization> pageResult = cloudOrganizationService.findPageByFilter(pageable, filterList);

			DataStore<CloudOrganizationDto> ds = new DataStore<>();
			if (pageResult != null) {
				ds.setTotal(pageResult.getTotalElements());

				List<CloudOrganization> orgList = pageResult.getContent();

				List<CloudOrganizationDto> dtoList = new ArrayList<>();
				CloudOrganizationDto dto = null;
				for (CloudOrganization org : orgList) {
					dto = new CloudOrganizationDto();
					BeanUtils.copyProperties(org, dto);
					dtoList.add(dto);
				}

				ds.setRows(dtoList);
			}

			return RestResultDto.newSuccess(ds);
		} catch (Exception e) {
			return RestResultDto.newFalid("获取列表分页失败", e.getMessage());
		}
	}

	/**
	 * 用于树的加载、刷新
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "loadTree", method = { RequestMethod.POST, RequestMethod.GET })
	public RestResultDto<String> loadTree(HttpServletRequest request, HttpServletResponse response) {
		try {

			String departmentId = SpringmvcUtils.getParameter("departmentId"); // 单位ID
			List<SearchFilter> filterList = new ArrayList<>();
			filterList.add(new SearchFilter("org.departmentId", Operator.EQ, departmentId));

			OrganizationTree organizationTree = OrganizationTree.getInstance();
			organizationTree.reloadOrganizationTree(departmentId, filterList);
			String jsonStr = treeService.generateJsonCheckboxTree(organizationTree, false);
			return RestResultDto.newSuccess(jsonStr);
		} catch (Exception e) {
			logger.error("加载树出错", e);
			return RestResultDto.newFalid("加载树出错", e.getMessage());
		}
	}

	/**
	 * 新增
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "addDtl", method = RequestMethod.POST)
	public RestResultDto<CloudOrganization> addDtl(HttpServletRequest request, CloudOrganizationDto dto) {
		try {
			return RestResultDto.newSuccess(cloudOrganizationService.save(dto), "添加成功");
		} catch (Exception e) {
			logger.error("CloudOrganizationController.addDtl", e);
			return RestResultDto.newFalid("添加失败", e.getMessage());
		}
	}

	/**
	 * 添加时的表单校验。
	 * 
	 * @param paramName
	 * @return
	 */
	@RequestMapping(value = "checkForAdd/{paramName}", method = RequestMethod.POST)
	public RestResultDto<Boolean> checkForAdd(@PathVariable("paramName") String paramName, HttpServletRequest request) {
		try {

			String tenantId = SpringmvcUtils.getParameter("tenantId");
			String paramVal = SpringmvcUtils.getParameter(paramName);
			if (StringUtils.isBlank(paramName) || StringUtils.isBlank(paramVal)) {
				return RestResultDto.newSuccess(false);
			}

			if ("orgCode".equals(paramName)) {
				if (cloudOrganizationService.isCodeExisted(tenantId, paramVal)) {
					return RestResultDto.newSuccess(false);
				} else {
					return RestResultDto.newSuccess(true);
				}
			}

			return RestResultDto.newSuccess(true);
		} catch (Exception e) {
			logger.error("校验出错", e);
			return RestResultDto.newFalid("校验出错", e.getMessage());
		}
	}

	/**
	 * 修改时的表单校验
	 * 
	 * @param paramName
	 * @return
	 */
	@RequestMapping(value = "checkForUpdate/{paramName}", method = RequestMethod.POST)
	public RestResultDto<Boolean> checkForUpdate(@PathVariable("paramName") String paramName) {
		try {

			String tenantId = SpringmvcUtils.getParameter("tenantId");
			String id = SpringmvcUtils.getParameter("id");
			if (StringUtils.isBlank(tenantId) || StringUtils.isBlank(id)) {
				logger.error("checkForUpdate(), 校验参数不足");
				throw new VortexException("校验参数不足");
			}

			String paramVal = SpringmvcUtils.getParameter(paramName);
			if (StringUtils.isBlank(paramName) || StringUtils.isBlank(paramVal)) {
				return RestResultDto.newSuccess(false);
			}

			if ("orgCode".equals(paramName)) {
				return RestResultDto.newSuccess(cloudOrganizationService.validateCodeOnUpdate(tenantId, id, paramVal));
			}

			return RestResultDto.newSuccess(true);
		} catch (Exception e) {
			return RestResultDto.newFalid("校验参数出错", e.getMessage());
		}
	}

	/**
	 * 修改项目组信息
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "updateDtl", method = RequestMethod.POST)
	public RestResultDto<Boolean> updateDtl(HttpServletRequest request, CloudOrganizationDto dto) {

		try {
			cloudOrganizationService.update(dto);
			return RestResultDto.newSuccess(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("CloudOrganizationController.updateDtl()", e);
			return RestResultDto.newFalid("修改失败", e.getMessage());
		}

	}

	/**
	 * 删除机构
	 * 
	 * @param request
	 * @param orgId
	 * @return
	 */
	@RequestMapping(value = "deleteOrg", method = { RequestMethod.POST, RequestMethod.GET })
	public RestResultDto<Boolean> deleteDept(HttpServletRequest request, String orgId) {

		try {
			cloudOrganizationService.deleteOrg(orgId);
			return RestResultDto.newSuccess(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();

			logger.error("CloudOrganizationController.deleteDept()", e);
			return RestResultDto.newFalid("删除失败", e.getMessage());
		}

	}

	@RequestMapping(value = "loadCloudOrgDtl", method = RequestMethod.POST)
	public RestResultDto<CloudOrganizationDto> loadCloudOrgDtl(HttpServletResponse response) {
		try {
			String id = SpringmvcUtils.getParameter("id");
			CloudOrganizationDto dto = cloudOrganizationService.getById(id);
			return RestResultDto.newSuccess(dto);
		} catch (Exception e) {
			return RestResultDto.newFalid("根据id加载机构失败", e.getMessage());
		}
	}

	@RequestMapping(value = "findById", method = RequestMethod.POST)
	public RestResultDto<CloudOrganization> findById(HttpServletResponse response) {
		try {
			String id = SpringmvcUtils.getParameter("id");
			CloudOrganization dto = cloudOrganizationService.findOne(id);
			return RestResultDto.newSuccess(dto);
		} catch (Exception e) {
			return RestResultDto.newFalid("根据id查询机构失败", e.getMessage());
		}
	}
}
