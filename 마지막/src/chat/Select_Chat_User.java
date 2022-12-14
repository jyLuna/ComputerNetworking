package chat;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.*;

import server.MsgInfo;

/*
 * 	메뉴바에서 대화하기를 눌렀을때 호출되는 클래스.
 *  상대가 없기 때문에 꼭 선택을 눌러서 대화상대를 지정해야 한다.
 */
class Select_Chat_User extends JFrame implements ActionListener{
	static JTextField inputid;
	JLabel targetuser;
	JButton selectid;
	JButton ok;
	JButton exit;
	String receiveid;
	String id;
	public Select_Chat_User(String id){
		this.id = id;
		Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		targetuser = new JLabel("대화 상대 ");
		inputid = new JTextField(13);
		inputid.setText("대상을 선택하세요.");
		selectid = new JButton("선택");
		ok = new JButton("확인");
		exit = new JButton("닫기");
		
		JPanel center = new JPanel();
		center.setLayout(new FlowLayout());
		center.add(targetuser); 	center.add(inputid);	center.add(selectid);
		center.setBorder(loweredetched);
		
		JPanel south = new JPanel();
		south.setLayout(new FlowLayout());
		south.add(ok);	south.add(exit);
		selectid.addActionListener(this);
		ok.addActionListener(this);
		exit.addActionListener(this);
		south.setBorder(loweredetched);
		
		add(center, "Center");
		add(south, "South");
		setBounds(200, 200, 300, 110);
		setVisible(true);
		
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		
	}
	public void actionPerformed(ActionEvent e) {
		receiveid = inputid.getText();
		String id = Wait_Room.id;
		System.out.println(receiveid+" "+id);
		if(e.getSource() == ok){
			try {
				MultiClient.sendMsg(MsgInfo.ADDCHAT, receiveid+"/"+id);			//1:1 채팅 유저 둘 추가
				MultiClient.sendMsg(MsgInfo.MAKECHAT, receiveid+"/"+id);		//[MAKECHAT]/받는사람/자기대화명
				MultiClient.sendMsg(MsgInfo.CHATQUESTION, receiveid+"/"+id);	//[QUESTION]/받는사람/자기대화명
			} catch (IOException e1) {e1.printStackTrace();}					
			dispose();
		}else if(e.getSource() == selectid){
			try {
				MultiClient.sendMsg(MsgInfo.SELUSER, null);
			} catch (IOException e1) {	e1.printStackTrace(); }
		}else if(e.getSource() == exit){
			dispose();
		}
	}
}