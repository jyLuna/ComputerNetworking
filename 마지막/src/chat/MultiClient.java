package chat;


import java.io.*;
import java.net.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import server.MsgInfo;

class InputId extends JFrame implements ActionListener{
	JTextField id;			//아이디 입력란
	JPasswordField ps;		//패스워드 입력란(JPassword로 사용자가 입력한 내용 *로 입력되게 함)
	JButton login;			//로그인 버튼
	JButton findId;			//아이디 찾기 버튼
	JButton findPs;			//비밀번호 찾기 버튼
	JButton applyID;		//회원가입 버튼
	
	
	
	InputId(){			//InputId 생성자 함수 시작
		
		id = new JTextField(12);	//아이디최대길이 12로 만듬 GUI깔끔함을 위함
		ps = new JPasswordField(12);	//패스워드최대길이 12로 아이디와 같게하여 GUI깔끔하게 함
		login = new JButton("LOGIN");	//login 버튼(LOGIN)으로 버튼 생성
		login.addActionListener(this);	//login 버튼에 대한 리스너 추가
		JPanel idPanel = new JPanel();	//id의 패널 id텍스트에 TitledBorder을 주어 GUI깔끔하게 함
		idPanel.add(id);		//idPanel에 id만 추가
		idPanel.setBorder(new TitledBorder("ID"));  //idPanel에 TitlededBoder을 생성
		JPanel psPanel = new JPanel();		    //ps의 패널 ps패스워드텍스트에 TotleBoder을 주어 GUI 깔끔하게 함
		psPanel.add(ps);			    //psPanel에 ps만 추가
		psPanel.setBorder(new TitledBorder("PassWord"));	//psPanel에 TitledBoder을 생성
		JPanel bindPanel1 = new JPanel(new BorderLayout());	//idpanel과 pspanel을 묶기 위해 panel생성하고 Layout을 BorderLayout으로 동서남북센터 지정가능하게 함
		bindPanel1.add("North",idPanel);	//북쪽에 idPanel을 추가
		bindPanel1.add("South",psPanel);	//남쪽에 psPanel을 추가
		findId = new JButton("아이디 찾기");	//findId 버튼 생성
		findId.addActionListener(this);		//findId 버튼에 대한 리스너 추가
		findPs = new JButton("비밀번호 찾기");	//findPs 버튼 생성
		findPs.addActionListener(this);		//findPs 버튼에 대한 리스너 추가
		applyID = new JButton("회원가입");	//applyID 버튼 생성
		applyID.addActionListener(this);	//applyID 버튼에 대한 리스너 추가
		JPanel bindPanel2 = new JPanel(new BorderLayout()); //생성된 3개의 버튼을 묶기위해 panel생성하고 Layout을 BorderLayout으로 동서남북센터 지정가능하게 함
		bindPanel2.add("West", findId);		//서쪽에 findId을 추가
		bindPanel2.add("East", findPs);		//동쪽에 findPs을 추가
		bindPanel2.add("South",applyID);	//남쪽에 applyID을 추가
		
		JPanel bindPanel3 = new JPanel(new BorderLayout());	//묶여진 패널을 다시 묶기 위한 패널 생성
		bindPanel3.add("Center",bindPanel1);	//센터에 id와 ps가 들어가있는 패널을 추가
		bindPanel3.add("East", login);		//동쪽에 위에서 만들어 놓은 login버튼 추가
		bindPanel3.add("South",bindPanel2);	//남쪽에 findId,findPs,applyID가 들어가 있는 패널을 추가
		
		ImageIcon image1 = new ImageIcon("C:\\Documents and Settings\\toe\\바탕 화면\\toe.gif"); //상단에 아이콘모양설정할 그림 생성
		ImageIcon image2 = new ImageIcon("C:\\Documents and Settings\\toe\\바탕 화면\\안내.jpg");//panel로 만들어놓은 content위쪽에 들어갈 그림 생성
		JLabel backImage1 = new JLabel(image2);	//이미지를 label로 만들어 사용
		
		JPanel bindPanel4 = new JPanel(new BorderLayout());//안내의 이미지와 content를 묶을 패널 생성
		bindPanel4.add("North",backImage1);	//북쪽에 안내 그림추가
		bindPanel4.add("South",bindPanel3);	//남쪽에 content를 추가
		ImageIcon image3 = new ImageIcon("C:\\Documents and Settings\\toe\\바탕 화면\\김태희_바탕화면_korea0364.jpg");	//메인그림이 될 사진 생성
		JLabel backImage2 = new JLabel(image3);	//이미지를 label로 만들어 사용
		JPanel bindPanel5 = new JPanel(new BorderLayout()); //메인 이미지와, content와 안내그림을 묶기 위해 패널 생성
		bindPanel5.add("West",backImage2);	//서쪽에 메인 이미지 추가
		bindPanel5.add(bindPanel4);	//동쪽에 content,안내 이미지 추가
		
		
		
		add(bindPanel5);			//만들어 놓은 GUI를 JFrame에 추가
		setIconImage(image1.getImage());	//JFrame의 상단 왼쪽아이콘 추가
		setBounds(500, 300, 525, 380);		//만들어 놓아질 위치와 크기 설정
		setTitle("채팅 프로그램");		//JFrame의 상단 왼쪽아이콘 옆에 title을 설정
		this.addWindowListener(new WindowAdapter(){	//사용자가 x를 눌러 윈도우 창을 끌 경우에 대한 리스너 추가
			public void windowClosing(WindowEvent e) {
				dispose();		//모든 자원 해체
				System.exit(0);		//시스템 종료
			}
		});
		
	}
	public void actionPerformed(ActionEvent e) {	//버튼에 대한 엑션 추가
		if(e.getActionCommand().equals("LOGIN")){//로그인 버튼 누를 경우
			try {
				this.dispose();		//자원 해체
				MultiClient client = new MultiClient(id.getText());//MultiClient 클래스 생성하여 다음 프레임 보여주게 함
			} catch (IOException e1) {}	//소켓으로 넘어가기 때문에 IOException 설정
		}
		else if(e.getActionCommand().equals("아이디 찾기")){	//아이디 찾기에 대한 버튼 추가
				
		}
		else if(e.getActionCommand().equals("비밀번호 찾기")){	//비밀번호 찾기에 대한 버튼 추가
			
		}
		else if(e.getActionCommand().equals("회원가입")){	//회원가입에 대한 버튼 추가
			Apply_User apply = new Apply_User("회원가입");
			apply.showFrame();
		}
	}
}	//InputID 클래스 끝
public class MultiClient{	//MultiClient 시작	
	Wait_Room waitroom = new Wait_Room("대기실");	//Wait_Room 클래스를 "대기실"을 넘겨주면서 생성	대기실:title
	static Socket socket;	//정적으로 소켓 생성
	static String name;	//정적으로 이름 생성
	static BufferedReader networkReader;	//정적으로 버퍼리더로 networkReader생성
	static BufferedWriter networkWriter;	//정적으로 버퍼라이터로 networkWriter생성
	static String ip = "localhost";					//사용자 ip
	static int port	= 3334;						// PORT번호
	public MultiClient(String name) throws IOException {
		this.name = name;				//사용자 ID
		waitroom.showFrame(name);			//Wait_Room클래스의 showFrame메소드에 name 넘겨줌
		setSocket(ip, port, name);			//사용자가 사용할 소켓 생성
	}
	public void setSocket(String ip, int port, String name) throws IOException{	//소켓 설정
		try{
			socket = new Socket(ip,port);	//ip와 port로 소켓 생성
			networkWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));	//버퍼라이터로 쓸수 있게 함
			networkReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));	//버퍼리더로 쓸수 있게 함

			Listener_Of_Client listener = new Listener_Of_Client(networkWriter, networkReader, socket ,waitroom);//Listener_Of_Client 생성 
			//만약 생성자를 waitroom으로만 하게 되면 Listener_Of_Client에서는 서버명.networkWriter[Reader]로 해야함.
			listener.setDaemon(true);
			listener.start();

			sendMsg(MsgInfo.NEW, name);		//채팅자가 입장했음을 알림.
		}catch(IOException e){
			System.out.println(e);
			e.printStackTrace();
		}
	}
	public static void sendMsg(String token, String msg) throws IOException {
		if(msg == null){
			msg = "";
		}
		networkWriter.write(token + "/" + msg + "\n");
		networkWriter.flush();
	}

	public static void main(String[] args) throws IOException {
		InputId getid = new InputId();
		getid.setVisible(true);
	}
}
