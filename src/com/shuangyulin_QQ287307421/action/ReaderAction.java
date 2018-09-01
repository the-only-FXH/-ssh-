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
import com.shuangyulin_QQ287307421.dao.ReaderDAO;
import com.shuangyulin_QQ287307421.domain.Reader;
import com.shuangyulin_QQ287307421.dao.ReaderTypeDAO;
import com.shuangyulin_QQ287307421.domain.ReaderType;
@Controller @Scope("prototype")
public class ReaderAction extends ActionSupport {

/*ͼƬ�ֶ�photo��������*/
	 private File photoFile;
	 private String photoFileFileName;
	 private String photoFileContentType;
	 public File getPhotoFile() {
		return photoFile;
	}
	public void setPhotoFile(File photoFile) {
		this.photoFile = photoFile;
	}
	public String getPhotoFileFileName() {
		return photoFileFileName;
	}
	public void setPhotoFileFileName(String photoFileFileName) {
		this.photoFileFileName = photoFileFileName;
	}
	public String getPhotoFileContentType() {
		return photoFileContentType;
	}
	public void setPhotoFileContentType(String photoFileContentType) {
		this.photoFileContentType = photoFileContentType;
	}
    /*�������Ҫ��ѯ������: ���߱��*/
    private String readerNo;
    public void setReaderNo(String readerNo) {
        this.readerNo = readerNo;
    }
    public String getReaderNo() {
        return this.readerNo;
    }

    /*�������Ҫ��ѯ������: ��������*/
    private ReaderType readerType;
    public void setReaderType(ReaderType readerType) {
        this.readerType = readerType;
    }
    public ReaderType getReaderType() {
        return this.readerType;
    }

