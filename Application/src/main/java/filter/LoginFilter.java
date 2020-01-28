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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.azure.core.http.HttpResponse;

import entity.Account;
import entity.BoringLog;

//@Component
//@Order(1)
public class LoginFilter implements Filter{

	@Autowired
	BoringLog boringLog;
	@Autowired
	Account account;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpRequest=null;
		HttpServletResponse httpResponse=null;
		
		httpRequest= (HttpServletRequest) request;
		httpResponse= (HttpServletResponse) response;
		
		HttpServletResponse  myResponse= (HttpServletResponse) response;
		boringLog.debug("Filter: URL"
				+ " called: "+httpRequest.getRequestURL().toString());
		System.out.println("mail utente "+account.getEmail());
		System.out.println("id utente" +account.getId());

		if (httpRequest.getRequestURL().toString().contains("/account") && account.getEmail() == null)	{			
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
