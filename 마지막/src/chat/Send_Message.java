package chat;

import java.io.IOException;

import server.MsgInfo;

public class Send_Message {
	public static void chattingStart(String consoleData){
		System.out.println(consoleData);
		try{
			if("".equals(consoleData)){
				consoleData = " ";
				MultiClient.sendMsg(MsgInfo.CHAT,consoleData);		//그냥 엔터를 치더라도 값이 나와야 함.
			}else if("EXIT".equals(consoleData)){		//exit는 종료
				MultiClient.sendMsg(MsgInfo.EXIT,null);
			}else{										//그 외는 대화이므로..
				MultiClient.sendMsg(MsgInfo.CHAT,consoleData);
			}
		}catch(Exception e1){
			try {MultiClient.networkWriter.close();} catch (IOException e) {}
			try {MultiClient.networkReader.close();} catch (IOException e) {}
			try {MultiClient.socket.close();       } catch (IOException e) {}
		}
	}
}
