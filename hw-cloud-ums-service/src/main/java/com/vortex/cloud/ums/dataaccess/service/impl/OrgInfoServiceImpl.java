package com.vortex.cloud.ums.dataaccess.service.impl;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vortex.cloud.ums.dataaccess.dao.ICloudOrgInfoDao;
import com.vortex.cloud.ums.dataaccess.dao.ICloudOrganizationDao;
import com.vortex.cloud.ums.dataaccess.service.ICloudDepartmentService;
import com.vortex.cloud.ums.dataaccess.service.ICloudOrganizationService;
import com.vortex.cloud.ums.dataaccess.service.IOrgInfoService;
import com.vortex.cloud.ums.dto.IdNameDto;
import com.vortex.cloud.ums.dto.OrgInfoDto;
import com.vortex.cloud.ums.dto.OrgInfoQueryDto;
import com.vortex.cloud.ums.enums.OrgTypeEnum;
import com.vortex.cloud.ums.model.CloudDepartment;
import com.vortex.cloud.ums.model.CloudOrgInfo;
import com.vortex.cloud.ums.model.CloudOrganization;
import com.vortex.cloud.vfs.common.exception.ServiceException;
import com.vortex.cloud.vfs.common.exception.VortexException;
import com.vortex.cloud.vfs.common.lang.StringUtil;
import com.vortex.cloud.vfs.data.support.SearchFilter;
import com.vortex.cloud.vfs.data.support.SearchFilter.Operator;

@Transactional
@Service("orgInfoService")
public class OrgInfoServiceImpl implements IOrgInfoService {
	private static final Logger logger = LoggerFactory.getLogger(OrgInfoServiceImpl.class);
	@Resource
	private ICloudDepartmentService cloudDepartmentService;
	@Resource
	private ICloudOrganizationService cloudOrganizationService;
	@Resource
	private ICloudOrgInfoDao cloudOrgInfoDao;
	@Resource
	private ICloudOrganizationDao cloudOrganizationDao;

	private static final String FLAG_DEPT = "1";
	private static final String FLAG_ORG = "2";
	private static final Integer YEAR_END = 2999;

	@Override
	public void saveDeptInfo(OrgInfoDto info) throws Exception {
		// 入参校验
		String checkInfo = this.checkInfoWhenAdd(info);
		if (StringUtils.isNotEmpty(checkInfo)) {
			logger.error(checkInfo);
			throw new VortexException(checkInfo);
		}

		// 保存department表
		CloudDepartment dt = new CloudDepartment();
		dt.setId(info.getCode());
		dt.setTenantId(info.getTenantId());
		dt.setDepName(info.getName());
		dt.setDepCode(info.getCode());
		dt.setDepType(null);
		dt.setHead(info.getHead());
		dt.setHeadMobile(info.getHeadMobile());
		dt.setDescription(info.getDescription());
		dt.setLngLats(info.getLngLats());
		dt.setAddress(info.getAddress());
		dt.setEmail(info.getEmail());
		dt.setOrderIndex(info.getOrderIndex());
		cloudDepartmentService.save(dt);

		// 保存外挂信息表
		CloudOrgInfo oi = new CloudOrgInfo();
		oi.setOrgId(dt.getId()); // dept或org表id
		oi.setOrgCid(dt.getDepCode()); // 单位终生码
		oi.setAbbr(info.getAbbr()); // 单位简称
		oi.setBeginYear(info.getBeginYear()); // 启用年
		oi.setEndYear(YEAR_END); // 结束年
		oi.setOrgType(info.getOrgType()); // 单位类型 EXTEND12
		oi.setIsDzd(info.getIsDzd()); // 是否代征点 EXTEND8
		oi.setIsReseau(info.getIsReseau()); // 是否网格 EXTEND9
		oi.setIsMgr(info.getIsMgr()); // 是否管辖单位 EXTEND4
		oi.setOrgState(CloudOrgInfo.STATE_YES); // 是否启用,默认1启用
		cloudOrgInfoDao.save(oi);

		// TODO 同步保存租户的行政区划表
	}

