package utility;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.AppServiceMSICredentials;
import com.microsoft.azure.credentials.MSICredentials;
import com.microsoft.azure.management.Azure;

@Component
public class AzureCredentialProducer implements InitializingBean{
	
	private Azure azure;
	
	@Autowired
	private Environment env;
	@Autowired
	private Logger logger;

	@Override
	public void afterPropertiesSet() throws Exception {
		//Only on local
		Resource resource = new ClassPathResource("appconfig.json");
		File devCredential = new File(resource.getURI());
//		new AppServiceMSICredentials(AzureEnvironment.AZURE) for online testing
		azure= Azure.authenticate(devCredential).withSubscription(env.getProperty("azure.subid"));  //Only on local
		logger.info("Logged into Azure account successfully!");
	}
	
	@Bean
	public Azure getAzure() {
		return azure;
	}
	

}
