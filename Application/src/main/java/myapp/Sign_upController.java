package myapp;

import java.sql.ResultSet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Sign_upController {
	
	public Account utente;
	
	@GetMapping("/sign_up")
	public String sign_in() {
		return "sign_up";
	}
	
	@PostMapping("/registrazione")
	public String Registrazione(@RequestParam(name = "firstName", required = true) String firstName,
			@RequestParam(name = "lastName", required = true) String lastName,
			@RequestParam(name = "mailAddress", required = true) String mailAddress,
			@RequestParam(name = "passwd", required = true) String passwd) {

		DatabaseSingleton Database;
		try {
			Database = DatabaseSingleton.getInstance();
			String query = "INSERT INTO utente (email, first_name, last_name, password) VALUES ('" + mailAddress + "', '"
					+ firstName + "', '" + lastName + "', '" + passwd + "');";
			Database.EseguiQuery(query);
			return "index";
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		
		return "sign_up";
	}
}
