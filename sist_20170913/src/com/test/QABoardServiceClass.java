package com.test;

import java.io.*;
import java.util.List;

import javax.servlet.*;
import javax.servlet.http.*;

public class QABoardServiceClass {

	// "/qaboardlist.it" 요청주소와 매핑되는 메소드
	public String qaboardlist(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
				
		// 액션 코드 작성
		QABoardDAO dao = new QABoardDAO();
		List<QABoard> qaboardlist = dao.qaboardlist();
		String qaboardcount = String.valueOf(qaboardlist.size());

		request.setAttribute("qaboardlist", qaboardlist);
		request.setAttribute("qaboardcount", qaboardcount);
		
		
		// 뷰 페이지 주소 지정 -> 포워딩
		return "/WEB-INF/view/qaboard/qaboardlist.jsp";

	}
	
	
	// "/qaboardinsert.it" 요청주소와 매핑되는 메소드
	public String qaboardinsert(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// 액션 코드 작성
		
		//문제) 클라이언트가 전달하는 데이터 수신(title, content_) 및 데이터베이스 액션(qaboardAdd() 메소드) 처리
		//-> 주의) pw는 선택적으로 수신된다.
		
		//request.setCharacterEncoding("UTF-8");
		//-> 필터 클래스로 대체한다.
		
		String title = request.getParameter("title");
		String content_ = request.getParameter("content_");
		String clientip = request.getRemoteAddr();
		String pw = request.getParameter("pw");
		
		String code = "101"; //글쓰기 실패
		if (!(title == null || title.equals(""))) {

			//패스워드 수신 여부에 따른 액션 구분
			if (pw == null) {
				//일반글쓰기 액션 처리
				//-> QABoardTable 테이블에만 입력 액션 처리

				QABoard q = new QABoard();
				q.setTitle(title);
				q.setContent_(content_);
				q.setClientip(clientip);
	
				QABoardDAO dao = new QABoardDAO();
				code = dao.qaboardAdd(q);
			} else {
				//비밀글쓰기 액션 처리
				//-> QABoardTable + QABoardPasswordTable 테이블에 입력 액션 처리
				//-> 최신 글번호 획득>QABoardTable Insert>QABoardPasswordTable Insert
				//-> 트랜잭션 처리 필요
				QABoard q = new QABoard();
				q.setTitle(title);
				q.setContent_(content_);
				q.setClientip(clientip);
				q.setPw(pw);
	
				QABoardDAO dao = new QABoardDAO();
				code = dao.qaboardPrivacyAdd(q);
			}

		}
		
		// 뷰 페이지 주소 지정 -> 리다이렉트
		return String.format("/WEB-INF/view/redirect.jsp?url=qaboardlist.it&code=%s", code);
	}
	
	
	// "/qaboardajaxcontent.it" 요청주소와 매핑되는 메소드
	public String qaboardajaxcontent(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//request.setCharacterEncoding("UTF-8");
		//-> 필터 클래스로 대체한다.
		
		String qid = request.getParameter("qid");
		String pw = request.getParameter("pw");
		
		QABoard q = new QABoard();
		q.setQid(qid);
		q.setPw(pw);
		
		QABoardDAO dao = new QABoardDAO();
		String content_ = dao.qaBoardPwReply(q); //"ERROR" or 글내용
		
		request.setAttribute("content_", content_);
		
		// 뷰 페이지 주소 지정 -> 포워딩
		return "/WEB-INF/view/qaboard/qaboardAjaxContent.jsp";
	}
	
	
	
	// "/qaboardajaxreply.it" 요청주소와 매핑되는 메소드
	public String qaboardajaxreply(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String qid = request.getParameter("qid");
		
		QABoardDAO dao = new QABoardDAO();
		String replyContent = dao.replyContent(qid);
		
		request.setAttribute("replyContent", replyContent);
		
		// 뷰 페이지 주소 지정 -> 포워딩
		return "/WEB-INF/view/qaboard/qaboardajaxreply.jsp";
	}
			
			
	
}