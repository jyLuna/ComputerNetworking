package chat;

import javax.swing.*;
import javax.swing.border.*;

import server.MsgInfo;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class ChatRoom extends JFrame implements KeyListener, ActionListener{
	static JTextArea showText = new JTextArea(10, 20);		//텍스트Area(대기실과 같은)만듦
	JTextField inputText = new JTextField();			//텍스트필드(입력란)
	List IDlist = new List();					//아이디리스트
	JButton exit = new JButton("나가기");				//나가기버튼
	static JLabel inputid;						//현재 사용중인 아이디를 작성할 라벨
	static String title;						//방제목
	String id;							//현재 사용중인 아이디
	JPopupMenu chatpopup;  						//팝업메뉴
	JMenuItem sendmemo, mantoman, transferFile, kick, change,save,exitpop,conneterAll,sendPaper,talk,sendFile,kickmenu; //팝업,메뉴바에 들어갈 아이템들
	JScrollPane chatJsp;						//채팅창에대한 스크롤 패널
	JScrollBar chatJsb;						//채팅창에대한 스크롤바
	JScrollPane userJsp;						//유저창에대한 스크롤 패널
	JScrollBar userJsb;						//유저창에대한 스크롤바
	int chiefcode = 0;						//주된코드번호
	
	ChatRoom(String title){
		super("toe's채팅방 : "+ title);
		this.title = title;
	}
	public void showFrame(String name){
		ActionListener ac = new Chat_EventClass(title, this.getFrames());

		JMenuBar mb = new JMenuBar();				//메뉴바 생성
		JMenu file = new JMenu("파일");				//메뉴바의 첫번째 메뉴 (파일) 생성
		mb.add(file);						//메뉴바에 추가
		JMenu connecter = new JMenu("접속자");			//메뉴바의 두번째 메뉴 (접속자) 생성
		mb.add(connecter);					//메뉴바에 추가
		JMenu messenger = new JMenu("메신저");			//메뉴바의 세번째 메뉴 (메신저) 생성
		mb.add(messenger);					//메뉴바에 추가
		save = new JMenuItem("대화내용저장");			//파일에 추가될 save 아이템 (대화내용저장) 생성
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
		kickmenu = new JMenuItem("강제퇴장");			//messenger의 네번째 아이템 강제퇴장
		messenger.add(kickmenu);				//messenger에 kickmenu
		save.addActionListener(this);				//save 아이템에 액션리스너 추가
		exit.addActionListener(this);				//exit 아이템에 액션리스너 추가
		conneterAll.addActionListener(this);			//conneterAll 아이템에 액션리스너 추가
		sendPaper.addActionListener(this);			//sendPaper 아이템에 액션리스너 추가
		talk.addActionListener(this);				//talk 아이템에 액션리스너 추가
		sendFile.addActionListener(this);			//sendFile 아이템에 액션리스너 추가
		
		id = name;
		inputid = new JLabel(id);
		Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		inputText.addKeyListener(this);
		
		//	오른쪽 버튼을 클릭 했을때.
		chatpopup=new JPopupMenu();
		sendmemo=new JMenuItem("쪽지보내기");  		sendmemo.addActionListener(this);
		mantoman=new JMenuItem("1:1대화");   		mantoman.addActionListener(this);
		transferFile=new JMenuItem("파일전송");  	transferFile.addActionListener(this);
		kick = new JMenuItem("강퇴");				kick.addActionListener(this);
		
		//스크롤바 추가
		chatJsp = new JScrollPane(showText);
		chatJsp.setWheelScrollingEnabled(true);
		chatJsb = chatJsp.getVerticalScrollBar();  
		
		JPanel left = new JPanel();
			left.setLayout(new BorderLayout());
			left.add(inputid, "North");
			left.add(chatJsp, "Center");
			left.add(inputText, "South");
			left.setBorder(loweredetched);
			
		//스크롤바 추가
		userJsp = new JScrollPane(IDlist);
		userJsp.setWheelScrollingEnabled(true);
		userJsb = userJsp.getVerticalScrollBar();
			
		JPanel right = new JPanel();
			right.setLayout(new GridLayout(1,1));
			right.add(userJsp);
			right.setBorder(loweredetched);
		
		IDlist.addMouseListener(
				new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(e.getButton() == 3){
//					System.out.println("방장 코드 : " + chiefcode);		
					if( chiefcode == 1 ){		//방장이면 전부다 출력.
						chatpopup.add(sendmemo);
						chatpopup.add(mantoman);
						chatpopup.add(transferFile);
						chatpopup.add(kick);
						chatpopup.show(e.getComponent(), e.getX(), e.getY());
					}else{						//아니면 기본적인것만.
						chatpopup.add(sendmemo);
						chatpopup.add(mantoman);
						chatpopup.add(transferFile);
						chatpopup.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			}
		});
		
		JPanel south = new JPanel();
			south.setLayout(new FlowLayout());
			south.add(exit);
			exit.addActionListener(this);
			south.setBorder(loweredetched);
			
		setBounds(100, 100, 500, 421);
		setLayout(new BorderLayout());
		add(left, "Center");
		add(right, "East");
		add(south, "South");
		setVisible(true);
		
		// X를 눌렀을때는 현재 대화방에서 나가기.
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				System.out.println("ChatRoom Frame 종료");
				try {
					MultiClient.sendMsg(MsgInfo.GOWAIT, inputid.getText()+"/"+title);
				} catch (IOException e1) { e1.printStackTrace(); }
			}
		});
	}
	
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {
		if(e.getKeyChar() == '\n'){
			chatJsb.setValue(chatJsb.getMaximum());			//엔터를 치켠 스크롤바도 밑으로 내려오게 하는 것.
			String consoleData = title+"/"+inputText.getText();		//입력한걸 저장
			inputText.setText("");							//저장후 지워야 함.
			Send_Message.chattingStart(consoleData);			//채팅 시작
		}
	}
	public void actionPerformed(ActionEvent e) {
		String receiveid = IDlist.getSelectedItem();
		if(e.getActionCommand().equals("나가기")){
			this.setVisible(false);
//			System.out.println("ChatRoom Frame 종료");		//확인
			try {
				MultiClient.sendMsg(MsgInfo.GOWAIT, inputid.getText()+"/"+title);
			} catch (IOException e1) { e1.printStackTrace(); }
		}
/*==================================================================
 *				 (오른쪽 버튼) 쪽지 보내기 눌렀을때
 * ==================================================================*/
		else if(e.getSource()==sendmemo||e.getSource()==sendPaper){
			if(receiveid == null){		//받는 사람이 없을때
				JOptionPane.showMessageDialog(null, "대상을 선택하세요.");
			}else if(receiveid.startsWith("[방장]")){		//[방장]이란 글자 떼어내고!
				receiveid = receiveid.substring(4);
			}else{
				Send_Memo memo = new Send_Memo();
				memo.showFrame(receiveid, id);
			}
		}
/*==================================================================
 *				 (오른쪽 버튼) 1:1대화 눌렀을때
 * ==================================================================*/
		else if(e.getSource()==mantoman||e.getSource()==talk){
			if(receiveid == null){		//받는 사람이 없을때
				JOptionPane.showMessageDialog(null, "대상을 선택하세요.");
			}else if(receiveid.startsWith("[방장]")){
				receiveid = receiveid.substring(4);		//[방장]이란 글자 떼어내고!
			}else{
				try {
					MultiClient.sendMsg(MsgInfo.ADDCHAT, receiveid+"/"+id);		//1:1 채팅 유저 둘 추가
					MultiClient.sendMsg(MsgInfo.MAKECHAT, receiveid+"/"+id);	//[MAKECHAT]/받는사람/자기대화명
					MultiClient.sendMsg(MsgInfo.CHATQUESTION, receiveid+"/"+id);	//[QUESTION]/받는사람/자기대화명
				} catch (IOException e1) { e1.printStackTrace();	}
			}
		}
/*==================================================================
 *				 (오른쪽 버튼) 파일 전송 눌렀을때
 * ==================================================================*/
		else if(e.getSource()==transferFile||e.getSource()==sendFile){
			if(receiveid == null){		//받는 사람이 없을때
				JOptionPane.showMessageDialog(null, "대상을 선택하세요.");
			}else if(receiveid.startsWith("[방장]")){
				receiveid = receiveid.substring(4);		//[방장]이란 글자 떼어내고!
			}else{
				Transfer_File sendfile = new Transfer_File("파일보내기", receiveid, id);
				sendfile.showFrame();
			}
		}
/*==================================================================
 *				 방장이 (오른쪽 버튼) 강퇴 눌렀을때
 * ==================================================================*/
		else if(e.getSource()==kick || e.getSource()==kickmenu){
			if(receiveid == null){		//받는 사람이 없을때
				JOptionPane.showMessageDialog(null, "대상을 선택하세요.");
			}else if(receiveid.startsWith("[방장]")){
				JOptionPane.showMessageDialog(null, "자신을 강퇴 시킬 수 없잖아!!");
			}else{
				try {
					MultiClient.sendMsg(MsgInfo.KICK, receiveid+"/"+title);		// 강퇴 명령어 전송
				} catch (IOException e1) { e1.printStackTrace(); }		
			}
		}
	}
}