    /*�������Ҫ��ѯ������: ����*/
    private String readerName;
    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }
    public String getReaderName() {
        return this.readerName;
    }

    /*�������Ҫ��ѯ������: ��������*/
    private String birthday;
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public String getBirthday() {
        return this.birthday;
    }

    /*�������Ҫ��ѯ������: ��ϵ�绰*/
    private String telephone;
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getTelephone() {
        return this.telephone;
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
    @Resource ReaderDAO readerDAO;

    @Resource ReaderTypeDAO readerTypeDAO;
    /*��������Reader����*/
    private Reader reader;
    public void setReader(Reader reader) {
        this.reader = reader;
    }
    public Reader getReader() {
        return this.reader;
    }

    /*��ת�����Reader��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*��ѯ���е�ReaderType��Ϣ*/
        List<ReaderType> readerTypeList = readerTypeDAO.QueryAllReaderTypeInfo();
        ctx.put("readerTypeList", readerTypeList);
        return "add_view";
    }

    /*���Reader��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddReader() {
        ActionContext ctx = ActionContext.getContext();
        /*��֤���߱���Ƿ��Ѿ�����*/
        String readerNo = reader.getReaderNo();
        Reader db_reader = readerDAO.GetReaderByReaderNo(readerNo);
        if(null != db_reader) {
            ctx.put("error",  java.net.URLEncoder.encode("�ö��߱���Ѿ�����!"));
            return "error";
        }
        try {
            if(true) {
            ReaderType readerType = readerTypeDAO.GetReaderTypeByReaderTypeId(reader.getReaderType().getReaderTypeId());
            reader.setReaderType(readerType);
            }
            String path = ServletActionContext.getServletContext().getRealPath("/upload"); 
            /*����ͼƬ�ϴ�*/
            String photoFileName = ""; 
       	 	if(photoFile != null) {
       	 		InputStream is = new FileInputStream(photoFile);
       			String fileContentType = this.getPhotoFileContentType();
       			if(fileContentType.equals("image/jpeg")  || fileContentType.equals("image/pjpeg"))
       				photoFileName = UUID.randomUUID().toString() +  ".jpg";
       			else if(fileContentType.equals("image/gif"))
       				photoFileName = UUID.randomUUID().toString() +  ".gif";
       			else {
       				ctx.put("error",  java.net.URLEncoder.encode("�ϴ�ͼƬ��ʽ����ȷ!"));
       				return "error";
       			}
       			File file = new File(path, photoFileName);
       			OutputStream os = new FileOutputStream(file);
       			byte[] b = new byte[1024];
       			int bs = 0;
       			while ((bs = is.read(b)) > 0) {
       				os.write(b, 0, bs);
       			}
       			is.close();
       			os.close();
       	 	}
            if(photoFile != null)
            	reader.setPhoto("upload/" + photoFileName);
            else
            	reader.setPhoto("upload/NoImage.jpg");
            readerDAO.AddReader(reader);
            ctx.put("message",  java.net.URLEncoder.encode("Reader��ӳɹ�!"));
            return "add_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Reader���ʧ��!"));
            return "error";
        }
    }

    /*��ѯReader��Ϣ*/
    public String QueryReader() {
        if(currentPage == 0) currentPage = 1;
        if(readerNo == null) readerNo = "";
        if(readerName == null) readerName = "";
        if(birthday == null) birthday = "";
        if(telephone == null) telephone = "";
        List<Reader> readerList = readerDAO.QueryReaderInfo(readerNo, readerType, readerName, birthday, telephone, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        readerDAO.CalculateTotalPageAndRecordNumber(readerNo, readerType, readerName, birthday, telephone);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = readerDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = readerDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("readerList",  readerList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("readerNo", readerNo);
        ctx.put("readerType", readerType);
        List<ReaderType> readerTypeList = readerTypeDAO.QueryAllReaderTypeInfo();
        ctx.put("readerTypeList", readerTypeList);
        ctx.put("readerName", readerName);
        ctx.put("birthday", birthday);
        ctx.put("telephone", telephone);
        return "query_view";
    }

    /*��̨������excel*/
    public String QueryReaderOutputToExcel() { 
        if(readerNo == null) readerNo = "";
        if(readerName == null) readerName = "";
        if(birthday == null) birthday = "";
        if(telephone == null) telephone = "";
        List<Reader> readerList = readerDAO.QueryReaderInfo(readerNo,readerType,readerName,birthday,telephone);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Reader��Ϣ��¼"; 
        String[] headers = { "���߱��","��������","����","�Ա�","��������","��ϵ�绰","��ϵqq","���ߵ�ַ","����ͷ��"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<readerList.size();i++) {
        	Reader reader = readerList.get(i); 
        	dataset.add(new String[]{reader.getReaderNo(),reader.getReaderType().getReaderTypeName(),
reader.getReaderName(),reader.getSex(),reader.getBirthday(),reader.getTelephone(),reader.getQq(),reader.getAddress(),reader.getPhoto()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Reader.xls");//filename�����ص�xls���������������Ӣ�� 
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
    /*ǰ̨��ѯReader��Ϣ*/
    public String FrontQueryReader() {
        if(currentPage == 0) currentPage = 1;
        if(readerNo == null) readerNo = "";
        if(readerName == null) readerName = "";
        if(birthday == null) birthday = "";
        if(telephone == null) telephone = "";
        List<Reader> readerList = readerDAO.QueryReaderInfo(readerNo, readerType, readerName, birthday, telephone, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        readerDAO.CalculateTotalPageAndRecordNumber(readerNo, readerType, readerName, birthday, telephone);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = readerDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = readerDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("readerList",  readerList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("readerNo", readerNo);
        ctx.put("readerType", readerType);
        List<ReaderType> readerTypeList = readerTypeDAO.QueryAllReaderTypeInfo();
        ctx.put("readerTypeList", readerTypeList);
        ctx.put("readerName", readerName);
        ctx.put("birthday", birthday);
        ctx.put("telephone", telephone);
        return "front_query_view";
    }

    /*��ѯҪ�޸ĵ�Reader��Ϣ*/
    public String ModifyReaderQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������readerNo��ȡReader����*/
        Reader reader = readerDAO.GetReaderByReaderNo(readerNo);

        List<ReaderType> readerTypeList = readerTypeDAO.QueryAllReaderTypeInfo();
        ctx.put("readerTypeList", readerTypeList);
        ctx.put("reader",  reader);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�Reader��Ϣ*/
    public String FrontShowReaderQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������readerNo��ȡReader����*/
        Reader reader = readerDAO.GetReaderByReaderNo(readerNo);

        List<ReaderType> readerTypeList = readerTypeDAO.QueryAllReaderTypeInfo();
        ctx.put("readerTypeList", readerTypeList);
        ctx.put("reader",  reader);
        return "front_show_view";
    }

    /*�����޸�Reader��Ϣ*/
    public String ModifyReader() {
        ActionContext ctx = ActionContext.getContext();
        try {
            if(true) {
            ReaderType readerType = readerTypeDAO.GetReaderTypeByReaderTypeId(reader.getReaderType().getReaderTypeId());
            reader.setReaderType(readerType);
            }
            String path = ServletActionContext.getServletContext().getRealPath("/upload"); 
            /*����ͼƬ�ϴ�*/
            String photoFileName = ""; 
       	 	if(photoFile != null) {
       	 		InputStream is = new FileInputStream(photoFile);
       			String fileContentType = this.getPhotoFileContentType();
       			if(fileContentType.equals("image/jpeg") || fileContentType.equals("image/pjpeg"))
       				photoFileName = UUID.randomUUID().toString() +  ".jpg";
       			else if(fileContentType.equals("image/gif"))
       				photoFileName = UUID.randomUUID().toString() +  ".gif";
       			else {
       				ctx.put("error",  java.net.URLEncoder.encode("�ϴ�ͼƬ��ʽ����ȷ!"));
       				return "error";
       			}
       			File file = new File(path, photoFileName);
       			OutputStream os = new FileOutputStream(file);
       			byte[] b = new byte[1024];
       			int bs = 0;
       			while ((bs = is.read(b)) > 0) {
       				os.write(b, 0, bs);
       			}
       			is.close();
       			os.close();
            reader.setPhoto("upload/" + photoFileName);
       	 	}
            readerDAO.UpdateReader(reader);
            ctx.put("message",  java.net.URLEncoder.encode("Reader��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Reader��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��Reader��Ϣ*/
    public String DeleteReader() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            readerDAO.DeleteReader(readerNo);
            ctx.put("message",  java.net.URLEncoder.encode("Readerɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Readerɾ��ʧ��!"));
            return "error";
        }
    }

}
