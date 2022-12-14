package chat;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.DateFormat;
import java.util.GregorianCalendar;

import javax.swing.*;
import javax.swing.border.*;

import server.MsgInfo;
import server.Room;
import server.Room_Manager;

class Wait_Room extends JFrame implements KeyListener, ActionListener{	//슈퍼 클래스가 JFrame이고, 인터페이스가 KeyListener, ActionListener로 구현된 Wait_Room
	static JTextArea showText = new JTextArea(10, 20); //대기실에서 보여질 채팅내용의 창 생성(가로10, 세로20)
	static JLabel inputid;		//사용자가 누구인지 알려주는 라벨 생성
	static String id;		//사용자 id
	JTextField inputText = new JTextField();	//채팅입력 공간
	List IDlist = new List(8);	//유저들의 리스트
	List roomList = new List(8);	//방의 리스트
	JButton make = new JButton("방 만들기");	//방만들기 버튼
	JButton enter = new JButton("들어가기");	//들어가기 버튼
	JButton exit = new JButton("나가기");		//나가기 버튼
	JPopupMenu waitpopup;  		//팝업 메뉴 오른쪽 버튼 눌렀을 경우
	JMenuItem sendmemo, mantoman, transferFile,save,exitpop,conneterAll,sendPaper,talk,sendFile;	//팝업,메뉴바에 들어갈 아이템들
	String roomtitle;		//방제목
	JScrollPane waitJsp;		//채팅창의 스클롤패널
	JScrollPane userListJsp;	//유저리스트의 스크롤 패널
	JScrollPane roomListJsp;	//룸리스트의 스크롤 패널
	JScrollBar waitJsb;		//채팅창의 스크롤 바
	JScrollBar userListJsb;		//유저리스트의 스크롤 바
	JScrollBar roomListJsb;		//룸리스트의 스크롤 바

	
	
	Wait_Room(String title){	//Wait_Room의 생성자 함수
		super(title);
		roomtitle = title;
	}

	//대기실 클라이언트
	public void showFrame(String name){	//사용자에게 보여질 GUI
		
		/*
		 * MyMenuBar 클래스의 메소드를 이용해 메뉴 추가.
		 */
		JMenuBar mb = new JMenuBar();				//메뉴바 생성
		JMenu file = new JMenu("파일");				//메뉴바의 첫번째 메뉴 (파일) 생성
		mb.add(file);						//메뉴바에 추가
		JMenu connecter = new JMenu("접속자");			//메뉴바의 두번째 메뉴 (접속자) 생성
		mb.add(connecter);					//메뉴바에 추가
		JMenu messenger = new JMenu("메신저");			//메뉴바의 세번째 메뉴 (메신저) 생성
		mb.add(messenger);					//메뉴바에 추가
		save = new JMenuItem("대화내용 저장");			//파일에 추가될 save 아이템 (대화내용저장) 생성
		file.add(save);						//file의 첫번째 아이템 save추가
		file.addSeparator();					//file의 save 아래의 라인 그리기
		exitpop = new JMenuItem("닫기");				//file의 두번째 아이템 exit추가
		file.add(exit);						//file의 라인 아래의 exit추가
		conneterAll = new JMenuItem("전체 접속자");		//connecter의 첫번째 아이템 전체 접속자
		connecter.add(conneterAll);				//connecter에 connecterAll을 추가
		sendPaper = new JMenuItem("쪽지보내기");		//messenger의 첫번째 아이템 쪽지보내기
		messenger.add(sendPaper);				//messenger에 sendPaper을 추가
		talk = new JMenuItem("대화하기");			//messenger의 두번째 아이템 대화하기
		messenger.add(talk);					//messenger에 talk 추가
		sendFile = new JMenuItem("파일보내기");			//messenger의 세번째 아이템 파일보내기
		messenger.add(sendFile);				//messenger에 sendFile 추가
		save.addActionListener(this);				//save 아이템에 액션리스너 추가
		exit.addActionListener(this);				//exit 아이템에 액션리스너 추가
		conneterAll.addActionListener(this);			//conneterAll 아이템에 액션리스너 추가
		sendPaper.addActionListener(this);			//sendPaper 아이템에 액션리스너 추가
		talk.addActionListener(this);				//talk 아이템에 액션리스너 추가
		sendFile.addActionListener(this);			//sendFile 아이템에 액션리스너 추가
		this.setJMenuBar(mb);
		
		

		this.id = name;						//받아온 아이디를 String name으로 함
		inputid = new JLabel(id);				//받아온 아이디를 라벨에 작성
		Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED); //라인 생성
		inputText.addKeyListener(this);				//채팅 입력란에 초점 맞추는 리스너 추가

