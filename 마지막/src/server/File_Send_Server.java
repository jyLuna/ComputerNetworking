package server;

import java.net.*;
import java.io.*;

public class File_Send_Server extends Thread
{
	Socket socket = null;
	private int port = 0;
	private String path = "";
	
	public File_Send_Server(int port, String path)
	{
		this.port = port;
		this.path = path;
	}
	
	
	// 파일 읽고 전송
	private void fileSend()
	{
		Socket socket = new Socket();
		FileInputStream fis = null;
		
		File f = new File(path);
		// 파일 읽기
		try
		{ fis = new FileInputStream(f); }
		catch(FileNotFoundException e)
		{ e.printStackTrace(); }
		BufferedInputStream bis = new BufferedInputStream(fis);
		
		// 파일 데이터 전송
		byte[] data = new byte[1024];
		try
		{
			DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			int len;
			while((len = bis.read(data)) != -1)
			{
				dos.write(data, 0, len);  //데이터 내용 전송
			}
		}
		catch(IOException e)
		{ e.printStackTrace(); }
	}
	
	public void run()
	{
		
	}
}