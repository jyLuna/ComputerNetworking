package chat;

import javax.swing.*;

import server.MsgInfo;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class Ask_For_Chat extends JFrame implements ActionListener {
	JLabel subject;
	JButton accept;
	JButton reject;
	String target;
	String shooter;
	Ask_For_Chat(String target, String shooter){
		super(shooter+"님의 1:1대화요청");
		this.target = target;
		this.shooter = shooter;
	}
	public void showFrame(){
		subject = new JLabel(shooter+"님 께서 대화를요청하셨습니다.");
		accept = new JButton("승인");
		reject = new JButton("거부");
		
		this.setLayout(new BorderLayout());
		add(subject, "North");
		
		JPanel center = new JPanel(new FlowLayout());
		center.add(accept);	center.add(reject);
		accept.addActionListener(this);
		reject.addActionListener(this);
		add(center, "Center");
		this.pack();
		this.setVisible(true);
		
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == accept){
			try {
				MultiClient.sendMsg(MsgInfo.MAKECHAT, shooter+"/"+target);	//[MAKECHAT]/받는사람/자기대화명
				this.dispose();
			} catch (IOException e1) {	e1.printStackTrace(); }	
		}else if(e.getSource() == reject){
			try {
				MultiClient.sendMsg(MsgInfo.EXITCHAT, target);
				MultiClient.sendMsg(MsgInfo.MANTOMAN, shooter+"/"+target+"/"+"상대방이 대화를 거절하였습니다.");
				this.dispose();
			} catch (IOException e1) {
				e1.printStackTrace();
			}	//[MAKECHAT]/받는사람/자기대화명
		}
	}
}
