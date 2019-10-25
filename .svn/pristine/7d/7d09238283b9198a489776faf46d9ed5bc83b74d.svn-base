package com.vortex.cloud.ums.ui.service;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.vortex.cloud.ums.dto.TenantDto;
import com.vortex.cloud.ums.ui.config.UmsFeignConfig;
import com.vortex.cloud.ums.ui.dto.UmsLoginReturnInfoDto;
import com.vortex.cloud.ums.ui.fallcallback.UmsFeignFallCallback;
import com.vortex.cloud.ums.ui.support.UmsFeignConstants;
import com.vortex.cloud.vfs.data.dto.RestResultDto;

/**
 * @ClassName: IUmsFeignClient
 * @Description: ums对外接口
 * @author ZQ shan
 * @date 2018年1月5日 下午4:32:49
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@FeignClient(name = UmsFeignConstants.SERVICE_NAME, url = "${vortex.feign.url.ums:}", fallback = UmsFeignFallCallback.class, configuration = UmsFeignConfig.class)
public interface IUmsFeignClient {

	/**
	 * @Title: login
	 * 
	 * @Description: 伏泰用户登录
	 * 
	 * @return RestResultDto<UmsLoginReturnInfoDto>
	 * 
	 * @throws
	 */
	@RequestMapping(value = "cloud/management/rest/np/login/login")
	RestResultDto<UmsLoginReturnInfoDto> login(@RequestBody Map<String, Object> params);

	/**
	 * @Title: loginFromThirdPartyApp
	 * 
	 * @Description: 第3方app登录
	 * 
	 * @return RestResultDto<UmsLoginReturnInfoDto>
	 * 
	 * @throws
	 */
	@RequestMapping(value = "cloud/management/rest/np/login/loginFromThirdPartyApp")
	RestResultDto<UmsLoginReturnInfoDto> loginFromThirdPartyApp(@RequestBody Map<String, Object> params);

	/**
	 * @Title: loginPortalUser
	 * 
	 * @Description: 门户网站用户登录
	 * 
	 * @return RestResultDto<UmsLoginReturnInfoDto>
	 * 
	 * @throws
	 */
	@RequestMapping(value = "cloud/management/rest/np/login/loginPortalUser")
	RestResultDto<UmsLoginReturnInfoDto> loginPortalUser(@RequestBody Map<String, Object> params);

	/**
	 * 根据租户id，查询租户信息
	 * 
	 * @param response
	 * @return
	 */
	@GetMapping(value = "cloud/management/rest/np/tenant/getById")
	public RestResultDto<TenantDto> getById(@RequestParam("id") String id);
}
