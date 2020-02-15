package controller;

import java.sql.SQLException;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import entity.Account;
import exeption.UserNotFoundException;
import exeption.WrongPasswordException;
import service.AccountService;

@Controller
public class LoginController {
	
	@Autowired
	private Account account;
	@Autowired
	private AccountService accountService;
	@Autowired
	private HttpSession session;
	
	@GetMapping("/")
	public String index() {
		return "login";
	}
	
	@PostMapping("/login")
	public String login(
				@RequestParam(name = "mailAddress",required = true) String mailAddress,
				@RequestParam(name = "passwd", required = true) String password_ins)
						throws ClassNotFoundException, LoginException, UserNotFoundException, WrongPasswordException, SQLException {
		
		accountService.login(mailAddress, password_ins);
		session.setAttribute("account", account);
		
		return "redirect:/account";		
	}
	
	@ExceptionHandler({LoginException.class,UserNotFoundException.class,WrongPasswordException.class,SQLException.class})
	public String loginError(HttpSession session, Exception e) {
		session.setAttribute("Exception", e.getMessage());
		return "redirect:/";
	}
	
}
