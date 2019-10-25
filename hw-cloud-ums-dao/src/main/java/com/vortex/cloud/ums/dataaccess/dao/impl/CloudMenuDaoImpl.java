/*   
\ * Copyright (C), 2005-2014, 苏州伏泰信息科技有限公司
 */
package com.vortex.cloud.ums.dataaccess.dao.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.vortex.cloud.ums.dataaccess.dao.ICloudMenuDao;
import com.vortex.cloud.ums.dto.CloudMenuDto;
import com.vortex.cloud.ums.dto.CloudMenuSearchDto;
import com.vortex.cloud.ums.dto.MenuTreeDto;
import com.vortex.cloud.ums.model.CloudMenu;
import com.vortex.cloud.ums.support.ManagementConstant;
import com.vortex.cloud.vfs.data.hibernate.repository.SimpleHibernateRepository;
import com.vortex.cloud.vfs.data.model.BakDeleteModel;
import com.vortex.cloud.vfs.data.support.SearchFilter;
import com.vortex.cloud.vfs.data.support.SearchFilter.Operator;

/**
 * @author LiShijun
 * @date 2016年5月23日 上午10:29:40
 * @Description History <author> <time> <desc>
 */
@Repository("cloudMenuDao")
public class CloudMenuDaoImpl extends SimpleHibernateRepository<CloudMenu, String> implements ICloudMenuDao {
	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public DetachedCriteria getDetachedCriteria() {
		DetachedCriteria criteria = DetachedCriteria.forClass(super.getPersistentClass());
		criteria.add(Restrictions.eq("beenDeleted", BakDeleteModel.NO_DELETED));
		return criteria;
	}

	/**
	 * 重写。 实现：nodeCode的写入。nodeCode一级为2个整数位，即一级最多含99个记录。
	 */
	@Override
	public <S extends CloudMenu> S save(S entity) {
		CloudMenu parent = super.findOne(entity.getParentId());
		if (parent == null) { // 顶级记录
			List<SearchFilter> filterList = new ArrayList<SearchFilter>();
			filterList.add(new SearchFilter("parentId", Operator.EQ, entity.getParentId()));
			filterList.add(new SearchFilter("systemId", Operator.EQ, entity.getSystemId()));
			List<CloudMenu> siblingList = super.findListByFilter(filterList, null);
			int siblingListSize = 0;
			if (CollectionUtils.isNotEmpty(siblingList)) {
				siblingListSize = siblingList.size();
			}

			entity.setNodeCode(StringUtils.EMPTY + new DecimalFormat("00").format(siblingListSize + 1));
		} else {
			parent.setChildSerialNumer(parent.getChildSerialNumer() + 1);
			super.update(parent);

			entity.setNodeCode(parent.getNodeCode() + new DecimalFormat("00").format(parent.getChildSerialNumer()));
		}

		entity.setChildSerialNumer(0);
		return super.save(entity);
	}

	@Override
	public Page<CloudMenuDto> findPage(Pageable pageable, CloudMenuSearchDto searchDto) {
		String systemId = searchDto.getSystemId(); // 系统id
		String parentId = searchDto.getParentId();
		String code = searchDto.getCode(); // 编码
		String name = searchDto.getName(); // 名称

		List<SearchFilter> sfList = new ArrayList<SearchFilter>();

		sfList.add(new SearchFilter("systemId", Operator.EQ, systemId));
		sfList.add(new SearchFilter("parentId", Operator.EQ, parentId));
		if (StringUtils.isNotBlank(code)) {
			sfList.add(new SearchFilter("code", Operator.LIKE, code));
		}

		if (StringUtils.isNotBlank(name)) {
			sfList.add(new SearchFilter("name", Operator.LIKE, name));
		}

		Page<CloudMenu> page = super.findPageByFilter(pageable, sfList);
		if (page == null) {
			return null;
		}

		// 将记录由CloudMenu类型转为CloudMenuDto类型
		List<CloudMenu> content = page.getContent();
		List<CloudMenuDto> dtoList = new ArrayList<CloudMenuDto>();
		if (CollectionUtils.isNotEmpty(content)) {
			CloudMenuDto dto = null;
			for (CloudMenu entity : content) {
				dto = new CloudMenuDto();
				BeanUtils.copyProperties(entity, dto);
				dtoList.add(dto);
			}
		}

		return new PageImpl<CloudMenuDto>(dtoList, pageable, page.getTotalElements());
	}

	@Override
	public List<CloudMenu> getMenuList(String systemId) {
		List<SearchFilter> sfList = new ArrayList<SearchFilter>();

		sfList.add(new SearchFilter("systemId", Operator.EQ, systemId));

		Sort sort = new Sort(Direction.DESC, "orderIndex");

		return super.findListByFilter(sfList, sort);
	}

	@Override
	public List<MenuTreeDto> getUserMenuList(String systemCode, String userId) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append(" select distinct code,name,description,id,parentId,photoIds,uri,isLeaf,level,orderIndex,isWelcomeMenu,iconFont from ( ");

