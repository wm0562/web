package com.vortex.cloud.ums.tree;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vortex.cloud.ums.dataaccess.service.ICloudFunctionGroupService;
import com.vortex.cloud.ums.dataaccess.service.ICloudFunctionService;
import com.vortex.cloud.ums.dataaccess.service.ICloudSystemService;
import com.vortex.cloud.ums.model.CloudFunction;
import com.vortex.cloud.ums.model.CloudFunctionGroup;
import com.vortex.cloud.ums.model.CloudSystem;
import com.vortex.cloud.ums.util.SpringContextHolder;
import com.vortex.cloud.ums.util.utils.ObjectUtil;
import com.vortex.cloud.vfs.common.lang.StringUtil;
import com.vortex.cloud.vfs.common.tree.CommonTree;
import com.vortex.cloud.vfs.common.tree.CommonTreeNode;
import com.vortex.cloud.vfs.data.support.SearchFilter;
import com.vortex.cloud.vfs.data.support.SearchFilter.Operator;

/**
 * 功能组树
 * 
 * @author lsm
 * @date 2016年4月5日
 */
public class FunctionGroupTree extends CommonTree {
	private static FunctionGroupTree instance;

	private FunctionGroupTree() {
	}

	public static FunctionGroupTree getInstance() {
		synchronized (FunctionGroupTree.class) {
			if (null == instance) {
				instance = new FunctionGroupTree();
			}
		}
		return instance;
	}

	@Override
	protected CommonTreeNode transform(Object info) {
		Map<String, Object> mapValue = Maps.newHashMap();
		CommonTreeNode node = new CommonTreeNode();
		if (info instanceof CloudFunctionGroup) {
			CloudFunctionGroup dd = (CloudFunctionGroup) info;
			node.setNodeId(StringUtil.clean(dd.getId()));
			node.setParentId(StringUtil.clean(dd.getParentId()));
			node.setText(dd.getName());
			node.setType("FunctionGroup");
			// node.setQtip(node.getType() + node.getText());
			node.setQtip(node.getText());
			mapValue = Maps.newHashMap();
			mapValue.put("id", dd.getId());
			mapValue.put("code", dd.getCode());// 编码
			mapValue.put("name", dd.getName()); // 名称
			mapValue.put("parentId", dd.getParentId()); // 父节点id
			mapValue.put("systemId", dd.getSystemId()); // 所属系统id
			node.setBindData(mapValue);
		} else if (info instanceof CommonTreeNode) {
			node = (CommonTreeNode) info;
		} else if (info instanceof CloudFunction) {
			CloudFunction dd = (CloudFunction) info;
			node.setNodeId(StringUtil.clean(dd.getId()));
			node.setParentId(StringUtil.clean(dd.getGroupId()));
			node.setText(dd.getName() + "(" + dd.getCode() + ")");
			node.setType("Function");
			// node.setQtip(node.getType() + node.getText());
			node.setQtip(node.getText());
			mapValue = Maps.newHashMap();
			mapValue.put("id", dd.getId());
			mapValue.put("code", dd.getCode());// 编码
			mapValue.put("name", dd.getName()); // 名称
			mapValue.put("groupId", dd.getGroupId()); // 组id
			mapValue.put("systemId", dd.getSystemId()); // 所属系统id
			mapValue.put("goalSystemId", dd.getGoalSystemId()); // 指向的系统id
			mapValue.put("functionType", dd.getFunctionType()); // 功能类型
			mapValue.put("mainFunctionId", dd.getMainFunctionId());// 主功能id
			node.setBindData(mapValue);
		} else if (info instanceof CloudSystem) {
			CloudSystem dd = (CloudSystem) info;
			node.setNodeId(StringUtil.clean(dd.getId()));
			node.setParentId("-1");
			node.setText(dd.getSystemName());
			node.setType("System");
			// node.setQtip(node.getType() + node.getText());
			node.setQtip(node.getText());
			mapValue = Maps.newHashMap();
			mapValue.put("id", dd.getId());
			mapValue.put("systemCode", dd.getSystemCode());// 编码
			mapValue.put("systemName", dd.getSystemName()); // 名称
			mapValue.put("systemType", dd.getSystemType()); // 系统类型
			mapValue.put("tenantId", dd.getTenantId()); // 所属系统id
			node.setBindData(ObjectUtil.attributesToMap(dd));
		}
		return node;
	}

	private CommonTreeNode generateRoot() {
		CommonTreeNode root = new CommonTreeNode();
		root.setNodeId("-1");
		root.setText("功能组");
		root.setParentId("0");
		root.setType("Root");
		return root;
	}

