package controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import entity.Account;
import utility.DatabaseSingleton;

@Controller
public class LoginController {
	
	@Autowired
	private Account account;
	
	@GetMapping("/")
	public String index() {
		return "login";
	}
	
	@PostMapping("/login")
	public String login(
				@RequestParam(name = "mailAddress",required = true) String mailAddress,
				@RequestParam(name = "passwd", required = true) String password_ins)throws ClassNotFoundException {
		
		DatabaseSingleton Database = DatabaseSingleton.getInstance();
		String query="select * from [dbo].[utente] where email = '"+ mailAddress +"';";
		ResultSet rs = Database.EseguiQuery(query);
		try {
			while(rs.next())
			{
				if( password_ins.equals(rs.getString(5))) {
					account.setEmail(mailAddress);
					account.setId(rs.getInt(1));
					account.setNome(rs.getString("first_name"));
					account.setLast_name(rs.getString("last_name"));
					
					return "redirect:/account";				
				}
				else {
					System.out.println("Password errata o utente non registrato");
					return "sign_up";
				}
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
}
