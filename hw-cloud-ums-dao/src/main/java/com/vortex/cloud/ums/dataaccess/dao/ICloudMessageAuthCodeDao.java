package com.vortex.cloud.ums.dataaccess.dao;

import com.vortex.cloud.ums.model.CloudMessageAuthCode;
import com.vortex.cloud.vfs.data.hibernate.repository.HibernateRepository;
/**
* @ClassName: ICloudMessageAuthCodeDao
* @Description: 手机短信验证码
* @author ZQ shan
* @date 2018年1月29日 下午2:13:55
* @see [相关类/方法]（可选）
* @since [产品/模块版本] （可选）
*/
public interface ICloudMessageAuthCodeDao extends HibernateRepository<CloudMessageAuthCode, String> {}
