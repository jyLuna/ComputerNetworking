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
		static String url = "jdbc:mysql://localhost/b'messenger_schema'?serverTimezone=Asia/Seoul";	// ? �տ� ��Ű�� �̸� 
		static String user = "root";
		static String passwd = "";		//MySQL root ��й�ȣ �����ּ���

		static messenger_server db = new messenger_server();
		
	// ============ LOGIN SETTING ================== / global var
		
		static int LOGINbit; // 0�̸� login success, 1�̸� fail -> default 1 
		// Login Error case�� ���� switch�� �����ϰ� error ������ ���� �޽��� ��� (ex) �������� �ʴ� ID, ��й�ȣ Ʋ�� ��
		static HashMap<Integer, String> flist = new HashMap<Integer, String>();
	
		static String UserID;
		
		
	// ============== USER INFO SETTING ===================
		
		static int fcount = 0;
		
		
		public static void printMap(HashMap<Integer, String> flist) {
			System.out.println("\n=======Here is the user's friends list ");
			for (Integer Keyindex : flist.keySet()) { // �� �κ� �߿��ѵ�
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
				//�����ͺ��̽� ����
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

					String Online = "UPDATE user SET statement='1' WHERE user_id =\"" + UserID + "\""; // 1�� online / ��� ����Ǵ� ���̿� User ID�� ������ �ȴ�
					String Offline = "UPDATE user SET statement='0' WHERE user_id =\"" + UserID + "\"";  // 0�� offline
					LOGINbit =0 ; // �α��� ���� ���� 
					
					// �α��� ���� ���ĺ��� ���� 
					switch (LOGINbit) {
					//the variable 'LOGINbit' is an integer number that notices the error occurred or not. 
					
					case 0 : { // the normal case (Login Succeed) - ���⿡ �α��� ���� �� ������ ��� ������ ���ϴ�. 
						
						out.write(LOGINbit); 
						out.flush();
						stmt.executeUpdate(Online); // On/Offline bit�� �ٲ��ش�
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
						out.flush(); // ģ�� �� ����
						
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
								System.out.println("����� �� ���� = " + each_myinfo);
								System.out.println("");

																
								}

							
							
							
							
						break;}
						
						case 4: {}
						
						case 5: {
							
							System.out.println("The user selected to logout. ===========\n\n");
							break;}
						
						default : {
							
							String notice3 = "������ �߻��Ͽ����ϴ�. �ٽ� �õ��� �ּ���";
							System.out.println(notice3);
							
						}
						
						}
						
						
						
						
						
						flist.clear(); // �����ߴ� ģ������� ����
						stmt.executeUpdate(Offline); // On/Offline bit�� �ٲ��ش� // �����ϰ� �α׾ƿ�		
					break;}
					
					
					case 1 : { // �α��� ���� (default 1, ���� ���� �з� �� case�� �߰��� �� �ֽ��ϴ�)
						out.write(LOGINbit + "\n"); 
						out.flush();
						
					
					break;}
					
					// the value of the variable ErrOccured is not 0 or 1 
					default : {
						
						System.out.println("�� �� ���� ������ �߻��Ͽ����ϴ�."); 
					
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