	/**
	 * 新增时校验数据
	 * 
	 * @param info
	 * @return
	 * @throws Exception
	 */
	private String checkInfoWhenAdd(OrgInfoDto info) throws Exception {
		if (info == null) {
			return "入参为空";
		}
		if (StringUtils.isEmpty(info.getTenantId())) {
			return "租户id不得为空";
		}
		if (StringUtils.isEmpty(info.getFlag())) {
			return "标志位不能为空";
		}
		if (StringUtils.isEmpty(info.getName())) {
			return "名称不得为空";
		}
		if (StringUtils.isEmpty(info.getCode())) {
			return "代码不得为空";
		}
		if (StringUtils.isEmpty(info.getAbbr())) {
			return "简称不得为空";
		}
		if (StringUtils.isEmpty(info.getIsDzd())) {
			return "是否代征点不得为空";
		}
		if (StringUtils.isEmpty(info.getOrgCid())) {
			return "终生码不得为空";
		}
		if (info.getBeginYear() == null) {
			return "启用年份不得为空";
		}

		if (this.isCodeExists(info.getCode(), info.getTenantId(), info.getFlag(), info.getOrgId())) {
			return "代码重复";
		}
		return null;
	}

	/**
	 * 更新时校验数据
	 * 
	 * @param info
	 * @return
	 * @throws Exception
	 */
	private String checkInfoWhenUpdate(OrgInfoDto info) throws Exception {
		String err = this.checkInfoWhenAdd(info);
		if (StringUtils.isNotEmpty(err)) {
			return err;
		}

		if (StringUtils.isEmpty(info.getOrgId())) {
			return "更新时机构id不得为空";
		}

		return null;
	}

	@Override
	public void saveOrgInfo(OrgInfoDto info) throws Exception {
		// 入参校验
		String checkInfo = this.checkInfoWhenAdd(info);
		if (StringUtils.isNotEmpty(checkInfo)) {
			logger.error(checkInfo);
			throw new VortexException(checkInfo);
		}

		// 处理deptid字段。如果父节点对应的机构也是org表，则直接去父节点的deptid字段；如果父节点对应的表是dept，则直接取父节点id；否则抛错
		String departmentId = null;
		String parentFlag = null; // 父节点类型
		CloudOrganization parent = cloudOrganizationService.findOne(info.getParentId());
		if (parent != null) {
			departmentId = parent.getDepartmentId();
			parentFlag = FLAG_ORG;
		} else {
			CloudDepartment dp = cloudDepartmentService.findOne(info.getParentId());
			if (dp != null) {
				parentFlag = FLAG_DEPT;
				departmentId = info.getParentId();
			} else {
				logger.error("未找到dept表信息");
				throw new VortexException("未找到dept表信息");
			}
		}

		// 处理nodecode树形字段
		String nodeCode = null;
		if (parentFlag.equals(FLAG_DEPT)) { // 如果是第一层org，则直接返回该租户下第一层org的nodecode的最大值+1
			nodeCode = cloudOrganizationDao.getNextNodeCodeByFirstLevel(info.getTenantId());
		} else if (parentFlag.equals(FLAG_ORG)) { // 如果不是第一层
			String max = cloudOrganizationDao.getMaxNodeCodeByParentId("cloud_organization", "parentId", info.getParentId());
			if (StringUtils.isEmpty(max)) {
				nodeCode = parent.getNodeCode() + "01";
			} else {
				String str = (new Integer(max) + 101) + "";
				nodeCode = parent.getNodeCode() + str.substring(str.length() - 2, str.length());
			}
		}

		// 保存org主表
		CloudOrganization org = new CloudOrganization();
		org.setId(info.getCode());
		org.setTenantId(info.getTenantId()); // 租户id
		org.setOrgName(info.getName()); // 机构名称
		org.setOrgCode(info.getCode()); // 机构代码
		org.setDepartmentId(departmentId); // 部门表id
		org.setParentId(info.getParentId()); // 上级id。对应第一层机构，值为departmentId
		org.setHead(info.getHead()); // 负责人
		org.setHeadMobile(info.getHeadMobile()); // 负责人电话
		org.setDescription(info.getDescription()); // 描述
		org.setLngLats(info.getLngLats()); // 经纬度
		org.setAddress(info.getAddress()); // 地址
		org.setEmail(info.getEmail()); // 邮箱
		org.setNodeCode(nodeCode); // 内置编号：用于层级数据结构的构造（如树）
		org.setChildSerialNumer(0); // 子层所有数据记录数，和己编号配置生成子编号
		org.setOrderIndex(info.getOrderIndex()); // 排序号
		this.cloudOrganizationService.save(org);

		// 保存外挂信息表
		CloudOrgInfo oi = new CloudOrgInfo();
		oi.setOrgId(info.getCode()); // dept或org表id
		oi.setOrgCid(info.getCode()); // 单位终生码
		oi.setAbbr(info.getAbbr()); // 单位简称
		oi.setBeginYear(info.getBeginYear()); // 启用年
		oi.setEndYear(YEAR_END); // 结束年
		oi.setOrgType(info.getOrgType()); // 单位类型 EXTEND12
		oi.setIsDzd(info.getIsDzd()); // 是否代征点 EXTEND8
		oi.setIsReseau(info.getIsReseau()); // 是否网格 EXTEND9
		oi.setIsMgr(info.getIsMgr()); // 是否管辖单位 EXTEND4
		oi.setOrgState(CloudOrgInfo.STATE_YES); // 是否启用,默认1启用
		cloudOrgInfoDao.save(oi);

		// TODO 同步保存租户的行政区划表
	}

