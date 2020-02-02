package controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import entity.*;

@Controller
public class LogoutController {
	
	@GetMapping("/logout")
	public String logout(HttpSession httpSession) {
		if(httpSession!=null)
			httpSession.invalidate();
		return "redirect:/";
	}

}
