package service;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.annotation.ApplicationScope;

import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.resources.fluentcore.arm.Region;

import entity.Account;
import exeption.AlreadyExistingException;
import utility.DatabaseSingleton;

@Service
@ApplicationScope
public class AccountService {
	
	@Autowired
	Azure azure;
	@Autowired
	Environment env;
	@Autowired
	private Account account;
	
	private final String storageAccountBasename;
	
	private String checkExistance = "Select * from [dbo].[utente] where [dbo].[utente].[email] = '%s' ";
	private String insert = "Insert into [dbo].[utente] values ('%s','%s','%s','%s')";
	private String retrieveAccount = "Select * from [dbo].[utente] where [dbo].[utente].[email] = '%s' ";
	
	@Autowired
	public AccountService(Environment tmpEnv) {
		this.storageAccountBasename = tmpEnv.getProperty("azure.account-name");
	}
	
	
	public Account registration(@NotNull String nome, @NotNull String cognome, 
							 @NotNull String password, @NotNull @Email String email) 
									throws SQLException, AlreadyExistingException, ClassNotFoundException {
		
		DatabaseSingleton Database = DatabaseSingleton.getInstance();
		
		//Check if this users already exists
		ResultSet rs = Database.EseguiQuery(String.format(checkExistance, email));
		if(rs.next())
			throw new  AlreadyExistingException("Questo mail è già stata registrata");
		
		//Create new user
		if(Database.EseguiQueryUpdate(String.format(insert, email,nome,cognome,password))==0)
			throw new SQLException("Errore durante l'inserimento dell'utente");
		
		//Get my just created account
		rs = Database.EseguiQuery(String.format(retrieveAccount, email));
		rs.next();
		Account tmpAccount = new Account(rs.getInt("Id"), 
							  rs.getString("email"), 
							  rs.getString("first_name"), 
							  rs.getString("last_name"), ""); //No password needed
		
		account.setId(tmpAccount.getId());
		account.setEmail(tmpAccount.getEmail());
		account.setLast_name(tmpAccount.getLast_name());
		account.setNome(tmpAccount.getNome());
		account.setPassword("");
		
		createStorageAccount();
		
		return tmpAccount;
	}
	
	public void createStorageAccount() {
			//Register a new Storage Account for an account
			azure.storageAccounts().define( storageAccountBasename + account.getId() )
							.withRegion(Region.EUROPE_WEST)
							.withExistingResourceGroup(env.getProperty("azure.resource-group"))
							.withOnlyHttpsTraffic()
							.withSystemAssignedManagedServiceIdentity()
							.create();
	}
}
