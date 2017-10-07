<%@ tag body-content="empty" pageEncoding="utf-8" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="value" type="java.lang.String" required="true"%>
<%
	value = value.replace("&", "&amp;");
	value = value.replace("<", "&lt;");
	value = value.replace(" ", "&nbsp;");
	//게시물 내용에서 줄바꿈 문자(\n)를 <br> 태그로 변환
	value = value.replace("\n", "\n<br>");
%>
<%= value %>