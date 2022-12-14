import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.*;

public class messenger_client {
	
	static int fcount;

	static String[][] flist; 
	static String[] myinfo;
	
	public static void infosplit(String info, int i) { // infosplit()은 서버로부터 전송받은 친구 정보를 공백을 기준으로 split하여 2차원 배열에 차례대로 저장함
		StringTokenizer st = new StringTokenizer(info, " ");
		String temp;
		
			for (int j = 0; j < 6; j++) {
				

				temp = st.nextToken();
		//		System.out.println(temp);
				flist[i][j] = temp;
			}
			
			
		
	}
	
	public static void MY_infosplit(String info) { // infosplit()은 서버로부터 전송받은 친구 정보를 공백을 기준으로 split하여 2차원 배열에 차례대로 저장함
		StringTokenizer st = new StringTokenizer(info, " ");
		String temp;
		
			for (int j = 0; j < 6; j++) {
				

				temp = st.nextToken();
		//		System.out.println(temp);
				myinfo[j] = temp;
			}
			
			
		
	}

	public static void main (String[] args) {
		
		// ============== Socket Setting 
		BufferedReader in = null;
		BufferedWriter out = null;
		Socket socket = null;
		Scanner scanner = new Scanner(System.in);
		
		// ============== User Message (includes Err msg) 
		String msg0, msg1;
		msg0 = "로그인에 성공하였습니다.\n친구목록을 불러오는 중 ...";
		msg1 = "로그인에 실패하였습니다.\n관리자에게 문의하세요"; // switch문 case(1) / LOGINbit = 1
		
		
		try {
			socket = new Socket("localhost", 7777); // 소켓 세팅
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			fcount = 0;

			
			
			while(true) { // 작동부
				
			System.out.println("Enter your ID :");
			String ID = scanner.nextLine();
			System.out.println( ID + " 님, 환영합니다. 비밀번호를 입력해 주세요" );
			out.write(ID + "\n");
			out.flush();
			String PW = scanner.nextLine(); // 원래는 GUI 통해 받아야 하지만 테스트를 위해서 스캐너로 입력하도록 했습니다
			System.out.println("You entered "+PW);
			out.write(PW + "\n");
			out.flush();	
				
			int LOGINbit = in.read(); // LOGIN 상태를 나타내는 bit을 먼저 전송받음

			switch (LOGINbit) {
			//the variable 'LOGINbit' is an integer number that notices the error occurred or not. 
			
			case 0 : { // the normal case (Login Succeed) - 여기에 로그인 성공 시 이후의 모든 동작이 들어갑니다. 
				
				System.out.println(msg0); // 디테일 추가하면서 타임아웃 기능 추가 예정 (로그인 시도 시에도)
				String loadedflist;

				System.out.println("\nMY INFORMATION ==================");
				myinfo = new String [7];
				loadedflist = in.readLine();
				MY_infosplit(loadedflist);
				System.out.println(Arrays.deepToString(myinfo));
				
				System.out.println("");
				

				System.out.println("FRIENDS LIST ==================");
				fcount = in.read(); // 친구 수를 받음
				String notice1 = fcount+" friends here!";
				System.out.println(notice1);
				
				
				
				flist = new String[fcount][6];
				
				for (int i =0; i<fcount; i++) {
					
					loadedflist = in.readLine();
					System.out.println(loadedflist);
					infosplit(loadedflist, i);
				}
				

				System.out.println(Arrays.deepToString(flist));
				
				// 친구 리스트인 flist의 행마다 각 친구 한 명의 정보가 포함되어 있습니다. 한 행씩 나타나도록 GUI 디자인을 설정해 주시면 좋을 것 같습니다
				
				// ===================== SELECT THE MENU ====================
				
				// 1: 유저 검색(+친구 추가) , 2: 친구 삭제 , 3: 정보 변경 , 4: 채팅 시작
				
				System.out.println("===========SELECT NEXT ACTION===========");
				String notice4 = "\n메뉴를 선택해 주세요 (1: 유저 검색(+친구 추가) , 2: 친구 삭제 , 3: 정보 변경 , 4: 채팅 시작 , 5: 로그아웃) : ";
				System.out.println(notice4);
				int select = scanner.nextInt();
				scanner.nextLine();
				out.write(select);
				out.flush();
				
				switch(select){
				
				case 1: {}
				
				case 2: {}
				
				case 3: {
					System.out.println("이름(1),닉네임(2),이메일 주소(3),전화번호(4),상태 메시지(5) 중 변경하고 싶은 것을 입력하세요. : ");
					
					// GUI 적용 시 버튼마다 상기된 값을 붙여 서버에 전달합니다. (편의상 print로 작성됨)
					String tochange;
					int se_select = scanner.nextInt();
					out.write(se_select);
					out.flush();
					scanner.nextLine();
					
					String notice2 = "변경할 값을 입력해 주세요. \n";
					System.out.print(notice2);
					
					tochange = scanner.nextLine();
					out.write(tochange+"\n");
					out.flush();
					//  변경 완료

					
					System.out.print("반영되었습니다.\n\n\n");
					
					
					
				break;}
				
				case 4: {}
				
				case 5: {
					
					System.out.println("로그아웃 합니다. 초기 화면으로 되돌아갑니다 ... \n\n");
					break;}
				
				default : {
					
					String notice3 = "오류가 발생하였습니다. 다시 시도해 주세요";
					System.out.println(notice3);
					
				}
				
				}
			
				
			
			break;}
			
			case 1 : { // 로그인 실패 (default 1, 세부 에러 분류 시 case를 추가할 수 있습니다)
				System.out.println(msg1); 
			
			break;}
			
			default : {
				
				System.out.println("알 수 없는 에러가 발생하였습니다."); 
			
			break;}
			}
			
			}
		}
		catch(IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				scanner.close();
				if(socket != null) socket.close(); // 클라이언트 소켓 닫기
				
			} catch(IOException e) {
				System.out.println("오류가 발생했습니다.");
			}
		}
		
	}
}