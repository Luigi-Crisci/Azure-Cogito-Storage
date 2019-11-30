package myapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import myapp.*;

@Controller
public class MyAppController {
    
    @RequestMapping("/")
    public String index() {
    	StorageController x= new StorageController();
    	x.connect();
        return "index.html";
    }
    
}
