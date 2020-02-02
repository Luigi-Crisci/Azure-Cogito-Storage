package controller;

import java.io.IOException;

import javax.servlet.http.Part;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

	@PostMapping("/account/upload")
	public ResponseEntity<?> upload(@RequestParam(name = "files") Part file, 
			@RequestParam(name = "dir",required = false, defaultValue = "") String dir) throws Exception{
		
		logger.info("Received file to upload");
		if(file==null)
			throw new Exception("No file to be uploaded");
		
		storageControllerBean.uploadFile(file,dir);
		return new ResponseEntity<>("successfully uploaded!",HttpStatus.OK);
	}
	
	@ExceptionHandler({Exception.class})
	public ResponseEntity<?> handlingError(){
		return new ResponseEntity<>("Error while uploading, please try again",HttpStatus.INTERNAL_SERVER_ERROR);
	}
	

}
