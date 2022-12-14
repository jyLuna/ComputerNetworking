package chatRoom;

// MainServer.java와 연결되는 제일 기본적인 client
// 1번만 실행
// 조건에 따라 Server.java의 port 번호를 전달하여 Client_chat.java 실행

import java.net.*;			// 서버 연결
import java.io.*;			// 서버 연결
import java.util.Scanner;	// console 입력
import java.util.Vector;

//sql 사용
import java.sql.*;


// thread 실행
public class Client
{
	Vector<Client_chat> Clients;
	private static String user_name = "ha";		// user_id
	private static int port = 0;				// 접속할 server의 port 번호
	
	// db와 data를 주고 받는 용도
	private static Connection con;				// db 연결
	private static Statement stmt = null;
	private static ResultSet rs = null;
	private static String query = null;			// db에 보낼 query 저장
	
	// server와 data를 주고 받는 용도
	private static ObjectOutputStream oos = null;
	private static ObjectInputStream ois = null;
	private static String str = null;			// server에 보낼 string 저장
	
	private static Socket clientSocket = null; // 클라이언트 선언
	
	public Client()
	{
		Clients = new Vector<>();
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
	
	
	// 유저, 채팅방 목록 출력
	private static void printList() throws Exception
	{
		// user
		query = "SELECT user_id FROM user";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		System.out.println("User:");
		while(rs.next())
		{ System.out.printf(rs.getString(1) + "  "); }
		System.out.println("\n");
		
		// chat_room
		query = "SELECT * FROM chat_room";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		System.out.println("Chat Room:");
		while(rs.next())
		{
			System.out.println(rs.getString(1) + " / " + rs.getString(2) + " / " + rs.getString(3) + " / "
					+ rs.getString(4) + " / " + rs.getString(5) + " / " + rs.getString(6) + " / " + rs.getString(7));
		}
		System.out.println("\n");
	}
	
	
	private static void connect()
	{
		try	// 현재 server의 port는 9999
		{ clientSocket = new Socket("127.0.0.1", 9999); }
		catch(Exception e)
		{ e.printStackTrace(); }
		System.out.println(clientSocket.getInetAddress());
	}
	
	public void disconnect()
	{
		try
		{
			clientSocket.close();
			System.out.println("서버와 연결을 종료했습니다.");
		}
		catch(IOException e)
		{ System.out.println("ERROR!"); }
	}
	
	
	public static void main(String[] args) throws Exception
	{
		Scanner sc = new Scanner(System.in);
		
		
		// 새로운 client에서 server(port: 9999) 연결
		Client main = new Client();
		main.connect();
		
		// chat_room 출력
		printList();
		
		// client가 하고 싶은 활동 선택
		String input;
		System.out.printf("채팅방 입장시 1, 채팅방 개설시 2, 친구 초대시 3을 입력해주세요! ");
		input = sc.nextLine();
		
		while(true)
		{
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
			oos.writeObject(input);
			if(input.equals("1"))	// 이후 '사용자/친구 관리'에서 사용할 기능
			{
				System.out.printf("참여하고 싶은 채팅방의 id를 입력해주세요! ");	// 이후 클릭한 채팅방의 id를 자동적으로 읽어오도록 설정
				input = sc.nextLine();
				str = input;
				// server에 chat_room의 id 전달
				oos = new ObjectOutputStream(clientSocket.getOutputStream());
				oos.writeObject(str);
				// server에서 chat_room의 title 전달 받기
				ois = new ObjectInputStream(clientSocket.getInputStream());
				str = String.valueOf(ois.readObject());
				input = str;
				// server에서 자동적으로 접속할 port 번호 받기
				ois = new ObjectInputStream(clientSocket.getInputStream());
				str = String.valueOf(ois.readObject());
				port = Integer.valueOf(str);
				// 채팅을 위한 thread 실행
				Client_chat thread = new Client_chat(input, port);
				main.disconnect();	// 연결 안 끊으면 충돌 발생
				thread.run();
				main.connect();
				System.out.println("서버 재연결 완료");
			}
			else if(input.equals("3"))
			{
				System.out.printf("참여시키고 싶은 채팅방의 id를 입력해주세요! ");	// 이후 클릭한 채팅방의 id를 자동적으로 읽어오도록 설정
				input = sc.nextLine();
				str = input;
				// server에 chat_room의 id 전달
				oos = new ObjectOutputStream(clientSocket.getOutputStream());
				oos.writeObject(str);
				
				System.out.printf("참여시키고 싶은 친구 id를 입력해주세요! ");
				input = sc.nextLine();
				str = input;
				// server에 user의 id 전달
				oos = new ObjectOutputStream(clientSocket.getOutputStream());
				oos.writeObject(str);
				
				System.out.println("success");
			}
			else
			{
				while(true)
				{
					System.out.printf("채팅빙을 만들고 싶은 사람의 이름을 작성해주세요! ");	// 이후 클릭한 user의 id를 자동적으로 읽어오도록 설정
					input = sc.nextLine();
					if(input.equals(user_name))
					{ System.out.println("본인의 이름 입력 불가!"); }
					else
					{
						str = input;
						// server에 user_name 전달
						oos = new ObjectOutputStream(clientSocket.getOutputStream());
						oos.writeObject(user_name);
						// server에 chat_user_id 전달
						oos = new ObjectOutputStream(clientSocket.getOutputStream());
						oos.writeObject(str);
						// server에서 db에 접근하여 확인
						// server에서 db 결과값 받기
						ois = new ObjectInputStream(clientSocket.getInputStream());
						str = String.valueOf(ois.readObject());
						
						if(str.equals("false"))	// 새로운 채팅방 생성
						{
							System.out.printf("원하는 채팅방 이름을 입력해주세요! ");
							input = sc.nextLine();
							if(input.equals(""))	// 사용자가 이름을 입력하지 않을 시 주어지는 기본 title
							{ input = "채팅방"; }
							input += "(현재 채팅 인원: 2명)";
							// server에 room_title 전달
							str = input;
							oos = new ObjectOutputStream(clientSocket.getOutputStream());
							oos.writeObject(str);
						}
						else	// 이미 해당 채팅방 존재
						{ System.out.println("이미 존재하는 채팅방입니다!"); }
						
						// server에서 자동적으로 접속할 port 번호 받기
						ois = new ObjectInputStream(clientSocket.getInputStream());
						str = String.valueOf(ois.readObject());
						port = Integer.valueOf(str);
						// 채팅을 위한 thread 실행
						Client_chat thread = new Client_chat(input, port);
						main.disconnect();
						thread.run();
						main.connect();
						System.out.println("서버 재연결 완료");
						
						break;
					}
				}
			}
		}
	}
}