	public void reloadFunctionGroupTree(Map<String, Object> map) {
		List<Object> nodes = Lists.newArrayList();
		try {
			// 添加根
			nodes.add(generateRoot());
			List<CloudFunctionGroup> actionGroups = findAllFunctionGroup(map);
			nodes.addAll(actionGroups);
			super.reload(nodes, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void reloadFunctionTree(Map<String, Object> map) {
		List<Object> nodes = Lists.newArrayList();
		try {
			// 添加根
			nodes.add(generateRoot());
			List<CloudFunctionGroup> actionGroups = findAllFunctionGroup(map);
			List<CloudFunction> actions = findAllFunction(map);
			nodes.addAll(actionGroups);
			nodes.addAll(actions);
			super.reload(nodes, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void reloadFunctionTreeByTenantId(String tenantId) {
		List<Object> nodes = Lists.newArrayList();
		try {
			// 添加根
			nodes.add(generateRoot());
			// 获取该租户下所有系统
			List<CloudSystem> sysList = findSystemByTenantId(tenantId);
			List<String> sysIds = Lists.newArrayList();
			if (CollectionUtils.isNotEmpty(sysList)) {
				for (CloudSystem entity : sysList) {
					sysIds.add(entity.getId());
				}
			}

			// 获取该租户下所有系统下所有功能组
			List<CloudFunctionGroup> actionGroups = findFunctionGroupBySystemIds(sysIds);
			List<String> groupIds = Lists.newArrayList();
			if (CollectionUtils.isNotEmpty(actionGroups)) {
				for (CloudFunctionGroup entity : actionGroups) {
					groupIds.add(entity.getId());
					if ("-1".equals(entity.getParentId())) {
						entity.setParentId(entity.getSystemId());
					}
				}
			}
			// 获取该租户下所有系统下所有功能组下所有功能
			List<CloudFunction> actions = findFunctionBySystemIdsGroupIds(sysIds, groupIds);
			nodes.addAll(sysList);
			nodes.addAll(actionGroups);
			nodes.addAll(actions);
			super.reload(nodes, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private List<CloudSystem> findSystemByTenantId(String tenantId) {
		if (StringUtil.isNullOrEmpty(tenantId)) {
			return Lists.newArrayList();
		}
		ICloudSystemService cloudSystemService = this.getCloudSystemService();
		List<SearchFilter> searchFilters = Lists.newArrayList();
		searchFilters.add(new SearchFilter("tenantId", Operator.EQ, tenantId));
		return cloudSystemService.findListByFilter(searchFilters, null);
	}

	private List<CloudFunctionGroup> findFunctionGroupBySystemIds(List<String> sysIds) {
		if (CollectionUtils.isEmpty(sysIds)) {
			return Lists.newArrayList();
		}
		ICloudFunctionGroupService cloudFunctionGroupService = this.getFunctionGroupService();
		List<SearchFilter> searchFilters = Lists.newArrayList();
		searchFilters
				.add(new SearchFilter("systemId", Operator.IN, (String[]) sysIds.toArray(new String[sysIds.size()])));
		return cloudFunctionGroupService.findListByFilter(searchFilters, null);
	}

	private List<CloudFunction> findFunctionBySystemIdsGroupIds(List<String> sysIds, List<String> groupIds) {
		if (CollectionUtils.isEmpty(sysIds) || CollectionUtils.isEmpty(groupIds)) {
			return Lists.newArrayList();
		}
		ICloudFunctionService cloudFunctionService = this.getFunctionService();
		List<SearchFilter> searchFilters = Lists.newArrayList();
		searchFilters
				.add(new SearchFilter("systemId", Operator.IN, (String[]) sysIds.toArray(new String[sysIds.size()])));

		searchFilters.add(
				new SearchFilter("groupId", Operator.IN, (String[]) groupIds.toArray(new String[groupIds.size()])));
		return cloudFunctionService.findListByFilter(searchFilters, null);
	}

	private List<CloudFunctionGroup> findAllFunctionGroup(Map<String, Object> map) {
		ICloudFunctionGroupService actionGroupService = this.getFunctionGroupService();
		return actionGroupService.findListByProperty(map, new Sort(Direction.ASC, "orderIndex"));
	}

	private List<CloudFunction> findAllFunction(Map<String, Object> map) {
		ICloudFunctionService actionService = this.getFunctionService();
		return actionService.findListByProperty(map, new Sort(Direction.ASC, "orderIndex"));

	}

	private ICloudFunctionGroupService getFunctionGroupService() {
		return SpringContextHolder.getBean("cloudFunctionGroupService");
	}

	private ICloudFunctionService getFunctionService() {
		return SpringContextHolder.getBean("cloudFunctionService");
	}

	private ICloudSystemService getCloudSystemService() {
		return SpringContextHolder.getBean("cloudSystemService");
	}

	/**
	 * 加载云系统功能组的树
	 * 
	 * @param map
	 */
	public void reloadCloudSystemFunctionGroupTree() {
		List<Object> nodes = Lists.newArrayList();
		try {
			// 添加根
			nodes.add(generateRoot());

			List<SearchFilter> sfList = Lists.newArrayList();
			sfList.add(new SearchFilter("cloudSystemId", Operator.NNULL, null));

			List<CloudFunctionGroup> actionGroups = this.findAllCloudSystemFunctionGroup(sfList);
			nodes.addAll(actionGroups);

			super.reload(nodes, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<CloudFunctionGroup> findAllCloudSystemFunctionGroup(Iterable<SearchFilter> searchFilter) {
		ICloudFunctionGroupService service = this.getFunctionGroupService();
		return service.findListByFilter(searchFilter, new Sort(Direction.ASC, "orderIndex"));
	}

	public void reloadCloudSystemFunctionTree() {
		List<Object> nodes = Lists.newArrayList();
		try {
			// 添加根
			nodes.add(generateRoot());

			List<SearchFilter> sfList = Lists.newArrayList();
			sfList.add(new SearchFilter("cloudSystemId", Operator.NNULL, null));

			List<CloudFunctionGroup> groupList = findAllCloudSystemFunctionGroup(sfList);
			List<CloudFunction> funList = findAllCloudSystemFunction(sfList);

			nodes.addAll(groupList);
			nodes.addAll(funList);

			super.reload(nodes, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<CloudFunction> findAllCloudSystemFunction(Iterable<SearchFilter> searchFilter) {
		ICloudFunctionService service = this.getFunctionService();

		return service.findListByFilter(searchFilter, new Sort(Direction.ASC, "orderIndex"));
	}
}
