package chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class Ask_For_File extends JFrame implements ActionListener {
	JLabel subject;
	JButton accept;
	JButton reject;
	String target;
	String senduser;
	File_Receive_Client client;
	int port;
	Ask_For_File(String target, String shooter, int portnum){
		super(shooter+"의 파일 수신 요청.");
		this.target = target;
		this.senduser = shooter;
		this.port = portnum;
	}
	public void showFrame(){
		subject = new JLabel(senduser+"님의 파일을 받으시겠습니까?");
		accept = new JButton("승인");
		reject = new JButton("거부");

		this.setLayout(new BorderLayout());
		add(subject, "North");

		JPanel center = new JPanel(new FlowLayout());
		center.add(accept);	center.add(reject);
		accept.addActionListener(this);
		reject.addActionListener(this);
		add(center, "Center");
		this.setBounds(200, 200, 200, 80);
		this.setVisible(true);
		
		
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		
	}
	public void actionPerformed(ActionEvent e) {
		try {
			/*
			 * 어느걸 클린하든 접속은 해야된다.
			 */
			client = new File_Receive_Client(MultiClient.ip, port);
			Save_File savefile = new Save_File("파일 저장", client);
			new Thread(client).start();
			if(e.getSource() == accept){
				savefile.showFrame();
				this.dispose();
			}else if(e.getSource() == reject){
				//거절하면 2를 파일 서버로 보낸다..
				client.writer.write(2);
				client.writer.flush();
				this.dispose();
			}
		} catch (Exception e1) {	e1.printStackTrace();	}
	}
}
