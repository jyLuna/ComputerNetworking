package chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/*
 * 보낼 파일에서 선택을 눌렀을때 호출되는 클래스!
 * 
 */
public class Select_Send_File extends JFrame{
	FileDialog fileopen; 	// 파일다이아로그.
	JTextArea ta;			//ta			
	String filepath;
	public Select_Send_File(String title){
		super(title);
		ta=new JTextArea();
		add(ta);
		setSize(300,300);
	}
	public String showFrame(){
		fileopen = new FileDialog(this, "문서열기", FileDialog.LOAD);		//열기모드
		fileopen.setVisible(true);
		
		String filename = fileopen.getFile();			//파일이름 얻어오기 
		String filedir = fileopen.getDirectory();		//파일의 경로 얻어오기

		filepath = filedir+"\\"+filename;				//파일의 경로+파일의 이름으로 파일이 위치한 파일을 얻어옴
		return filepath;
	}
}
