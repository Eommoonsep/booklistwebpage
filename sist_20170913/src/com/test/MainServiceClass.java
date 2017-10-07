package com.test;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

public class MainServiceClass {

	// "/main.it" 요청주소와 매핑되는 메소드
	public String main(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 액션 코드 작성
		
		

		// 뷰 페이지 주소 지정 -> 포워딩
		return "/WEB-INF/view/sistmain.jsp";

	}
	
	// "/sistintro.it" 요청주소와 매핑되는 메소드
	public String sistintro(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 뷰 페이지 주소 지정 -> 포워딩
		return "/WEB-INF/view/centerintro/sistintro.jsp";

	}
	
	
	
	// "/sisteducation.it" 요청주소와 매핑되는 메소드
	public String sisteducation(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 뷰 페이지 주소 지정 -> 포워딩
		return "/WEB-INF/view/education/sisteducation.jsp";

	}
	
}