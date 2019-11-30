package myapp;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Env;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.AppServiceMSICredentials;
import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.storage.*;

public class StorageController {
	
	private String subscriptionId="9ffa1034-e692-4bb6-aaa7-172fa6352018";
	
	public void connect() {
//		Azure azure = Azure.authenticate(new AppServiceMSICredentials(AzureEnvironment.AZURE))
//		        .withSubscription(subscriptionId);
//		StorageAccount account= azure.storageAccounts().getByResourceGroup("azureias-rg", "azureiasstorage");
//		System.out.println("Account id: " + account.id());
		//Vault myKeyVault = azure.vaults().getByResourceGroup(resourceGroup, keyvaultName);
		
		String str=System.getenv("CONNECT_STR");
		BlobServiceClient client= new BlobServiceClientBuilder().connectionString(str).buildClient();
		client.createBlobContainer("TestJavaContainer");
		
	
	}

}
