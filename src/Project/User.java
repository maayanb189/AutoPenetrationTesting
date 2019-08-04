package Project;

import java.util.ArrayList;

public class User {
	
	private String userName;
	private String organization;
	private String password;
	private ArrayList<Rule> policy;
	
	public User() {
		
	}
	
	public User(String userName, String organization, String password,  ArrayList<Rule>  policy) {
		super();
		this.userName = userName;
		this.organization = organization;
		this.password = password;
		this.policy = policy;
	}
	
	

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public ArrayList<Rule> getPolicy() {
		return policy;
	}
	public void setPolicy(ArrayList<Rule> policy) {
		this.policy = policy;
	}
	
	@Override
	public String toString() {
		return "User [User Name=" + userName + ", Organization=" + organization + ", Password=" + password + ", Policy="
				+ policy + "]";
	}

}
