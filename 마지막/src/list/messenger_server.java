import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.*;
import java.util.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

	
public class messenger_server {
	
	
	
	// ========== DATABASE SETTING ===================
		static Connection con = null;
		static Statement stmt = null;
		static String url = "jdbc:mysql://localhost/b'messenger_schema'?serverTimezone=Asia/Seoul";	// ? 앞에 스키마 이름 
		static String user = "root";
		static String passwd = "";		//MySQL root 비밀번호 적어주세요

		static messenger_server db = new messenger_server();
		
	// ============ LOGIN SETTING ================== / global var
		
		static int LOGINbit; // 0이면 login success, 1이면 fail -> default 1 
		// Login Error case에 따라서 switch문 적용하고 error 유형에 따른 메시지 출력 (ex) 존재하지 않는 ID, 비밀번호 틀림 등
		static HashMap<Integer, String> flist = new HashMap<Integer, String>();
	
		static String UserID;
		
		
	// ============== USER INFO SETTING ===================
		
		static int fcount = 0;
		
		
		public static void printMap(HashMap<Integer, String> flist) {
			System.out.println("\n=======Here is the user's friends list ");
			for (Integer Keyindex : flist.keySet()) { // 이 부분 중요한듯
				String fID = flist.get(Keyindex);
				System.out.println("friend --> " + fID );
				
			}
			
			System.out.println();
		}
	public static int loadingFriendsList (){

		
		try {
		flist.clear();
		String loadfriendslist = "SELECT addressee_id FROM friendship WHERE requester_id =\'" + UserID + "\'";
		ResultSet rs_flist = stmt.executeQuery(loadfriendslist);
		
		
		while (rs_flist.next()) {
			
			flist.put(fcount, rs_flist.getString("addressee_id"));
			fcount++;
			
		}
		}catch(Exception e) {
		System.out.println("FAILED (loadingFriendsList) : " + e.toString());
		}
		
		return fcount;
	}
	
	public static void main(String[] args) throws Exception{
		ServerSocket listener = new ServerSocket(7777); 
		System.out.println("The server is running...");
		ExecutorService pool = Executors.newFixedThreadPool(20); 

		while(true){ 
			Socket sock = listener.accept();
		    pool.execute(new Capitalizer(sock)); // the operating part of thread server
		    }
		
		}
	
	private static class Capitalizer implements Runnable{
		private Socket socket;
		Capitalizer (Socket socket){
			this.socket = socket;
		}
		
