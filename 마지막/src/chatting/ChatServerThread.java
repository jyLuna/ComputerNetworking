package chatting;

import java.net.*;
import java.util.Vector;
import java.io.*;
import java.util.Random;

public class ChatServerThread	extends Thread
{
	Vector<ServiceThread> Clients;
	
	// 현재 채팅방의 인원수
	private static int numOfClient = 0;

	public ChatServerThread()
	{ Clients = new Vector<>(); }

	public void addClient(ServiceThread st)	// 클라이언트 추가
	{ Clients.addElement(st); }

	public void removeClient(ServiceThread st)	// 클라이언트 삭제 
	{ Clients.removeElement(st); }

	public void sendMessageAll(String msg)	// 전체 메시지 출력 
	{
		try
		{
			for (int i = 0; i < Clients.size(); i++)
			{
				ServiceThread st = ((ServiceThread) Clients.elementAt(i));	// 존재하는 모든 thread에
				st.sendMessage(msg);
			}
		}
		catch (Exception e)
		{ e.printStackTrace(); }
	}
	
	
	ChatServerThread server;
	ServerSocket serverSocket = null;
	Socket serviceSocket;
	
	@Override
	public void run()
	{
		// port 값 랜덤하게 얻기(범위는 1000 ~ 9999)
		Random mrandom = new Random(); 
		int port = 1000 + mrandom.nextInt(9000);
		System.out.println("port: #" + port);
		server = new ChatServerThread();	// 클라이언트를 관리하는 객체(추가, 삭제, 메시지 전달)
		try
		{ serverSocket = new ServerSocket(port); }
		catch (Exception e)
		{
			System.err.println("연결에 실패했습니다.");
			System.exit(1);
		}
		//System.out.println("서버 | " + serverSocket + "에서 연결을 기다립니다.");
		try
		{
			while (true)
			{
				serviceSocket = serverSocket.accept();	// 클라이언트 접속
				// 현재 방의 인원수 업데이트 및 client에 값 전송
				numOfClient++;
				ObjectOutputStream oos = new ObjectOutputStream(serviceSocket.getOutputStream());
				oos.writeObject(numOfClient);
				oos.flush();
				/* // 현재 방의 port 번호 전달
				oos = new ObjectOutputStream(serviceSocket.getOutputStream());
				oos.writeObject(port);
				oos.flush(); */

				// 클라이언트 관리하는 객체, 클라이언트를 가진 객체를 생성한 후
				// Thread를 통해서 작업을 (읽고 쓰는) Run 메소드를 명시한 후 실행한다.
				ServiceThread thread = new ServiceThread(server, serviceSocket);
				thread.start();	// run을 호출한다.
				//System.out.println("PASS --> run 시작까지는 ok");
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
		System.out.println("서버를 종료합니다.");
	}
}