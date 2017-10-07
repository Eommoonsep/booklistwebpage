package com.test;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

public class LoginServiceClass {

	// "/login.it" 요청주소와 매핑되는 메소드
	public String login(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 액션 코드 작성
		/*
		1. 로그인 정보(id_, pw) 수신
		2. 데이터베이스 질의 
		3. 반환된 결과값 분석
		4. (로그인 성공한 경우) 세션 객체 생성
		5. 페이지 전환
		*/
		request.setCharacterEncoding("UTF-8");
		String id_ = request.getParameter("id_");
		String pw = request.getParameter("pw");
		
		String url = "loginfail.it"; //로그인 실패 초기값
		
		if (id_ == null || id_.equals("")) {
			
		} else {
			
			LoginDAO dao = new LoginDAO();
			/* login() 메소드에서 "0"은 관리자 로그인 구분하는 값 */
			LoginInfo result = dao.login(id_, pw, "0");
			
			if (result instanceof LoginInfo) {
				
				//세션 객체 생성
				HttpSession session = request.getSession();
				session.setAttribute("logininfo", result);
				url = "adminmain.it"; //로그인 성공
				
			}
		}
		
		// 뷰 페이지 주소 지정 -> 리다이렉트
		return String.format("/WEB-INF/view/redirect.jsp?url=%s&code=000", url);

	}
	
	// "/logout.it" 요청주소와 매핑되는 메소드
	public String logout(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 액션 코드 작성
		// 세션 객체 소멸
		HttpSession session = request.getSession();
		session.invalidate();

		// 뷰 페이지 주소 지정 -> 리다이렉트
		return String.format("/WEB-INF/view/redirect.jsp?url=logoutform.it&code=000");

	}
	
	
	// "/loginfail.it" 요청주소와 매핑되는 메소드
	public String loginfail(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 뷰 페이지 주소 지정 -> 포워딩
		return "/WEB-INF/view/loginfail.jsp";

	}
	
	// "/logoutform.it" 요청주소와 매핑되는 메소드
	public String logoutform(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 뷰 페이지 주소 지정 -> 포워딩
		return "/WEB-INF/view/logoutform.jsp";

	}
	
}