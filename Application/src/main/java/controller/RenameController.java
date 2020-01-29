package controller;

import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import exeption.AlreadyExistingException;
import exeption.BlobNotFoundExeption;
import service.StorageService;

@Controller
public class RenameController {
	
	@Autowired
	private StorageService storageService;
	
	@PostMapping("/account/rename")
	public ResponseEntity<?> rename(@RequestParam(name = "oldFilename", required = true) String oldFilename, 
			@RequestParam(name = "newFilename", required = true) @Pattern(regexp = "^[a-zA-Z0-9.-_]+$" ) String newFilename, 
			@RequestParam(name = "Overwrite", required = false, defaultValue = "false") String overwrite){
		
			//TODO: Chissà che cos'era sta cosa
			Boolean ow=new Boolean(false);
			try{
				ow=Boolean.parseBoolean(overwrite);
			}catch (Exception e) {
				ow=false;
			}
			
			try {
					storageService.rename(oldFilename, newFilename, ow);
				} catch (IllegalArgumentException e) {
					return new ResponseEntity<>("Error while deleting blob, plese try later",HttpStatus.INTERNAL_SERVER_ERROR);
				} catch (BlobNotFoundExeption e) {
					return new ResponseEntity<>("Original blob not found",HttpStatus.BAD_REQUEST);
				} catch (AlreadyExistingException e) {
					return new ResponseEntity<>("There are already a file with the specified name, plese change it "
							+ "or check the overwrite label",HttpStatus.BAD_REQUEST);
				}
			
			return new ResponseEntity<>("Renamed successfully",HttpStatus.OK);
		
	}

}
