package com.test;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerServlet extends HttpServlet {
	

	//확장자 문자열 저장용 임시 변수
    private String ext;

    //init() 메소드는 서블릿이 최초 실행시 자동 호출되는 메소드
    //->주의) 한 번만 호출되고 실행된다.
    public void init() throws ServletException {
    	
    	//web.xml 안에 있는 <init-param> 엘리먼트 정보
        ext = getInitParameter("ext");
 
    }
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//doGet() 메소드가 처리할 내용을 사용자 정의 메소드로 넘긴다.
		processRequest(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//doPost() 메소드가 처리할 내용을 사용자 정의 메소드로 넘긴다.
		processRequest(req, resp);
	}
	
	//사용자 정의 메소드 추가
	//-> doGet() + doPost()
	//-> 사용자 요청 방식을 구분하지 않고 하나로 통합 처리 가능.
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//1. 사용자 요청(/hello.do or /test.do)에 대한 분석 처리
		//2. 적절한 모델 클래스 액션 요청
		//3. 돌려받은 결과를 뷰(JSP) 페이지로 전달
		//-> request 객체에 결과 저장 후 
		//-> RequestDispatcher 객체를 이용한 포워딩
		
		//사용자 요청 주소 분석 -> 특정 메소드와 매핑
		//사용자 요청 주소가 /hello.do 인 경우 -> hello()
		//사용자 요청 주소가 /test.do 인 경우 -> test()
		String command = request.getRequestURI();
		//System.out.println(command); //"/프로젝트이름/test.do"
		if (command.indexOf(request.getContextPath()) == 0) {
			command = command.substring(request.getContextPath().length());
			//System.out.println(command); //"/test.do"
		}
		command = command.substring(1, command.indexOf(ext));
		//System.out.println(command); //"test"
		
		//***ServiceClass 객체의 특정 메소드 호출(invoke) 및 View 정보 반환
		String viewPage = "";
		try {
			/*
			ServiceClass service = new ServiceClass();
			Method m = ServiceClass.class.getMethod(command, HttpServletRequest.class, HttpServletResponse.class);
			viewPage = (String)m.invoke(service, request, response);
			*/
			
			//main** or sist** 액션 요청시 사용하는 클래스
			if (command.startsWith("main") || command.startsWith("sist")) {
				MainServiceClass service = new MainServiceClass();
				Method m = MainServiceClass.class.getMethod(command, HttpServletRequest.class, HttpServletResponse.class);
				viewPage = (String)m.invoke(service, request, response);
			}
			
			//admin** 액션 요청시 사용하는 클래스
			if (command.startsWith("admin")) {
				AdminServiceClass service = new AdminServiceClass();
				Method m = AdminServiceClass.class.getMethod(command, HttpServletRequest.class, HttpServletResponse.class);
				viewPage = (String)m.invoke(service, request, response);
			}

			//qaboard** 액션 요청시 사용하는 클래스
			if (command.startsWith("qaboard")) {
				QABoardServiceClass service = new QABoardServiceClass();
				Method m = QABoardServiceClass.class.getMethod(command, HttpServletRequest.class, HttpServletResponse.class);
				viewPage = (String)m.invoke(service, request, response);
			}

			//log** 액션 요청시 사용하는 클래스
			if (command.startsWith("log")) {
				LoginServiceClass service = new LoginServiceClass();
				Method m = LoginServiceClass.class.getMethod(command, HttpServletRequest.class, HttpServletResponse.class);
				viewPage = (String)m.invoke(service, request, response);
			}

		} catch(Exception e) {
			e.printStackTrace();
		}
		
		//View 페이지 포워딩
		RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
		dispatcher.forward(request, response);
	
	}
	

}