	@Override
	public void updateDeptInfo(OrgInfoDto info) throws Exception {
		// 入参校验
		String checkInfo = this.checkInfoWhenUpdate(info);
		if (StringUtils.isNotEmpty(checkInfo)) {
			logger.error(checkInfo);
			throw new VortexException(checkInfo);
		}

		CloudDepartment dt = this.cloudDepartmentService.findOne(info.getOrgId());
		if (dt == null) {
			logger.error("根据id[" + info.getOrgId() + "]未找到机构信息");
			throw new VortexException("根据id[" + info.getOrgId() + "]未找到机构信息");
		}

		CloudOrgInfo oi = this.cloudOrgInfoDao.findByOrgId(info.getOrgId());
		if (oi == null) {
			logger.error("根据id[" + info.getOrgId() + "]未找到机构附属信息");
			throw new VortexException("根据id[" + info.getOrgId() + "]未找到机构附属信息");
		}

		dt.setDepName(info.getName());
		dt.setDepCode(info.getCode());
		dt.setDepType(null);
		dt.setHead(info.getHead());
		dt.setHeadMobile(info.getHeadMobile());
		dt.setDescription(info.getDescription());
		dt.setLngLats(info.getLngLats());
		dt.setAddress(info.getAddress());
		dt.setEmail(info.getEmail());
		dt.setOrderIndex(info.getOrderIndex());
		cloudDepartmentService.update(dt);

		oi.setAbbr(info.getAbbr()); // 单位简称
		oi.setBeginYear(info.getBeginYear()); // 启用年
		oi.setOrgType(info.getOrgType()); // 单位类型 EXTEND12
		oi.setIsDzd(info.getIsDzd()); // 是否代征点 EXTEND8
		oi.setIsReseau(info.getIsReseau()); // 是否网格 EXTEND9
		oi.setIsMgr(info.getIsMgr()); // 是否管辖单位 EXTEND4
		cloudOrgInfoDao.update(oi);
	}

	@Override
	public void updateOrgInfo(OrgInfoDto info) throws Exception {
		// 入参校验
		String checkInfo = this.checkInfoWhenUpdate(info);
		if (StringUtils.isNotEmpty(checkInfo)) {
			logger.error(checkInfo);
			throw new VortexException(checkInfo);
		}

		CloudOrganization org = this.cloudOrganizationService.findOne(info.getOrgId());
		if (org == null) {
			logger.error("根据id[" + info.getOrgId() + "]未找到机构信息");
			throw new VortexException("根据id[" + info.getOrgId() + "]未找到机构信息");
		}

		CloudOrgInfo oi = this.cloudOrgInfoDao.findByOrgId(info.getOrgId());
		if (oi == null) {
			logger.error("根据id[" + info.getOrgId() + "]未找到机构附属信息");
			throw new VortexException("根据id[" + info.getOrgId() + "]未找到机构附属信息");
		}

		org.setOrgName(info.getName()); // 机构名称
		org.setOrgCode(info.getCode()); // 机构代码
		org.setHead(info.getHead()); // 负责人
		org.setHeadMobile(info.getHeadMobile()); // 负责人电话
		org.setDescription(info.getDescription()); // 描述
		org.setLngLats(info.getLngLats()); // 经纬度
		org.setAddress(info.getAddress()); // 地址
		org.setEmail(info.getEmail()); // 邮箱
		org.setOrderIndex(info.getOrderIndex()); // 排序号
		this.cloudOrganizationService.update(org);

		// 保存外挂信息表
		oi.setAbbr(info.getAbbr()); // 单位简称
		oi.setBeginYear(info.getBeginYear()); // 启用年
		oi.setOrgType(info.getOrgType()); // 单位类型 EXTEND12
		oi.setIsDzd(info.getIsDzd()); // 是否代征点 EXTEND8
		oi.setIsReseau(info.getIsReseau()); // 是否网格 EXTEND9
		oi.setIsMgr(info.getIsMgr()); // 是否管辖单位 EXTEND4
		cloudOrgInfoDao.update(oi);
	}

