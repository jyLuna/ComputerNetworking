package chatRoom;

// 기본적으로 client(Client.java)와 연결되는 server
// 1번만 실행
// 조건에 따라 Server.java에게 port를 지정하여 실행시킴 

import java.net.*;			// 서버 연결
import java.io.*;			// 서버 연결
import java.util.Scanner;	// console 입력
import java.util.Vector;

//sql 사용
import java.sql.*;
import java.util.Calendar;	// timestamp를 사용 위해

import java.util.Random;	// port 랜덤 생성

// thread 실행
public class MainServer
{
	Vector<Server> Chat;
	private static String user_name = null;
	private static int port = 1000;				// port의 범위는 1000~9998(main server의 port는 9999)
	
	private static MainServer server;
	private static ServerSocket serverSocket = null;
	private static Socket serviceSocket;
	
	// db와 data를 주고 받는 용도
	private static Connection con;				// db 연결
	private static Statement stmt = null;
	private static ResultSet rs = null;
	private static PreparedStatement ps = null;
	private static String query = null;			// db에 보낼 query 저장
		
	// server와 data를 주고 받는 용도
	private static ObjectOutputStream oos = null;
	private static ObjectInputStream ois = null;
	private static String str = null;			// server에 보낼 string 저장

	
	public MainServer()
	{
		Chat = new Vector<>();
		// sql 연결
		String Driver = "";
		String url = "jdbc:mysql://localhost:3306/computernetwork";
		String my_id = "root";			// 자신의 mysql id 입력
		String my_pwd = "12345";		// 자신의 mysql password 입력
		try
		{ Class.forName("com.mysql.cj.jdbc.Driver"); }
		catch(ClassNotFoundException e)
		{ e.printStackTrace(); }
		try	// db 연결
		{ con = DriverManager.getConnection(url, my_id, my_pwd); }
		catch(SQLException e)
		{ e.printStackTrace(); }
		//
	}
	
	
	public void addChat(Server cst)	// 클라이언트 추가
	{ Chat.add(cst); }

	public void removeChat(Server cst)	// 클라이언트 삭제 
	{ Chat.remove(cst); }
	