		// 第一部分，受控的未隐藏的菜单
		sql.append(" SELECT f.name,f.description,f.id,f.code,f.parentId,f.photoIds,1 isLeaf,LENGTH(f.nodeCode)/2 level,f.orderIndex,f.isWelcomeMenu,CONCAT(g.website,'/',e.uri) uri,f.iconFont ");
		sql.append("  from cloud_system a,cloud_role b,cloud_user_role c,cloud_function_role d,cloud_function e,cloud_menu f,cloud_system g ");
		sql.append(" where a.systemCode=? ");
		sql.append("   and a.id=b.systemId ");
		sql.append("   and b.id=c.roleId ");
		sql.append("   and c.userId=? ");
		sql.append("   and c.roleId=d.roleId ");
		sql.append("   and d.functionId=e.id ");
		sql.append("   and e.id=f.functionId ");
		sql.append("   and e.goalSystemId=g.id ");
		sql.append("   and f.isControlled=? ");
		sql.append("   and f.isHidden=? ");
		sql.append("   and a.beenDeleted=? ");
		sql.append("   and b.beenDeleted=? ");
		sql.append("   and c.beenDeleted=? ");
		sql.append("   and d.beenDeleted=? ");
		sql.append("   and e.beenDeleted=? ");
		sql.append("   and f.beenDeleted=? ");
		sql.append("   and g.beenDeleted=? ");

		sql.append(" UNION ALL ");

		// 第二部分，该系统下不受控且未隐藏的菜单
		sql.append(" SELECT a.name,a.description,a.id,a.code,a.parentId,a.photoIds,1 isLeaf,LENGTH(a.nodeCode)/2 level,a.orderIndex,a.isWelcomeMenu,CONCAT(d.website,'/',c.uri) uri,a.iconFont ");
		sql.append(" from cloud_menu a,cloud_system b,cloud_function c,cloud_system d ");
		sql.append(" where a.systemId=b.id ");
		sql.append("   and b.systemCode=? ");
		sql.append("   and a.functionId=c.id ");
		sql.append("   and c.goalSystemId = d.id ");
		sql.append("   and a.isControlled=? ");
		sql.append("   and a.isHidden=? ");
		sql.append("   and a.beenDeleted=? ");
		sql.append("   and b.beenDeleted=? ");
		sql.append("   and c.beenDeleted=? ");
		sql.append("   and d.beenDeleted=? ");

		sql.append(" ) a order by level,orderIndex ");

		args.add(systemCode);
		args.add(userId);
		args.add(CloudMenu.CONTROLLED_YES);
		args.add(CloudMenu.HIDDEN_NOT);
		args.add(BakDeleteModel.NO_DELETED);
		args.add(BakDeleteModel.NO_DELETED);
		args.add(BakDeleteModel.NO_DELETED);
		args.add(BakDeleteModel.NO_DELETED);
		args.add(BakDeleteModel.NO_DELETED);
		args.add(BakDeleteModel.NO_DELETED);
		args.add(BakDeleteModel.NO_DELETED);

		args.add(systemCode);
		args.add(CloudMenu.CONTROLLED_NOT);
		args.add(CloudMenu.HIDDEN_NOT);
		args.add(BakDeleteModel.NO_DELETED);
		args.add(BakDeleteModel.NO_DELETED);
		args.add(BakDeleteModel.NO_DELETED);
		args.add(BakDeleteModel.NO_DELETED);

		return jdbcTemplate.query(sql.toString(), args.toArray(), BeanPropertyRowMapper.newInstance(MenuTreeDto.class));
	}

	@Override
	public MenuTreeDto getMenuTreeDtoById(String id) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sql.append(" select a.name,a.description,a.id,a.code,a.parentId,a.photoIds,'' uri,0 isLeaf,LENGTH(a.nodeCode)/2 level,a.isWelcomeMenu,a.orderIndex,a.iconFont ");
		sql.append(" from cloud_menu a ");
		sql.append(" where a.id=? ");

		args.add(id);

		List<MenuTreeDto> list = jdbcTemplate.query(sql.toString(), args.toArray(), BeanPropertyRowMapper.newInstance(MenuTreeDto.class));

		return list.get(0);
	}

	@Override
	public List<CloudMenu> getMenusByFunctionId(String functionId) {
		List<SearchFilter> filterList = new ArrayList<SearchFilter>();
		filterList.add(new SearchFilter("functionId", Operator.EQ, functionId));
		return super.findListByFilter(filterList, null);
	}

	@Override
	public List<CloudMenu> getMenusByParentId(String systemId, String parentId) {
		if (StringUtils.isEmpty(systemId) || StringUtils.isEmpty(parentId)) {
			return null;
		}

		String sql = "";
		if (parentId.equals("-1")) { // 第一层需要去掉预设的菜单组
			sql = "select * from cloud_menu t where t.beenDeleted=0 and t.systemId='" + systemId + "' and t.parentId='" + parentId + "' and t.code<>'" + ManagementConstant.SYS_ROOT_MENU_GROUP + "'";
		} else {
			sql = "select * from cloud_menu t where t.beenDeleted=0 and t.systemId='" + systemId + "' and t.parentId='" + parentId + "'";
		}

		return jdbcTemplate.query(sql.toString(), BeanPropertyRowMapper.newInstance(CloudMenu.class));
	}

	@Override
	public String getNextNodeCode(String parentId, String systemId) {
		String sql = "select max(nodeCode) from cloud_menu where parentId=? and systemId=? and beenDeleted=0";
		List<Object> args = new ArrayList<Object>();
		args.add(parentId);
		args.add(systemId);
		List<String> rst = jdbcTemplate.queryForList(sql, String.class, args.toArray());
		String maxCode = null;
		if (CollectionUtils.isEmpty(rst) || StringUtils.isEmpty(rst.get(0))) {
			maxCode = "01";
		} else {
			maxCode = (new Integer(rst.get(0)) + 1) > 9 ? (new Integer(rst.get(0)) + 1) + "" : "0" + (new Integer(rst.get(0)) + 1);
		}

		if ("-1".equals(parentId)) {
			return maxCode;
		} else {
			CloudMenu parent = this.findOne(parentId);
			return parent.getNodeCode() + maxCode;
		}
	}
}
