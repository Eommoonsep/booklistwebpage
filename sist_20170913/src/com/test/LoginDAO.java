package com.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO {
	
	//매개변수에 grade 항목 추가
	//-> 관리자 로그인
	public LoginInfo login(String id_, String pw, String grade) {

		//초기값으로 null 지정
		LoginInfo result = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DBConnection.connect();
			
			//grade 항목 추가
			String sql = "SELECT id_, pw, grade FROM sistlogin WHERE id_=? AND pw=? AND grade=0";
			
			System.out.println(sql);
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id_);
			pstmt.setString(2, pw);
			ResultSet rs = pstmt.executeQuery();
			
			//SELECT 결과가 존재하는 경우만 while 블럭 실행
			//id_, pw_가 데이터베이스에 존재하는 경우만 while 블럭 실행
			while(rs.next()) {
				
				int g = rs.getInt("grade");
				
				//로그인 성공한 경우 개인 정보를 임시 객체에 저장
				result = new LoginInfo();
				result.setId_(id_);
				result.setGrade(g);
				
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

	
}
