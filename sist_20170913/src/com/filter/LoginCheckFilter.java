package com.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

//필터 클래스 선언을 위한 javax.servlet.Filter 인터페이스 구현 과정
public class LoginCheckFilter implements Filter {
	
	//필터 클래스 초기 실행시 호출되는 메소드
   @Override
    public void init(FilterConfig config) throws ServletException {
    }

	//doFilter() 메소드는 request, response 요청시마다 매번 호출되는 메소드
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

    	//세션 객체 확보
    	HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);

        //로그인 여부 확인을 위한 임시 변수
        boolean login = false;
        
        //세션 객체 존재 여부 확인
        if (session != null) {
        	
        	//세션 객체의 정보 확인
            if (session.getAttribute("logininfo") != null) {
            	//로그인 사용자 인증을 위한 값 저장
                login = true;
            }
            
        }
        
        //로그인 사용자 인증 값 검사
        if (login) {
        	//로그인 사용자인 경우 다음 단계 실행
            chain.doFilter(request, response);
        } else {
        	//로그인하지 않은 사용자인 경우 강제 페이지 전환
            RequestDispatcher dispatcher = request
                    .getRequestDispatcher("/WEB-INF/view/redirect.jsp?url=loginfail.it");
            dispatcher.forward(request, response);
        }
    }

    @Override
    public void destroy() {
    }
}