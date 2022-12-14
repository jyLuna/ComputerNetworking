package chatting;

import java.io.*;
import java.net.*;

public class Server
{
	private static void connect()
	{
		try(ServerSocket s = new ServerSocket(9999);)
		{
			Socket sm = s.accept();
			InputStream iis = sm.getInputStream();
			DataInputStream dis = new DataInputStream(iis);
			System.out.println(dis.readUTF());
			dis.close();
		}
		catch(Exception e)
		{ System.out.println(e); }
	}
	
	public static void main(String[] args)
	{
		Server main = new Server();
		main.connect();
	}
}