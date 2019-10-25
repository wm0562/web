/*   
 * Copyright (C), 2005-2014, 苏州伏泰信息科技有限公司
 */
package com.vortex.cloud.ums.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.vortex.cloud.ums.dto.base.BackDeleteModelDto;

/**
 * @author LiShijun
 * @date 2016年4月5日 上午10:14:51
 * @Description 人员 History <author> <time> <desc>
 */
public class CloudStaffDto extends BackDeleteModelDto {

	private static final long serialVersionUID = 1L;

	private String unitName; // 所属单位
	private String userId; // 登录用户记录ID
	private String userName; // 登录用户名

	private String mobilePushMsgId;// 手机推送id
	private String rongLianAccount; // 容联帐号

	private String workTypeName;
	private String imToken;// 融云账号token
	private String photoId; // 头像ID
	private String companyName;// department name,只存储departmentName！！！
	private String postOrderIndex;// 职位参数的排序号（前端职位排序按照这个排序的）
	private String password; // 密码
	private String permissionScope;// 权限范围对应PermissionScopeEnum的值
	private String customScope;// 自定义范围 depart，orgId 用,分割
	private String lockuser; // 锁定标志
	// 意愿检查区域(行政区划Names)
	private String willCheckDivisionNames;

	// 退休
	public static final String STATUS_RETRIVE = "2";
	// 离职
	public static final String STATUS_LEAVE_YES = "1";
	// 在职
	public static final String STATUS_LEAVE_NO = "0";
	private String departmentId; // 所属公司或者环卫处
	private String orgId; // 所属部门
	private String tenantId; // 租户id
	private String orgName; // 所属单位/部门名称

	// 人员代码 Varchar(64)
	private String code;
	// 姓名 Varchar(20) Not null
	private String name;

	// 性别 Char(32) FK 男，女
	private String gender;
	// 民族 Char(32) FK 汉族等 pk ParameterSetting
	private String nationId;
	private String nationName;

	// 生日 Date 可以用于计算年龄
	private String birthday;

	// 身体状况 Char(32) pk ParameterSetting
	private String healthId;
	private String healthName;

	// 证件类型 Char(32) FK 身份证等 pk ParameterSetting
	private String credentialType;
	// 证件号 Varchar(64)
	private String credentialNum;

	// 婚姻状况 Char(32) FK 未婚、已婚、离异、丧偶 pk ParameterSetting
	private String maritalStatusId;
	private String maritalStatusName;
	// 政治面貌 Char(32) FK 党员等 pk ParameterSetting
	private String politicalStatusId;
	private String politicalStatusName;

	// 参加工作时间
	private String joinWorkTime;
	// 工作年限
	private String workYearLimit;

	// 是否离职退休
	private String isLeave;
	// 离职日期
	private String leaveTime;

	// 描述 varchar(4000) 可以用HTML
	private String description;

	// 原籍 Varchar(64)
	private String birthPlace;
	// 现籍 Varchar(64)
	private String presentPlace;
	// 居住地 Varchar(64)
	private String livePlace;

	// 手机
	private String phone;
	// 办公室电话 **/
	private String officeTel;
	// 邮箱 **/
	private String email;
	// 内部邮件 **/
	private String innerEmail;

	// 毕业学校
	private String graduate;
	// 学历
	private String educationId;
	private String educationName;

	// 人员编制性质
	private String authorizeId;
	private String authorizeName;
	// 职位 varchar(32)
	private String postId;
	private String postName;
	// 职务
	private String partyPostId;
	private String partyPostName;

	// 进入本单位时间
	private String entryHereTime;
	// ID卡
	private String idCard;

	// 社保号
	private String socialSecurityNo;
	// 社保缴纳情况
	private String socialSecuritycase;

	private Integer orderIndex; // 排序号

	/**
	 * 用工类型,WorkTypeEnum 巡检，保洁
	 */
	private String workTypeCode;

