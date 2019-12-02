package myapp;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GetUriController {

	
	@GetMapping("/geturi")
	public String getUri(Model model) throws IOException {
    	StorageController x= new StorageController();
    	model.addAttribute("file","https://azureiasstorage.blob.core.windows.net/testjavacontainer/vm_quic_http3_nginx.mf");
    	model.addAttribute("FileUri",x.connect());
		return "geturi";
	}
}
