package chat;


import java.io.Serializable;
//사용자가 회원가입할때 입력될 생성자 함수
class Userinfo implements Serializable{
	String ID;			//사용자 사용ID
	String password;	//사용자 사용 Password
	String name;		//사용자 사용 이름
	String secnum;		//사용자 사용 주민번호
	public Userinfo(String ID, String password, String name, String secnum){
		this.ID = ID;		
		this.password = password;
		this.name = name;
		this.secnum=secnum;
	}
}
