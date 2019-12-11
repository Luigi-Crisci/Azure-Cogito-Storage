package myapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.servlet.http.Part;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestUpdateController {
	
	@Autowired
	Environment env;
	@Autowired
	Logger logger;
	
	/**
	 * Upload a file from a form
	 * @param file
	 * @return
	 */
	@PostMapping("/account/upload")
	public ResponseEntity<?> upload(@RequestParam(name = "files") Part file){
		logger.info("Received file to upload");
		if(file==null)
			return new ResponseEntity<>("Secelt a file",HttpStatus.OK);
		try {
			saveUploadFile(file);
		}catch (Exception e) {
			logger.error(e.toString());
			return new ResponseEntity<>("Error while uploading, please try again",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("successfully uploaded!",HttpStatus.OK);
	}
	
	
	/**
	 * Save a file to the path specified as default in the Environment
	 * @param file
	 * @throws IOException
	 */
	public void saveUploadFile(Part file) throws IOException {
		//Select tmp file position
		String path=env.getProperty("file.tmp-position");
		Path savePath=Paths.get(String.format("%s%s%s", path,File.separator,file.getSubmittedFileName()));
		Files.createDirectories(savePath.getParent()); //Create directories if not existing
		
		//Write file 20Mb at time
		FileOutputStream fileOutputStream = new FileOutputStream(savePath.toString());
		InputStream fileStream = file.getInputStream();
		byte[] b= new byte[20*1024*1024];
		logger.info("Writing file..\n");
		int numBytesRead;
		while ((numBytesRead=fileStream.read(b, 0, b.length))>0) {
			fileOutputStream.write(b,0,numBytesRead);
		}
		logger.info(String.format("Saved ad position: %s", savePath));
		
		fileOutputStream.close();
		fileStream.close();
	}

}
