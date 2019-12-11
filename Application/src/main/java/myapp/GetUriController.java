package myapp;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.azure.storage.blob.models.BlobItem;

@Controller
public class GetUriController {

	@Autowired
	private StorageConnectorBean storageBeanController;

	@GetMapping("/account")
	public String getUri(Model model,HttpServletRequest request) throws IOException {
		HttpSession session = request.getSession();
		HashMap<BlobItem,String> blobs=storageBeanController.retrieveAll();
		session.setAttribute("Files", blobs);
		return "account";

	}
}
