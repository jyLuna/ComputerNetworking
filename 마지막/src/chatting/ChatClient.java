package chatting;

import java.awt.event.*;
import java.net.*;

import javax.swing.*;
import java.io.*;
import java.awt.*;

// file 선택창 보이게 하기 위해
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class ChatClient extends JFrame implements Runnable
{

	Socket clientSocket = null; // 클라이언트 선언

	PrintWriter out = null;	// gui 창에 표시될 글자를 출력하는 객체
	BufferedReader in = null;	// 서버가 전체에 알려주는 내용을 받을 객체

	JTextArea outputArea;
	JTextField inputField;
	JScrollBar sb;
	JPanel  pa;
	

	public ChatClient(String title)	// 디자인 구현
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
	}
	
	
	public static ChatClient mf = new ChatClient("자바 채팅 클라이언트");
	
	
	// 전체 주고받는 메시지에 입력한 값을 추가한다.
	public void addMessage(String msg)
	{
		outputArea.append(msg); 
		//updateTitle();
	}
	
	private void updateTitle()
	{
		try
		{
			ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
			int numOfClient = (int) ois.readObject();
			mf.setTitle("자바 채팅 클라이언트(현재 인원수: " + numOfClient + "명)");
		}
		catch (Exception e)
		{
			System.err.println("채팅방이 존재하지 않습니다.");
			System.exit(1);
		}
	}

	public void connect(String host, int port)
	{
		try
		{
			clientSocket = new Socket(host, port);	// 서버에 접속
			
			// 현재 채팅방에 있는 인원수 확인
			ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
			int numOfClient = (int) ois.readObject();
			mf.setTitle("자바 채팅 클라이언트(현재 인원수: " + numOfClient + "명 / 서버 port: #" + port + ")");

			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		}
		catch (Exception e)
		{
			System.err.println("채팅방이 존재하지 않습니다.");
			System.exit(1);
		}
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
		try
		{
			while (true)
			{
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
			}
		}
		catch (IOException e)
		{  disconnect(); }
	}

	// port
	private static int port = 4648;
	public static void main(String[] args)
	{
		mf.pack();
		mf.setSize(500, 300);
		mf.setVisible(true);
		mf.connect("127.0.0.1", port);
		Thread thread = new Thread(mf);
		thread.start();
	}

	class InputListener implements ActionListener
	{
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