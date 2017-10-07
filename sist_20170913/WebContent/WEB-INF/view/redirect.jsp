<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"  trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:url var="redirect" value="${param.url}">
	<c:param name="code" value="${param.code}" />
</c:url>
<c:redirect url="${redirect}" />