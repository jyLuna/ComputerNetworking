package chatRoom;

// MainServer.java에서 port 번호를 받아 실행되는 server
// 한 채팅방 당 한 개의 server
// 채팅방의 client들을 관리
// multi-thread 형식으로 여러번 생성 가능
// 조건에 따라 Service.java 실행

import java.net.*;
import java.util.Vector;
import java.io.*;
import java.util.Random;
//sql 사용
import java.sql.*;

public class Server extends Thread
{
	Vector<Service> Clients;
	
	Server server;
	ServerSocket serverSocket = null;
	Socket serviceSocket;
	private static int port = 0;
	
	// db와 data를 주고 받는 용도
	private static Connection con;				// db 연결
	private static Statement stmt = null;
	private static ResultSet rs = null;
	private static PreparedStatement ps = null;
	private static String query = null;			// db에 보낼 query 저장
	
	
	public Server(int port)
	{
		Clients = new Vector<>();
		
		try
		{ serverSocket = new ServerSocket(port); }
		catch (Exception e)
		{
			System.err.println("연결에 실패했습니다.");
			System.exit(1);
		}
		
		this.port = port;
	}

	public void addClient(Service st)	// 클라이언트 추가
	{ Clients.addElement(st); }

	public void removeClient(Service st)	// 클라이언트 삭제 
	{
		Clients.removeElement(st);
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
		
		try
		{
			stmt = con.createStatement();
			
			query = "SELECT num_of_user FROM chat_room WHERE port = " + serverSocket.getLocalPort();
			rs = stmt.executeQuery(query);
			int num = rs.getInt(1) - 1;
			if(num <= 1)
			{ System.exit(1); }
			else
			{
				query = "UPDATE chat_room SET num_of_user = " + num + "WHERE port = " + serverSocket.getLocalPort();
				rs = stmt.executeQuery(query);
			}
		}
		catch(SQLException e)
		{ e.printStackTrace(); }
	}

	public void sendMessageAll(String msg)	// 전체 메시지 출력 
	{
		try
		{
			for (int i = 0; i < Clients.size(); i++)
			{
				Service st = ((Service) Clients.elementAt(i));	// 존재하는 모든 thread에
				st.sendMessage(msg);
			}
		}
		catch (Exception e)
		{ e.printStackTrace(); }
	}
	
	
	@Override
	public void run()
	{
		System.out.println("주소: " + serverSocket.getInetAddress());
		System.out.println("포트번호: " + serverSocket.getLocalPort());
		try
		{
			while (true)
			{
				serviceSocket = serverSocket.accept();	// 클라이언트 접속

				// 클라이언트 관리하는 객체, 클라이언트를 가진 객체를 생성한 후
				// Thread를 통해서 작업을 (읽고 쓰는) Run 메소드를 명시한 후 실행한다.
				Service thread = new Service(server, serviceSocket);
				thread.start();	// run을 호출한다.
				System.out.println("PASS --> run 시작까지는 ok");
				server.addClient(thread);
			}
		}
		catch (Exception e)
		{
			try
			{ serverSocket.close(); }	// 서버 종료
			catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		System.out.println("client_chat와 연결된 서버를 종료합니다.");
	}
}