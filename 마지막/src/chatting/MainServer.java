package chatting;

import java.net.*;
import java.util.Vector;
import java.io.*;
import java.util.Scanner;

public class MainServer
{
	private static int numOfChat = 0;
	
	Vector<ChatServerThread> Chat;

	public MainServer()
	{ Chat = new Vector<>(); }
	
	public void addChat(ChatServerThread cst)	// 클라이언트 추가
	{ Chat.add(cst); }

	public void removeChat(ChatServerThread cst)	// 클라이언트 삭제 
	{ Chat.remove(cst); }
	
	public static void startChat()
	{
		System.out.println("Type 1 if you want to start " + (numOfChat+1) + "-th chatting!");
		Scanner sc = new Scanner(System.in);
		int start = sc.nextInt();
		
		if(start == 1)
		{
			MainServer ms = new MainServer();
			numOfChat++;
			ChatServerThread server = new ChatServerThread();
			server.start();
			System.out.println("PASS --> run 시작까지는 ok");
			ms.addChat(server);
			System.out.println("PASS --> vector에 thread 추가 ok");
			System.out.println("서비스를 시작합니다.\n");
			startChat();
		}
		else
		{
			System.out.println("서비스를 종료합니다.");
		}
	}
	
	public static void main(String[] args) throws Exception	// 클라이언트 창에 출력하는 코드를 작성
	{
		startChat();
	}
}