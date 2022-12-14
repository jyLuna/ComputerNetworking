package chat;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
//유저 정보를 소켓으로 넘겨주기
public class Crateuser implements Runnable{
	Userinfo user;
	private PrintWriter writer=null;
	private BufferedReader reader;
	private Socket socket=null;
	public Crateuser(Socket socket, Userinfo user){
		this.socket=socket;
		this.user=user;
		try{
			writer = new PrintWriter(socket.getOutputStream());
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public void sendMsg(String msg){
		writer.print(msg+"\n");
		writer.flush();
	}
	public void run(){
		
	}
}
class Hashset extends Thread{
	Userinfo user=null;
	Socket socket=null;
	ObjectInputStream reader = null;
	String name = null;
	String secnum = null;
	String ID = null;
	HashSet<String> set1=null;
	HashSet<String> set2=null;
	HashSet<String> set3=null;
	
	Hashset(Socket socket, String name,String secnum,String ID){
		this.socket = socket;
		this.ID=ID;
		this.secnum=secnum;
		this.name=name;
	}
	public void run(){
		try{
			reader = new ObjectInputStream(socket.getInputStream());
			set1 = new HashSet<String>();
			set2 = new HashSet<String>();
			while(true){
				 user = (Userinfo) reader.readObject();
				 name = user.name;
				 secnum = user.secnum;
				 ID = user.ID;				 
				 set1.add(name);
				 set2.add(secnum);
				 set3.add(ID);
			}
		}
		catch(EOFException e){
		}
		catch(FileNotFoundException e){
			System.out.println("파일이 존재 하지 않습니다.");
		}
		catch(IOException e){
			System.out.println("파일을 읽을수 없습니다.");
		}
		catch(ClassNotFoundException e){
			System.out.println("해당 클래스가 존재하지 않습니다.");
		}
		finally{
			try{
				reader.close();
			}
			catch(Exception e){
			}
		}
	}
}
