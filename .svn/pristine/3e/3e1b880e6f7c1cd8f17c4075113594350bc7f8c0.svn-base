package com.vortex.cloud.ums.dataaccess.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.vortex.cloud.ums.dataaccess.dao.ICloudTenantRelationDao;
import com.vortex.cloud.ums.dataaccess.dao.ITenantDao;
import com.vortex.cloud.ums.dataaccess.service.ICloudTenantRelationService;
import com.vortex.cloud.ums.dto.TenantDto;
import com.vortex.cloud.ums.dto.tenantgroup.CloudTenantRelationDto;
import com.vortex.cloud.ums.dto.tenantgroup.TenantInfoDto;
import com.vortex.cloud.ums.model.CloudTenantRelation;
import com.vortex.cloud.ums.model.Tenant;
import com.vortex.cloud.vfs.common.exception.VortexException;

@Transactional
@Service("cloudTenantRelationService")
public class CloudTenantRelationServiceImpl implements ICloudTenantRelationService {
	Logger logger = LoggerFactory.getLogger(CloudTenantRelationServiceImpl.class);
	@Resource
	private ICloudTenantRelationDao cloudTenantRelationDao;
	@Resource
	private ITenantDao tenantDao;

	@Override
	public List<TenantInfoDto> listExceptViceTenant() throws Exception {
		return cloudTenantRelationDao.listExceptViceTenant();
	}

	@Override
	public List<TenantInfoDto> listViceTenant(String mainTenantId) throws Exception {
		return cloudTenantRelationDao.listViceTenant(mainTenantId);
	}

	@Override
	public void bandingRelation(String mainTenantId, List<String> viceTenantIds) throws Exception {
		if (StringUtils.isEmpty(mainTenantId)) {
			logger.error("绑定租户关系时未传入租户id");
			throw new VortexException("绑定租户关系时未传入租户id");
		}

		// 删除关联关系
		this.cloudTenantRelationDao.deleteRelation(mainTenantId);

		// 添加新关系
		if (CollectionUtils.isNotEmpty(viceTenantIds)) {
			List<CloudTenantRelation> list = Lists.newArrayList();
			for (String vid : viceTenantIds) {
				if (vid.equals(mainTenantId)) {
					continue;
				}
				CloudTenantRelation tr = new CloudTenantRelation();
				tr.setMainTenantId(mainTenantId);
				tr.setViceTenantId(vid);
				list.add(tr);
			}

			this.cloudTenantRelationDao.save(list);
		}
	}

	@Override
	public List<TenantDto> getGroupTenantList(String id) throws Exception {
		if (StringUtils.isEmpty(id)) {
			return null;
		}

		List<TenantDto> rst = null;

		CloudTenantRelationDto dto = this.cloudTenantRelationDao.getRelationByTenantId(id);

		// 如果没有在集团关系中找到该租户，直接返回该租户
		if (dto == null) {
			return this.getMainTenant(id);
		}

		// 如果该租户在集团关系中出现
		if (id.equals(dto.getMainTenantId())) { // 如果是集团公司
			// 主租户放入第一位
			rst = this.getMainTenant(id);
			List<TenantDto> viceList = this.cloudTenantRelationDao.listViceTenantDto(id);
			if (CollectionUtils.isNotEmpty(viceList)) {
				rst.addAll(viceList);
			}
		} else { // 如果是子公司
			// 主租户放入第一位
			rst = this.getMainTenant(dto.getMainTenantId());
			List<TenantDto> viceList = this.cloudTenantRelationDao.listViceTenantDto(dto.getMainTenantId());
			if (CollectionUtils.isNotEmpty(viceList)) {
				rst.addAll(viceList);
			}
		}

		return rst;
	}

	private List<TenantDto> getMainTenant(String id) {
		List<TenantDto> rst = Lists.newArrayList();
		Tenant tenant = this.tenantDao.findOne(id);
		if (tenant != null) {
			rst = Lists.newArrayList();
			TenantDto td = new TenantDto();
			BeanUtils.copyProperties(tenant, td);
			td.setParentId("-1");
			rst.add(td);
			return rst;
		} else {
			return null;
		}
	}

	@Override
	public List<TenantDto> listViceTenantDto(String mainTenantId) throws Exception {
		return this.cloudTenantRelationDao.listViceTenantDto(mainTenantId);
	}

	@Override
	public TenantDto getById(String id) throws Exception {
		if (StringUtils.isEmpty(id)) {
			return null;
		}

		Tenant tenant = this.tenantDao.findOne(id);
		if (tenant == null) {
			logger.error("根据id未找到该租户");
			throw new VortexException("根据id未找到该租户");
		}

		TenantDto rst = new TenantDto();
		BeanUtils.copyProperties(tenant, rst);

		CloudTenantRelationDto rd = this.cloudTenantRelationDao.getRelationByTenantId(id);
		if (rd == null) { // 租户不在集团关系表
			rst.setParentId(null);
		} else {
			if (id.equals(rd.getMainTenantId())) { // 如果是集团，父节点返回-1
				rst.setParentId("-1");
			} else {
				rst.setParentId(rd.getMainTenantId());
			}
		}

		return rst;
	}

	@Override
	public TenantDto getParentTenantById(String id) throws Exception {
		if (StringUtils.isEmpty(id)) {
			return null;
		}

		CloudTenantRelationDto rd = this.cloudTenantRelationDao.getRelationByTenantId(id);

		if (rd == null || id.equals(rd.getMainTenantId())) {
			return null;
		}

		Tenant tenant = this.tenantDao.findOne(rd.getMainTenantId());
		TenantDto td = new TenantDto();
		BeanUtils.copyProperties(tenant, td);

		return td;
	}
}