		@Override
		public void run() {
			
			// Socket set
			BufferedReader in = null;
			BufferedWriter out = null;
			// I/O, DB set
			Scanner scanner = new Scanner(System.in);
			
			// User Info set                 
			String Password;

			String[] myinfoset = {"user_name","nickname","email_address","hp","message"};
			
			
			
			try {
				System.out.println("Connected.");
				in = new BufferedReader (new InputStreamReader(socket.getInputStream()));
				out = new BufferedWriter (new OutputStreamWriter(socket.getOutputStream()));
				//데이터베이스 연결
				Class.forName("com.mysql.cj.jdbc.Driver");
				db.con = DriverManager.getConnection(db.url, db.user, db.passwd);
				db.stmt = db.con.createStatement();
				
				
				while(true) {

					System.out.println("Now waiting for user input ID ... ");
					UserID = in.readLine();
					System.out.println("The user entered for ID : " + UserID);
					System.out.println("Now waiting for user input Password ... ");
					Password = in.readLine();
					System.out.println("The user entered PW. ");
					
					System.out.println("now the login process will be start ... ");

					String Online = "UPDATE user SET statement='1' WHERE user_id =\"" + UserID + "\""; // 1은 online / 끊어도 실행되니 사이에 User ID를 넣으면 된다
					String Offline = "UPDATE user SET statement='0' WHERE user_id =\"" + UserID + "\"";  // 0은 offline
					LOGINbit =0 ; // 로그인 성공 고정 
					
					// 로그인 성공 이후부터 진행 
					switch (LOGINbit) {
					//the variable 'LOGINbit' is an integer number that notices the error occurred or not. 
					
					case 0 : { // the normal case (Login Succeed) - 여기에 로그인 성공 시 이후의 모든 동작이 들어갑니다. 
						
						out.write(LOGINbit); 
						out.flush();
						stmt.executeUpdate(Online); // On/Offline bit를 바꿔준다
						String loadfriendslist = "SELECT addressee_id FROM friendship WHERE requester_id =\'" + UserID + "\'";
						ResultSet rs_fID = stmt.executeQuery(loadfriendslist);
						
						
						db.loadingFriendsList();
						printMap(flist);
						System.out.println("The data of friends =========== ");
						

						// ================ MY INFORMATION ===============	
							String myinfo = "SELECT * FROM user WHERE user_id = \'" +UserID+"\'";
							ResultSet rs_myinfo = stmt.executeQuery(myinfo);
							
							while (rs_myinfo.next()) {
								
								String each_myinfo = rs_myinfo.getString("user_name")+" "+rs_myinfo.getString("nickname")+" "+rs_myinfo.getString("email_address")+" " +rs_myinfo.getString("hp")+" "+rs_myinfo.getString("statement")+" "+rs_myinfo.getString("birth_date")+" "+rs_myinfo.getString("message");
								System.out.println(each_myinfo);
								System.out.println("");

								out.write(each_myinfo + "\n");
								}
								out.flush();
							
								
	
						
						
					// ============= FRIENDS LIST ===================	
						ResultSet rs_flist;
						String each_friendsinfo;

						out.write(fcount);
						out.flush(); // 친구 수 보냄
						
						int i = 0;
						while (i < fcount) {
							
								String loadfriendsinfo = "SELECT * FROM user WHERE user_id = \'" +flist.get(i)+"\'";
								rs_flist = stmt.executeQuery(loadfriendsinfo);
								while (rs_flist.next()) {
								
								each_friendsinfo = rs_flist.getString("user_name")+" "+rs_flist.getString("nickname")+" "+rs_flist.getString("email_address")+" " +rs_flist.getString("hp")+" "+rs_flist.getString("statement")+" "+rs_flist.getString("message");
								System.out.println(each_friendsinfo);
								System.out.println("");

								out.write(each_friendsinfo + "\n");
								}
								i++;
									}

						out.flush();
						
						
						//=============== MENU SELECTION 
						
						int select = in.read();
						System.out.println("===========SELECT MENU=========");
						System.out.println("select value is " +select);
						switch(select){
						
						case 1: {}
						
						case 2: {}
						
						case 3: {
							
							int se_select = in.read();
							System.out.println("se_select value is " + se_select);
							
							String tochange = in.readLine();
							System.out.println("tochange value is " + tochange);

							System.out.println(myinfoset[se_select-1]);
							
							String Changing = "UPDATE user SET "+myinfoset[se_select-1]+"= \"" + tochange + "\" WHERE user_id = \"" + UserID + "\"";
							System.out.println(Changing);
							stmt.executeUpdate(Changing);
							
							rs_myinfo = stmt.executeQuery(myinfo);
							
							while (rs_myinfo.next()) {
								
								String each_myinfo = rs_myinfo.getString("user_name")+" "+rs_myinfo.getString("nickname")+" "+rs_myinfo.getString("email_address")+" " +rs_myinfo.getString("hp")+" "+rs_myinfo.getString("statement")+" "+rs_myinfo.getString("birth_date")+" "+rs_myinfo.getString("message");
								System.out.println("변경된 내 정보 = " + each_myinfo);
								System.out.println("");

																
								}

							
							
							
							
						break;}
						
						case 4: {}
						
						case 5: {
							
							System.out.println("The user selected to logout. ===========\n\n");
							break;}
						
						default : {
							
							String notice3 = "오류가 발생하였습니다. 다시 시도해 주세요";
							System.out.println(notice3);
							
						}
						
						}
						
						
						
						
						
						flist.clear(); // 전송했던 친구목록을 비운다
						stmt.executeUpdate(Offline); // On/Offline bit를 바꿔준다 // 종료하고 로그아웃		
					break;}
					
					
					case 1 : { // 로그인 실패 (default 1, 세부 에러 분류 시 case를 추가할 수 있습니다)
						out.write(LOGINbit + "\n"); 
						out.flush();
						
					
					break;}
					
					// the value of the variable ErrOccured is not 0 or 1 
					default : {
						
						System.out.println("알 수 없는 에러가 발생하였습니다."); 
					
					break;}
					}
					
					
					out.flush();
				}		
				
			}  catch (Exception e) {
				System.out.println("Error:" + socket);
			} finally {
				try {
					socket.close();
					db.stmt.close();
					db.con.close();				
					} 
				catch (Exception e){}
				System.out.println("Closed:" + socket);
				
			}
			
		}
		

	}


}