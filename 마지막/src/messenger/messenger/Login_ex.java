package messenger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login_ex extends JFrame{
	public Login_ex()
	{
		JPanel panel = new JPanel();
		JLabel label = new JLabel("ID : ");
		JLabel password = new JLabel("PW : ");
		JTextField FieldId = new JTextField(10);
		JPasswordField FieldPw = new JPasswordField(10);
		JButton logButton = new JButton("Login");
		
		add(panel);	//JFrame에 panel이 들어감
		
		panel.add(label);	//JPanel에 label이 들어감
		panel.add(FieldId);	//JPanel에 FieldId가 들어감
		
		panel.add(password);	//JPanel에 password가 들어감
		panel.add(FieldPw);		//JPanel에 FieldId가 들어감
		
		panel.add(logButton);		//JPanel에 logButton이 들어감
		logButton.addActionListener(new ActionListener() {
			//logButton의 기능 추가
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = "lee jimin";
				String pass = "12345";
				
				System.out.printf("%s %s",FieldId.getText(),FieldPw.getText());
				
				//팝업창으로 새로운 메시지 띄우기
				JOptionPane.showMessageDialog(null,"ID : " + FieldId.getText() + "\n"
						+ "PW : "  + FieldPw.getText());
				
				if(id.equals(FieldId.getText()) && pass.equals(FieldPw.getText()))
				{
					JOptionPane.showMessageDialog(null, "You have logged in success");
				}
				else
				{
					JOptionPane.showMessageDialog(null, "You have failed to log in");
				}
				
			}
		});
		
		
		setVisible(true);	//JFrame이 보이도록 함
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//Frame을 닫았을 때 실제로 종료되도록 함
		setSize(600,400);
		setResizable(false);	//Frame의 사이즈를 변경하지 못하도록 함.
		setLocationRelativeTo(null);	//Frame을 가운데로 정렬
	}
	
	public static void main(String[] args)
	{
		new Login_ex();
	}
}