	/**
	 * 是否外包 默认否
	 * 
	 */
	private Boolean outSourcing = false;
	/**
	 * 外包单位 text，是外包的时候开放填写
	 */
	private String outSourcingComp;
	/**
	 * 名字首字母
	 */
	private String nameInitial;
	// 是否意愿者
	private Boolean isWillMan = false;
	// 意愿检查区域(行政区划Ids)
	private String willCheckDivisionIds;
	// 工作单位
	private String willWorkUnit;
	// 地址
	private String address;

	/**
	 * 照片id
	 */
	private String photograph;

	public String getLockuser() {
		return lockuser;
	}

	public void setLockuser(String lockuser) {
		this.lockuser = lockuser;
	}

	public String getPermissionScope() {
		return permissionScope;
	}

	public void setPermissionScope(String permissionScope) {
		this.permissionScope = permissionScope;
	}

	public String getCustomScope() {
		return customScope;
	}

	public void setCustomScope(String customScope) {
		this.customScope = customScope;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 新增，删除，修改标记
	 */
	private Integer flag;

	public String getPostOrderIndex() {
		return postOrderIndex;
	}

	public void setPostOrderIndex(String postOrderIndex) {
		this.postOrderIndex = postOrderIndex;
	}

	public String getPhotoId() {
		return photoId;
	}

	public void setPhotoId(String photoId) {
		this.photoId = photoId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getImToken() {
		return imToken;
	}

	public void setImToken(String imToken) {
		this.imToken = imToken;
	}

	public String getRongLianAccount() {
		return rongLianAccount;
	}

	public void setRongLianAccount(String rongLianAccount) {
		this.rongLianAccount = rongLianAccount;
	}

	public String getMobilePushMsgId() {
		return mobilePushMsgId;
	}

	public void setMobilePushMsgId(String mobilePushMsgId) {
		this.mobilePushMsgId = mobilePushMsgId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getWorkTypeName() {
		return workTypeName;
	}

	public void setWorkTypeName(String workTypeName) {
		this.workTypeName = workTypeName;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getWillCheckDivisionNames() {
		return willCheckDivisionNames;
	}

	public void setWillCheckDivisionNames(String willCheckDivisionNames) {
		this.willCheckDivisionNames = willCheckDivisionNames;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getNationId() {
		return nationId;
	}

	public void setNationId(String nationId) {
		this.nationId = nationId;
	}

	public String getNationName() {
		return nationName;
	}

	public void setNationName(String nationName) {
		this.nationName = nationName;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getHealthId() {
		return healthId;
	}

	public void setHealthId(String healthId) {
		this.healthId = healthId;
	}

	public String getHealthName() {
		return healthName;
	}

	public void setHealthName(String healthName) {
		this.healthName = healthName;
	}

	public String getCredentialType() {
		return credentialType;
	}

	public void setCredentialType(String credentialType) {
		this.credentialType = credentialType;
	}

	public String getCredentialNum() {
		return credentialNum;
	}

	public void setCredentialNum(String credentialNum) {
		this.credentialNum = credentialNum;
	}

	public String getMaritalStatusId() {
		return maritalStatusId;
	}

	public void setMaritalStatusId(String maritalStatusId) {
		this.maritalStatusId = maritalStatusId;
	}

	public String getMaritalStatusName() {
		return maritalStatusName;
	}

	public void setMaritalStatusName(String maritalStatusName) {
		this.maritalStatusName = maritalStatusName;
	}

	public String getPoliticalStatusId() {
		return politicalStatusId;
	}

	public void setPoliticalStatusId(String politicalStatusId) {
		this.politicalStatusId = politicalStatusId;
	}

	public String getPoliticalStatusName() {
		return politicalStatusName;
	}

	public void setPoliticalStatusName(String politicalStatusName) {
		this.politicalStatusName = politicalStatusName;
	}

	public String getJoinWorkTime() {
		return joinWorkTime;
	}

	public void setJoinWorkTime(String joinWorkTime) {
		this.joinWorkTime = joinWorkTime;
	}

	public String getWorkYearLimit() {
		return workYearLimit;
	}

	public void setWorkYearLimit(String workYearLimit) {
		this.workYearLimit = workYearLimit;
	}

	public String getIsLeave() {
		return isLeave;
	}

	public void setIsLeave(String isLeave) {
		this.isLeave = isLeave;
	}

	public String getLeaveTime() {
		return leaveTime;
	}

	public void setLeaveTime(String leaveTime) {
		this.leaveTime = leaveTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBirthPlace() {
		return birthPlace;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}

	public String getPresentPlace() {
		return presentPlace;
	}

	public void setPresentPlace(String presentPlace) {
		this.presentPlace = presentPlace;
	}

	public String getLivePlace() {
		return livePlace;
	}

	public void setLivePlace(String livePlace) {
		this.livePlace = livePlace;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getOfficeTel() {
		return officeTel;
	}

	public void setOfficeTel(String officeTel) {
		this.officeTel = officeTel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getInnerEmail() {
		return innerEmail;
	}

	public void setInnerEmail(String innerEmail) {
		this.innerEmail = innerEmail;
	}

	public String getGraduate() {
		return graduate;
	}

	public void setGraduate(String graduate) {
		this.graduate = graduate;
	}

	public String getEducationId() {
		return educationId;
	}

	public void setEducationId(String educationId) {
		this.educationId = educationId;
	}

	public String getEducationName() {
		return educationName;
	}

	public void setEducationName(String educationName) {
		this.educationName = educationName;
	}

	public String getAuthorizeId() {
		return authorizeId;
	}

	public void setAuthorizeId(String authorizeId) {
		this.authorizeId = authorizeId;
	}

	public String getAuthorizeName() {
		return authorizeName;
	}

	public void setAuthorizeName(String authorizeName) {
		this.authorizeName = authorizeName;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public String getPartyPostId() {
		return partyPostId;
	}

	public void setPartyPostId(String partyPostId) {
		this.partyPostId = partyPostId;
	}

	public String getPartyPostName() {
		return partyPostName;
	}

	public void setPartyPostName(String partyPostName) {
		this.partyPostName = partyPostName;
	}

	public String getEntryHereTime() {
		return entryHereTime;
	}

	public void setEntryHereTime(String entryHereTime) {
		this.entryHereTime = entryHereTime;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getSocialSecurityNo() {
		return socialSecurityNo;
	}

	public void setSocialSecurityNo(String socialSecurityNo) {
		this.socialSecurityNo = socialSecurityNo;
	}

	public String getSocialSecuritycase() {
		return socialSecuritycase;
	}

	public void setSocialSecuritycase(String socialSecuritycase) {
		this.socialSecuritycase = socialSecuritycase;
	}

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

	public String getWorkTypeCode() {
		return workTypeCode;
	}

	public void setWorkTypeCode(String workTypeCode) {
		this.workTypeCode = workTypeCode;
	}

	public Boolean getOutSourcing() {
		return outSourcing;
	}

	public void setOutSourcing(Boolean outSourcing) {
		this.outSourcing = outSourcing;
	}

	public String getOutSourcingComp() {
		return outSourcingComp;
	}

	public void setOutSourcingComp(String outSourcingComp) {
		this.outSourcingComp = outSourcingComp;
	}

	public String getNameInitial() {
		return nameInitial;
	}

	public void setNameInitial(String nameInitial) {
		this.nameInitial = nameInitial;
	}

	public Boolean getIsWillMan() {
		return isWillMan;
	}

	public void setIsWillMan(Boolean isWillMan) {
		this.isWillMan = isWillMan;
	}

	public String getWillCheckDivisionIds() {
		return willCheckDivisionIds;
	}

	public void setWillCheckDivisionIds(String willCheckDivisionIds) {
		this.willCheckDivisionIds = willCheckDivisionIds;
	}

	public String getWillWorkUnit() {
		return willWorkUnit;
	}

	public void setWillWorkUnit(String willWorkUnit) {
		this.willWorkUnit = willWorkUnit;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhotograph() {
		return photograph;
	}

	public void setPhotograph(String photograph) {
		this.photograph = photograph;
	}
}