	private void makeChatRoom() throws Exception
	{
		// client에서 user_name 전달 받기
		ois = new ObjectInputStream(serviceSocket.getInputStream());
		user_name = String.valueOf(ois.readObject());
		// client에서 chat_user_id 값 전달 받기
		ois = new ObjectInputStream(serviceSocket.getInputStream());
		str = String.valueOf(ois.readObject());
		
		// db에서 해당 값에 대한 결과 확인
		query = "SELECT * FROM chat_room where num_of_user = 2 and open_user_id = \'" + user_name + "\' and chat_user_id = \'" + str + "\';";
		rs = stmt.executeQuery(query);
		// query의 결과값이 있으면(chat_room에 이미 해당 user와의 채팅방이 존재) 해당 chat_room id 전송
		if(rs.next())	// query의 결과값이 있음
		{
			str = rs.getString(1);
			port = rs.getInt(2);
			
			// 해당 채팅방의 title 얻기
			String title = rs.getString(3);
			// client에 채팅방의 title 전송
			oos = new ObjectOutputStream(serviceSocket.getOutputStream());
			oos.writeObject(title);
			
			
			// 해당 채팅방의 서버 실행
			Server thread = new Server(port);
			thread.start();	// run을 호출한다
			System.out.println("open server");
			server.addChat(thread);
		}
		else
		{
			query = "SELECT * FROM chat_room WHERE num_of_user = 2 and open_user_id = \'" + str + "\' and chat_user_id = \'" + user_name + "\'";
			rs = stmt.executeQuery(query);
			if(rs.next())	// query의 결과값이 있음
			{
				str = rs.getString(1);
				port = rs.getInt(2);
				
				// 해당 채팅방의 title 얻기
				String title = rs.getString(3);
				// client에 채팅방의 title 전송
				oos = new ObjectOutputStream(serviceSocket.getOutputStream());
				oos.writeObject(title);
				
				
				// 해당 채팅방의 서버 실행
				Server thread = new Server(port);
				thread.start();	// run을 호출한다
				System.out.println("open server");
				server.addChat(thread);
				
				// client에 채팅방의 db 검색 결과
				oos = new ObjectOutputStream(serviceSocket.getOutputStream());
				oos.writeObject(str);
			}
			// 채팅방 생성
			else
			{
				String chat_user_id = str;		// chat_user_id 저장
				str = "false";					// client에게 보낼 값
				
				query = "SELECT * FROM chat_room";
				rs = stmt.executeQuery(query);
				while(rs.next()) { port = rs.getInt(2); }	// 가장 마지막 port 번호를 받아 중복 방지
				port++;
				
				// client에 채팅방의 db 검색 결과
				oos = new ObjectOutputStream(serviceSocket.getOutputStream());
				oos.writeObject(str);
				
				// client에게서 room_title 전달 받기
				ois = new ObjectInputStream(serviceSocket.getInputStream());
				str = String.valueOf(ois.readObject());
				
				// chat_room에 생성한 채팅방 정보 기록
				query = "INSERT INTO chat_room VALUES(NULL, " + port + ", \'" + str + "\', now(), "
						+ 2 + ", \'" + user_name + "\', \'" + chat_user_id + "\');";
				System.out.println(query);
				ps = con.prepareStatement(query);
				ps.executeUpdate();
				
				// 해당 채팅방의 서버 생성
				Server thread = new Server(port);
				thread.start();	// run을 호출한다
				System.out.println("open server");
				server.addChat(thread);
			}
		}
		// client에 채팅방의 port 전송(입장할 수 있도록)
		oos = new ObjectOutputStream(serviceSocket.getOutputStream());
		oos.writeObject(port);
	}
	
	
	public static void main(String[] args)	throws Exception
	{
		while(true)
		{
			server = new MainServer();		// 클라이언트를 관리하는 객체(추가, 삭제, 메시지 전달)
			stmt = con.createStatement();
			
			
			try
			{
				System.out.println("연결 기다리는 중"); 
				serverSocket = new ServerSocket(9999);
			}
			catch (Exception e)
			{
				System.err.println("연결에 실패했습니다.");
				System.exit(1);
			}
			
			while (true)
			{
				serviceSocket = serverSocket.accept();	// 클라이언트 접속
				System.out.println("연결 성공");
				
				// 선택한 부분 확인
				ois = new ObjectInputStream(serviceSocket.getInputStream());
				str = String.valueOf(ois.readObject());
				
				if(str.equals("1"))	// 채팅방 입장
				{
					// client에서 chat_room의 id 값 전달 받기
					ois = new ObjectInputStream(serviceSocket.getInputStream());
					str = String.valueOf(ois.readObject());
					
					query = "SELECT * FROM chat_room WHERE id = " + str + ";";
					rs = stmt.executeQuery(query);
					// chat_room에서 해당 id의 title 찾기
					String title = "";
					if(rs.next())
					{ title = rs.getString(3); }
					// client에 채팅방의 title 전송
					oos = new ObjectOutputStream(serviceSocket.getOutputStream());
					oos.writeObject(title);
					
					
					// chat_room에서 해당 id의 port 찾기
					if(rs.next())
					{ port = rs.getInt(2); }
					// client에 채팅방의 port 전송(입장할 수 있도록)
					oos = new ObjectOutputStream(serviceSocket.getOutputStream());
					oos.writeObject(port);
					
					// 해당 채팅방의 서버 실행
					Server thread = new Server(port);
					thread.start();	// run을 호출한다
					System.out.println("open server");
				}
				else if(str.equals("3"))	// 친구 채팅방에 추가
				{
					// client에서 chat_room의 id 값 전달 받기
					ois = new ObjectInputStream(serviceSocket.getInputStream());
					str = String.valueOf(ois.readObject());
					String id = str;
					// client에서 user의 id 값 전달 받기
					ois = new ObjectInputStream(serviceSocket.getInputStream());
					str = String.valueOf(ois.readObject());
					String uid = str;
					
					// 기존 채팅방 인원수 확인
					int num = 0;
					query = "SELECT num_of_user FROM chat_room WHERE id = " + id + ";";
					rs = stmt.executeQuery(query);
					if(rs.next())
					{ num = rs.getInt(1);}
					// 채팅방 인원수 update
					num++;
					query = "UPDATE chat_room SET num_of_user = " + num + " WHERE id = " + id + ";";
					rs = stmt.executeQuery(query);
					System.out.println(query);
				}
				else				// 채팅방 생성
				{ server.makeChatRoom(); }
			}
		}
	}
}