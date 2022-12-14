package messenger;

import java.sql.*;
import java.time.LocalDate;

/* DAO = Database Access Object*/
//실질적으로 DB에서 회원정보를 불러오거나 넣을 때 사용

public class UserDAO {
	private Connection conn;	//DB에 접근할 수 있게 하는 객체
	private PreparedStatement pstmt;	//SQL문 실행 시 사용되는 인터페이스
	private ResultSet rs;	//SELECT문의 실행결과를 저장하는 객체
	
	public UserDAO() //DB connection 체크
	{
		try {
			String dbURL = "jdbc:mysql://localhost:3306/messenger";
			String dbID = "root";
			String dbPassword = "12345";
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL,dbID,dbPassword);
//			System.out.println("Success");
		}catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public int login(String user_id, String pw)	//로그인 메서드
	{
		String query = "SELECT CAST(AES_DECRYPT(UNHEX(pw),'mysql') AS char(50)) FROM user WHERE user_id = ?";
		//암호화된 pw를 복호화함.
		try
		{
			pstmt = conn.prepareStatement(query);	//db에 데이터 삽입
			pstmt.setString(1, user_id);	
			rs = pstmt.executeQuery();	//rs에 실행 결과 저장
			if(rs.next())	//결과가 존재
			{
				if(rs.getString(1).equals(pw))	//DB의 pw와 일치
					return 1; //로그인 성공
				else
					return 0; //비밀번호 불일치
			}
			return -1;	//아이디가 없음
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return -2; //Database 오류
	}
	
	public int join(User user)	//회원가입 메서드
	{
		String query = "INSERT INTO user VALUES (?, HEX(AES_ENCRYPT(?,'mysql')), ?, ?, ?, ?, ?, ?)";
		//pw는 암호화되어 들어감.
		try
		{
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, user.getUser_id());	//user_id
			pstmt.setString(2, user.getPw());	//pw
			pstmt.setString(3, user.getNickname());	//nickname
			pstmt.setString(4, user.getUser_name());	//user_name
			pstmt.setString(5, user.getEmail_address());	//email_address
			pstmt.setString(6, user.getBirth_date());	//birth_date
			pstmt.setString(7, user.getHp());	//hp
			pstmt.setString(8, user.getCreateDate());	//CreateDate
			return pstmt.executeUpdate();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return -1; //Database 오류
	}
	
	public static void main(String[] args) 
	{ 
		 UserDAO sample = new UserDAO();
	  
		 User person1 = new User(); 
		 person1.setUser_id("12345");
		 person1.setPw("12345"); 
		 person1.setNickname("Jmin");
		 person1.setUser_name("lee jimin");
		 person1.setEmail_address("dlwlals1207@gachon.ac.kr");
		 person1.setBirth_date("1111-11-11"); 
		 person1.setHp("010-1111-1111");
		 LocalDate create_time = LocalDate.now(); 
		 person1.setCreateDate(create_time.toString());
	  
//		 sample.join(person1);
		 sample.login(person1.getUser_id(), person1.getPw());
	}
	 
	
}
