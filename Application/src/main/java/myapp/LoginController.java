package myapp;

import java.sql.ResultSet;
import java.sql.SQLException;


import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;

import myapp.Account;

@Controller
public class LoginController {
	
	private Account account;
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	@PostMapping("/login")
		public String login(
				@RequestParam(name = "mailAddress",required = true) String mailAddress,
				@RequestParam(name = "passwd", required = true) String password_ins)throws ClassNotFoundException {
		
		DatabaseSingleton Database = DatabaseSingleton.getInstance();
		String query="select password from [dbo].[utente] where email = '"+ mailAddress +"';";
		ResultSet rs = Database.EseguiQuery(query);
		try {
			while(rs.next())
			{
				if( password_ins.equals(rs.getString(1))) {
					System.out.println("Password corretta");
					return "/account"; //ovvero la view dove stanno i file dell'utente
				}
				else {
					System.out.println("Password errata");
					return "/index";
				}
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return "";
		

		
//		if(name.equals("luigi") && password.equals("root")) {
//			//Database.EseguiQuery(query);
//			account.setNome(name);
//			model.addAttribute("account", account);
//			return "account";
//		}
//		else {
//			return "index";
//		}
		
	}
	

	
	
//	@GetMapping("/account")
//	public String checkAccount(Model model) {
//		if(!model.containsAttribute("account"))
//			return "index";
//		return "account";
//		
//	}

}
