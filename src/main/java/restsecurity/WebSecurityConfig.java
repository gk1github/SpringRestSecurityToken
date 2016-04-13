package restsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  	 	
	@Autowired
	private TokenAuthenticationService tokenAuthenticationService;
	
	
	//**If Spring Security is on the classpath, the Spring Boot automatically secures 
   //all HTTP endpoints with "basic" authentication. 
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		System.out.println(" WebSecurityConfig.configure() called.");
        http        	
        	.sessionManagement()
        	.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and() //does it really need securitycontextholder clearing? its automatic anyway
            .authorizeRequests()            
            .antMatchers(HttpMethod.POST, "/api/login").permitAll()
            .antMatchers("/api/greeting")
            .fullyAuthenticated();
        
        http.anonymous().disable();
        //csrf is stored in the HttpSession and since we want a state-less application, we have to disable this.
        //also consider what happens in a load balanced environment - if we don't enable sticky sessions
        http.csrf().disable();
		
		//SecurityContextPersistenceFilter - so a SecurityContext can be set up in the SecurityContextHolder at the beginning of a web request, 
		//and any changes to the SecurityContext can be copied to the HttpSession when the web request ends (ready for use with the next web request)
		//By this time SecurityContextPersistenceFilter would have completed its job in sthe filter chain
        
        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setAuthenticationManager(authenticationManagerBean());
        loginFilter.setTokenAuthenticationService(tokenAuthenticationService);
		http.addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	System.out.println(" WebSecurityConfig.configureGlobal() called.");
        auth
            .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER").and()
                .withUser("admin").password("password").roles("ADMIN");
    }
    
    @Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}