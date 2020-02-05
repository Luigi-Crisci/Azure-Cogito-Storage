package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import entity.BlobItemKeyStruct;
import service.StorageService;

@Controller
public class AccountController {

	@Autowired
	private StorageService storageBeanController;
	
	@Autowired
	HttpSession session;
	
	@GetMapping("/account")
	public String getUri(@RequestParam(required = false, name = "dir",defaultValue = "") String path) throws IOException {
		
		List<BlobItemKeyStruct> blobs=storageBeanController.retrieve(path);
		session.setAttribute("Files", blobs);
		return "account";
	}
}