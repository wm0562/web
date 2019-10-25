package com.vortex.cloud.ums.web.basic;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.vortex.cloud.ums.dataaccess.service.ICommonUtilsService;
import com.vortex.cloud.vfs.data.dto.LoginReturnInfoDto;

/**
 * @ClassName: BaseController
 * @Description: 该类用于获取cas登录的登录信息，controller只要继承 该类就可以访问登陆者信息
 * @author liShijun
 * @date 2016-4-15 下午09:13:00
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public abstract class BaseController {
	@Resource
	private ICommonUtilsService commonUtilsService;
	/**
	 * 获取业务系统访问基础设施云系统时的请求参数
	 * 
	 * @return
	 */
	public LoginReturnInfoDto getLoginInfo(HttpServletRequest request) throws Exception {
		return commonUtilsService.getLoginInfo(request);
	}

	/**
	 * 获取登录用户对应的租户ID
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	protected String getTenantId(HttpServletRequest request) throws Exception {
		return getLoginInfo(request).getTenantId();
	}

	/**
	 * 获取登录用户的Id
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	protected String getUserId(HttpServletRequest request) throws Exception {
		return getLoginInfo(request).getUserId();
	}
}
