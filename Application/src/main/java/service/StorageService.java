package service;

import java.util.List;
import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.naming.InvalidNameException;
import javax.servlet.http.Part;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import com.azure.core.http.rest.PagedIterable;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.BlobListDetails;
import com.azure.storage.blob.models.ListBlobsOptions;
import com.azure.storage.blob.models.UserDelegationKey;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasQueryParameters;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.azure.storage.blob.specialized.BlobOutputStream;
import com.azure.storage.common.sas.SasProtocol;
import com.google.common.base.Stopwatch;
import com.microsoft.azure.management.Azure;
import entity.Account;
import entity.BlobItemKeyStruct;
import exeption.AlreadyExistingException;
import exeption.BlobNotFoundExeption;
import utility.UploadUtils;

@Service
@SessionScope
public class StorageService {

//	@Autowired
//	private Environment env;
	@Autowired
	private Logger logger;
	@Autowired
	private CognitiveUploadService cognitiveUploadService;
//	@Autowired
//	private Account account;
	private BlobServiceClient blobServiceClient;
	private BlobContainerClient blobContainerClient;
	private UserDelegationKey key;
	private final String storageAccountName;
	private final String containerName;
	
	/**
	 * Initialize Azure's user resources
	 * @param tmpEnv Application environment
	 * @throws IOException Only on local, if the appconfig.json is not present in classpath
	 */
	@Autowired
	public StorageService(Environment tmpEnv,Account account,Azure azure) throws IOException {
		Stopwatch stopwatch = Stopwatch.createStarted(); //Needed to count method time call

		storageAccountName = tmpEnv.getProperty("azure.account-name")+account.getId();
		containerName = tmpEnv.getProperty("azure.default-container");
		
		//TODO: I don't know if I'll need it
//		storageAccount = azure.storageAccounts().getByResourceGroup(tmpEnv.getProperty("azure.resource-group"),storageAccountName);

		//Get a reference to a BlobServiceClientBuilder
		blobServiceClient = new BlobServiceClientBuilder().credential(new DefaultAzureCredentialBuilder().build())
				.endpoint(String.format("https://%s.blob.core.windows.net/", storageAccountName)).buildClient();
		
		//Get reference to Container
		blobContainerClient=blobServiceClient.getBlobContainerClient(containerName);
		if(!blobContainerClient.exists())
			blobContainerClient=blobServiceClient.createBlobContainer(containerName);
		
		System.out.println("EXT: Blob clients get completed in: " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
		stopwatch.stop();
	}
	
	/**
	 * Retrieve all blobs for the current user
	 * @return the blobs
	 */
	public List<BlobItemKeyStruct> retrieve(){
		key= blobServiceClient.getUserDelegationKey(OffsetDateTime.now(), OffsetDateTime.now().plusHours(1));
		//Generate String for each blob
		ListBlobsOptions options= new ListBlobsOptions();
		BlobListDetails detail= new BlobListDetails();
		detail.setRetrieveMetadata(true);
		options.setDetails(detail);
		
		PagedIterable<BlobItem> blobs=blobContainerClient.listBlobs(options,null);
		List<BlobItemKeyStruct> blobsList= new ArrayList<BlobItemKeyStruct>();
		
		blobs.stream()
			.forEach(e->{
				final String blobName = e.getName();
				String trueName= blobName.contains("/") ? blobName.substring(blobName.lastIndexOf('/')) : blobName;
				blobsList.add(new BlobItemKeyStruct(e, createAccessLink(blobName, key), false, trueName));
			});
		
		return blobsList;
	}
	
	/**
	 * Retrieve blobs from the current user
	 * @param path root path of retrieved blobs
	 * @return each blob with a sas link key associated
	 */
	public List<BlobItemKeyStruct> retrieve(String path){
		Stopwatch stopwatch = Stopwatch.createStarted();
		
		//Generate a new key 
		key= blobServiceClient.getUserDelegationKey(OffsetDateTime.now(), OffsetDateTime.now().plusHours(1));
		
		//Generate String for each blob
		ListBlobsOptions options= new ListBlobsOptions();
		BlobListDetails detail= new BlobListDetails();
		detail.setRetrieveDeletedBlobs(false); //Do not retrieve deleted blobs
		detail.setRetrieveSnapshots(false); //Do not retrieve any snapshot of any file
		detail.setRetrieveMetadata(true);
		options.setDetails(detail);
		
		PagedIterable<BlobItem> blobs=blobContainerClient.listBlobs(options,null);
		List<BlobItemKeyStruct> blobsList= new ArrayList<BlobItemKeyStruct>();
		
		//Omega tarantella
		String regexPath=path.replaceAll("\\/", "\\/");
		blobs.stream()
		.filter(e-> Pattern.matches("^"+regexPath+"[\\/]{0,1}[\\w._\\%\\-()!?&$£]*", e.getName()) && !e.getName().contains(".blank"))
		.forEach(e->{
			final String name = e.getName();
			logger.info(name  + ": " + e.getProperties().getContentLength());
			String trueName= name.contains("/") ? name.substring(name.lastIndexOf('/')+1) : name;
			blobsList.add(new BlobItemKeyStruct(e, createAccessLink(name,key), false, trueName));
		});
		
		//Get directories
		blobs.stream()
				.filter(e-> Pattern.matches(regexPath+"[\\/]{0,1}[\\w.%-]*[\\/]{1}[\\w.%-]*", e.getName()))
				.map(e->{
					String x = e.getName();
					if(x.indexOf('/', path.length())!=-1)
						return x.substring(path.length(), (x.indexOf('/', path.length())+1));
					else
						return x;
				})
				.distinct()
				.forEach(e->blobsList.
						add(new BlobItemKeyStruct(null, String.format("/account?dir=%s%s", path,e), true,e)));
		
		//Print all blobs, just to log them in the console
		for(BlobItemKeyStruct x : blobsList) {
			System.out.println(x.toString());
		}
		
		//Sort blobs
		blobsList.sort((a,b)-> a.getTrueName().compareTo(b.getTrueName())); //Sort item
		
		System.out.println("EXT: Retrieve blobs completed in: " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
		return blobsList;
	}
	
	
	public String createAccessLink(String blobName,UserDelegationKey key){
		BlobSasPermission blobPermission = new BlobSasPermission()
			    .setReadPermission(true)
			    .setWritePermission(true);
		
		BlobServiceSasSignatureValues builder = new BlobServiceSasSignatureValues()
				.setProtocol(SasProtocol.HTTPS_ONLY)
				.setExpiryTime(OffsetDateTime.now().plusDays(2))
				.setContainerName(containerName)
				.setBlobName(blobName)
				.setPermissions(blobPermission);
		
		BlobServiceSasQueryParameters param = builder.generateSasQueryParameters(key,storageAccountName);
		return String.format("https://%s.blob.core.windows.net/default/%s?%s", storageAccountName,blobName,param.encode());
	}



	/**
	 * Upload file to user account. If a path is specified, it will create the directory tree too
	 * @param file
	 * @throws IOException
	 */
	public void uploadFile(Part file,String path) throws IOException{
		String filename = path+file.getSubmittedFileName();
		BlobClient blobClient=blobContainerClient.getBlobClient(filename);
		
		if(blobClient.exists()) { //If a file with the same name exists, generate a new progressive name 
			int i = 1;
			while(true) {
				String tmpFilename = filename.substring(0, filename.lastIndexOf('.')) + "(" + i + ")" + filename.substring(filename.lastIndexOf('.'));
				blobClient = blobContainerClient.getBlobClient(tmpFilename);
				if(blobClient.exists()) {
					i++;
					continue;
				}
				filename = tmpFilename;
				break;
			}
		}
		
		BlobOutputStream blobOutputStream=blobClient.getBlockBlobClient().getBlobOutputStream();
		InputStream fileStream = file.getInputStream();
		byte[] b= new byte[20*1024*1024];
		logger.info(String.format("Writing file %s to %s\n", file.getSubmittedFileName(),blobClient.getBlobUrl()));
		int numBytesRead;
		while ((numBytesRead=fileStream.read(b, 0, b.length))>0) {
			blobOutputStream.write(b,0,numBytesRead);
		}
		logger.info(String.format("Saved ad position: %s", blobClient.getBlobUrl()));
		
		blobOutputStream.close();
		fileStream.close();
		
		key= blobServiceClient.getUserDelegationKey(OffsetDateTime.now(), OffsetDateTime.now().plusHours(1));
		
		HashMap<String,String> metadata=cognitiveUploadService.getMetadata(file,createAccessLink(blobClient.getBlobName(), key)); //Set medatada for blob
		if(metadata!=null) 
			blobClient.setMetadata(metadata);
	}
	
	/**
	 * Create a Directory: it creates a blank file named .blank into "path"
	 * @param nameDir Directory name
	 * @param path Where to save it
	 * @return if has been created
	 */
	public boolean createDir(String nameDir,String path) {
		try{
			uploadFile(UploadUtils.blankFile,path+nameDir+'/');
			return true;
		}catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Search blobs in according to a query
	 * @param query the string that a blob must contain (in name or tags)
	 * @return the blobs list
	 */
	public List<BlobItemKeyStruct> search(String query){
		List<BlobItemKeyStruct> blobs = retrieve();
		
		return blobs.stream().filter(blob->{
			Map<String,String> metadata=blob.getItem().getMetadata();
			//Get metadata, null if no tags are found
			List<String> tags=(metadata!=null && metadata.containsKey("Tags")) ? 
								Arrays.asList(metadata.get("Tags").split(",")) :
								null;
			return (blob.getTrueName().contains(query)) || (tags!=null && tags.contains(query));
			}).collect(Collectors.toList());	
	}

	/**
	 * Delete a blob
	 * @param fileName blob file name
	 * @return if has been deleted
	 */
	public boolean delete(String filename) {
		filename = filename.trim();
		try {
			logger.info("Deleting blob : " + filename);
			
			if(filename.endsWith("/")) {
				//Is a directory, delete all files inside it
				List<BlobItemKeyStruct> list = retrieve(filename);
				for(BlobItemKeyStruct b : list) 
					delete(filename+b.getTrueName());
				delete(filename+".blank"); //delete the blank file. It is not returned from retrieve function
			}
			else
				blobContainerClient.getBlobClient(filename).delete(); //delete the requested Blob
			
			logger.info("Delete completed");
			return true;
		}catch(Exception e) {
				logger.info("Unable to delete blob: \n");
				e.printStackTrace();
				return false;
			}
	}
	
	/**
	 * Rename a file
	 * @param blobName
	 * @param newFilename
	 * @param overwrite
	 * @return
	 * @throws BlobNotFoundExeption
	 * @throws AlreadyExistingException
	 * @throws InvalidNameException 
	 */
	public synchronized String rename(String blobName,String newFilename,boolean overwrite) throws BlobNotFoundExeption, AlreadyExistingException, IllegalArgumentException, InvalidNameException {
		
		if(blobName.equals(newFilename))
			return blobName;
		
		if(blobName.matches("^[\\w.-_()\\%?!&$£]+.[\\w-_()\\%?!&$£]+$")){
			if(newFilename.matches("^[\\w.-_()\\%?!&$£]+.[\\w-_()\\%?!&$£]+$")) {
				String ext = blobName.substring(blobName.lastIndexOf('.')+1);
				if(!newFilename.substring(newFilename.lastIndexOf(".") + 1).equals(ext))
					throw new InvalidNameException("Invalid name inserted");
			}
			else throw new InvalidNameException("Invalid name inserted"); 
		}
		
		
		BlobClient oldBlobClient=blobContainerClient.getBlobClient(blobName);
		if(!oldBlobClient.exists())
			throw new BlobNotFoundExeption(String.format("Blob %s not found", blobName));
		BlobClient newBlobClient=blobContainerClient.getBlobClient(newFilename);
		if(!overwrite && newBlobClient.exists())
			throw new AlreadyExistingException(String.format("Blob %s already existing! Set overwrite to write on it", newFilename));
		
		key= blobServiceClient.getUserDelegationKey(OffsetDateTime.now(), OffsetDateTime.now().plusHours(1));
		newBlobClient.copyFromUrl(createAccessLink(blobName, key));
		oldBlobClient.delete();
		
		return createAccessLink(newFilename, key);
	} 


}
