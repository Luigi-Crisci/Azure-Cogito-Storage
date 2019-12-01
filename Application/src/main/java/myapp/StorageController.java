package myapp;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.AppServiceMSICredentials;
import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.storage.*;

public class StorageController {
	
	private String subscriptionId="9ffa1034-e692-4bb6-aaa7-172fa6352018";
	
	
	
	public void connect() throws IOException {
		DefaultAzureCredential x = new DefaultAzureCredentialBuilder().build();

		Azure azure= Azure.authenticate(new AppServiceMSICredentials(AzureEnvironment.AZURE)).withSubscription(subscriptionId);
		StorageAccount account = azure.storageAccounts().getByResourceGroup("azureias-rg", "azureiasstorage");
		
		
		System.out.println("Endpoint: " + account.endPoints().primary().blob()+"\n");
		BlobServiceClient blobClient = new BlobServiceClientBuilder().credential(x).endpoint(account.endPoints().primary().blob()).buildClient();
		
		//BlobServiceClient blobClient = new BlobServiceClientBuilder().connectionString(account.getKeys().get(0).value()).buildClient();
		blobClient.createBlobContainer("testdefaultcredential"+UUID.randomUUID().toString().toLowerCase());
		
		//System.out.println("Account id: " + azure.storageAccounts().checkNameAvailability("Azzzzz").isAvailable());
		
//		String str=System.getenv("CONNECT_STR");
//		System.out.println("String: " + str);
//		BlobServiceClient client= new BlobServiceClientBuilder().connectionString(str).buildClient();
//		client.createBlobContainer("" + UUID.randomUUID().toString().toLowerCase());
		
	
	}

}
