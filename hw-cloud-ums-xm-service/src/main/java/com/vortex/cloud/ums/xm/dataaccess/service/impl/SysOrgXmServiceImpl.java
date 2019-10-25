package com.vortex.cloud.ums.xm.dataaccess.service.impl;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vortex.cloud.ums.dataaccess.dao.xm.ISysOrgXmDao;
import com.vortex.cloud.ums.dto.IdNameDto;
import com.vortex.cloud.ums.support.ManagementConstant;
import com.vortex.cloud.ums.xm.dataaccess.service.ISysOrgXmService;
import com.vortex.cloud.ums.xm.dto.SysOrgQueryDto;
import com.vortex.cloud.ums.xm.dto.SysOrgXmDto;
import com.vortex.cloud.ums.xm.model.SysOrgXm;
import com.vortex.cloud.vfs.common.exception.ServiceException;
import com.vortex.cloud.vfs.common.exception.VortexException;
import com.vortex.cloud.vfs.common.lang.StringUtil;
import com.vortex.cloud.vfs.data.support.SearchFilter;
import com.vortex.cloud.vfs.data.support.SearchFilter.Operator;

@Service("sysOrgXmService")
public class SysOrgXmServiceImpl implements ISysOrgXmService {
	private static final Logger logger = LoggerFactory.getLogger(SysOrgXmServiceImpl.class);
	@Resource
	private ISysOrgXmDao sysOrgXmDao;

	private void validateForm(SysOrgXmDto dto) {
		if (StringUtils.isBlank(dto.getPid())) {
			throw new ServiceException("上级机构ID为空");
		}
		if (StringUtils.isBlank(dto.getOrg_code())) {
			throw new ServiceException("编码为空");
		}
		if (StringUtils.isBlank(dto.getOrg_cid())) {
			throw new ServiceException("终身码为空");
		}
		if (StringUtils.isBlank(dto.getOrg_name())) {
			throw new ServiceException("名称为空");
		}
	}

	private void validateOnSave(SysOrgXmDto dto) throws Exception {

		this.validateForm(dto);

		// 逻辑业务校验
		if (!dto.getOrg_code().equals(dto.getOrg_cid())) {
			throw new ServiceException("编号与终身码不一致！");
		}
		if (this.isCodeExists(dto.getOrg_cid(), null)) {
			throw new ServiceException("终身码和编码已存在！");
		}
	}

	private void validateOnUpdate(SysOrgXmDto dto) throws Exception {

		this.validateForm(dto);

		// 逻辑业务校验
		if (this.isCodeExists(dto.getOrg_cid(), dto.getId())) {
			throw new ServiceException("终身码已存在！");
		}
		if (this.isCodeExists(dto.getOrg_code(), dto.getId())) {
			throw new ServiceException("编码已存在！");
		}

		if (this.hasChild(dto.getId()) && dto.getHas_child().equals(ManagementConstant.NO)) {
			throw new ServiceException("已存在子节点，不可修改为非节点目录！");
		}
	}

	@Override
	@Transactional
	public void saveOrg(SysOrgXmDto dto) throws Exception {
		// 校验
		this.validateOnSave(dto);
		dto.setId(dto.getOrg_cid());
		// 拷贝对象
		SysOrgXm entity = new SysOrgXm();
		BeanUtils.copyProperties(dto, entity);

		String parentCode = null;
		Integer new_index = 0;
		Integer child_index = 0;
		// level_code 设置
		if (!"-1".equals(entity.getPid())) {
			SysOrgXm parent = sysOrgXmDao.findOne(entity.getPid());
			if (parent != null) {
				if (!StringUtil.isNullOrEmpty(parent.getHas_child()) && "0".equals(parent.getHas_child())) {
					throw new ServiceException("父级为节点目录，不能继续添加子节点");
				}
				parentCode = parent.getLevel_code();
			} else {
				throw new ServiceException("未找到父级");
			}
		}
		List<SearchFilter> filterList = Lists.newArrayList();
		filterList.add(new SearchFilter("pid", Operator.EQ, entity.getPid()));
		List<SysOrgXm> siblingList = sysOrgXmDao.findListByFilter(filterList, null);
		if (CollectionUtils.isNotEmpty(siblingList)) {
			for (SysOrgXm child : siblingList) {
				if (!StringUtil.isNullOrEmpty(parentCode)) {
					child_index = Integer.valueOf(child.getLevel_code().replaceFirst(parentCode, StringUtils.EMPTY));
				} else {
					child_index = Integer.valueOf(child.getLevel_code());
				}
				if (child_index > new_index) {
					new_index = child_index;
				}
			}
		}
		if (StringUtil.isNullOrEmpty(parentCode)) {
			parentCode = "";
		}
		new_index = new_index + 1;
		entity.setLevel_code(parentCode + new DecimalFormat("000").format(new_index));
		sysOrgXmDao.save(entity);
	}

