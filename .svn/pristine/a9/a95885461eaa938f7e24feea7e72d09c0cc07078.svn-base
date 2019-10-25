package com.vortex.cloud.ums.dataaccess.service.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vortex.cloud.cas.server.ui.service.ICasServerFeignClient;
import com.vortex.cloud.ums.dataaccess.service.ICommonUtilsService;
import com.vortex.cloud.ums.support.ManagementConstant;
import com.vortex.cloud.vfs.common.lang.StringUtil;
import com.vortex.cloud.vfs.common.mapper.JsonMapper;
import com.vortex.cloud.vfs.common.web.Servlets;
import com.vortex.cloud.vfs.common.web.springmvc.SpringmvcUtils;
import com.vortex.cloud.vfs.data.dto.LoginReturnInfoDto;
import com.vortex.cloud.vfs.data.dto.RestResultDto;
import com.vortex.cloud.vfs.data.support.DateConverter;
import com.vortex.cloud.vfs.data.support.SearchFilter;
import com.vortex.cloud.vfs.data.support.SearchFilter.FieldType;
import com.vortex.cloud.vfs.data.support.SearchFilter.Operator;

@SuppressWarnings("all")
@Transactional
@Service("commonUtilsService")
public class CommonUtilsServiceImpl implements ICommonUtilsService {
	@Autowired
	private ICasServerFeignClient casServerFeignClient;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 绑定租户id
	 * 
	 * @param searchFilters
	 */
	public List<SearchFilter> bindTenantId(List<SearchFilter> searchFilters) {
		if (null == searchFilters) {
			searchFilters = Lists.newArrayList();
		}

		String tenantId = getLoginInfo().getTenantId();
		searchFilters.add(new SearchFilter("tenantId", SearchFilter.Operator.EQ, tenantId));

		return searchFilters;
	}

	@SuppressWarnings("unused")
	public Map<String, Object> searchFilters2Map(List<SearchFilter> list) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (SearchFilter searchFilter : list) {
			String fieldName = searchFilter.getFieldName();
			Class<?> clazz = searchFilter.getFieldClass();
			Operator operator = searchFilter.getOperator();
			Object value = searchFilter.getValue();
			if (operator.equals(Operator.LIKE)) {
				value = "%" + value + "%";
			}
			map.put(fieldName, value);
		}

