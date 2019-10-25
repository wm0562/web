package com.vortex.cloud.ums.dataaccess.service;

import java.util.List;

/**
 * @ClassName: IMcsService
 * @Description: 消息发送
 * @author ZQ shan
 * @date 2018年1月31日 上午10:10:03
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface IMcsService {
	/**
	 * @Title: send
	 * 
	 * @Description: 发送短信
	 * 
	 * @return void
	 * 
	 * @throws
	 */
	public void send(String templateId, List<String> targets, List<Object> params) throws Exception;

	/**
	 * @Title: send
	 * 
	 * @Description: 发送消息(sendType不填或为空，默认发送短信)
	 * 
	 * @return void
	 * 
	 * @throws
	 */
	public void send(String templateId, List<String> targets, List<Object> params, String sendType) throws Exception;
}
