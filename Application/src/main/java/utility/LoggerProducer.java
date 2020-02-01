package utility;

import org.apache.log4j.*;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class LoggerProducer {
	

	@Bean
	@Scope("prototype")
	Logger logger(InjectionPoint injectionPoint){
    	return Logger.getLogger(injectionPoint.getAnnotatedElement().getClass());
	}

}
