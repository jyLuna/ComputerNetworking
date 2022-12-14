package messenger;

/* DB에서 가져온 데이터를 처리하는 클래스인 자바 빈즈*/
public class User 
{
	private String user_id;
	private String pw;
	private String nickname;
	private String user_name;
	private String email_address;
	private String birth_date;
	private String hp;
	private String CreateDate;
	
	//user_id getter,setter
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	//pw getter,setter
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	
	//nickname getter,setter
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	//name getter,setter
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String name) {
		this.user_name = name;
	}
	
	//email_address getter,setter
	public String getEmail_address() {
		return email_address;
	}
	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}
	
	//birth_date getter,setter
	public String getBirth_date() {
		return birth_date;
	}
	public void setBirth_date(String birth_date) {
		this.birth_date = birth_date;
	}
	
	//hp getter,setter
	public String getHp() {
		return hp;
	}
	public void setHp(String hp) {
		this.hp = hp;
	}
	
	//createDate getter,setter
	public String getCreateDate() {
		return CreateDate;
	}
	public void setCreateDate(String createDate) {
		CreateDate = createDate;
	}
	
	
}
