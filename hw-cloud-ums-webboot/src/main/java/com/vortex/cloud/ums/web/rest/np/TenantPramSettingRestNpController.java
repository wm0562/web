package com.vortex.cloud.ums.web.rest.np;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vortex.cloud.ums.dataaccess.service.ITenantParamSettingService;
import com.vortex.cloud.ums.model.TenantPramSetting;
import com.vortex.cloud.ums.support.ManagementConstant;
import com.vortex.cloud.ums.util.utils.PropertyUtils;
import com.vortex.cloud.vfs.common.mapper.JsonMapper;
import com.vortex.cloud.vfs.common.web.springmvc.SpringmvcUtils;
import com.vortex.cloud.vfs.data.dto.RestResultDto;

@RestController
@RequestMapping("cloud/management/rest/np/tenant/paramSetting")
public class TenantPramSettingRestNpController {
	private static final Logger logger = LoggerFactory.getLogger(TenantPramSettingRestNpController.class);

	@Resource
	private ITenantParamSettingService tenantParamSettingService;

	/**
	 * 获取多个参数类型下的参数列表
	 * 
	 * @param request
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "loadMultiParamList", method = RequestMethod.POST)
	public RestResultDto loadMultiParamList(HttpServletRequest request) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			JsonMapper jm = new JsonMapper();
			Map<String, Object> prams = jm.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS),
					Map.class);
			List<String> typeCodes = (List<String>) prams.get("typeCodes");
			String tenantId = (String) prams.get("tenantId");
			data = tenantParamSettingService.loadMultiParamList(tenantId, typeCodes);
		} catch (Exception e) {
			e.printStackTrace();
			result = ManagementConstant.REST_RESULT_FAIL;
			msg = "查询失败！";
			exception = e.getMessage();
			data = null;
			logger.error(msg, e);
		} finally {
			restResultDto.setResult(result);
			restResultDto.setMsg(msg);
			restResultDto.setData(data);
			restResultDto.setException(exception);
		}

		return restResultDto;
	}

	/**
	 * 获取单个参数类型下的参数列表
	 * 
	 * @param request
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "loadSingleParam", method = RequestMethod.POST)
	public RestResultDto<List<Map<String, Object>>> loadSingleParam(HttpServletRequest request) {
		try {
			String tenantId = SpringmvcUtils.getParameter("tenantId");
			String typeCode = SpringmvcUtils.getParameter("typeCode");

			return RestResultDto.newSuccess(this.getParamList(tenantId, typeCode));
		} catch (Exception e) {
			logger.error("获取单个参数类型下的参数列表出错", e);
			return RestResultDto.newFalid("获取单个参数类型下的参数列表出错", e.getMessage());
		}
	}

	/**
	 * 根据参数类型获取到参数列表，用于页面下拉框展示
	 * 
	 * @param tenantId
	 * @param parameterType
	 * @return
	 */
	private List<Map<String, Object>> getParamList(String tenantId, String parameterType) {
		List<Map<String, Object>> selectOptionList = Lists.newArrayList();

		// 获取parameter setting列表
		List<TenantPramSetting> list = this.getByParamTypeCode(tenantId, parameterType);

		if (CollectionUtils.isNotEmpty(list)) {
			Map<String, Object> mapValue = null;
			for (TenantPramSetting entity : list) {
				mapValue = Maps.newHashMap();
				mapValue.put("value", entity.getParmCode());
				mapValue.put("text", entity.getParmName());
				selectOptionList.add(mapValue);
			}
		}

		return selectOptionList;
	}

	/**
	 * 获取指定的参数类型的参数列表
	 * 
	 * @param tenantId
	 * @param parameterType
	 * @return
	 */
	private List<TenantPramSetting> getByParamTypeCode(String tenantId, String parameterType) {
		// 封装获取配置文件参数常量
		if (StringUtils.isBlank(tenantId) || StringUtils.isBlank(parameterType)) {// 若无该配置属性，返回
			return null;
		}

		// 从文件中获取参数类型code
		parameterType = PropertyUtils.getPropertyValue(parameterType);
		if (StringUtils.isBlank(parameterType)) {// 若无该配置属性，返回
			return null;
		}

		// 获取parameter setting列表
		return tenantParamSettingService.findListByParamTypeCode(tenantId, parameterType);
	}
}
