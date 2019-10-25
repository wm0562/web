package com.vortex.cloud.ums.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.vortex.cloud.vfs.data.model.BakDeleteModel;

@Entity
@Table(name = "cloud_user_fingerprint")
public class CloudUserFingerprint extends BakDeleteModel {
	private static final long serialVersionUID = 1L;
	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 指纹字符串，英文逗号分隔
	 */
	private String fingerprints;

	@Column(name = "userId", length = 64, nullable = false)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Type(type = "text")
	@Column(name = "fingerprints", nullable = false)
	public String getFingerprints() {
		return fingerprints;
	}

	public void setFingerprints(String fingerprints) {
		this.fingerprints = fingerprints;
	}
}