	@Override
	public void deleteOrgs(List<String> ids) throws Exception {
		if (CollectionUtils.isEmpty(ids)) {
			logger.error("删除机构时，请传入机构id");
			throw new VortexException("删除机构时，请传入机构id");
		}

		CloudOrganization org = null;
		CloudDepartment dept = null;
		CloudOrgInfo info = null;
		for (int i = 0; i < ids.size(); i++) {
			org = cloudOrganizationService.findOne(ids.get(i));
			dept = cloudDepartmentService.findOne(ids.get(i));
			info = cloudOrgInfoDao.findByOrgId(ids.get(i));
			if (org != null) {
				cloudOrganizationService.delete(org);
			}
			if (dept != null) {
				cloudDepartmentService.delete(dept);
			}
			if (info != null) {
				cloudOrgInfoDao.delete(info);
			}
		}
	}

	@Override
	public void changeParent(String id, String npid) throws Exception {
		if (StringUtil.isNullOrEmpty(id) || StringUtil.isNullOrEmpty(npid)) {
			throw new ServiceException("参数为空！");
		}
		CloudOrganization org = cloudOrganizationService.findOne(id);
		if (null == org) {
			throw new ServiceException("未找到指定组织！");
		}
		if (npid.equals(org.getParentId())) {
			throw new ServiceException("不能转移到原父级！");
		}
		List<SearchFilter> searchFilters = Lists.newArrayList();
		List<CloudOrganization> updateList = Lists.newArrayList();
		// 节点老的内置编号
		String old_nodeCode = org.getNodeCode();
		// 节点老的部门表id
		String old_deptId = org.getDepartmentId();
		// 节点新的内置编号
		String new_nodeCode = null;
		// 节点新的部门表id
		String new_deptId = null;
		// 判断新的父级是dept还是org
		CloudOrganization new_org_parent = cloudOrganizationService.findOne(npid);
		CloudDepartment new_dept_parent = null;
		if (null == new_org_parent) {
			new_dept_parent = cloudDepartmentService.findOne(npid);
			if (null == new_dept_parent) {
				throw new ServiceException("未找到指定父级！");
			}
		}

		// 获取新的部门表id
		if (null != new_org_parent) {// 新父级为org
			// 节点新的部门表id 为新父级的 部门表id
			new_deptId = new_org_parent.getDepartmentId();
			if (new_deptId.equals(old_deptId)) {// 同部门，并且2者有层级关系，不可以
				if (new_org_parent.getNodeCode().startsWith(old_nodeCode)) {
					throw new ServiceException("同部门，并且新父级属于此节点，异常！");
				}
			}

			// 新父级的子层所有数据记录数 +1,节点原父级的 子层所有数据记录数 不修改,避免冲突
			new_org_parent.setChildSerialNumer(new_org_parent.getChildSerialNumer() + 1);
			updateList.add(new_org_parent);
		} else {// 新父级为dept
			// 节点新的部门表id 为新父级的 id
			new_deptId = new_dept_parent.getId();
		}
		searchFilters.add(new SearchFilter("parentId", Operator.EQ, npid));
		List<CloudOrganization> parent_childs = cloudOrganizationService.findListByFilter(searchFilters, null);
		Integer new_index = 0;
		Integer child_index = 0;
		String replace_code = null;
		// 获取新的内置编号
		if (null != new_org_parent) {// 新父级为org
			replace_code = new_org_parent.getNodeCode();
		}
		if (CollectionUtils.isNotEmpty(parent_childs)) {// 新父级下有子级
			for (CloudOrganization child : parent_childs) {
				if (!StringUtil.isNullOrEmpty(replace_code)) {
					child_index = Integer.valueOf(child.getNodeCode().replaceFirst(replace_code, StringUtils.EMPTY));
				} else {
					child_index = Integer.valueOf(child.getNodeCode());

				}
				if (child_index > new_index) {
					new_index = child_index;
				}
			}
			new_nodeCode = replace_code + new DecimalFormat("00").format(new_index + 1);
		} else {
			new_nodeCode = replace_code + "01";
		}

		org.setParentId(npid);
		org.setDepartmentId(new_deptId);
		org.setNodeCode(new_nodeCode);
		updateList.add(org);

		searchFilters.clear();
		searchFilters.add(new SearchFilter("nodeCode", Operator.RLIKE, old_nodeCode));
		searchFilters.add(new SearchFilter("departmentId", Operator.EQ, old_deptId));
		List<CloudOrganization> old_childs = cloudOrganizationService.findListByFilter(searchFilters, null);
		if (CollectionUtils.isNotEmpty(old_childs)) {// 更新节点所有子节点的内置编号,部门表id
			for (CloudOrganization child : old_childs) {
				if (child.getId().equals(org.getId())) {
					continue;
				}
				child.setNodeCode(child.getNodeCode().replaceFirst(old_nodeCode, new_nodeCode));
				child.setDepartmentId(new_deptId);
				updateList.add(child);
			}
		}
		if (CollectionUtils.isNotEmpty(updateList)) {// 更新数据
			cloudOrganizationService.update(updateList);
		}
	}

