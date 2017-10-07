package com.test;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

public class ServiceClass {

	// "/test.do" 요청주소와 매핑되는 메소드
	public String test(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 액션 코드 작성
		request.setAttribute("value", "TEST");

		// 뷰 페이지 주소 지정 -> 포워딩
		return "/WEB-INF/view/test.jsp";

	}
	
}