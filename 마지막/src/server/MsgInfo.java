package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;


public class MsgInfo {
	/*ChatRoom*/
	public static String GOWAIT;
	public static String ADDCHAT;
	public static String MAKECHAT;
	public static String CHATQUESTION;
	public static String KICK;
	/*ManToManChat_Send*/
	public static String MANTOMAN;
	public static String EXITCHAT;
	/*File_Receive_Client*/
	public static String SHOWUSER;
	/*MultiClient*/
	public static String NEW;
	/*Listener_Of_Client*/
	public static String MAIN;
	public static String USERLIST;
	public static String ROOMLIST;
	public static String MAKEROOM;
	public static String ENTER;
	public static String CHATUSER;
	public static String SENDMEMO;
	public static String FILEQUESTION;
	public static String CHIEF;
	public static String SELUSER;
	
	public static String CHAT;
	public static String EXIT;
	
	
	private static MsgInfo server;
	private static ServerSocket serverSocket = null;
	private static Socket serviceSocket;
	
	
	public void main(String[] args)
	{
		while(true)
		{
			server = new MsgInfo();
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
				try
				{
					serviceSocket = serverSocket.accept();	// 클라이언트 접속
				}
				catch(IOException e)
				{ e.printStackTrace(); }
			}
		}
	}
}