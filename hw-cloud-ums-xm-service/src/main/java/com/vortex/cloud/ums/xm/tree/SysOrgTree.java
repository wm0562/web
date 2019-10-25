package com.vortex.cloud.ums.xm.tree;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vortex.cloud.ums.dataaccess.dao.xm.ISysOrgXmDao;
import com.vortex.cloud.ums.dataaccess.service.ICloudDepartmentService;
import com.vortex.cloud.ums.dataaccess.service.ICloudOrganizationService;
import com.vortex.cloud.ums.dataaccess.service.ITenantDivisionService;
import com.vortex.cloud.ums.model.CloudDepartment;
import com.vortex.cloud.ums.model.CloudOrganization;
import com.vortex.cloud.ums.model.TenantDivision;
import com.vortex.cloud.ums.util.SpringContextHolder;
import com.vortex.cloud.ums.xm.dto.SysOrgTreeDto;
import com.vortex.cloud.ums.xm.dto.SysOrgXmDto;
import com.vortex.cloud.ums.xm.model.SysOrgXm;
import com.vortex.cloud.vfs.common.lang.StringUtil;
import com.vortex.cloud.vfs.common.tree.CommonTree;
import com.vortex.cloud.vfs.common.tree.CommonTreeNode;
import com.vortex.cloud.vfs.data.support.SearchFilter;
import com.vortex.cloud.vfs.data.support.SearchFilter.Operator;
import com.vortex.cloud.vfs.data.util.ObjectUtil;

public class SysOrgTree extends CommonTree {
	private static SysOrgTree instance;

	private SysOrgTree() {

	}

	public static SysOrgTree getInstance() {
		synchronized (SysOrgTree.class) {
			if (null == instance) {
				instance = new SysOrgTree();
			}
		}
		return instance;
	}

	@Override
	protected CommonTreeNode transform(Object info) {
		CommonTreeNode node = new CommonTreeNode();
		if (info instanceof CommonTreeNode) {
			node = (CommonTreeNode) info;
		} else if (info instanceof SysOrgTreeDto) {
			SysOrgTreeDto entity = (SysOrgTreeDto) info;
			node.setNodeId(StringUtil.clean(entity.getId()));
			node.setParentId(StringUtil.clean(entity.getParentId()));
			node.setText(entity.getName());

			node.setBindData(ObjectUtil.attributesToMap(entity));
			if ("0".equals(node.getParentId())) {
				node.setType("Root");
			}

		}
		return node;
	}

	/**
	 * @param param
	 */
	public void reloadTree(Map<String, Object> param) {
		List<Object> nodes = Lists.newArrayList();
		try {
			// 添加
			List<SysOrgTreeDto> list = this.getNodeInfoList(param);
			nodes.addAll(list);

			super.reload(nodes, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<SysOrgTreeDto> getNodeInfoList(Map<String, Object> param) {
		SysOrgTreeDto node = null;
		List<String> divisionIds = Lists.newArrayList();
		List<SysOrgTreeDto> returnValue = Lists.newArrayList();
		if (MapUtils.isEmpty(param)) {
			return returnValue;
		}
		String tenantId = (String) param.get("tenantId");
		if (StringUtil.isNullOrEmpty(tenantId)) {
			return returnValue;
		}
		ICloudDepartmentService cloudDepartmentService = getDepartmentService();
		List<SearchFilter> searchFilters = Lists.newArrayList();
		searchFilters.add(new SearchFilter("tenantId", Operator.EQ, tenantId));
		List<CloudDepartment> dept_list = cloudDepartmentService.findListByFilter(searchFilters, null);
		if (CollectionUtils.isEmpty(dept_list)) {
			return returnValue;
		}
		// 根节点
		CloudDepartment root = dept_list.get(0);
		node = new SysOrgTreeDto().transFromDept(root);
		returnValue.add(node);
		if (!StringUtil.isNullOrEmpty(root.getDivisionId())) {
			divisionIds.add(root.getDivisionId());
		}
		// org
		searchFilters.clear();
		searchFilters.add(new SearchFilter("tenantId", Operator.EQ, tenantId));
		searchFilters.add(new SearchFilter("departmentId", Operator.EQ, root.getId()));
		Sort sort = new Sort(Direction.ASC, "orderIndex");
		ICloudOrganizationService cloudOrganizationService = getOrganizationService();
		List<CloudOrganization> org_list = cloudOrganizationService.findListByFilter(searchFilters, sort);
		for (CloudOrganization org : org_list) {
			node = new SysOrgTreeDto().transFromOrg(org);
			returnValue.add(node);
			if (StringUtil.isNullOrEmpty(org.getDivisionId())) {
				continue;
			}
			if (divisionIds.contains(org.getDivisionId())) {
				continue;
			}
			divisionIds.add(org.getDivisionId());
		}
		// 其他信息
		searchFilters.clear();
		sort = new Sort(Direction.ASC, "order_no");
		List<SysOrgXmDto> xm_list = getSysOrgXmDao().listAll();
		for (SysOrgXm sysOrgXm : xm_list) {
			node = new SysOrgTreeDto().transFromSysOrg(sysOrgXm);
			if ("-1".equals(node.getParentId()) || "0".equals(node.getParentId())) {
				node.setParentId(root.getId());
			}
			returnValue.add(node);
		}
		if (CollectionUtils.isEmpty(divisionIds)) {
			return returnValue;
		}

		// 填充行政区划信息
		ITenantDivisionService tenantDivisionService = getTenantDivisionService();
		searchFilters.clear();
		searchFilters.add(new SearchFilter("tenantId", Operator.EQ, tenantId));
		searchFilters.add(new SearchFilter("id", Operator.IN, (String[]) divisionIds.toArray(new String[divisionIds.size()])));
		List<TenantDivision> divisionList = tenantDivisionService.findListByFilter(searchFilters, null);
		if (CollectionUtils.isEmpty(divisionList)) {
			return returnValue;
		}
		Map<String, TenantDivision> divisionMap = Maps.newHashMap();
		for (TenantDivision cloudDivision : divisionList) {
			divisionMap.put(cloudDivision.getId(), cloudDivision);
		}
		TenantDivision division = null;
		for (SysOrgTreeDto oneNode : returnValue) {
			if (StringUtil.isNullOrEmpty(oneNode.getDivisionId())) {
				continue;
			}
			if (!divisionMap.containsKey(oneNode.getDivisionId())) {
				continue;
			}
			division = divisionMap.get(oneNode.getDivisionId());
			oneNode.setDivisionName(division.getName());
			oneNode.setDivisionType(division.getLevel() + "");
		}
		return returnValue;
	}

	private ICloudDepartmentService getDepartmentService() {
		return SpringContextHolder.getBean("cloudDepartmentService");
	}

	private ICloudOrganizationService getOrganizationService() {
		return SpringContextHolder.getBean("cloudOrganizationService");
	}

	private ITenantDivisionService getTenantDivisionService() {
		return SpringContextHolder.getBean("tenantDivisionService");
	}

	private ISysOrgXmDao getSysOrgXmDao() {
		return SpringContextHolder.getBean("sysOrgXmDao");
	}
}
