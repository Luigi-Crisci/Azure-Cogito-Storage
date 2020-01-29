package controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import entity.Account;
import exeption.AlreadyExistingException;
import service.AccountService;

@Controller
public class RegistrationController {
	
	@Autowired
	private AccountService accountService;
	@Autowired
	private HttpSession httpSession;
	@Autowired
	private Account account;
	
	@PostMapping("/registration")
	public String registration(@RequestParam(required = true, name = "firstName") String name,
							   @RequestParam(required = true, name = "lastName") String surname,
							   @RequestParam(required = true, name = "mailAddress") @Email String mail,
							   @RequestParam(required = true, name = "passwd") String password) throws ClassNotFoundException, SQLException, AlreadyExistingException {
		
		Account tmpAccount = accountService.registration(name, surname, password, mail);
		account.copyAccount(tmpAccount); //Copy new account into session-scoped account
		
		httpSession.setAttribute("account", account);
		
		return "redirect:/account";
		
	}
	
	
	@ExceptionHandler({MissingServletRequestParameterException.class,MethodArgumentNotValidException.class})
	public String missingRegistrationParameter(HttpServletRequest request) {
		request.getSession().setAttribute("RegistrationError", "Errore nella formattazione o parametri mancanti");
		return "sign_up";
	}

}
