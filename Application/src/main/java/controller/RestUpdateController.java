package controller;

import javax.servlet.http.Part;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import entity.Account;
import service.StorageService;

@RestController
public class RestUpdateController {
	
	@Autowired
	Environment env;
	@Autowired
	Account utente;
	@Autowired
	Logger logger;
	@Autowired
	StorageService storageControllerBean;
	
	/**
	 * Upload a file from a form
	 * @param file
	 * @return
	 */
	@PostMapping("/account/upload")
	public ResponseEntity<?> upload(@RequestParam(name = "files") Part file, 
			@RequestParam(name = "dir",required = false, defaultValue = "") String dir){
		
		logger.info("Received file to upload");
		if(file==null)
			return new ResponseEntity<>("Secelt a file",HttpStatus.OK);
		try {
			
			storageControllerBean.uploadFile(file,dir);
		
		}catch (Exception e) {
			logger.error(e.toString());
			return new ResponseEntity<>("Error while uploading, please try again",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("successfully uploaded!",HttpStatus.OK);
	}
	

}
