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
		
		add(panel);	//JFrame�� panel�� ��
		
		panel.add(label);	//JPanel�� label�� ��
		panel.add(FieldId);	//JPanel�� FieldId�� ��
		
		panel.add(password);	//JPanel�� password�� ��
		panel.add(FieldPw);		//JPanel�� FieldId�� ��
		
		panel.add(logButton);		//JPanel�� logButton�� ��
		logButton.addActionListener(new ActionListener() {
			//logButton�� ��� �߰�
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = "lee jimin";
				String pass = "12345";
				
				System.out.printf("%s %s",FieldId.getText(),FieldPw.getText());
				
				//�˾�â���� ���ο� �޽��� ����
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
		
		
		setVisible(true);	//JFrame�� ���̵��� ��
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//Frame�� �ݾ��� �� ������ ����ǵ��� ��
		setSize(600,400);
		setResizable(false);	//Frame�� ����� �������� ���ϵ��� ��.
		setLocationRelativeTo(null);	//Frame�� ����� ����
	}
	
	public static void main(String[] args)
	{
		new Login_ex();
	}
}