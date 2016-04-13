package restsecurity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenHandler {
	private final String secret;
    
    public TokenHandler(String secret) {
        this.secret = secret;        
    }

    public User parseUserFromToken(String token) {
        
        // Construct User with roles from token.
        
        // Pull roles from token.
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
               
        String username = claims.getSubject();
        String role = String.valueOf(claims.get("Role"));
        
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(role));
		
		return new User(username, "", authorities);
        
        //return userService.loadUserByUsername(username);
    }

    public String createTokenForUser(String userName)
    {
        long validityInMillis = TimeUnit.HOURS.toMillis(1l);
        Date expiry = new Date(new Date().getTime() + validityInMillis);
        String result = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(userName)
                .claim("Role", "ROLE_USER")
                .setExpiration(expiry)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        return result;
    }    
}
