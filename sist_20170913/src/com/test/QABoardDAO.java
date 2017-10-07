package com.test;

import java.util.*;
import java.sql.*;

public class QABoardDAO {

	// 문제) QA게시판의 게시물 전체 출력 메소드
	public List<QABoard> qaboardlist() {
		List<QABoard> result = new ArrayList<QABoard>();

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DBConnection.connect();

			/*
			 * CREATE OR REPLACE VIEW userqaboardview AS SELECT qid, title, content_,
			 * clientip ,
			 * DECODE(TO_CHAR(writeday,'YYYY-MM-DD'),TO_CHAR(sysdate,'YYYY-MM-DD'),TO_CHAR(
			 * writeday,'HH24:MI'),TO_CHAR(writeday,'YYYY-MM-DD')) AS writeday , blind,
			 * privacy , (SELECT qid FROM QABoardReplyTable WHERE qid=qt.qid) AS replyqid
			 * FROM qaboardtable qt WHERE blind=0 ORDER BY qid DESC;
			 */

			String sql = "SELECT qid, title, content_, clientip, writeday, privacy, replyqid FROM userqaboardview";
			System.out.println(sql);

			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				String qid = rs.getString("qid");
				String title = rs.getString("title");
				String content_ = rs.getString("content_");
				String clientip = rs.getString("clientip");
				String writeday = rs.getString("writeday");
				int privacy = rs.getInt("privacy");
				String replyqid = rs.getString("replyqid");

				QABoard q = new QABoard();
				q.setQid(qid);
				q.setTitle(title);
				q.setContent_(content_);
				q.setClientip(clientip);
				q.setWriteday(writeday);
				q.setPrivacy(privacy);
				q.setReplyqid(replyqid);

				result.add(q);
			}
			rs.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException se2) {
			}
			try {
				DBConnection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return result;
	}

	// 문제) QA게시판의 게시물 입력 메소드
	public String qaboardAdd(QABoard q) {

		String temp = "101"; // 글쓰기 실패

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DBConnection.connect();

			String sql = "INSERT INTO QABoardTable (qid, title, content_, clientip) VALUES ((SELECT NVL(MAX(qid),0)+1 FROM QABoardTable), ?, ?, ?)";
			// 주의) 이 부분에 sql 구문 출력할 것. 액션 진행 상황 분석용.
			System.out.println(sql);

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, q.getTitle());
			pstmt.setString(2, q.getContent_());
			pstmt.setString(3, q.getClientip());

			pstmt.executeUpdate();
			temp = "100"; // 글쓰기 성공

		} catch (ClassNotFoundException | SQLException e) {
			temp = "101"; // 글쓰기 실패
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException se2) {
			}
			try {
				DBConnection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return temp;
	}

	// QA게시판의 비밀글 입력 메소드
	public String qaboardPrivacyAdd(QABoard q) {

		String temp = "101"; // 글쓰기 실패

		Connection conn = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		try {
			conn = DBConnection.connect();

			// 트랜잭션 처리를 위한 설정 추가 -> autocommit false 설정
			conn.setAutoCommit(false);

			String sql1 = "SELECT NVL(MAX(qid),0)+1 AS qid FROM QABoardTable";
			System.out.println(sql1);
			pstmt1 = conn.prepareStatement(sql1);
			ResultSet rs = pstmt1.executeQuery();
			String qid = "";
			while (rs.next()) {
				qid = rs.getString("qid");
			}
			rs.close();

			String sql2 = "INSERT INTO QABoardTable (qid, title, content_, clientip, privacy) VALUES (?, ?, ?, ?, 1)";
			System.out.println(sql2);
			pstmt2 = conn.prepareStatement(sql2);
			pstmt2.setString(1, qid);
			pstmt2.setString(2, q.getTitle());
			pstmt2.setString(3, q.getContent_());
			pstmt2.setString(4, q.getClientip());
			pstmt2.executeUpdate();

			String sql3 = "INSERT INTO QABoardPasswordTable (qid, pw) VALUES (?, ?)";
			System.out.println(sql3);
			pstmt3 = conn.prepareStatement(sql3);
			pstmt3.setString(1, qid);
			pstmt3.setString(2, q.getPw());
			pstmt3.executeUpdate();

			// 트랜잭션 처리를 위한 설정 추가 -> 커밋
			conn.commit();

			temp = "100"; // 글쓰기 성공

		} catch (ClassNotFoundException | SQLException e) {

			// 트랜잭션 처리를 위한 설정 추가 -> 롤백
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			temp = "101"; // 글쓰기 실패
		} finally {
			try {
				if (pstmt1 != null)
					pstmt1.close();
				if (pstmt2 != null)
					pstmt2.close();
				if (pstmt3 != null)
					pstmt3.close();
			} catch (SQLException se2) {
			}
			try {
				DBConnection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return temp;
	}

	// 관리자용 QA게시판의 게시물 전체 출력 메소드
	public List<QABoard> adminqaboardlist() {
		List<QABoard> result = new ArrayList<QABoard>();

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DBConnection.connect();

			/*
			 * CREATE OR REPLACE VIEW adminqaboardview AS SELECT qid, title, content_,
			 * clientip ,
			 * DECODE(TO_CHAR(writeday,'YYYY-MM-DD'),TO_CHAR(sysdate,'YYYY-MM-DD'),TO_CHAR(
			 * writeday,'HH24:MI'),TO_CHAR(writeday,'YYYY-MM-DD')) AS writeday , blind,
			 * privacy , (SELECT qid FROM QABoardReplyTable WHERE qid=qt.qid) AS replyqid
			 * FROM qaboardtable qt ORDER BY qid DESC;
			 */

			String sql = "SELECT qid, title, content_, clientip, writeday, privacy, replyqid, blind FROM adminqaboardview";
			System.out.println(sql);

			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				String qid = rs.getString("qid");
				String title = rs.getString("title");
				String content_ = rs.getString("content_");
				String clientip = rs.getString("clientip");
				String writeday = rs.getString("writeday");
				int privacy = rs.getInt("privacy");
				int blind = rs.getInt("blind");
				String replyqid = rs.getString("replyqid");

				QABoard q = new QABoard();
				q.setQid(qid);
				q.setTitle(title);
				q.setContent_(content_);
				q.setClientip(clientip);
				q.setWriteday(writeday);
				q.setPrivacy(privacy);
				q.setReplyqid(replyqid);
				q.setBlind(blind);

				result.add(q);
			}
			rs.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException se2) {
			}
			try {
				DBConnection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return result;
	}

	// 문제) 답변글게시판(QABoardReplyTable)의 게시물 입력 메소드
	public String adminqaboardAdd(QABoard q) {

		String temp = "101"; // 글쓰기 실패

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DBConnection.connect();

			String sql = "INSERT INTO QABoardReplyTable (qid,replycontent) VALUES (?, ?)";
			// 주의) 이 부분에 sql 구문 출력할 것. 액션 진행 상황 분석용.
			System.out.println(sql);

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, q.getQid());
			pstmt.setString(2, q.getReplyContent());

			pstmt.executeUpdate();
			temp = "100"; // 글쓰기 성공

		} catch (ClassNotFoundException | SQLException e) {
			temp = "101"; // 글쓰기 실패
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException se2) {
			}
			try {
				DBConnection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return temp;
	}
	
	
	//QA게시판의 비밀글에 대한 글내용 반환하는 메소드
	public String qaBoardPwReply(QABoard q) {
		
		//패스워드가 틀린 경우에 대비한 초기값
		String result = "ERROR";

		Connection conn = null;
		PreparedStatement pstmt= null;
		
		try {
			conn = DBConnection.connect();
		
			String sql = "SELECT (SELECT content_ FROM QABOARDTABLE WHERE qid = qp.qid) AS content_ FROM qaBoardPasswordTable qp WHERE qid =? AND pw =?";
			System.out.println(sql);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, q.getQid());
			pstmt.setString(2, q.getPw());
			ResultSet rs = pstmt.executeQuery();
		
			while (rs.next()) {
				result = rs.getString("content_");
			}
			rs.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException se2) {
			}
			try {
				DBConnection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return result;
	}
	
	
	
	
	//QA게시판 관리자 답변글 출력 메소드 -> Ajax 응답용
	public String replyContent(String qid) {
		String result = "";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DBConnection.connect();

			/*
			SELECT replyContent 
				FROM QABoardReplyTable 
				WHERE qid=?
			*/

			String sql = "SELECT replyContent FROM QABoardReplyTable WHERE qid=?";
			System.out.println(sql);
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, qid);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("replyContent");
			}
			rs.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				DBConnection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
		
		
		return result;
	}

}
