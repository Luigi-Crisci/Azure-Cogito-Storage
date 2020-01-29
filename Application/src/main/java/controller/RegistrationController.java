package controller;

import javax.servlet.http.HttpServletRequest;
import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.AccountService;

@Controller
public class RegistrationController {
	
	@Autowired
	private AccountService accountService;
	
	@PostMapping("/registration")
	public String registration(@RequestParam(required = true, name = "firstName") String name,
							   @RequestParam(required = true, name = "lastName") String surname,
							   @RequestParam(required = true, name = "mailAddress") @Email String mail,
							   @RequestParam(required = true, name = "passwd") String password) {
		
		accountService.registration(name, surname, password, mail);
		//TODO: Handling exception from registration, but what are them?
		return "redirect:/account";
		
	}
	
	
	@ExceptionHandler({MissingServletRequestParameterException.class,MethodArgumentNotValidException.class})
	public String missingRegistrationParameter(HttpServletRequest request) {
		request.getSession().setAttribute("RegistrationError", "Errore nella formattazione o parametri mancanti");
		return "sign_up";
	}

}
