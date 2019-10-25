package com.vortex.cloud.ums.web.rest.np;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vortex.cloud.ums.dataaccess.service.ICentralCacheRedisService;
import com.vortex.cloud.ums.dataaccess.service.ICommonUtilsService;
import com.vortex.cloud.ums.dataaccess.service.IUploadResultInfoService;
import com.vortex.cloud.ums.dataaccess.service.impl.CentralCacheRedisServiceImpl;
import com.vortex.cloud.ums.dto.CloudStaffDto;
import com.vortex.cloud.ums.model.upload.UploadResultInfo;
import com.vortex.cloud.ums.support.ManagementConstant;
import com.vortex.cloud.ums.util.support.ForeContext;
import com.vortex.cloud.vfs.common.mapper.JsonMapper;
import com.vortex.cloud.vfs.common.web.springmvc.SpringmvcUtils;
import com.vortex.cloud.vfs.data.dto.DataStore;
import com.vortex.cloud.vfs.data.dto.RestResultDto;
import com.vortex.cloud.vfs.data.support.SearchFilter;
import com.vortex.cloud.vfs.data.support.SearchFilter.Operator;

@RequestMapping("cloud/management/rest/np/uploadResultInfo")
@RestController
public class UploadResultInfoRestNpController {
	private static final Logger logger = LoggerFactory.getLogger(UploadResultInfoRestNpController.class);

	@Resource
	private IUploadResultInfoService uploadResultInfoService;

	@Resource(name = CentralCacheRedisServiceImpl.CLASSNAME)
	private ICentralCacheRedisService centralCacheRedisService;
	@Resource
	private ICommonUtilsService commonUtilsService;
	/**
	 * 新增
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "queryList", method = RequestMethod.POST)
	public RestResultDto queryList(HttpServletRequest request, CloudStaffDto dto) {
		String msg = null;
		Integer result = ManagementConstant.REST_RESULT_SUCC;
		Object data = null;
		String exception = null;
		RestResultDto restResultDto = new RestResultDto();

		try {
			Map<String, String> paramMap = new JsonMapper()
					.fromJson(SpringmvcUtils.getParameter(ManagementConstant.REST_PMS), Map.class);
			String tenantId = paramMap.get("tenantId");
			DataStore<UploadResultInfo> ds = new DataStore<>();
			Order defaultOrder = new Order(Direction.ASC, "rowNum");
			Sort defaultSort = ForeContext.getSort(request, defaultOrder);
			Pageable pageable = ForeContext.getPageable(request, defaultSort);

			List<SearchFilter> searchFilter = commonUtilsService.buildFromHttpRequest(request);

			// 由于分布式，所以存到redis中，然后从redis获取
			String marks = centralCacheRedisService.getObject(ManagementConstant.MARK_KEY_PREFIX + tenantId,
					String.class);
			searchFilter.add(new SearchFilter("marks", Operator.EQ, marks));
			data = uploadResultInfoService.queryDataStorePage(pageable, searchFilter);

			msg = "保存成功";
		} catch (Exception e) {
			e.printStackTrace();
			result = ManagementConstant.REST_RESULT_FAIL;
			msg = "保存失败！";
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
}
