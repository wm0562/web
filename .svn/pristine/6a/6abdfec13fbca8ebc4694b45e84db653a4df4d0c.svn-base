package com.vortex.cloud.ums.job;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.vortex.cloud.ums.dataaccess.service.IRedisSyncService;

/**
 * @ClassName: RedisAuthority
 * @Description: 按租户同步用户权限信息（菜单，功能码）
 * @author ZQ shan
 * @date 2017年10月9日 下午2:18:04
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Component
public class RedisAuthority {
	@Resource
	private IRedisSyncService redisSyncService;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Scheduled(cron = "0 0/5 * * * ?")
	public void init() {
		long t0 = System.currentTimeMillis();

		// 同步系统完整菜单
		redisSyncService.syncSystemMenuByTenant(null);
		// 同步系统完整机构部门
		redisSyncService.syncDeptOrgByTenant(null);
		// 同步系统完整人员
		redisSyncService.syncStaffByTenant(null);
		// 同步用户权限信息
		redisSyncService.syncUserAuthorityByTenant(null);
		logger.error("[同步redis,定时器同步缓存]，总耗时：" + (System.currentTimeMillis() - t0) + "ms");
	}
}
