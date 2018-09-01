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

    /*�������Ҫ��ѯ������: ͼ�����*/
    private Book book;
    public void setBook(Book book) {
        this.book = book;
    }
    public Book getBook() {
        return this.book;
    }

    /*�������Ҫ��ѯ������: ���߶���*/
    private Reader reader;
    public void setReader(Reader reader) {
        this.reader = reader;
    }
    public Reader getReader() {
        return this.reader;
    }

    /*��ǰ�ڼ�ҳ*/
    private int currentPage;
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public int getCurrentPage() {
        return currentPage;
    }

    /*һ������ҳ*/
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

    /*��ǰ��ѯ���ܼ�¼��Ŀ*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*ҵ������*/
    @Resource LoanInfoDAO loanInfoDAO;

    @Resource BookDAO bookDAO;
    @Resource ReaderDAO readerDAO;
    /*��������LoanInfo����*/
    private LoanInfo loanInfo;
    public void setLoanInfo(LoanInfo loanInfo) {
        this.loanInfo = loanInfo;
    }
    public LoanInfo getLoanInfo() {
        return this.loanInfo;
    }

    /*��ת�����LoanInfo��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*��ѯ���е�Book��Ϣ*/
        List<Book> bookList = bookDAO.QueryAllBookInfo();
        ctx.put("bookList", bookList);
        /*��ѯ���е�Reader��Ϣ*/
        List<Reader> readerList = readerDAO.QueryAllReaderInfo();
        ctx.put("readerList", readerList);
        return "add_view";
    }

    /*���LoanInfo��Ϣ*/
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
            ctx.put("message",  java.net.URLEncoder.encode("LoanInfo��ӳɹ�!"));
            return "add_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("LoanInfo���ʧ��!"));
            return "error";
        }
    }

    /*��ѯLoanInfo��Ϣ*/
    public String QueryLoanInfo() {
        if(currentPage == 0) currentPage = 1;
        List<LoanInfo> loanInfoList = loanInfoDAO.QueryLoanInfoInfo(book, reader, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        loanInfoDAO.CalculateTotalPageAndRecordNumber(book, reader);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = loanInfoDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
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

    /*��̨������excel*/
    public String QueryLoanInfoOutputToExcel() { 
        List<LoanInfo> loanInfoList = loanInfoDAO.QueryLoanInfoInfo(book,reader);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "LoanInfo��Ϣ��¼"; 
        String[] headers = { "���ı��","ͼ�����","���߶���","����ʱ��","�黹ʱ��"};
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
		HttpServletResponse response = null;//����һ��HttpServletResponse���� 
		OutputStream out = null;//����һ����������� 
		try { 
			response = ServletActionContext.getResponse();//��ʼ��HttpServletResponse���� 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"LoanInfo.xls");//filename�����ص�xls���������������Ӣ�� 
			response.setContentType("application/msexcel;charset=UTF-8");//�������� 
			response.setHeader("Pragma","No-cache");//����ͷ 
			response.setHeader("Cache-Control","no-cache");//����ͷ 
			response.setDateHeader("Expires", 0);//��������ͷ  
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
    /*ǰ̨��ѯLoanInfo��Ϣ*/
    public String FrontQueryLoanInfo() {
        if(currentPage == 0) currentPage = 1;
        List<LoanInfo> loanInfoList = loanInfoDAO.QueryLoanInfoInfo(book, reader, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        loanInfoDAO.CalculateTotalPageAndRecordNumber(book, reader);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = loanInfoDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
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

    /*��ѯҪ�޸ĵ�LoanInfo��Ϣ*/
    public String ModifyLoanInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������loadId��ȡLoanInfo����*/
        LoanInfo loanInfo = loanInfoDAO.GetLoanInfoByLoadId(loadId);

        List<Book> bookList = bookDAO.QueryAllBookInfo();
        ctx.put("bookList", bookList);
        List<Reader> readerList = readerDAO.QueryAllReaderInfo();
        ctx.put("readerList", readerList);
        ctx.put("loanInfo",  loanInfo);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�LoanInfo��Ϣ*/
    public String FrontShowLoanInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������loadId��ȡLoanInfo����*/
        LoanInfo loanInfo = loanInfoDAO.GetLoanInfoByLoadId(loadId);

        List<Book> bookList = bookDAO.QueryAllBookInfo();
        ctx.put("bookList", bookList);
        List<Reader> readerList = readerDAO.QueryAllReaderInfo();
        ctx.put("readerList", readerList);
        ctx.put("loanInfo",  loanInfo);
        return "front_show_view";
    }

    /*�����޸�LoanInfo��Ϣ*/
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
            ctx.put("message",  java.net.URLEncoder.encode("LoanInfo��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("LoanInfo��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��LoanInfo��Ϣ*/
    public String DeleteLoanInfo() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            loanInfoDAO.DeleteLoanInfo(loadId);
            ctx.put("message",  java.net.URLEncoder.encode("LoanInfoɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("LoanInfoɾ��ʧ��!"));
            return "error";
        }
    }

}
