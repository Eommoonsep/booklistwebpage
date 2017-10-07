<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"  trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
				<ul class="nav navbar-nav">
					<li     ><a
						href="${pageContext.request.contextPath}/adminmain.it">Home</a></li>
					<li><a href="#">과정</a></li>
					<li><a href="#">공지사항</a></li>
					<li><a
						href="${pageContext.request.contextPath}/adminqaboardlist.it">문의게시판</a></li>
					<li><a href="#">회원</a></li>
					<li><a href="#">상담</a></li>
					<li><a href="#">성적</a></li>
					<li><a href="#">교재</a></li>

					<!-- EL 표현을 이용해서 세션객체에 저장된 정보를 확인할 수 있다 -->
					<li><a href="${pageContext.request.contextPath}/logout.it">관리자(${sessionScope.logininfo.id_})
							로그아웃</a></li>

				</ul>