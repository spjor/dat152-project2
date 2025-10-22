/**
 * 
 */
package no.hvl.dat152.rest.ws.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * @author tdoy
 */
@Component
public class AuthTokenFilter extends OncePerRequestFilter {
	
	@Autowired
	private UserRepository userRepository;
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenFilter.class);
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		JwtAuthenticationToken oauthJwtToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		Jwt jwt = oauthJwtToken.getToken();
		
		try {
			if(jwt != null) {
				
				// update security context with the userDetail object
				UserDetailsImpl userDetails = getUserDetails(jwt, oauthJwtToken);
				
				oauthJwtToken.setDetails(userDetails);		// this is a hack!!! - using setDetails(Object) to encapsulate userDetails
				
				SecurityContextHolder.getContext().setAuthentication(oauthJwtToken);
				
			}
		}catch(Exception e) {
			LOGGER.error("Failed to update token authentication with user details: {}", e);
		}
		
		filterChain.doFilter(request, response);
	}
	
	private UserDetailsImpl getUserDetails(Jwt jwt, JwtAuthenticationToken oauthJwtToken) throws UserNotFoundException {
		
		User user = new User();
		
		String email = jwt.getClaimAsString("email");				// keycloak attribute name (claim)
		String firstname = jwt.getClaimAsString("given_name");		// keycloak attribute name (claim)
		String lastname = jwt.getClaimAsString("family_name");		// keycloak attribute name (claim)
		
		user.setEmail(email);
		user.setFirstname(firstname);
		user.setLastname(lastname);	
		
		// create user in the library database if it does not exist
		Optional<User> libuser = userRepository.findByEmail(email);
		
		User user1 = libuser.orElse(null);
		
		if(libuser.isEmpty()) {
			user1 = userRepository.save(user);
			System.out.println(user1);
		} else {
			user.setUserid(user1.getUserid());		
		}
		
		
		return UserDetailsImpl.build(user, oauthJwtToken.getAuthorities());
		
	}

}