	@Override
	@Transactional
	public void updateOrg(SysOrgXmDto dto) throws Exception {
		// 校验
		this.validateOnUpdate(dto);
		SysOrgXm entity = sysOrgXmDao.findOne(dto.getId());
		// 编码
		entity.setOrg_code(dto.getOrg_code());
		// 名称
		entity.setOrg_name(dto.getOrg_name());
		// 简称
		entity.setAbbr(dto.getAbbr());
		// 是否有子节点 0-无，1-有
		entity.setHas_child(dto.getHas_child());
		// 单位类型 EXTEND12
		entity.setExtend12(dto.getExtend12());
		// 是否代征点 EXTEND8
		entity.setExtend8(dto.getExtend8());
		// 是否网格 EXTEND9
		entity.setExtend9(dto.getExtend9());
		// 是否管辖单位 EXTEND4
		entity.setExtend4(dto.getExtend4());
		entity.setOrder_no(dto.getOrder_no());
		entity.setRemark(dto.getRemark());
		/**
		 * 区
		 */
		entity.setDistrictId(dto.getDistrictId());

		/**
		 * 街道
		 */
		entity.setSubDistrictId(dto.getSubDistrictId());

		/**
		 * 社区居委会
		 */
		entity.setCommunityId(dto.getCommunityId());

		/**
		 * 地址
		 */
		entity.setAddress(dto.getAddress());

		/**
		 * 主要联系人
		 */
		entity.setPrimaryContact(dto.getPrimaryContact());

		/**
		 * 联系人电话
		 */
		entity.setContactPhone(dto.getContactPhone());
		sysOrgXmDao.update(entity);
	}

	@Transactional
	@Override
	public SysOrgXmDto loadOrg(String id) throws Exception {
		SysOrgXmDto returnValue = new SysOrgXmDto();
		SysOrgXm entity = sysOrgXmDao.findOne(id);
		if (null == entity) {
			return null;
		}
		BeanUtils.copyProperties(entity, returnValue);
		if (!StringUtil.isNullOrEmpty(returnValue.getPid())) {
			SysOrgXm parent = sysOrgXmDao.findOne(returnValue.getPid());
			if (null != parent) {
				returnValue.setPname(parent.getOrg_name());
			} else {
				returnValue.setPname("厦门市环卫处");
			}
		}
		return returnValue;
	}

