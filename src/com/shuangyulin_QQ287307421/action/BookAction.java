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
import com.shuangyulin_QQ287307421.dao.BookDAO;
import com.shuangyulin_QQ287307421.domain.Book;
import com.shuangyulin_QQ287307421.dao.BookTypeDAO;
import com.shuangyulin_QQ287307421.domain.BookType;

@Controller @Scope("prototype")
public class BookAction extends ActionSupport {

/*ͼƬ�ֶ�bookPhoto��������*/
	 private File bookPhotoFile;
	 private String bookPhotoFileFileName;
	 private String bookPhotoFileContentType;
	 public File getBookPhotoFile() {
		return bookPhotoFile;
	}
	public void setBookPhotoFile(File bookPhotoFile) {
		this.bookPhotoFile = bookPhotoFile;
	}
	public String getBookPhotoFileFileName() {
		return bookPhotoFileFileName;
	}
	public void setBookPhotoFileFileName(String bookPhotoFileFileName) {
		this.bookPhotoFileFileName = bookPhotoFileFileName;
	}
	public String getBookPhotoFileContentType() {
		return bookPhotoFileContentType;
	}
	public void setBookPhotoFileContentType(String bookPhotoFileContentType) {
		this.bookPhotoFileContentType = bookPhotoFileContentType;
	}
    /*�������Ҫ��ѯ������: ͼ��������*/
    private String barcode;
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
    public String getBarcode() {
        return this.barcode;
    }

    /*�������Ҫ��ѯ������: ͼ������*/
    private String bookName;
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
    public String getBookName() {
        return this.bookName;
    }

    /*�������Ҫ��ѯ������: ͼ���������*/
    private BookType bookType;
    public void setBookType(BookType bookType) {
        this.bookType = bookType;
    }
    public BookType getBookType() {
        return this.bookType;
    }