		waitpopup=new JPopupMenu();				//팝업 생성
		sendmemo=new JMenuItem("쪽지보내기");  		sendmemo.addActionListener(this);	//팝업에 추가할 아이템(쪽지보내기)만들고 액션리스너 추가
		mantoman=new JMenuItem("1:1대화");   		mantoman.addActionListener(this);	//팝업에 추가할 아이템(1:1대화)만들고 액션리스너 추가
		transferFile=new JMenuItem("파일전송");  	transferFile.addActionListener(this);	//팝업에 추가할 아이템(파일전송)만들고 액션리스너 추가

		//대기실 채팅창에 스크롤바 추가.
		waitJsp = new JScrollPane(showText);			//스크롤 패널 생성(텍스트Area)
		waitJsp.setWheelScrollingEnabled(true);			//스크롤에 휠로 내리고 올릴수 있도록 설정
		waitJsb = waitJsp.getVerticalScrollBar();  		//세로 스크롤바 생성
		
		JPanel left = new JPanel(new BorderLayout());		//모니터 왼쪽에 들어갈 content를 묶을 panel생성
		left.add(inputid, "North");				//북쪽에 사용자가 입력한라벨 추가
		left.add(waitJsp, "Center");				//센터에 스크롤패널(텍스트Area가 포함된) 추가
		left.add(inputText, "South");				//남쪽에 텍스트 필드 넣기
		left.setBorder(loweredetched);				//라인 넣기
		
		//접속자 스크롤바 추가.
		userListJsp = new JScrollPane(IDlist);			//스크롤 패널 생성(IDlist)
		userListJsp.setWheelScrollingEnabled(true);		//스크롤에 휠로 내리고 올릴수 있도록 설정
		userListJsb = userListJsp.createVerticalScrollBar();	//세로 스크롤바 생성
		
