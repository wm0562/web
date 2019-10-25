package com.vortex.cloud.ums.dataaccess.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.vortex.cloud.ums.dataaccess.dao.ICloudThirdPartyAppDao;
import com.vortex.cloud.ums.dto.UmsLoginReturnInfoDto;
import com.vortex.cloud.ums.model.CloudThirdPartyApp;
import com.vortex.cloud.vfs.common.lang.StringUtil;
import com.vortex.cloud.vfs.data.hibernate.repository.SimpleHibernateRepository;
import com.vortex.cloud.vfs.data.support.SearchFilter;
import com.vortex.cloud.vfs.data.support.SearchFilter.Operator;

@Repository("cloudThirdPartyAppDao")
public class CloudThirdPartyAppDaoImpl extends SimpleHibernateRepository<CloudThirdPartyApp, String>
		implements ICloudThirdPartyAppDao {

	@Override
	public DetachedCriteria getDetachedCriteria() {
		return defaultCriteria();
	}

	private DetachedCriteria defaultCriteria() {
		DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass(), "cloudThirdPartyApp");
		criteria.add(Restrictions.eq("beenDeleted", 0));
		return criteria;
	}

	@Override
	public List<UmsLoginReturnInfoDto> getLoginInfo(String appKey) {
		List<UmsLoginReturnInfoDto> returnValue = Lists.newArrayList();
		if (StringUtil.isNullOrEmpty(appKey)) {
			return returnValue;
		}
		List<SearchFilter> searchFilters = Lists.newArrayList();
		searchFilters.add(new SearchFilter("appKey", Operator.EQ, appKey));
		List<CloudThirdPartyApp> list = this.findListByFilter(searchFilters, null);
		if (CollectionUtils.isEmpty(list)) {
			return returnValue;
		}
		Date nowTime = new Date();
		UmsLoginReturnInfoDto dto = null;
		for (CloudThirdPartyApp entity : list) {
			// 判断有效时间范围
			if (null == entity.getDateFrom() || null == entity.getDateTo()) {
				continue;
			}
			if (nowTime.getTime() - entity.getDateFrom().getTime() < 0
					|| nowTime.getTime() - entity.getDateTo().getTime() > 0) {// 不在有效时间范围内
				continue;
			}
			dto = new UmsLoginReturnInfoDto();
			dto.setUserId(entity.getId());
			dto.setUserName(entity.getAppKey());
			dto.setName(entity.getAppKey());
			dto.setPassword(entity.getAppSecret());
			dto.setTenantId(entity.getTenantId());
			dto.setPeriod(entity.getPeriod());
			dto.setPeriodCount(entity.getPeriodCount());
			dto.setWhiteList(entity.getWhiteList());
			returnValue.add(dto);
		}
		return returnValue;
	}

}
