package com.vortex.cloud.ums.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.vortex.cloud.vfs.data.model.BakDeleteModel;

/**
 * 门户网站人员
 * 
 * @author XY
 *
 */
@Entity
@Table(name = "cloud_portal_user")
public class CloudPortalUser extends BakDeleteModel {
	private static final long serialVersionUID = 1L;

	private String userName; // 用户名(所有租户下唯一)
	private String password; // 密码
	private String nickname; // 昵称
	private String profilePhoto; // 头像
	private String phone; // 手机号(所有租户下唯一)
	private String gender; // 性别，男-M，女-F
	private String birthday; // 生日，yyyy-MM-dd标准字符串
	// 租户id
	private String tenantId;

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getProfilePhoto() {
		return profilePhoto;
	}

	public void setProfilePhoto(String profilePhoto) {
		this.profilePhoto = profilePhoto;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
}
