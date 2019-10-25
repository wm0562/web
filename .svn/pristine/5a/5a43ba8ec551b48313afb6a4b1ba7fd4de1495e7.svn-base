package com.vortex.cloud.ums.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.vortex.cloud.vfs.data.model.BakDeleteModel;

/**
 * 第三方app注册表，在第三方系统访问伏泰接口时使用
 * 
 * @author XY
 *
 */
@Entity
@Table(name = CloudThirdPartyApp.TABLE_NAME)
public class CloudThirdPartyApp extends BakDeleteModel {
	public static final String TABLE_NAME = "cloud_third_party_app";
	private static final long serialVersionUID = 1L;
	/**
	 * 租户id
	 */
	private String tenantId;
	/**
	 * appKey，第三方用户唯一凭证，类似系统code，全表唯一
	 */
	private String appKey;
	/**
	 * 第三方用户唯一凭证密钥，采用32位uuid
	 */
	private String appSecret;
	/**
	 * 控制访问频次的时间周期枚举-分钟、小时、天等
	 */
	private String period;
	/**
	 * 周期中可以访问的次数限制
	 */
	private Long periodCount;
	/**
	 * 有效期开始时间
	 */
	private Date dateFrom;
	/**
	 * 有效期截止时间
	 */
	private Date dateTo;
	/**
	 * ip白名单列表，英文逗号分隔
	 */
	private String whiteList;

	@Column(name = "tenantId", length = 32, nullable = false)
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	@Column(name = "appKey", length = 32, nullable = false)
	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	@Column(name = "appSecret", length = 32, nullable = false)
	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	@Column(name = "period", nullable = true)
	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	@Column(name = "periodCount", nullable = true)
	public Long getPeriodCount() {
		return periodCount;
	}

	public void setPeriodCount(Long periodCount) {
		this.periodCount = periodCount;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	@Column(name = "whiteList", length = 255, nullable = true)
	public String getWhiteList() {
		return whiteList;
	}

	public void setWhiteList(String whiteList) {
		this.whiteList = whiteList;
	}
}
