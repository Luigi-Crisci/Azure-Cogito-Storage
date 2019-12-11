package myapp;

import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.azure.core.http.rest.PagedIterable;
import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerAsyncClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceAsyncClient;
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
import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.AppServiceMSICredentials;
import com.microsoft.azure.credentials.MSICredentials;
import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.storage.*;
import com.microsoft.azure.storage.SharedAccessAccountPolicy;
import com.microsoft.azure.storage.SharedAccessPolicy;
import com.microsoft.azure.storage.blob.BlobListingDetails;

public class StorageController {
	
	private String subscriptionId="9ffa1034-e692-4bb6-aaa7-172fa6352018";
	private DefaultAzureCredential x;
	private File devCredential;
	private Azure azure;
	private StorageAccount account;

	public StorageController() throws IOException {
		x = new DefaultAzureCredentialBuilder().build();
		devCredential = new File("appconfig.json"); //Only on local
		//azure= Azure.authenticate(new AppServiceMSICredentials(AzureEnvironment.AZURE)).withSubscription(subscriptionId);
		azure= Azure.authenticate(devCredential).withSubscription(subscriptionId);  //Only on local
		account = azure.storageAccounts().getByResourceGroup("azureias-rg", "azureiasstorage");
	}
	
	public String connect(){
		//Get The account
		
		
		BlobServiceClient blobClient = new BlobServiceClientBuilder().credential(x).endpoint(account.endPoints().primary().blob()).buildClient();
		
		//Defining permission
		BlobSasPermission blobPermission = new BlobSasPermission()
			    .setReadPermission(true)
			    .setWritePermission(false);
		
		//Defining token properties
		BlobServiceSasSignatureValues builder = new BlobServiceSasSignatureValues()
					.setProtocol(SasProtocol.HTTPS_ONLY)
					.setExpiryTime(OffsetDateTime.now().plusDays(2))
					.setContainerName("testjavacontainer")
					.setBlobName("vm_quic_http3_nginx.mf")
					.setPermissions(blobPermission);
		//Get the key for the
		UserDelegationKey key= blobClient.getUserDelegationKey(OffsetDateTime.now(), OffsetDateTime.now().plusHours(1));
		BlobServiceSasQueryParameters param = builder.generateSasQueryParameters(key,"azureiasstorage");
		
		return param.encode();
	
	}
	
	
	public void testmetadata() {
		BlobServiceClient blobClient = new BlobServiceClientBuilder().credential(x).endpoint(account.endPoints().primary().blob()).buildClient();
		
		BlobContainerClient container= blobClient.getBlobContainerClient("default");
		
		BlobClient file=container.getBlobClient("img2.png");
		HashMap<String, String> metadata= new HashMap<String, String>();
		metadata.put("Oggetto", "Cane");
		file.uploadFromFile("D:\\Download\\scenarioB.png",true);
		file.setMetadata(metadata);
		
		PagedIterable<BlobItem> blobs = container.listBlobs();
		System.out.println("Recuperata lista\n");
		for(BlobItem i: blobs) {
			System.out.println("Blob name: " + i.getName());
		}
		System.out.println("Dopo stampa\n");
		ListBlobsOptions options= new ListBlobsOptions();
		BlobListDetails detail= new BlobListDetails();
		detail.setRetrieveMetadata(true);
		options.setDetails(detail);
		container.listBlobs(options, null).forEach(e->{
			System.out.println("\nName file: " + e.getName()+'\n');
			e.getMetadata().forEach((k,v) -> System.out.println("Key: " + k + ", value: " + v+"\n"));
		});;
		
	}

}
