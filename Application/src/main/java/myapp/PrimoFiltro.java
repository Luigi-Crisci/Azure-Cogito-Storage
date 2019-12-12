package myapp;

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

@Component
@Order(1)
public class PrimoFiltro implements Filter{

	@Autowired
	BoringLog boringLog;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest=null;
		
		httpRequest= (HttpServletRequest) request;
		HttpServletResponse  myResponse= (HttpServletResponse) response;
		boringLog.debug("Filter: URL"
				+ " called: "+httpRequest.getRequestURL().toString());

		
//		if (httpRequest.getRequestURL().toString().endsWith("/sbagliata"))	{			
//			myResponse.addHeader("PROFE", "FILTERED");						
//			chain.doFilter(httpRequest, myResponse);
//			return;
//		}
//		if (httpRequest.getRequestURL().toString().endsWith("/login"))	{			
//			myResponse.addHeader("PROFE", "REDIRECTED");
//			myResponse.sendRedirect("redirected");
//			chain.doFilter(httpRequest, myResponse);
//			return;
//		}
//                if (httpRequest.getRequestURL().toString().endsWith("/sbagliata"))	{   
//                    myResponse.setStatus(HttpStatus.BAD_GATEWAY.value());
//		    myResponse.getOutputStream().flush();
//		    myResponse.getOutputStream().println("-- Niente da dire --");
//                    return; // non faccio nulla
//                }
//		if (httpRequest.getRequestURL().toString().endsWith("/sbagliata"))	{			
//			myResponse.addHeader("PROFE", "CANCEL");
//			myResponse.setStatus(HttpStatus.BAD_REQUEST.value());
//			myResponse.getOutputStream().flush();
//			myResponse.getOutputStream().println("-- Output filter errore --");
//			chain.doFilter(httpRequest, myResponse);
//			return;
//		}

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
