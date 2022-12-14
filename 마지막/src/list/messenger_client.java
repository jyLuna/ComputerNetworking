import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.*;

public class messenger_client {
	
	static int fcount;

	static String[][] flist; 
	static String[] myinfo;
	
	public static void infosplit(String info, int i) { // infosplit()�� �����κ��� ���۹��� ģ�� ������ ������ �������� split�Ͽ� 2���� �迭�� ���ʴ�� ������
		StringTokenizer st = new StringTokenizer(info, " ");
		String temp;
		
			for (int j = 0; j < 6; j++) {
				

				temp = st.nextToken();
		//		System.out.println(temp);
				flist[i][j] = temp;
			}
			
			
		
	}
	
	public static void MY_infosplit(String info) { // infosplit()�� �����κ��� ���۹��� ģ�� ������ ������ �������� split�Ͽ� 2���� �迭�� ���ʴ�� ������
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
		msg0 = "�α��ο� �����Ͽ����ϴ�.\nģ������� �ҷ����� �� ...";
		msg1 = "�α��ο� �����Ͽ����ϴ�.\n�����ڿ��� �����ϼ���"; // switch�� case(1) / LOGINbit = 1
		
		
		try {
			socket = new Socket("localhost", 7777); // ���� ����
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			fcount = 0;

			
			
			while(true) { // �۵���
				
			System.out.println("Enter your ID :");
			String ID = scanner.nextLine();
			System.out.println( ID + " ��, ȯ���մϴ�. ��й�ȣ�� �Է��� �ּ���" );
			out.write(ID + "\n");
			out.flush();
			String PW = scanner.nextLine(); // ������ GUI ���� �޾ƾ� ������ �׽�Ʈ�� ���ؼ� ��ĳ�ʷ� �Է��ϵ��� �߽��ϴ�
			System.out.println("You entered "+PW);
			out.write(PW + "\n");
			out.flush();	
				
			int LOGINbit = in.read(); // LOGIN ���¸� ��Ÿ���� bit�� ���� ���۹���

			switch (LOGINbit) {
			//the variable 'LOGINbit' is an integer number that notices the error occurred or not. 
			
			case 0 : { // the normal case (Login Succeed) - ���⿡ �α��� ���� �� ������ ��� ������ ���ϴ�. 
				
				System.out.println(msg0); // ������ �߰��ϸ鼭 Ÿ�Ӿƿ� ��� �߰� ���� (�α��� �õ� �ÿ���)
				String loadedflist;

				System.out.println("\nMY INFORMATION ==================");
				myinfo = new String [7];
				loadedflist = in.readLine();
				MY_infosplit(loadedflist);
				System.out.println(Arrays.deepToString(myinfo));
				
				System.out.println("");
				

				System.out.println("FRIENDS LIST ==================");
				fcount = in.read(); // ģ�� ���� ����
				String notice1 = fcount+" friends here!";
				System.out.println(notice1);
				
				
				
				flist = new String[fcount][6];
				
				for (int i =0; i<fcount; i++) {
					
					loadedflist = in.readLine();
					System.out.println(loadedflist);
					infosplit(loadedflist, i);
				}
				

				System.out.println(Arrays.deepToString(flist));
				
				// ģ�� ����Ʈ�� flist�� �ึ�� �� ģ�� �� ���� ������ ���ԵǾ� �ֽ��ϴ�. �� �྿ ��Ÿ������ GUI �������� ������ �ֽø� ���� �� �����ϴ�
				
				// ===================== SELECT THE MENU ====================
				
				// 1: ���� �˻�(+ģ�� �߰�) , 2: ģ�� ���� , 3: ���� ���� , 4: ä�� ����
				
				System.out.println("===========SELECT NEXT ACTION===========");
				String notice4 = "\n�޴��� ������ �ּ��� (1: ���� �˻�(+ģ�� �߰�) , 2: ģ�� ���� , 3: ���� ���� , 4: ä�� ���� , 5: �α׾ƿ�) : ";
				System.out.println(notice4);
				int select = scanner.nextInt();
				scanner.nextLine();
				out.write(select);
				out.flush();
				
				switch(select){
				
				case 1: {}
				
				case 2: {}
				
				case 3: {
					System.out.println("�̸�(1),�г���(2),�̸��� �ּ�(3),��ȭ��ȣ(4),���� �޽���(5) �� �����ϰ� ���� ���� �Է��ϼ���. : ");
					
					// GUI ���� �� ��ư���� ���� ���� �ٿ� ������ �����մϴ�. (���ǻ� print�� �ۼ���)
					String tochange;
					int se_select = scanner.nextInt();
					out.write(se_select);
					out.flush();
					scanner.nextLine();
					
					String notice2 = "������ ���� �Է��� �ּ���. \n";
					System.out.print(notice2);
					
					tochange = scanner.nextLine();
					out.write(tochange+"\n");
					out.flush();
					//  ���� �Ϸ�

					
					System.out.print("�ݿ��Ǿ����ϴ�.\n\n\n");
					
					
					
				break;}
				
				case 4: {}
				
				case 5: {
					
					System.out.println("�α׾ƿ� �մϴ�. �ʱ� ȭ������ �ǵ��ư��ϴ� ... \n\n");
					break;}
				
				default : {
					
					String notice3 = "������ �߻��Ͽ����ϴ�. �ٽ� �õ��� �ּ���";
					System.out.println(notice3);
					
				}
				
				}
			
				
			
			break;}
			
			case 1 : { // �α��� ���� (default 1, ���� ���� �з� �� case�� �߰��� �� �ֽ��ϴ�)
				System.out.println(msg1); 
			
			break;}
			
			default : {
				
				System.out.println("�� �� ���� ������ �߻��Ͽ����ϴ�."); 
			
			break;}
			}
			
			}
		}
		catch(IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				scanner.close();
				if(socket != null) socket.close(); // Ŭ���̾�Ʈ ���� �ݱ�
				
			} catch(IOException e) {
				System.out.println("������ �߻��߽��ϴ�.");
			}
		}
		
	}
}