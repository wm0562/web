package com.vortex.cloud.ums.dataaccess.service;

import com.vortex.cloud.ums.dto.ParamTypeDto;
import com.vortex.cloud.ums.model.PramType;
import com.vortex.cloud.vfs.data.hibernate.service.PagingAndSortingService;

public interface IParamTypeService extends PagingAndSortingService<PramType, String> {

	/**
	 * 通过自定义code查询参数类型
	 * 
	 * @param code
	 * @return
	 */
	public PramType findByCode(String code);

	long deletes(String[] idArr);

	/**
	 * 新增参数类型
	 * 
	 * @param groupCode
	 * @param paramTypeCode
	 * @param paramTypeName
	 * @param orderIndex
	 * @param description
	 * @return
	 */
	public PramType addParamType(ParamTypeDto dto) throws Exception;
}
