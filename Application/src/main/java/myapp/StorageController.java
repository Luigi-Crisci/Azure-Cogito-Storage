package myapp;

import java.io.IOException;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAmount;
import java.util.UUID;

import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.identity.ManagedIdentityCredential;
import com.azure.identity.ManagedIdentityCredentialBuilder;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
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

public class StorageController {
	
	private String subscriptionId="9ffa1034-e692-4bb6-aaa7-172fa6352018";
	
	public String connect() throws IOException {
		DefaultAzureCredential x = new DefaultAzureCredentialBuilder().build(); //Access token to resources
		//File devCredential = new File("C:\\Users\\luigi\\git\\AzureIAS\\Application\\appconfig.json"); //Only on local
		
		//Authenticate with management api
		Azure azure= Azure.authenticate(new AppServiceMSICredentials(AzureEnvironment.AZURE)).withSubscription(subscriptionId);
		//Azure azure= Azure.authenticate(devCredential).withSubscription(subscriptionId);  //Only on local
		
		//Get The account
		StorageAccount account = azure.storageAccounts().getByResourceGroup("azureias-rg", "azureiasstorage");
		
		BlobServiceClient blobClient = new BlobServiceClientBuilder().credential(x).endpoint(account.endPoints().primary().blob()).buildClient();
		//String tmpName="testdefaultcredential"+UUID.randomUUID().toString().toLowerCase();
	//	blobClient.createBlobContainer(tmpName);
	//	blobClient.getBlobContainerClient("testdefaultcredential").getBlobClient("azz");
		
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

		
		
				
//		String str=System.getenv("CONNECT_STR");
//		System.out.println("String: " + str);
//		BlobServiceClient client= new BlobServiceClientBuilder().connectionString(str).buildClient();
//		client.createBlobContainer("" + UUID.randomUUID().toString().toLowerCase());
		
	
	}

}
