package myapp;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GetUriController {

//	@Autowired
//	private StorageConnectorBean storageBeanController;
	
	@GetMapping("/geturi")
	public String getUri(Model model) throws IOException {
//    	model.addAttribute("Files",storageBeanController.retrieveAll());
		StorageController x = new StorageController();
		x.testmetadata();
		return "geturi";
	}
}
