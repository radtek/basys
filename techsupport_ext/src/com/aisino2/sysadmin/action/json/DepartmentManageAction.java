package com.aisino2.sysadmin.action.json;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.aisino2.sysadmin.action.PageAction;
import com.aisino2.sysadmin.domain.Department;
import com.aisino2.sysadmin.service.IDepartmentService;
import com.aisino2.sysadmin.tree.TreeNodeTool;

@Component
@Scope("prototype")
public class DepartmentManageAction extends PageAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8963651412037411136L;
	private IDepartmentService department_service;
	private Department department;
	private List<Department> department_list;
	private String department_treenode_list;
	private TreeNodeTool tree_node_tool;
	private static final Logger log = Logger.getLogger(DepartmentManageAction.class);
	@Resource(name = "treeNodeTool")
	public void setTree_node_tool(TreeNodeTool tree_node_tool) {
		this.tree_node_tool = tree_node_tool;
	}

	/**
	 * 机构查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public String querylist() throws Exception {
		try {
			List page_list = department_service.getListForPage(department,
					this.start, this.limit, this.dir, this.sort);
			this.total = (Integer) page_list.get(0);
			department_list = (List<Department>) page_list.get(1);

		} catch (Exception e) {
			this.returnNo = 1;
			this.returnMessage = "获取列表发生错误";
			if(log.isDebugEnabled()){
				log.debug(e,e.fillInStackTrace());
				this.returnMessageDebug = e.getMessage() +"\n";
			}
		}

		return SUCCESS;
	}

	/**
	 * 查询机构树形节点
	 * 
	 * @return
	 * @throws Exception
	 */
	public String query_department_node() throws Exception {
		if (department == null)
			department = new Department();
		Integer departlevel = department.getDepartlevel();
		if(department.getDepartid()!=null && department.getDepartid() ==0)
			department.setDepartid(null);
		List<Department> child_depart_list = this.department_service
				.getChildDepart(department,departlevel);
		try{
			department = this.department_service.getDepartment(department);
		}catch (Exception e) {
			department = null;
		}
		StringBuffer buff = new StringBuffer();
		department_treenode_list = "["+tree_node_tool.make_ext_tree_node(tree_node_tool
				.parse_to_tree_node_from_department(child_depart_list,
						department, false,departlevel),buff).toString()+"]";
		this.response.setContentType("text/html; charset=utf-8");
		PrintWriter out = this.response.getWriter();
		out.print(department_treenode_list);
		out.close();
		return null;
	}

	/**
	 * 查询单个机构
	 * 
	 * @return
	 * @throws Exception
	 */
	public String query() throws Exception {
		try{
			department = department_service.getDepartment(department);
		}catch (Exception e) {
			returnNo = 1;
			returnMessage = "详情获取错误";
			
			log.error(e);
			if(log.isDebugEnabled()){
				log.debug(e,e.fillInStackTrace());
				this.returnMessageDebug = e.getMessage() +"\n";
			}
		}
		
		return SUCCESS;
	}

	public String getDepartment_treenode_list() {
		return department_treenode_list;
	}

	public void setDepartment_treenode_list(String department_treenode_list) {
		this.department_treenode_list = department_treenode_list;
	}

	public String add() throws Exception {
		try{
			if(department==null)
				throw new RuntimeException("需要添加的机构实体为空");
			this.department_service.insertDepartment(department);
			returnNo = 0;
			returnMessage="添加成功";
		}catch (Exception e) {
			this.returnNo = 1;
			this.returnMessage = "添加发生错误";
			log.error(e);
			if(log.isDebugEnabled()){
				log.debug(e,e.fillInStackTrace());
				this.returnMessageDebug = e.getMessage() +"\n";
			}
		}
			
		return SUCCESS;
	}

	public String remove() throws Exception {
		try{
			if(department_list==null || department_list.isEmpty())
				throw new RuntimeException("未选择需要删除的机构");
			this.returnMessageDebug="";
			List<Department> removedList = new ArrayList<Department>();
			for(Department d : department_list){
				try{
					this.department_service.deleteDepartment(d);
					removedList.add(d);
				}
				catch (Exception e) {
					log.error(e);
					this.returnMessageDebug+=e.getMessage()+"\n"; 
				}
			}
			
			if(department_list.size() != removedList.size() )
				throw new RuntimeException("未全部删除，部分无法删除");
			this.returnNo=0;
			this.returnMessage="已删除选择的记录";
		}catch (Exception e) {
			this.returnNo=1;
			this.returnMessage="删除发生了错误";
			log.error(e);
			if(log.isDebugEnabled()){
				log.debug(e,e.fillInStackTrace());
				this.returnMessageDebug=e.getMessage()+"\n";
			}
		}
		
		return SUCCESS;
	}

	public String modify() throws Exception {
		try{
			if(department == null || department.getDepartid()==null)
				throw new RuntimeException("需要修改的机构为空");
			this.department_service.updateDepartment(department);
			returnNo=0;
			returnMessage="修改成功";
		}catch (Exception e) {
			returnNo = 1;
			returnMessage = "修改发生错误";
			log.error(e);
			if(log.isDebugEnabled()){
				log.debug(e,e.fillInStackTrace());
				this.returnMessageDebug=e.getMessage()+"\n";
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 效验机构代码是否可用
	 * @return
	 * @throws Exception
	 */
	public String check_departcode() throws Exception{
		try{
			if(department == null || department.getDepartcode() == null)
				throw new RuntimeException("机构代码为空");
			boolean result  =  this.department_service.check_departcode(department);
			if(result)
				returnNo = 0;
			else
				returnNo = 1;
		}catch (Exception e) {
			returnNo = 1;
			returnMessage = "效验机构代码发生了错误";
			log.error(e);
			if(log.isDebugEnabled()){
				log.debug(e,e.fillInStackTrace());
				returnMessageDebug = e.getMessage();
			}
		}
		return SUCCESS;
	}

	@Resource(name = "departmentServiceImpl")
	public void setDepartment_service(IDepartmentService department_service) {
		this.department_service = department_service;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public List<Department> getDepartment_list() {
		return department_list;
	}

	public void setDepartment_list(List<Department> department_list) {
		this.department_list = department_list;
	}

}
