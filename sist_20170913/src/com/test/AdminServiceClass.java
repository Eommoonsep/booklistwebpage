package com.test;

import java.io.*;
import java.util.List;

import javax.servlet.*;
import javax.servlet.http.*;

public class AdminServiceClass {

	// "/adminmain.it" 요청주소와 매핑되는 메소드
	public String adminmain(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//관리자 로그인 상태 검사 추가
		/*
		HttpSession session = request.getSession();
		Object obj = session.getAttribute("logininfo");
		if (obj == null) {
			// 뷰 페이지 주소 지정 -> 리다이렉트
			return String.format("/WEB-INF/view/redirect.jsp?url=loginform.do&code=000");
		}
		*/
		//-> 로그인 필터로 대체
		
		// 액션 코드 작성

		
		// 뷰 페이지 주소 지정 -> 포워딩
		return "/WEB-INF/view/admin/adminmain.jsp";

	}
	
	// "/adminqaboardlist.it" 요청주소와 매핑되는 메소드
	public String adminqaboardlist(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//관리자 로그인 상태 검사 추가
		/*
		HttpSession session = request.getSession();
		Object obj = session.getAttribute("logininfo");
		if (obj == null) {
			// 뷰 페이지 주소 지정 -> 리다이렉트
			return String.format("/WEB-INF/view/redirect.jsp?url=loginform.do&code=000");
		}
		*/
		//-> 로그인 필터로 대체
		
		
		//액션 코드
		QABoardDAO dao = new QABoardDAO();
		List<QABoard> qaboardlist = dao.adminqaboardlist();
		String qaboardcount = String.valueOf(qaboardlist.size());
		
		request.setAttribute("qaboardlist", qaboardlist);
		request.setAttribute("qaboardcount", qaboardcount);
		
		// 뷰 페이지 주소 지정 -> 포워딩
		return "/WEB-INF/view/admin/adminqaboardlist.jsp";
	}
	
	// "/adminqaboardinsert.it" 요청주소와 매핑되는 메소드
	public String adminqaboardinsert(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//관리자 로그인 상태 검사 추가
		/*
		HttpSession session = request.getSession();
		Object obj = session.getAttribute("logininfo");
		if (obj == null) {
			// 뷰 페이지 주소 지정 -> 리다이렉트
			return String.format("/WEB-INF/view/redirect.jsp?url=loginform.do&code=000");
		}
		*/
		//-> 로그인 필터로 대체
		
		//액션 코드
		//문제) 
		//1. 답변글내용 수신
		//2. 데이터베이스 액션 -> 답변글테이블에 글저장
		
		//request.setCharacterEncoding("UTF-8");
		//-> 필터 클래스로 대체한다.
		
		String qid = request.getParameter("qid");
		String replyContent = request.getParameter("replyContent");
		
		QABoard qa = new QABoard();
		qa.setQid(qid);
		qa.setReplyContent(replyContent);
		
		QABoardDAO dao = new QABoardDAO();
		dao.adminqaboardAdd(qa);
		
		// 뷰 페이지 주소 지정 -> 리다이렉트
		return "/WEB-INF/view/redirect.jsp?url=adminqaboardlist.it&code=000";

	}
	
	
	
}