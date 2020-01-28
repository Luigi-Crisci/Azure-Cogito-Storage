package service;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.resources.fluentcore.arm.Region;

import entity.Account;

@Service
public class AccountService {
	
	@Autowired
	Azure azure;
	@Autowired
	Environment env;
	@Autowired
	private Account account;
	
	
	@PostMapping("/registration")
	public void registration(@NotNull String nome, @NotNull String cognome, 
								@NotNull String Password, @NotNull @Email String email) {
		
		
		//Inserisco nel db i dati 
		createStorageAccount();		
	}
	
	public void createStorageAccount() {
			//Register a new Storage Account for an account
			azure.storageAccounts().define( env.getProperty("azure.account-name") + account.getId() )
							.withRegion(Region.EUROPE_WEST)
							.withExistingResourceGroup(env.getProperty("azure.resource-group"))
							.withOnlyHttpsTraffic()
							.withSystemAssignedManagedServiceIdentity()
							.create();
	}
}
