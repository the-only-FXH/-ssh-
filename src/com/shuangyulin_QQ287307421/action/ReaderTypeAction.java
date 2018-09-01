package com.shuangyulin_QQ287307421.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.shuangyulin_QQ287307421.utils.ExportExcelUtil;
import com.shuangyulin_QQ287307421.dao.ReaderTypeDAO;
import com.shuangyulin_QQ287307421.domain.ReaderType;
@Controller @Scope("prototype")
public class ReaderTypeAction extends ActionSupport {

    /*当前第几页*/
    private int currentPage;
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public int getCurrentPage() {
        return currentPage;
    }

    /*一共多少页*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    private int readerTypeId;
    public void setReaderTypeId(int readerTypeId) {
        this.readerTypeId = readerTypeId;
    }
    public int getReaderTypeId() {
        return readerTypeId;
    }

    /*当前查询的总记录数目*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*业务层对象*/
    @Resource ReaderTypeDAO readerTypeDAO;

    /*待操作的ReaderType对象*/
    private ReaderType readerType;
    public void setReaderType(ReaderType readerType) {
        this.readerType = readerType;
    }
    public ReaderType getReaderType() {
        return this.readerType;
    }

    /*跳转到添加ReaderType视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        return "add_view";
    }

    /*添加ReaderType信息*/
    @SuppressWarnings("deprecation")
    public String AddReaderType() {
        ActionContext ctx = ActionContext.getContext();
        try {
            readerTypeDAO.AddReaderType(readerType);
            ctx.put("message",  java.net.URLEncoder.encode("ReaderType添加成功!"));
            return "add_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("ReaderType添加失败!"));
            return "error";
        }
    }

    /*查询ReaderType信息*/
    public String QueryReaderType() {
        if(currentPage == 0) currentPage = 1;
        List<ReaderType> readerTypeList = readerTypeDAO.QueryReaderTypeInfo(currentPage);
        /*计算总的页数和总的记录数*/
        readerTypeDAO.CalculateTotalPageAndRecordNumber();
        /*获取到总的页码数目*/
        totalPage = readerTypeDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = readerTypeDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("readerTypeList",  readerTypeList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        return "query_view";
    }

    /*后台导出到excel*/
    public String QueryReaderTypeOutputToExcel() { 
        List<ReaderType> readerTypeList = readerTypeDAO.QueryReaderTypeInfo();
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "ReaderType信息记录"; 
        String[] headers = { "读者类型编号","读者类型","可借阅数目"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<readerTypeList.size();i++) {
        	ReaderType readerType = readerTypeList.get(i); 
        	dataset.add(new String[]{readerType.getReaderTypeId() + "",readerType.getReaderTypeName(),readerType.getNumber() + ""});
        }
        /*
        OutputStream out = null;
		try {
			out = new FileOutputStream("C://output.xls");
			ex.exportExcel(title,headers, dataset, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		HttpServletResponse response = null;//创建一个HttpServletResponse对象 
		OutputStream out = null;//创建一个输出流对象 
		try { 
			response = ServletActionContext.getResponse();//初始化HttpServletResponse对象 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"ReaderType.xls");//filename是下载的xls的名，建议最好用英文 
			response.setContentType("application/msexcel;charset=UTF-8");//设置类型 
			response.setHeader("Pragma","No-cache");//设置头 
			response.setHeader("Cache-Control","no-cache");//设置头 
			response.setDateHeader("Expires", 0);//设置日期头  
			String rootPath = ServletActionContext.getServletContext().getRealPath("/");
			ex.exportExcel(rootPath,title,headers, dataset, out);
			out.flush();
		} catch (IOException e) { 
			e.printStackTrace(); 
		}finally{
			try{
				if(out!=null){ 
					out.close(); 
				}
			}catch(IOException e){ 
				e.printStackTrace(); 
			} 
		}
		return null;
    }
    /*前台查询ReaderType信息*/
    public String FrontQueryReaderType() {
        if(currentPage == 0) currentPage = 1;
        List<ReaderType> readerTypeList = readerTypeDAO.QueryReaderTypeInfo(currentPage);
        /*计算总的页数和总的记录数*/
        readerTypeDAO.CalculateTotalPageAndRecordNumber();
        /*获取到总的页码数目*/
        totalPage = readerTypeDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = readerTypeDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("readerTypeList",  readerTypeList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        return "front_query_view";
    }

    /*查询要修改的ReaderType信息*/
    public String ModifyReaderTypeQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键readerTypeId获取ReaderType对象*/
        ReaderType readerType = readerTypeDAO.GetReaderTypeByReaderTypeId(readerTypeId);

        ctx.put("readerType",  readerType);
        return "modify_view";
    }

    /*查询要修改的ReaderType信息*/
    public String FrontShowReaderTypeQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键readerTypeId获取ReaderType对象*/
        ReaderType readerType = readerTypeDAO.GetReaderTypeByReaderTypeId(readerTypeId);

        ctx.put("readerType",  readerType);
        return "front_show_view";
    }

    /*更新修改ReaderType信息*/
    public String ModifyReaderType() {
        ActionContext ctx = ActionContext.getContext();
        try {
            readerTypeDAO.UpdateReaderType(readerType);
            ctx.put("message",  java.net.URLEncoder.encode("ReaderType信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("ReaderType信息更新失败!"));
            return "error";
       }
   }

    /*删除ReaderType信息*/
    public String DeleteReaderType() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            readerTypeDAO.DeleteReaderType(readerTypeId);
            ctx.put("message",  java.net.URLEncoder.encode("ReaderType删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("ReaderType删除失败!"));
            return "error";
        }
    }

}
