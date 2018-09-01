<%@ page language="java" import="java.util.*"  contentType="text/html;charset=gb2312"%> 
<%@ page import="com.shuangyulin_QQ287307421.domain.Reader" %>
<%@ page import="com.shuangyulin_QQ287307421.domain.ReaderType" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //��ȡ���е�readerType��Ϣ
    List<ReaderType> readerTypeList = (List<ReaderType>)request.getAttribute("readerTypeList");
    Reader reader = (Reader)request.getAttribute("reader");

%>
<HTML><HEAD><TITLE>�鿴����</TITLE>
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
    <td width=30%>���߱��:</td>
    <td width=70%><%=reader.getReaderNo() %></td>
  </tr>

  <tr>
    <td width=30%>��������:</td>
    <td width=70%>
      <select name="reader.readerType.readerTypeId" disabled>
      <%
        for(ReaderType readerType:readerTypeList) {
          String selected = "";
          if(readerType.getReaderTypeId() == reader.getReaderType().getReaderTypeId())
            selected = "selected";
      %>
          <option value='<%=readerType.getReaderTypeId() %>' <%=selected %>><%=readerType.getReaderTypeName() %></option>
      <%
        }
      %>
    </td>
  </tr>

  <tr>
    <td width=30%>����:</td>
    <td width=70%><%=reader.getReaderName() %></td>
  </tr>

  <tr>
    <td width=30%>�Ա�:</td>
    <td width=70%><%=reader.getSex() %></td>
  </tr>

  <tr>
    <td width=30%>��������:</td>
    <td width=70%><%=reader.getBirthday() %></td>
  </tr>

  <tr>
    <td width=30%>��ϵ�绰:</td>
    <td width=70%><%=reader.getTelephone() %></td>
  </tr>

  <tr>
    <td width=30%>��ϵEmail:</td>
    <td width=70%><%=reader.getEmail() %></td>
  </tr>

  <tr>
    <td width=30%>��ϵqq:</td>
    <td width=70%><%=reader.getQq() %></td>
  </tr>

  <tr>
    <td width=30%>���ߵ�ַ:</td>
    <td width=70%><%=reader.getAddress() %></td>
  </tr>

  <tr>
    <td width=30%>����ͷ��:</td>
    <td width=70%><img src="<%=basePath %><%=reader.getPhoto() %>" width="200px" border="0px"/></td>
  </tr>
  <tr>
      <td colspan="4" align="center">
        <input type="button" value="����" onclick="history.back();"/>
      </td>
    </tr>

</table>
</s:form>
   </TD></TR>
  </TBODY>
</TABLE>
</BODY>
</HTML>