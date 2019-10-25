package com.vortex.cloud.ums.web.rest.np;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vortex.cloud.vfs.common.web.springmvc.SpringmvcUtils;
import com.vortex.cloud.vfs.data.dto.RestResultDto;

@SuppressWarnings("all")
@RestController
@RequestMapping("cloud/management/rest/np/gpsCoorConvert")
public class GpsCoorConvert {
	/**
	 * 将某张表的某个列，从某种坐标系转换到目标坐标系。
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "convert", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResultDto<?> convert() throws Exception {
		try {
			String tableName = SpringmvcUtils.getParameter("tableName");
			String columnName = SpringmvcUtils.getParameter("columnName");
			String fromCoord = SpringmvcUtils.getParameter("fromCoord");
			String toCoord = SpringmvcUtils.getParameter("toCoord");

			return RestResultDto.newSuccess(true, "转换成功");
		} catch (Exception e) {
			return RestResultDto.newFalid("转换失败：" + e.getMessage());
		}
	}
}
