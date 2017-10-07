package com.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

//필터 클래스 선언을 위한 javax.servlet.Filter 인터페이스 구현 과정
public class CharacterEncodingFilter implements Filter {

	private String encoding;

	
	//doFilter() 메소드는 request, response 요청시마다 매번 호출되는 메소드
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
	
		//인코딩 설정
		req.setCharacterEncoding(encoding);
		
		//필터 체인
		chain.doFilter(req, res);
	}

	//필터 클래스 초기 실행시 호출되는 메소드
	@Override
	public void init(FilterConfig config) throws ServletException {
		//web.xml의 <init-param> 엘리먼트의 설정값을 읽어오는 과정
		encoding = config.getInitParameter("encoding");
		if (encoding == null) {
			encoding = "UTF-8";
		}
	}

	@Override
	public void destroy() {
	}

}
