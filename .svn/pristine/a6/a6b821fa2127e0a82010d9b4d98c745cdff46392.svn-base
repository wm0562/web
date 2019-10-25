package com.vortex.cloud.ums.dataaccess.dao.xm.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.PageImpl;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.vortex.cloud.ums.dataaccess.dao.xm.ISysOrgXmDao;
import com.vortex.cloud.ums.dto.IdNameDto;
import com.vortex.cloud.ums.util.utils.QueryUtil;
import com.vortex.cloud.ums.xm.dto.SysOrgQueryDto;
import com.vortex.cloud.ums.xm.dto.SysOrgXmDto;
import com.vortex.cloud.ums.xm.model.SysOrgXm;
import com.vortex.cloud.vfs.data.hibernate.repository.SimpleHibernateRepository;
import com.vortex.cloud.vfs.data.model.BakDeleteModel;
import com.vortex.cloud.vfs.data.util.StaticDBType;

@Repository("sysOrgXmDao")
public class SysOrgXmDaoImpl extends SimpleHibernateRepository<SysOrgXm, String> implements ISysOrgXmDao {
	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public DetachedCriteria getDetachedCriteria() {
		DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass(), "sysOrgXm");
		criteria.add(Restrictions.eq("beenDeleted", BakDeleteModel.NO_DELETED));
		return criteria;
	}

	@Override
	public List<SysOrgXmDto> listAll() {
		String sql = "select * from SYS_ORG where beenDeleted = 0 order by order_no";
		return jdbcTemplate.query(sql.toString(), Lists.newArrayList().toArray(), BeanPropertyRowMapper.newInstance(SysOrgXmDto.class));
	}

	@Override
	public org.springframework.data.domain.Page<SysOrgXmDto> queryOrgInfo(SysOrgQueryDto dto) throws Exception {
		if (dto == null) {
			return null;
		}
		StringBuffer sql = new StringBuffer();
		List<Object> argList = Lists.newArrayList();

		sql.append("select * from sys_org where 1=1 ");
		if (StringUtils.isNotEmpty(dto.getParentId())) {
			sql.append(" and pid=? ");
			argList.add(dto.getParentId());
		}
		if (StringUtils.isNotEmpty(dto.getCode())) {
			sql.append(" and org_code like ? ");
			argList.add("%" + dto.getCode() + "%");
		}
		if (StringUtils.isNotEmpty(dto.getName())) {
			sql.append(" and org_name like ? ");
			argList.add("%" + dto.getName() + "%");
		}
		sql.append(" and beenDeleted = 0 ");

		String sqlCnt = " SELECT COUNT(1) FROM ( " + sql.toString() + " ) t ";
		long totalCnt = jdbcTemplate.queryForObject(sqlCnt, argList.toArray(), Integer.class);

		sql.append(" order by order_no ");

		// 组合分页条件
		Integer startRow = dto.getPageNum() * dto.getPageSize();
		Integer endRow = (dto.getPageNum() + 1) * dto.getPageSize();
		String sqlString = QueryUtil.getPagingSql(sql.toString(), startRow, endRow, StaticDBType.getDbType());

		List<SysOrgXmDto> pageList = jdbcTemplate.query(sqlString, argList.toArray(), BeanPropertyRowMapper.newInstance(SysOrgXmDto.class));

		return new PageImpl<>(pageList, null, totalCnt);
	}

	@Override
	public List<SysOrgXmDto> listByType(String orgType) throws Exception {
		List<Object> args = Lists.newArrayList();
		StringBuffer sql = new StringBuffer(" select * from sys_org where beenDeleted=? ");
		args.add(BakDeleteModel.NO_DELETED);
		if (StringUtils.isNotEmpty(orgType)) {
			sql.append(" and extend12=? ");
			args.add(orgType);
		}
		sql.append(" ORDER BY order_no ");
		return jdbcTemplate.query(sql.toString(), args.toArray(), BeanPropertyRowMapper.newInstance(SysOrgXmDto.class));
	}

	@Override
	public List<IdNameDto> getNamesByIds(List<String> ids) throws Exception {
		if (CollectionUtils.isEmpty(ids)) {
			return null;
		}

		String str = "";
		for (int i = 0; i < ids.size(); i++) {
			if (i == 0) {
				str += "?";
			} else {
				str += ",?";
			}
		}

		String sql = "select id,org_name name from sys_org where id in(" + str + ")";

		return jdbcTemplate.query(sql.toString(), ids.toArray(), BeanPropertyRowMapper.newInstance(IdNameDto.class));
	}

	@Override
	public List<IdNameDto> getIdsByNames(List<String> names) throws Exception {
		if (CollectionUtils.isEmpty(names)) {
			return null;
		}

		String str = "";
		for (int i = 0; i < names.size(); i++) {
			if (i == 0) {
				str += "?";
			} else {
				str += ",?";
			}
		}

		// 重名的取最后修改时间最新的
		String sql = "select id,org_name name  from (select * from sys_org ORDER BY lastChangeTime desc) a where org_name in(" + str + ") AND beenDeleted=0 GROUP BY org_name";

		return jdbcTemplate.query(sql.toString(), names.toArray(), BeanPropertyRowMapper.newInstance(IdNameDto.class));
	}

	@Override
	public boolean isCodeExists(String code, String id) throws Exception {
		if (StringUtils.isEmpty(code)) {
			return false;
		}

		List<Object> args = Lists.newArrayList();

		StringBuffer sql = new StringBuffer();
		sql.append(" select count(1) from sys_org a ");
		sql.append(" where a.beenDeleted=? and (a.id=? or a.org_cid=? or a.org_code=?) ");
		args.add(BakDeleteModel.NO_DELETED);
		args.add(code);
		args.add(code);
		args.add(code);
		if (StringUtils.isNotEmpty(id)) { // 如果是更新，则去掉本身
			sql.append(" and a.id <>? ");
			args.add(id);
		} else { // 如果是新增，则要注意已删除的记录的id不得和现code重复，因为现code将会被复制当做id保存，届时会id冲突
			sql.append(" or a.beenDeleted=? and a.id=? ");
			args.add(BakDeleteModel.DELETED);
			args.add(code);
		}

		Integer count = jdbcTemplate.queryForObject(sql.toString(), args.toArray(), Integer.class);

		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean hasChild(String id) throws Exception {
		if (StringUtils.isEmpty(id)) {
			return false;
		}

		String sql = "select count(1) from sys_org a where a.beenDeleted=? and a.pid=?";
		List<Object> args = Lists.newArrayList();
		args.add(BakDeleteModel.NO_DELETED);
		args.add(id);

		Integer count = jdbcTemplate.queryForObject(sql, args.toArray(), Integer.class);

		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}
}
