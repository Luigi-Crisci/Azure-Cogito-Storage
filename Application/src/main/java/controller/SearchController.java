package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import service.StorageService;

@Controller
public class SearchController {
	
	@Autowired
	private StorageService storageService;

	@PostMapping("/account/search")
	public String search(@RequestParam(name = "query", required = true, defaultValue = "") String query,HttpServletRequest request ) {
		HttpSession session = request.getSession();
		session.setAttribute("results", storageService.search(query));
		return "result";
	}
}