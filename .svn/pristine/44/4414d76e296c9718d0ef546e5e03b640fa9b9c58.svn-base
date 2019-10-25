package com.vortex.cloud.ums.xm.tree;

import java.util.ArrayList;
import java.util.List;

import com.vortex.cloud.ums.dataaccess.dao.xm.ISysOrgXmDao;
import com.vortex.cloud.ums.util.SpringContextHolder;
import com.vortex.cloud.ums.xm.dto.SysOrgXmDto;
import com.vortex.cloud.vfs.common.lang.StringUtil;
import com.vortex.cloud.vfs.common.tree.CommonTree;
import com.vortex.cloud.vfs.common.tree.CommonTreeNode;
import com.vortex.cloud.vfs.data.util.ObjectUtil;

/**
 * 角色组树
 * 
 * @author lsm
 * @date 2016年4月5日
 */
public class SysOrgDominaTree extends CommonTree {
	private static SysOrgDominaTree instance;

	private SysOrgDominaTree() {
	}

	public static SysOrgDominaTree getInstance() {
		synchronized (SysOrgDominaTree.class) {
			if (null == instance) {
				instance = new SysOrgDominaTree();
			}
		}
		return instance;
	}

	@Override
	protected CommonTreeNode transform(Object info) {

		CommonTreeNode node = new CommonTreeNode();
		if (info instanceof SysOrgXmDto) {
			SysOrgXmDto dd = (SysOrgXmDto) info;

			node.setNodeId(StringUtil.clean(dd.getId()));
			node.setParentId(StringUtil.clean(dd.getPid()));
			node.setText(dd.getOrg_name());
			node.setType(dd.getExtend12());
			// node.setQtip(node.getType() + node.getText());
			node.setQtip(node.getText());
			node.setBindData(ObjectUtil.attributesToMap(dd));
		}
		return node;
	}

	

	public void reloadSysOrgDominaTree() {
		List<Object> nodes = new ArrayList<Object>();
		try {
			// 添加根
			//nodes.add(generateRoot());
			List<SysOrgXmDto> sysOrgXms = getSysOrgXmDao().listAll();
			nodes.addAll(sysOrgXms);
			super.reload(nodes, null);
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	

	

	

	private ISysOrgXmDao getSysOrgXmDao() {
		return SpringContextHolder.getBean("sysOrgXmDao");
	}

}
