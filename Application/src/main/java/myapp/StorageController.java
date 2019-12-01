package myapp;

import java.util.Random;
import java.util.UUID;

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
		AppServiceMSICredentials credential = new AppServiceMSICredentials(AzureEnvironment.AZURE);
		
		Azure azure = Azure.authenticate(credential).withSubscription(subscriptionId);

		System.out.println("Account id: " + azure.storageAccounts().checkNameAvailability("Azzzzz"));
		
//		String str=System.getenv("CONNECT_STR");
//		System.out.println("String: " + str);
//		BlobServiceClient client= new BlobServiceClientBuilder().connectionString(str).buildClient();
//		client.createBlobContainer("" + UUID.randomUUID().toString().toLowerCase());
		
	
	}

}
