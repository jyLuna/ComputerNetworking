package chatRoom;

// Client.java를 통해 실행됨
// multi-thread 형식으로 한 client가 여러개 실행 가능
// GUI에서 입력받은 값으로 출력 및 퇴장, 파일 전송 등 실행

import java.awt.event.*;	// 화면창
import java.net.*;			// 서버 연결
import java.io.*;			// 서버 연결
import java.util.*;			// scanner, 이후 제거

// file 선택창 보이게 하기 위해
import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;


public class Client_chat extends JFrame implements Runnable
{
	Socket clientSocket; // 클라이언트 선언
	private static int port = 0;
	private static String title = "";

	PrintWriter out;		// gui 창에 표시될 글자를 출력하는 객체
	BufferedReader in;	// 서버가 전체에 알려주는 내용을 받을 객체

	JTextArea outputArea;
	JTextField inputField;
	JScrollBar sb;
	JPanel  pa;
	

	public Client_chat(String title, int port)	// 디자인 구현
	{
		super(title);
		pa=new JPanel(new BorderLayout());
		outputArea = new JTextArea();	

		outputArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(outputArea);  // 스크롤패인 추가
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		outputArea.setCaretPosition(outputArea.getDocument().getLength());

		pa.add(scrollPane, "Center");
		inputField = new JTextField();
		pa.add(inputField, "South");
		inputField.addActionListener(new InputListener());	// 이벤트 호출
		add(pa);
		
		
		this.port = port;
		this.title = title;
	}
	
	
	public static Client_chat mf = new Client_chat(title, port);
	
	
	// 전체 주고받는 메시지에 입력한 값을 추가한다.
	public void addMessage(String msg)
	{ outputArea.append(msg); }

	
	public void connect(int port)
	{
		try
		{
			clientSocket = new Socket("127.0.0.1", port);
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		}
		catch(Exception e)
		{ e.printStackTrace(); }
	}
	
	public void disconnect()
	{
		try
		{
			in.close();
			out.close();
			clientSocket.close();
			mf.setVisible(false);
		}
		catch (IOException e)
		{ System.out.println("ERROR!"); }
	}
	
	
	@Override
	public void run()	// 실제로 메시지를 뿌려주는 곳
	{
		mf.pack();
		mf.setSize(300, 800);
		mf.setTitle(title);
		mf.setVisible(true);
		mf.connect(port);
		try
		{
			while (true)
			{
				Scanner sc = new Scanner(System.in);
				String input = sc.nextLine();
				
				if(input.equals("exit"))	// client의 퇴장
				{
					disconnect();	// 화면 끄기 및 연결 끊기
					// 시스템 종료(화면 종료가 아니라 client를 종료)
					System.exit(1);
				}
				else if(input.equals("file"))	// file 보내기
				{
					System.out.println("Input file");
					 String folderPath = "";
				        
				        JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()); // 디렉토리 설정
				        chooser.setCurrentDirectory(new File("/")); // 현재 사용 디렉토리를 지정
				        chooser.setAcceptAllFileFilterUsed(true);   // Filter 모든 파일 적용 
				        chooser.setDialogTitle("타이틀"); // 창의 제목
				        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); // 파일 선택 모드
				        
				        FileNameExtensionFilter filter = new FileNameExtensionFilter("Binary File", "cd11"); // filter 확장자 추가
				        chooser.setFileFilter(filter); // 파일 필터를 추가
				        
				        int returnVal = chooser.showOpenDialog(null);	// 열기용 창 오픈
				        
				        if(returnVal == JFileChooser.APPROVE_OPTION)	// 열기를 클릭 
				        {
				        	folderPath = chooser.getSelectedFile().toString();
				        	System.out.println(folderPath);
				        }
				        else if(returnVal == JFileChooser.CANCEL_OPTION)	// 취소를 클릭
				        {
				            System.out.println("cancel"); 
				            folderPath = "";
				        }
				}
				
				/* 원래 코드, 화면 상의 글을 읽고 쓰는 문제 발생()
				String input =  in.readLine();
				
				if(input.equals("exit"))	// client의 퇴장
				{
					disconnect();	// 화면 끄기 및 연결 끊기
					// 시스템 종료(화면 종료가 아니라 client를 종료)
					System.exit(1);
				}
				else if(input.equals("file"))	// file 보내기
				{
					System.out.println("Input file");
					 String folderPath = "";
				        
				        JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()); // 디렉토리 설정
				        chooser.setCurrentDirectory(new File("/")); // 현재 사용 디렉토리를 지정
				        chooser.setAcceptAllFileFilterUsed(true);   // Filter 모든 파일 적용 
				        chooser.setDialogTitle("타이틀"); // 창의 제목
				        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); // 파일 선택 모드
				        
				        FileNameExtensionFilter filter = new FileNameExtensionFilter("Binary File", "cd11"); // filter 확장자 추가
				        chooser.setFileFilter(filter); // 파일 필터를 추가
				        
				        int returnVal = chooser.showOpenDialog(null);	// 열기용 창 오픈
				        
				        if(returnVal == JFileChooser.APPROVE_OPTION)	// 열기를 클릭 
				        {
				        	folderPath = chooser.getSelectedFile().toString();
				        	System.out.println(folderPath);
				        }
				        else if(returnVal == JFileChooser.CANCEL_OPTION)	// 취소를 클릭
				        {
				            System.out.println("cancel"); 
				            folderPath = "";
				        }
				}
				else
				{ addMessage(input + "\n"); }
				*/
			}
		}
		//catch (IOException e)
		//{  disconnect(); }
		catch (NullPointerException e)
		{ e.printStackTrace(); }
	}
	

	class InputListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			String input = inputField.getText();
			inputField.setText("");
			try
			{ out.println(input); } 
			catch (Exception e1)
			{ e1.printStackTrace(); }
		}

	}
}