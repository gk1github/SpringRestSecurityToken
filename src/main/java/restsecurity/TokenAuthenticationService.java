package restsecurity;


//import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class TokenAuthenticationService {

    private static final String AUTH_HEADER_NAME = "Authorization";
    private static final String AUTH_TOKEN_PREFIX = "Bearer ";

    
    public TokenAuthenticationService() {}
    
    public String createJwt(Authentication authentication, String secret) {
    	TokenHandler tokenHandler = new TokenHandler(secret);         
        return tokenHandler.createTokenForUser(authentication.getName());
    }


    public void addAuthentication(HttpServletResponse response, String jwt) {
        response.addHeader(AUTH_HEADER_NAME, AUTH_TOKEN_PREFIX + jwt);
    }


    /*public Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(AUTH_HEADER_NAME);
        if (token != null) {
        	// Strip prefix.
        	token = token.replace(AUTH_TOKEN_PREFIX, "").trim();
            final User user = tokenHandler.parseUserFromToken(token);
            if (user != null) {
                return new Authentication(user);
            }
        }
        return null;
    }*/
	
}