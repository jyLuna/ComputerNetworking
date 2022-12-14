package chat;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.border.*;

import server.File_Send_Server;

import java.net.*;

public class Send_File extends Frame implements ActionListener{
	static JTextField filename;			//보낼 파일의 이름이 적힐 텍스트 란
	static JTextField filepath;			//보낼 파일을 적을 텍스트란
	JButton exit;						//닫기 버튿
	String targetuser;
	String targetfile;
	String myname;
	File file;
	FileWriter filereader;
	File_Receive_Client client;
	public static JProgressBar statusBar;
	public static JTextField size;
	public static JTextField allsize;
	String path;
	int port;
	File_Send_Server fileserver;
	//파일보내기 생성자 함수
	public Send_File(String title, int port, String filepath) throws IOException{
		super(title);
		this.port = port;
		this.path = filepath;
		file = new File(filepath);
	}
	//파일보내기 GUI
	public void showFrame() throws IOException{
		Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		//영역을 나눌 선
		JLabel first = new JLabel("보낼 파일");	//보낼파일 앞에들어갈 라벨
		filename = new JTextField(20);			//보낼파일의 이름
		filename.setText(file.getName());		
		
		JPanel north = new JPanel();
		north.setLayout(new FlowLayout());
		north.add(first);	north.add(filename);
		north.setBorder(loweredetched);

		JLabel rec_size = new JLabel("보낸 크기");
		JLabel all_size = new JLabel("/");
		JLabel allsizebytes = new JLabel("Bytes");

		size = new JTextField(10);					//보낸 만큼의  사이즈 크기 
		allsize = new JTextField(10);				//보낼파일의 전체 크기 텍스트
		allsize.setText(String.valueOf(file.length()));		//보낼파일의 전체크기 알아오기
		JPanel center_2 = new JPanel();				//
		center_2.setBorder(loweredetched);
		center_2.setLayout(new FlowLayout());
		center_2.add(rec_size); center_2.add(size);		center_2.add(allsizebytes);
		center_2.add(all_size);center_2.add(allsize);	center_2.add(allsizebytes);

		statusBar = new JProgressBar();
		statusBar.setStringPainted(true);
		statusBar.setForeground(Color.BLUE);

		JPanel center_3 = new JPanel();
		center_3.setBorder(loweredetched);
		center_3.setLayout(new FlowLayout());
		JLabel status = new JLabel("상태");
		center_3.add(status);	center_3.add(statusBar);

		JPanel center = new JPanel();
		center.setLayout(new GridLayout(2,1));
		center.add(center_2);
		center.add(center_3);
		center.setBorder(loweredetched);

		exit = new JButton("닫기");					//닫기 버튼 생성
		JPanel south = new JPanel();				//닫기버튼과 보내기 버튼을 묶을 패널 생성
		south.setLayout(new FlowLayout());			//패널에 대한 레이아웃
		south.add(exit);							//닫기버튼 추가
		south.setBorder(loweredetched);				//라인추가
		exit.addActionListener(this);				//닫기버튼에대한 버튼액션리스너추가

		add(north,"North");	add(center, "Center");	add(south, "South");
		pack();
		setVisible(true);

		fileserver = new File_Send_Server(port, path);
		new Thread(fileserver).start();		//Thread를 실행 시키지 않으면 채팅이 불가능하다.
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == exit){
			dispose();
		}
	}
	/*
	 * 	상대방이 파일 수신을 거부하면 아래가 호출됨.
	 */
	public static void reject_File(){
		JOptionPane.showMessageDialog(null, "상대방이 거절 하였습니다.");
	}
	/*
	 *  파일을 다 전송했을때 아래가 호출됨.
	 */
	public static void hideFrame(){
		JOptionPane.showMessageDialog(null, "전송 끝!");
	}
}