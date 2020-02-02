package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


import entity.Account;

@Component
@Order(1)
public class LoginFilter implements Filter{

	@Autowired
	Logger logger;
	@Autowired
	Account account;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest= (HttpServletRequest) request;
		HttpServletResponse httpResponse= (HttpServletResponse) response;
		
		logger.debug("Filter: URL"
				+ " called: "+httpRequest.getRequestURL().toString());
		
		if (httpRequest.getRequestURL().toString().contains("/account") && account.getEmail() == null)	{		
			logger.debug("Not passed request " + httpRequest.getRequestURI());
			httpResponse.sendRedirect("/");
			return;
		}
		

		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}
}