	@Override
	@Transactional
	public void changeParent(String id, String npid) throws Exception {
		if (StringUtil.isNullOrEmpty(id) || StringUtil.isNullOrEmpty(npid)) {
			throw new ServiceException("参数为空！");
		}
		SysOrgXm org = sysOrgXmDao.findOne(id);
		if (null == org) {
			throw new ServiceException("未找到指定组织！");
		}
		if (npid.equals(org.getPid())) {
			throw new ServiceException("不能转移到原父级！");
		}
		List<SearchFilter> searchFilters = Lists.newArrayList();
		List<SysOrgXm> updateList = Lists.newArrayList();
		// 节点老的内置编号
		String old_nodeCode = org.getLevel_code();
		// 节点老的部门表id
		// 节点新的内置编号
		String new_nodeCode = null;
		// 获取新的内置编号
		String replace_code = null;
		if (!"-1".equals(npid)) {
			// 判断新的父级是dept还是org
			SysOrgXm new_org_parent = sysOrgXmDao.findOne(npid);
			if (null == new_org_parent) {
				throw new ServiceException("未找到指定父级！");
			}
			if (!StringUtil.isNullOrEmpty(new_org_parent.getHas_child()) && "0".equals(new_org_parent.getHas_child())) {
				throw new ServiceException("新父级为节点目录，不能继续添加子节点");
			}

			// 同部门，并且2者有层级关系，不可以
			if (new_org_parent.getLevel_code().startsWith(old_nodeCode)) {
				throw new ServiceException("同部门，并且新父级属于此节点，异常！");
			}
			replace_code = new_org_parent.getLevel_code();
		}
		// 节点原父级的 子层所有数据记录数 不修改,避免冲突
		searchFilters.add(new SearchFilter("pid", Operator.EQ, npid));
		List<SysOrgXm> parent_childs = sysOrgXmDao.findListByFilter(searchFilters, null);
		Integer new_index = 0;
		Integer child_index = 0;

		if (CollectionUtils.isNotEmpty(parent_childs)) {// 新父级下有子级
			for (SysOrgXm child : parent_childs) {
				if (!StringUtil.isNullOrEmpty(replace_code)) {
					child_index = Integer.valueOf(child.getLevel_code().replaceFirst(replace_code, StringUtils.EMPTY));
				} else {
					child_index = Integer.valueOf(child.getLevel_code());
				}
				if (child_index > new_index) {
					new_index = child_index;
				}
			}
		}
		if (StringUtil.isNullOrEmpty(replace_code)) {
			replace_code = "";
		}
		new_nodeCode = replace_code + new DecimalFormat("000").format(new_index + 1);
		org.setPid(npid);
		org.setLevel_code(new_nodeCode);
		updateList.add(org);

		searchFilters.clear();
		searchFilters.add(new SearchFilter("level_code", Operator.RLIKE, old_nodeCode));
		List<SysOrgXm> old_childs = sysOrgXmDao.findListByFilter(searchFilters, null);
		if (CollectionUtils.isNotEmpty(old_childs)) {// 更新节点所有子节点的内置编号,部门表id
			for (SysOrgXm child : old_childs) {
				if (child.getId().equals(org.getId())) {
					continue;
				}
				child.setLevel_code(child.getLevel_code().replaceFirst(old_nodeCode, new_nodeCode));
				updateList.add(child);
			}
		}
		if (CollectionUtils.isNotEmpty(updateList)) {// 更新数据
			sysOrgXmDao.update(updateList);
		}
	}

	@Override
	@Transactional
	public boolean isCodeExists(String code, String id) throws Exception {
		// if (StringUtil.isNullOrEmpty(id)) {
		// SearchFilters orFilters = new
		// SearchFilters(com.vortex.framework.core.orm.SearchFilters.Operator.OR);
		// orFilters.add(new SearchFilter("org_cid", Operator.EQ, code));
		// orFilters.add(new SearchFilter("org_code", Operator.EQ, code));
		// orFilters.add(new SearchFilter("id", Operator.EQ, code));
		// if
		// (CollectionUtils.isNotEmpty(sysOrgXmDao.findListByFilters(orFilters,
		// null))) {
		// return true;
		// }
		// } else {
		// SearchFilters andFilters = new
		// SearchFilters(com.vortex.framework.core.orm.SearchFilters.Operator.AND);
		// SearchFilters orFilters = new
		// SearchFilters(com.vortex.framework.core.orm.SearchFilters.Operator.OR);
		// orFilters.add(new SearchFilter("org_cid", Operator.EQ, code));
		// orFilters.add(new SearchFilter("org_code", Operator.EQ, code));
		// orFilters.add(new SearchFilter("id", Operator.EQ, code));
		//
		// andFilters.add(orFilters);
		// andFilters.add(new SearchFilter("id", Operator.NE, id));
		// if
		// (CollectionUtils.isNotEmpty(sysOrgXmDao.findListByFilters(andFilters,
		// null))) {
		// return true;
		// }
		// }
		// return false;
		return this.sysOrgXmDao.isCodeExists(code, id);
	}

	@Override
	@Transactional
	public void deleteOrgs(List<String> ids) throws Exception {
		if (CollectionUtils.isEmpty(ids)) {
			return;
		}
		for (String id : ids) {
			this.deleteNotWithDescendant(id);
		}
	}

	/**
	 * 只删除自身，不级联删除后代节点
	 * 
	 * @param ids
	 * @return
	 */
	private void deleteNotWithDescendant(String id) throws Exception {
		List<SearchFilter> filterList = Lists.newArrayList();
		filterList.add(new SearchFilter("pid", Operator.EQ, id));
		List<SysOrgXm> childList = sysOrgXmDao.findListByFilter(filterList, null);
		if (CollectionUtils.isNotEmpty(childList)) {
			logger.error("id=" + id + "的部门存在子节点，不能删除");
			throw new VortexException("id=" + id + "的部门存在子节点，不能删除");
		}
		sysOrgXmDao.delete(id);
	}

