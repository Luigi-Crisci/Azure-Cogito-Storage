package myapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import myapp.Account;

@Controller
public class LoginController {
	
	private Account account = new Account();
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	@PostMapping("/login")
		public String login(@RequestParam(name = "name",required = true) String name, @RequestParam(name = "password", required = true) String password, Model model ) {
		System.out.println("name: " + name + " password: " + password + "\n");
		
		if(name.equals("luigi") && password.equals("root")) {
			System.out.println("aaaaa");
			account.setNome(name);
			model.addAttribute("account", account);
			return "account";
		}
		else {
			return "index";
		}
		
	}
	
	@GetMapping("/account")
	public String checkAccount(Model model) {
		if(!model.containsAttribute("account"))
			return "index";
		return "account";
		
	}

}
