package myapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
		public String login(@RequestParam(name = "name",required = true) String name, @RequestParam(name = "password", required = true) String password, Model model ) throws ClassNotFoundException {
		System.out.println("name: " + name + " password: " + password + "\n");
		
		//DatabaseSingleton Database = DatabaseSingleton.getInstance();
		String query="INSERT INTO DBO.UTENTE (email, password_auth) VALUES ('peppeXXX@test.it', 'test')";
		
		if(name.equals("luigi") && password.equals("root")) {
			//Database.EseguiQuery(query);
			account.setNome(name);
			model.addAttribute("account", account);
			return "account";
		}
		else {
			return "index";
		}
		
	}
	
//	@GetMapping("/account")
//	public String checkAccount(Model model) {
//		if(!model.containsAttribute("account"))
//			return "index";
//		return "account";
//		
//	}
	
	public boolean testdb() throws ClassNotFoundException {
		DatabaseSingleton Database = DatabaseSingleton.getInstance();
		String query="INSERT INTO DBO.UTENTE (email, password_auth) VALUES ('peppeXXX@test.it', 'test')";
		Database.EseguiQuery(query);
		return true;
	}
	


}
