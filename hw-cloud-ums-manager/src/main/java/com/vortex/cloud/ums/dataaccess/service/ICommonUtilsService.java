package com.vortex.cloud.ums.dataaccess.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.vortex.cloud.vfs.data.dto.LoginReturnInfoDto;
import com.vortex.cloud.vfs.data.support.SearchFilter;

/**
 * @ClassName: IMcsService
 * @Description: 消息发送
 * @author ZQ shan
 * @date 2018年1月31日 上午10:10:03
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface ICommonUtilsService {
	/**
	 * 绑定租户id
	 * 
	 * @param searchFilters
	 */
	public List<SearchFilter> bindTenantId(List<SearchFilter> searchFilters);

	public Map<String, Object> searchFilters2Map(List<SearchFilter> list);

	public Map<String, String> sort2Map(Sort sort);

	public String getTenantId();

	public List<SearchFilter> buildFromHttpRequest(HttpServletRequest request);

	public List<Order> getCommonSort(String sort, String order, String objName);

	public String getUserId(HttpServletRequest request);
	
	public LoginReturnInfoDto getLoginInfo(HttpServletRequest request);
}
