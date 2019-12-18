package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.azure.storage.blob.models.BlobItem;

import entity.BlobItemKeyStruct;
import service.StorageService;

@Controller
public class AccountController {

	@Autowired
	private StorageService storageBeanController;
	
	@GetMapping("/account")
	public String getUri(Model model,HttpServletRequest request,@RequestParam(required = false, name = "dir",defaultValue = "") String path) throws IOException {
		HttpSession session = request.getSession();
		List<BlobItemKeyStruct> blobs=storageBeanController.retrieve(path);
		session.setAttribute("Files", blobs);
		return "account";
	}
}