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
import com.shuangyulin_QQ287307421.dao.LoanInfoDAO;
import com.shuangyulin_QQ287307421.domain.LoanInfo;
import com.shuangyulin_QQ287307421.dao.BookDAO;
import com.shuangyulin_QQ287307421.domain.Book;
import com.shuangyulin_QQ287307421.dao.ReaderDAO;
import com.shuangyulin_QQ287307421.domain.Reader;
@Controller @Scope("prototype")
public class LoanInfoAction extends ActionSupport {

    /*界面层需要查询的属性: 图书对象*/
    private Book book;
    public void setBook(Book book) {
        this.book = book;
    }
    public Book getBook() {
        return this.book;
    }

    /*界面层需要查询的属性: 读者对象*/
    private Reader reader;
    public void setReader(Reader reader) {
        this.reader = reader;
    }
    public Reader getReader() {
        return this.reader;
    }

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

    private int loadId;
    public void setLoadId(int loadId) {
        this.loadId = loadId;
    }
    public int getLoadId() {
        return loadId;
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
    @Resource LoanInfoDAO loanInfoDAO;

    @Resource BookDAO bookDAO;
    @Resource ReaderDAO readerDAO;
    /*待操作的LoanInfo对象*/
    private LoanInfo loanInfo;
    public void setLoanInfo(LoanInfo loanInfo) {
        this.loanInfo = loanInfo;
    }
    public LoanInfo getLoanInfo() {
        return this.loanInfo;
    }

    /*跳转到添加LoanInfo视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*查询所有的Book信息*/
        List<Book> bookList = bookDAO.QueryAllBookInfo();
        ctx.put("bookList", bookList);
        /*查询所有的Reader信息*/
        List<Reader> readerList = readerDAO.QueryAllReaderInfo();
        ctx.put("readerList", readerList);
        return "add_view";
    }

    /*添加LoanInfo信息*/
    @SuppressWarnings("deprecation")
    public String AddLoanInfo() {
        ActionContext ctx = ActionContext.getContext();
        try {
            if(true) {
            Book book = bookDAO.GetBookByBarcode(loanInfo.getBook().getBarcode());
            loanInfo.setBook(book);
            }
            if(true) {
            Reader reader = readerDAO.GetReaderByReaderNo(loanInfo.getReader().getReaderNo());
            loanInfo.setReader(reader);
            }
            loanInfoDAO.AddLoanInfo(loanInfo);
            ctx.put("message",  java.net.URLEncoder.encode("LoanInfo添加成功!"));
            return "add_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("LoanInfo添加失败!"));
            return "error";
        }
    }

    /*查询LoanInfo信息*/
    public String QueryLoanInfo() {
        if(currentPage == 0) currentPage = 1;
        List<LoanInfo> loanInfoList = loanInfoDAO.QueryLoanInfoInfo(book, reader, currentPage);
        /*计算总的页数和总的记录数*/
        loanInfoDAO.CalculateTotalPageAndRecordNumber(book, reader);
        /*获取到总的页码数目*/
        totalPage = loanInfoDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = loanInfoDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("loanInfoList",  loanInfoList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("book", book);
        List<Book> bookList = bookDAO.QueryAllBookInfo();
        ctx.put("bookList", bookList);
        ctx.put("reader", reader);
        List<Reader> readerList = readerDAO.QueryAllReaderInfo();
        ctx.put("readerList", readerList);
        return "query_view";
    }

    /*后台导出到excel*/
    public String QueryLoanInfoOutputToExcel() { 
        List<LoanInfo> loanInfoList = loanInfoDAO.QueryLoanInfoInfo(book,reader);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "LoanInfo信息记录"; 
        String[] headers = { "借阅编号","图书对象","读者对象","借阅时间","归还时间"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<loanInfoList.size();i++) {
        	LoanInfo loanInfo = loanInfoList.get(i); 
        	dataset.add(new String[]{loanInfo.getLoadId() + "",loanInfo.getBook().getBookName(),
loanInfo.getReader().getReaderName(),
loanInfo.getBorrowDate(),loanInfo.getReturnDate()});
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
			response.setHeader("Content-disposition","attachment; filename="+"LoanInfo.xls");//filename是下载的xls的名，建议最好用英文 
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
    /*前台查询LoanInfo信息*/
    public String FrontQueryLoanInfo() {
        if(currentPage == 0) currentPage = 1;
        List<LoanInfo> loanInfoList = loanInfoDAO.QueryLoanInfoInfo(book, reader, currentPage);
        /*计算总的页数和总的记录数*/
        loanInfoDAO.CalculateTotalPageAndRecordNumber(book, reader);
        /*获取到总的页码数目*/
        totalPage = loanInfoDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = loanInfoDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("loanInfoList",  loanInfoList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("book", book);
        List<Book> bookList = bookDAO.QueryAllBookInfo();
        ctx.put("bookList", bookList);
        ctx.put("reader", reader);
        List<Reader> readerList = readerDAO.QueryAllReaderInfo();
        ctx.put("readerList", readerList);
        return "front_query_view";
    }

    /*查询要修改的LoanInfo信息*/
    public String ModifyLoanInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键loadId获取LoanInfo对象*/
        LoanInfo loanInfo = loanInfoDAO.GetLoanInfoByLoadId(loadId);

        List<Book> bookList = bookDAO.QueryAllBookInfo();
        ctx.put("bookList", bookList);
        List<Reader> readerList = readerDAO.QueryAllReaderInfo();
        ctx.put("readerList", readerList);
        ctx.put("loanInfo",  loanInfo);
        return "modify_view";
    }

    /*查询要修改的LoanInfo信息*/
    public String FrontShowLoanInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键loadId获取LoanInfo对象*/
        LoanInfo loanInfo = loanInfoDAO.GetLoanInfoByLoadId(loadId);

        List<Book> bookList = bookDAO.QueryAllBookInfo();
        ctx.put("bookList", bookList);
        List<Reader> readerList = readerDAO.QueryAllReaderInfo();
        ctx.put("readerList", readerList);
        ctx.put("loanInfo",  loanInfo);
        return "front_show_view";
    }

    /*更新修改LoanInfo信息*/
    public String ModifyLoanInfo() {
        ActionContext ctx = ActionContext.getContext();
        try {
            if(true) {
            Book book = bookDAO.GetBookByBarcode(loanInfo.getBook().getBarcode());
            loanInfo.setBook(book);
            }
            if(true) {
            Reader reader = readerDAO.GetReaderByReaderNo(loanInfo.getReader().getReaderNo());
            loanInfo.setReader(reader);
            }
            loanInfoDAO.UpdateLoanInfo(loanInfo);
            ctx.put("message",  java.net.URLEncoder.encode("LoanInfo信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("LoanInfo信息更新失败!"));
            return "error";
       }
   }

    /*删除LoanInfo信息*/
    public String DeleteLoanInfo() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            loanInfoDAO.DeleteLoanInfo(loadId);
            ctx.put("message",  java.net.URLEncoder.encode("LoanInfo删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("LoanInfo删除失败!"));
            return "error";
        }
    }

}
