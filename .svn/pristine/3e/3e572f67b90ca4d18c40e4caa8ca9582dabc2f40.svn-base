package com.vortex.cloud.ums.tree;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.google.common.collect.Lists;
import com.vortex.cloud.ums.dataaccess.service.ITenantRoleGroupService;
import com.vortex.cloud.ums.dataaccess.service.ITenantRoleService;
import com.vortex.cloud.ums.model.TenantRole;
import com.vortex.cloud.ums.model.TenantRoleGroup;
import com.vortex.cloud.ums.util.SpringContextHolder;
import com.vortex.cloud.vfs.common.lang.StringUtil;
import com.vortex.cloud.vfs.common.tree.CommonTree;
import com.vortex.cloud.vfs.common.tree.CommonTreeNode;
import com.vortex.cloud.vfs.data.support.SearchFilter;
import com.vortex.cloud.vfs.data.support.SearchFilter.Operator;
import com.vortex.cloud.vfs.data.util.ObjectUtil;

/**
 * 角色组树
 * 
 * @author lsm
 * @date 2016年4月5日
 */
public class TenantRoleGroupTree extends CommonTree {
	private static TenantRoleGroupTree instance;

	private TenantRoleGroupTree() {
	}

	public static TenantRoleGroupTree getInstance() {
		synchronized (TenantRoleGroupTree.class) {
			if (null == instance) {
				instance = new TenantRoleGroupTree();
			}
		}
		return instance;
	}

	@Override
	protected CommonTreeNode transform(Object info) {

		CommonTreeNode node = new CommonTreeNode();
		if (info instanceof TenantRoleGroup) {
			TenantRoleGroup dd = (TenantRoleGroup) info;
			node.setNodeId(StringUtil.clean(dd.getId()));
			node.setParentId(StringUtil.clean(dd.getParentId()));
			node.setText(dd.getName());
			node.setType("RoleGroup");
			// node.setQtip(node.getType() + node.getText());
			node.setQtip(node.getText());
			node.setBindData(ObjectUtil.attributesToMap(dd));
		} else if (info instanceof CommonTreeNode) {
			node = (CommonTreeNode) info;
		} else if (info instanceof TenantRole) {
			TenantRole dd = (TenantRole) info;
			node.setNodeId(StringUtil.clean(dd.getId()));
			node.setParentId(StringUtil.clean(dd.getGroupId()));
			node.setText(dd.getName());
			node.setType("Role");
			// node.setQtip(node.getType() + node.getText());
			node.setQtip(node.getText());
			node.setBindData(ObjectUtil.attributesToMap(dd));
		}
		return node;
	}

	private CommonTreeNode generateRoot() {
		CommonTreeNode root = new CommonTreeNode();
		root.setNodeId("-1");
		root.setText("角色组");
		root.setParentId("0");
		root.setType("Root");
		return root;
	}

	public void reloadRoleGroupTree(String tenantId) {
		List<Object> nodes = Lists.newArrayList();
		try {
			// 添加根
			nodes.add(generateRoot());
			List<TenantRoleGroup> actionGroups = findAllRoleGroup(tenantId);
			nodes.addAll(actionGroups);
			super.reload(nodes, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void reloadRoleTree(String tenantId) {
		List<Object> nodes = Lists.newArrayList();
		try {
			// 添加根
			nodes.add(generateRoot());
			List<TenantRoleGroup> actionGroups = findAllRoleGroup(tenantId);
			List<TenantRole> actions = findAllRole(tenantId);
			nodes.addAll(actionGroups);
			nodes.addAll(actions);
			super.reload(nodes, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private List<TenantRoleGroup> findAllRoleGroup(String tenantId) {
		ITenantRoleGroupService actionGroupService = this.getRoleGroupService();
		List<SearchFilter> searchFilters = Lists.newArrayList();
		searchFilters.add(new SearchFilter("tenantId", Operator.EQ, tenantId));
		return actionGroupService.findListByFilter(searchFilters, new Sort(Direction.ASC, "orderIndex"));

	}

	private List<TenantRole> findAllRole(String tenantId) {
		ITenantRoleService actionService = this.getRoleService();
		List<Order> orders = Lists.newArrayList();
		Order order1 = new Order(Direction.ASC, "orderIndex");
		Order order2 = new Order(Direction.ASC, "name");
		orders.add(order1);
		orders.add(order2);
		List<SearchFilter> searchFilters = Lists.newArrayList();
		searchFilters.add(new SearchFilter("tenantId", Operator.EQ, tenantId));
		return actionService.findListByFilter(searchFilters, new Sort(orders));

	}

	private ITenantRoleGroupService getRoleGroupService() {
		return SpringContextHolder.getBean("tenantRoleGroupService");
	}

	private ITenantRoleService getRoleService() {
		return SpringContextHolder.getBean("tenantRoleService");
	}

}
