package chat;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Apply_User extends JFrame implements ActionListener{
	
	
	Userinfo user;
	JTextField nameField;
	JTextField secField1;
	JTextField secField2;
	JTextField idField;
	JPasswordField psField1;
	JPasswordField psField2;
	JButton certification;
	JButton idconfirm;
	JButton confirm;
	JButton exit;
	String applyTitle;
	int secchecker=0;
	int idchecker=0;
	Apply_User(String title){
		super(title);
		this.applyTitle = title;
	}
	public void showFrame(){
		JLabel namelabel = new JLabel("이름 : ",JLabel.LEFT);
		JLabel seclabel = new JLabel("주민번호 : ",JLabel.LEFT);
		JLabel idlabel = new JLabel("아이디 : ",JLabel.LEFT);
		JLabel pslabel1 = new JLabel("비밀번호 : ",JLabel.LEFT);
		JLabel pslabel2 = new JLabel("비밀번호 확인 : ",JLabel.LEFT);
		JPanel westPanel = new JPanel(new GridLayout(5,1));
		westPanel.add(namelabel);
		westPanel.add(seclabel);
		westPanel.add(idlabel);
		westPanel.add(pslabel1);
		westPanel.add(pslabel2);
		JPanel eastPanel = new JPanel(new GridLayout(5,1));
		JPanel northPanel = new JPanel(new BorderLayout());
		nameField = new JTextField(14);
		certification= new JButton("인증");
		certification.addActionListener(this);
		northPanel.add("West", nameField);
		northPanel.add("East", certification);
		eastPanel.add(northPanel);
		JPanel centerPanel = new JPanel(new BorderLayout());
		secField1 = new JTextField(9);
		secField2 = new JTextField(9);
		centerPanel.add("West",secField1);
		centerPanel.add("Center", new JLabel("-",JLabel.CENTER));
		centerPanel.add("East",secField2);
		eastPanel.add(centerPanel);
		idField = new JTextField(12);
		idconfirm = new JButton("중복검사");
		idconfirm.addActionListener(this);
		JPanel idPanel = new JPanel(new BorderLayout());
		idPanel.add("West",idField);
		idPanel.add("East",idconfirm);
		psField1 = new JPasswordField(10);
		psField2 = new JPasswordField(10);
		eastPanel.add(idPanel);
		eastPanel.add(psField1);
		eastPanel.add(psField2);
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		confirm = new JButton("확인");
		confirm.addActionListener(this);
		exit = new JButton("취소");
		exit.addActionListener(this);
		southPanel.add(confirm);
		southPanel.add(exit);
		setLayout(new BorderLayout());
		add("West",westPanel);
		add("East",eastPanel);
		add("South",southPanel);
		pack();
		setLocation(500,400);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("인증")){
			//서버에서 가져오는 헤쉬셋으로 인증
		}
		else if(e.getActionCommand().equals("중복검사")){
			String id = idField.getText();
			String name = nameField.getText();
			String secnum = secField1.getText()+secField2.getText();
			//서버에서 가져오는 헤쉬셋으로 인증
		}
		else if(e.getActionCommand().equals("확인")){
			String id = idField.getText();
			String password1 = new String(psField1.getPassword());
			String password2 = new String(psField2.getPassword());
			String name = nameField.getText();
			String secnum = secField1.getText()+secField2.getText();
			
			if(password1.compareTo(password2)!=0){
				JOptionPane.showMessageDialog(this,"비밀번호가 다릅니다.","경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(name==null||name.trim().length()==0){
				JOptionPane.showMessageDialog(this,"아이디가 비어있습니다.","경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			else if(secnum==null||secnum.trim().length()==0){
				JOptionPane.showMessageDialog(this,"주민번호가 비어있습니다.","경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			else if(id==null||id.trim().length()==0){
				JOptionPane.showMessageDialog(this,"아이디가 비어있습니다.","경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			else if(password1==null||password1.trim().length()==0||password2==null||password2.trim().length()==0){
				JOptionPane.showMessageDialog(this,"비밀번호가  비어있습니다.","경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			else if(secchecker==0){
				JOptionPane.showMessageDialog(this,"가입확인이 되지 않았습니다.","경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			else if(idchecker==0){
				JOptionPane.showMessageDialog(this,"ID중복검사를 하지않았습니다.","경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			JOptionPane.showMessageDialog(this,"회원가입이 완료 되었습니다.","회원가입", JOptionPane.INFORMATION_MESSAGE);
			user = new Userinfo(id,password1,name,secnum);
			//소켓으로 서버한테 만든것을 객체로 해서 넘겨주기
			try {
				this.dispose();		//자원 해체
				MultiClient client = new MultiClient(idField.getText());//MultiClient 클래스 생성하여 다음 프레임 보여주게 함
			} catch (IOException e1) {}	//소켓으로 넘어가기 때문에 IOException 설정
		}
		else if(e.getActionCommand().equals("취소")){
			dispose();
		}
	}
}