    /*�������Ҫ��ѯ������: ��������*/
    private String publishDate;
    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }
    public String getPublishDate() {
        return this.publishDate;
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

    /*��ǰ��ѯ���ܼ�¼��Ŀ*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*ҵ������*/
    @Resource BookDAO bookDAO;

    @Resource BookTypeDAO bookTypeDAO;
    /*��������Book����*/
    private Book book;
    public void setBook(Book book) {
        this.book = book;
    }
    public Book getBook() {
        return this.book;
    }

    /*��ת�����Book��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*��ѯ���е�BookType��Ϣ*/
        List<BookType> bookTypeList = bookTypeDAO.QueryAllBookTypeInfo();
        ctx.put("bookTypeList", bookTypeList);
        return "add_view";
    }

    /*���Book��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddBook() {
        ActionContext ctx = ActionContext.getContext();
        /*��֤ͼ���������Ƿ��Ѿ�����*/
        String barcode = book.getBarcode();
        Book db_book = bookDAO.GetBookByBarcode(barcode);
        if(null != db_book) {
            ctx.put("error",  java.net.URLEncoder.encode("��ͼ���������Ѿ�����!"));
            return "error";
        }
        try {
            if(true) {
            BookType bookType = bookTypeDAO.GetBookTypeByBookTypeId(book.getBookType().getBookTypeId());
            book.setBookType(bookType);
            }
            String path = ServletActionContext.getServletContext().getRealPath("/upload"); 
            /*����ͼƬ�ϴ�*/
            String bookPhotoFileName = ""; 
       	 	if(bookPhotoFile != null) {
       	 		InputStream is = new FileInputStream(bookPhotoFile);
       			String fileContentType = this.getBookPhotoFileContentType();
       			if(fileContentType.equals("image/jpeg")  || fileContentType.equals("image/pjpeg"))
       				bookPhotoFileName = UUID.randomUUID().toString() +  ".jpg";
       			else if(fileContentType.equals("image/gif"))
       				bookPhotoFileName = UUID.randomUUID().toString() +  ".gif";
       			else {
       				ctx.put("error",  java.net.URLEncoder.encode("�ϴ�ͼƬ��ʽ����ȷ!"));
       				return "error";
       			}
       			File file = new File(path, bookPhotoFileName);
       			OutputStream os = new FileOutputStream(file);
       			byte[] b = new byte[1024];
       			int bs = 0;
       			while ((bs = is.read(b)) > 0) {
       				os.write(b, 0, bs);
       			}
       			is.close();
       			os.close();
       	 	}
            if(bookPhotoFile != null)
            	book.setBookPhoto("upload/" + bookPhotoFileName);
            else
            	book.setBookPhoto("upload/NoImage.jpg");
            bookDAO.AddBook(book);
            ctx.put("message",  java.net.URLEncoder.encode("Book��ӳɹ�!"));
            return "add_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Book���ʧ��!"));
            return "error";
        }
    }

    /*��ѯBook��Ϣ*/
    public String QueryBook() {
        if(currentPage == 0) currentPage = 1;
        if(barcode == null) barcode = "";
        if(bookName == null) bookName = "";
        if(publishDate == null) publishDate = "";
        List<Book> bookList = bookDAO.QueryBookInfo(barcode, bookName, bookType, publishDate, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        bookDAO.CalculateTotalPageAndRecordNumber(barcode, bookName, bookType, publishDate);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = bookDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = bookDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("bookList",  bookList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("barcode", barcode);
        ctx.put("bookName", bookName);
        ctx.put("bookType", bookType);
        List<BookType> bookTypeList = bookTypeDAO.QueryAllBookTypeInfo();
        ctx.put("bookTypeList", bookTypeList);
        ctx.put("publishDate", publishDate);
        return "query_view";
    }

    /*��̨������excel*/
    public String QueryBookOutputToExcel() { 
        if(barcode == null) barcode = "";
        if(bookName == null) bookName = "";
        if(publishDate == null) publishDate = "";
        List<Book> bookList = bookDAO.QueryBookInfo(barcode,bookName,bookType,publishDate);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Book��Ϣ��¼"; 
        String[] headers = { "ͼ��������","ͼ������","ͼ���������","ͼ��۸�","���","��������","������","ͼ��ͼƬ"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<bookList.size();i++) {
        	Book book = bookList.get(i); 
        	dataset.add(new String[]{book.getBarcode(),book.getBookName(),book.getBookType().getBookTypeName(),
book.getPrice() + "",book.getCount() + "",book.getPublishDate(),book.getPublish(),book.getBookPhoto()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Book.xls");//filename�����ص�xls���������������Ӣ�� 
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
    /*ǰ̨��ѯBook��Ϣ*/
    public String FrontQueryBook() {
        if(currentPage == 0) currentPage = 1;
        if(barcode == null) barcode = "";
        if(bookName == null) bookName = "";
        if(publishDate == null) publishDate = "";
        List<Book> bookList = bookDAO.QueryBookInfo(barcode, bookName, bookType, publishDate, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        bookDAO.CalculateTotalPageAndRecordNumber(barcode, bookName, bookType, publishDate);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = bookDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = bookDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("bookList",  bookList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("barcode", barcode);
        ctx.put("bookName", bookName);
        ctx.put("bookType", bookType);
        List<BookType> bookTypeList = bookTypeDAO.QueryAllBookTypeInfo();
        ctx.put("bookTypeList", bookTypeList);
        ctx.put("publishDate", publishDate);
        return "front_query_view";
    }

    /*��ѯҪ�޸ĵ�Book��Ϣ*/
    public String ModifyBookQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������barcode��ȡBook����*/
        Book book = bookDAO.GetBookByBarcode(barcode);

        List<BookType> bookTypeList = bookTypeDAO.QueryAllBookTypeInfo();
        ctx.put("bookTypeList", bookTypeList);
        ctx.put("book",  book);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�Book��Ϣ*/
    public String FrontShowBookQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������barcode��ȡBook����*/
        Book book = bookDAO.GetBookByBarcode(barcode);

        List<BookType> bookTypeList = bookTypeDAO.QueryAllBookTypeInfo();
        ctx.put("bookTypeList", bookTypeList);
        ctx.put("book",  book);
        return "front_show_view";
    }

    /*�����޸�Book��Ϣ*/
    public String ModifyBook() {
        ActionContext ctx = ActionContext.getContext();
        try {
            if(true) {
            BookType bookType = bookTypeDAO.GetBookTypeByBookTypeId(book.getBookType().getBookTypeId());
            book.setBookType(bookType);
            }
            String path = ServletActionContext.getServletContext().getRealPath("/upload"); 
            /*����ͼƬ�ϴ�*/
            String bookPhotoFileName = ""; 
       	 	if(bookPhotoFile != null) {
       	 		InputStream is = new FileInputStream(bookPhotoFile);
       			String fileContentType = this.getBookPhotoFileContentType();
       			if(fileContentType.equals("image/jpeg") || fileContentType.equals("image/pjpeg"))
       				bookPhotoFileName = UUID.randomUUID().toString() +  ".jpg";
       			else if(fileContentType.equals("image/gif"))
       				bookPhotoFileName = UUID.randomUUID().toString() +  ".gif";
       			else {
       				ctx.put("error",  java.net.URLEncoder.encode("�ϴ�ͼƬ��ʽ����ȷ!"));
       				return "error";
       			}
       			File file = new File(path, bookPhotoFileName);
       			OutputStream os = new FileOutputStream(file);
       			byte[] b = new byte[1024];
       			int bs = 0;
       			while ((bs = is.read(b)) > 0) {
       				os.write(b, 0, bs);
       			}
       			is.close();
       			os.close();
            book.setBookPhoto("upload/" + bookPhotoFileName);
       	 	}
            bookDAO.UpdateBook(book);
            ctx.put("message",  java.net.URLEncoder.encode("Book��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Book��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��Book��Ϣ*/
    public String DeleteBook() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            bookDAO.DeleteBook(barcode);
            ctx.put("message",  java.net.URLEncoder.encode("Bookɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Bookɾ��ʧ��!"));
            return "error";
        }
    }

}