		return map;
	}

	public Map<String, String> sort2Map(Sort sort) {
		Map<String, String> map = new HashMap<String, String>();
		if (sort != null) {
			for (Order order : sort) {
				if (order.isAscending()) {
					map.put(order.getProperty(), Direction.ASC.toString());
				} else {
					map.put(order.getProperty(), Direction.DESC.toString());
				}
			}
		}
		return map;
	}

	public void getSortSql(Map<String, Object> filterPropertyMap, Map<String, String> sortValueMap) {
		if (!(null == sortValueMap || sortValueMap.isEmpty())) {
			StringBuilder orderbySql = new StringBuilder();
			String value;
			Set<String> keys = sortValueMap.keySet();
			for (String key : keys) {
				value = sortValueMap.get(key);
				if (!StringUtil.isNullOrEmpty(key) && !StringUtil.isNullOrEmpty(value)) {
					if (orderbySql.length() == 0) {
						orderbySql.append(" order by ");
					}
					orderbySql.append(StringUtil.clean(key)).append(" ").append(StringUtil.clean(value)).append(",");
				}

			}
			if (orderbySql.length() > 0) {
				if (orderbySql.charAt(orderbySql.length() - 1) == ',') {
					orderbySql.deleteCharAt(orderbySql.length() - 1);
				}
			}
			filterPropertyMap.put("orderbySql", orderbySql.toString());
		}
	}

	/**
	 * @Title: getCommonSort @Description: (获取通用排序) @return List<Order> @author ZQ
	 *         shan @date 2016年4月21日 下午2:24:20 @throws
	 */
	public List<Order> getCommonSort(String sort, String order, String objName) {
		List<Order> orders = Lists.newArrayList();
		if (!StringUtil.isNullOrEmpty(sort) && !StringUtil.isNullOrEmpty(order) && !StringUtil.isNullOrEmpty(objName)) {
			String[] props = StringUtil.splitComma(sort);
			String[] orderArray = StringUtil.splitComma(order);
			if (props.length > 0) {
				String propert = "";
				for (int i = 0; i < props.length; i++) {
					propert = objName + "." + props[i];
					Order ord = null;
					if (Direction.DESC.name().equalsIgnoreCase(orderArray[i])) {
						ord = new Order(Direction.DESC, propert);
					} else {
						ord = new Order(Direction.ASC, propert);
					}
					orders.add(ord);
				}
			}
		}
		return orders;
	}

	/**
	 * 获取tenantId
	 * 
	 * @return
	 */
	public String getTenantId() {
		return getLoginInfo().getTenantId();
	}

	/**
	 * 获取登录用户的Id
	 * 
	 * @param request
	 * @return
	 */
	public String getUserId() {
		logger.info("getUserId()");

		return getLoginInfo().getUserId();
	}

	/**
	 * 获取业务系统访问基础设施云系统时的请求参数
	 * 
	 * @return
	 */
	public LoginReturnInfoDto getLoginInfo() {
		return this.getLoginInfo(SpringmvcUtils.getRequest());

	}

	/**
	 * 将异常堆栈转换为字符串
	 * 
	 * @param throwable
	 *            异常
	 * @return String
	 */
	public String getStackTraceAsString(Throwable throwable) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		throwable.printStackTrace(printWriter);
		return result.toString();
	}

	/**
	 * 从HttpRequest中创建PropertyFilter列表, 默认Filter属性名前缀为filter.
	 * 
	 * @see #buildFromHttpRequest(HttpServletRequest, String)
	 */
	public List<SearchFilter> buildFromHttpRequest(final HttpServletRequest request) {
		return buildFromHttpRequest(request, ManagementConstant.SEARCH_PREFIX);
	}

	/**
	 * 从HttpRequest中创建SearchFilter列表 SearchFilter命名规则为Filter属性前缀_比较类型属性类型_属性名.
	 * 
	 * eg. s_EQ_name_S
	 */
	public List<SearchFilter> buildFromHttpRequest(final HttpServletRequest request, final String filterPrefix) {
		List<SearchFilter> filterList = Lists.newArrayList();

		// 从request中获取含属性前缀名的参数,构造去除前缀名后的参数Map.
		Map<String, Object> filterParamMap = Servlets.getParametersStartingWith(request, filterPrefix + "_");

		Map<String, SearchFilter> filters = parse(filterParamMap);
		filterList.addAll(filters.values());
		return filterList;
	}

	/**
	 * searchParams中key的格式为OPERATOR_FIELDNAME_Class
	 */
	public Map<String, SearchFilter> parse(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = Maps.newHashMap();

		for (Entry<String, Object> entry : searchParams.entrySet()) {
			// 过滤掉空值
			String key = entry.getKey();
			Class<?> keyType = null;
			String value = (String) entry.getValue();
			if (StringUtils.isBlank(value)) {
				continue;
			}

			// 拆分operator与filedAttribute
			String[] names = StringUtils.split(key, "_");
			if (names.length < 2) {
				throw new IllegalArgumentException(key + " is not a valid search filter name");
			}
			if (names.length == 3) {
				keyType = Enum.valueOf(FieldType.class, names[2]).getValue();
			} else {
				keyType = Enum.valueOf(FieldType.class, FieldType.S.name()).getValue();
			}

			String filedName = names[1];
			Operator operator = Operator.valueOf(names[0]);

			// 创建searchFilter
			SearchFilter filter = getSearchFilter(filedName, keyType, operator, value);
			filters.put(key, filter);
		}

		return filters;
	}

	private SearchFilter getSearchFilter(String fieldName, Class<?> fieldClass, Operator operator, String value) {
		Object keyValue = null;
		switch (operator) {
		case EQ:
		case NE:
		case LIKE:
		case NLIKE:
		case LLIKE:
		case RLIKE:
		case GT:
		case LT:
		case GTE:
		case LTE:
			keyValue = ConvertUtils.convert(value, fieldClass);
			break;
		case IN:
		case NIN:
		case NBETWEEN:
		case BETWEEN:
			String[] str = value.split(",");
			if (fieldClass == Enum.valueOf(FieldType.class, FieldType.D.name()).getValue()) {
				ConvertUtils.register(new DateConverter(), fieldClass);
			}
			keyValue = ConvertUtils.convert(str, fieldClass);
			break;
		case NULL:
			keyValue = null;
			break;
		case NNULL:
			keyValue = null;
			break;
		}
		return new SearchFilter(fieldName, fieldClass, operator, keyValue);
	}

	private static JsonMapper mapper = new JsonMapper();

	public static String getAccessTokenFromHead(HttpServletRequest request) {
		String access_token = StringUtil.isNullOrEmpty(request.getHeader(ManagementConstant.ACCESSTOKEN_HEAD_KEY)) ? request.getParameter(ManagementConstant.ACCESSTOKEN_PARAM_KEY) : request.getHeader(ManagementConstant.ACCESSTOKEN_HEAD_KEY);
		if (StringUtil.isNullOrEmpty(access_token)) {
			return null;
		}
		access_token = access_token.split(" ").length == 2 ? access_token.split(" ")[1] : access_token.split(" ")[0];
		return access_token;
	}

	public String getUserId(HttpServletRequest request) {
		return getUserId(getAccessTokenFromHead(request));
	}

	/**
	 * 獲取用戶信息
	 *
	 * @param accessToken
	 * @return
	 */
	public String getUserId(String accessToken) {
		if (StringUtil.isNullOrEmpty(accessToken)) {
			return null;
		}
		LoginReturnInfoDto info = null;
		try {
			info = getLoginInfo(accessToken);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null == info) {
			return null;
		}
		return info.getUserId();
	}

	public LoginReturnInfoDto getLoginInfo(HttpServletRequest request) {
		LoginReturnInfoDto returnValue = new LoginReturnInfoDto();
		try {
			return getLoginInfo(getAccessTokenFromHead(request));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}

	/**
	 * 获取业务系统访问基础设施云系统时的请求参数
	 * 
	 * @return
	 * @throws Exception
	 */
	public LoginReturnInfoDto getLoginInfo(String access_token) throws Exception {
		LoginReturnInfoDto returnValue = new LoginReturnInfoDto();
		long t0 = System.currentTimeMillis();
		if (StringUtil.isNullOrEmpty(access_token)) {
			return returnValue;
		}
		RestResultDto user = casServerFeignClient.getUserByToken("Bearer " + access_token);
		if (user.getResult() == RestResultDto.RESULT_FAIL) {
			return returnValue;
		}
		long t1 = System.currentTimeMillis() - t0;

		Map<String, Object> mapValue = mapper.fromJson(mapper.toJson(user.getData()), Map.class);
		String systemList = (String) mapValue.get("systemList");
		mapValue.remove("systemList");
		LoginReturnInfoDto info = mapper.fromJson(mapper.toJson(mapValue), LoginReturnInfoDto.class);
		if (!StringUtil.isNullOrEmpty(systemList)) {
			info.setSystemList(mapper.fromJson(systemList, List.class));
		}
		logger.error("用户access_token：" + access_token + "，获取登录人信息总耗时：" + (System.currentTimeMillis() - t0) + "ms，http请求耗时：" + t1 + "ms");
		return info;
	}

}
