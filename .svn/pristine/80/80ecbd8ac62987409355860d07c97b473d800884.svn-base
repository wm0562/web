package com.vortex.cloud.ums.xm.tree;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import com.google.common.collect.Lists;
import com.vortex.cloud.ums.dataaccess.dao.xm.ISysOrgXmDao;
import com.vortex.cloud.ums.dataaccess.service.ICloudDepartmentService;
import com.vortex.cloud.ums.model.CloudDepartment;
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

public class SysOrgXmTree extends CommonTree {
	private static SysOrgXmTree instance;

	private SysOrgXmTree() {

	}

	public static SysOrgXmTree getInstance() {
		synchronized (SysOrgXmTree.class) {
			if (null == instance) {
				instance = new SysOrgXmTree();
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

		// 其他信息
		searchFilters.clear();
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

		return returnValue;
	}

	private ICloudDepartmentService getDepartmentService() {
		return SpringContextHolder.getBean("cloudDepartmentService");
	}

	private ISysOrgXmDao getSysOrgXmDao() {
		return SpringContextHolder.getBean("sysOrgXmDao");
	}
}
