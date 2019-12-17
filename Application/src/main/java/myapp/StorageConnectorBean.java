package myapp;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.servlet.http.Part;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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
import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.appservice.FunctionApp;
import com.microsoft.azure.management.resources.fluentcore.arm.Region;
import com.microsoft.azure.management.storage.StorageAccount;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.BufferedSink;

@Service
@SessionScope
public class StorageConnectorBean {

	private static OkHttpClient httpClient;
	private static Pattern pattern;
	@Autowired
	private Environment env;
	@Autowired
	private Logger logger;
	@Autowired
	private CognitiveUploadService cognitiveUploadService;
	@Autowired
	private Account account;

	private Azure azure;
	private File devCredential;
	private StorageAccount storageAccount;
	@Value("${azure.subid}")
	private String subId;
	private BlobServiceClient blobServiceClient;
	private BlobContainerClient blobContainerClient;
	private UserDelegationKey key;
	final String storageAccountName;
	final String containerName;
	
	@Autowired
	public StorageConnectorBean(Environment tmpEnv) throws IOException {
		env=tmpEnv;
		
		//Get JSON Authentication file
		Resource resource = new ClassPathResource("appconfig.json");
		devCredential = new File(resource.getURI());
		
		azure= Azure.authenticate(devCredential).withSubscription(env.getProperty("azure.subid"));  //Only on local

		storageAccountName = env.getProperty("azure.account-name")+account.getId();
		containerName = env.getProperty("azure.default-container");
		
		storageAccount = azure.storageAccounts().getByResourceGroup(env.getProperty("azure.resource-group"),storageAccountName);
		if(storageAccount==null) registerAccount();
		blobServiceClient = new BlobServiceClientBuilder().credential(new DefaultAzureCredentialBuilder().build())
				.endpoint(storageAccount.endPoints().primary().blob()).buildClient(); 
		
		//Get reference to Container
		blobContainerClient=blobServiceClient.getBlobContainerClient(containerName);
		if(!blobContainerClient.exists())
			blobContainerClient=blobServiceClient.createBlobContainer(containerName);

	}
	
	/**
	 * Retrieve blobs from the current user
	 * @param path root path of retrieved blobs
	 * @return each blob with a sas link key associated
	 */
	public List<BlobItemKeyStruct> retrieve(String path){
		//Generate a new key only if is not expired
		if(key!=null) key.setSignedExpiry(OffsetDateTime.now().plusHours(1));
		else key= blobServiceClient.getUserDelegationKey(OffsetDateTime.now(), OffsetDateTime.now().plusHours(1));
		
		//Generate String for each blob
		//HashMap<BlobItem,String> mappedBlobs= new HashMap<BlobItem, String>();
		ListBlobsOptions options= new ListBlobsOptions();
		BlobListDetails detail= new BlobListDetails();
		detail.setRetrieveMetadata(true);
		options.setDetails(detail);
		
		PagedIterable<BlobItem> blobs=blobContainerClient.listBlobs(options,null);
		List<BlobItemKeyStruct> blobsList= new ArrayList<BlobItemKeyStruct>();
		
		//Omega tarantella
		String regexPath=path.replaceAll("\\/", "\\/");
		blobs.stream()
		.filter(e-> Pattern.matches("^"+regexPath+"[\\/]{0,1}[\\w.%-]*", e.getName()))
		.forEach(e->{
			final String name = e.getName();
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
		
		for(BlobItemKeyStruct x : blobsList) {
			System.out.println(x.toString());
		}
		
		blobsList.sort((a,b)-> a.getTrueName().compareTo(b.getTrueName())); //Sort item
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
		return String.format("https://%s.blob.core.windows.net/default/%s?%s", storageAccount.name(),blobName,param.encode());
	}
	
	/**
	 * Register a new storage account for a new user
	 * @return creation success or failure
	 */
	public boolean registerAccount() {
		try{
			//Register a new Storage Account for an account
			storageAccount = azure.storageAccounts().define(storageAccountName)
							.withRegion(Region.EUROPE_WEST)
							.withExistingResourceGroup(env.getProperty("azure.resource-group"))
							.withOnlyHttpsTraffic()
							.withSystemAssignedManagedServiceIdentity()
							.create();
			
		return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	/**
	 * Upload file to user account. If a path is specified, it will create the directory tree too
	 * @param file
	 * @throws IOException
	 */
	public void uploadFile(Part file,String path) throws IOException{
		BlobItem blob = new BlobItem();
		BlobClient blobClient=blobContainerClient.getBlobClient(path+file.getSubmittedFileName());
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
		
		//Setting file metadata
		if(key!=null) key.setSignedExpiry(OffsetDateTime.now().plusHours(1));
		else key= blobServiceClient.getUserDelegationKey(OffsetDateTime.now(), OffsetDateTime.now().plusHours(1));
		
		HashMap<String,String> metadata=cognitiveUploadService.getMetadata(file,createAccessLink(blobClient.getBlobName(), key)); //Set medatada for blob
		if(metadata!=null) 
			blobClient.setMetadata(metadata);
	}
	
	
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
		List<BlobItemKeyStruct> blobs = retrieve("");
		for(BlobItemKeyStruct blob: blobs) {
			Map<String,String> metadata=blob.getItem().getMetadata();
			
			//Get metadata, null if no tags are found
			List<String> tags=metadata.containsKey("tags") ? 
								Arrays.asList(metadata.get("tags").split(",")) :
								null;
								
			if(!(blob.getTrueName().contains(query) || (tags!=null && tags.contains(query))))
					blobs.remove(blob);
		}
		return blobs;
	}


}
