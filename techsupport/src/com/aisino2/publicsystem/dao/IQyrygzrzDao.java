package com.aisino2.publicsystem.dao;

import java.util.Map;

import com.aisino2.core.dao.Page;
import com.aisino2.publicsystem.domain.Qyrygzrz;
public interface IQyrygzrzDao {
	/** @param 企业人员工作日志(t_qyrygzrz) 增加 */
	Qyrygzrz insertQyrygzrz(Qyrygzrz qyrygzrz);

	/** @param 企业人员工作日志(t_qyrygzrz) 删除 */
	int deleteQyrygzrz(Qyrygzrz qyrygzrz);

	/** @param 企业人员工作日志(t_qyrygzrz) 修改 */
	int updateQyrygzrz(Qyrygzrz qyrygzrz);

	/** @param 企业人员工作日志(t_qyrygzrz) 查询单条*/
	Qyrygzrz getQyrygzrz(Qyrygzrz qyrygzrz);

	/** @param 企业人员工作日志(t_qyrygzrz) 分页查询  企业端查询*/
	Page getListForPage(Map map, int pageNo,int pageSize,String sort,String desc);
	
	/** @param 企业人员工作日志(t_qyrygzrz) 分页查询  公安端查询*/
	Page getListForPageGad(Map map, int pageNo,int pageSize,String sort,String desc);

	/** @param 企业人员工作日志(t_qyrygzrz) 多条查询 */
	List getListQyrygzrz(Qyrygzrz qyrygzrz);
}