	@Override
	public boolean isCodeExists(String code, String tenantId, String flag, String id) throws Exception {
		boolean rst = false;
		if (flag.equals(FLAG_DEPT) && StringUtils.isEmpty(id)) {
			rst = this.cloudDepartmentService.isCodeExisted(tenantId, code);
		} else if (flag.equals(FLAG_DEPT) && StringUtils.isNotEmpty(id)) {
			rst = !this.cloudDepartmentService.validateCodeOnUpdate(tenantId, id, code);
		} else if (flag.equals(FLAG_ORG) && StringUtils.isEmpty(id)) {
			rst = this.cloudOrganizationService.isCodeExisted(tenantId, code);
		} else if (flag.equals(FLAG_ORG) && StringUtils.isNotEmpty(id)) {
			rst = !this.cloudOrganizationService.validateCodeOnUpdate(tenantId, id, code);
		}
		return rst;
	}

	@Override
	public OrgInfoDto loadDeptInfo(String id) throws Exception {
		return this.cloudOrgInfoDao.loadDeptInfo(id);
	}

	@Override
	public OrgInfoDto loadOrgInfo(String id) throws Exception {
		OrgInfoDto rst = this.cloudOrgInfoDao.loadOrgInfo(id);
		// 处理parentName
		CloudOrganization org = this.cloudOrganizationDao.findOne(rst.getParentId());
		if (org != null) {
			rst.setParentName(org.getOrgName());
		} else {
			CloudDepartment dep = this.cloudDepartmentService.findOne(rst.getParentId());
			if (dep != null) {
				rst.setParentName(dep.getDepName());
			}
		}
		return rst;
	}

	@Override
	public Page<OrgInfoDto> queryOrgInfo(OrgInfoQueryDto dto) throws Exception {
		return this.cloudOrgInfoDao.queryOrgInfo(dto);
	}

	@Override
	public Map<String, String> getOrgTypeEnums() {
		OrgTypeEnum[] arr = OrgTypeEnum.values();
		Map<String, String> rst = Maps.newLinkedHashMap();
		for (int i = 0; i < arr.length; i++) {
			rst.put(arr[i].getKey(), arr[i].getValue());
		}
		return rst;
	}

	@Override
	public LinkedHashMap<String, String> getOrgsByType(String tenantId, String parentId, String orgType) throws Exception {
		List<IdNameDto> list = this.cloudOrgInfoDao.getOrgsByType(tenantId, parentId, orgType);
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
	public LinkedHashMap<String, String> getOrgsByNames(String tenantId, List<String> names) throws Exception {
		List<IdNameDto> list = this.cloudOrgInfoDao.getOrgsByNames(tenantId, names);
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
}
