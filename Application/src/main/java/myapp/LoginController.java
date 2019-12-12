package myapp;

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

import myapp.Account;

@Controller
public class LoginController {
	
	@Autowired
	private Account account;
	
	@GetMapping("/")
	public String index() {
		return "login";
	}
//	@GetMapping("/sign_up")
//	public String sign_up_Page() {
//		return "sign_up";
//	}
	
	
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
					System.out.println("Password inserita: "+ password_ins +" Password in database: "+ rs.getString(1));
					System.out.println("Password corretta");
					return "account"; //ovvero la view dove stanno i file dell'utente
				}
				else {
					System.out.println("Password errata o non sei registrato");
					return "sign_up";
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
