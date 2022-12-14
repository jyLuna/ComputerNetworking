package chatting;

import java.net.*;
import java.io.*;

// 클라이언트가 소켓을 통해 접속되면 객체를 생성하고 전체 메시지를 리턴하는 구조를 연동하게 되는데
// 서버를 연동할 때, 클라이언트가 접속되어 연결하는 코드를 작성한 클래스이다.
// 메시지를 관리하는 스트림을 지정하여 클라이언트가 보낸 메세지를 ChatServer가 가진 메소드를 통해 전달하도록 지정한 클래스이다.

public class ServiceThread extends Thread
{
	
private ChatServerThread server;	// 클라이언트의 정보(추가, 클라이언트 삭제, 모든 메시지 관리)
private Socket socket;

	String UserName; // 클라이언트 이름
	String UserNameDeleted;

	PrintWriter out;   
	BufferedReader in;
	FileInputStream fileIn;

	public ServiceThread(ChatServerThread server, Socket socket)
	{
		this.server = server;
		this.socket = socket;
		//System.out.println("PASS --> server, socket 저장 ok");
	}

	// 클라이언트가 메시지를 입력하게 되면 out 객체에 출력하는 메소드 
	public void sendMessage(String msg) throws IOException
	{
		if (out != null)
		{ out.println(msg); }
	}	

	@Override
	public void run()	// 클라이언트 창에 출력하는 코드를 작성
	{
		try
		{
			//System.out.println("클라이언트 | " + socket + "에서 접속하였습니다.");

			// 메시지 입출력 객체를 소켓에서 받아온다
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// 줄바꿈
			out.println();
			out.println("UserName:");
			UserName = in.readLine();
			//System.out.println("PASS --> client 화면에서 username 받기(username: " + UserName + ")");
			sendMessage(UserName + "\n");	// userName을 전달하여 클라이언트 창에 출력
			server.sendMessageAll("# " + UserName + " 님이 들어오셨습니다");
			String inputLine;
			while ((inputLine = in.readLine()) != null)
			{
				if(inputLine.equals("exit"))	// client의 퇴장("exit" 입력시)
				{
					out.println("exit");
					server.removeClient(this); // 클라이언트 삭제
					server.sendMessageAll("# " + UserName + " 님이 나가셨습니다");
					System.out.println("클라이언트 " + socket + "에서 접속이 끊겼습니다...");
				}
				else if(inputLine.equals("file"))	// file 보내기("file" 입력시)
				{
					out.println("file");
					server.sendMessageAll("# " + UserName + " 님이 파일 전송을 시도합니다");
					System.out.println("클라이언트 " + socket + "에서 파일 전송을 시도합니다");
					
					// file 받기
					//fileIn = new FileInputStream();
				}
				else
				{ server.sendMessageAll("[" + UserName + "] " + inputLine); }
			}
			out.close();
			in.close();
			socket.close();

		}
		catch (IOException e)
		{
			server.removeClient(this); // 클라이언트 삭제
			server.sendMessageAll("# " + UserName + " 님이 나가셨습니다");
			System.out.println("클라이언트 " + socket + "에서 접속이 끊겼습니다...");
		}
	}
}