		JPanel right_up = new JPanel();				//모니터 오른쪽상단에 위치할 패널 생성
		right_up.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "접속자"));	//접속자라고 알려줄 타이틀 생성
		right_up.add(userListJsp);				//유저 정보를 패널에 추가
		
		IDlist.addMouseListener(				//오른쪽 버튼 클릭했을때 리스너
				new MouseAdapter(){			//오른쪽 버튼
					public void mouseClicked(MouseEvent e){
						if(e.getButton() == 3){
							waitpopup.add(sendmemo);
							waitpopup.add(mantoman);
							waitpopup.add(transferFile);
							waitpopup.show(e.getComponent(), e.getX(), e.getY());	//팝업을 생성,팝업창 위치
						}
					}
				});

		//방 목록 스크롤바 추가.
		roomListJsp = new JScrollPane(roomList);		//스크롤 패널 생성(roomList)
		roomListJsp.setWheelScrollingEnabled(true);		//스크롤에 휠로 내리고 올릴수 있도록 설정
		roomListJsb = roomListJsp.createVerticalScrollBar();	//세로 스크롤바 생성
		
		JPanel right_down = new JPanel();			//모니터 오른쪽하단에 위치할 패널 생성
		right_down.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "대화방"));	//대화방이라고 알려줄 타이틀 생성
		right_down.add(roomListJsp);				//방 정보를 패널에 추가

		JPanel right = new JPanel(new GridLayout(2,1));		//오른쪽 패널을 묶기 위한 패널 생성
		right.setBorder(loweredetched);				//오른쪽도 왼쪽과 같이 라인 넣기
		right.add(right_up);	right.add(right_down);		//패널을 패널에 넣기 순서대로 들어감
		
		
		JPanel south = new JPanel();				//아래쪽에 들어갈 버튼을 위한 패널 생성
		south.setLayout(new FlowLayout());			//크기에 상관있게 넣어주기위해 FlowLayout으로 잡아줌
		south.add(make);	south.add(enter);	south.add(exit);	//make,enter,exit버튼 차례대로 넣어줌
		enter.addActionListener(this);				//enter버튼의 액션리스너 추가
		make.addActionListener(this);				//make버튼의 액션리스너 추가
		exit.addActionListener(this);				//exit버튼의 액션리스너 추가
		
		south.setBorder(loweredetched);				//남쪽에 들어갈 패널에 라인 설정
		setBounds(100, 100, 500, 421);				//이프래임이 위치할 자리와 크기 설정
		setLayout(new BorderLayout());				//프래임의 레이아웃 설정
		add(left, "Center");					//센터에 left패널
		add(right, "East");					//동쪽에 right패널
		add(south, "South");					//남쪽에 south패널
		setVisible(true);					//프레임 활성화

		this.addWindowListener(new WindowAdapter(){		//프레임의 X버튼 눌렀을경우
			public void windowClosing(WindowEvent e) {
				dispose();				//자원해제
				String consoleData = "EXIT";		
				Send_Message.chattingStart(consoleData);//Send_Message클래스의 chattingStart메소드에(EXIT)문자열을 보냄
			}
		});
	}

	//키 이벤트
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {
		if(e.getKeyChar() == '\n'){
			waitJsb.setValue(waitJsb.getMaximum());				
			String consoleData = "Main"+"/"+inputText.getText();		//입력한걸 저장
			inputText.setText("");						//저장후 지워야 함.
			Send_Message.chattingStart(consoleData);			//채팅 시작
		}
	}

	//버튼에 대한 이벤트
	public void actionPerformed(ActionEvent ac) {
		String receiveid = IDlist.getSelectedItem();		//아이디리스트에서 선택된아이템을 가져옴
		if(ac.getActionCommand().equals("방 만들기")){		//방만들기 버튼을 눌렀을 경우
			dispose();	dispose();			//자원 해체
			/*
			 *						//실험해봐야됨
			 */
			final JFrame makeroom = new JFrame();		//프레임 생성
			final JTextField roomname;			//방제목을 입력받을 텍스트 만듬
			JButton ok;					//버튼
			JLabel title = new JLabel("방이름을 입력하세요");	//방이름을 입력하세요라고 알려줄 라벨 생성
			roomname = new JTextField(10);			//방제목입력란(10)생성
			ok = new JButton("OK");				//ok 버튼 생성
			ok.addActionListener(this);			//ok 버튼에 대한 리스너 추가
			makeroom.add(title, "North");			//프레임의 북쪽에 title 라벨 추가
			JPanel southPanel = new JPanel();			//패널 생성(방제목 입력란,ok버튼 추가)
			southPanel.add(roomname);	southPanel.add(ok);
			makeroom.add(southPanel, "Center");		//프레임의 가운데에 southPanel을 추가
			makeroom.setBounds(500, 300, 200, 80);		//프레임이 표시될 위치와 크기 설정
			makeroom.setVisible(true);			//프레임 활성화

		/*==================================================================
		 *				 방 이름 입력하고 Ok누를때
		 * ==================================================================*/
			ok.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(e.getActionCommand().equals("OK")){
						makeroom.dispose();	//makeroom프레임창의 자원해체
						Room room = null;
						//room = Room_Manager.roomMap.get("Main"); //Room 클래스 생성 해쉬셋으로 연결하기 위함
						System.out.println(room);	//방제목을 프린트
						try {		//MultiClient클래스의 sendMsg메소드사용
							MultiClient.sendMsg(MsgInfo.MAKEROOM, roomname.getText());
						} catch (IOException e1) { e1.printStackTrace(); }	//실패시 실패했는 이유 나타냄
					}
				}

			});
		/*==================================================================
		 *				 방 클릭하고 들어가기누를때
		 * ==================================================================*/
		}
		else if(ac.getActionCommand().equals("들어가기")){
			String roomname = roomList.getSelectedItem();	//룸리스트에서 선택된것을 가져와 roomname을 초기화시킴
			try{
				if(roomname == null){		//방이 없는데 없는걸 선택해서 들어가기 누르는걸 방지.
					JOptionPane.showMessageDialog(null, "선택된 방이 없습니다. 방을 만드세요.");
				}else{
					this.setVisible(false);		//프레임활성화
					MultiClient.sendMsg(MsgInfo.ENTER, roomname);	//Multiclient클래스에서 sendMsg메소드 사용
				}
			}catch(IOException e1) { e1.printStackTrace(); }		//실패시 실패했는 이유 나타냄
		/*==================================================================
		 *		 나가기 눌렀을때, (메뉴바) 닫기 눌렀을때
		 * ==================================================================*/
		}
		else if(ac.getActionCommand().equals("나가기")||ac.getSource()==exit){
			dispose();					//자원 해체
			String consoleData = "EXIT";
			Send_Message.chattingStart(consoleData);			//Exit 보내기
			System.exit(0);
		}
		/*==================================================================
		 *(오른쪽 버튼) 쪽지 보내기 눌렀을때,  (메뉴바) 쪽지보내기 눌렀을때
		 * ==================================================================*/
		else if(ac.getSource()==sendmemo||ac.getSource()==sendPaper){
			Send_Memo memo = new Send_Memo();		//Send_Memo클래스 생성
			if(receiveid == null){		//받는 사람이 없을때
				memo.showFrame(null, id);		//Send_Memo클래스의 showFrame메소드 null값과 현재사용자 아이디 넘김
			}else{
				memo.showFrame(receiveid, id);		//Send_Memo클래스의 showFrame메소드 사용 아이디리스트에서 얻어온 아이디와 현재사용자 아이디 넘김
			}
		}
		/*==================================================================
		 *	(오른쪽 버튼) 1:1대화 눌렀을때, (메뉴바) 대화하기 눌렀을때
		 * ==================================================================*/
		else if(ac.getSource()==mantoman || ac.getSource()==talk){
			if(receiveid == null){		//받는 사람이 없을때
				Select_Chat_User seluser = new Select_Chat_User(id);	//Select_Chat_User클래스 생성(사용자 id보냄)
			}else{
				try {
					MultiClient.sendMsg(MsgInfo.ADDCHAT, receiveid+"/"+id);		//1:1 채팅 유저 둘 추가
					MultiClient.sendMsg(MsgInfo.MAKECHAT, receiveid+"/"+id);	//[MAKECHAT]/받는사람/자기대화명
					MultiClient.sendMsg(MsgInfo.CHATQUESTION, receiveid+"/"+id);	//[QUESTION]/받는사람/자기대화명
				} catch (IOException e) { e.printStackTrace();	}
			}
		}
		/*==================================================================
		 *	(오른쪽 버튼) 파일 전송 눌렀을때,(메뉴바) 파일보내기 눌렀을때
		 * ==================================================================*/
		else if(ac.getSource()==transferFile || ac.getSource()==sendFile){
			Transfer_File sendfile;					//Transfer_File 생성
			if(receiveid == null){		//받는 사람이 없을때
				sendfile = new Transfer_File("파일보내기", null, id);	//받는사람이 없다는 상태로 보내기
			}else{
				sendfile = new Transfer_File("파일보내기", receiveid, id);	//그외 받는사람표시해서 보내기
			}
			sendfile.showFrame();										//파일보내기 프레임 보여주기
		}
		String id = Wait_Room.inputid.getText();
		String selectmenu = ac.getActionCommand();
		/* ===================================================================
		 *	팝업에서 대화내용 저장 눌렀을때 
		 *===================================================================*/
		if (selectmenu.equals("대화내용 저장")){
			FileDialog fdial = new FileDialog(this, "", FileDialog.SAVE);	
			fdial.setVisible(true);
			GregorianCalendar gc = new GregorianCalendar();
			DateFormat df = DateFormat.getInstance();
			String data = df.format(gc.getTime())+"\r\n["+roomtitle+"]"+"에서의 대화내용"+"\r\n";
			// \n의 값을 \r\n으로 해야 메모장에서 엔터 기능이 이루어 진다.
			data += Wait_Room.showText.getText().replaceAll("\n", "\r\n");
			BufferedWriter bw;
			try {
				try{
//				BufferedWriter를 저장 위치로 설정.
				bw = new BufferedWriter(new FileWriter(fdial.getDirectory()+"\\"+fdial.getFile()));
				bw.write(data);
				bw.close();
			}catch(FileNotFoundException file){}
			} catch (IOException e1) {e1.printStackTrace();}
			/* ===================================================================
			 *	팝업에서 젙체 접속자 눌렀을때 
			 *===================================================================*/
		}else if(selectmenu.equals("전체 접속자")){
			try {
				MultiClient.sendMsg(MsgInfo.SHOWUSER, null);
			} catch (IOException e1) { e1.printStackTrace(); }
			
		}
	}
}
