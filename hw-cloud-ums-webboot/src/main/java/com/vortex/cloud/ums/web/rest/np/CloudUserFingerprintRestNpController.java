package com.vortex.cloud.ums.web.rest.np;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vortex.cloud.ums.dataaccess.service.ICloudUserFingerprintService;
import com.vortex.cloud.vfs.data.dto.RestResultDto;

@RestController
@RequestMapping("cloud/management/rest/np/userFingerprint")
public class CloudUserFingerprintRestNpController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private ICloudUserFingerprintService cloudUserFingerprintService;

	@RequestMapping(value = "saveFingerprint", method = RequestMethod.POST)
	@ResponseBody
	public RestResultDto<?> saveFingerprint(@RequestParam("userId") String userId, @RequestParam("fingerprint") String fingerprint) {
		try {
			cloudUserFingerprintService.saveFingerprint(userId, fingerprint);
			return RestResultDto.newSuccess("保存用户指纹成功");
		} catch (Exception e) {
			String error_msg = "保存用户指纹失败";
			logger.error(error_msg, e.getMessage());
			return RestResultDto.newFalid(error_msg, e.getMessage());
		}
	}

	@RequestMapping(value = "refreshFingerprint", method = RequestMethod.POST)
	@ResponseBody
	public RestResultDto<?> refreshFingerprint(@RequestParam("userId") String userId, @RequestParam("fingerprint") String fingerprint) {
		try {
			cloudUserFingerprintService.refreshFingerprint(userId, fingerprint);
			return RestResultDto.newSuccess("刷新用户指纹成功");
		} catch (Exception e) {
			String error_msg = "刷新用户指纹失败";
			logger.error(error_msg, e.getMessage());
			return RestResultDto.newFalid(error_msg, e.getMessage());
		}
	}

	@RequestMapping(value = "getFingerprint", method = RequestMethod.GET)
	public RestResultDto<?> getFingerprint(String userId) {
		try {
			return RestResultDto.newSuccess(cloudUserFingerprintService.getFingerprint(userId), "获取用户指纹成功");
		} catch (Exception e) {
			String error_msg = "获取用户指纹失败";
			logger.error(error_msg, e.getMessage());
			return RestResultDto.newFalid(error_msg, e.getMessage());
		}
	}

	@RequestMapping(value = "syncFingerprint", method = RequestMethod.GET)
	public RestResultDto<?> syncFingerprint(String tenantId, Integer pageSize, Long syncTime) {
		try {
			return RestResultDto.newSuccess(cloudUserFingerprintService.syncFingerprint(tenantId, pageSize, syncTime), "获取用户指纹成功");
		} catch (Exception e) {
			String error_msg = "获取用户指纹失败";
			logger.error(error_msg, e.getMessage());
			return RestResultDto.newFalid(error_msg, e.getMessage());
		}
	}
}