	@Override
	public Page<SysOrgXmDto> queryOrgInfo(SysOrgQueryDto filter) throws Exception {
		// List<SysOrgXmDto> list = Lists.newArrayList();
		// SysOrgXmDto dto = null;
		// if (null == filter || null == filter.getPageNum() || null ==
		// filter.getPageSize()) {
		// return new PageImpl<>(list, null, 0);
		// }
		// Pageable pageable = new PageRequest(filter.getPageNum(),
		// filter.getPageSize(), Direction.DESC, "createTime");
		// List<SearchFilter> searchFilters = Lists.newArrayList();
		// if (!StringUtil.isNullOrEmpty(filter.getName())) {
		// searchFilters.add(new SearchFilter("org_name", Operator.LIKE,
		// filter.getName()));
		// }
		// if (!StringUtil.isNullOrEmpty(filter.getCode())) {
		// searchFilters.add(new SearchFilter("org_code", Operator.LIKE,
		// filter.getCode()));
		// }
		// if (!StringUtil.isNullOrEmpty(filter.getParentId())) {
		// searchFilters.add(new SearchFilter("pid", Operator.EQ,
		// filter.getParentId()));
		// }
		// Page<SysOrgXm> pageList = sysOrgXmDao.findPageByFilter(pageable,
		// searchFilters);
		// if (CollectionUtils.isNotEmpty(pageList.getContent())) {
		// List<String> pidList = Lists.newArrayList();
		// for (SysOrgXm entity : pageList) {
		// if (StringUtil.isNullOrEmpty(entity.getPid())) {
		// continue;
		// }
		// if (pidList.contains(entity.getPid())) {
		// continue;
		// }
		// pidList.add(entity.getPid());
		// }
		// Map<String, SysOrgXm> parentMap = Maps.newHashMap();
		// if (CollectionUtils.isNotEmpty(pidList)) {
		// searchFilters.clear();
		// searchFilters.add(new SearchFilter("id", Operator.IN, (String[])
		// pidList.toArray(new String[pidList.size()])));
		// List<SysOrgXm> parentList =
		// sysOrgXmDao.findListByFilter(searchFilters,
		// null);
		// if (CollectionUtils.isNotEmpty(parentList)) {
		// for (SysOrgXm sysOrgXm : parentList) {
		// parentMap.put(sysOrgXm.getId(), sysOrgXm);
		// }
		// }
		// }
		// for (SysOrgXm entity : pageList) {
		// dto = new SysOrgXmDto();
		// BeanUtils.copyProperties(entity, dto);
		// if (MapUtils.isNotEmpty(parentMap)) {
		// if (!StringUtil.isNullOrEmpty(dto.getPid()) &&
		// parentMap.containsKey(dto.getPid())) {
		// dto.setPname(parentMap.get(dto.getPid()).getOrg_name());
		// }
		// }
		// list.add(dto);
		// }
		// }
		// return new PageImpl<>(list, pageable, pageList.getTotalElements());
		return this.sysOrgXmDao.queryOrgInfo(filter);
	}

	@Override
	public List<SysOrgXmDto> listByType(String orgType) throws Exception {
		return this.sysOrgXmDao.listByType(orgType);
	}

	@Override
	public LinkedHashMap<String, String> getNamesByIds(List<String> ids) throws Exception {
		List<IdNameDto> list = this.sysOrgXmDao.getNamesByIds(ids);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}

		// 转化成键值对
		LinkedHashMap<String, String> rst = Maps.newLinkedHashMap();
		for (int i = 0; i < list.size(); i++) {
			rst.put(list.get(i).getId(), list.get(i).getName());
		}

		return rst;
	}

	@Override
	public LinkedHashMap<String, String> getIdsByNames(List<String> names) throws Exception {
		List<IdNameDto> list = this.sysOrgXmDao.getIdsByNames(names);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}

		// 转化成键值对
		LinkedHashMap<String, String> rst = Maps.newLinkedHashMap();
		for (int i = 0; i < list.size(); i++) {
			rst.put(list.get(i).getName(), list.get(i).getId());
		}

		return rst;
	}

	@Override
	public boolean hasChild(String id) throws Exception {
		return this.sysOrgXmDao.hasChild(id);
	}

	@Override
	public Object getAllSysOrg() {
		List<SysOrgXmDto> orgs = sysOrgXmDao.listAll();
		return orgs;
	}
}
