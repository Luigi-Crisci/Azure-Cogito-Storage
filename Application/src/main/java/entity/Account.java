package entity;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class Account {

	private Integer id;
	private String email;
	private String first_name;
	private String last_name;
	private String password;
	
	public Account() {
		email=first_name=last_name=password=null;
		id=null;
	}
	
	public Account(Integer id, String email, String first_name, String last_name, String password) {
		super();
		this.id = id;
		this.email = email;
		this.first_name = first_name;
		this.last_name = last_name;
		this.password = password;
	}
	
	public Account(Account tmpAccount) {
		super();
		copyAccount(tmpAccount);
	}
	
	public void copyAccount(Account tmpAccount) {
			this.id = tmpAccount.id;
			this.email = tmpAccount.email;
			this.first_name = tmpAccount.first_name;
			this.last_name = tmpAccount.last_name;
			this.password = tmpAccount.password;
	}
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
	public String getNome() {
		return first_name;
	}

	public void setNome(String first_name) {
		this.first_name = first_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
