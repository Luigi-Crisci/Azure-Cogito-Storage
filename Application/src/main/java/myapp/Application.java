package myapp;

import java.util.Arrays;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import filter.LoginFilter;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.JspServlet;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;

@SpringBootApplication(scanBasePackages = {"controller,service,entity,filter,utility"})
public class Application extends SpringBootServletInitializer{
	

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
    
    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }
            
        };
    }
    
    
    
    /**
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return (ConfigurableEmbeddedServletContainer container) -> {
            TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;
            JspServlet servlet = tomcat.getJspServlet();
            Map<String, String> jspServletInitParams = servlet.getInitParameters();
            jspServletInitParams.put("compilerSourceVM", "1.8");
            jspServletInitParams.put("compilerTargetVM", "1.8");
            servlet.setInitParameters(jspServletInitParams);
        };
    }
    */
    
//	@Bean
//	public FilterRegistrationBean LoginFilterRegistration(){
//		FilterRegistrationBean login = new FilterRegistrationBean();
//		login.setFilter(new LoginFilter());
//		login.setOrder(1);
//		login.addUrlPatterns("/account*");
//		return login;
//	}

}
