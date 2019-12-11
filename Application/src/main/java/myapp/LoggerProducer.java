package myapp;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class LoggerProducer {
	
	@Bean
	public Logger getLogger() {
		return Logger.getRootLogger();
	}

}
