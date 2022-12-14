package chat;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
/*
 * 	쪽지 보내기, 대화하기, 파일 보내기 등에서 선택을 눌렀을때
 *  이 클래스가 호출된다.
 */
public class Select_ID extends JFrame implements ActionListener{
	static List idlist;
	private JButton confirm;
	private JButton exit;
	public Select_ID(){
		JLabel name = new JLabel("전체 접속자");
		idlist = new List();

		JPanel south = new JPanel();
		south.setLayout(new FlowLayout());
		confirm = new JButton("확인");
		exit = new JButton("닫기");
		confirm.addActionListener(this);
		exit.addActionListener(this);
		south.add(confirm);	south.add(exit);

		this.setLayout(new BorderLayout());
		add(name, "North");
		add(idlist, "Center");	add(south, "South");
		setBounds(500, 200, 200, 300);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		String id = idlist.getSelectedItem();
		if(e.getSource() == confirm){
			System.out.println(id);
			if(id == null){
				JOptionPane.showMessageDialog(null,"아이디를 선택하세요.");
			}else{
				/*
				 *  셋 중에 하나에서 요청한다.
				 *  즉.. 하나는 걸린다
				 */
				try{
					Send_Memo.inputid.setText(id);
				}catch(NullPointerException e1){}
				try{
					Select_Chat_User.inputid.setText(id);
				}catch(NullPointerException e2){}
				try{
					Transfer_File.target.setText(id);
				}catch(NullPointerException e3){}
			dispose();
		}
	}else if(e.getSource() == exit){
		dispose();
	}
}	// 액션 이벤트 종료
public static void main(String[] args) {
	Select_ID id = new Select_ID();
}
}
