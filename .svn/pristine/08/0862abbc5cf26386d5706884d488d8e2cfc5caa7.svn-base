package com.vortex.cloud.ums.dataaccess.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vortex.cloud.ums.dataaccess.dao.ICloudUserFingerprintDao;
import com.vortex.cloud.ums.dataaccess.service.ICloudUserFingerprintService;
import com.vortex.cloud.ums.dto.CloudUserFingerprintDto;
import com.vortex.cloud.ums.model.CloudUserFingerprint;
import com.vortex.cloud.vfs.common.exception.VortexException;

@Transactional
@Service("cloudUserFingerprintService")
public class CloudUserFingerprintServiceImpl implements ICloudUserFingerprintService {
	private static final Logger logger = LoggerFactory.getLogger(CloudUserFingerprintServiceImpl.class);
	@Resource
	private ICloudUserFingerprintDao cloudUserFingerprintDao;

	@Override
	public void saveFingerprint(String userId, String fingerprint) throws Exception {
		if (StringUtils.isEmpty(userId)) {
			logger.error("保存指纹时，传入的用户id为空");
			throw new VortexException("保存指纹时，传入的用户id为空");
		}

		if (StringUtils.isEmpty(fingerprint)) {
			logger.error("保存指纹时，传入的指纹为空");
			throw new VortexException("保存指纹时，传入的指纹为空");
		}

		CloudUserFingerprint cf = this.cloudUserFingerprintDao.findByUserId(userId);
		if (cf == null) {
			cf = new CloudUserFingerprint();
			cf.setUserId(userId);
			cf.setFingerprints(fingerprint);
			this.cloudUserFingerprintDao.save(cf);
		} else {
			// 如果已经有此指纹，则直接返回
			if (cf.getFingerprints().contains(fingerprint)) {
				return;
			}

			cf.setFingerprints(cf.getFingerprints() + "," + fingerprint);
			this.cloudUserFingerprintDao.update(cf);
		}
	}

	@Override
	public void refreshFingerprint(String userId, String fingerprint) throws Exception {
		if (StringUtils.isEmpty(userId)) {
			logger.error("刷新指纹时，传入的用户id为空");
			throw new VortexException("保存指纹时，传入的用户id为空");
		}

		if (StringUtils.isEmpty(fingerprint)) {
			logger.error("刷新指纹时，传入的指纹为空");
			throw new VortexException("保存指纹时，传入的指纹为空");
		}

		CloudUserFingerprint cf = this.cloudUserFingerprintDao.findByUserId(userId);
		if (cf == null) {
			cf = new CloudUserFingerprint();
			cf.setUserId(userId);
			cf.setFingerprints(fingerprint);
			this.cloudUserFingerprintDao.save(cf);
		} else {
			cf.setFingerprints(fingerprint);
			this.cloudUserFingerprintDao.update(cf);
		}
	}

	@Override
	public String getFingerprint(String userId) throws Exception {
		if (StringUtils.isEmpty(userId)) {
			logger.error("查询指纹时，传入的用户id为空");
			throw new VortexException("查询指纹时，传入的用户id为空");
		}

		CloudUserFingerprint cf = this.cloudUserFingerprintDao.findByUserId(userId);

		if (cf == null) {
			return null;
		} else {
			return cf.getFingerprints();
		}
	}

	@Override
	public List<CloudUserFingerprintDto> syncFingerprint(String tenantId, Integer pageSize, Long syncTime) throws Exception {
		if (StringUtils.isEmpty(tenantId)) {
			logger.error("同步指纹时，传入的租户id为空");
			throw new VortexException("同步指纹时，传入的租户id为空");
		}
		if (pageSize == null || pageSize < 1) {
			logger.error("同步指纹时，传入的同步数量非法");
			throw new VortexException("同步指纹时，传入的同步数量非法");
		}
		if (syncTime == null || syncTime < 0) {
			logger.error("同步指纹时，传入的同步时间戳非法");
			throw new VortexException("同步指纹时，传入的同步时间戳非法");
		}

		return this.cloudUserFingerprintDao.syncFingerprint(tenantId, pageSize, syncTime);
	}
}
