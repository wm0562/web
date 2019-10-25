package com.vortex.cloud.ums.dataaccess.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.vortex.cloud.sms.netease.model.ShortMsgTemplate;
import com.vortex.cloud.sms.netease.signature.Signature;
import com.vortex.cloud.sms.netease.signature.SignatureVortex;
import com.vortex.cloud.ums.dataaccess.service.IMcsService;
import com.vortex.cloud.vfs.common.exception.ServiceException;
import com.vortex.cloud.vfs.common.lang.StringUtil;

@SuppressWarnings("all")
@Transactional
@Service("mcsService")
public class McsServiceImpl implements IMcsService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void send(String templateId, List<String> targets, List<Object> params) throws Exception {
		send(templateId, targets, params, null);
	}

	@Override
	public void send(String templateId, List<String> targets, List<Object> params, String sendType) throws Exception {
		if (StringUtil.isNullOrEmpty(templateId) || CollectionUtils.isEmpty(targets)
				|| CollectionUtils.isEmpty(params)) {
			throw new ServiceException("发送消息，参数不全");
		}
		if (StringUtil.isNullOrEmpty(sendType)) {
			sendType = "phone";
		}

		if ("phone".equals(sendType)) {
			List<String> paramters = Lists.newArrayList();
			for (Object obj : params) {
				paramters.add(String.valueOf(obj));
			}
			Signature signature = new SignatureVortex(templateId);
			ShortMsgTemplate.getInstance().send(signature, targets, paramters);
		}
	}
}
