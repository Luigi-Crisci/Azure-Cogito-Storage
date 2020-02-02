package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import service.StorageService;

@Controller
public class CreateDirectoryController {

	@Autowired
	private StorageService storageController;
	
	@PostMapping("/account/createDir")
	public ResponseEntity<?> createDir(@RequestParam(name = "dirName", required = true) String name, 
			 @RequestParam(name = "dir",required = false, defaultValue = "") String currentDir) {
		
		return storageController.createDir(name,currentDir) ?
				new ResponseEntity<>("Created",HttpStatus.OK) : 
				new ResponseEntity<>("Error while creating directory",HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
