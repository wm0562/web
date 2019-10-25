package com.vortex.cloud.ums.ui.fallcallback;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.vortex.cloud.ums.dto.TenantDto;
import com.vortex.cloud.ums.ui.dto.UmsLoginReturnInfoDto;
import com.vortex.cloud.ums.ui.service.IUmsFeignClient;
import com.vortex.cloud.vfs.data.dto.RestResultDto;

/**
 * @ClassName: UmsFeignFallCallback
 * @Description: 调用失败返回
 * @author ZQ shan
 * @date 2018年1月5日 下午4:33:08
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Component
public class UmsFeignFallCallback implements IUmsFeignClient {

	public RestResultDto<UmsLoginReturnInfoDto> login(Map<String, Object> params) {
		return RestResultDto.newFalid("用户登录失败");
	}

	public RestResultDto<UmsLoginReturnInfoDto> loginFromThirdPartyApp(Map<String, Object> params) {
		return RestResultDto.newFalid("第3方app登录失败");
	}

	public RestResultDto<UmsLoginReturnInfoDto> loginPortalUser(Map<String, Object> params) {
		return RestResultDto.newFalid("门户网站用户登录失败");
	}

	@Override
	public RestResultDto<TenantDto> getById(String id) {
		return RestResultDto.newFalid("查询租户信息失败");
	}
}
