package messenger;

import java.sql.*;
import java.time.LocalDate;

/* DAO = Database Access Object*/
//���������� DB���� ȸ�������� �ҷ����ų� ���� �� ���

public class UserDAO {
	private Connection conn;	//DB�� ������ �� �ְ� �ϴ� ��ü
	private PreparedStatement pstmt;	//SQL�� ���� �� ���Ǵ� �������̽�
	private ResultSet rs;	//SELECT���� �������� �����ϴ� ��ü
	
	public UserDAO() //DB connection üũ
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
	
	public int login(String user_id, String pw)	//�α��� �޼���
	{
		String query = "SELECT CAST(AES_DECRYPT(UNHEX(pw),'mysql') AS char(50)) FROM user WHERE user_id = ?";
		//��ȣȭ�� pw�� ��ȣȭ��.
		try
		{
			pstmt = conn.prepareStatement(query);	//db�� ������ ����
			pstmt.setString(1, user_id);	
			rs = pstmt.executeQuery();	//rs�� ���� ��� ����
			if(rs.next())	//����� ����
			{
				if(rs.getString(1).equals(pw))	//DB�� pw�� ��ġ
					return 1; //�α��� ����
				else
					return 0; //��й�ȣ ����ġ
			}
			return -1;	//���̵� ����
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return -2; //Database ����
	}
	
	public int join(User user)	//ȸ������ �޼���
	{
		String query = "INSERT INTO user VALUES (?, HEX(AES_ENCRYPT(?,'mysql')), ?, ?, ?, ?, ?, ?)";
		//pw�� ��ȣȭ�Ǿ� ��.
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
		return -1; //Database ����
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
