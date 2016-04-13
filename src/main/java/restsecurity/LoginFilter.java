package restsecurity;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.FilterChain;
//import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * AbstractAuthenticationProcessingFilter
 *     ^
 *     | 
 * CasAuthenticationFilter, OpenIDAuthenticationFilter, UsernamePasswordAuthenticationFilter
 *  
 * a good place to catch specific url paths to do some job
 *  
 * @author gramach
 *
 */
public class LoginFilter extends AbstractAuthenticationProcessingFilter  {

	private TokenAuthenticationService tokenAuthenticationService;
	
	
	public void setTokenAuthenticationService(TokenAuthenticationService tokenAuthenticationService) {
		this.tokenAuthenticationService = tokenAuthenticationService;
	}

	public LoginFilter() {
		//TODO: should this /api/login be hard-coded?
		super(new AntPathRequestMatcher("/api/login"));		
	}
	
	
	/*public LoginFilter(String defaultFilterProcessesUrl) {
		super(new AntPathRequestMatcher(defaultFilterProcessesUrl));
		// TODO Auto-generated constructor stub
		setAuthenticationManager(myAuthenticationManager);
	}*/
	

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {		
		logRequestDetails(request);
		final CustomUser user = new ObjectMapper().readValue(request.getInputStream(), CustomUser.class);
		final UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(
				user.getUsername(), user.getPassword());		
		return getAuthenticationManager().authenticate(loginToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			FilterChain chain, Authentication authentication) throws IOException, ServletException {
		
		// Lookup the complete User object from the database and create an Authentication for it
		//UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
				
		// Add the custom token as HTTP header to the response
		String jwt = tokenAuthenticationService.createJwt(authentication, "9SyECk96oDsTmXfogIieDI0cD/8FpnojlYSUJT5U9I/FGVmBz5oskmjOR8cbXTvoPjX+Pq/T/b1PqpHX0lYm0oCBjXWICA==");
		System.out.println(jwt);
		tokenAuthenticationService.addAuthentication(response, jwt);		
		// Add the authentication to the Security context
		SecurityContextHolder.getContext().setAuthentication(authentication);		
	}
	
	

	private void logRequestDetails(HttpServletRequest request) {
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("++                     Request Details                           ++");
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("Method: " + request.getMethod());
		System.out.println("User: " + (request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "No User Principal"));
		System.out.println("User In Role B_W_Client1095BSearch_U ? " + request.isUserInRole("B_W_Client1095BSearch_U"));
		System.out.println("request.getUserPrincipal().getClass():" + request.getUserPrincipal()); 
		System.out.println("Auth Type: " + (request.getAuthType() != null ? request.getAuthType() : "No Auth Type"));
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

		System.out.println("");

		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("++                         Attributes                            ++");
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		Enumeration<String> enNames = request.getAttributeNames();
		while (enNames.hasMoreElements()) {
			String param = enNames.nextElement();

			System.out.println(param + " - " + request.getAttribute(param));
		}
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

		System.out.println("");

		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("++                          Headers                              ++");
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			String headerValue = request.getHeader(headerName);

			System.out.println(headerName + " - " + headerValue);
		}
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

		System.out.println("");

		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("++                          Cookies                              ++");
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

		if (request.getCookies() != null) {
			int i = 1;
			for (Cookie cookie : request.getCookies()) {
				System.out.println("Cookie #" + i);
				System.out.println("Domain: " + cookie.getDomain());
				System.out.println("Name: " + cookie.getName());
				System.out.println("Value: " + cookie.getValue());
				System.out.println("Path: " + cookie.getPath());
				System.out.println("");
				i++;
			}
		}

		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	}
	
	
}
