<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.shuangyulin_QQ287307421.domain.LoanInfo" %>
<%@ page import="com.shuangyulin_QQ287307421.domain.Book" %>
<%@ page import="com.shuangyulin_QQ287307421.domain.Reader" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //获取所有的book信息
    List<Book> bookList = (List<Book>)request.getAttribute("bookList");
    //获取所有的reader信息
    List<Reader> readerList = (List<Reader>)request.getAttribute("readerList");
    LoanInfo loanInfo = (LoanInfo)request.getAttribute("loanInfo");

%>
<HTML><HEAD><TITLE>查看借阅信息</TITLE>
<STYLE type=text/css>
body{margin:0px; font-size:12px; background-image:url(<%=basePath%>images/bg.jpg); background-position:bottom; background-repeat:repeat-x; background-color:#A2D5F0;}
.STYLE1 {color: #ECE9D8}
.label {font-style.:italic; }
.errorLabel {font-style.:italic;  color:red; }
.errorMessage {font-weight:bold; color:red; }
</STYLE>
 <script src="<%=basePath %>calendar.js"></script>
</HEAD>
<BODY><br/><br/>
<s:fielderror cssStyle="color:red" />
<TABLE align="center" height="100%" cellSpacing=0 cellPadding=0 width="80%" border=0>
  <TBODY>
  <TR>
    <TD align="left" vAlign=top ><s:form action="" method="post" onsubmit="return checkForm();" enctype="multipart/form-data" name="form1">
<table width='100%' cellspacing='1' cellpadding='3'  class="tablewidth">
  <tr>
    <td width=30%>借阅编号:</td>
    <td width=70%><%=loanInfo.getLoadId() %></td>
  </tr>

  <tr>
    <td width=30%>图书对象:</td>
    <td width=70%>
      <select name="loanInfo.book.barcode" disabled>
      <%
        for(Book book:bookList) {
          String selected = "";
          if(book.getBarcode().equals(loanInfo.getBook().getBarcode()))
            selected = "selected";
      %>
          <option value='<%=book.getBarcode() %>' <%=selected %>><%=book.getBookName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>读者对象:</td>
    <td width=70%>
      <select name="loanInfo.reader.readerNo" disabled>
      <%
        for(Reader reader:readerList) {
          String selected = "";
          if(reader.getReaderNo().equals(loanInfo.getReader().getReaderNo()))
            selected = "selected";
      %>
          <option value='<%=reader.getReaderNo() %>' <%=selected %>><%=reader.getReaderName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>借阅时间:</td>
    <td width=70%><%=loanInfo.getBorrowDate() %></td>
  </tr>

  <tr>
    <td width=30%>归还时间:</td>
    <td width=70%><%=loanInfo.getReturnDate() %></td>
  </tr>

  <tr>
      <td colspan="4" align="center">
        <input type="button" value="返回" onclick="history.back();"/>
      </td>
    </tr>

</table>
</s:form>
   </TD></TR>
  </TBODY>
</TABLE>
</BODY>
</HTML>
