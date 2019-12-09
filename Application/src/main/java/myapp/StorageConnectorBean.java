package myapp;

import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.azure.core.http.rest.PagedIterable;
import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.BlobListDetails;
import com.azure.storage.blob.models.ListBlobsOptions;
import com.azure.storage.blob.models.UserDelegationKey;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasQueryParameters;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.azure.storage.common.sas.SasProtocol;
import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.resources.fluentcore.arm.Region;
import com.microsoft.azure.management.resources.fluentcore.model.Creatable;
import com.microsoft.azure.management.storage.StorageAccount;

@Service
@SessionScope
public class StorageConnectorBean {

	@Autowired
	Environment env;
	
	private Azure azure;
	private File devCredential;
	private StorageAccount storageAccount;
	@Value("${azure.subid}")
	private String subId;
	private Account account;
	private BlobServiceClient blobClient;
	private UserDelegationKey key;
	
	@Autowired
	public StorageConnectorBean(Environment tmpEnv) throws IOException {
		env=tmpEnv;
		devCredential = new File("C:\\Users\\luigi\\git\\AzureIAS\\Application\\appconfig.json");
		azure= Azure.authenticate(devCredential).withSubscription(env.getProperty("azure.subid"));  //Only on local
		//For testing
		account = new Account();
		account.setId(1);
		account.setNome("Luigi");
		//
		storageAccount = azure.storageAccounts().getByResourceGroup("azureias-rg", "azureiasstorage");
		if(storageAccount==null) registerAccount();
		String endpoint = String.format(Locale.ROOT, "https://%s.blob.core.windows.net", "azureiasstorage");
		blobClient = new BlobServiceClientBuilder().credential(new DefaultAzureCredentialBuilder().build())
				.endpoint(endpoint).buildClient();
	}
	
	/**
	 * Retrieve all blobs from the current user
	 * @return each blob with a sas link key associated
	 */
	public HashMap<BlobItem,String> retrieveAll(){
		//Generate a new key only if is not expired
		if(key!=null) key.setSignedExpiry(OffsetDateTime.now().plusHours(1));
		else key= blobClient.getUserDelegationKey(OffsetDateTime.now(), OffsetDateTime.now().plusHours(1));
		
		blobClient.listBlobContainers().forEach(e -> System.out.println(e.getName()+"\n"));
		
		//Generate String for each blob
		HashMap<BlobItem,String> mappedBlobs= new HashMap<BlobItem, String>();
		ListBlobsOptions options= new ListBlobsOptions();
		BlobListDetails detail= new BlobListDetails();
		detail.setRetrieveMetadata(true);
		options.setDetails(detail);
		PagedIterable<BlobItem> blobs=blobClient.getBlobContainerClient("default").listBlobs(options,null);
		blobs.forEach(e->{
			System.out.println("\nName file: " + e.getName()+'\n');});
		
		blobs.forEach(e-> mappedBlobs.put(e, createAccessLink(e,key)));
		
		return mappedBlobs;
	}
	
	public String createAccessLink(BlobItem blob,UserDelegationKey key){
		BlobSasPermission blobPermission = new BlobSasPermission()
			    .setReadPermission(true)
			    .setWritePermission(false);
		
		BlobServiceSasSignatureValues builder = new BlobServiceSasSignatureValues()
				.setProtocol(SasProtocol.HTTPS_ONLY)
				.setExpiryTime(OffsetDateTime.now().plusDays(2))
				.setContainerName("dafault")
				.setBlobName(blob.getName())
				.setPermissions(blobPermission);
		
		BlobServiceSasQueryParameters param = builder.generateSasQueryParameters(key,""+account.getId());
		return String.format("https://%s.blob.core.windows.net/default/%s?%s", storageAccount.name(),blob.getName(),param.encode());
	}
	
	/**
	 * Register a new storage account for a new user
	 * @return creation success or failure
	 */
	public boolean registerAccount() {
		try{
			//Register a new Storage Account for an account
			storageAccount = azure.storageAccounts().define(""+account.getId())
							.withRegion(Region.EUROPE_WEST)
							.withExistingResourceGroup(env.getProperty("resource-group-name"))
							.withOnlyHttpsTraffic()
							.create();
		storageAccount.manager().blobContainers().defineContainer("default"); //No need for custom container
		return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	

}
